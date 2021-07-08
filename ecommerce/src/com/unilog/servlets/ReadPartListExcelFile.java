package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oreilly.servlet.MultipartRequest;
import com.unilog.VelocityTemplateEngine.LayoutLoader;
import com.unilog.database.CommonDBQuery;
import com.unilog.products.MarketBasketData;
import com.unilog.users.UsersDAO;
import com.unilog.utility.CommonUtility;


public class ReadPartListExcelFile extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public ReadPartListExcelFile() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		LinkedHashMap<String,MarketBasketData> searchListWithDuplicate = new LinkedHashMap<String,MarketBasketData>();
		LinkedHashMap<String, MarketBasketData> searchDetailMapToCheckDuplicates = new LinkedHashMap<String, MarketBasketData>();
		LinkedHashMap<String, ArrayList<MarketBasketData>> searchListMap = new LinkedHashMap<String, ArrayList<MarketBasketData>>();
		ArrayList<MarketBasketData> rowSearchDetailList = new ArrayList<MarketBasketData>();
		ArrayList<Integer> skippedRows = new ArrayList<Integer>();
		String errorMsg = "";
		String singleKeyword = "";
		int xlsfile_column_size = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("XLSFILE_COLUMN_SIZE"));
		boolean duplicateFlagForAlert = false;
		int validateFieldCount = 0;
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
		String[] headingArray = null;
		JsonObject jsonObject = new JsonObject();
		int columnCnt=0;
		session.removeAttribute("lastRowNum");
		session.removeAttribute("colCnt");
		session.removeAttribute("InvalidPartList");
		session.removeAttribute("readPartListWithDuplicate");
		session.removeAttribute("searchListMap");
		session.removeAttribute("skippedRows");
		session.removeAttribute("MarketBasketErrorMsg");
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
						int lastRowNum = sheet.getLastRowNum();
						session.setAttribute("lastRowNum", lastRowNum);
						if(lastRowNum > 0){
							columnCnt = headerRow.getLastCellNum();
							for (int rowNum = 0; rowNum <=lastRowNum; rowNum++){
								Row row = sheet.getRow(rowNum);
								if(row==null || row.getPhysicalNumberOfCells()<=0)
								{
									break;
								}
								innerObject = new JsonObject();
								String itemDescrip = "";
								int count = 0;
								validateFieldCount=0;
								String searchType = "";
								String keyWord = "";
								String lineItemComment = "";
								int quantity = 0;
								for(int celNum=0; celNum<headerRow.getLastCellNum(); celNum++){
									boolean invalidQuantity = false;
									count++;
									Cell cell = row.getCell(celNum);
									String xlValue="";
									if(cell==null || cell.getCellType()==Cell.CELL_TYPE_BLANK){
										xlValue="";
										validateFieldCount++;
									}else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
									{
										xlValue = cell.getStringCellValue().trim();
									}
									else if (cell!=null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									{
										cell.setCellType(Cell.CELL_TYPE_STRING);
										//int cellValueReadNumeric = (int) cell.getStringCellValue();
										xlValue = cell.getStringCellValue();
									}
									if(xlValue!=null && !xlValue.trim().equalsIgnoreCase("")){
										xlValue =xlValue.replaceAll("&", " and ");
										//xlValue= xlValue.replaceAll("[\\p{P}\\p{S}]*","");	
										System.out.println(xlValue);
										if(rowNum==0){
											if(CommonUtility.validateString(heading).equalsIgnoreCase("")){
												heading = xlValue;
											}else{
												heading = heading+"|"+xlValue;
											}
											heading = heading.replace(" ","_");
										}else{ 
											headingArray = null;
											if(heading!=null){
												headingArray= heading.split("\\|");
											}
											if(headingArray!=null && headingArray.length>celNum && headingArray[celNum]!=null && !headingArray[celNum].trim().equalsIgnoreCase("")){
												if(headingArray[celNum].trim().equalsIgnoreCase("Description")){
													innerObject.addProperty(headingArray[celNum], xlValue);
												}else if(headingArray[celNum].trim().equalsIgnoreCase(CommonUtility.validateString(LayoutLoader.getMessageProperties().get(session.getAttribute("localeCode").toString().toUpperCase()).getProperty("label.linecomment")).replace(" ","_"))){
													lineItemComment = xlValue;
												}else if(headingArray[celNum].trim().equalsIgnoreCase("Quantity")){
													quantity = CommonUtility.validateNumber(xlValue);
													if(quantity == 0){

													}
													innerObject.addProperty(headingArray[celNum], xlValue);
												} else{
													singleKeyword = xlValue.trim();
													itemDescrip = itemDescrip + "," + xlValue.trim();

													if(celNum!=0 && celNum!=headerRow.getLastCellNum() && searchType!=""){
														searchType = searchType + "," + headingArray[celNum];
														keyWord = keyWord + "," + xlValue.trim();
													}else{
														searchType = searchType + headingArray[celNum];
														keyWord = keyWord + xlValue.trim();
													}
													innerObject.addProperty(headingArray[celNum], xlValue);
													System.out.println(searchType);
													System.out.println(keyWord);
													String results = "";
													if(results!=null && !results.trim().equalsIgnoreCase("")){
														JsonElement o = new JsonParser().parse(results);
														innerObject.add("Result",o);
														innerObject.addProperty("ResultCount",(String)session.getAttribute("resultValQuickCart"));
													}
												}

											}
										} 
									}else if(headingArray!=null && celNum<columnCnt && headingArray[celNum].trim().equalsIgnoreCase("Quantity")){
										invalidQuantity = true;
									}
									if(rowNum!=0){
										if(count==columnCnt && !skippedRows.contains(rowNum) && !invalidQuantity){

											String[] searchTypes = searchType.split(",");
											String[] keyWords = keyWord.split(",");

											if(searchTypes != null && searchTypes.length > 0){
												rowSearchDetailList = new ArrayList<MarketBasketData>();
												for (int i = 0; i < searchTypes.length; i++) {
													MarketBasketData marketBasketData = new MarketBasketData();
													String dupItem = checkPartialKey(searchDetailMapToCheckDuplicates, singleKeyword+","+searchTypes[i]);
													if(CommonUtility.validateString(dupItem).length() > 0){
														duplicateFlagForAlert = true;
														marketBasketData.setDuplicate(true);
													}else{
														marketBasketData.setDuplicate(false);
													}

													marketBasketData.setLineItemComment(lineItemComment);
													marketBasketData.setUserQuantity(quantity);
													marketBasketData.setSearchType(searchTypes[i]);
													marketBasketData.setSearchKeyWord(keyWords[i]);
													marketBasketData.setRowNum(rowNum);
													marketBasketData.setDuplicateItemRows(dupItem);
													marketBasketData.setIndex(CommonUtility.validateString(""+(rowSearchDetailList.size())));


													searchDetailMapToCheckDuplicates.put(singleKeyword+","+searchTypes[i]+rowNum, marketBasketData);
													searchListWithDuplicate.put(searchTypes[i]+CommonUtility.validateString(""+rowNum),marketBasketData);

													rowSearchDetailList.add(marketBasketData);
												}
											}
											searchListMap.put(CommonUtility.validateString(""+rowNum), rowSearchDetailList);
										}else if(xlsfile_column_size!=2 && validateFieldCount==(xlsfile_column_size - 2) && !skippedRows.contains(rowNum)){
											skippedRows.add(rowNum);
										}
										jsonObject.add("Items"+rowNum, innerObject);
									}
								}
							}
						}
					}catch(Exception exec){
						exec.printStackTrace();
					}
				}
			}

			if(jsonObject!=null){

				System.out.println(jsonObject.toString());
				sendResponse(response, jsonObject.toString(),jsonObject);
			}

			if(filePathToDelete != null)
				deletefile(filePathToDelete);
		}catch(Exception e){
			e.printStackTrace();
		}

		session.setAttribute("colCnt", Integer.toString(columnCnt));

		if(columnCnt != xlsfile_column_size)
			errorMsg = "Uploaded file format is not in desired format, Please download a sample file from the above Download Sample File link and try again.";
		if(columnCnt == xlsfile_column_size)
			session.setAttribute("InvalidPartList", "NO");
		else
			session.setAttribute("InvalidPartList", "YES");

		if(filePathToDelete != null)
			deletefile(filePathToDelete);

		session.setAttribute("readPartListWithDuplicate", searchListWithDuplicate);
		session.setAttribute("searchListMap", searchListMap);
		session.setAttribute("skippedRows", skippedRows);
		int uploadItemCnt = CommonUtility.validateNumber(CommonDBQuery.getSystemParamtersList().get("PARTLIST_LOADER_ITEM_SIZE"));

		if(searchListMap.size() > uploadItemCnt){

			errorMsg = "Uploaded list contains more than "+uploadItemCnt+" items, maximum upload item count is "+uploadItemCnt+" or less than "+uploadItemCnt+" items.";

		}else{
			if(errorMsg.equalsIgnoreCase("")){
				if(duplicateFlagForAlert){
					errorMsg = "Duplicate";
				}else if(searchListMap.size() == 0){
					errorMsg = "Uploaded list contains no items to search. Maximum upload item count is "+uploadItemCnt+" or less than "+uploadItemCnt+" items.";
				}else {
					errorMsg = "No Duplicate";
				}
			}else{
				session.setAttribute("MarketBasketErrorMsg", errorMsg);
			}
		}
		session.setAttribute("MarketBasketErrorMsg", errorMsg);	
		response.sendRedirect(response.encodeRedirectURL("quickCartFileUploadPage.action"));
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
		if(jsonObject!=null)
			out.print(jsonObject);

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

	String duplicateItemRows = "";
	public String checkPartialKey(LinkedHashMap<String, MarketBasketData> map, String key){
		duplicateItemRows = "";
		for (Entry<String, MarketBasketData> e : map.entrySet()) {
			if (e.getKey().contains(key)) {
				duplicateItemRows = duplicateItemRows + CommonUtility.validateString(""+map.get(e.getKey()).getRowNum());
				break;
			}
		}
		return duplicateItemRows;
	}
}
