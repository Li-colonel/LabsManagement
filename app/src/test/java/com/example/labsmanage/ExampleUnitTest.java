package com.example.labsmanage;

import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        String connectionUrl = "jdbc:sqlserver://10.72.70.78:1433;databaseName=LABM;user=sa;password=dqy20010710";
        try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT TOP 10 * FROM Person.Contact";
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                System.out.println(rs.getString("FirstName") + " " + rs.getString("LastName"));
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        DBUtil db = new DBUtil();
        db.getSQLConnection();
        String sql = "SELECT COUNT(*) FROM type1;";
        int res =db.produceSQL(sql);
        System.out.println("result = "+res);
    }
}
