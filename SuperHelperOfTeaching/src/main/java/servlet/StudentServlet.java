package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StudentDao;
import model.Page;
import model.Student;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @Description:学生信息管理功能实现Servlet
 * @Author:Anan
 * @Date:2019/8/11
 */
public class StudentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }
    public void doPost(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        String method = req.getParameter("method");
        if("toStudentListView".equals(method)){
            studentList(req,resp);
        }else if("AddStudent".equals(method)){
            addStudent(req,resp);
        }else if("StudentList".equals(method)){
            getStudentList(req,resp);
        }else if("EditStudent".equals(method)){
            editStudent(req,resp);
        }else if("DeleteStudent".equals(method)){
            deleteStudent(req,resp);
        }
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) {
        String[] ids = req.getParameterValues("ids[]");
        StudentDao studentDao = new StudentDao();
        boolean flag = false;
        for(String id : ids){
            Student student = new Student();
            student.setId(Integer.parseInt(id));
            if(!studentDao.deleteStudent(student)){
                flag = true;
            }
        }
        studentDao.closeConnection();
        if(!flag){//全部删除成功
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editStudent(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String mobile = req.getParameter("mobile");
        String qq = req.getParameter("qq");
        int clazzId = Integer.parseInt(req.getParameter("clazzid"));
        String sn = req.getParameter("sn");
        Student student = new Student();
        student.setClazzId(clazzId);
        student.setMobile(mobile);
        student.setName(name);
        student.setId(id);
        student.setQq(qq);
        student.setSex(sex);
        student.setSn(sn);
        StudentDao studentDao = new StudentDao();
        try {
            if(studentDao.editStudent(student)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            studentDao.closeConnection();
        }
    }
    private void getStudentList(HttpServletRequest req,
                                HttpServletResponse resp) {
        String name = req.getParameter("studentName");
        Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
        Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazzid"));
        //获取当前登录用户类型
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        Student student = new Student();
        student.setName(name);
        student.setClazzId(clazz);
        if(userType == 2){ //如果是学生，只能查看自己的信息
            Student currentUser = (Student)req.getSession().getAttribute("user");
            student.setId(currentUser.getId());
        }
        StudentDao studentDao = new StudentDao();
        List<Student> clazzList = studentDao.getStudentList(student, new Page(currentPage, pageSize));
        int total = studentDao.getStudentListTotal(student);
        studentDao.closeConnection();
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> ret = new HashMap<>();
        ret.put("total", total);
        ret.put("rows", clazzList);
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                resp.getWriter().write(JSONArray.fromObject(clazzList).toString());
            }else{
                resp.getWriter().write(JSONObject.fromObject(ret).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addStudent(HttpServletRequest req,
                            HttpServletResponse resp) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String sex = req.getParameter("sex");
        String mobile = req.getParameter("mobile");
        String qq = req.getParameter("qq");
        int clazzId = Integer.parseInt(req.getParameter("clazzid"));
        String sn = req.getParameter("sn");
        Student student = new Student();
        student.setClazzId(clazzId);
        student.setMobile(mobile);
        student.setName(name);
        student.setPassword(password);
        student.setQq(qq);
        student.setSex(sex);
        student.setSn(sn);
        StudentDao studentDao = new StudentDao();
        if(studentDao.addStudent(student)){
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                studentDao.closeConnection();
            }
        }
    }
    private void studentList(HttpServletRequest req,
                             HttpServletResponse resp) throws IOException {
        try {
            req.getRequestDispatcher("view/student/studentList.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}






/*
package servlet;

import dao.StudentDao;
import model.Page;
import model.Student;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * @Description:学生信息管理功能实现Servlet
 * @Author:Anan
 * @Date:2019/8/11
 *//*

public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toStudentListView".equals(method)){
            studentList(req,resp);
        }else if("AddStudent".equals(method)){
            addStudent(req,resp);
        }else if("StudentList".equals(method)){
            getStudentList(req,resp);
        }
    }

    private void getStudentList(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("studentName");
        Integer currentPage = req.getParameter("page") == null ? 1 :Integer.parseInt(req.getParameter("page"));//当前页面
        Integer rows = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));//显示行数
        Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazz"));
        Student student = new Student();
        student.setName(name);
        System.out.println(name);
        student.setClazzId(clazz);
        StudentDao studentDao = new StudentDao();
        List<Student> studentList = studentDao.getStudentList(student,new Page(currentPage,rows));
        int total = studentDao.getStudentListTotal(student);
        resp.setCharacterEncoding("UTF-8");
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",studentList);
        studentDao.closeConnection();
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                resp.getWriter().write(JSONArray.fromObject(studentList).toString());
            }else{
                resp.getWriter().write(JSONObject.fromObject(map).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String gender = req.getParameter("sex");
        String tel = req.getParameter("mobile");
        String qq = req.getParameter("qq");
        Integer clazzId = Integer.parseInt(req.getParameter("clazzid"));
        String sn = req.getParameter("sn");
        Student student = new Student();
        student.setClazzId(clazzId);
        student.setGender(gender);
        student.setQq(qq);
        student.setSn(sn);
        student.setName(name);
        student.setPassword(password);
        student.setTel(tel);
        StudentDao studentDao = new StudentDao();
        try {
            if(studentDao.addStudent(student)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            studentDao.closeConnection();
        }
    }

    private void studentList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("view/student/studentList.jsp").forward(req,resp);
    }
}
*/
