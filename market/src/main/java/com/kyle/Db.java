package com.kyle;

import java.sql.*;

public class Db {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/fixme";

    static final String USER = "root";
    static final String PASS = "admin";

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    String sql;

    public Db() {
        // TODO
    }

    public String getInstruments() {
        String msg = "**These are the instruments currently listed on the fruit market**\n";
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT * FROM fruitMarket";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Retrieve by column name
                String type = rs.getString("type");
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");

                // Display values

                msg += ("TYPE " + type);
                msg += (" | QUANTITY " + quantity);
                msg += (" | PRICE " + price + "\n");
                // stmt.close();
                // conn.close();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                System.out.println(se2);
            } // nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                System.out.println(se);
            }
        }

        return msg;
    }

    public void sellInstrument(String instrument, String qty, String price, String brokerId){
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "INSERT INTO fruitmarket (type, quantity, price, seller) VALUES (?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, Integer.parseInt(qty));
            pstmt.setInt(3, Integer.parseInt(price));
            pstmt.setString(1, instrument);
            pstmt.setString(4, brokerId);
            int i=pstmt.executeUpdate();  
            System.out.println(i+" records inserted");  
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                System.out.println(se2);
            } // nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                System.out.println(se);
            }
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
                System.out.println(se);
            }
        }

    }
}
