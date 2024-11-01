
package employees;

import static EmpApp.config.connectDB;
import EmpApp.config;
import java.util.Scanner;
import EmpApp.config.*;




   
public class employees {
    
    
    
    public void addEmployees(){
         
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Employee's First Name: ");
        String fname = sc.next();
        System.out.print("Employee's Last Name: ");
        String lname = sc.next();
        System.out.print("Employee's Address: ");
        String addrss = sc.next();
        System.out.print("Employee's Email: ");
        String email = sc.next();
        System.out.print("Employee's Contact #: ");
        String num = sc.next();
        System.out.print("Employee's Hire Date: ");
        String hdate = sc.next();
        System.out.print("Department: ");
        String dept = sc.next();
        System.out.print("Position: ");
        String pos = sc.next();
        System.out.print("Rate (hr/r): ");
        int rate = sc.nextInt();
       

        String sql = "INSERT INTO tbl_employees (emp_fname, emp_lname, emp_add, emp_email, emp_contactnum, emp_hdate, emp_dept, emp_position, emp_rate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        conf.addRecord(sql, fname, lname, addrss, email, num, hdate, dept, pos, rate);


    }
    
    public void viewEmployees(){
        config cfg = new config();
        
        String emp_dtls = "select * from tbl_employees";
        String[] emp_hdrs = {"ID", "FIRST NAME", "LAST NAME", "ADDRESS", "EMAIL", "CONTACT #", "HIRE DATE", "Department", "Position", "Rate"};
         String[] emp_clmn = {"emp_id", "emp_fname", "emp_lname", "emp_add", "emp_email", "emp_contactnum", "emp_hdate", "emp_dept", "emp_position", "emp_rate"};
        cfg.viewRecords(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    
    public void updateEmployees() {
        
         config cfg = new config();
        
        Scanner oha = new Scanner(System.in);
        
        System.out.print("Enter Employee ID: ");
        int id = oha.nextInt();
        
        
        System.out.print("Enter New Email Address: ");
        String email = oha.next();
        
        System.out.print("Enter New Contact #: ");
        String contact = oha.next();
        
        System.out.print("Enter New Job Title: ");
        String title = oha.next();
        

        String sqlUpdate = "UPDATE tbl_employees SET emp_add = ?, emp_contantnum = ?, emp_title = ? WHERE emp_id = ?";


       
      
      
       cfg.updateEmployee(sqlUpdate, email, contact, title, id);
    }
    
     public void deleteEmployees() {
        Scanner sc = new Scanner(System.in);
        config dbConfig = new config();

        System.out.print("Enter the ID you want to delete: ");
        int id = sc.nextInt();
        
        String sqlDelete = "DELETE FROM tbl_employees WHERE emp_id = ?";

        config cfg = new config();
        
        cfg.deleteEmployees(sqlDelete, id);
    }


    
    
       
//    public static void main(String[] args) {
//        config dbConfig = new config();
//
//       
//        String sqlDelete = "DELETE FROM students WHERE id = ?";
//
//  
//        int studentIdToDelete = 1;
//
//        
//        cfg.deleteEmployee(sqlDelete, studentIdToDelete);
//    }

    
    public static void Employee(){
        
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        
        
      do{  
        
        System.out.println("\nEmployee's Data... Loading...");
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
                
                use.addEmployees();
                break;
                
            case 2:
                
                use.viewEmployees();
                break;
                
            case 3: 
                 use.viewEmployees();
                 use.updateEmployees();
                
                 use.viewEmployees();
                break;
                
            case 4: 
                use.viewEmployees();
                use.deleteEmployees();
                
                use.viewEmployees();
               
                break;
                
                
            case 5:
                
               
      }
            
          System.out.print("Do you want to continue (y/n): ");
          choice = sc.next();
        
      }while(choice.equals("Y") || choice.equals("y"));
}

    


}