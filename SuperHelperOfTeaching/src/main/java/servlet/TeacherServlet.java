package servlet;

import dao.TeacherDao;
import model.Page;
import model.Teacher;
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

/**
 * @Description:教师信息管理Servlet类
 * @Author:Anan
 * @Date:2019/8/14
 */
public class TeacherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toTeacherListView".equals(method)){
            teacherList(req,resp);
        }else if("AddTeacher".equals(method)){
            addTeacher(req,resp);
        }else if("TeacherList".equals(method)){
            getTeacherList(req,resp);
        }else if("EditTeacher".equals(method)){
            editTeacher(req,resp);
        }else if("DeleteTeacher".equals(method)){
            deleteTeacher(req,resp);
        }
    }
    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) {
        String[] ids = req.getParameterValues("ids[]");
        TeacherDao teacherDao = new TeacherDao();
        boolean flag = false;
        for(String id : ids){
            Teacher teacher = new Teacher();
            teacher.setId(Integer.parseInt(id));
            if(!teacherDao.deleteTeacher(teacher)){
                flag = true;
            }
        }
        teacherDao.closeConnection();
        if(!flag){//全部删除成功
            try {
                resp.getWriter().write("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editTeacher(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String mobile = req.getParameter("mobile");
        String qq = req.getParameter("qq");
        int clazzId = Integer.parseInt(req.getParameter("clazzid"));
        String sn = req.getParameter("sn");
        Teacher teacher = new Teacher();
        teacher.setClazzId(clazzId);
        teacher.setMobile(mobile);
        teacher.setName(name);
        teacher.setId(id);
        teacher.setQq(qq);
        teacher.setSex(sex);
        teacher.setSn(sn);
        TeacherDao teacherDao= new TeacherDao();
        try {
            if(teacherDao.editTeacher(teacher)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            teacherDao.closeConnection();
        }
    }
    private void getTeacherList(HttpServletRequest req,
                                HttpServletResponse resp) {
        String name = req.getParameter("teacherName");
        Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
        Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazzid"));
        System.out.println(clazz);
        //获取当前登录用户类型
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setClazzId(clazz);
        if(userType == 3){
            Teacher currentUser = (Teacher) req.getSession().getAttribute("user");
            teacher.setId(currentUser.getId());
        }
        TeacherDao teacherDao = new TeacherDao();
        List<Teacher> teacherList = teacherDao.getTeacherList(teacher, new Page(currentPage, pageSize));
        int total = teacherDao.getTeacherListTotal(teacher);
        teacherDao.closeConnection();
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> ret = new HashMap<>();
        ret.put("total", total);
        ret.put("rows", teacherList);
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                resp.getWriter().write(JSONArray.fromObject(teacherList).toString());
            }else{
                resp.getWriter().write(JSONObject.fromObject(ret).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addTeacher(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String sex = req.getParameter("sex");
        String mobile = req.getParameter("mobile");
        String qq = req.getParameter("qq");
        int clazzId = Integer.parseInt(req.getParameter("clazzid"));
        String sn = req.getParameter("sn");
        Teacher teacher = new Teacher();
        teacher.setClazzId(clazzId);
        teacher.setMobile(mobile);
        teacher.setName(name);
        teacher.setPassword(password);
        teacher.setQq(qq);
        teacher.setSex(sex);
        teacher.setSn(sn);
        TeacherDao teacherDao = new TeacherDao();
        try {
            if(teacherDao.addTeacher(teacher)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            teacherDao.closeConnection();
        }
    }

    private void teacherList(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("view/teacher/teacherList.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
