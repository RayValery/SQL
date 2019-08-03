import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class JDBC_Runner {

    static final String userName = "root";
    static final String password = "lerona-makarona18239";
    static final String URL = "jdbc:mysql://127.0.0.1:3306/my_project?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";

    public static void main(String[] args) throws IOException {
        try (Connection connection = DriverManager.getConnection(URL, userName, password)) {
            System.out.println("Connected to DataBase");
            String sql;
            sql = "create table Books2 (id int not null auto_increment, name varchar(30) not null, dt date, img longblob, primary key (id));";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            sql = "insert into Books2 (name) values ('Angels_and_Daemons');";
            statement.executeUpdate(sql);

            // DATE
            PreparedStatement dateStatement =        //protection against Injection using PreparedStatement
                    connection.prepareStatement("insert into Books2 (name, dt) values ('Inferno', ?);");
            dateStatement.setDate(1, new Date(1564743895136L));
            dateStatement.execute();

            ResultSet dateResultSet = dateStatement.executeQuery("select * from Books;");
            while (dateResultSet.next()){
                System.out.println(dateResultSet.getDate("dt"));
            }

            // BLOB
            /*BufferedImage image = ImageIO.read(new File("smile.jpg"));
            Blob blob = connection.createBlob();
            try (OutputStream outputStream = blob.setBinaryStream(1)) {
                ImageIO.write(image, "jpg", outputStream);
            }

            PreparedStatement statement1 =
                    connection.prepareStatement("insert into Books2 (name, img) values (?,?);");
            statement1.setString(1, "DaVinci_Code");
            statement1.setBlob(2, blob);
            statement1.execute();

            ResultSet resultSet = statement1.executeQuery("select * from Books2");
            if (resultSet.next()){
                Blob blob2 = resultSet.getBlob("img");
                BufferedImage image2 = ImageIO.read(blob.getBinaryStream());
                File outputFIle = new File("saved.png");
                ImageIO.write(image,"png", outputFIle);
            }*/



            // PROCEDURE

            /*
              create procedure BooksCount (out n int)
              begin
              select count(*) into n from Books;
              end
            */
/*
            CallableStatement callableStatement = connection.prepareCall("{call BooksCount(?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER); // type of the OutParameter
            callableStatement.execute();
            System.out.println(callableStatement.getInt(1));
*/
            /*
             create procedure getBooks (bookId int)
             begin
             select * from books where id = bookId;
             end
            */
/*
            CallableStatement callableStatement2 = connection.prepareCall("{call getBooks(?)}");
            callableStatement2.setInt(1,1); // value of InParameter
            if (callableStatement2.execute()){
                ResultSet procedureResultSet = callableStatement2.getResultSet();
                while (procedureResultSet.next()){
                    System.out.println(procedureResultSet.getInt("id"));
                    System.out.println(procedureResultSet.getString("name"));
                }
            }
*/
            /*
              create procedure getCount ()
              begin
              select count(*) from Books;
              select count(*) from person;
              end
            */
/*
            CallableStatement callableStatement3 = connection.prepareCall("{call getCount(?)}"); // without "?" ???
            boolean hasResults = callableStatement3.execute();
            while (hasResults){
                ResultSet resultSet = callableStatement3.getResultSet();
                while (resultSet.next()){
                System.out.println(resultSet.getInt(1));}
                hasResults = callableStatement3.getMoreResults();
            }
*/

            sql = "insert into Books2 (name) values ('Solomon_key');";
            statement.executeUpdate(sql);

            // SCROLLABLE ROWSET
            Statement statement2 =
                    connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    // INSENSITIVE - scrolling(forward and backward) without changes
                    // SENSITIVE - scrolling(forward and backward) with changes
            ResultSet scrollResultSet = statement2.executeQuery("select * from Books2;");

            System.out.println("\nForward:\n");
            while (scrollResultSet.next()){
                System.out.println(scrollResultSet.getString("name"));
            }

            System.out.println("\nBackward:\n");
            while (scrollResultSet.previous()){
                System.out.println(scrollResultSet.getString("name"));
            }

            if(scrollResultSet.relative(3)){                        // сдвиг на 3 вправо
                System.out.println("\nrelative(3): "+scrollResultSet.getString("name"));
            }
            if(scrollResultSet.relative(-2)){                        // сдвиг на 2 влево
                System.out.println("relative(-2): "+scrollResultSet.getString("name"));
            }
            if (scrollResultSet.absolute(2)){                         // 2-ой от начала
                System.out.println("\nabsolute(2): "+scrollResultSet.getString("name"));
            }
            if (scrollResultSet.first()){                                  // first row
                System.out.println("\nfirst: "+scrollResultSet.getString("name"));
            }
            if (scrollResultSet.last()){                                    // last row
                System.out.println("last: "+scrollResultSet.getString("name"));
            }


            // UPDATE RESULTSET
            Statement statement3 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet3 = statement3.executeQuery("select * from Books2");

            resultSet3.last();
            resultSet3.updateString("name", "new Value");
            resultSet3.rowUpdated();

            resultSet3.moveToInsertRow();
            resultSet3.insertRow();
            resultSet3.updateString("name", "inserted row");
            resultSet3.rowUpdated();

            resultSet3.absolute(2);
            resultSet3.deleteRow();

            resultSet3.beforeFirst();
            while (resultSet3.next()){
                System.out.println();
            }



            statement.executeUpdate("drop table Books2;");

        } catch (SQLSyntaxErrorException e) {
            System.out.println("You have an error in your SQL syntax");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

