package net.proselyte.jdbc;

import java.sql.*;

public class DevelopersJdbcDemo {

    /*
         JDBC Driver and database url
    */

    static final String JDBC_Driver = "com.mysql.jdbc.Driver";
    static  final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/my_project?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";

    /*
         User and Password
    */

    static  final  String USER = "root";
    static  final String PASSWORD = "lerona-makarona18239";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;

        System.out.println("Registering JDBC driver...");

        Class.forName("com.mysql.cj.jdbc.Driver");

        System.out.println("Creating database connection...");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        System.out.println("Executing statement...");
        statement = connection.createStatement();

        String sql;
        sql = "SELECT * FROM person";

        ResultSet resultSet = statement.executeQuery(sql);

        System.out.println("Retrieving data from database...");
        System.out.println("\nPeople:");
        while (resultSet.next()){

            int id = resultSet.getInt("id");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            int age = resultSet.getInt("age");

            System.out.println("\n========================\n");
            System.out.println("id: "+id);
            System.out.println("First name: " + first_name);
            System.out.println("Last name: " + last_name);
            System.out.println("Age: " + age);
        }
        System.out.println("Closing connection and releasing resources...");
        resultSet.close();
        statement.close();
        connection.close();
    }
}
