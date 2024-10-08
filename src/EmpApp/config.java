
package EmpApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class config {
    
 
    
      public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:sheed.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
        
        
    }
      
      public void addRecord(String sql, Object... values) {
    try (Connection conn = this.connectDB(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

  
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
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); 
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]);
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
            } else {
                pstmt.setString(i + 1, values[i].toString()); 
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}   
      
     public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
      
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-24s | ", header)); 
            }
            headerLine.append("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

           
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-24s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    } 
      
//    
      
    
//      
//      public void addRecord(String sql, String... values) {
//        try (Connection conn = this.connectDB(); // Use the connectDB method
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            // Loop through the values and set them in the prepared statement
//            for (int i = 0; i < values.length; i++) {
//                pstmt.setString(i + 1, values[i]); // PreparedStatement index starts at 1
//            }
//
//            pstmt.executeUpdate();
//            System.out.println("Record added successfully!");
//        } catch (SQLException e) {
//            System.out.println("Error adding record: " + e.getMessage());
//        }
//}
      
      


    
      
}
