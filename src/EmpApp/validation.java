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

       
        if (name.isEmpty()) {
            System.out.print("\tInvalid Input: Name cannot be empty. Please try again: ");
            continue;
        }

        
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            System.out.print("\tInvalid Input: Name can only contain letters, spaces, hyphens, and apostrophes. Please try again: ");
            continue;
        }

       
        if (name.length() > 50) {
            System.out.print("\tInvalid Input: Name is too long. Please enter a shorter name. Please try again: ");
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


private String capitalizeWord(String word) {
    if (word.isEmpty()) return "";
    return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
}

    
    public String validateAddress() {
    String address;

    while (true) {
        
        address = sc.nextLine().trim();

       
        if (address.isEmpty()) {
            System.out.print("\tInvalid Input: Address cannot be empty. Please try again: ");
            continue;
        }

        
        if (address.length() > 100) {
            System.out.print("\tInvalid Input: Address is too long. Please enter a shorter address (max 100 characters): ");
            continue;
        }

        
        if (!address.matches(".*[a-zA-Z]+.*") || !address.matches("[a-zA-Z0-9\\s,.-]+")) {
            System.out.print("\tInvalid Input: Please try again: ");
            continue;
        }

        
        return address;
    }
}

    
   public String validateconNum() {
    while (true) {
        String contactNumber = sc.nextLine().trim();

        if (contactNumber.isEmpty()) {
            System.out.print("\tInvalid Input: Contact number cannot be empty: ");
            continue;
        }

        if (!contactNumber.matches("09\\d{9}")) {
            System.out.print("\tInvalid Input: Contact number must start with '09' and be exactly 11 digits long: ");
            continue;
        }

        
        if (isContactNumberExists(contactNumber)) {
            System.out.print("\tError: Contact number is already registered: ");
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
    String getEmail;

    while (true) {
        
        getEmail = sc.nextLine().trim();

       
        if (getEmail.isEmpty()) {
            System.out.print("\tInvalid Input: Email cannot be empty. Please try again: ");
            continue;
        }

        
        if (!getEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            System.out.print("\tInvalid Input: Please enter a valid email address: ");
            continue;
        }

        
        String[] validDomains = {"@gmail.com", "@yahoo.com", "@outlook.com"};
        boolean validDomain = false;

        for (String domain : validDomains) {
            if (getEmail.endsWith(domain)) {
                validDomain = true;
                break;
            }
        }

        if (!validDomain) {
            System.out.print("\tError: Email must have one of the supported domain names (@gmail.com, @yahoo.com, @outlook.com), try again: ");
            continue;
        }

       
        try {
            PreparedStatement findEmail = connectDB().prepareStatement("SELECT emp_email FROM tbl_employees WHERE emp_email = ?");
            findEmail.setString(1, getEmail);
            ResultSet result = findEmail.executeQuery();

            if (result.next()) {
                System.out.print("\tError: Email is already registered, try again: ");
                continue;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            continue;
        }

    
        return getEmail;
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
 
public String DeptandPosi() {
    
    Scanner sc = new Scanner(System.in);
    
    
    System.out.println("Select Employee's Department:");
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
    int deptChoice = sc.nextInt();
    sc.nextLine(); 
    
    String dept = "";
    String position = "";

   
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
        default: System.out.println("Invalid department choice."); return "";
    }

    
    System.out.println("Select a position for " + dept + ": ");

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
        default:
            System.out.println("Invalid department choice.");
            return "";
    }

    System.out.print(": ");
    int posChoice = sc.nextInt();
    sc.nextLine();

  
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
        default:
            System.out.println("Invalid position choice.");
            return "";
    }

    
    return dept + ": " + position;
}



public int Rate() {
    int rate = 0;
    
    while (true) {
       
        if (!sc.hasNextInt()) {
            System.out.print("\tInvalid Input: Please enter a valid number for the hourly rate: ");
            sc.nextLine(); 
            continue;
        }
        
        rate = sc.nextInt();
        sc.nextLine(); 
        
        
        if (rate < 100 || rate > 1000) {
            System.out.print("\tInvalid Input: Rate must be between ₱100 and ₱1,000. Please try again: ");
            continue;
        }
        
        break; 
    }
    
    return rate;
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

   
        if (hdate.isEmpty()) {
            System.out.print("\tInvalid Input: Hire date cannot be empty. Please try again: ");
            continue;
        }

        try {
            
            LocalDate parsedDate = LocalDate.parse(hdate, formatter);

            
            if (parsedDate.isAfter(LocalDate.now())) {
                System.out.print("\tInvalid Input: Hire date cannot be in the future. Please try again: ");
                continue;
            }

           
            return hdate; 
        } catch (DateTimeParseException e) {
            
            System.out.print("\tInvalid Input: Please enter a valid date in the format (yyyy-MM-dd): ");
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
   
    public int validateInt() {
        int getNum;
        
        while(true) {
            try {
                    getNum = sc.nextInt();
                    break;
            } catch(InputMismatchException e) {
                System.out.print("Invalid Input: Must only be a number, try again: ");
                sc.next();
            }
        }
        return getNum;
    }
    
    
    public String UpdateDeptandPosi() {
    
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
    
    System.out.print(": ");
    int deptChoice = sc.nextInt();
    sc.nextLine(); 
    
    String dept = "";
    String position = "";

   
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
        default: System.out.println("Invalid department choice."); return "";
    }

    
    System.out.println("Select a New position for " + dept + ": ");

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
        default:
            System.out.println("Invalid department choice.");
            return "";
    }

    System.out.print(": ");
    int posChoice = sc.nextInt();
    sc.nextLine();

  
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
        default:
            System.out.println("Invalid position choice.");
            return "";
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





    
    






























 






