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
    Market market;

    public Db(Market market) {
        this.market = market;
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

    public void buyInstrument(String instrument, String qty, String price, String brokerId) {
        // TODO
    }

    public boolean checkBuyPossible(String instrument, String qty, String priceReq, String brokerId) {
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
                int id = rs.getInt("id");

            //     if (type.equalsIgnoreCase(instrument)) {
            //         if (quantity >= Integer.parseInt(qty)) {
            //             if (price <= Integer.parseInt(priceReq)) {
            //                 int leftover = quantity - Integer.parseInt(qty);
            //                 if (leftover <= 0) {
            //                     removeInstrument(id);
            //                 } else {
            //                     updateQty(id, leftover);
            //                     market.setErrorReason(null);
            //                     market.getFix().sendExecuteReport(brokerId, 0);
            //                 }
            //                 return true;
            //             } else {
            //                 market.setErrorReason("None of the listed instruments are within the price range given");
            //             }
            //         } else {
            //             market.setErrorReason("The quantity of the instrument you request is not available");
            //         }
            //     } else {
            //         market.setErrorReason("Instrument is not listed on the market");
            //     }
            // }
            // rs.close();
            // // market.setErrorReason("Could not find an applicable instrument for sale in
            // // the price range and quantity");
            // if (market.getErrorReason().equals("The quantity of the instrument you request is not available")) {
                return checkBuyMultiPossible(instrument, qty, priceReq, brokerId);
            // } else {
                // market.getFix().sendExecuteReport(brokerId, 0);
                // return false;
            }
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
        return false;

    }

    public int calcSpent(int total, int amount, int price) {
        return total + (amount * price);
    }

    public boolean checkBuyMultiPossible(String instrument, String qty, String priceReq, String brokerId) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT * FROM fruitMarket ORDER BY price ASC";
            ResultSet rs = stmt.executeQuery(sql);
            int amount = Integer.parseInt(qty);
            int totalspent = 0;
            int saved = 0;
            while (rs.next() && amount > 0) {

                // Retrieve by column name
                String type = rs.getString("type");
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");
                int id = rs.getInt("id");

                if (type.equalsIgnoreCase(instrument) && price <= Integer.parseInt(priceReq)) {
                    if (quantity > amount) {
                        totalspent = calcSpent(totalspent, amount, price);
                        // updateQty(id, quantity - amount);
                        amount = 0;
                    } else if (quantity < amount) {
                        totalspent = calcSpent(totalspent, quantity, price);
                        amount = amount - quantity;
                        // removeInstrument(id);
                    }
                }
            }
            rs.close();
            if (amount == 0) {
                amount = Integer.parseInt(qty);
                totalspent = 0;
                saved = 0;
                rs = stmt.executeQuery(sql);
                while (rs.next() && amount > 0) {

                    // Retrieve by column name
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    int price = rs.getInt("price");
                    int id = rs.getInt("id");
                    String seller = rs.getString("seller");
                    if (type.equalsIgnoreCase(instrument) && price <= Integer.parseInt(priceReq)) {
                        if (quantity > amount) {
                            totalspent = calcSpent(totalspent, amount, price);
                            updateQty(id, quantity - amount);
                            if (seller != null) 
                                market.getFix().sendSaleExecuteReport(seller, amount * price);
                            amount = 0;
                        } else if (quantity < amount) {
                            totalspent = calcSpent(totalspent, quantity, price);
                            amount = amount - quantity;
                            removeInstrument(id);
                            if (seller != null) 
                                market.getFix().sendSaleExecuteReport(seller, quantity * price);
                        }
                    }
                }
            }
            rs.close();
            if (amount == 0) {
                market.setErrorReason(null);
                saved = (Integer.parseInt(qty) * Integer.parseInt(priceReq)) - totalspent;
                market.getFix().sendExecuteReport(brokerId, saved);
                return true;
            }
            market.setErrorReason("Not enough instruments within the price range to execute the order");
            market.getFix().sendExecuteReport(brokerId, 0);
            return false;
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
        return false;

    }

    public void sellInstrument(String instrument, String qty, String price, String brokerId) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            sql = "INSERT INTO fruitmarket (type, quantity, price, seller) VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(2, Integer.parseInt(qty));
            pstmt.setInt(3, Integer.parseInt(price));
            pstmt.setString(1, instrument);
            pstmt.setString(4, brokerId);
            int i = pstmt.executeUpdate();
            System.out.println(i + " records inserted");
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

    public void removeInstrument(int id) {
        try {
            sql = "DELETE FROM fruitmarket WHERE id=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("Removal error:" + e);
        }

    }

    public void updateQty(int id, int leftover) {
        try {
            sql = "update fruitmarket set quantity=? where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, leftover);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Leftover update error:" + e);
        }
    }
}
