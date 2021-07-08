package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.JsonObject;
import com.oreilly.servlet.MultipartRequest;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.defaults.Global;
import com.unilog.products.ProductsModel;
import com.unilog.utility.CommonUtility;


public class ReadPartListExcelFileRFQ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int lastRowNum = 0;
	int columnCnt = 0;
	public ReadPartListExcelFileRFQ() {
        super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String locale = session.getAttribute("localeCode").toString().toUpperCase();
		String invalidFileError = "";
		int numberOfColumns = Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_COLUMNS_IN_RFQ"));
		int numberOfRecords = Integer.parseInt(CommonDBQuery.getSystemParamtersList().get("NUMBER_OF_RECORDS_IN_RFQ"));
		if(numberOfRecords<1){
			numberOfRecords = 100;
		}
		boolean recordLimitCrossed = false;
	    String browserType=(String)request.getHeader("User-Agent");
	    if(browserType!=null && browserType.trim().equalsIgnoreCase("")){
			if(browserType.indexOf("MSIE") != -1 || browserType.indexOf("MSIE 10") != -1||browserType.indexOf("MSIE 9") != -1||browserType.indexOf("MSIE 8")!=-1||browserType.indexOf("Trident")!=-1)
			{
				response.setContentType("text/plain");
			}else{
				response.setContentType("application/json");
			}
		}else{
			response.setContentType("application/json");
		}
	    MultipartRequest multi = new MultipartRequest(request, ".", 1024 * 1024 * 1024);
		Enumeration files = multi.getFileNames();
		String savedFileName = null;
		String fileName = "";
		String filePathToDelete = null;
		String sessionId = session.getId();
		String heading="";
		JsonObject innerObject =null;
		JsonObject jsonObject = new JsonObject();
		boolean flag = false;
		boolean invalidFile = false;
		boolean headerMissMatch = false;
		boolean manditoryFieldEmpty = false;
		boolean descEmpty = false;
		boolean brandEmpty = false;
		boolean MpnEmpty = false;
		boolean rowAlert = false;
		int quantity=0;
		try{
		while(files.hasMoreElements()) {
			String name = (String) files.nextElement();
			File fileToUpload = multi.getFile(name);
			fileName = multi.getFilesystemName(name);
			savedFileName = sessionId+"_"+fileName  ;
			String folder = CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH");
			String saveFile = CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")+"/"+savedFileName;
			filePathToDelete =  CommonDBQuery.getSystemParamtersList().get("TEMPUPLOADDIRECTORYPATH")+"/"+savedFileName;
			String path = folder.trim();
			File destination = new File(path);

			if(!destination.exists()){
				System.out.println(path+" : destination dir doesnt exists");
				destination.mkdir();
			}
			if(copyFile(fileToUpload, new File(saveFile), session) == true){
			        try{
			        	Workbook workbook = null;
			        	FileInputStream inp=new FileInputStream(saveFile);
			        	workbook = WorkbookFactory.create(inp);
						Sheet sheet = workbook.getSheetAt(0);
						Row headerRow = sheet.getRow(0);
						lastRowNum = sheet.getLastRowNum();
						columnCnt = headerRow.getLastCellNum();
						session.setAttribute("rowsCount", lastRowNum);
						if(lastRowNum > 0 && lastRowNum<=numberOfRecords && columnCnt==numberOfColumns){
							for (int rowNum = 0; rowNum <=lastRowNum; rowNum++){
								Row row = sheet.getRow(rowNum);
								if(row==null || row.getPhysicalNumberOfCells()<=0)
								{
									break;
								}
								innerObject = new JsonObject();
								boolean breakPoint = false;
								for(int celNum=0; celNum<headerRow.getLastCellNum(); celNum++){
									Cell cell = row.getCell(celNum);
									String xlValue="";
									if(cell==null || cell.getCellType()==Cell.CELL_TYPE_BLANK)
										xlValue="";
									else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									{
										xlValue = cell.getStringCellValue().trim();
									}
									else if (cell!=null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									{
										int cellValueReadNumeric = (int) cell.getNumericCellValue();
										xlValue = Integer.toString(cellValueReadNumeric);
									}
									if(xlValue!=null){
										xlValue =xlValue.replaceAll("&", " and ");
										//xlValue= xlValue.replaceAll("[\\p{P}\\p{S}]*","");	
									System.out.println(xlValue);
									if(rowNum==0){
		                            	if(CommonUtility.validateString(heading).length() == 0){
		                            		
		                            		heading = xlValue;
		                            	}else{
		                            		heading = heading+"|"+xlValue;
		                            	}
		                            	heading = heading.replace(" ","_");
			                    	}else{ 
			                    		String[] headingArray = null;
			                    		if(heading!=null)
			                    			headingArray= heading.split("\\|");
			                    		if(headingArray!=null && headingArray.length>celNum && headingArray[celNum]!=null && !headingArray[celNum].trim().equalsIgnoreCase("")){
			                    			if(headingArray[celNum].trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.quantity").trim().replace(" ","_"))){
			                    				quantity = CommonUtility.validateNumber(xlValue);
												if(quantity == 0){
													innerObject.addProperty(headingArray[celNum], "1");
												}else {
													innerObject.addProperty(headingArray[celNum], xlValue);
												}
			                    			}else if(headingArray[celNum].trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.description").trim().replace(" ","_"))){
			                    				if(CommonUtility.validateString(xlValue).length()<1 || CommonUtility.validateString(xlValue).equalsIgnoreCase(" ")) {
			                    					manditoryFieldEmpty=true;
			                    					descEmpty=true;
			                    					innerObject.addProperty(headingArray[celNum], " ");
			                    				}else {
			                    					innerObject.addProperty(headingArray[celNum], xlValue);
			                    				}
			                    			}
			                    			else if(headingArray[celNum].trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.brandname").trim().replace(" ","_"))){
			                    				if(CommonUtility.validateString(xlValue).length()<1 || CommonUtility.validateString(xlValue).equalsIgnoreCase(" ")) {
			                    					manditoryFieldEmpty=true;
			                    					brandEmpty=true;
			                    					innerObject.addProperty(headingArray[celNum], " ");
			                    				}else {
			                    					innerObject.addProperty(headingArray[celNum], xlValue);
			                    				}
			                    			}
			                    			else if(headingArray[celNum].trim().equalsIgnoreCase(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.mfrpartnumber").trim().replace(" ","_"))){
			                    				if(CommonUtility.validateString(xlValue).length()<1 || CommonUtility.validateString(xlValue).equalsIgnoreCase(" ")) {
			                    					manditoryFieldEmpty=true;
			                    					MpnEmpty = true;
			                    					innerObject.addProperty(headingArray[celNum], " ");
			                    				}else {
			                    					innerObject.addProperty(headingArray[celNum], xlValue);
			                    				}
			                    			}
			                    			else{
			                    				innerObject.addProperty(headingArray[celNum], xlValue);
			                    				System.out.println(xlValue);
			                    				headerMissMatch=true;
				                    			//String results = ProductsAction.SearchProductForQuickCart(session,headingArray[celNum], xlValue,defaultShiptoId,subsetId,generalSubset,session.getId(),buyingCompanyId,entityId,userId,(String) session.getAttribute("userToken"),(String)session.getAttribute(Global.USERNAME_KEY));
				                    			/*if(results!=null && !results.trim().equalsIgnoreCase("")){
				                    				JsonElement o = new JsonParser().parse(results);
					                    			innerObject.add("Result",o);
					                    			innerObject.addProperty("ResultCount",(String)session.getAttribute("resultValQuickCart"));
				                    			}*/
				                    		}
			                    			
			                    		}
			                    	} 
								}
									
								if(rowNum!=0){
									jsonObject.add("Items"+rowNum, innerObject);
		                        	if(breakPoint){
		                        		break;
		                        	}
		                        }else{
		                        	innerObject.addProperty("rowsCount", lastRowNum);
		                        	jsonObject.add("Items"+rowNum, innerObject);
		                        }
								}
								if(descEmpty && brandEmpty && MpnEmpty) {
									rowAlert = true;
								}else {
									descEmpty=false;
									brandEmpty=false;
									MpnEmpty=false;
								}
							}
						}else{
							invalidFile = true;
							if(lastRowNum<1){
								invalidFileError = "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.empty");							
							}else if(lastRowNum>numberOfRecords){
								recordLimitCrossed = true;
								invalidFileError = "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.rowlimit")+numberOfRecords+"   /   "+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.rowAlert");
							}else if(columnCnt!=numberOfColumns){
								recordLimitCrossed = true;
								invalidFileError = "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.columnlimit")+numberOfColumns;
							}
						}
			        }catch(Exception exec){
			        	exec.printStackTrace();
			        }
			}
		}
		
		if(recordLimitCrossed){
			invalidFile = true;
		}
		
		/* empty cells in deleted or edited sheet pop up  */
		if(rowAlert) {
			sendErrorResponse(response, "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.rowAlert"));
		}else if(invalidFile){
			sendErrorResponse(response, invalidFileError);
		}else if(headerMissMatch){
			sendErrorResponse(response, "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.headingMissmatch"));
		}else if(manditoryFieldEmpty){
			sendErrorResponse(response, "0|"+LayoutLoader.getMessageProperties().get(locale).getProperty("rfq.label.fileupload.manditoryFields"));
		}else if(jsonObject!=null){
			System.out.println(jsonObject.toString());
			if(lastRowNum<=numberOfRecords){
			sendResponse(response, jsonObject.toString()+"|"+lastRowNum,jsonObject);
		}
		}
		
		if(filePathToDelete != null)
		deletefile(filePathToDelete);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	private void sendResponse(HttpServletResponse response, String excelPartListJson,JsonObject jsonObject) {
		 PrintWriter out= null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		out.print(jsonObject);
	}
	
	private void sendErrorResponse(HttpServletResponse response, String errorMessage) {
		PrintWriter out= null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(CommonUtility.validateString(errorMessage).length()>0){
			out.print(errorMessage);
		}else{
			out.print("0|Error while processing the file");
		}
		
	}
	private boolean copyFile(File from, File to, HttpSession session) {
		try {
			FileInputStream fileInStream = new FileInputStream(from);
			FileOutputStream fileOutStream = new FileOutputStream(to);
			boolean emptyFile = true;
			byte buf[] = new byte[1024];
			int i = 0;
			
			while((i = fileInStream.read(buf)) != -1) {
				emptyFile = false;
				fileOutStream.write(buf, 0, i);
			}
			fileInStream.close();
			fileOutStream.close();
			if(emptyFile == true) {
				to.delete();
				return false;
			}
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void deletefile(String file){
		  File f1 = new File(file);
		  boolean success = f1.delete();
		  if (!success){
			  System.out.println("PartList Excel Deletion failed.");
		  }else{
			  System.out.println("PartList Excel File deleted.");
		  }
	}

}
