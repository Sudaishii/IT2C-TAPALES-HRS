
package CSV_Test;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class test {

    public static void main(String[] args) {
        String filePath = "D:\\College\\MySys\\IT2C-TAPALES-HRS\\src\\DailyTimeRecords\\DailyTimeRecords.csv"; 

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            
            String header = br.readLine();
            String[] headers = header.split(",");
            for (String headerName : headers) {
                System.out.printf("%-20s", headerName.trim()); 
            }
            System.out.println();  

            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    System.out.printf("%-20s", value.trim());  
                }
                System.out.println();  
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
