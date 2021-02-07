/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.tblsuppliers;

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
public class SuppliersDAO extends Vector<SuppliersDTO> {

    public void loadFromDB() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "select supCode, supName, address, collaborating "
                        + "from tblSuppliers ";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String supCode = rs.getString(1);
                    String supName = rs.getString(2);
                    String address = rs.getString(3);
                    boolean colloborating = rs.getBoolean(4);
                    SuppliersDTO supplier = new SuppliersDTO(supCode, supName, address, colloborating);
                    this.add(supplier);
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

    public static boolean insert(SuppliersDTO s) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Insert tblSuppliers "
                        + "(supCode, supName, address, collaborating) "
                        + "Values(?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, s.getSupCode());
                ps.setString(2, s.getSupName());
                ps.setString(3, s.getAddress());
                ps.setBoolean(4, s.isColloborating());
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

    public static boolean update(SuppliersDTO s) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "update tblSuppliers set supName = ?,address = ?, collaborating = ?"
                        + " where supCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, s.getSupName());
                ps.setString(2, s.getAddress());
                ps.setBoolean(3, s.isColloborating());
                ps.setString(4, s.getSupCode());
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
                String sql = "delete from tblSuppliers "
                        + "where supCode = ?";
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

    public static SuppliersDTO getSupByCode(String code) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Select supCode, supName, address, collaborating "
                        + "From tblSuppliers "
                        + "Where SupCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, code);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String supCode = rs.getString(1);
                    String supName = rs.getString(2);
                    String address = rs.getString(3);
                    boolean colloborating = rs.getBoolean(4);
                    SuppliersDTO s = new SuppliersDTO(supCode, supName, address, colloborating);
                    return s;
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

    public static String getItemBySupCode(String supCode) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "select itemCode "
                        + "from tblItems "
                        + "where supCode = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, supCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String s = rs.getString(1);
                    return s;
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

    public int find(String supCode) {
        for (int i = 0; i < this.size(); i++) {
            if (supCode.equals(this.get(i).getSupCode())) {
                return i;
            }
        }
        return -1;
    }

    public SuppliersDTO findSupplier(String supCode) {
        int i = find(supCode);
        return i < 0 ? null : this.get(i);
    }
}
