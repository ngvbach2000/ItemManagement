/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachnv.tblusers;

import bachnv.util.DBAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ngvba
 */
public class UsersDAO {
    
    public String checkLogin(String username, String password) throws SQLException{
        //check if username have fullname --> login success
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBAccess.openConnection();
            if (con != null) {
                String sql = "Select fullName "
                        + "From tblUsers "
                        + "Where userID = ? and password = ? and status = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setBoolean(3, true);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString(1);
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
        return "";
    }
    
}
