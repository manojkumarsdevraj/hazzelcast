package com.unilog.servlets;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.unilog.cmsmanagementdao.WebLiveDAO;
import com.unilog.database.CommonDBQuery;
import com.unilog.utility.CommonUtility;

/**
 * Servlet implementation class ConnectorServlet
 */

public class ConnectorServlet extends HttpServlet
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1740441653986821988L;
private static String baseDir;
  private static boolean debug = false;
  private int siteId;
  public void init()
    throws ServletException
  {
    baseDir = getInitParameter("baseDir");
    debug = new Boolean(getInitParameter("debug")).booleanValue();
    if (baseDir == null)
      baseDir = "/UserFiles/";
    String realBaseDir = baseDir;
    File baseFile = new File(realBaseDir);
    if (!baseFile.exists())
      baseFile.mkdir();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  try{
		 System.out.println("Path : " + CommonDBQuery.getSystemParamtersList().get("STATIC_IMAGES")); 
		 System.out.println("VIRTUALFOLDERPATH : " + CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH")); 
		 
		  HttpSession session = request.getSession();	
	
			  if (debug) System.out.println("--- BEGIN DOGET ---");

			    response.setContentType("text/xml; charset=UTF-8");
			    response.setHeader("Cache-Control", "no-cache");
			    PrintWriter out = response.getWriter();

			    String commandStr = request.getParameter("Command");
			    String typeStr = request.getParameter("Type");
			    String currentFolderStr = request.getParameter("CurrentFolder");
			   
			    if(typeStr==null){
			    	typeStr = "";
			    }
			    if(typeStr.trim().equalsIgnoreCase(""))
			    	typeStr = "STATIC_IMAGES";
			    String virutalPath = CommonDBQuery.getSystemParamtersList().get(typeStr);
			    String warFolderName = getWarFileName(virutalPath);

			baseDir =  CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH")+warFolderName;
			System.out.println(baseDir);
			    String currentPath = baseDir  + currentFolderStr;
			    String currentDirPath = currentPath;

			    File currentDir = new File(currentDirPath);
			    if (!currentDir.exists()) {
			      currentDir.mkdir();
			    }

			    Document document = null;
			    try {
			      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			      DocumentBuilder builder = factory.newDocumentBuilder();
			      document = builder.newDocument();
			    } catch (ParserConfigurationException pce) {
			      pce.printStackTrace();
			    }

			    Node root = CreateCommonXml(document, commandStr, virutalPath, currentFolderStr, request.getContextPath() + currentPath);

			    if (debug) System.out.println("Command = " + commandStr);

			    if (commandStr.equals("GetFolders")) {
			      getFolders(currentDir, root, document);
			    }
			    else if (commandStr.equals("GetFoldersAndFiles")) {
			      getFolders(currentDir, root, document);
			      getFiles(currentDir, root, document);
			    }
			    else if (commandStr.equals("CreateFolder")) {
			      String newFolderStr = request.getParameter("NewFolderName");
			      File newFolder = new File(currentDir, newFolderStr);
			      String retValue = "110";

			      if (newFolder.exists())
			        retValue = "101";
			      else {
			        try
			        {
			          boolean dirCreated = newFolder.mkdir();
			          if (dirCreated)
			            retValue = "0";
			          else
			            retValue = "102";
			        } catch (SecurityException sex) {
			          retValue = "103";
			        }
			      }

			      setCreateFolderResponse(retValue, root, document);
			    }

			    document.getDocumentElement().normalize();
			    try {
			      TransformerFactory tFactory = TransformerFactory.newInstance();
			      Transformer transformer = tFactory.newTransformer();

			      DOMSource source = new DOMSource(document);

			      StreamResult result = new StreamResult(out);
			      transformer.transform(source, result);

			      if (debug) {
			        StreamResult dbgResult = new StreamResult(System.out);
			        transformer.transform(source, dbgResult);
			        System.out.println("");
			        System.out.println("--- END DOGET ---");
			      }
			    }
			    catch (Exception ex)
			    {
			      ex.printStackTrace();
			    }

			    out.flush();
			    out.close(); 
		 
	  }catch (Exception e) {
		
		 e.printStackTrace();
	} 
	  
    
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    

    response.setContentType("application/json");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader ("Expires", -1);
    PrintWriter out = response.getWriter();

    String commandStr = request.getParameter("Command");
    String typeStr = request.getParameter("Type");
    String currentFolderStr = request.getParameter("CurrentFolder");
    String overWriteImage = request.getParameter("overwriteImage");
    System.out.println("OverWrite Image Option: "+overWriteImage);
    String[] filesList=request.getParameterValues("imageList[]"); 
	System.out.println("checked images info"+filesList);
	String selectedType=request.getParameter("selectedType");
	System.out.println("selectedType:"+selectedType);
	String deleteFilesList = request.getParameter("spDeletedList");
	System.out.println("deleteFilesList:"+deleteFilesList);  
    String reqType = "";
    String itemIdStr = "";
    int itemId = 0;   
    String  taxTreeIdIdStr="";
    int taxTreeIdId=0;
    String saveCategImg="";
    String  attrIdStr="";
    int attributeId=0;
    String saveAttrImg="";
    String  itemAttrValueTableIdstr="";
    long itemAttrValueTableId=0;
    String retVal = "0";
    String newName = "",bannerUpdate="";
    List<String> notRemovedFileNameList;
    int userId = 1;
    bannerUpdate=(String)request.getSession().getAttribute("updateBanner");
    String StaticFilesRemove="";
    
    try {
    	System.out.println("Path : " + CommonDBQuery.getSystemParamtersList().get("STATIC_IMAGES")); 
		 System.out.println("VIRTUALFOLDERPATH : " + CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH")); 
		 
		  HttpSession session = request.getSession();		
		 
		  if(CommonDBQuery.getGlobalSiteId()>0 && siteId == 0){
      		siteId = CommonDBQuery.getGlobalSiteId();
  		   }
		  WebLiveDAO webLive= new WebLiveDAO();
			  if(filesList!=null && "fileRemoveFromServer".equalsIgnoreCase(request.getParameter("fileRemove"))){
				    String currentDirPath = baseDir;
				    notRemovedFileNameList=webLive.getStaticFilesInfo(userId,siteId,filesList,currentDirPath,selectedType);
				    String notRemovedFileNameListString = "";
				    String separator = "";
				    for (String s : notRemovedFileNameList){
				    	notRemovedFileNameListString = notRemovedFileNameListString+separator+s;
				    	separator = ",";
				    }
				    response.setContentType("text/html");
				    response.setCharacterEncoding("UTF-8");
				    response.getWriter().print(notRemovedFileNameListString);
			  } else  if(CommonUtility.validateString(deleteFilesList).length()>0 && "fileRemoveFromSP".equalsIgnoreCase(request.getParameter("fileRemoveFromSP"))){
				    String currentDirPath = baseDir;
				    StaticFilesRemove=webLive.deleteStaticFiles(userId,siteId,CommonUtility.validateString(deleteFilesList).split(","),currentDirPath,selectedType);
				    response.setContentType("text/html");
				    response.setCharacterEncoding("UTF-8");
				    response.getWriter().print(StaticFilesRemove);
			  } else {
				  
			    boolean imageExist = false;
		    	 DiskFileUpload upload = new DiskFileUpload();
		         
		         List items = upload.parseRequest(request);

		         Map fields = new HashMap();

		         Iterator iter = items.iterator();
		         while (iter.hasNext()) {
		           FileItem item = (FileItem)iter.next();
		           if (item.isFormField())
		             fields.put(item.getFieldName(), item.getString());
		           else
		             fields.put(item.getFieldName(), item);
		         }
		         if(overWriteImage ==null){
		        	 overWriteImage = (String) fields.get("overwriteImage");
		         }
		         if(typeStr==null)
		        	 typeStr = (String) fields.get("fileType");
		         if(commandStr==null)
		        	 commandStr = (String) fields.get("command");
		         if(currentFolderStr==null)
		        	 currentFolderStr = (String) fields.get("path");
		         reqType = (String) fields.get("reqType");
		         itemIdStr = (String) fields.get("itemId");
		         if(itemIdStr!=null && !itemIdStr.trim().equalsIgnoreCase("")){
		        	 itemId = Integer.parseInt(itemIdStr);
		         }
		         
		         taxTreeIdIdStr = (String) fields.get("taxTreeIdId");
		         if(taxTreeIdIdStr!=null && !taxTreeIdIdStr.trim().equalsIgnoreCase("")){
		        	 taxTreeIdId = Integer.parseInt(taxTreeIdIdStr);
		         }
		         saveCategImg=(String) fields.get("saveCategImg");
		         
		         attrIdStr = (String) fields.get("attributeId");
		         if(attrIdStr!=null && !attrIdStr.trim().equalsIgnoreCase("")){
		        	 attributeId = Integer.parseInt(attrIdStr);
		         }
		         saveAttrImg=(String) fields.get("saveAttrImg");
		         
		         itemAttrValueTableIdstr = (String) fields.get("itemAttrValueTableId");
		         if(itemAttrValueTableIdstr!=null && !itemAttrValueTableIdstr.trim().equalsIgnoreCase("")){
		        	 itemAttrValueTableId = Integer.parseInt(itemAttrValueTableIdstr);
		         }
		         
		         
		        System.out.println("itemId not null");
		        String virtualPath = CommonDBQuery.getSystemParamtersList().get(typeStr);
		        virtualPath = virtualPath==null?"":virtualPath;
		        String warFolderName=getWarFileName(virtualPath);
				baseDir = CommonDBQuery.getSystemParamtersList().get("VIRTUALFOLDERPATH")+warFolderName;
				//baseDir = "d:/LaptopBackup/E Drive/SupplyForce_images/server/default/deploy/PRODUCTIMAGES.war/OriginalImage/";
				System.out.println(baseDir);
		         String currentPath = baseDir + currentFolderStr;
		         String currentDirPath = currentPath;
		         
		    if (debug) System.out.println(currentDirPath);

		   

		    if (!commandStr.equals("FileUpload")) {
		      retVal = "203";
		    } else {
		    	String folderPath = (String) fields.get("path");
		        System.out.println(folderPath);
		        FileItem uplFile = (FileItem)fields.get("file");
		        String fileNameLong = uplFile.getName();
		      
		    	fileNameLong = fileNameLong.replace('\\', '/');
		    	Pattern regex = Pattern.compile("[$&+,:;=?@%#|]");
		    	Matcher matcher = regex.matcher(fileNameLong);
		    	 if(matcher.find()){
			        	out.print("{ \"error\": \"Image Name Not Valid.\" }");
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			     }else{
		        String[] pathParts = fileNameLong.split("/");
		        String fileName = pathParts[(pathParts.length - 1)];
		        
		        if(bannerUpdate!=null && !bannerUpdate.isEmpty()){
	 				// TempDataHolder.getInstance().setStringValue(fileName);
		        }	 
		        
		        int result = 0;
		   
		        String nameWithoutExt = getNameWithoutExtension(fileName);
		        String ext = getExtension(fileName);
		        File pathToSave = new File(currentDirPath, fileName);
		        int counter = 1;
		        if (pathToSave.exists()) {
		         /* newName = nameWithoutExt + "(" + counter + ")" + "." + ext;
		          retVal = "201";
		          pathToSave = new File(currentDirPath, newName);
		          counter++;*/
		        	imageExist = true;
		        	//overWriteImage="Y";
		        }
		        if(imageExist && overWriteImage == null){
		        	
		        	/**
		        	 * @author harish
		        	 * this if statment required as uploading banner doesnot have overwrite option
		        	 * and to return if the banner image already exists.
		        	 * */
		        	if(commandStr.equalsIgnoreCase("FileUpload")){
		        		out.print("{ \"error\": \"Image Already Exists.To upload Image with same name please overwrite\" }");
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
		        	}
		        	
		        	 File dir = new File(currentDirPath);
		        	 if(!dir.exists()){
		        		 out.print("{ \"error\": \"Directory not available\" }");
		        	 }else{
		        			 try {
		        				virtualPath = "/PRODUCTIMAGES/OriginalImage/";
		        				              uplFile.write(new File(currentDirPath, fileName));
		        				              String extenstion = getExtension(fileName);
		        						        if(!extenstion.equalsIgnoreCase("jpg") && !extenstion.equalsIgnoreCase("png") && !extenstion.equalsIgnoreCase("gif") && !extenstion.equalsIgnoreCase("jpeg")){
		        						        }else{imageResize(currentDirPath,fileName);}
		        						        out.print("{ \"path\": \""+virtualPath+fileName+"\" }");
		        				             // ImageIO.write(bImage, ext, new File(currentDirPath+fileName));
		        				             } catch (IOException e) {
		        				 
		        				                System.out.println("Exception occured :" + e.getMessage());
		        		 }
		        	 }
		               
		        	/*out.print("{ \"error\": \"Image Already Exists.\" }");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);*/
		        	//JOptionPane.showMessageDialog(null, "test", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
		        }else if(imageExist && !(overWriteImage!=null && overWriteImage.equalsIgnoreCase("Y"))){
		        	out.print("{ \"error\": \"Image Already Exists.To upload Image with same name please overwrite\" }");
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		        	
		        }else {
		        
		        	uplFile.write(pathToSave);
			        String extenstion = getExtension(fileName);
			        if(!extenstion.equalsIgnoreCase("jpg") && !extenstion.equalsIgnoreCase("png") && !extenstion.equalsIgnoreCase("gif") && !extenstion.equalsIgnoreCase("jpeg")){
			        }else{imageResize(currentDirPath,fileName);}
			        out.print("{ \"path\": \""+virtualPath+fileName+"\" }");
		        }
		        
		}
		        /*
		            out.println("<script type=\"text/javascript\">");
		            out.println("window.parent.frames['frmUpload'].OnUploadCompleted(" + retVal + ",'" + newName + "');");
		            out.println("</script>");*/
		            out.flush();
		            out.close();

		    }
		      
		  }
		  
    	
    }catch (Exception ex) {
		out.print("{ \"error\": \"Authentication Required.\" }");
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	  	  ex.printStackTrace();
	      retVal = "203";
	    }
    

    if (debug) System.out.println("--- END DOPOST ---");
  }

  private void setCreateFolderResponse(String retValue, Node root, Document doc)
  {
    Element myEl = doc.createElement("Error");
    myEl.setAttribute("number", retValue);
    root.appendChild(myEl);
  }

  private void imageResize(String currentDirPath, String fileName)
	{
	  
	  try
	  {
		long startTime = System.currentTimeMillis();
		  File f = new File(currentDirPath,fileName);
		  BufferedImage img = ImageIO.read(f); // load image

		  //Quality indicate that the scaling implementation should do everything
		   // create as nice of a result as possible , other options like speed
		   // will return result as fast as possible
		//Automatic mode will calculate the resultant dimensions according
		//to image orientation .so resultant image may be size of 50*36.if you want
		//fixed size like 50*50 then use FIT_EXACT
		//other modes like FIT_TO_WIDTH..etc also available.

		  BufferedImage thumbImg = Scalr.resize(img, Method.QUALITY,Mode.AUTOMATIC, 122,122, Scalr.OP_ANTIALIAS);
		  //BufferedImage cropImg = Scalr.crop(img, 100, 200, Scalr.OP_ANTIALIAS);
		  /*Scalr.crop(src, x, y, width, height, ops);*/
		   //convert bufferedImage to outpurstream 
		 /* ByteArrayOutputStream os = new ByteArrayOutputStream();
		  ImageIO.write(thumbImg,"jpg",os);
		  */
		  
		  //or wrtite to a file
		  File newFolder = new File(currentDirPath, "thumbnail");
	      String retValue = "110";

	      if (newFolder.exists())
	        retValue = "101";
	      else {
	        try
	        {
	          boolean dirCreated = newFolder.mkdir();
	          if (dirCreated)
	            retValue = "0";
	          else
	            retValue = "102";
	        } catch (SecurityException sex) {
	          retValue = "103";
	        }
	      }
		  File f2 = new File(newFolder,fileName);
		  
		  
		  ImageIO.write(thumbImg, getExtension(fileName), f2);
		  img.flush();
		  System.out.println("time is : " +(System.currentTimeMillis()-startTime));
	  }
	  catch (Exception e) {
		e.printStackTrace();
	}
		
	}
  private void getFolders(File dir, Node root, Document doc)
  {
    Element folders = doc.createElement("Folders");
    root.appendChild(folders);
    File[] fileList = dir.listFiles();
    for (int i = 0; i < fileList.length; i++)
      if (fileList[i].isDirectory()) {
    	  if(!fileList[i].getName().equalsIgnoreCase("thumbnail") && !fileList[i].getName().equalsIgnoreCase("WEB-INF")){
    		  Element myEl = doc.createElement("Folder");
    	        
    	        myEl.setAttribute("name", fileList[i].getName());
    	        folders.appendChild(myEl);
    	  }
      }
  }

  private void getFiles(File dir, Node root, Document doc)
  {
	  try
	  {
    Element files = doc.createElement("Files");
    
	/* BufferedImage image = null;*/
    root.appendChild(files);
    File[] fileList = dir.listFiles();
    for (int i = 0; i < fileList.length; i++)
      if (fileList[i].isFile()) {
    	  
        Element myEl = doc.createElement("File");
        
        myEl.setAttribute("name", fileList[i].getName());
        myEl.setAttribute("size", "" + fileList[i].length() / 1024L);
        myEl.setAttribute("dimension", " x " );
        myEl.setAttribute("extension", getExtension(fileList[i].getName()));
        files.appendChild(myEl);
      }
	  }
	  catch (Exception e) {
		
		  e.printStackTrace();
	}
	  }

  private Node CreateCommonXml(Document doc, String commandStr, String typeStr, String currentPath, String currentUrl)
  {
    Element root = doc.createElement("Connector");
    doc.appendChild(root);
    root.setAttribute("command", commandStr);
    root.setAttribute("resourceType", typeStr);

    Element myEl = doc.createElement("CurrentFolder");
    myEl.setAttribute("path", currentPath);
    myEl.setAttribute("url", typeStr+currentPath);
    root.appendChild(myEl);

    return root;
  }

  public static String getWarFileName(String path){
		
		String str[] = path.split("/");
		String warFile = "";
		if(str.length > 1){
			warFile = "/" + str[1] + ".war";
		}
		warFile = warFile + "/";
		for(int i=2; i<str.length; i++){
			
			warFile = warFile + str[i] + "/";
		}
		
		return warFile;
	}
  
  private static String getNameWithoutExtension(String fileName)
  {
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  private String getExtension(String fileName)
  {
    return fileName.substring(fileName.lastIndexOf(".") + 1);
  }

public int getSiteId() {
	return siteId;
}

public void setSiteId(int siteId) {
	this.siteId = siteId;
}
  

  
}