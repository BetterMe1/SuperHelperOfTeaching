package dao;

import model.Clazz;
import model.Page;
import myUtil.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:班级数据库操作封装
 * @Author:Anan
 * @Date:2019/8/11
 */
public class ClazzDao extends BaseDao{
    public List<Clazz>  getClazzList(Clazz clazz, Page page) {
        List<Clazz> ret = new ArrayList<>();
        String sql = "SELECT * FROM s_clazz ";
        if(!StringUtil.isEmpty(clazz.getName())){
            sql += "WHERE clazzName LIKE '%" + clazz.getName() + "%'";
        }
        sql += " limit " + page.getStart() + "," + page.getRows();
        ResultSet resultSet = query(sql);
        try {
            while(resultSet.next()){
                Clazz cl = new Clazz();
                cl.setId(resultSet.getInt("id"));
                cl.setName(resultSet.getString("clazzName"));
                cl.setInfo(resultSet.getString("info"));
                ret.add(cl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int  getClazzListTotal(Clazz clazz) {
        int total = 0;
        String sql = "SELECT COUNT(*) as total FROM s_clazz ";
        if(!StringUtil.isEmpty(clazz.getName())){
            sql += "WHERE clazzName LIKE '%" + clazz.getName() + "%'";
        }
        ResultSet resultSet = query(sql);
        try {
            while(resultSet.next()){
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    public boolean addClazz(Clazz clazz) {
        if(clazz== null){
            return false;
        }
        String sql = "INSERT INTO s_clazz (clazzName,info) VALUES('"+clazz.getName()+"','"+clazz.getInfo()+"')";
        return update(sql);
    }

    public boolean deleteClazz(Clazz clazz) {
        if(clazz== null){
            return false;
        }
        String sql = " DELETE FROM s_clazz WHERE id="+clazz.getId();
        return update(sql);
    }
    public boolean editClazz(Clazz clazz) {
        if(clazz== null){
            return false;
        }
        String sql = " UPDATE s_clazz SET clazzName='"+clazz.getName()+"' , info ='"+clazz.getInfo()+"' WHERE id="+clazz.getId();
        return update(sql);
    }
}
