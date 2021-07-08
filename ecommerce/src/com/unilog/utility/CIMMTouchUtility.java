package com.unilog.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.ConversionTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.view.tools.LinkTool;
import org.jdom.input.SAXBuilder;
import org.jsoup.parser.Parser;

import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.misc.MenuAndBannersDAO;
import com.unilog.misc.MenuAndBannersModal;
import com.unilog.security.SecureData;
import com.unilog.users.UsersDAO;
import com.unilog.utility.model.LocaleModel;
import com.unilog.velocitytool.CIMM2VelocityTool;

public class CIMMTouchUtility {
	private static CIMMTouchUtility cimmTouchUtility = null;

	private CIMMTouchUtility() {

	}
	public static CIMMTouchUtility getInstance() {
		synchronized (CIMMTouchUtility.class) {
			if (cimmTouchUtility == null) {
				cimmTouchUtility = new CIMMTouchUtility();
			}
		}
		return cimmTouchUtility;
	}
	public LinkedHashMap<String, Object> getStaticPageById(String pageId, String pageName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String headerHtml = null;
		String footerHtml = null;
		String fullPageLayout = "";
		LinkedHashMap<String, Object> contentObject = new LinkedHashMap<>();
		ArrayList<MenuAndBannersModal> subMenuList = null;

		String sql = null;
		try {
			if (pageId != null) {
				sql = "SELECT page_name, static_page_id,header,footer,FULL_PAGE_LAYOUT FROM STATIC_PAGES WHERE static_page_id  = ?";
			} else if (pageName != null) {
				sql = "SELECT page_name, static_page_id,header,footer,FULL_PAGE_LAYOUT FROM STATIC_PAGES WHERE UPPER(page_name)  = UPPER(?)";
				pageName = pageName.replace("--", "-");
				pageName = pageName.replace("-", " ");
			}
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			if (pageId != null) {
				pstmt.setInt(1, CommonUtility.validateNumber(pageId));
			} else if (pageName != null) {
				pstmt.setString(1, pageName);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pageId = rs.getString("STATIC_PAGE_ID");
				if (rs.getString("HEADER") != null && !rs.getString("HEADER").trim().equalsIgnoreCase("HEADER")) {
					headerHtml = rs.getString("HEADER");
				}

				if (rs.getString("FOOTER") != null && !rs.getString("FOOTER").trim().equalsIgnoreCase("FOOTER")) {
					footerHtml = rs.getString("FOOTER");
				}
				fullPageLayout = rs.getString("FULL_PAGE_LAYOUT");
			}

			boolean getParentMenu = true;
			subMenuList = new ArrayList<>();
			int staticLinkId = 0;

			if (MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(pageId)) != null) {
				staticLinkId = MenuAndBannersDAO.getStaticLinkId().get(CommonUtility.validateNumber(pageId));
				if (staticLinkId > 0) {
					if (MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId) != null) {
						subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticLinkId);
						getParentMenu = false;
					}
				}
			}
			if (getParentMenu) {
				int staticParentId = 0;
				if (MenuAndBannersDAO.getStaticLinkParentId().get(CommonUtility.validateNumber(pageId)) != null) {
					staticParentId = MenuAndBannersDAO.getStaticLinkParentId()
							.get(CommonUtility.validateNumber(pageId));
					if (staticParentId > 0 && MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId) != null) {

						subMenuList = MenuAndBannersDAO.getStaticSubmenuList().get(staticParentId);

					}
				}

			}
			getXmlContent(pageId, contentObject);
			contentObject.put("pageTypeReq", "staticPage");
			contentObject.put("ID", pageId);
			contentObject.put("subMenuList", subMenuList);
			if (headerHtml != null && !headerHtml.isEmpty()) {
				contentObject.put("headerHtml", headerHtml);
			}
			if (footerHtml != null && !footerHtml.isEmpty()) {
				contentObject.put("footerHtml", footerHtml);
			}
			if (CommonUtility.validateString(fullPageLayout).length() > 0) {
				contentObject.put("fullPageLayout", fullPageLayout);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBConnection(conn);

		}
		return contentObject;
	}

	public CIMMTouchUtilityModel getBlogEntry(int dataId, String entityType) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CIMMTouchUtilityModel blogEntry = null;
		try {

			String sql = "SELECT BEL.*,REPLACE(EXTERNAL_SYS_URL,'roller-ui/') BLOGURL FROM BLOG_ENTRY_LINK BEL,(SELECT * FROM EXTERNAL_SYS_INFO WHERE EXTERNAL_SYS_NAME='$ROLLER_BLOG') WHERE ENTITY_TYPE=? AND ENTITY_ID = ?";
			conn = ConnectionManager.getDBConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, entityType);
			pstmt.setInt(2, dataId);
			rs = pstmt.executeQuery();
			blogEntry = new CIMMTouchUtilityModel();
			if (rs.next()) {
				blogEntry.setBlogId(rs.getString("BLOG_ID"));
				blogEntry.setBlogEntryId(rs.getString("BLOG_ENTRY_ID"));
				blogEntry.setBlogEntryAvailable(true);
			} else {
				blogEntry.setBlogEntryAvailable(false);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.closeDBPreparedStatement(pstmt);
			ConnectionManager.closeDBResultSet(rs);
			ConnectionManager.closeDBConnection(conn);

		}
		return blogEntry;
	}

	public void getXmlContent(String pageId, LinkedHashMap<String, Object> contentObject) {
		String staticPagePath = CommonDBQuery.getSystemParamtersList().get("STATICPAGESPATH");
		String pageName = null ;
		String pageTitle = null ;
		String pageContent = null ;
		String metaKeywords = null ;
		String metaDesc = null;
		boolean foundPage = false;
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String tempSubset = (String) session.getAttribute("userSubsetId");
			String tempGeneralSubset = (String) session.getAttribute("generalCatalog");
			int generalSubset = CommonUtility.validateNumber(tempGeneralSubset);
			int subsetId = CommonUtility.validateNumber(tempSubset);
			String staticPageFullPath = staticPagePath + pageId + ".xml";


			SAXBuilder builder = new SAXBuilder();
			File file = new File(staticPageFullPath);
			if (!file.exists()) {
				if (CommonUtility.validateString(pageId).length() > 0 && !CommonUtility.validateString(pageId).equalsIgnoreCase("NULL") && session.getAttribute("localeCode") != null && CommonUtility.validateString(session.getAttribute("localeCode").toString()).length() > 0) {
					LinkedHashMap<String, LocaleModel> localeList = CommonDBQuery.getLocaleList();
					if (localeList != null && !localeList.isEmpty()) {
						LocaleModel localeModel = localeList.get(CommonUtility.validateString(session.getAttribute("localeCode").toString()));
						if (localeModel != null && localeModel.getLocaleId() > 0) {
							File fileCheck = new File(staticPagePath + pageId + "_" + localeModel.getLocaleId() + ".xml");
							if (fileCheck.exists()) {
								file = new File(staticPagePath + pageId + "_" + localeModel.getLocaleId() + ".xml");
								foundPage = true;
							}
						}
					}
				} else {
					file = new File(staticPagePath + CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("PAGE_NOT_FOUND_STATIC_PAGE_ID"))+ ".xml");
					foundPage = true;
				}
			} else if (file.exists()) {
				foundPage = true;
			}
			if(foundPage) {
				org.jdom.Document dom = builder.build(file);
				pageName = dom.getRootElement().getChildText("pageName");
				pageTitle = dom.getRootElement().getChildText("pageTitle");
				pageContent = dom.getRootElement().getChildText("pageContent");
				metaKeywords = dom.getRootElement().getChildText("metaKeywords");
				metaDesc = dom.getRootElement().getChildText("metaDesc");
			}
			
			if (foundPage) {
				String fullPageLayout = UsersDAO.getLayoutType(pageId);
				ToolManager velocityToolManager = new ToolManager();
				velocityToolManager.configure("velocity-tools.xml");
				VelocityContext context = new VelocityContext(velocityToolManager.createContext());
				VelocityEngine velocityTemplateEngine = new VelocityEngine();
				String templatePath = CommonDBQuery.getSystemParamtersList().get("SITE_TEMPLATE_PATH")+ CommonDBQuery.getSystemParamtersList().get("SITE_NAME")+ CommonDBQuery.getSystemParamtersList().get("MACROS_FOLDER");
				velocityTemplateEngine.setProperty("file.resource.loader.path", templatePath);
				velocityTemplateEngine.setProperty("velocimacro.library", "defaultMacro.vm");
				velocityTemplateEngine.init();
				String pricePrecision = "2";
				String pricePrecisionFormate = "#0.00";
				String protocol = request.getScheme();
				if (session != null && session.getAttribute("pricePrecision") != null
						&& CommonUtility.validateString((String) session.getAttribute("pricePrecision")).length() > 0) {
					pricePrecision = CommonUtility.validateString((String) session.getAttribute("pricePrecision"));
				} else if (CommonUtility
						.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"))
						.length() > 0) {
					pricePrecision = CommonUtility
							.validatePricePrecisionString(CommonDBQuery.getSystemParamtersList().get("PricePrecision"));
				} else {
					pricePrecision = "2";
				}
				if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("5")) {
					pricePrecisionFormate = "#0.00000";
				} else if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("4")) {
					pricePrecisionFormate = "#0.0000";
				} else if (CommonUtility.validateString(pricePrecision).equalsIgnoreCase("3")) {
					pricePrecisionFormate = "#0.000";
				} else {
					pricePrecisionFormate = "#0.00";
				}
				context.put("numberTool", new NumberTool());
				context.put("escapeTool", new EscapeTool());
				context.put("math", new MathTool());
				context.put("dispalyTool", new DisplayTool());
				context.put("convert", new ConversionTool());
				context.put("dateTool", new ComparisonDateTool());
				context.put(Integer.class.getSimpleName(), Integer.class);
				context.put("session", session);
				context.put("CIMMUtility", CIMM2VelocityTool.getInstance());
				context.put("linkTool", new LinkTool());
				context.put("sortTool", new SortTool());
				context.put("CommonDBQuerySystemParameter", CommonDBQuery.getSystemParamtersList());
				context.put("request", request);
				context.put("hostName", protocol + "://" + request.getServerName() + "/");
				context.put("moodname", CommonDBQuery.getSystemParamtersList().get("MOOD_NAME"));
				if (session.getAttribute("localeCode") != null) {
					context.put("locale", LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()));
					context.put("localeLang", (String) session.getAttribute("sessionLocale"));
				} else {
					context.put("locale", LayoutLoader.getMessageProperties().get("EN"));
					context.put("localeLang", "1_en");
					session.setAttribute("localeCode", "EN");
					session.setAttribute("sessionLocale", "1_en");
				}
				String refreshVersion = "rv="+session.getId();
				context.put("refreshVersion",CommonUtility.validateString(refreshVersion));
				context.put("webThemes", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("WEB_THEMES")));
				context.put("siteName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_NAME")));
				context.put("siteId", CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("SITE_ID")));
				context.put("siteDisplayName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SITE_DISPLAY_NAME")));
				context.put("internationalUser", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENABLE_INTERNATIONAL_USER")));
				context.put("eclipseIsDownMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEISDOWNMESSAGE")));
				context.put("eclipseDownCartMessage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ECLIPSEDOWNCARTMESSAGE")));
				context.put("POValidStatus",CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("IS_PO_MANDATORY")));
				context.put("thumbNail",CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("THUMBNAIL")));
				context.put("itemImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ITEMIMAGE")));
				context.put("detailImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DETAILIMAGE")));
				context.put("enlargedImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("ENLARGEIMAGE")));
				context.put("taxonomyImage", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("TAXONOMYIMAGEPATH")));
				context.put("documentPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("DOCUMENTS")));
				context.put("brandLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BRANDLOGO")));
				context.put("buyingCompanyLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BUYINGCOMPANYLOGO")));
				context.put("bannerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("BANNERLOGO")));
				context.put("userProfileImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_IMAGE_PATH")));
				context.put("userProfileThumbImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("USER_PROFILE_THUMB_IMAGE_PATH")));
				context.put("manufacturerLogoPath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("MNF_IMAGES")));
				context.put("checkCVVandCardHolderName", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("CHECK_CVV_AND_CARDHOLDER_NAME")));
				context.put("attributeSwatchImagePath", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SWATCH")));
				context.put("pricePrecision", CommonUtility.validateString(pricePrecision));
				context.put("pricePrecisionFormate", CommonUtility.validateString(pricePrecisionFormate));
				context.put("generalSubset", generalSubset);
				context.put("subsetId", subsetId);
				if (session.getAttribute("userLogin") != null) {
					context.put("userLogin", (Boolean) session.getAttribute("userLogin"));
				}
				if (session.getAttribute("userEmailAddress") != null) {
					context.put("userEmailAddress", CommonUtility.validateString((String) session.getAttribute("userEmailAddress")));
				}
				StringWriter writer = new StringWriter();
				velocityTemplateEngine.evaluate(context, writer, "", pageContent);
				StringBuilder finalMessage = new StringBuilder();
				finalMessage.append(writer.toString());
				pageContent = finalMessage.toString();
				contentObject.put("pageContent", pageContent);
				contentObject.put("fullPageLayout", fullPageLayout);
				contentObject.put("pageName", pageName);
				contentObject.put("pageTitle", pageTitle);
				contentObject.put("metaKeywords", metaKeywords);
				contentObject.put("metaTagString", metaKeywords);
				contentObject.put("metaDescription", metaDesc);
				contentObject.put("metaDesc", metaDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public StringBuilder getFileContent(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
			try {
				File fileCheck = new File(filePath);
				if (fileCheck.exists()) {
					System.out.println("File exist at getFileContent: "+filePath);
					 BufferedReader in = new BufferedReader(new FileReader(filePath));
					    String str;
					    while ((str = in.readLine()) != null) {
					        contentBuilder.append(Parser.unescapeEntities(str, false));
					    }
					    in.close();
				}else {
					System.out.println("File do not exist at getFileContent: "+filePath);
					contentBuilder = null;
				}
			   
			} catch (IOException e) {
				contentBuilder = null;
				e.printStackTrace();
			}
			return contentBuilder;
	}

	public boolean generateAndSendEmail(String fromEmail, String toEmail, String mailFrom, String subject, String body,
			String imagePath) {
		boolean mailSent = true;
		try {
			SecureData validUserPass = new SecureData();
			String mailArr[] = toEmail.split(",");
			String emailRelayEncrpt = CommonDBQuery.getSystemParamtersList().get("EMAILRELAY");
			int emailRelayPort = CommonUtility
					.validateNumber(CommonDBQuery.getSystemParamtersList().get("MAILSMTPPORT"));

			String emailRelay = validUserPass.validatePassword(emailRelayEncrpt);
			Properties props = new Properties();

			props.put("mail.smtp.host", emailRelay);

			if (emailRelayPort == 0) {
				emailRelayPort = 25;
			}

			Address[] toAddress = null;
			int loop = 0;
			toAddress = new Address[mailArr.length];
			for (String toAddr : mailArr) {
				toAddress[loop] = new InternetAddress(toAddr.trim());
				loop++;
			}
			props.put("mail.smtp.port", emailRelayPort);
			props.put("mail.smtp.from", fromEmail);
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.starttls.enable", "false");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.socketFactory.port", emailRelayPort);
			props.put("mail.smtp.socketFactory.fallback", "false");
			if(CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")).length()>0) {
                props.put("mail.smtp.ssl.trust", CommonUtility.validateString(CommonDBQuery.getSystemParamtersList().get("SMTP_TRUSTEDHOST")));
        	}
			Session session = Session.getInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
			message.setFrom(new InternetAddress(fromEmail));
			message.setSubject(subject, "UTF-8");
			message.setSentDate(new Date());
			message.addRecipients(Message.RecipientType.TO, toAddress);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(imagePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(new File(imagePath).getName());
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<br><h3>Find attached image</h3>" + "<img src='cid:image_id'>", "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			mailSent = true;
			System.out.println("Email Sent Successfully With Image Attachment");
			System.out.println("Mail send ends..");
		} catch (MessagingException e) {
			mailSent = false;
			e.printStackTrace();
		} catch (Exception e) {
			mailSent = false;
			e.printStackTrace();
		}
		return mailSent;
	}

}
