import java.sql.Connection;
import java.util.Scanner;

public class A3P2 {
  static Connection connection;
  
  public static void main(String[] paramArrayOfString) throws Exception {
    MyDatabase myDatabase = new MyDatabase();
    runConsole(myDatabase);
    System.out.println("Exiting...");
  }
  
  public static void runConsole(MyDatabase paramMyDatabase) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("db > ");
    String str1 = scanner.nextLine();
    String str2 = "";
    while (str1 != null && !str1.equals("q")) {
      String[] arrayOfString = str1.split("\\s+");
      if (str1.indexOf(" ") > 0)
        str2 = str1.substring(str1.indexOf(" ")).trim(); 
      if (arrayOfString[0].equals("h")) {
        printHelp();
      } else if (arrayOfString[0].equals("w")) {
        paramMyDatabase.allWards();
      } else if (arrayOfString[0].equals("c")) {
        paramMyDatabase.allCouncilors();
      } else if (arrayOfString[0].equals("e")) {
        paramMyDatabase.allExpenses();
      } else if (arrayOfString[0].equals("wt")) {
        try {
          if (arrayOfString.length >= 2) {
            paramMyDatabase.singleWard(str2);
          } else {
            System.out.println("Require an argument for this command");
          } 
        } catch (Exception exception) {
          System.out.println("id must be an integer");
        } 
      } else if (arrayOfString[0].equals("ct")) {
        if (arrayOfString.length >= 2) {
          paramMyDatabase.singleCouncilor(str2);
        } else {
          System.out.println("Require an argument for this command");
        } 
      } else if (arrayOfString[0].equals("de")) {
        try {
          if (arrayOfString.length >= 2) {
            paramMyDatabase.deleteExpense(str2);
          } else {
            System.out.println("Require an argument for this command");
          } 
        } catch (Exception exception) {
          System.out.println("id must be an integer");
        } 
      } else if (arrayOfString[0].equals("dc")) {
        if (arrayOfString.length >= 2) {
          paramMyDatabase.deleteCouncilor(str2);
        } else {
          System.out.println("Require an argument for this command");
        } 
      } else if (arrayOfString[0].equals("m")) {
        paramMyDatabase.highestExpense();
      } else {
        System.out.println("Read the help with h, or find help somewhere else.");
      } 
      System.out.print("db > ");
      str1 = scanner.nextLine();
    } 
    scanner.close();
  }
  
  private static void printHelp() {
    System.out.println("Winnipeg Council Member Expenses console");
    System.out.println("Commands:");
    System.out.println("h - Get help");
    System.out.println("w - Print all wards");
    System.out.println("c - Print all coucillors");
    System.out.println("ct name - Print total expenses for councilors 'name'");
    System.out.println("wt name - Print total expenses for ward 'name'");
    System.out.println("dc name - Delete councilors named 'name'");
    System.out.println("de id - delete expense 'id'");
    System.out.println("m - Show the highest single-time expense for each councilors");
    System.out.println("q - Exit the program");
    System.out.println("---- end help ----- ");
  }
}
