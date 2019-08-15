package dao;

import model.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description：管理员数据库操作封装
 * @Author:Anan
 * @Date:2019/8/10
 */
public class AdminDao extends BaseDao{
    public Admin adminLogin(String name,String password){
        String sql = "SELECT * FROM s_admin WHERE NAME = '"+name+"' AND PASSWORD = '"+password+"'";
        ResultSet resultSet  = query(sql);
        try {
            if(resultSet.next()){
                Admin admin = new Admin();
                admin.setId(resultSet.getInt("id"));
                admin.setName(resultSet.getString("name"));
                admin.setPassword(resultSet.getString("password"));
                admin.setStatus(resultSet.getInt("status"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改密码
     * @param admin
     * @param newPassword
     */
    public boolean editPassword(Admin admin,String newPassword){
       String sql = "UPDATE s_admin SET password='"+newPassword+"' WHERE id="+admin.getId();
       return update(sql);
    }
}
