/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.tblitems;

import bachnv.tblsuppliers.SuppliersDAO;
import bachnv.tblsuppliers.SuppliersDTO;
import bachnv.util.DBAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author ngvba
 */
public class ItemsDAO extends Vector<ItemsDTO> {

    public void loadFromDB(SuppliersDAO suppliers) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Select itemCode, itemName, unit, price, supplying, supCode "
                        + "From tblItems ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String itemCode = rs.getString(1);
                    String itemName = rs.getString(2);
                    String unit = rs.getString(3);
                    Float price = rs.getFloat(4);
                    boolean supplying = rs.getBoolean(5);
                    String supplierCode = rs.getString(6);
                    SuppliersDTO supplier = suppliers.findSupplier(supplierCode);
                    ItemsDTO item = new ItemsDTO(itemCode, itemName, supplier, unit, price, supplying);
                    this.add(item);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public static boolean insert(ItemsDTO i) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Insert tblItems "
                        + "(itemCode, itemName, unit, price, supplying, supCode) "
                        + "Values(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, i.getItemCode());
                ps.setString(2, i.getItemName());
                ps.setString(3, i.getUnit());
                ps.setFloat(4, i.getPrice());
                ps.setBoolean(5, i.isSupplying());
                ps.setString(6, i.getSuppiler().getSupCode());
                int result = ps.executeUpdate();
                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public static boolean update(ItemsDTO i) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "update tblItems set itemName = ?,supCode = ?, unit = ?, price = ?, supplying = ?"
                        + " where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, i.getItemName());
                ps.setString(2, i.getSuppiler().getSupCode());
                ps.setString(3, i.getUnit());
                ps.setFloat(4, i.getPrice());
                ps.setBoolean(5, i.isSupplying());
                ps.setString(6, i.getItemCode());
                int result = ps.executeUpdate();
                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public static boolean delete(String id) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "delete from tblItems "
                        + "where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, id);
                int result = ps.executeUpdate();
                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public static ItemsDTO getItemByCode(SuppliersDAO suppliers, String code) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Select itemCode, itemName, unit, price, supplying, supCode "
                        + "From tblItems "
                        + "Where itemCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String itemCode = rs.getString(1);
                    String itemName = rs.getString(2);
                    String unit = rs.getString(3);
                    Float price = rs.getFloat(4);
                    boolean supplying = rs.getBoolean(5);
                    String supplierCode = rs.getString(6);
                    SuppliersDTO supplier = suppliers.findSupplier(supplierCode);
                    ItemsDTO i = new ItemsDTO(itemCode, itemName, supplier, unit, price, supplying);
                    return i;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }
}
