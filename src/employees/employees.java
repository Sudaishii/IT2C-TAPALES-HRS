
package employees;

import static EmpApp.config.connectDB;
import EmpApp.config;
import java.util.Scanner;



   
public class employees {
    
    public void addEmployees(){
         
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Employee's First Name: ");
        String fname = sc.next();
        System.out.print("Employee's Last Name: ");
        String lname = sc.next();
        sc.nextLine();
        System.out.print("Employee's Address: ");
        String email = sc.nextLine();
        System.out.print("Employee's Contact #: ");
        String num = sc.next();
        System.out.print("Employee's Job Title: ");
        String title = sc.next();
        System.out.print("Employee's Hire Date: ");
        String hdate = sc.next();

        String sql = "INSERT INTO tbl_employees (emp_fname, emp_lname, emp_add, emp_contantnum, emp_title, emp_hdate) VALUES (?, ?, ?, ?, ?, ?)";


        conf.addRecord(sql, fname, lname, email, num, title, hdate);


    }
    
    private void viewEmployees(){
        config cfg = new config();
        
        String emp_details = "select * from tbl_employees";
        String[] employeesColumns = {"emp_id", "emp_fname", "emp_lname", "emp_add", "emp_contantnum", "emp_title", "emp_hdate"};
        String[] employeesHeaders = {"ID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT NUMBER", "JOB TITLE", "HIRE DATE"};
        cfg.viewRecords(emp_details, employeesHeaders, employeesColumns);
        
    }
        
    
    public static void Employee(){
        
        Scanner sc = new Scanner(System.in);
        String choice;
       
      do{  
        
        System.out.println("\nWelcome to Payslip App");
        System.out.println("--------------------------");
        System.out.println("1. ADD EMPLOYEE");
        System.out.println("2. VIEW EMPLOYEES");
        System.out.println("3. UPDATE EMPLOYEE");
        System.out.println("4. DELETE EMPLOYEE");
        System.out.println("5. EXIT");
        System.out.println("--------------------------");
        
          System.out.print("Enter Action: ");
          int actn = sc.nextInt();
//          while() VALIDATION
        
        switch(actn){
            case 1:
                employees add = new employees();
                add.addEmployees();
                break;
                
            case 2:
                employees view = new employees();
                view.viewEmployees();
                break;
                
            case 3: 
                
                break;
                
            case 4: 
                
                break;
                
                
            case 5:
                
                break;
      }
            
          System.out.print("Do you want to continue (y/n): ");
          choice = sc.next();
        
      }while(1==1);
}
}