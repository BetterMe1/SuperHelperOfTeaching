

package servlet;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
import dao.StudentDao;
import dao.TeacherDao;
import model.Student;
import model.Teacher;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @Description: 图片处理类Servlet
 * @Author:Anan
 * @Date:2019/8/12
 */

public class PhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println(method);
        if("getPhoto".equals(method)){
            getPhoto(req,resp);
        }else if("SetPhoto".equals(method)){
            uploadPhoto(req,resp);
        }
    }

    private void uploadPhoto(HttpServletRequest req, HttpServletResponse resp) {
        Integer sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
        //System.out.println(sid);
        Integer tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));
        FileUpload fileUpload = new FileUpload(req);
        fileUpload.setFileFormat("jpg");//文件格式
        fileUpload.setFileFormat("png");
        fileUpload.setFileFormat("jpeg");
        fileUpload.setFileSize(2048);//文件大小：KB
        resp.setCharacterEncoding("UTF-8");
        try {
            InputStream fileInputStream = fileUpload.getUploadInputStream();
            if(sid != 0){//学生
                Student student = new Student();
                student.setId(sid);
                student.setPhoto(fileInputStream);
                StudentDao studentDao = new StudentDao();
                if(studentDao.setStudentPhoto(student)){
                    resp.getWriter().write("<div id='message'>上传成功</div>");
                }else{
                    resp.getWriter().write("<div id='message'>上传失败</div>");
                }
            }
            if(tid != 0){//教师
                Teacher teacher = new Teacher();
                teacher.setId(sid);
                teacher.setPhoto(fileInputStream);
                TeacherDao teacherDao = new TeacherDao();
                if(teacherDao.setTeacherPhoto(teacher)){
                    resp.getWriter().write("<div id='message'>上传成功</div>");
                }else{
                    resp.getWriter().write("<div id='message'>上传失败</div>");
                }
            }
        } catch (ProtocolException e) {
            try {
                resp.getWriter().write("<div id='message'>上传协议错误</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (NullFileException e) {
            try {
                resp.getWriter().write("<div id='message'>上传文件为空</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (SizeException e) {
            try {
                resp.getWriter().write("<div id='message'>上传文件大小不能超过"+fileUpload.getFileSize()+"KB</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (FileFormatException e) {
            try {
                resp.getWriter().write("<div id='message'>文件格式不正确（支持"+fileUpload.getFileFormat()+")</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            try {
                resp.getWriter().write("<div id='message'>读取文件出错</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (FileUploadException e) {
            try {
                resp.getWriter().write("<div id='message'>上传文件失败</div>");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    private void getPhoto(HttpServletRequest req, HttpServletResponse resp) {
        Integer sid = req.getParameter("sid") == null ? 0 : Integer.parseInt(req.getParameter("sid"));
        Integer tid = req.getParameter("tid") == null ? 0 : Integer.parseInt(req.getParameter("tid"));

        if(sid != 0){//学生
            StudentDao studentDao = new StudentDao();
            Student student = studentDao.getStudent(sid);
            studentDao.closeConnection();
            if(student != null){
                InputStream photo = student.getPhoto();
                showPhoto(req,resp,photo);
                return;
            }
        }else if(tid != 0){//教师
            TeacherDao teacherDao = new TeacherDao();
            Teacher teacher= teacherDao.getTeacher(tid);
            teacherDao.closeConnection();
            if(teacher != null){
                InputStream photo = teacher.getPhoto();
                showPhoto(req,resp,photo);
                return;
            }
        }else {
            defaultPhoto(req, resp);
        }
    }

    private void showPhoto(HttpServletRequest req, HttpServletResponse resp,InputStream photo){
        try {
            if(photo != null) {
                byte[] bytes = new byte[photo.available()];
                photo.read(bytes);
                resp.getOutputStream().write(bytes,0,bytes.length);
            }else{
                //默认照片
                defaultPhoto(req, resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void defaultPhoto(HttpServletRequest req, HttpServletResponse resp){
        String path = req.getSession().getServletContext().getRealPath("");//获取当前路径
        System.out.println(path);
        File file = new File(path +File.separator+"file" +File.separator+"头像1.JPG");
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            resp.getOutputStream().write(bytes,0,bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

