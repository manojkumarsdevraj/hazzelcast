package com.unilog.cmsmanagementdao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.unilog.cmsmanagementdaoSQL.WebLiveDaoSQL;
import com.unilog.database.ConnectionManager;
import com.unilog.utility.CommonUtility;

public class WebLiveDAO {
	
	public List<String> getStaticFilesInfo(long userId,long siteId,String[] filesList,String currentDirPath,String selectedType) throws IOException {
		Connection conn = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		String pageContent="";
		List<String> notRemoveFileNameList=new ArrayList<String>();
		List<String> inputlist=new LinkedList<String>(Arrays.asList(filesList));
		File file=null;
		InputStream inStream = null;
		OutputStream outStream = null;
		File delfile = null;
	    int length;
		try{
			conn = ConnectionManager.getDBConnection();
			String sql1 = WebLiveDaoSQL.getStaticPageInfo();			
			ps1 = conn.prepareStatement(sql1);
			ps1.setLong(1, siteId);
			//ps1.setLong(2, userId);
			rs= ps1.executeQuery();
			while(rs.next()) {
				System.out.println("STATIC_PAGE_ID-> "+rs.getInt("STATIC_PAGE_ID")+" - PAGE_NAME->"+rs.getString("PAGE_NAME"));
				pageContent = clobToStringConversion(rs.getClob("PAGE_CONTENT"));
				for(int i=0;i<inputlist.size();i++) {
					String childFileName = URLDecoder.decode(CommonUtility.validateString(inputlist.get(i)), "UTF-8");
					if(pageContent.contains(selectedType+"/" +childFileName)) {
						System.out.println("Yes !! " +childFileName);
						notRemoveFileNameList.add(childFileName);
						//inputlist.remove(i);
					}						
				}
			}
			inputlist.removeAll(new HashSet(notRemoveFileNameList));
			System.out.println(inputlist.size());
			ConnectionManager.closeDBPreparedStatement(ps1);
			if(inputlist!=null && !inputlist.isEmpty()) {
				for(int i=0;i<inputlist.size();i++) {
					try {
						String childFileName = URLDecoder.decode(CommonUtility.validateString(inputlist.get(i)), "UTF-8");
						file=new File(currentDirPath.concat(childFileName));
						System.out.println(" is file? "+file.isFile()+" OR is directory? "+file.isDirectory());
						
						boolean saveToFolderFlag = false;
						if(currentDirPath.contains(selectedType)) {
							saveToFolderFlag = true;
						}else if (CommonUtility.validateString(selectedType).contains("DOCUMENTS")) {
							saveToFolderFlag = true;
							selectedType = "DOCUMENTS";
						}
						
						if(saveToFolderFlag) {
							delfile = new File((currentDirPath.replaceAll(selectedType,(("DEL_").concat(selectedType))).concat(childFileName.replaceAll(childFileName.substring(childFileName.lastIndexOf("/")+1,childFileName.length()),("DEL_"+userId+"_"+new Date().getTime()+"_").concat(childFileName.substring(childFileName.lastIndexOf("/")+1,childFileName.length()))))));
							delfile.getParentFile().mkdirs(); 
													
							if(file.isFile()) {
								delfile.createNewFile();
								inStream = new FileInputStream(file);
						    	outStream = new FileOutputStream(delfile);
						    	byte[] buffer = new byte[1024];
					    	    //copy the file content in bytes 
					    	    while ((length = inStream.read(buffer)) > 0){
					    	    	outStream.write(buffer, 0, length);
					    	    }
							}else if(file.isDirectory()) {
								if(!file.exists()){
						           System.out.println("Directory does not exist.");
						           //just exit //System.exit(0);
						        }else{
						           copyFolder(file,delfile);
						        }
							}
						} 
					} catch(Exception ex) {
						ex.printStackTrace();
					} finally {
						if (outStream != null) {outStream.close();}
						if (inStream != null) {inStream.close();}
						//file.delete();
						recursiveDelete(file);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(ps1);
	        ConnectionManager.closeDBConnection(conn);
	        ConnectionManager.closeInputStream(inStream);
		}
		return notRemoveFileNameList;
   }
	
	public static String clobToStringConversion(Clob clb) throws IOException, SQLException
    {
      if (clb == null)
        return  "";            
      StringBuffer str = new StringBuffer();
       String strng;          
    
    BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());   
      while ((strng=bufferRead .readLine())!=null)
       str.append(strng);   
      return str.toString();

    }
	
	public String deleteStaticFiles(long userId,long siteId,String[] deleteFilesList,String currentDirPath,String selectedType) {
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		String pageContent="";
		InputStream inStream = null;
		OutputStream outStream = null;	
		List<String> inputlist=new LinkedList<String>(Arrays.asList(deleteFilesList));
		String childFileName=null;
		int removeCount=0;
		int length;
		File file=null;
		File delfile = null;
		boolean dstatus = false;
		StringBuilder statusMessage =new StringBuilder();
		try{
			con = ConnectionManager.getDBConnection();
			String sql1=WebLiveDaoSQL.getUpdatePageContent();
			ps1 = con.prepareStatement(sql1);
			for(int i=0;i<inputlist.size();i++)
			{		
				try {
					childFileName = URLDecoder.decode(CommonUtility.validateString(inputlist.get(i)), "UTF-8");
					file=new File(currentDirPath.concat(childFileName));
					System.out.println(" is file? "+file.isFile()+" OR is directory? "+file.isDirectory());
					
					boolean saveToFolderFlag = false;
					if(currentDirPath.contains(selectedType)) {
						saveToFolderFlag = true;
					}else if (CommonUtility.validateString(selectedType).contains("DOCUMENTS")) {
						saveToFolderFlag = true;
						selectedType = "DOCUMENTS";
					}
					
					if(saveToFolderFlag) {
						delfile = new File((currentDirPath.replaceAll(selectedType,(("DEL_").concat(selectedType))).concat(childFileName.replaceAll(childFileName.substring(childFileName.lastIndexOf("/")+1,childFileName.length()),("DEL_"+userId+"_"+new Date().getTime()+"_").concat(childFileName.substring(childFileName.lastIndexOf("/")+1,childFileName.length()))))));
						delfile.getParentFile().mkdirs(); 
												
						if(file.isFile()) {
							delfile.createNewFile();
							inStream = new FileInputStream(file);
					    	outStream = new FileOutputStream(delfile);
					    	byte[] buffer = new byte[1024];
				    	    //copy the file content in bytes 
				    	    while ((length = inStream.read(buffer)) > 0){
				    	    	outStream.write(buffer, 0, length);
				    	    }
						}else if(file.isDirectory()) {
							if(!file.exists()){
					           System.out.println("Directory does not exist.");
					           //just exit //System.exit(0);
					        }else{
					           copyFolder(file,delfile);
					        }
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					if (outStream != null) {outStream.close();}
					if (inStream != null) {inStream.close();}
					//file.delete();
					dstatus = recursiveDelete(file);
					//file=new File(currentDirPath.concat(removestr));
					//boolean dstatus=file.delete();
					ps1.setString(1,childFileName);
					ps1.setString(2,childFileName);
					ps1.setLong(3, siteId);
					//ps1.setLong(4, userId);
					int status=ps1.executeUpdate();
					if(dstatus&&status>0) {
						removeCount++;
					}
				}
									
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ConnectionManager.closeDBPreparedStatement(ps1);
	        ConnectionManager.closeDBConnection(con);
	        ConnectionManager.closeInputStream(inStream);
		}
		if(removeCount==inputlist.size())
		{
			return "File(s)Deleted Successfully";
		}
		else
		{
			return "Delete Failed";
		}
      }
	
	public static void copyFolder(File src, File dest){
		
		try {
			
			if(src.isDirectory()){
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		   System.out.println("Directory copied from " + src + "  to " + dest);
	    		}
	    		//list all the directory contents
	    		String files[] = src.list();
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		  
	    		   copyFolder(srcFile,destFile);
	    		}
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	    OutputStream out = new FileOutputStream(dest); 
	    	    byte[] buffer = new byte[1024];
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = in.read(buffer)) > 0){
	    	       out.write(buffer, 0, length);
	    	    }
	    	    in.close();
	    	    out.close();
	    	    System.out.println("File copied from " + src + " to " + dest);
	    	}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
	 }
	
	public boolean recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists()) {
        	return false;
        }
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        System.out.println("Deleted file/folder: "+file.getAbsolutePath());
        return file.delete();
       
    }
	

}