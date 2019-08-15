package myUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description:封装数据库相关操作
 * @Author:Anan
 * @Date:2019/8/10
 */
public class DBUtil {
    private static String url;
    private static String username;
    private static String password;
    private static Connection connection = null;

    /**
     * 加载配置信息
     * @param fileName
     * @return
     */
    private static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        // 将配置文件转为输入流
        InputStream in = DBUtil.class.getClassLoader()
                .getResourceAsStream(fileName);
        // 加载配置信息
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        Properties properties =
                DBUtil.loadProperties("db.properties");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        try {
            Class.forName(properties.getProperty("driverClassName"));
            connection = DriverManager.getConnection(url,username,password);
            System.out.println("数据库连接成功");
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭连接
     * @return
     */
    public static void closeConnection() {
        if(connection != null){
            try {
                connection.close();
                System.out.println("数据库关闭成功");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
