package EmpApp;

import DailyTimeRecords.Record;
import EmpApp.config.*;
import static EmpApp.config.connectDB;
import Reports.Reports;
import employees.employees;
import java.util.Scanner;


public class main {
    
    /*DELETE FROM tbl_employees;*/
     

    public static void main(String[] args) {
        
        Scanner oha = new Scanner(System.in);
        String choice;  
        
       do{
           
        System.out.println();
        System.out.println("***********************************************");
        System.out.println("*                                             *");
        System.out.println("*             Payslip Management App          *");
        System.out.println("*                                             *");
        System.out.println("***********************************************");
        System.out.println();
        System.out.println("Please select an option:");
        System.out.println();
        System.out.println("   1. Manage Employee Records");
        System.out.println("   2. Manage Daily Time Records");
        System.out.println("   3. Generate Reports");
        System.out.println("   4. Exit Application");
        System.out.println();
        System.out.println("***********************************************");

        System.out.print("Enter Action: ");
        int act = oha.nextInt();
           
         
         switch(act){
             
             case 1:
                    employees emp = new employees();
                    emp.Employee();
                    break;
                    
             case 2:
                    Record rcrd = new Record();
                    rcrd.DailyTimeRecord();
                    break;
                    
             case 3:
                 Reports rpt = new Reports();
                 rpt.Report();
                 break;
                    
                    
         }
         
           
         
       }while(true);
       
          
    }
    
    
    
}
