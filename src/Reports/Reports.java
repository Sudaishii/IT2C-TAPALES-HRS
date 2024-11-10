    package Reports;

    import DailyTimeRecords.Record;
    import EmpApp.config;
import static EmpApp.config.connectDB;
import EmpApp.validation;
    import employees.employees;
    import java.sql.*;
import java.time.DayOfWeek;
    import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashSet;
    import java.util.Scanner;
import java.util.Set;

    public class Reports {

        config cfg = new config();

        public static Connection connectDB() {
            Connection con = null;
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:sheed.db");
            } catch (Exception e) {
                System.out.println("Connection Failed: " + e);
            }
            return con;
        }
        
   public void indivPayroll() {
    Scanner sc = new Scanner(System.in);

   
    System.out.print("Enter Report ID: ");
    String reportId = sc.nextLine();

       System.out.println("");
    
    try (Connection con = connectDB()) {
        if (con == null) {
            System.out.println("Failed to connect to the database.");
            return;
        }

   
        String query = "SELECT r.*, e.emp_fname, e.emp_lname, e.emp_dept, e.emp_rate "
                     + "FROM Reports r "
                     + "JOIN tbl_employees e ON r.emp_id = e.emp_id "
                     + "WHERE r.report_id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, reportId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                
                String firstName = rs.getString("emp_fname");
                String lastName = rs.getString("emp_lname");
                String employeeId = rs.getString("emp_id");
                String department = rs.getString("emp_dept");
                double rate = rs.getDouble("emp_rate");
                String month = rs.getString("month");
                String year = rs.getString("year");
                double totalHours = rs.getDouble("total_hours");
                double totalOvertime = rs.getDouble("total_ovtime");
                double initialSalary = rs.getDouble("gross_salary");
                double sss = rs.getDouble("sss");
                double philHealth = rs.getDouble("philhealth");
                double pagIbig = rs.getDouble("pagibig");
                double totalDeductions = rs.getDouble("t_deductions");
                double overtimePay = rs.getDouble("overtime_pay");
                double netPay = rs.getDouble("net_pay");
                String dateGenerated = rs.getString("date_generated");

              
                System.out.println("***************************************************************************");
                System.out.println("                            PAYROLL STATEMENT                             ");
                System.out.println("***************************************************************************");
                System.out.printf("%-20s: %-30s %-5s: %-20s%n", "Report", reportId, "Date", dateGenerated);
                System.out.printf("%-20s: %-30s%n", "Name", firstName + " " + lastName);
                System.out.printf("%-20s: %-30s%n", "Employee", employeeId);
                System.out.printf("%-20s: %-30s%n", "Department", department);
                System.out.printf("%-20s: %-30s", "Period", month);
                System.out.println("\n---------------------------------------------------------------------------");
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Rate", "", rate);
                System.out.printf("%-20s: %-30s %.2f%n", "Total Hours Worked", "", totalHours);
                System.out.printf("%-20s: %-30s %.2f%n", "Overtime Hours", "", totalOvertime);
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Initial Salary", "", initialSalary);
                System.out.println("---------------------------------------------------------------------------");
                System.out.println("Deductions: ");
                System.out.printf("   %-17s: %-30s PHP %.2f%n", "SSS", "", sss);
                System.out.printf("   %-17s: %-30s PHP %.2f%n", "PhilHealth", "", philHealth);
                System.out.printf("   %-17s: %-30s PHP %.2f%n", "Pag-IBIG", "", pagIbig);
                System.out.printf("   %-17s: %-30s PHP %.2f%n", "Total Deductions", "", totalDeductions);
                System.out.println("---------------------------------------------------------------------------");
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Overtime Pay", "", overtimePay);
                System.out.println("---------------------------------------------------------------------------");
                System.out.printf("%-20s: %-30s PHP %.2f%n", "Net Pay", "", netPay);
                System.out.println("************************************************************************");

            } else {
                System.out.println("No report data found for the given Report ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving employee payroll data: " + e.getMessage());
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
   
   public String selectDepartment() {
    Scanner sc = new Scanner(System.in);

    String dept = "";
    int deptChoice;

    while (true) {
        System.out.println("Select Department:");
        System.out.println("1. Finance");
        System.out.println("2. Marketing");
        System.out.println("3. Sales");
        System.out.println("4. Executive");
        System.out.println("5. Information Technology");
        System.out.println("6. Customer Service");
        System.out.println("7. Administrative");
        System.out.println("8. Product Management");
        System.out.println("9. Legal");

        System.out.print(": ");
        
        if (sc.hasNextInt()) {
            deptChoice = sc.nextInt();
            sc.nextLine(); 
            switch (deptChoice) {
                case 1: dept = "Finance"; break;
                case 2: dept = "Marketing"; break;
                case 3: dept = "Sales"; break;
                case 4: dept = "Executive"; break;
                case 5: dept = "Information Technology"; break;
                case 6: dept = "Customer Service"; break;
                case 7: dept = "Administrative"; break;
                case 8: dept = "Product Management"; break;
                case 9: dept = "Legal"; break;
                default: 
                    System.out.println("Invalid department choice, please try again.");
                    continue; 
            }
            break;
        } else {
            System.out.println("\tERROR: Please enter a valid number between 1 and 9.");
            sc.nextLine(); 
        }
    }

    return dept; 
}
   
   

    public void viewGeneratedReports() {

    Scanner sc = new Scanner(System.in);
    validation val = new validation();

    System.out.println("\nFilter Reports:");
    System.out.println("1. By Employee ID");
    System.out.println("2. By Month");
    System.out.println("3. By Department");
    System.out.println("4. All Reports");
    System.out.print("Choose (1/2/3/4): ");
    
    String month;
    int choice = val.validateChoice();
    while (choice < 1 || choice > 4) {
        System.out.print("\tERROR: Invalid choice, please choose (1-4): ");
        choice = val.validateChoice();
    }

    String query = "SELECT r.report_id, r.emp_id, e.emp_fname, e.emp_lname, e.emp_dept, r.date_generated, r.net_pay, r.status, r.month "
                 + "FROM reports r "
                 + "JOIN tbl_employees e ON r.emp_id = e.emp_id ";

    if (choice == 1) {
        Record rcrd = new Record();
        rcrd.viewEmployeesv2();
        
        System.out.print("Enter Employee ID: ");
         String empIdInput = sc.nextLine();
        int empId = val.validateint(empIdInput);

        while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", empId) == 0) {
            System.out.print("\tERROR: ID doesn't exist, try again: ");
            empIdInput = sc.nextLine();
            empId = val.validateint(empIdInput);
        }
    } 
  
    else if (choice == 2) {
         month = validateMonthInput(sc);
        query += "WHERE r.month = '" + month + "' ";
    } 
  
    else if (choice == 3) {
        
        String dept = selectDepartment();
        query += "WHERE e.emp_dept = '" + dept + "' ";
        
        System.out.print("Do you want to filter by Month? (y/n): ");
        String filterByMonth = sc.nextLine().toLowerCase();
        if (filterByMonth.equals("y")) {
          
           month = validateMonthInput(sc);
            query += "AND r.month = '" + month + "' ";
        }
    }

    query += "ORDER BY r.date_generated DESC"; 

    try (Connection con = connectDB(); 
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {
        System.out.println("\n\n");
        System.out.println("****************************************************");
        System.out.println("             LIST OF GENERATED REPORTS              ");
        System.out.println("****************************************************");
        System.out.println(" REPORT ID | EMP ID  |           NAME            |            DEPARTMENT           |   MONTH   |   STATUS  |   NET PAY   | DATE GENERATED ");
        System.out.println("-----------|---------|---------------------------|---------------------------------|-----------|-----------|-------------|----------------");

        while (rs.next()) {
            
            int reportId = rs.getInt("report_id");
            String empId = rs.getString("emp_id");
            String empName = rs.getString("emp_fname") + " " + rs.getString("emp_lname");
            String dept = rs.getString("emp_dept");
            String dateGenerated = rs.getString("date_generated");
            double netPay = rs.getDouble("net_pay");
            String status = rs.getString("status");
            month = rs.getString("month"); 

           
            System.out.printf("%-11d| %-8s| %-26s| %-32s| %-10s| %-10s| %-12.2f| %-19s%n", 
                              reportId, empId, empName, dept, month, status, netPay, dateGenerated);
        }

        System.out.println("****************************************************");
        
        System.out.println("\n\n");
        
    } catch (SQLException e) {
        System.out.println("Error retrieving reports: " + e.getMessage());
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
    
  

private static String validateMonthInput(Scanner sc) {
    String month;
    String[] validMonths = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
                            "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    int currentMonthIndex = currentDate.getMonthValue() - 1; 

    while (true) {
        System.out.print("Enter the Month you want to Sort (Must be Uppercase): ");
        month = sc.nextLine().trim();

      
        if (!month.matches("[A-Z]+")) {
            System.out.println("Invalid input. Please enter a valid month using uppercase letters only.");
            continue;
        }


        if (!isValidMonth(month, validMonths)) {
            System.out.println("Invalid month. Please enter a valid month (e.g., JANUARY).");
            continue;
        }

        int enteredMonthIndex = getMonthIndex(month, validMonths);

        if (enteredMonthIndex > currentMonthIndex) {
            System.out.println("The entered month is in the future. Please enter a valid past or current month.");
            continue;
        }

     
        if (enteredMonthIndex == currentMonthIndex) {
         
            YearMonth currentYearMonth = YearMonth.of(currentYear, currentDate.getMonthValue());
            int totalDaysInMonth = currentYearMonth.lengthOfMonth();

            if (currentDate.getDayOfMonth() != totalDaysInMonth) {
                System.out.println("The current month is not yet finished. Please wait until the month ends.");
                continue;
            }
        }

     
        return month;
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

private static int getMonthIndex(String month, String[] validMonths) {
    for (int i = 0; i < validMonths.length; i++) {
        if (validMonths[i].equals(month)) {
            return i;
        }
    }
    return -1;
}



public void generateReport(Connection con) throws SQLException {
    Scanner sc = new Scanner(System.in);
    validation val = new validation();

    System.out.print("Enter Employee ID: ");
    String idInput = sc.nextLine();
    int id = val.validateint(idInput);

    while (getSingleValue("SELECT emp_id FROM tbl_employees WHERE emp_id = ?", id) == 0) {
        System.out.print("\tERROR: ID doesn't exist, try again: ");
        idInput = sc.nextLine();
        id = val.validateint(idInput);
    }

    String month = validateMonthInput(sc);
    int year = LocalDate.now().getYear();

    if (!areAllWeekdaysCovered(con, id, month, year)) {
        System.out.println("The DTR entries for all weekdays in " + month + " " + year + " are incomplete.");
        return; 
    }

    final double SSS_RATE = 0.045;
    final double PHILHEALTH_RATE = 0.025;
    final double PAGIBIG_FIXED = 200;

    String status = "Approved";
    String checkReportQuery = "SELECT 1 FROM reports WHERE emp_id = ? AND month = ? AND year = ?";
    String selectQuery = "SELECT dtr.employee_id, dtr.month, strftime('%Y', dtr.entry_date) AS year, "
            + "SUM(dtr.hours_worked) AS total_hours, "
            + "SUM(dtr.overtime_hrs) AS total_ovtime, "
            + "SUM(dtr.hours_worked) * e.emp_rate AS gross_salary, "
            + "(SUM(dtr.hours_worked) * e.emp_rate * ?) AS sss_deduction, "
            + "(SUM(dtr.hours_worked) * e.emp_rate * ?) AS philhealth_deduction, "
            + "? AS pagibig_deduction, "
            + "SUM(dtr.overtime_hrs) * (e.emp_rate * 1.5) AS overtime_pay, "
            + "(SUM(dtr.hours_worked) * e.emp_rate + SUM(dtr.overtime_hrs) * (e.emp_rate * 1.5)) - "
            + "(SUM(dtr.hours_worked) * e.emp_rate * ? + SUM(dtr.hours_worked) * e.emp_rate * ? + ?) AS net_pay "
            + "FROM DailyTimeRecords dtr JOIN tbl_employees e ON dtr.employee_id = e.emp_id "
            + "WHERE dtr.employee_id = ? AND dtr.month = ? AND strftime('%Y', dtr.entry_date) = ? "
            + "GROUP BY dtr.employee_id, dtr.month, strftime('%Y', dtr.entry_date)";

    try (PreparedStatement checkStmt = con.prepareStatement(checkReportQuery);
         PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {

        checkStmt.setInt(1, id);
        checkStmt.setString(2, month);
        checkStmt.setInt(3, year);

        ResultSet checkResult = checkStmt.executeQuery();
        if (checkResult.next()) {
            System.out.println("A report for Employee ID " + id + " already exists for the month " + month + ".");
            return;
        }

        selectStmt.setDouble(1, SSS_RATE);
        selectStmt.setDouble(2, PHILHEALTH_RATE);
        selectStmt.setDouble(3, PAGIBIG_FIXED);
        selectStmt.setDouble(4, SSS_RATE);
        selectStmt.setDouble(5, PHILHEALTH_RATE);
        selectStmt.setDouble(6, PAGIBIG_FIXED);
        selectStmt.setInt(7, id);
        selectStmt.setString(8, month);
        selectStmt.setString(9, Integer.toString(year));

        ResultSet rs = selectStmt.executeQuery();
        if (rs.next()) {
            System.out.println("Report generated successfully...");
        } else {
            System.out.println("No records found for report generation.");
        }
    }
}

        private boolean areAllWeekdaysCovered(Connection con, int empId, String month, int year) throws SQLException {
    YearMonth yearMonth = YearMonth.of(year, Month.valueOf(month.toUpperCase()).getValue());
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();

   
    String dtrQuery = "SELECT entry_date, time_in, time_out, absent " +
                      "FROM DailyTimeRecords " +
                      "WHERE employee_id = ? AND month = ? AND strftime('%Y', entry_date) = ?";
    
    Set<LocalDate> recordedDates = new HashSet<>();
    
    try (PreparedStatement stmt = con.prepareStatement(dtrQuery)) {
        stmt.setInt(1, empId);
        stmt.setString(2, month.toUpperCase());
        stmt.setString(3, Integer.toString(year));

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            LocalDate entryDate = LocalDate.parse(rs.getString("entry_date"));
            String timeIn = rs.getString("time_in");
            String timeOut = rs.getString("time_out");
            String absent = rs.getString("absent");

           
            if (absent.equalsIgnoreCase("No") && timeIn != null && timeOut != null) {
                recordedDates.add(entryDate);
            }
        }
    }

   
    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            
            if (!recordedDates.contains(date)) {
                System.out.println("Missing or incomplete DTR entry for " + date + " (" + dayOfWeek + ")");
                return false;
            }
        }
    }

    return true; 
}
        
         public void deleteReport() {
             
         Scanner sc = new Scanner(System.in);
         config cfg = new config();
             
          System.out.println("\nFilter Reports:");
          System.out.println("1. By Employee ID");
          System.out.println("2. By Month");
          System.out.println("3. By Department");
          System.out.println("4. All Reports");
          System.out.print("Choose (1/2/3/4): ");
          int choice = sc.nextInt();
          sc.nextLine(); 
      
          String query = "SELECT r.*, e.emp_fname, e.emp_lname, e.emp_dept, e.emp_rate "
                     + "FROM Reports r "
                     + "JOIN tbl_employees e ON r.emp_id = e.emp_id "
                     + "WHERE r.report_id = ?";
        
        
         if (choice == 1) {
             Record rcrd = new Record();
             rcrd.viewEmployeesv2();
             System.out.print("Enter Employee ID: ");
             int empId = sc.nextInt();
             query += "WHERE r.emp_id = " + empId + " ";
         } 

         else if (choice == 2) {
             System.out.print("Enter Month (Must be Uppercase): ");
             String month = sc.nextLine();
             query += "WHERE r.month = '" + month + "' ";
         } 

         else if (choice == 3) {
             System.out.print("Enter Department: ");
             String dept = sc.nextLine();
             query += "WHERE e.emp_dept = '" + dept + "' ";

             System.out.print("Do you want to filter by Month? (y/n): ");
             String filterByMonth = sc.nextLine().toLowerCase();
             if (filterByMonth.equals("y")) {
                 System.out.print("Enter Month (Must be Uppercase): ");
                 String month = sc.nextLine();
                 query += "AND r.month = '" + month + "' ";
             }
         }

         query += "ORDER BY r.date_generated DESC"; 
        
        System.out.print("Enter the Report ID you want to delete: ");
        int id = sc.nextInt();
        
        String sqlDelete = "DELETE FROM reports WHERE report_id = ?";

        
        cfg.deleteEmployees(sqlDelete, id);
    }

         
        
         
         
         
        public void Report() {
            
            validation val = new validation();
            Scanner sc = new Scanner(System.in); 
            String choice;
            boolean selected = false;
            
            

            Connection con = connectDB();
            if (con == null) {
                System.out.println("Failed to connect to the database.");
                return;
            }

            do {

                System.out.println();
                System.out.println("***********************************************");
                System.out.println("*               REPORTS MANAGEMENT           *");
                System.out.println("***********************************************");
                System.out.println();

                System.out.println("1. Generate New Reports");
                System.out.println("2. View All Reports");
                System.out.println("3. View Individual Report");
                System.out.println("4. Delete Report");
                System.out.println("5. Back");
                System.out.println("***********************************************");
                System.out.println();


                System.out.print("Enter Action: ");
                int actn = val.validateChoice();
                

                switch (actn) {
                    case 1:
                        try {

                            Record rcrd = new Record();
                            rcrd.viewEmployeesv2();
                            generateReport(con);
                        } catch (SQLException e) {
                            System.out.println("Error while generating report: " + e.getMessage());
                        }
                        break;

                    case 2:
                        viewGeneratedReports();
                        break;

                    case 3:
                        
                        viewGeneratedReports();
                        indivPayroll();
                        break;

                    case 4:
                        
                        deleteReport();
                        break;
                          
                    case 5:
                        selected = true;
                        System.out.println("Going back to main menu . . .");
                        return;

                    default:
                        
                         System.out.println("Invalid Selection!");
                         System.out.println("Going Back to the Main Menu . . .");
                         
                }

              
            } while (!selected);


            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }
