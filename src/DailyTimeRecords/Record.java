
package DailyTimeRecords;


import EmpApp.config;
import static EmpApp.config.connectDB;
import EmpApp.validation;
import employees.employees;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;


public class Record {
        
    Scanner sc = new Scanner(System.in);
    
    


    public void importDataToDatabase(String csvFilePath) {
        String selectQuery = "SELECT COUNT(*) FROM DailyTimeRecords WHERE employee_id = ? AND entry_date = ?";
        String insertQuery = "INSERT INTO DailyTimeRecords (employee_id, entry_date, time_in, time_out, month, hours_worked, overtime_hrs, absent) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        

        try (Connection conn = config.connectDB();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");

            
                if (record.length != 8) {
                    System.out.println("Skipping invalid record: " + line);
                    continue;
                }

          
                int empId = Integer.parseInt(record[0].trim());
                String entryDate = record[1].trim();
                String timeIn = record[2].trim();
                String timeOut = record[3].trim();
                String month = record[4].trim();
                double hoursWorked = Double.parseDouble(record[5].trim());
                int overtimeHrs = Integer.parseInt(record[6].trim());
                String absent = record[7].trim();

 
                selectStmt.setInt(1, empId);
                selectStmt.setString(2, entryDate);

                try (ResultSet rs = selectStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        System.out.println("Skipping existing record for employee ID: " + empId + " on date: " + entryDate);
                        continue;
                    }
                }

                insertStmt.setInt(1, empId);
                insertStmt.setString(2, entryDate);
                insertStmt.setString(3, timeIn);
                insertStmt.setString(4, timeOut);
                insertStmt.setString(5, month);
                insertStmt.setDouble(6, hoursWorked);
                insertStmt.setInt(7, overtimeHrs);
                insertStmt.setString(8, absent);

                insertStmt.executeUpdate();
                System.out.println("Inserted new record for employee ID: " + empId + "for the Month of: "+ month);
            }
            System.out.println("DailyTimeRecord.csv data imported completed successfully!");

        } catch (Exception e) {
            System.out.println("Error during CSV import: " + e.getMessage());
        }
    }

    
  public void AddDTR() {
    Scanner sc = new Scanner(System.in);  
    config cfg = new config();
    validation val = new validation();

    System.out.print("Enter Employee's ID: ");
    String idInput = sc.nextLine();
    int id = val.validateint(idInput);  
    
    while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0) {
        System.out.print("\tERROR: ID doesn't exist, try again: ");
        idInput = sc.nextLine();  
        id = val.validateint(idInput);  
    }
   
   
    System.out.print("Enter Entry Date (yyyy-MM-dd): ");
    String entrydate = val.validateEntryDate();


    LocalDate date = LocalDate.parse(entrydate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String month = date.getMonth().toString();

 
    System.out.print("Is the Employee Absent? (Yes || No):  ");
    String absnt = sc.next();  
    
    while (!(absnt.equalsIgnoreCase("Yes") || absnt.equalsIgnoreCase("No"))) {
        System.out.print("Invalid input. Please enter 'Yes' or 'No': ");
        absnt = sc.next();
    }

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
                    System.out.print("\tInvalid Time In. Please enter a time between 08:00 and 22:00.");
                    timeIn = null;
                }
            } catch (DateTimeParseException e) {
                System.out.print("\tInvalid format. Please enter time in HH:mm format.");
            }
        }

        LocalTime timeOut = null;
        while (timeOut == null) {
            try {
                System.out.print("Time Out (HH:mm): ");
                timeout = sc.next(); 
                timeOut = LocalTime.parse(timeout, DateTimeFormatter.ofPattern("HH:mm"));

          
                if (timeOut.isBefore(minTimeIn) || timeOut.isAfter(maxTimeIn)) {
                    System.out.print("\tInvalid Time Out. Please enter a time between 08:00 and 22:00.");
                    timeOut = null;  
                }

   
                if (timeout.equals("24:00")) {
                    timeOut = LocalTime.of(0, 0);
                }

            } catch (DateTimeParseException e) {
                System.out.print("\tInvalid format. Please enter time in (HH:mm | 24 HOUR) format.");
            }
        }

        Duration workedDuration = Duration.between(timeIn, timeOut);
        h_worked = (int) workedDuration.toHours();
        int remainingMinutes = (int) workedDuration.toMinutes() % 60;


        if (h_worked < 0) {
            System.out.print("\tTime Out cannot be earlier than Time In. Please check the times entered.");
            return;
        }

        System.out.print("Total Hours Worked: " + h_worked + " hours and " + remainingMinutes + " minutes");

        int standardHours = 8;
        if (h_worked > standardHours) {
            ovTime = h_worked - standardHours;
        }

        System.out.println("Total Over Time Hours: " + ovTime);
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
    
    public void viewEmployeesv2(){
        config cfg = new config();
        
        String emp_dtls = "select * from tbl_employees";
        String[] emp_hdrs = {"ID", "FIRST NAME", "LAST NAME"};
         String[] emp_clmn = {"emp_id", "emp_fname", "emp_lname"};
        cfg.viewRecordsEmp(emp_dtls, emp_hdrs, emp_clmn);
        
    }
    public double getSingleValuev2(String sql, Object... params) {
    double result = 0.0;
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set all the parameters in the prepared statement dynamically
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
   
 public int viewRecordsForUpdate() {
     
    config cfg = new config();
    Scanner sc = new Scanner(System.in);
    validation val = new validation();

    try {

        System.out.print("Enter the Employee's ID you want to Sort: ");
        String idInput = sc.nextLine();
        int id = val.validateint(idInput);

        while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0) {
            System.out.print("\tERROR: ID doesn't exist, try again: ");
            idInput = sc.nextLine();
            id = val.validateint(idInput);
        }

                 String month = validateMonthInput(sc);
   
        String recordQuery = "SELECT * FROM DailyTimeRecords WHERE employee_id = ? AND month = ?";
        String[] dtrHeaders = {
            "Record ID", "Employee ID", "Entry Date", "Time In",
            "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"
        };
        String[] dtrColumns = {
            "record_id", "employee_id", "entry_date", "time_in",
            "time_out", "month", "hours_worked", "overtime_hrs", "absent"
        };

        boolean[] foundFlag = {false};
        cfg.viewRecordsV2(recordQuery, dtrHeaders, dtrColumns, id, month, foundFlag);

       
        if (!foundFlag[0]) {
            return -1; 
        }

        
        System.out.print("Enter the Record ID you want to Edit: ");
        String idInputForRecord = sc.nextLine();
        int recordId = val.validateint(idInputForRecord);

      
        while (getSingleValue("SELECT record_id FROM DailyTimeRecords WHERE record_id = ?", recordId) == 0) {
            System.out.print("\tERROR: Record ID doesn't exist, try again: ");
            idInputForRecord = sc.nextLine();
            recordId = val.validateint(idInputForRecord);
        }

        return recordId;

    } catch (Exception e) {
        System.out.println("An error occurred. Please try again.");
        return -1;
    }
}

    

   
    public boolean viewRecords() {
     config cfg = new config();
     Scanner sc = new Scanner(System.in);
     validation val = new validation();
     
     

     try {

         System.out.print("Enter the Employee's ID you want to Sort: ");
         String idInput = sc.nextLine();
         int id = val.validateint(idInput);

         while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0) {
             System.out.print("\tERROR: ID doesn't exist, try again: ");
             idInput = sc.nextLine();
             id = val.validateint(idInput);
             
         }
         
         String month = validateMonthInput(sc);

         String recordQuery = "SELECT * FROM DailyTimeRecords WHERE employee_id = ? AND month = ?";
         String[] dtrHeaders = {
             "Record ID", "Employee ID", "Entry Date", "Time In",
             "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"
         };
         String[] dtrColumns = {
             "record_id", "employee_id", "entry_date", "time_in",
             "time_out", "month", "hours_worked", "overtime_hrs", "absent"
         };


         boolean[] foundFlag = {false};
         cfg.viewRecordsV2(recordQuery, dtrHeaders, dtrColumns, id, month, foundFlag);


         if (!foundFlag[0]) {
             System.out.println("No records found. Returning to the main menu...");
             return false;
         }

         return true; 
     } catch (Exception e) {
         e.printStackTrace();
         System.out.println("An error occurred while fetching records. Please try again.");
         return false;
     }
 }



    
   private static String validateMonthInput(Scanner sc) {
        String month;
        String[] validMonths = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", 
                                 "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

        while (true) {
            System.out.print("Enter the Month you want to Sort (Must be Uppercase): ");
            month = sc.nextLine().trim();  

     
            if (month.matches("[A-Z]+") && isValidMonth(month, validMonths)) {
                return month; 
            } else {
                System.out.println("Invalid input. Please enter a valid month (uppercase letters only).");
            }
        }
    }

    private static boolean isValidMonth(String month, String[] validMonths) {
        for (String validMonth : validMonths) {
            if (validMonth.equals(month)) {
                return true; 
            }
        }
        return false;  
    }


 public boolean updateRecord() {
        config cfg = new config();
        Scanner sc = new Scanner(System.in);
        validation val = new validation();


        int recordId = viewRecordsForUpdate();
        if (recordId == -1) {
            System.out.println("No records found. Returning to the main menu...");
            return false;
        }

        try {
            String timein = "", timeout = "", absent = "";
            int h_worked = 0, ovTime = 0;

            System.out.print("Is the Employee Absent? (Yes || No): ");
            String absnt = sc.next();
            while (!(absnt.equalsIgnoreCase("Yes") || absnt.equalsIgnoreCase("No"))) {
                System.out.print("Invalid input. Please enter 'Yes' or 'No': ");
                absnt = sc.next();
            }


            if (absnt.equalsIgnoreCase("Yes")) {
                timein = "";
                timeout = "";
                h_worked = 0;
                ovTime = 0;
                absent = "Yes";
            } else {
                absent = "No";

                LocalTime minTimeIn = LocalTime.of(8, 0);
                LocalTime maxTimeIn = LocalTime.of(22, 0);
                LocalTime timeIn = null;

                while (timeIn == null) {
                    try {
                        System.out.print("Time In (HH:mm): ");
                        timein = sc.next();
                        timeIn = LocalTime.parse(timein, DateTimeFormatter.ofPattern("HH:mm"));

                        if (timeIn.isBefore(minTimeIn) || timeIn.isAfter(maxTimeIn)) {
                            System.out.print("\tInvalid Time In. Please enter a time between 08:00 and 22:00.");
                            timeIn = null;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.print("\tInvalid format. Please enter time in HH:mm format.");
                    }
                }

                LocalTime timeOut = null;
                while (timeOut == null) {
                    try {
                        System.out.print("Time Out (HH:mm): ");
                        timeout = sc.next();
                        timeOut = LocalTime.parse(timeout, DateTimeFormatter.ofPattern("HH:mm"));

                        if (timeOut.isBefore(minTimeIn) || timeOut.isAfter(maxTimeIn)) {
                            System.out.print("\tInvalid Time Out. Please enter a time between 08:00 and 22:00.");
                            timeOut = null;
                        }

                        if (timeout.equals("24:00")) {
                            timeOut = LocalTime.of(0, 0);
                        }
                    } catch (DateTimeParseException e) {
                        System.out.print("\tInvalid format. Please enter time in (HH:mm | 24 HOUR) format.");
                    }
                }

                Duration workedDuration = Duration.between(timeIn, timeOut);
                h_worked = (int) workedDuration.toHours();
                int remainingMinutes = (int) workedDuration.toMinutes() % 60;

                if (h_worked < 0) {
                    System.out.print("\tTime Out cannot be earlier than Time In. Please check the times entered.");
                    return false;
                }

                System.out.println("Total Hours Worked: " + h_worked + " hours and " + remainingMinutes + " minutes");

                int standardHours = 8;
                if (h_worked > standardHours) {
                    ovTime = h_worked - standardHours;
                }

                System.out.println("Total Over Time Hours: " + ovTime);
            }

            String sqlUpdate = "UPDATE DailyTimeRecords SET absent = ?, time_in = ?, time_out = ?, hours_worked = ?, overtime_hrs = ? WHERE record_id = ?";
            cfg.updateEmployee(sqlUpdate, absent, timein, timeout, h_worked, ovTime, recordId);

            System.out.println("");
            return true;

        } catch (Exception e) {
            System.out.println("An error occurred during the update: " + e.getMessage());
            return false;
        }
    }

 
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

 public int viewRecordsForDelete() {
    config cfg = new config();
    Scanner sc = new Scanner(System.in);
    validation val = new validation();

    try {

        System.out.print("Enter the Employee's ID you want to Sort: ");
        String idInput = sc.nextLine();
        int id = val.validateint(idInput);


        while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0) {
            System.out.print("\tERROR: ID doesn't exist, try again: ");
            idInput = sc.nextLine();
            id = val.validateint(idInput);
        }

        String month = validateMonthInput(sc);

        String recordQuery = "SELECT * FROM DailyTimeRecords WHERE employee_id = ? AND month = ?";
        int recordCount = (int) getSingleValue(recordQuery, id, month);

        if (recordCount == 0) {
            System.out.println("No records found for this employee in " + month);
            return -1;
        }

        String[] dtrHeaders = {
            "Record ID", "Employee ID", "Entry Date", "Time In", 
            "Time Out", "Month", "Hours Worked", "Overtime Hours", "Absent Status"
        };
        String[] dtrColumns = {
            "record_id", "employee_id", "entry_date", "time_in", 
            "time_out", "month", "hours_worked", "overtime_hrs", "absent"
        };

        boolean[] foundFlag = {false};
        cfg.viewRecordsV2(recordQuery, dtrHeaders, dtrColumns, id, month, foundFlag);

        if (!foundFlag[0]) {
            return -1;
        }

        System.out.print("Enter the Record ID you want to Delete: ");
        String idInputForRecord = sc.nextLine();
        int recordId = val.validateint(idInputForRecord);
        
         while (getSingleValue("SELECT record_id FROM DailyTimeRecords WHERE record_id = ?", recordId) == 0) {
            System.out.print("\tERROR: Employee ID doesn't exist. Please try again: ");
            idInput = sc.nextLine();
            recordId = val.validateint(idInput);
        }
        
        while (getSingleValue("SELECT record_id FROM DailyTimeRecords WHERE record_id = ?", recordId) == 0) {
            System.out.print("\tERROR: Record ID doesn't exist, try again: ");
            idInputForRecord = sc.nextLine();
            recordId = val.validateint(idInputForRecord);
        }

        return recordId;

    } catch (Exception e) {
        System.out.println("An error occurred. Please try again.");
        return -1;
    }
}

 
 
 
 public void deleteRecord() {
    Scanner sc = new Scanner(System.in);
    config dbConfig = new config();
    validation val = new validation();

    int recordId = viewRecordsForDelete();
    if (recordId == -1) {
        
        return;
    }

    try {
      
        System.out.print("Enter the Employee ID: ");
        String idInput = sc.nextLine();
        int empId = val.validateint(idInput);


        while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", empId) == 0) {
            System.out.print("\tERROR: Employee ID doesn't exist. Please try again: ");
            idInput = sc.nextLine();
            empId = val.validateint(idInput);
        }


        String checkRecordQuery = "SELECT COUNT(*) FROM DailyTimeRecords WHERE record_id = ? AND employee_id = ?";
        int recordExists = (int) getSingleValue(checkRecordQuery, recordId, empId);


        if (recordExists == 0) {
            System.out.println("ERROR: Record ID does not belong to this Employee ID. Deletion aborted.");
            return;
        }

     
        String sqlDelete = "DELETE FROM DailyTimeRecords WHERE record_id = ? AND employee_id = ?";
        dbConfig.deleteEmployees(sqlDelete, recordId, empId); 

        
    } catch (Exception e) {
        System.out.println("An error occurred while deleting the record.");
        e.printStackTrace(); 
    }
}

 public int getSingleValueInt(String sql, Object... params) {
    int result = 0; 
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        setPreparedStatementValues(pstmt, params);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            result = rs.getInt(1); 
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving single value: " + e.getMessage());
    }
    return result;
}

    public void DailyTimeRecord(){
        
        Scanner sc = new Scanner(System.in);
        String choice;
        employees use = new employees();
        validation val = new validation();
        boolean selected = false;
        
        do{
        
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*             Daily Time Record               *");
            System.out.println("***********************************************");
            System.out.println();
            System.out.println("1. Import Daily Time Record");
            System.out.println("2. View Daily Time Records");
            System.out.println("3. Edit Daily Time Record");
            System.out.println("4. Delete Daily Time Record");
            System.out.println("5. Exit");
            System.out.println();
            System.out.println("***********************************************");
            System.out.println();
            
            
            System.out.print("Enter Action: ");
            int actn = val.validateChoice();
           
           
        switch(actn){
            
            case 1:
            
                 String DataFilePath = "D:\\College\\MySys\\IT2C-TAPALES-HRS\\src\\DailyTimeRecords\\DailyTimeRecords.csv";
                 importDataToDatabase(DataFilePath);
//                viewEmployeesv2();
//                System.out.print("\n\n");
//                AddDTR();
                break;
            
            case 2:
                
                viewEmployeesv2();
                viewRecords();
                break;
               
            case 3:
            viewEmployeesv2();
            if (!updateRecord()) {
                System.out.print("");
            }
            break;
                
            case 4:
                
                viewEmployeesv2();
                deleteRecord();
                break;
                
            case 5:
                selected = true;
                System.out.println("Going back . . .");
                break;
                
            default:
                
                System.out.println("Invalid Selection!");
                    
        }

        }while(!selected);
        
        return;
     
    }
    
}
