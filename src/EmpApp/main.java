package EmpApp;

import CSV_Test.test;
import DailyTimeRecords.Record;
import EmpApp.config.*;
import static EmpApp.config.connectDB;
import Reports.Reports;
import employees.employees;
import java.util.Scanner;
import EmpApp.validation;


public class main {
    
    
     

    public static void main(String[] args) {
        
        Scanner oha = new Scanner(System.in);
        String choice;  
        employees emp = new employees();
        Record rcrd = new Record();
        Reports rpt = new Reports();
        validation val = new validation();
        
        
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
        System.out.println("   3. Reports");
        System.out.println("   4. Exit Application");
        System.out.println();
        System.out.println("***********************************************");

        
        System.out.print("Enter Action: ");
        int act = val.validateChoice();
           
         
         switch(act){
             
             case 1:
                    emp.Employee();
                    break;
                    
             case 2:
                    rcrd.DailyTimeRecord();
                    break;
                    
             case 3:
                 rpt.Report();
                 break;
                 
                         
             case 4:
                 
                 System.out.print("Please Confirm to Exit (y/n): ");
                 String cnfrm = oha.nextLine();
                 
                    if (cnfrm.equals("y") || cnfrm.equals("Y") ){
                        
                           System.out.println("Exiting the application. Goodbye! Thank You!");
                           System.exit(0); 
                           
                    }
                    else if (cnfrm.equals("n") || cnfrm.equals("N")){
                        break;
                    }
                                    
             default:
                    
                    System.out.print("\tError! Invalid selection.\n");    
                    break;
                    
         }
         
       }while(true);
       
          
    }
    
    
    
}
