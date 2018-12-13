//package com.ppaper.norbi.whatwillweeattoday.config;
//
//import android.content.Context;
//import android.os.Environment;
//
//import com.ppaper.norbi.whatwillweeattoday.domain.Food;
//import com.ppaper.norbi.whatwillweeattoday.exception.WWWETException;
//
//import junit.framework.Assert;
//
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JdbcDriver {
//
//    // JDBC driver name and database URL
//    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    final String DB_URL = "jdbc:mysql://192.168.1.104:3306/receipts";
////    static final String DB_URL = "jdbc:mysql://den1.mysql2.gear.host:3306/adventlog";
//
//    //  Database credentials
//    private final String USER = "root";
//    private final String PASS = "password";
////
////    static final String USER = "adventlog";
////    static final String PASS = "Pe180g_lc5-3";
//
//
//    public Food getFoodtById(int id) {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        Food food = null;
//        String sql;
//        sql = "SELECT * FROM dinner WHERE id=?";
//        ResultSet rs = null;
//        try {
//            Class.forName(JDBC_DRIVER);
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, id);
//            rs = pstmt.executeQuery();
//
//            food = new Food();
//            while (rs.next()) {
//
//                food.setName(rs.getString("name"));
//
//
//                InputStream picture = rs.getBinaryStream("picture");
//                if (picture != null) {
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"temp");
//                    file.deleteOnExit();
//                    FileUtils.copyInputStreamToFile(picture, file);
//                    food.setPicture(file);
//                }
//                InputStream receipt = rs.getBinaryStream("receipt");
//                food.setReceipt(IOUtils.toString(receipt, StandardCharsets.UTF_8));
//
//                food.setName(rs.getString("name"));
//
//            }
//            //STEP 6: Clean-up environment
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            closeConnection(conn, pstmt);
//        }
//        return food;
//    }
//
//    private void closeConnection(Connection conn, Statement stmt) {
//        try {
//            stmt.close();
//            conn.close();
//        } catch (SQLException se) {
//
//            se.printStackTrace();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        } finally {
//
//            try {
//                if (stmt != null)
//                    stmt.close();
//            } catch (SQLException se2) {
//                se2.printStackTrace();
//            }
//            try {
//                if (conn != null)
//                    conn.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }
//    }
//
//    public void uploadFood(Food food) throws WWWETException {
//        Assert.assertNotNull(food);
//        Assert.assertNotNull(food.getName());
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//
//        String sql;
//        sql = "INSERT INTO dinner VALUES (NULL, ?, ?, ?);";
//        ResultSet rs = null;
//        try {
//            Class.forName(JDBC_DRIVER);
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, food.getName());
//            pstmt.setBinaryStream(2, (food.getReceipt() == null) ? null : new ByteArrayInputStream(food.getReceipt().getBytes(StandardCharsets.UTF_8)));
//            pstmt.setBinaryStream(3, (food.getPicture() == null) ? null : new FileInputStream(food.getPicture()));
//            pstmt.executeUpdate();
//
//            System.out.println("Food uploaded succesful.");
//        } catch (SQLException e) {
//            if (e.getMessage().contains("Duplicate entry")) {
//                throw new WWWETException("Food name is already in database.");
//            } else {
//                e.printStackTrace();
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            closeConnection(conn, pstmt);
//        }
//    }
//
//    public List<String> getAllFoodName() {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//
//        List<String> result = new ArrayList<>();
//        String sql;
//        sql = "SELECT name FROM dinner";
//        ResultSet rs = null;
//        try {
//            Class.forName(JDBC_DRIVER);
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            pstmt = conn.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//
//                result.add(rs.getString("name"));
//
//            }
//            //STEP 6: Clean-up environment
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            closeConnection(conn, pstmt);
//        }
//        return result;
//    }
//}
