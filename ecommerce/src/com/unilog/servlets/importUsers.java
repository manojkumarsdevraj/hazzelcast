package com.unilog.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.erp.service.UserManagement;
import com.erp.service.impl.UserManagementImpl;
import com.unilog.database.CommonDBQuery;
import com.unilog.users.UsersDAO;
import com.unilog.users.UsersModel;
import com.unilog.utility.CommonUtility;


/**
 * Servlet implementation class ImportExistingCustomerList
 */
public class importUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public importUsers() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		
		 HttpSession session = request.getSession();
		 String s = (String)session.getAttribute("erpType");
		 System.out.println("Session :" +s);
		    	String filePath = CommonDBQuery.getSystemParamtersList().get("IMPORT_CUSTOMER_LIST");
		    	if(CommonUtility.validateString(filePath).length()>0){
		    		readRecordsFromFile(filePath,session);
		    	}
	}
	
		public static void readRecordsFromFile(String filePath,HttpSession session){
			String defaultUserRoleForImport = CommonDBQuery.getSystemParamtersList().get("DEFAULT_USER_ROLE_FOR_IMPORT");
	    	ArrayList<UsersModel> userDetails = new ArrayList<UsersModel>();
	        try {
	        	File excel = new File(filePath);
	        	
	            FileInputStream fis = new FileInputStream(excel);
	            XSSFWorkbook book = new XSSFWorkbook(fis);
	            XSSFSheet sheet = book.getSheetAt(0);

	            Iterator<Row> itr = sheet.iterator();
	         
	            // Iterating over Excel file in Java
	            while (itr.hasNext()) {
	            	
	                Row row = itr.next();	               
	            
	                
	                Iterator<Cell> cellIterator = row.cellIterator();
	                System.out.println("row number : " + row.getRowNum());
	               if(row.getRowNum() > 0){
	            	   UsersModel registerDetails=new UsersModel();
	            	   while (cellIterator.hasNext()) {
		                	
		                    Cell cell = cellIterator.next();
		                    String cellValue = "";
		                    
		                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_STRING:
		                    	
		                        System.out.print(cell.getColumnIndex()+" - "+cell.getStringCellValue() + "\t");
		                        cellValue = CommonUtility.validateString(cell.getStringCellValue());
		                        
		                        if(cell.getColumnIndex()==0){
		                        	registerDetails.setCompanyName(cellValue);
		                        	
		                        }
		                        else if(cell.getColumnIndex()==1){
		                        	registerDetails.setAccountName(cellValue);
		                        	
		                        }
		                        else if(cell.getColumnIndex()==2){
		                        	registerDetails.setWareHouseCodeStr(cellValue);
		                      	
		                      }else if(cell.getColumnIndex()==3){
		                      	
		                      	
		                      }else if(cell.getColumnIndex()==4){
		                    	  registerDetails.setFirstName(cellValue);
		                      	
		                      }else if(cell.getColumnIndex()==5){
		                      	registerDetails.setLastName((cellValue != null)? cellValue : "");
		                      	
		                      }else if(cell.getColumnIndex()==6){
		                    	  if(cellValue != null){
		                    		  registerDetails.setEmailAddress(cellValue.trim());
		                    	  }
		                      }
		                      else if(cell.getColumnIndex()==8){
			                      	registerDetails.setCity(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==9){
			                      	registerDetails.setState(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==10){
			                      	registerDetails.setZipCode(cellValue);
			                      	registerDetails.setZipCodeStringFormat(cellValue);
		                      }
		                      
		                      else if(cell.getColumnIndex()==11){
			                      	registerDetails.setCountry(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==12){
			                      	registerDetails.setPhoneNo(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==13){
			                      	registerDetails.setPassword(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==15){
			                      	registerDetails.setSalesRepContact(cellValue);
		                      }
		                      else if(cell.getColumnIndex()==16){
		                    	  
		                      }
		                      else if(cell.getColumnIndex()==17){
			                      	registerDetails.setSubsetId(CommonUtility.validateNumber(cellValue));
		                      }
		                      else if(cell.getColumnIndex()==18){
			                      	registerDetails.setUserRole(cellValue);
		                      }
		                        break;
		                        
		                    	case Cell.CELL_TYPE_NUMERIC:
		                    	
		                        System.out.print(cell.getColumnIndex()+" - "+cell.getNumericCellValue() + "\t");
		                         Double s = cell.getNumericCellValue();
		                        cellValue = CommonUtility.validateParseIntegerToString(s.intValue());
		                        if(cell.getColumnIndex()==0){
		                        	registerDetails.setAccountName(cellValue);	                        	
		                        	
		                        }
		                        
		                        break;
		                  
		                }
		                   
		                }
		                
		                registerDetails.setUserStatus("Y");
		              
		                registerDetails.setRequestAuthorizationLevel(defaultUserRoleForImport);
		                
		                registerDetails.setSession(session);
		                userDetails.add(registerDetails);
		                System.out.println("test");
	               }
	                
	            }
	            
	            
	                if(userDetails!=null && userDetails.size()>0){
	                	
	                	int count = 0;
	                	int inActiveCount = 0;
	                	
	               for(UsersModel registerDetails1 : userDetails){
	            	   if(registerDetails1!=null){
	            		   
	            		   registerDetails1.setSession(session);
	            		    
	            		   UserManagement usersObj = new UserManagementImpl();
	            		   String result = usersObj.newOnAccountUserRegistration( registerDetails1);
	            		   
	            		   
	            		   if(result!=null && result.toLowerCase().contains("successfully")){
	            			   
	            			 System.out.println(registerDetails1.getAccountName() +": Successfully Registered Record" + count);
	            			 count++;
	            		   }else if(result!=null && result.contains("Error While Registering")){
	            			   
	            			   String data = registerDetails1.getAccountName() +":"+registerDetails1.getEmailAddress() +" Duplicate User/DB Error";
	            	    		String inactiveFolderPath = CommonDBQuery.getSystemParamtersList().get("INACTIVE_USER_LIST");
	            	    		
	            	    		 boolean newFile = false;
	            	    		 
	            	    		 if(CommonUtility.validateString(inactiveFolderPath).trim().equalsIgnoreCase(""))
	            	    			 inactiveFolderPath = "E:/Project/HandM/InactiveUser/";
	            	     		UsersDAO.folderExist(inactiveFolderPath);
	            	     		
	            				String fileNameGen = inactiveFolderPath+registerDetails1.getAccountName()+"-Duplicate.txt";
	            				System.out.println("Inactive File Name : " + fileNameGen);
	            	    		File file =new File(fileNameGen);
	            	    		if(!file.exists()){
	            	    			file.createNewFile();
	            	    			newFile = true;
	            	    		}
	            	    		PrintWriter pw = null;
	            	    		pw = new PrintWriter(new FileWriter(file, true));
	            	    		if(newFile) {
	            	    			 pw.println("Customer# \t Email-Id");
	            	    		}
	            	    		pw.println(data);
	            	    		pw.flush();
	            	    		inActiveCount++;
	            			   System.out.println(registerDetails1.getAccountName() +":"+registerDetails1.getEmailAddress() +" Duplicate User/DB Error (" + inActiveCount+")"); 
	            			   
	            		   }else{
	            			   String data = registerDetails1.getAccountName() +":"+registerDetails1.getEmailAddress() +" Failed To register";
	            	    		String inactiveFolderPath = CommonDBQuery.getSystemParamtersList().get("INACTIVE_USER_LIST");
	            	    		
	            	    		 boolean newFile = false;
	            	    		 
	            	    		 if(CommonUtility.validateString(inactiveFolderPath).trim().equalsIgnoreCase(""))
	            	    			 inactiveFolderPath = "E:/Project/HandM/InactiveUser/";
	            	     		UsersDAO.folderExist(inactiveFolderPath);
	            	     		
	            				String fileNameGen = inactiveFolderPath+registerDetails1.getAccountName()+"-Inactive.txt";
	            				System.out.println("Inactive File Name : " + fileNameGen);
	            	    		File file =new File(fileNameGen);
	            	    		if(!file.exists()){
	            	    			file.createNewFile();
	            	    			newFile = true;
	            	    		}
	            	    		PrintWriter pw = null;
	            	    		pw = new PrintWriter(new FileWriter(file, true));
	            	    		if(newFile)
	            	    			 pw.println("Customer# \t Email-Id");
	            	    		pw.println(data);
	            	    		pw.flush();
	            	    		inActiveCount++;
	            	    		 System.out.println(registerDetails1.getAccountName() +":"+registerDetails1.getEmailAddress() +" Failed To register (" + inActiveCount+")");      
	            		   }
	         }
	            
	                	}
	               System.out.println("******************************************** File Read Completed  ********************************************************* \n" );
	                }
	            
	        } catch (FileNotFoundException fe) {
	            fe.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	    
	    }

}