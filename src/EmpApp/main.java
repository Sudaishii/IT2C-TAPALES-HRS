package EmpApp;

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
        
        System.out.println("\nWelcome to Payslip App");
        System.out.println("--------------------------");
        System.out.println("1. EMPLOYEES");
        System.out.println("2. REPORTS");
        System.out.println("3. EXIT");
        System.out.println("--------------------------");

        System.out.print("Enter Action: ");
        int act = oha.nextInt();

         
         switch(act){
             case 1:
                    employees emp = new employees();
                    emp.Employee();
                    break;
             case 2:
//                    String sql = "ALTER TABLE insect AUTO_INCREMENT = 5";
                    
                    
                    
                    
         }
         
           System.out.print("Do you want to continue? (y:n): ");
           choice = oha.next();
         
       }while(choice.equals("Y") || choice.equals("y"));
           
    }
    
}
