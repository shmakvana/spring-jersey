package org.javasavvy.rest.controller;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Path("/user")
public class UserRestResource {

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create-user")
	public String createUser(@FormDataParam("file") InputStream uploadedInputStream,
																		@FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

		String fileLocation = "/home/admin1/development/" + fileDetail.getFileName();
		System.out.println("File Name: " + fileLocation);
		Map<String,String> list = new HashMap<String,String>();

		Workbook workbook = null;
		if(fileLocation.toLowerCase().endsWith("xlsx")){
			workbook = new XSSFWorkbook(uploadedInputStream);
		}else if(fileLocation.toLowerCase().endsWith("xls")){
			workbook = new HSSFWorkbook(uploadedInputStream);
		}
		DataFormatter dataFormatter = new DataFormatter();
		Sheet sheet = workbook.getSheetAt(0);
		Row row1 = sheet.getRow(0);
		int index = 0;
		int index1 = 0;
		for (Cell cell : row1) {
			if (cell.getRichStringCellValue().getString().trim().equals("Name")) {
				index =  cell.getColumnIndex();
			} if (cell.getRichStringCellValue().getString().trim().equals("Number")) {
				index1 =  cell.getColumnIndex();
			}
		}
		for (Iterator<Row> iterator = sheet.iterator(); iterator.hasNext(); ) {
			Row row = iterator.next();
			String cellValue1 = dataFormatter.formatCellValue(row.getCell(index));
			String cellValue2 = dataFormatter.formatCellValue(row.getCell(index1));
			list.put(cellValue1, cellValue2);
		}
		System.out.println(list);

		return fileDetail.getName();
	}
}
