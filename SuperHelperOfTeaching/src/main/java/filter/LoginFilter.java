package filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:拦截用户在未登陆状态下的操作
 * @Author:Anan
 * @Date:2019/8/10
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Object user = req.getSession().getAttribute("user");
        if(user == null){
            resp.sendRedirect("index.jsp");
            return;
        }else {
            chain.doFilter(req,resp);
        }
    }

    @Override
    public void destroy() {

    }
}
