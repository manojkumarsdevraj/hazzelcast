package com.unilog.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.oreilly.servlet.MultipartRequest;
import com.unilog.database.CommonDBQuery;
import com.unilog.database.ConnectionManager;
import com.unilog.misc.EventModel;
import com.unilog.utility.CommonUtility;
import com.unilog.utility.CustomServiceUtility;

/**
 * Servlet implementation class WriteDataToExcel
 */
public class WriteDataToExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteDataToExcel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());		
    }
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String[] columns = {"EventTitle", "StartDate", "EndDate", "EventDescription","ContactPerson", "Address", "Location", "TotalSeats", "BookedSeats"};
    	String startDates = request.getParameter("startDate");
		String endDates = request.getParameter("endDate");
    	Workbook workbooks = new HSSFWorkbook();
    	CreationHelper createHelper = workbooks.getCreationHelper();
    	Sheet sheet = workbooks.createSheet("Eventlist");
    	ArrayList<EventModel> data = new ArrayList<EventModel>();
    	Font headerFont = workbooks.createFont();
        //headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        
    	CellStyle headerCellStyle = workbooks.createCellStyle();
        headerCellStyle.setFont(headerFont);
        
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbooks.createCellStyle();
        try {
			data = CustomServiceUtility.getEventslist(startDates, endDates);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int rowNum = 1;
        
        int eventBodyCol = 255;
        int eventTitleCol = 255;
        int eventAddressCol = 255;
        
        for(EventModel events: data) {             		
        		 Row row = sheet.createRow(rowNum++);
                 row.createCell(0).setCellValue(events.getTitle());
                 if(CommonUtility.validateString(events.getTitle()).length()>eventTitleCol) {
                	 eventTitleCol = CommonUtility.validateString(events.getTitle()).length();
                 }   
                 row.createCell(1).setCellValue(events.getDateV2());
                 row.createCell(2).setCellValue(events.getEndV2());
                 row.createCell(3).setCellValue(events.getEventBodyKeyword());
                 if(CommonUtility.validateString(events.getEventBodyKeyword()).length()>eventBodyCol) {
                	 eventBodyCol = CommonUtility.validateString(events.getEventBodyKeyword()).length();
                 }                 
                 row.createCell(4).setCellValue(events.getContact());
                 row.createCell(5).setCellValue(events.getLocation());                 
                 row.createCell(6).setCellValue(events.getAddress());
                 if(CommonUtility.validateString(events.getAddress()).length()>eventAddressCol) {
                	 eventAddressCol = CommonUtility.validateString(events.getAddress()).length();
                 }  
                 row.createCell(7).setCellValue(events.getTotalSeats());
                 row.createCell(8).setCellValue(events.getBookedSeats());
        }
        
        sheet.setColumnWidth(0, eventTitleCol);
        sheet.setColumnWidth(3, eventBodyCol);
        sheet.setColumnWidth(6, eventAddressCol);
        
        String fileNames = "EventListofCurrentMonth_"+startDates+".xls";        		
		String folder = CommonDBQuery.getSystemParamtersList().get("EVENT_DOCUMENTS_UPLOAD");		
		String path = folder.trim();
		File destination = new File(path);
		if(!destination.exists()){
			System.out.println(path+" : destination dir doesnt exists");
			destination.mkdir();
		}
		String saveFile = CommonDBQuery.getSystemParamtersList().get("EVENT_DOCUMENTS_UPLOAD")+"/"+fileNames;
        FileOutputStream fileOut = new FileOutputStream(new File(saveFile));
        workbooks.write(fileOut);
        fileOut.close();
        PrintWriter out = response.getWriter();
        out.println(fileNames); 
        out.close();
        // Closing the workbook
        //workbooks.close();
	}
	
}
