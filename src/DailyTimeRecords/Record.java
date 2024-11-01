/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DailyTimeRecords;

import java.time.LocalDate;
import EmpApp.config;
import employees.employees;
import java.util.Scanner;

/**
 *
 * @author Snezhy
 */
public class Record {
        
    public void TimeIn(){
        
        Scanner sc = new Scanner(System.in);
        employees emp = new employees();
        config conf = new config();
        
        LocalDate date = LocalDate.now();
        String month = date.getMonth().toString();
        
        
        System.out.print("Enter Your ID: ");
        int id = sc.nextInt();
        
        String checkSql = "SELECT COUNT(*) FROM DailyTimeRecords WHERE employee_id = ? AND entry_date = ?";
        boolean alreadyTimedIn = conf.recordExists(checkSql, id, date);  
       
      if(alreadyTimedIn){
          System.out.print("You have already timed in today."); 
      }
      else{
          
        System.out.print("Enter Time In (HH:MM): ");
        String in = sc.next();
        String sql = "INSERT INTO DailyTimeRecords (employee_id, entry_date, time_in, month) VALUES (?, ?, ?, ?)";


       conf.addRecord(sql, id, date, in, month);
    }
    }
    
    public void viewDTR(){
        config cfg = new config();
        
        String emp_dtls = "select * from DailyTimeRecords";
        String[] emp_hdrs = {"Record ID", "Employee ID", "Entry Date", "Time In", "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"};
         String[] emp_clmn = {"record_id", "employee_id", "entry_date", "time_in", "time_out", "month", "hours_worked", "overtime_hrs", "absent"};
        cfg.viewRecords(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    
    public void viewEmployees(){
        config cfg = new config();
        
        String emp_dtls = "select * from tbl_employees";
        String[] emp_hdrs = {"ID", "FIRST NAME", "LAST NAME"};
         String[] emp_clmn = {"emp_id", "emp_fname", "emp_lname"};
        cfg.viewRecords(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    
    public void DailyTimeRecord(){
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        
   

          System.out.println("\n Daily Time Record");
          System.out.println("--------------------------");
          System.out.println("1. Time In");
          System.out.println("2. Time Out");
          System.out.println("3. Absnent");
          System.out.println("4. Go Back");
          System.out.println("5. Exit");
          System.out.println("--------------------------");

            System.out.print("Enter Action: ");
            int actn = sc.nextInt();
            //Validation
        
        switch(actn){
            
            case 1:
                viewEmployees();
                TimeIn();
                System.out.print("\n\n");
                viewDTR();
                break;
            
            case 2:
                break;
               
            case 3:
                return;
            case 4:
                System.out.println("Going back to main menu . . .");
                return;
            default:
                System.out.print("Error Selection!");
            
        }
    
          return;
     
    }
    
}