import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class MyDatabase {
  private Connection connection;
  
  private final String filename = "Council_Member_Expenses.csv";
  
  public MyDatabase() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      this.connection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
      createTables();
      readInData();
    } catch (ClassNotFoundException classNotFoundException) {
      classNotFoundException.printStackTrace(System.out);
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void allCouncilors() {
    try {
      String str = "Select * from councilors;";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      while (resultSet.next())
        System.out.println(resultSet.getInt("cID") + ": " + resultSet.getString("name")); 
      resultSet.close();
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void allExpenses() {
    try {
      String str = "Select eID, councilors.name as cname, wards.name as wname, description, amount from councilors join  expenses on councilors.cID = expenses.cID join wards on wards.wardID = councilors.wardID";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      while (resultSet.next())
        System.out.println(resultSet.getInt("eID") + " - " + resultSet.getString("wname") + " " + resultSet.getString("cname") + ", " + resultSet.getString("description") + ":" + resultSet.getDouble("amount")); 
      resultSet.close();
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void singleCouncilor(String paramString) {
    try {
      String str = "select name as name, sum(amount) as amount from councilors join expenses on councilors.cID = expenses.cID  where name = '" + paramString + "' group by name;";
      //String str = "select name as name, sum(amount) as amount from councilors join expenses on councilors.cID = expenses.cID  where name = word' or 1=1 group by name; update councilors set name = 'help';--";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      while (resultSet.next())
        System.out.println(resultSet.getString("name") + ":" + resultSet.getDouble("amount")); 
      resultSet.close();
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void singleWard(String paramString) {
    try {
      PreparedStatement preparedStatement = this.connection.prepareStatement("select wards.name as name, sum(amount) as amount from wards join councilors on wards.wardID = councilors.wardID  join expenses on councilors.cID = expenses.cID  where wards.name = ? group by wards.name;");
      preparedStatement.setString(1, paramString);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
        System.out.println(resultSet.getString("name") + ":" + resultSet.getDouble("amount")); 
      resultSet.close();
      preparedStatement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void deleteExpense(String paramString) {
    try {
      //String str = "delete from expenses where eID = " + paramString + ";";
      String str = "delete from expenses where eID = word or 1=1;";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void deleteCouncilor(String paramString) {
    try {
      String str = "delete from councilors where name = '" + paramString + "';";
      Statement statement = this.connection.createStatement();
      statement.execute(str);
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void highestExpense() {
    try {
      String str = "Select name, max(amount) as amount from councilors join expenses on councilors.cID = expenses.cID  group by councilors.name;";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      while (resultSet.next())
        System.out.println(resultSet.getString("name") + ": " + resultSet.getDouble("amount")); 
      resultSet.close();
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  private void createTables() {
    String str = "create table wards (  wardID integer, name VARCHAR(60), primary key(wardID))";
    try {
      this.connection.createStatement().executeUpdate(str);
      String str1 = "create table councilors ( cID integer IDENTITY, wardID integer, name VARCHAR(60), primary key(cID), foreign key (wardID) references wards);";
      this.connection.createStatement().executeUpdate(str1);
      String str2 = "create table expenses ( eID integer IDENTITY, cID integer, description VARCHAR(200),  account VARCHAR(60),  amount DECIMAL, primary key (eID, cID), foreign key (cID) references councilors on delete cascade);";
      this.connection.createStatement().executeUpdate(str2);
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  public void allWards() {
    try {
      String str = "Select * from wards;";
      Statement statement = this.connection.createStatement();
      ResultSet resultSet = statement.executeQuery(str);
      while (resultSet.next())
        System.out.println(resultSet.getInt("wardID") + ": " + resultSet.getString("name")); 
      resultSet.close();
      statement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
  
  private int getOrMakeWard(String paramString1, String paramString2) {
    int i = -1;
    try {
      PreparedStatement preparedStatement = this.connection.prepareStatement("Select * From  wards where wardID = ?;");
      preparedStatement.setInt(1, Integer.parseInt(paramString1));
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        i = resultSet.getInt("wardID");
      } else {
        PreparedStatement preparedStatement1 = this.connection.prepareStatement("insert into wards (wardID, name) values (?, ?);");
        i = Integer.parseInt(paramString1);
        preparedStatement1.setInt(1, i);
        preparedStatement1.setString(2, paramString2);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();
      } 
      resultSet.close();
      preparedStatement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
    return i;
  }
  
  private int getOrMakeCouncilor(String paramString, int paramInt) {
    int i = -1;
    try {
      PreparedStatement preparedStatement = this.connection.prepareStatement("Select cID From  councilors where name = ?;");
      preparedStatement.setString(1, paramString);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        i = resultSet.getInt("cID");
      } else {
        PreparedStatement preparedStatement1 = this.connection.prepareStatement("insert into councilors (name, wardID) values (?, ?);", 1);
        preparedStatement1.setString(1, paramString);
        preparedStatement1.setInt(2, paramInt);
        int j = preparedStatement1.executeUpdate();
        ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
        if (resultSet1.next())
          i = resultSet1.getInt(1); 
        preparedStatement1.close();
      } 
      resultSet.close();
      preparedStatement.close();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
    return i;
  }
  
  private void readInData() {
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader("Council_Member_Expenses.csv"));
      bufferedReader.readLine();
      String str = bufferedReader.readLine();
      while (str != null) {
        String[] arrayOfString = str.split(",");
        int i = getOrMakeWard(arrayOfString[0], arrayOfString[1]);
        int j = getOrMakeCouncilor(arrayOfString[2], i);
        PreparedStatement preparedStatement = this.connection.prepareStatement("insert into expenses  (cID, description, account, amount) values (?, ?, ?, ?);");
        preparedStatement.setInt(1, j);
        int k = arrayOfString.length;
        preparedStatement.setString(2, arrayOfString[k - 3]);
        preparedStatement.setString(3, arrayOfString[k - 2]);
        preparedStatement.setDouble(4, Double.parseDouble(arrayOfString[k - 1]));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        str = bufferedReader.readLine();
      } 
      bufferedReader.close();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } catch (SQLException sQLException) {
      sQLException.printStackTrace(System.out);
    } 
  }
}
