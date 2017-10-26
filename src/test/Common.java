package test;

import java.sql.*;

public class Common {

    private Connection myConn = null;
    private Statement myStmt = null;
    private ResultSet myRs = null;
    private PreparedStatement prepMyStmt = null;
    private CallableStatement callableMyStmt = null;

    private final String URL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private final String USER = "employee";
    private final String PASS = "employee";

    private void getConnectionToDB() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            myStmt = myConn.createStatement();
            System.out.println("\n===============================");
            System.out.println("Database connection successful!");
            System.out.println("===============================\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getConnectionToDBPreparedStmt() {
        try {
            myConn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("\n===============================");
            System.out.println("Database connection successful!");
            System.out.println("===============================\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseSalary(String theDepartment, double theIncreaseAmount) {

        try {
            getConnectionToDBPreparedStmt();
            callableMyStmt = myConn.prepareCall("{call increase_salaries_for_department(?, ?)}");

            callableMyStmt.setString(1, theDepartment);
            callableMyStmt.setDouble(2, theIncreaseAmount);

            System.out.println("Calling stored procedure: \tincrease_salaries_for_department('" + theDepartment + "', '" + theIncreaseAmount + "')");
            callableMyStmt.execute();
            System.out.println("Finished calling stored procedure");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void comparisonSalary(String operator,double salary) {
        try {
            getConnectionToDBPreparedStmt();

            if((operator.equals("<")) || (operator.equals(">")) || (operator.equals("<="))
                    || (operator.equals(">=")) || (operator.equals("!=")) || (operator.equals("=="))) {
                prepMyStmt = myConn.prepareStatement("SELECT * FROM employees WHERE salary " + operator + " ?");
                prepMyStmt.setDouble(1, salary);
                myRs = prepMyStmt.executeQuery();
                display(myRs);
            } else {
                System.out.println("Provide Valid Operator: <, >, <=, >=, !=, == ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void updateData(String email, String lastName, String firstName) {
        try {
            getConnectionToDB();
            System.out.println("BEFORE THE UPDATE ... ");
            displayEmployee(lastName, firstName);

            myStmt.executeUpdate(
                    "UPDATE employees SET email='" + email + "' WHERE last_name='" + lastName + "' AND first_name='" + firstName + "'");

            if(lastName == "") {
                System.err.println("Cannot keep lastname empty");
            }

            System.out.println("AFTER THE UPDATE ... ");
            displayEmployee(lastName, firstName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void removeData(String lastName, String firstName) {
        try {
            getConnectionToDB();
            System.out.println("BEFORE THE UPDATE ... ");
            displayEmployee(lastName, firstName);

            myStmt.executeUpdate("DELETE from employees " +
                    "WHERE last_name='" + lastName + "' AND first_name='" + firstName + "'");

            System.out.println("DELETED DATA ... ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void removeData(int id, String lastName, String firstName) {
        try {
            getConnectionToDB();
            System.out.println("BEFORE THE UPDATE ... ");
            displayEmployee("Isac", "Khan");

            myStmt.executeUpdate("DELETE from employees " +
                    "WHERE id='" + id + "' AND last_name='" + lastName + "' AND first_name='" + firstName + "'");

            System.out.println("DELETED DATA ... ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void insertData(String lastName, String firstName, String email, String department, double salary) {
        try {
            getConnectionToDB();
            insertDataQuery(lastName, firstName, email, department, salary);
            myRs = myStmt.executeQuery("select * from employees");
            while(myRs.next()) {
                display(myRs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void readData() {
        try {
            getConnectionToDB();
            myRs = myStmt.executeQuery("select * from employees");
            while (myRs.next()) {
                display(myRs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void readData(String lastName, String firstName) {
        try {
            getConnectionToDBPreparedStmt();
            prepMyStmt = myConn.prepareStatement("SELECT * FROM employees " +
                    "WHERE last_name = ? AND first_name = ?");
            prepMyStmt.setString(1, lastName);
            prepMyStmt.setString(2, firstName);
            myRs = prepMyStmt.executeQuery();
            display(myRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    public void readData(int id, String lastName, String firstName) {
        try {
            getConnectionToDBPreparedStmt();
            prepMyStmt = myConn.prepareStatement("SELECT * FROM employees " +
                    "WHERE id = ? AND last_name = ? AND first_name = ?");
            prepMyStmt.setInt(1,id);
            prepMyStmt.setString(2, lastName);
            prepMyStmt.setString(3, firstName);
            myRs = prepMyStmt.executeQuery();
            display(myRs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            close(myRs, myStmt, myConn);
        }
    }

    private void close(ResultSet rs, Statement stmt, Connection conn) {
        if((rs != null) && (stmt != null) && (conn != null)) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void display(ResultSet rs) {
        try {
            System.out.print("ID\tLASTNAME\tFIRSTNAME\tEMAIL\tDEPARTMENT\tSALARY\n");
            System.out.println("=======================================================");
            while (rs.next()) {
                System.out.println("Employee: " + rs.getString("id") + ", " + rs.getString("last_name") + ", " + rs.getString("first_name")
                        + ", " + rs.getString("email") + ", " + rs.getString("department") + ", " + rs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayEmployee(String lastName, String firstName) {
        try {
            myRs = myStmt.executeQuery("select * from employees where last_name='" + lastName + "' and first_name='" + firstName + "'");
            while(myRs.next()) {
                System.out.println(myRs.getString("id") + ", " + myRs.getString("last_name") + ", " + myRs.getString("first_name")
                        + ", " + myRs.getString("email") + ", " + myRs.getString("department") + ", " + myRs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDataQuery(String lastName, String firstName, String email, String department, double salary) {
        try {
            myStmt.executeUpdate("insert into employees " +
                    "(last_name, first_name, email, department, salary)" +
                    "values " +
                    "('" + lastName + "', '" + firstName + "', '" + email + "', '" + department + "', " + salary + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
