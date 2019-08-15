package servlet;

import dao.AdminDao;
import dao.StudentDao;
import dao.TeacherDao;
import model.Admin;
import model.Student;
import model.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:系统登录后主界面
 * @Author:Anan
 * @Date:2019/8/10
 */
public class SystemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toPersonalView".equals(method)){
            personalView(req,resp);
            return;
        }else if("EditPasswod".equals(method)){
            editPassword(req,resp);
            return;
        }
        req.getRequestDispatcher("view/admin/system.jsp").forward(req,resp);
    }

    private void editPassword(HttpServletRequest req, HttpServletResponse resp) {
        String password = req.getParameter("password");//旧密码
        String newPassword = req.getParameter("newpassword");//新密码
        resp.setCharacterEncoding("UTF-8");
        int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
        if(userType == 1){
            //管理员
            Admin admin = (Admin)req.getSession().getAttribute("user");
            if(!checkPassword(req,resp,admin.getPassword(),password)){//原密码输入错误
                return;
            }
            AdminDao adminDao = new AdminDao();
            boolean flag = adminDao.editPassword(admin, newPassword);//修改密码是否成功
            adminDao.closeConnection();
            checkExitPassword(req,resp,flag);
        }
        if(userType == 2){//学生
            Student student = (Student)req.getSession().getAttribute("user");
            if(!checkPassword(req,resp,student.getPassword(),password)){
                return;
            }
            StudentDao studentDao = new StudentDao();
            boolean flag = studentDao.editPassword(student, newPassword);
            studentDao.closeConnection();
            checkExitPassword(req, resp,flag);
        }
        if(userType == 3){
            //教师
            Teacher teacher = (Teacher)req.getSession().getAttribute("user");
            if(!checkPassword(req,resp,teacher.getPassword(),password)){
                return;
            }
            TeacherDao teacherDao = new TeacherDao();
            boolean flag = teacherDao.editPassword(teacher, newPassword);
            teacherDao.closeConnection();
            checkExitPassword(req,resp,flag);
        }
    }
    private boolean checkExitPassword(HttpServletRequest req, HttpServletResponse resp,boolean flag){
        if(flag){
            try {
                resp.getWriter().write("success");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            resp.getWriter().write("数据库修改错误");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }






    /**
     * 检查用户输入的原密码是否正确
     * @param req
     * @param resp
     * @param userPassword  用户输入密码
     * @param password   原密码
     * @return
     */
    private boolean checkPassword(HttpServletRequest req, HttpServletResponse resp,
                                  String userPassword,String password){
        if(!userPassword.equals(password)){
            try {
                resp.getWriter().write("原密码错误！");
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    private void personalView(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("view/other/personalView.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
