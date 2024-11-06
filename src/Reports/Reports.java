/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import EmpApp.config;
import employees.employees;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Snezhy
 */
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Reports {
    
    config cfg = new config();
    
    public void GeneralReports() {
    String sqlQuery = "SELECT r.report_id, r.emp_id, r.month, r.year, r.total_hours, "
                    + "r.total_ovtime, r.initial_salary, r.deductions, r.overtime_pay, "
                    + "r.net_pay, r.status, e.emp_fname, e.emp_lname, e.emp_rate "
                    + "FROM Reports r "
                    + "INNER JOIN tbl_employees e ON r.emp_id = e.emp_id";  
    
    String[] columnHeaders = {
        "Report ID", "Employee ID", "First Name", "Last Name", "Month", "Year", 
        "Total Hours", "Total Overtime", "Initial Salary", "Deductions", 
        "Overtime Pay", "Net Pay", "Rate", "Status"
    };
    
    String[] columnNames = {
        "report_id", "emp_id", "emp_fname", "emp_lname", "month", "year", 
        "total_hours", "total_ovtime", "initial_salary", "deductions", 
        "overtime_pay", "net_pay", "emp_rate", "status"
    };

    cfg.viewRecords(sqlQuery, columnHeaders, columnNames);
}
    

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

    public void ohaha(Connection con) throws SQLException {
    Scanner scanner = new Scanner(System.in);

    // Get user inputs for employee ID and month
    System.out.print("Enter Employee ID: ");
    int id = scanner.nextInt();
    scanner.nextLine();  // Consume newline

    System.out.print("Enter Month: ");
    String month = scanner.nextLine();

    // Automatically get the current year
    int year = LocalDate.now().getYear(); // Gets the current year

    double deduction = 0;
    while (true) {
        System.out.print("Enter Deduction Percentage (e.g., 10 for 10%): ");
        if (scanner.hasNextDouble()) {
            deduction = scanner.nextDouble() / 100; // Convert to decimal
            break; // Exit the loop if valid input is received
        } else {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next(); // Clear invalid input
        }
    }

    scanner.nextLine(); // Consume the newline

    System.out.print("Enter Report Status ('Approved' or 'Cancelled', press Enter if neither): ");
    String statusInput = scanner.nextLine();
    String status = statusInput.isEmpty() ? "Pending" : statusInput;

    String selectQuery = "SELECT dtr.employee_id, dtr.month, strftime('%Y', dtr.entry_date) AS year, "
            + "SUM(dtr.hours_worked) AS total_hours, "
            + "SUM(dtr.overtime_hrs) AS total_ovtime, "
            + "SUM(dtr.hours_worked) * e.emp_rate AS initial_salary, "
            + "(SUM(dtr.hours_worked) * e.emp_rate) * ? AS deductions, "
            + "SUM(dtr.overtime_hrs) * (e.emp_rate * 1.5) AS overtime_pay, "
            + "(SUM(dtr.hours_worked) * e.emp_rate + SUM(dtr.overtime_hrs) * (e.emp_rate * 1.5)) - "
            + "(SUM(dtr.hours_worked) * e.emp_rate) * ? AS net_pay "
            + "FROM DailyTimeRecords dtr JOIN tbl_employees e ON dtr.employee_id = e.emp_id "
            + "WHERE dtr.employee_id = ? AND dtr.month = ? AND strftime('%Y', dtr.entry_date) = ? "
            + "GROUP BY dtr.employee_id, dtr.month, strftime('%Y', dtr.entry_date)";

    String insertQuery = "INSERT INTO Reports (emp_id, month, year, total_hours, total_ovtime, initial_salary, "
            + "deductions, overtime_pay, net_pay, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
         PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

        // Set parameters for deduction percentage, employee ID, month, and year
        selectStmt.setDouble(1, deduction);
        selectStmt.setDouble(2, deduction);
        selectStmt.setInt(3, id);
        selectStmt.setString(4, month);
        selectStmt.setString(5, Integer.toString(year)); // Pass the current year
        
        ResultSet rs = selectStmt.executeQuery();

        while (rs.next()) {
            insertStmt.setInt(1, rs.getInt("employee_id"));
            insertStmt.setString(2, rs.getString("month"));
            insertStmt.setString(3, rs.getString("year"));
            insertStmt.setInt(4, rs.getInt("total_hours"));
            insertStmt.setInt(5, rs.getInt("total_ovtime"));
            insertStmt.setDouble(6, rs.getDouble("initial_salary"));
            insertStmt.setDouble(7, rs.getDouble("deductions"));
            insertStmt.setDouble(8, rs.getDouble("overtime_pay"));
            insertStmt.setDouble(9, rs.getDouble("net_pay"));
            insertStmt.setString(10, status);
            insertStmt.executeUpdate();
        }
    }
}

    public void Report() {
        Scanner sc = new Scanner(System.in);
        String choice;

        // Establish connection once
        Connection con = connectDB();
        if (con == null) {
            System.out.println("Failed to connect to the database.");
            return; // Exit if connection failed
        }

        do {
            System.out.println();
            System.out.println("***********************************************");
            System.out.println("*               Payslip Reports               *");
            System.out.println("***********************************************");
            System.out.println();

            System.out.println("1. Generate Reports");
            System.out.println("2. View General Reports");
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
                        ohaha(con);
                        GeneralReports();
                    } catch (SQLException e) {
                        System.out.println("Error while generating report: " + e.getMessage());
                    }
                    break;

                case 2:
                    GeneralReports();
                    break;

                case 3:
                    
                    break;

                case 4:
                    System.out.println("Going back to main menu . . .");
                    break;

                default:
                    System.out.print("Error Selection!");
            }

            System.out.print("Do you want to continue (y/n): ");
            choice = sc.next();

        } while (choice.equals("Y") || choice.equals("y"));
        
        // Close the scanner and connection
        sc.close();
        try {
            con.close(); // Close the connection after use
        } catch (SQLException e) {
            System.out.println("Failed to close the connection: " + e.getMessage());
        }
    }
}
