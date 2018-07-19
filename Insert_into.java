
import java.sql.*;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;



import org.apache.poi.xssf.usermodel.XSSFCell;

import org.apache.poi.xssf.usermodel.XSSFRow;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Insert_into {
	public static void main(String[] args) throws IOException {



		// File location

		String path = new File("/Users/hajeng/Desktop").getAbsolutePath();

		String fileName = path + "/orde.xlsx";

		// Order class lists 

		List<Order> orderList = new ArrayList<>();

		int a[] = new int[5];

		// Initial needed values

		XSSFWorkbook inputWorkbook = null;

		XSSFRow incurRow;

		XSSFCell incurCell;

		XSSFSheet incurSheet;



		int rowCount = 0;



		// Open file & get file data

		FileInputStream fis = new FileInputStream(fileName);

		inputWorkbook = new XSSFWorkbook(fis);

		fis.close();



		// Process sheet data



		// Get 1ST sheet

		incurSheet = inputWorkbook.getSheetAt(0);



		// Get 1ST sheet number of rows

		rowCount = incurSheet.getPhysicalNumberOfRows();

        try {  
            Class.forName("com.mysql.jdbc.Driver");       
            //Class.forName("org.gjt.mm.mysql.Driver");  
           System.out.println("OK");  
          }  
          catch (Exception e) {  
            System.out.print("Error loading Mysql Driver!");  
            e.printStackTrace();  
          }  
          try {  
            Connection connect = DriverManager.getConnection(  
                "jdbc:mysql://localhost/Order?useSSL=false&user=root&password=rihajeng");  
            
            System.out.println("mysql OK"); 
            Statement stmt=connect.createStatement();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {

			// Get row

			incurRow = incurSheet.getRow(rowIndex);



			String invalue[] = new String[4];



			int cellCount = incurRow.getPhysicalNumberOfCells();

			for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {

				// Get every cell data

				incurCell = incurRow.getCell(cellIndex);



				switch (incurCell.getCellTypeEnum()) {



				case STRING:

					invalue[cellIndex] = incurCell.getStringCellValue() + "";

					break;



				case NUMERIC:

					invalue[cellIndex] = incurCell.getNumericCellValue() + "";

					break;



				default:

					System.out.println(incurCell.getCellTypeEnum());

				}

			}



			Order order = new Order();

			order.setTableNumber((int) Float.parseFloat(invalue[0]));
			a[rowIndex]=(int) Float.parseFloat(invalue[0]);
			System.out.println(a[rowIndex]);

			orderList.add(order);

		}
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
			stmt.executeUpdate("insert into Test(id)values("+a[i]+");");
		}
        System.out.println("���뵽���ݿ�ɹ�");
        connect.close();
        System.out.println("�ر����ݿ�ɹ�");
        System.out.println("Success connect Mysql server!"); 
      }  
      catch (Exception e) {  
        System.out.print("get data error!");  
        e.printStackTrace();  
      } 


		inputWorkbook.close();

		

		for (Order order : orderList) {

			System.out.println(order.toString());

		}

            
 

		

		

	}

}
