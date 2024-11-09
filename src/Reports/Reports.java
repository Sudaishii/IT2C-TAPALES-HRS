package Reports;

import DailyTimeRecords.Record;
import EmpApp.config;
import employees.employees;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Reports {

    config cfg = new config();
    
    // Method to connect to the database
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
  

   public void viewGeneratedReports() {
       
       Scanner sc = new Scanner(System.in);
      
        System.out.println("\nFilter Reports:");
        System.out.println("1. By Employee ID");
        System.out.println("2. By Month");
        System.out.println("3. All Reports");
        System.out.print("Choose (1/2/3): ");
        int choice = sc.nextInt();
        sc.nextLine(); 
       
        System.out.println("\n");
        
       String query = "SELECT r.report_id, r.emp_id, e.emp_fname, e.emp_lname, r.date_generated, r.net_pay, r.status, r.month "
                 + "FROM reports r "
                 + "JOIN tbl_employees e ON r.emp_id = e.emp_id ";
              
    if (choice == 1) {
        // Filter by Employee ID
        System.out.print("Enter Employee ID: ");
        int empId = sc.nextInt();
        query += "WHERE r.emp_id = " + empId + " ";
    } else if (choice == 2) {
        System.out.print("Enter Month (Must be Uppercase: ");
        String month = sc.nextLine();
        query += "WHERE r.month = '" + month + "' ";
    }
 
        query += "ORDER BY r.date_generated DESC"; 
   
    try (Connection con = connectDB(); 
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {
        
        System.out.println("**************************************************");
        System.out.println("         LIST OF GENERATED REPORTS               ");
        System.out.println("**************************************************");
        System.out.println("   EMP ID  |         NAME              |   MONTH   |   STATUS  |   NET PAY   | DATE GENERATED ");
        System.out.println("-----------|---------------------------|-----------|-----------|-------------|----------------");

        while (rs.next()) {
            String empId = rs.getString("emp_id");
            String empName = rs.getString("emp_fname") + " " + rs.getString("emp_lname");
            String dateGenerated = rs.getString("date_generated");
            double netPay = rs.getDouble("net_pay");
            String status = rs.getString("status");
            String month = rs.getString("month"); 
            
            System.out.printf("%-11s| %-26s| %-10s| %-10s| %-12.2f| %-19s%n", 
                              empId, empName, month, status, netPay, dateGenerated);
        }

        System.out.println("**************************************************");

    } catch (SQLException e) {
        System.out.println("Error retrieving reports: " + e.getMessage());
    }
}





    
    

  
    public void generateReport(Connection con) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Get employee ID and month from the user
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter Month: ");
        String month = scanner.nextLine();

        int year = LocalDate.now().getYear();

        // Deduction rates
        final double SSS_RATE = 0.045;
        final double PHILHEALTH_RATE = 0.025;
        final double PAGIBIG_FIXED = 200;

        String status = "Approved";  // Set the report status to Approved

        // SQL query to select the employee's data
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

        
        String insertQuery = "INSERT INTO reports (emp_id, month, year, total_hours, total_ovtime, gross_salary, "
        + "sss, philhealth, pagibig, t_deductions, overtime_pay, net_pay, status, date_generated) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
             PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

            // Set parameters for the select statement
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
            
            String currentDate = LocalDate.now().toString();
            while (rs.next()) {
                    // Calculations for deductions and net pay
                    double sssDeduction = rs.getDouble("sss_deduction");
                    double philhealthDeduction = rs.getDouble("philhealth_deduction");
                    double pagibigDeduction = rs.getDouble("pagibig_deduction");
                    double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;

                    // Set parameters for the insert statement, including the date_generated
                    insertStmt.setInt(1, rs.getInt("employee_id"));
                    insertStmt.setString(2, rs.getString("month"));
                    insertStmt.setString(3, rs.getString("year"));
                    insertStmt.setInt(4, rs.getInt("total_hours"));
                    insertStmt.setInt(5, rs.getInt("total_ovtime"));
                    insertStmt.setDouble(6, rs.getDouble("gross_salary"));
                    insertStmt.setDouble(7, sssDeduction);
                    insertStmt.setDouble(8, philhealthDeduction);
                    insertStmt.setDouble(9, pagibigDeduction);
                    insertStmt.setDouble(10, totalDeductions);
                    insertStmt.setDouble(11, rs.getDouble("overtime_pay"));
                    insertStmt.setDouble(12, rs.getDouble("net_pay"));
                    insertStmt.setString(13, status);
                    insertStmt.setString(14, currentDate); // Add current date

                    insertStmt.executeUpdate();
                }
        }
    }

    public void Report() {
        Scanner sc = new Scanner(System.in); 
        String choice;

        Connection con = connectDB();
        if (con == null) {
            System.out.println("Failed to connect to the database.");
            return;
        }

        do {
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*               Reports Menu                  *");
            System.out.println("***********************************************");
            System.out.println();

            System.out.println("1. Generate Reports");
            System.out.println("2. View Reports");
            System.out.println("3. Approve or Cancel Request");
            System.out.println("4. Exit");
            System.out.println("***********************************************");
            System.out.println();

            System.out.print("Enter Action: ");
            int actn = sc.nextInt();
            sc.nextLine(); 

            switch (actn) {
                case 1:
                    try {
                        
                        Record rcrd = new Record();
                        rcrd.viewEmployeesv2();
                        generateReport(con);
                        System.out.println("Generated Sucessfully . . .");
                    } catch (SQLException e) {
                        System.out.println("Error while generating report: " + e.getMessage());
                    }
                    break;

                case 2:
                    viewGeneratedReports();
                    break;

                case 3:
                    
                    break;

                case 4:
                    System.out.println("Going back to main menu . . .");
                    break;

                default:
                    System.out.println("Error: Invalid selection.");
            }

            System.out.print("Do you want to continue (y/n): ");
            choice = sc.next();

        } while (choice.equals("Y") || choice.equals("y"));

       
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Failed to close the connection: " + e.getMessage());
        }
    }
}
