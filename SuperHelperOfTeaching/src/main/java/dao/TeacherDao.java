package dao;

import model.Page;
import model.Teacher;
import myUtil.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:教师表数据数操作封装
 * @Author:Anan
 * @Date:2019/8/14
 */
public class TeacherDao extends BaseDao {

    public Teacher teacherLogin(String name,String password){//登陆用户名是工号
        String sql = "SELECT * FROM s_teacher WHERE sn = '"+name+"' AND PASSWORD = '"+password+"'";
        ResultSet resultSet  = query(sql);
        try {
            if(resultSet.next()){
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getInt("id"));
                teacher.setSn(resultSet.getString("sn"));
                teacher.setPassword(resultSet.getString("password"));
                return teacher;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean addTeacher(Teacher teacher){
        String sql = "INSERT INTO s_teacher VALUES(null,'"+teacher.getSn()+"','"+teacher.getName()
                + "','" + teacher.getPassword()+ "'," + teacher.getClazzId()
                + ",'" +teacher.getSex() + "','"  + teacher.getMobile()+ "','"
                + teacher.getQq() + "',null)";
        System.out.println(sql);
        return update(sql);
    }
    public boolean editTeacher(Teacher teacher) {
        if(teacher== null){
            return false;
        }
        String sql = " UPDATE s_teacher SET name='"+teacher.getName()+"' , sex ='"+teacher.getSex()
                +"', mobile='" +teacher.getMobile()+"', qq='" +teacher.getQq()+"', sn='" +teacher.getSn()
                +"', clazz_id=" +teacher.getClazzId() +" WHERE id="+teacher.getId();
        //System.out.println(sql);
        return update(sql);
    }

    public boolean deleteTeacher(Teacher teacher) {
        if(teacher == null){
            return false;
        }
        String sql = "DELETE FROM s_teacher WHERE id="+teacher.getId();
        return update(sql);
    }


    public Teacher getTeacher(int id){
        String sql = "SELECT * FROM s_teacher WHERE id = " + id;
        Teacher teacher = null;
        ResultSet resultSet = query(sql);
        try {
            if(resultSet.next()){
                teacher = new Teacher();
                teacher.setId(resultSet.getInt("id"));
                teacher.setClazzId(resultSet.getInt("clazz_id"));
                teacher.setMobile(resultSet.getString("mobile"));
                teacher.setName(resultSet.getString("name"));
                teacher.setPassword(resultSet.getString("password"));
                teacher.setPhoto(resultSet.getBinaryStream("photo"));
                teacher.setQq(resultSet.getString("qq"));
                teacher.setSex(resultSet.getString("sex"));
                teacher.setSn(resultSet.getString("sn"));
                return teacher;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacher;
    }
    public List<Teacher> getTeacherList(Teacher teacher, Page page){
        List<Teacher> ret = new ArrayList<>();
        String sql = "SELECT * FROM s_teacher ";
        if(!StringUtil.isEmpty(teacher.getName())){
            sql += "AND name like '%" + teacher.getName() + "%'";
        }
        if(teacher.getClazzId() != 0){
            sql += " AND clazz_id = " + teacher.getClazzId();
        }
        if(teacher.getId() != 0){
            sql += " AND id = " + teacher.getId();
        }
        sql += " limit " + page.getStart() + "," + page.getRows();
        ResultSet resultSet = query(sql.replaceFirst("AND", "WHERE"));
        try {
            while(resultSet.next()){
                Teacher t = new Teacher();
                t.setId(resultSet.getInt("id"));
                t.setClazzId(resultSet.getInt("clazz_id"));
                t.setMobile(resultSet.getString("mobile"));
                t.setName(resultSet.getString("name"));
                t.setPassword(resultSet.getString("password"));
                t.setPhoto(resultSet.getBinaryStream("photo"));
                t.setQq(resultSet.getString("qq"));
                t.setSex(resultSet.getString("sex"));
                t.setSn(resultSet.getString("sn"));
                ret.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    public int getTeacherListTotal(Teacher teacher){
        int total = 0;
        String sql = "SELECT COUNT(*)as total FROM s_teacher ";
        if(!StringUtil.isEmpty(teacher.getName())){
            sql += "AND name LIKE '%" + teacher.getName() + "%'";
        }
        if(teacher.getClazzId() != 0){
            sql += " AND clazz_id = " + teacher.getClazzId();
        }
        if(teacher.getId() != 0){
            sql += " AND id = " + teacher.getId();
        }
        ResultSet resultSet = query(sql.replaceFirst("AND", "WHERE"));
        try {
            while(resultSet.next()){
                total = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public boolean setTeacherPhoto(Teacher teacher) {
        if(teacher== null){
            return false;
        }
        String sql = " UPDATE s_teacher SET photo= ?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setBinaryStream(1,teacher.getPhoto());
            return preparedStatement.executeUpdate() > 0;
        }  catch (Exception e) {
            e.printStackTrace();
    }
        return update(sql);
    }
    public boolean editPassword(Teacher teacher,String newPassword){
        String sql = "UPDATE s_teacher SET password='"+newPassword+"' WHERE id="+teacher.getId();
        return update(sql);
    }
}
