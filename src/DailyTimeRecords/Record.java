/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DailyTimeRecords;


import EmpApp.config;
import employees.employees;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Snezhy
 */
public class Record {
        
    Scanner sc = new Scanner(System.in);
    
    public void AddDTR(){
        
        config cfg = new config();
        
        System.out.print("Enter Employee's ID: ");
        String id = sc.next();
        System.out.print("Enter Entry Date (yyyy-MM-dd): ");
        String entrydate = sc.next();
        LocalDate date = LocalDate.parse(entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String month = date.getMonth().toString(); 
        System.out.print("Is the Employee Absent? (Yes || No):  ");
        String absnt = sc.next();
        String timein;
        String timeout;
        int h_worked;
        int ovTime;
        
        if (absnt.equals("Yes") || absnt.equals("yes") ||  absnt.equals('y') || absnt.equals('Y') ){
            timein = "";
            timeout = "";
            h_worked = 0;
            ovTime = 0;
        }
        
        else{   
            
            System.out.print("Time In: ");
            timein = sc.next();
            System.out.print("Time Out: ");
            timeout = sc.next();
            System.out.print("Total Hours Worked: ");
            h_worked = sc.nextInt();
            System.out.print("Total Over Time Hours: ");
            ovTime = sc.nextInt();
        
        }
        
        String sql = "INSERT INTO DailyTimeRecords (employee_id, entry_date, month, absent, time_in, time_out, hours_worked, overtime_hrs) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        cfg.addRecord(sql, id, entrydate, month, absnt, timein, timeout, h_worked, ovTime);
      
    }
    
    public void viewRecord(){
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
    
    public void viewRecords(){
        
        config cfg = new config();
        
        
        
        System.out.print("Enter the Employee's ID you want to Filter: ");
        int id = sc.nextInt();
        
        String record_dtls = "select * from DailyTimeRecords where employee_id = ?";
        String[] dtr_hdrs = {"Record ID", "Employee ID", "Entry Date", "Time In", "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"};
        String[] dtr_clmn = {"record_id", "employee_id", "entry_date", "time_in", "time_out", "month", "hours_worked", "overtime_hrs", "absent"};
        cfg.viewRecordsV2(record_dtls, dtr_hdrs, dtr_clmn, id);
        
    }
    
     public void updateRecord() {
        
       config cfg = new config();
     
   
       
       
        System.out.print("Do you want to Filter Records for (Employee ID) (y/n): ");
        String opt = sc.nextLine();
        int rid, h_worked, ovTime;
        String timein, timeout, absent;
        String sqlUpdate = "UPDATE DailyTimeRecords SET absent = ?, time_in = ?, time_out = ?, hours_worked = ?, overtime_hrs = ? WHERE record_id = ?";
        
       
                
         switch(opt){
             
                 case "y":
                 case "Y":
           
                     
                 viewRecords();
                     
                 System.out.print("Enter What Record ID you want to Update: ");
                 rid = sc.nextInt();
                     
       
       
                System.out.print("Is the Employee Absent? (Yes || No):  ");
                String absnt = sc.next();
                

                if (absnt.equals("Yes") || absnt.equals("yes") ){
                    timein = "";
                    timeout = "";
                    h_worked = 0;
                    ovTime = 0;
                    absent = "Yes";
                }

                else{   
                    absent = "No";
                    System.out.print("Time In: ");
                    timein = sc.next();
                    System.out.print("Time Out: ");
                    timeout = sc.next();
                    System.out.print("Total Hours Worked: ");
                    h_worked = sc.nextInt();
                    System.out.print("Total Over Time Hours: ");
                    ovTime = sc.nextInt();

                }

                

               cfg.updateEmployee(sqlUpdate, absnt, timein, timeout, h_worked, ovTime, rid);
                     break;
                     
                 case "N":
                 case "n":
                  
                     System.out.print("Enter What Record ID you want to Update: ");
                     rid = sc.nextInt();
        
       
        System.out.print("Is the Employee Absent? (Yes || No):  ");
        absnt = sc.next();
        
        
        if (absnt.equals("Yes") || absnt.equals("yes") ){
            timein = "";
            timeout = "";
            h_worked = 0;
            ovTime = 0;
            absent = "Yes";
        }
        
        else{   
            absent = "No";
            System.out.print("Time In: ");
            timein = sc.next();
            System.out.print("Time Out: ");
            timeout = sc.next();
            System.out.print("Total Hours Worked: ");
            h_worked = sc.nextInt();
            System.out.print("Total Over Time Hours: ");
            ovTime = sc.nextInt();
        
        }
       
        
        
       cfg.updateEmployee(sqlUpdate, absnt, timein, timeout, h_worked, ovTime, rid);
                     break;
         }
         
         
    }
     
     
     public void deleteRecord() {
         
        Scanner sc = new Scanner(System.in);
        config dbConfig = new config();
        
        System.out.print("Do you want to Filter Records for (Employee ID) (y/n): ");
        String chc = sc.nextLine();
        
        if (chc.equals("y") || chc.equals("Y")){
                   
                   viewRecords();
               
               }
               else if (chc.equals("n") || chc.equals("N")){
                   
                   viewRecord();
                   
               }
        
        System.out.print("Enter the Record ID you want to delete: ");
        int id = sc.nextInt();
        
        String sqlDelete = "DELETE FROM DailyTimeRecords WHERE record_id = ?";

        config cfg = new config();
        
        cfg.deleteEmployees(sqlDelete, id);
    }
     
    
    public void DailyTimeRecord(){
        
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        
        
        do{
        
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*             Daily Time Record               *");
            System.out.println("***********************************************");
            System.out.println();
            System.out.println("1. Add Daily Time Record");
            System.out.println("2. View Daily Time Records");
            System.out.println("3. Update Daily Time Record");
            System.out.println("4. Delete Daily Time Record");
            System.out.println("5. Exit");
            System.out.println();
            System.out.println("***********************************************");
            System.out.println();
            
            
            System.out.print("Enter Action: ");
            int actn = sc.nextInt();
            sc.nextLine();
           
        
        switch(actn){
            
            case 1:
                viewEmployees();
                System.out.print("\n\n");
                AddDTR();
                break;
            
            case 2:
                
               System.out.print("Do you want to Filter Records for (Employee ID) (y/n): ");
               String chc = sc.nextLine();
                
               if (chc.equals("y") || chc.equals("Y")){
                   
                   viewRecords();
               
               }
               else if (chc.equals("n") || chc.equals("N")){
                   
                   viewRecord();
                   
               }
                break;
               
            case 3:
                viewRecord();
                updateRecord();
                return;
                
            case 4:
                viewEmployees();
                deleteRecord();
                break;
                
            case 5:
                System.out.println("Going back to main menu . . .");
                return;
            default:
                System.out.print("Error Selection!");
            
        }
            
            System.out.print("Do you want to continue (y/n): ");
            choice = sc.next();
        
        }while(choice.equals("Y") || choice.equals("y"));
        
        return;
     
    }
    
}