package myUtil;

/**
 * @Description:封装一些String常用的操作
 * @Author:Anan
 * @Date:2019/8/10
 */
public class StringUtil {
    /**
     * 字符串是否为null或""
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str == null || "".equals(str)){
            return true;
        }
        return false;
    }
}
