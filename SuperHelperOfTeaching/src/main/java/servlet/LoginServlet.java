package servlet;

import dao.AdminDao;
import dao.StudentDao;
import dao.TeacherDao;
import model.Admin;
import model.Student;
import model.Teacher;
import myUtil.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description:登陆Servlet
 * @Author:Anan
 * @Date:2019/8/10
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        resp.setContentType("text/html;charset=utf-8");
        String method = req.getParameter("method");
        System.out.println(method);
        if("loginout".equals(method)){
            loginout(req,resp);
            return;
        }
        //验证验证码
        String loginCaptcha = req.getSession().getAttribute("loginCaptcha").toString();//获取生成的验证码
        String vcode = req.getParameter("vcode");//获取输入的验证码
        if (StringUtil.isEmpty(loginCaptcha) || !(vcode.toUpperCase().equals(loginCaptcha.toUpperCase()))) {
            resp.getWriter().write("vcodeError");
            return;
        }
        //对比用户名、密码是否正确
        String name = req.getParameter("account");
        String password = req.getParameter("password");
        Integer type = Integer.parseInt(req.getParameter("type"));
        String loginStatus = "LoginFailed";
        switch (type){
            //管理员
            case 1:{
                AdminDao adminDao = new AdminDao();
                Admin admin = adminDao.adminLogin(name, password);
                adminDao.closeConnection();
                if(admin == null){
                    resp.getWriter().write("loginError");
                    return;
                }
                HttpSession session = req.getSession();
                session.setAttribute("user",admin);
                session.setAttribute("userType",type);
                loginStatus = "LoginSuccess";
                break;
            }
            //学生：
            case 2:{
                StudentDao studentDao = new StudentDao();
                Student student = studentDao.studentLogin(name,password);
                studentDao.closeConnection();
                if(student == null){
                    resp.getWriter().write("loginError");
                    return;
                }
                HttpSession session = req.getSession();
                session.setAttribute("user",student);
                session.setAttribute("userType",type);
                loginStatus = "LoginSuccess";
                break;
            }
            //教师：
            case 3:{
                TeacherDao teacherDao = new TeacherDao();
                Teacher teacher = teacherDao.teacherLogin(name,password);
                teacherDao.closeConnection();
                if(teacher == null){
                    resp.getWriter().write("loginError");
                    return;
                }
                HttpSession session = req.getSession();
                session.setAttribute("user",teacher);
                session.setAttribute("userType",type);
                loginStatus = "LoginSuccess";
                break;
            }
            default:{
                break;
            }
        }
        resp.getWriter().write(loginStatus);
    }

    private void loginout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("user");
        req.getSession().removeAttribute("userType");
        resp.sendRedirect("index.jsp");
    }
}
