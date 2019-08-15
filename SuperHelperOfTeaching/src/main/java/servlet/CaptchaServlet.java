package servlet;

import myUtil.CaptchaUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Description:验证码Servlet
 * @Author:Anan
 * @Date:2019/8/9
 */
public class CaptchaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    /**
     * 生成验证码图片
     * @param req
     * @param resp
     */
    private void generateLoginCaptcha(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CaptchaUtil captchaUtil = new CaptchaUtil();
        String generatorVCode = captchaUtil.generatorVCode();//生成验证码
        req.getSession().setAttribute("loginCaptcha",generatorVCode);
        BufferedImage rotateVCodeImage = captchaUtil.generatorRotateVCodeImage(generatorVCode, true);//生成旋转图片
        ImageIO.write(rotateVCodeImage,"gif",resp.getOutputStream());//写入图像到ImageOutputStream
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        resp.setContentType("text/html;charset=utf-8");
        String method = req.getParameter("method");
        if("loginCaptcha".equals(method)){
            generateLoginCaptcha(req,resp);
            return;
        }
        resp.getWriter().write("error method");
    }
}
