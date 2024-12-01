package EmpApp;



import static EmpApp.config.connectDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class validation {

    Scanner sc = new Scanner(System.in);
    
     public String validateName() {
        String name;
        while (true) {
            
            name = sc.nextLine().trim();

      
            if (name.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return "EXIT"; 
            }

          
            if (name.isEmpty()) {
                System.out.print("\tName cannot be empty. Please try again (or press 'X' to cancel): ");
                continue;
            }

            
            if (!name.matches("^[a-zA-Z\\s'-]+$")) {
                System.out.print("\tName can only contain letters, spaces, hyphens, and apostrophes. Please try again (or press 'X' to cancel): ");
                continue;
            }

      
            if (name.length() > 50) {
                System.out.print("\tName is too long. Please enter a shorter name. Please try again (or press 'X' to cancel): ");
                continue;
            }

         
            String[] words = name.split("\\s+");
            StringBuilder formattedName = new StringBuilder();

            for (String word : words) {
                if (word.contains("-")) {
                    
                    String[] hyphenatedParts = word.split("-");
                    for (int i = 0; i < hyphenatedParts.length; i++) {
                        hyphenatedParts[i] = capitalizeWord(hyphenatedParts[i]);
                    }
                    formattedName.append(String.join("-", hyphenatedParts));
                } else if (word.contains("'")) {
                  
                    String[] apostropheParts = word.split("'");
                    for (int i = 0; i < apostropheParts.length; i++) {
                        apostropheParts[i] = capitalizeWord(apostropheParts[i]);
                    }
                    formattedName.append(String.join("'", apostropheParts));
                } else {
                 
                    formattedName.append(capitalizeWord(word));
                }
                formattedName.append(" ");
            }

            return formattedName.toString().trim(); 
        }
    }

     public String validateGender() {
    while (true) {
        System.out.print("Enter gender (M/F or 'X' to cancel): ");
        String input = sc.nextLine().trim().toUpperCase();
        if ("X".equals(input)) return "EXIT";
        if (input.equals("M") || input.equals("F")) return input; 
        System.out.println("Invalid gender. Please enter M (Male) or F (Female).");
         }
        }
     
     
     public int validateAge() {
    while (true) {
        System.out.print("Enter age (or -1 to cancel): ");
        try {
            int age = Integer.parseInt(sc.nextLine().trim());
            if (age == -1) return -1;
            if (age >= 18 && age <= 65) return age; // Adjust range as needed
            System.out.println("Age must be between 18 and 65.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}
     
private String capitalizeWord(String word) {
    if (word.isEmpty()) return "";
    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
}

    
   public static String validateAddress() {
        String address;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Employee's Address (or enter 'X' to cancel): ");
            address = sc.nextLine().trim();

            // Check if the user wants to cancel
            if (address.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return "EXIT";
            }

            // Validate address length
            if (address.length() < 5) {
                System.out.println("\tAddress is too short. Please enter at least 5 characters.");
                continue;
            }

            // Validate address format
            if (!address.matches(".*[a-zA-Z]+.*") || !address.matches("[a-zA-Z0-9\\s,.-]+")) {
                System.out.println("\tInvalid address format. Please enter a valid address.");
                continue;
            }

            // Return valid address
            return address;
        }
    }


    
   public String validateconNum() {
    while (true) {
        
        String contactNumber = sc.nextLine().trim();
        
        if (contactNumber.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return "EXIT"; 
            }

        else if (contactNumber.isEmpty()) {
            System.out.print("\tInvalid Input: Contact number cannot be empty (or press 'X' to cancel): ");
            continue;
        }

        else if (!contactNumber.matches("09\\d{9}")) {
            System.out.print("\tContact number must start with '09' and be exactly 11 digits long (or press 'X' to cancel): ");
            continue;
        }

        
        else if (isContactNumberExists(contactNumber)) {
            System.out.print("\tError: Contact number is already registered (or press 'X' to cancel): ");
            continue;
        }

        return contactNumber;
    }
}

private boolean isContactNumberExists(String contactNumber) {
    try (PreparedStatement findContact = connectDB().prepareStatement("SELECT emp_contactnum FROM tbl_employees WHERE emp_contactnum = ?")) {
        findContact.setString(1, contactNumber);
        ResultSet result = findContact.executeQuery();
        return result.next();
    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
        return false;
    }
}
    
    public String validateEmail() {
    String email;

    while (true) {
        
        email = sc.nextLine().trim();
        
        if (email.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return "EXIT"; 
            }
       
        else if (email.isEmpty()) {
            System.out.print("\tEmail cannot be empty. Please try again (or press 'X' to cancel): ");
            continue;
        }

        
        else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            System.out.print("\tPlease enter a valid email address (or press 'X' to cancel): ");
            continue;
        }

        
        String[] validDomains = {"@gmail.com", "@yahoo.com", "@outlook.com"};
        boolean validDomain = false;

        for (String domain : validDomains) {
            if (email.endsWith(domain)) {
                validDomain = true;
                break;
            }
        }

        if (!validDomain) {
            System.out.print("\tEmail must have one of the supported domain names (@gmail.com, @yahoo.com, @outlook.com), try again (or press 'X' to cancel): ");
            continue;
        }

       
        try {
            PreparedStatement findEmail = connectDB().prepareStatement("SELECT emp_email FROM tbl_employees WHERE emp_email = ?");
            findEmail.setString(1, email);
            ResultSet result = findEmail.executeQuery();

            if (result.next()) {
                System.out.print("\tError: Email is already registered, try again (or press 'X' to cancel): ");
                continue;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            continue;
        }

    
        return email;
    }
  }  
   
    
    public int validateChoice() {
      
        int getNum;
        
       while(true) {
            try {
                    getNum = sc.nextInt();
                    break;
            } catch(InputMismatchException e) {
                System.out.print("\tInvalid Input: Must only be a number, try again: ");
                sc.next();
            }
            
            
        }
        return getNum;
   }
 
public String AddDeptandPosi() {
    Scanner sc = new Scanner(System.in);

    
    System.out.println("Select Employee's New Department:");
    System.out.println("1. Finance");
    System.out.println("2. Marketing");
    System.out.println("3. Sales");
    System.out.println("4. Executive");
    System.out.println("5. Information Technology");
    System.out.println("6. Customer Service");
    System.out.println("7. Administrative");
    System.out.println("8. Product Management");
    System.out.println("9. Legal");

    int deptChoice = 0;
    String dept = "";
    
 
    while (true) {
        
        System.out.print(": ");
        String input = sc.nextLine().trim();
            
             if (input.equalsIgnoreCase("X")) {
            System.out.println("\tProcess canceled!");
            return "EXIT";  
        }

            if (input.matches("[1-9]")) {  
            deptChoice = Integer.parseInt(input);
            if (deptChoice >= 1 && deptChoice <= 9) {
                break; 
            } else {
                System.out.print("Please choose a number between 1 and 9 (or press 'X' to cancel).");
            }
        } else {
            System.out.print("Please enter a valid number (or enter 'X' to cancel).");
        }
    }

  
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
    }


    String position = "";
    System.out.println("Select a position for " + dept + " (or enter 'X' to cancel): ");
    int posChoice = 0;
    

    while (true) {

        switch (dept) {
            case "Finance":
                System.out.println("1. Accountant");
                System.out.println("2. Financial Analyst");
                System.out.println("3. CFO");
                break;
            case "Marketing":
                System.out.println("1. Marketing Specialist");
                System.out.println("2. Marketing Manager");
                System.out.println("3. Chief Marketing Officer");
                break;
            case "Sales":
                System.out.println("1. Sales Representative");
                System.out.println("2. Sales Manager");
                System.out.println("3. Regional Sales Director");
                break;
            case "Executive":
                System.out.println("1. CEO");
                System.out.println("2. COO");
                break;
            case "Information Technology":
                System.out.println("1. IT Specialist");
                System.out.println("2. System Administrator");
                System.out.println("3. IT Manager");
                break;
            case "Customer Service":
                System.out.println("1. Customer Service Representative");
                System.out.println("2. Customer Service Supervisor");
                break;
            case "Administrative":
                System.out.println("1. Office Assistant");
                System.out.println("2. Administrative Manager");
                break;
            case "Product Management":
                System.out.println("1. Product Manager");
                System.out.println("2. Senior Product Manager");
                break;
            case "Legal":
                System.out.println("1. Legal Counsel");
                System.out.println("2. Legal Assistant");
                break;
        }

        System.out.print(": ");
        String input = sc.nextLine().trim();
        
        if (input.equalsIgnoreCase("X")) {
             System.out.println("\tProcess canceled!");
            return "EXIT";  
        }

        if (input.matches("[1-3]")) {  
            posChoice = Integer.parseInt(input);
            break;  
        } else {
            System.out.println("Invalid input. Please enter a valid number (or enter 'X' to cancel)");
        }
    }

  
    switch (dept) {
        case "Finance":
            position = (posChoice == 1) ? "Accountant" : (posChoice == 2) ? "Financial Analyst" : "CFO";
            break;
        case "Marketing":
            position = (posChoice == 1) ? "Marketing Specialist" : (posChoice == 2) ? "Marketing Manager" : "Chief Marketing Officer";
            break;
        case "Sales":
            position = (posChoice == 1) ? "Sales Representative" : (posChoice == 2) ? "Sales Manager" : "Regional Sales Director";
            break;
        case "Executive":
            position = (posChoice == 1) ? "CEO" : "COO";
            break;
        case "Information Technology":
            position = (posChoice == 1) ? "IT Specialist" : (posChoice == 2) ? "System Administrator" : "IT Manager";
            break;
        case "Customer Service":
            position = (posChoice == 1) ? "Customer Service Representative" : "Customer Service Supervisor";
            break;
        case "Administrative":
            position = (posChoice == 1) ? "Office Assistant" : "Administrative Manager";
            break;
        case "Product Management":
            position = (posChoice == 1) ? "Product Manager" : "Senior Product Manager";
            break;
        case "Legal":
            position = (posChoice == 1) ? "Legal Counsel" : "Legal Assistant";
            break;
    }

    return dept + ": " + position;
}


public int Rate() {
    
    int rate = 0;
    
    while (true) {
        System.out.print("\tPlease enter the hourly rate (or enter 'X' to cancel): ");
        
        String input = sc.nextLine().trim();  
        
        if (input.equalsIgnoreCase("X")) {
            System.out.println("\tProcess canceled!");
            return -1;  
        }
        
        try {
            rate = Integer.parseInt(input); 
        } catch (NumberFormatException e) {
            System.out.println("\tInvalid input! Please enter a valid number (or press 'X' to cancel):");
            continue;
        }
        
        if (rate < 100 || rate > 1000) {
            System.out.print("\tRate must be between ₱100 and ₱1000. Please try again (or press 'X' to cancel): ");
            continue;
        }
        
        break;  
    }
    
    return rate;
}
public int validateIntOrX() {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine().trim();

    if ("X".equalsIgnoreCase(input)) {
        return -1;
    }

    try {
        int number = Integer.parseInt(input);
        return number;
    } catch (NumberFormatException e) {
        return -2;
    } finally {
        // Clear the buffer to prevent any extra newline issues
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}





public String validateEntryDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String hdate;

    while (true) {
        
        hdate = sc.nextLine().trim(); 

   
        if (hdate.isEmpty()) {
            System.out.print("\tInvalid Input: Entry date cannot be empty. Please try again: ");
            continue;
        }

        try {
            
            LocalDate parsedDate = LocalDate.parse(hdate, formatter);

       
            if (parsedDate.isAfter(LocalDate.now())) {
                System.out.print("\tInvalid Input: Entry date cannot be in the future. Please try again: ");
                continue;
            }

           
            return hdate; 
        } catch (DateTimeParseException e) {
            
            System.out.print("\tInvalid Input: Please enter a valid date in the format (yyyy-MM-dd): ");
        }
    }
}
 

public String validateHireDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String hdate;

    while (true) {
        
        hdate = sc.nextLine().trim(); 
        
        if (hdate.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return "EXIT"; 
            }
   
        else if (hdate.isEmpty()) {
            System.out.print("\tHire date cannot be empty. Please try again (or press 'X' to cancel): ");
            continue;
        }

        try {
            
            LocalDate parsedDate = LocalDate.parse(hdate, formatter);

            
            if (parsedDate.isAfter(LocalDate.now())) {
                System.out.print("\tHire date cannot be in the future. Please try again (or press 'X' to cancel): ");
                continue;
            }

           
            return hdate; 
        } catch (DateTimeParseException e) {
            
            System.out.print("\tPlease enter a valid date in the format (yyyy-MM-dd or press 'X' to cancel): ");
        }
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
   
    public static int validateInt() {
        
        int getNum;
        Scanner sc = new Scanner(System.in);
        while (true) {
            
            String input = sc.nextLine().trim();  

            
            if (input.equalsIgnoreCase("X")) {
                System.out.println("\tProcess canceled!");
                return -1;  
            }

            try {
              
                getNum = Integer.parseInt(input);
                break;  
            } catch (NumberFormatException e) {
                System.out.print("Invalid Input: Must only be a number, try again (or Enter 'X' to cancel): ");
            }
        }
        return getNum;  
    }

    
  public String UpdateDeptandPosi() {
    Scanner sc = new Scanner(System.in);

    System.out.println("Select New Employee's Department:");
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
    int deptChoice = 0;
    String dept = "";
    
    while (true) {
        if (sc.hasNextInt()) {
            deptChoice = sc.nextInt();
            sc.nextLine(); 
            
            if (deptChoice >= 1 && deptChoice <= 9) {
                break;
            } else {
                System.out.println("Invalid department choice. Please choose a number between 1 and 9.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
            sc.nextLine(); 
        }
    }

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
    }

    System.out.println("Select a New position for " + dept + ": ");
    int posChoice = 0;
    String position = "";

    while (true) {
        
        switch (dept) {
            case "Finance":
                System.out.println("1. Accountant");
                System.out.println("2. Financial Analyst");
                System.out.println("3. CFO");
                break;
            case "Marketing":
                System.out.println("1. Marketing Specialist");
                System.out.println("2. Marketing Manager");
                System.out.println("3. Chief Marketing Officer");
                break;
            case "Sales":
                System.out.println("1. Sales Representative");
                System.out.println("2. Sales Manager");
                System.out.println("3. Regional Sales Director");
                break;
            case "Executive":
                System.out.println("1. CEO");
                System.out.println("2. COO");
                break;
            case "Information Technology":
                System.out.println("1. IT Specialist");
                System.out.println("2. System Administrator");
                System.out.println("3. IT Manager");
                break;
            case "Customer Service":
                System.out.println("1. Customer Service Representative");
                System.out.println("2. Customer Service Supervisor");
                break;
            case "Administrative":
                System.out.println("1. Office Assistant");
                System.out.println("2. Administrative Manager");
                break;
            case "Product Management":
                System.out.println("1. Product Manager");
                System.out.println("2. Senior Product Manager");
                break;
            case "Legal":
                System.out.println("1. Legal Counsel");
                System.out.println("2. Legal Assistant");
                break;
        }

        System.out.print(": ");
        if (sc.hasNextInt()) {
            posChoice = sc.nextInt();
            sc.nextLine(); 
            
           
            if ((dept.equals("Executive") && posChoice >= 1 && posChoice <= 2) || 
                (dept.equals("Finance") && posChoice >= 1 && posChoice <= 3) || 
                (dept.equals("Marketing") && posChoice >= 1 && posChoice <= 3) ||
                (dept.equals("Sales") && posChoice >= 1 && posChoice <= 3) ||
                (dept.equals("Information Technology") && posChoice >= 1 && posChoice <= 3) ||
                (dept.equals("Customer Service") && posChoice >= 1 && posChoice <= 2) ||
                (dept.equals("Administrative") && posChoice >= 1 && posChoice <= 2) ||
                (dept.equals("Product Management") && posChoice >= 1 && posChoice <= 2) ||
                (dept.equals("Legal") && posChoice >= 1 && posChoice <= 2)) {
                break; 
            } else {
                System.out.println("Invalid position choice. Please select a valid number.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
            sc.nextLine();
        }
    }


    switch (dept) {
        case "Finance":
            position = (posChoice == 1) ? "Accountant" : (posChoice == 2) ? "Financial Analyst" : "CFO";
            break;
        case "Marketing":
            position = (posChoice == 1) ? "Marketing Specialist" : (posChoice == 2) ? "Marketing Manager" : "Chief Marketing Officer";
            break;
        case "Sales":
            position = (posChoice == 1) ? "Sales Representative" : (posChoice == 2) ? "Sales Manager" : "Regional Sales Director";
            break;
        case "Executive":
            position = (posChoice == 1) ? "CEO" : "COO";
            break;
        case "Information Technology":
            position = (posChoice == 1) ? "IT Specialist" : (posChoice == 2) ? "System Administrator" : "IT Manager";
            break;
        case "Customer Service":
            position = (posChoice == 1) ? "Customer Service Representative" : "Customer Service Supervisor";
            break;
        case "Administrative":
            position = (posChoice == 1) ? "Office Assistant" : "Administrative Manager";
            break;
        case "Product Management":
            position = (posChoice == 1) ? "Product Manager" : "Senior Product Manager";
            break;
        case "Legal":
            position = (posChoice == 1) ? "Legal Counsel" : "Legal Assistant";
            break;
    }

    return dept + ": " + position;
}


    public int validateint(String input) {
        Scanner sc = new Scanner(System.in);
        boolean valid = false;
        int result = 0;

       
        while (!valid) {
            try {
                result = Integer.parseInt(input);  
                valid = true;  
            } catch (NumberFormatException e) {
                System.out.print("\tInvalid input. Please enter a valid integer: ");
                input = sc.nextLine(); 
            }
        }
        return result;  
    
}


}





    
    






























 






