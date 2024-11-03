
package employees;

import static EmpApp.config.connectDB;
import EmpApp.config;
import java.util.Scanner;
import EmpApp.config.*;




   
public class employees {
    
    
    
    public void addEmployees(){
         
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter Employee's First Name: ");
        String fname = sc.nextLine();
        System.out.print("Enter Employee's Last Name: ");
        String lname = sc.nextLine();
        System.out.print("Enter Employee's Address: ");
        String addrss = sc.nextLine();
        System.out.print("Enter Employee's Email Address: ");
        String email = sc.nextLine();
        System.out.print("Enter Employee's Contact Number: ");
        String num = sc.nextLine();
        System.out.print("Enter Employee's Hire Date (yyyy-MM-dd): ");
        String hdate = sc.nextLine();
        System.out.print("Enter Employee's Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Employee's Position: ");
        String pos = sc.nextLine();
        System.out.print("Enter Employee's Rate (per hour): ");
        int rate = sc.nextInt();
        sc.nextLine();
       

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

        boolean updating = true;
        while(updating){
            
        System.out.println("***********************************************");
        System.out.println("*            Update Employee Details          *");
        System.out.println("***********************************************");
        System.out.println("1. Update Address");
        System.out.println("2. Update Email Address");
        System.out.println("3. Update Contact Number");
        System.out.println("4. Update Department");
        System.out.println("5. Update Job Position");
        System.out.println("6. Update All Details");
        System.out.println("7. Exit Update Menu");
        System.out.println("***********************************************");
        
        System.out.print("Please select an option (1-7): ");
        int choice = oha.nextInt();
        
        int id;
        String email, contact, dept, post, add;
        
        switch(choice){
            
            case 1: 
                
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Address: ");
                add = oha.nextLine();
                
                String sqlUpdateAdd = "UPDATE tbl_employees SET emp_add = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateAdd, add, id);
                
                break;
             
            case 2:
                 
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Email Address: ");
                email = oha.nextLine();
                
                String sqlUpdateEmail = "UPDATE tbl_employees SET emp_email = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateEmail, email, id);
                
                break;
                
            case 3: 
                
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Contact Number: ");
                 contact = oha.nextLine();
                
                String sqlUpdateNum = "UPDATE tbl_employees SET emp_contactnum = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateNum, contact, id);
                
                break;
                
            case 4:
                
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Department: ");
                 dept = oha.next();
                
                String sqlUpdateDept = "UPDATE tbl_employees SET emp_dept = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateDept, dept, id);
                
                break;
                
            case 5:
                
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Position: ");
                 post = oha.next();
                
                String sqlUpdatePos = "UPDATE tbl_employees SET emp_position = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdatePos, post, id);
                break;
                
            case 6:
                
                System.out.print("Enter Employee ID: ");
                id = oha.nextInt();
                oha.nextLine();
                
                System.out.print("Enter New Address: ");
                add = oha.nextLine();
                
                System.out.print("Enter New Email Address: ");
                email = oha.nextLine();
                
                System.out.print("Enter New Contact Number: ");
                contact = oha.nextLine();
                
                System.out.print("Enter New Department: ");
                 dept = oha.next();
                
                System.out.print("Enter New Position: ");
                post = oha.next();
                
                String sqlUpdate = "UPDATE tbl_employees SET emp_add = ?, emp_email = ?, emp_contactnum = ?, emp_dept = ?, emp_positon = ? WHERE emp_id = ?";
                
                
                cfg.updateEmployee(sqlUpdate, add, email, contact, dept, post, id);
                break;
            
            case 7:
                updating = false;
                System.out.println("Exiting Main Menu . . .");
                break;
                
            default:
                 System.out.println("\t\tInvalid option. Please select a valid option (1-7).");
                break;
        }
        
        
            
        }
        
        
        
        
//         
//        
//        System.out.print("Enter Employee ID: ");
//        int id = oha.nextInt();
//        
//        
//        System.out.print("Enter New Email Address: ");
//        String email = oha.next();
//        
//        System.out.print("Enter New Contact #: ");
//        String contact = oha.next();
//        
//        System.out.print("Enter New Job Title: ");
//        String title = oha.next();
//        
//
//        String sqlUpdate = "UPDATE tbl_employees SET emp_add = ?, emp_contantnum = ?, emp_title = ? WHERE emp_id = ?";
//
//
//       
//      
//      
//       
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
        
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*           Employee Management               *");
            System.out.println("***********************************************");
            System.out.println();
            System.out.println("1. Add New Employee Record");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee Details");
            System.out.println("4. Remove Employee Record");
            System.out.println("5. Return to Main Menu");
            System.out.println();
            System.out.println("***********************************************");
            System.out.println();
    
          System.out.print("Enter Action: ");
          int actn = sc.nextInt();

        
        switch(actn){
            case 1:
                
                use.addEmployees();
                System.out.println("");
                break;
                
            case 2:
                
                use.viewEmployees();
                break;
                
            case 3: 
                 System.out.println("\n\nListing Employees . . .");
                 use.viewEmployees();
                 use.updateEmployees();
                 System.out.println("\n\nListing Updated Employees Details . . .");
                 use.viewEmployees();
                break;
                
            case 4: 
                use.viewEmployees();
                use.deleteEmployees();
                use.viewEmployees();
                break;
                
                
            case 5:
                return;
            
            default: 
                
                System.out.println("Invalid Selection!");
                System.out.println("Going Back to the Main Menu . . .");
                return;
                  
      }
            
          System.out.print("Do you want to continue (y/n): ");
          choice = sc.next();
        
      }while(choice.equals("Y") || choice.equals("y"));
      
      return;
}

    


}