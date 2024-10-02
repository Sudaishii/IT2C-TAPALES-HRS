package EmpApp;

import static EmpApp.config.connectDB;
import java.util.Scanner;

public class main {
    
    
     public void addEmployees(){
         
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Employee's First Name: ");
        String fname = sc.next();
        System.out.print("Employee's Last Name: ");
        String lname = sc.next();
        sc.nextLine();
        System.out.print("Employee's Address: ");
        String email = sc.nextLine();
        System.out.print("Employee's Contact #: ");
        String num = sc.next();
        System.out.print("Employee's Job Title: ");
        String title = sc.next();
        System.out.print("Employee's Hire Date: ");
        String hdate = sc.next();

        String sql = "INSERT INTO tbl_employees (emp_fname, emp_lname, emp_add, emp_contantnum, emp_title, emp_hdate) VALUES (?, ?, ?, ?, ?, ?)";


        conf.addRecord(sql, fname, lname, email, num, title, hdate);


    }

    public static void main(String[] args) {
        
        Scanner oha = new Scanner(System.in);
        String choice;
        
       do{
        
        System.out.println("\nWelcome to Payslip App");
        System.out.println("--------------------------");
        System.out.println("1. ADD");
        System.out.println("2. VIEW");
        System.out.println("3. UPDATE");
        System.out.println("4. DELETE");
        System.out.println("5. EXIT");
        System.out.println("--------------------------");

        System.out.print("Enter Action: ");
        int act = oha.nextInt();

         
         switch(act){
             case 1:
                    main demo = new main();
                    demo.addEmployees();
                    break;
                    
                    
                    
                    
         }
         
           System.out.print("Do you want to continue? (y:n): ");
           choice = oha.next();
         
       }while(choice.equals("Y") || choice.equals("y"));
           
    }
    
}
