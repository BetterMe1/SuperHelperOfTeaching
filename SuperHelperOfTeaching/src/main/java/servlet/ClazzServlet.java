package servlet;

import dao.ClazzDao;
import model.Clazz;
import model.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:班级信息管理Servlet
 * @Author:Anan
 * @Date:2019/8/11
 */
public class ClazzServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("toClazzListView".equals(method)){
            clazzList(req,resp);
        }else if("getClazzList".equals(method)){
            getClazzList(req, resp);
        }else if("AddClazz".equals(method)){
            addClazz(req,resp);
        }else if("DeleteClazz".equals(method)){
            deleteClazz(req,resp);
        }else if("EditClazz".equals(method)){
            editClazz(req,resp);
        }
    }

    private void editClazz(HttpServletRequest req, HttpServletResponse resp) {
        Integer id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String info = req.getParameter("info");
        System.out.println(id);
        Clazz clazz = new Clazz();
        clazz.setId(id);
        clazz.setName(name);
        clazz.setInfo(info);
        ClazzDao clazzDao = new ClazzDao();
        try {
            if (clazzDao.editClazz(clazz)) {
                resp.getWriter().write("success");
            }
        } catch(IOException e){
            e.printStackTrace();
        }finally{
            clazzDao.closeConnection();
        }
    }

    private void deleteClazz(HttpServletRequest req, HttpServletResponse resp) {
        Integer id = Integer.parseInt(req.getParameter("clazzid"));
        Clazz clazz = new Clazz();
        clazz.setId(id);
        ClazzDao clazzDao = new ClazzDao();
        try {
            if(clazzDao.deleteClazz(clazz)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clazzDao.closeConnection();
        }
    }


    private void addClazz(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String info = req.getParameter("info");
        Clazz clazz = new Clazz();
        clazz.setName(name);
        clazz.setInfo(info);
        ClazzDao clazzDao = new ClazzDao();
        try {
            if(clazzDao.addClazz(clazz)) {
                resp.getWriter().write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clazzDao.closeConnection();
        }
    }

    private void clazzList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("view/admin/clazzList.jsp").forward(req,resp);
    }

    private void getClazzList(HttpServletRequest req, HttpServletResponse resp)  {
        String name = req.getParameter("clazzName");
        Integer currentPage = req.getParameter("page") == null ? 1 :Integer.parseInt(req.getParameter("page"));//当前页面
        Integer rows = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));//显示行数
        Clazz clazz = new Clazz();
        clazz.setName(name);
        ClazzDao clazzDao = new ClazzDao();
        int total = clazzDao.getClazzListTotal(clazz);
        List<Clazz> clazzList = clazzDao.getClazzList(clazz, new Page(currentPage, rows));
       /* //json字符串
        JsonConfig jsonConfig = new JsonConfig();
        String clazzListString = JSONArray.fromObject(clazzList, jsonConfig).toString();*/
        resp.setCharacterEncoding("UTF-8");
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",clazzList);
        try {
            String from = req.getParameter("from");
            if("combox".equals(from)){
                resp.getWriter().write(JSONArray.fromObject(clazzList).toString());
            }else{
                resp.getWriter().write(JSONObject.fromObject(map).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clazzDao.closeConnection();
    }
}
