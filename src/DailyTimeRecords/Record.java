
package DailyTimeRecords;


import EmpApp.config;
import employees.employees;
import java.time.Duration;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;


public class Record {
        
    Scanner sc = new Scanner(System.in);
    
    
  public void AddDTR() {
    Scanner sc = new Scanner(System.in);
    config cfg = new config();

    System.out.print("Enter Employee's ID: ");
    String id = sc.next();
    System.out.print("Enter Entry Date (yyyy-MM-dd): ");
    String entrydate = sc.next();
    LocalDate date = LocalDate.parse(entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String month = date.getMonth().toString();
    
    System.out.print("Is the Employee Absent? (Yes || No):  ");
    String absnt = sc.next();
    
  
    String timein = "";
    String timeout = "";
    int h_worked = 0;
    int ovTime = 0;

    if (absnt.equalsIgnoreCase("No") || absnt.equalsIgnoreCase("n")) {
        
        
        LocalTime minTimeIn = LocalTime.of(8, 0);
        LocalTime maxTimeIn = LocalTime.of(22, 0); 

       
        LocalTime timeIn = null;
        while (timeIn == null) {
            try {
                System.out.print("Time In (HH:mm): ");
                timein = sc.next();
                timeIn = LocalTime.parse(timein, DateTimeFormatter.ofPattern("HH:mm"));

              
                if (timeIn.isBefore(minTimeIn) || timeIn.isAfter(maxTimeIn)) {
                    System.out.println("Invalid Time In. Please enter a time between 08:00 and 22:00.");
                    timeIn = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please enter time in HH:mm format.");
            }
        }

       
        LocalTime timeOut = null;
        while (timeOut == null) {
            try {
                System.out.print("Time Out (HH:mm): ");
                timeout = sc.next();
                timeOut = LocalTime.parse(timeout, DateTimeFormatter.ofPattern("HH:mm"));
                
            
                if (timeOut.isBefore(minTimeIn) || timeOut.isAfter(maxTimeIn)) {
                    System.out.println("Invalid Time Out. Please enter a time between 08:00 and 22:00.");
                    timeOut = null; 
                }
               
                if (timeout.equals("24:00")) {
                    timeOut = LocalTime.of(0, 0);
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please enter time in (HH:mm | 24 HOUR) format.");
            }
        }

     
        Duration workedDuration = Duration.between(timeIn, timeOut);
        h_worked = (int) workedDuration.toHours();
        int remainingMinutes = (int) workedDuration.toMinutes() % 60;


        if (h_worked < 0) {
            System.out.println("Time Out cannot be earlier than Time In. Please check the times entered.");
            return;
        }

        System.out.println("Total Hours Worked: " + h_worked + " hours and " + remainingMinutes + " minutes");

    
        int standardHours = 8;

  
        if (h_worked > standardHours) {
            ovTime = h_worked - standardHours; 
        }

    
        System.out.println("Total Over Time Hours: " + ovTime);
    }

   
    String sql = "INSERT INTO DailyTimeRecords (employee_id, entry_date, month, absent, time_in, time_out, hours_worked, overtime_hrs) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    cfg.addRecord(sql, id, entrydate, month, absnt, timein, timeout, h_worked, ovTime);
}



    
    
//    public void AddDTR(){
//        
//        config cfg = new config();
//        
//        System.out.print("Enter Employee's ID: ");
//        String id = sc.next();
//        System.out.print("Enter Entry Date (yyyy-MM-dd): ");
//        String entrydate = sc.next();
//        LocalDate date = LocalDate.parse(entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String month = date.getMonth().toString(); 
//        System.out.print("Is the Employee Absent? (Yes || No):  ");
//        String absnt = sc.next();
//        String timein;
//        String timeout;
//        int h_worked;
//        int ovTime;
//        
//        if (absnt.equals("Yes") || absnt.equals("yes") ||  absnt.equals('y') || absnt.equals('Y') ){
//            timein = "";
//            timeout = "";
//            h_worked = 0;
//            ovTime = 0;
//        }
//        
//        else{   
//            
//            System.out.print("Time In: ");
//            timein = sc.next();
//            System.out.print("Time Out: ");
//            timeout = sc.next();
//            System.out.print("Total Hours Worked: ");
//            h_worked = sc.nextInt();
//            System.out.print("Total Over Time Hours: ");
//            ovTime = sc.nextInt();
//        
//        }
//        
//        String sql = "INSERT INTO DailyTimeRecords (employee_id, entry_date, month, absent, time_in, time_out, hours_worked, overtime_hrs) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//        cfg.addRecord(sql, id, entrydate, month, absnt, timein, timeout, h_worked, ovTime);
//      
//    }
    
    public void viewRecord(){
        config cfg = new config();
        
        String emp_dtls = "select * from DailyTimeRecords";
        String[] emp_hdrs = {"Record ID", "Employee ID", "Entry Date", "Time In", "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"};
         String[] emp_clmn = {"record_id", "employee_id", "entry_date", "time_in", "time_out", "month", "hours_worked", "overtime_hrs", "absent"};
        cfg.viewRecords(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    
    public void viewEmployeesv2(){
        config cfg = new config();
        
        String emp_dtls = "select * from tbl_employees";
        String[] emp_hdrs = {"ID", "FIRST NAME", "LAST NAME"};
         String[] emp_clmn = {"emp_id", "emp_fname", "emp_lname"};
        cfg.viewRecordsEmp(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    
    public void viewRecords(){
        
        config cfg = new config();
        

        Scanner sc = new Scanner(System.in);

    
        try {
            
            System.out.print("Enter the Employee's ID you want to Sort: ");
            int id = sc.nextInt();  
            
            
            sc.nextLine();  

           
            System.out.print("Enter the Month you want to Sort (Must be Uppercase): ");
            String month = sc.nextLine().trim();  

            
            String record_dtls = "SELECT * FROM DailyTimeRecords WHERE employee_id = ? AND month = ?";

           
            String[] dtr_hdrs = {
                "Record ID", "Employee ID", "Entry Date", "Time In", 
                "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"
            };

          
            String[] dtr_clmn = {
                "record_id", "employee_id", "entry_date", "time_in", 
                "time_out", "month", "hours_worked", "overtime_hrs", "absent"
            };

           
            cfg.viewRecordsV2(record_dtls, dtr_hdrs, dtr_clmn, id, month);

        } catch (Exception e) {
            
            e.printStackTrace();
            System.out.println("An error occurred while fetching records. Please try again.");
        }
    }

    
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;  
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    
    
     public void updateRecord() {
        
       config cfg = new config();
     
        int rid, h_worked, ovTime;
        String timein, timeout, absent;
        String sqlUpdate = "UPDATE DailyTimeRecords SET absent = ?, time_in = ?, time_out = ?, hours_worked = ?, overtime_hrs = ? WHERE record_id = ?";
       
                 System.out.print("Enter What Record ID you want to Update: ");
                 rid = sc.nextInt();
                     
                 viewRecords();
                     
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
            System.out.println("3. Edit Daily Time Record");
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
                viewEmployeesv2();
                System.out.print("\n\n");
                AddDTR();
                break;
            
            case 2:
                
                   viewEmployeesv2();
                   viewRecords();
             
                break;
               
            case 3:
                
                viewEmployeesv2();
                viewRecords();
                updateRecord();
                break;
                
            case 4:
                viewEmployeesv2();
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