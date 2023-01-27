package com.example.labsmanage;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DBUtil {
    //从properties中读取数据库信息,文件应放在根目录，否则应如下
    ResourceBundle reader = ResourceBundle.getBundle("assets/dbconfig");

    //建立连接
    public Connection getSQLConnection() {
        Connection con = null;
        String connectionUrl = reader.getString("db.url");
        String connectionUser = reader.getString("db.username");
        String connectionPsw = reader.getString("db.password");
        try {
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connectionUrl, connectionUser, connectionPsw);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }

    public String LoginSQL(String name, String pass, String ut) {
        String result = "";
        try {
            Connection conn = getSQLConnection();//根据自己的数据库信息填写对应的值
            String sql = null;
            if (ut.equals("学生登录"))
                sql = "select * from Student_login where Sno='" + name + "' and  pass='" + pass + "'";
            else if (ut.equals("教师登录"))
                sql = "select * from Teacher_login where Tno='" + name + "' and  pass='" + pass + "'";//加单引号！不然隐式转换会报错查询不出来
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                result = "1";
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result += "查询数据异常!" + e.getMessage();
        }
        return result;
    }

    public int produceSQL(String sql) { //TODO 长连接？
        int result = 0;
        try {
            Connection conn = getSQLConnection();//根据自己的数据库信息填写对应的值
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
   /* public int viewSQL(String sql) {
        int result = 0;
        try {
            Connection conn = getSQLConnection();
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }*/

    /**
     * @Description 使用PreparedStatement实现针对于不同表的通用的查询操作
     */
    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                list.add(t);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, rs);
        }
        return null;
    }


    /**
     * 关闭资源操作
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commonSQL(String sql) {
        try {
            Connection conn = getSQLConnection();//根据自己的数据库信息填写对应的值
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
