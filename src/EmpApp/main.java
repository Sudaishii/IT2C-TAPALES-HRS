package EmpApp;

import DailyTimeRecords.Record;
import EmpApp.config.*;
import static EmpApp.config.connectDB;
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
        System.out.println("*          Welcome to Payslip App             *");
        System.out.println("*                                             *");
        System.out.println("***********************************************");
        System.out.println();
        
        System.out.println("Please select an option:");
        System.out.println();
        System.out.println("   1. Employees");
        System.out.println("   2. Daily Time Records");
        System.out.println("   3. Reports");
        System.out.println("   4. Exit");
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
             case 3:
                 break;
                    
                    
         }
         
           
         
       }while(true);
       
          
    }
    
}
