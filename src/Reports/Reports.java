/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import EmpApp.config;
import employees.employees;
import java.util.Scanner;

/**
 *
 * @author Snezhy
 */
public class Reports {
    
    
   public void viewReports() {
       
       config cfg = new config();
       
        // SQL query to join the reports with employee and daily time records
        String reportQuery = "SELECT e.emp_id, e.emp_fname, e.emp_lname, " +
                             "d.entry_date, d.time_in, d.time_out, d.hours_worked, " +
                             "d.overtime_hrs, r.month, r.year, " +
                             "r.initial_salary, r.deductions, r.overtime_pay, r.net_pay " +
                             "FROM Reports r " +
                             "JOIN tbl_employees e ON e.emp_id = r.emp_id " +
                             "JOIN DailyTimeRecords d ON d.record_id = r.record_id";

        // Column headers for display
        String[] reportHeaders = {
            "Employee ID", "First Name", "Last Name", "Entry Date", 
            "Time In", "Time Out", "Hours Worked", "Overtime Hours", 
            "Month", "Year", "Initial Salary", "Deductions", 
            "Overtime Pay", "Net Pay"
        };

        // Corresponding column names from the ResultSet
        String[] reportColumns = {
            "emp_id", "emp_fname", "emp_lname", "entry_date", 
            "time_in", "time_out", "hours_worked", "overtime_hrs", 
            "month", "year", "initial_salary", "deductions", 
            "overtime_pay", "net_pay"
        };

        // Call the viewRecords method to execute the query and display results
        cfg.viewRecords(reportQuery, reportHeaders, reportColumns);
    }
    
    
    public void Report(){
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        config cfg = new config();
        
        
        do{
        
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*             Daily Time Record               *");
            System.out.println("***********************************************");
            System.out.println();
            
            System.out.println("5. Exit");
            System.out.println();
            System.out.println("***********************************************");
            System.out.println();
            
            
            System.out.print("Enter Action: ");
            int actn = sc.nextInt();
            sc.nextLine();
           
        
        switch(actn){
            
            case 1:
                viewReports();
                break;
            
            case 2:
                System.out.print("Enter the month (e.g., 'OCTOBER'): ");
                String month = sc.nextLine().toUpperCase();

                System.out.print("Enter the year (e.g., 2024): ");
                int year = sc.nextInt();

//    cfg.generateReports(month, year);
               
                break;
               
            case 3:
                
                
            case 4:
               
                break;
                
            case 5:
                System.out.println("Going back to main menu . . .");
             
            default:
                System.out.print("Error Selection!");
            
        }
            
            System.out.print("Do you want to continue (y/n): ");
            choice = sc.next();
        
        }while(choice.equals("Y") || choice.equals("y"));
        
        return;
    }
     
    }

