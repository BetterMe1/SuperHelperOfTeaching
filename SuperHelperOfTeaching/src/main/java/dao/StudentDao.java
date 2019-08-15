package dao;

import model.Page;
import model.Student;
import myUtil.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:学生数据库操作封装
 * @Author:Anan
 * @Date:2019/8/11
 */

public class StudentDao extends BaseDao {
	public Student studentLogin(String name,String password){//登陆用户名是学号
		String sql = "SELECT * FROM s_student WHERE sn = '"+name+"' AND PASSWORD = '"+password+"'";
		ResultSet resultSet  = query(sql);
		try {
			if(resultSet.next()){
				Student student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setSn(resultSet.getString("sn"));
				student.setPassword(resultSet.getString("password"));
				return student;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public boolean addStudent(Student student){
		String sql = "INSERT INTO s_student VALUES(null,'"+student.getSn()+"','"+student.getName()+"'"
		+ ",'" + student.getPassword() + "','" + student.getSex()
				+ "'," + student.getClazzId()+ ",'" + student.getQq()+ "','"
				+ student.getMobile() + "',null)";
		//System.out.println(sql);
		return update(sql);
	}
	public boolean editStudent(Student student) {
		if(student== null){
			return false;
		}
		String sql = " UPDATE s_student SET name='"+student.getName()+"' , gender ='"+student.getSex()
				+"', tel='" +student.getMobile()+"', qq='" +student.getQq()+"', sn='" +student.getSn()
				+"', class_id=" +student.getClazzId() +" WHERE id="+student.getId();
		//System.out.println(sql);
		return update(sql);
	}
    public boolean deleteStudent(Student student) {
	    if(student == null){
	        return false;
        }
        String sql = "DELETE FROM s_student WHERE id="+student.getId();
	    return update(sql);
    }


	public Student getStudent(int id){
		String sql = "SELECT * FROM s_student WHERE id = " + id;
		Student student = null;
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()){
				student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setClazzId(resultSet.getInt("class_id"));
				student.setMobile(resultSet.getString("tel"));
				student.setName(resultSet.getString("name"));
				student.setPassword(resultSet.getString("password"));
				student.setPhoto(resultSet.getBinaryStream("photo"));
				student.setQq(resultSet.getString("qq"));
				student.setSex(resultSet.getString("gender"));
				student.setSn(resultSet.getString("sn"));
				return student;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return student;
	}
	public List<Student> getStudentList(Student student, Page page){
		List<Student> ret = new ArrayList<Student>();
		String sql = "SELECT * FROM s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "AND name like '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " AND class_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " AND id = " + student.getId();
		}
		sql += " limit " + page.getStart() + "," + page.getRows();
		ResultSet resultSet = query(sql.replaceFirst("AND", "WHERE"));
		try {
			while(resultSet.next()){
				Student s = new Student();
				s.setId(resultSet.getInt("id"));
				s.setClazzId(resultSet.getInt("class_id"));
				s.setMobile(resultSet.getString("tel"));
				s.setName(resultSet.getString("name"));
				s.setPassword(resultSet.getString("password"));
				s.setPhoto(resultSet.getBinaryStream("photo"));
				s.setQq(resultSet.getString("qq"));
				s.setSex(resultSet.getString("gender"));
				s.setSn(resultSet.getString("sn"));
				ret.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public int getStudentListTotal(Student student){
		int total = 0;
		String sql = "SELECT COUNT(*)as total FROM s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "AND name LIKE '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " AND class_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " AND id = " + student.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("AND", "WHERE"));
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public boolean setStudentPhoto(Student student) {
		if(student== null){
			return false;
		}
		String sql = " UPDATE s_student SET photo= ?";
		try {
			PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setBinaryStream(1,student.getPhoto());
			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update(sql);
	}

	public boolean editPassword(Student student,String newPassword){
		String sql = "UPDATE s_student SET password='"+newPassword+"' WHERE id="+student.getId();
		return update(sql);
	}
}
