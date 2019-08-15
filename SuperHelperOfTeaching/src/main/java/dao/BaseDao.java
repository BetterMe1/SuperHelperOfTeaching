package dao;

import myUtil.DBUtil;

import java.sql.*;

/**
 * @Description:基础类封装
 * @Author:Anan
 * @Date:2019/8/10
 */
public class BaseDao {


    public Connection getConnection(){
        return DBUtil.getConnection();
    }
    /**
     * 关闭数据库连接
     */
    public void closeConnection(){
        DBUtil.closeConnection();
    }

    /**
     * 查询
     * @param sql
     * @return
     */
    public ResultSet query(String sql){
        Connection connection =null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 改变数据库内容操作:插入、更新等
     * @param sql
     */
    public boolean update(String sql){
        try {
            return DBUtil.getConnection().prepareStatement(sql).executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
