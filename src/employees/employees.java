
package employees;

import static EmpApp.config.connectDB;
import EmpApp.config;
import java.util.Scanner;
import EmpApp.config.*;
import static EmpApp.config.connectDB;
import EmpApp.validation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;





   
public class employees {
    
    
     public double getSingleValue(String sql, Object... params) {
        double result = 0.0;
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving single value: " + e.getMessage());
        }
        return result;
    }
    
   private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]);
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]);
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]);
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]);
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]);
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]);
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
            } else {
                pstmt.setString(i + 1, values[i].toString());
            }
        }
    }
    
    
    public void addEmployees(){
         
        Scanner sc = new Scanner(System.in);
        validation val = new validation();
        config conf = new config();
        
        
        System.out.print("Enter Employee's First Name: ");
        String fname = val.validateName();
        
        System.out.print("Enter Employee's Last Name: ");
        String lname = val.validateName();
        
        System.out.print("Enter Employee's Address: ");
        String addrss = val.validateAddress();
        
        System.out.print("Enter Employee's Email Address: ");
        String email = val.validateEmail();
           
        System.out.print("Enter Employee's Contact Number: ");
        String num = val.validateconNum();
        
        System.out.print("Enter Employee's Hire Date (yyyy-MM-dd): ");
        String hdate = val.validateHireDate();
        
        String deptAndPos = val.AddDeptandPosi();
        String[] deptPosArray = deptAndPos.split(":");
        String dept = deptPosArray[0].trim();
        String pos = deptPosArray[1].trim();


        
        System.out.print("Enter Employee's Rate (per hour): ");
        int rate = val.Rate();

       

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
        validation val = new validation();
        boolean selected = false;

    do{
            
        System.out.println("***********************************************");
        System.out.println("*            Update Employee Details          *");
        System.out.println("***********************************************");
        System.out.println("1. Update Address");
        System.out.println("2. Update Email Address");
        System.out.println("3. Update Contact Number");
        System.out.println("4. Update Department and Position");
        System.out.println("5. Update All Details");
        System.out.println("6. Exit Update Menu");
        System.out.println("***********************************************");
        
        System.out.print("Please select an option (1-6): ");
        int choice = val.validateChoice();
        
        int id;
        String email, contact, dept, post, add;
         String deptAndPos;
         String[] deptPosArray;
        
        
        
        switch(choice){
            
            case 1: 
                
                System.out.print("Enter Employee ID: ");
                id = val.validateInt();
                
                while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
            }
                
                System.out.print("Enter New Address: ");
                add = val.validateAddress();
                
                String sqlUpdateAdd = "UPDATE tbl_employees SET emp_add = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateAdd, add, id);
                
                break;
             
            case 2:
                 
                System.out.print("Enter Employee ID: ");
                id = val.validateInt();
                
                while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
            }
                
                System.out.print("Enter New Email Address: ");
                email = val.validateEmail();
                
                String sqlUpdateEmail = "UPDATE tbl_employees SET emp_email = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateEmail, email, id);
                
                break;
                
            case 3: 
                
                System.out.print("Enter Employee ID: ");
                id = val.validateInt();
                
                while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
            }
                
                System.out.print("Enter New Contact Number: ");
                contact = val.validateconNum();
                
                String sqlUpdateNum = "UPDATE tbl_employees SET emp_contactnum = ? WHERE emp_id = ?";
                
                cfg.updateEmployee(sqlUpdateNum, contact, id);
                
                break;
                
            case 4:
                
               System.out.print("Enter Employee ID: ");
                id = val.validateInt();
                
                while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
            }
                deptAndPos = val.UpdateDeptandPosi(); 
                deptPosArray = deptAndPos.split(":");

                dept = deptPosArray[0].trim();
                post = deptPosArray[1].trim();
                
                String sqlUpdateDept = "UPDATE tbl_employees SET emp_dept = ?, emp_position = ? WHERE emp_id = ?";
                cfg.updateEmployee(sqlUpdateDept, dept, post, id);
                
                break;
             
                
            case 5:
                
                System.out.print("Enter Employee ID: ");
                id = val.validateInt();
                
                while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
            }
                
                System.out.print("Enter New Address: ");
                add = val.validateAddress();
                
                System.out.print("Enter New Email Address: ");
                email = val.validateEmail();
                
                System.out.print("Enter New Contact Number: ");
                contact = val.validateconNum();
                
                deptAndPos = val.UpdateDeptandPosi(); 
                deptPosArray = deptAndPos.split(":");

                dept = deptPosArray[0].trim();
                post = deptPosArray[1].trim();
                
                String sqlUpdate = "UPDATE tbl_employees SET emp_add = ?, emp_email = ?, emp_contactnum = ?, emp_dept = ?, emp_positon = ? WHERE emp_id = ?";
                
                
                cfg.updateEmployee(sqlUpdate, add, email, contact, dept, post, id);
                break;
            
            case 6:
                
                selected = true;
                System.out.println("Going Back . . .");
                break;
                
            default:
                
                 System.out.println("\t\tInvalid option. Please select a valid option (1-6).\n");
                
            } 
            
        }while(!selected);
        
           
    
}   
     public void deleteEmployees() {
        Scanner sc = new Scanner(System.in);
        validation val = new validation();
        config cfg = new config();
        
        System.out.print("Enter the ID you want to delete: ");
        int id = val.validateInt();
        
         while(getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0){
                System.out.print("\tERROR: ID doesn't exist, try again: ");
                id = val.validateInt();
         }
        
        String sqlDelete = "DELETE FROM tbl_employees WHERE emp_id = ?";

        cfg.deleteEmployees(sqlDelete, id);
    }
    
    public static void Employee(){
        
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        validation val = new validation();
        
        boolean selected = false;
        
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
          int actn = val.validateChoice();

        
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
                
                selected = true;
            
            default: 
                
                System.out.println("Invalid Selection!");
               
        }         
      

    } while (!selected);

    System.out.println("Exiting Employee Management Menu...");
}

    


}