package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.IAspectsDao;
import de.enwida.web.model.Right;

public class AspectDaoImpl implements IAspectsDao {
    
    @Autowired
    private DriverManagerDataSource datasource;
    
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);

    public List<Right> getAllAspects(long roleID) throws Exception {
        String sql = "select * FROM users.rights";
        Connection conn = null;
        ArrayList<Right> rights = new ArrayList<Right>();
//        try {
//            conn = datasource.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Right right = new Right();
//                right.setRightID(rs.getLong("right_id"));
//                right.setAspectName(rs.getLong("aspect_id"));
//                right.setRoleID(rs.getLong("role_id"));
//                right.setResolution(rs.getString("resolution"));
//                right.setProduct(rs.getString("product"));
//                right.setTso(rs.getString("tso"));
//                right.setEnabled(rs.getBoolean("enabled"));
//                rights.add(right);
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (conn != null) {
//                try {
//                conn.close();
//                } catch (SQLException e) {
//                    logger.error(e.getMessage());
//                }
//            }
//        }
        return rights;
    }

}
