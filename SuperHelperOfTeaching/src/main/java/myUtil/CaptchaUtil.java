package myUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Description:验证码生成器
 * @Author:Anan
 * @Date:2019/8/9
 */
public class CaptchaUtil {
    /**
     * 验证码来源
     */
    final private char[] code = {
            '2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z','A','B','C','D','E',
            'F', 'G','H','I','J','K','L','M','N','O','P',
            'Q','R','S','T','U','V','W','X','Y','Z'
    };

    /**
     * 字体
     */
    final private String[] fontNames = {"黑体","宋体","Courier", "Arial",
            "Verdana", "Times", "Tahoma", "Georgia"};
    /**
     * 字体样式
     */
    final private Integer[] fontStyles = {Font.BOLD, Font.ITALIC|Font.BOLD};
    /**
     * 验证码长度
     */
    private Integer vcodeLen = 4;
    /**
     * 验证码图片字体大小
     */
    private Integer fontSize = 21;
    /**
     * 验证码图片宽度
     */
    private Integer width = (fontSize+1)*vcodeLen+10;
    /**
     * 验证码图片高度
     */
    private Integer height = fontSize+12;
    /**
     * 干扰线条数：默认3条
     */
    private Integer disturbline = 3;

    public CaptchaUtil() {}
    public CaptchaUtil(Integer vcodeLen) {
        this.vcodeLen = vcodeLen;
        this.width = (this.fontSize+1)*vcodeLen+10;
    }
    /**
     * 生成验证码
     * @return 验证码
     */
    public String generatorVCode(){
        int len = code.length;
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < vcodeLen;i++){
            int index = ran.nextInt(len);
            sb.append(code[index]);
        }
        return sb.toString();
    }

    /**
     * 生成验证图片
     * @param vcode 要生成的验证码
     * @param drawline  是否要画干扰线
     * @return 验证图片
     */
    public BufferedImage generatorVCodeImage(String vcode, boolean drawline){
        //创建验证码图片
        BufferedImage vcodeImage = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = vcodeImage.getGraphics();
        //填充背景颜色
        graphics.setColor(new Color(246, 240, 250));
        graphics.fillRect(0,0,this.width,this.height);
        //画干扰线
        if(drawline){
            drawDisturbLine(graphics);
        }
        //随机数生成器
        Random random = new Random();
        //在图片上画验证码
        for(int i=0; i< vcode.length(); i++){
            //设置字体
            graphics.setFont(new Font(this.fontNames[random.nextInt(this.fontNames.length)],
                    this.fontStyles[random.nextInt(this.fontStyles.length)],this.fontSize));
            //生成随机颜色
            graphics.setColor(getRandomColor());
            //画验证码
            graphics.drawString(vcode.charAt(i)+"", i*this.fontSize+10, this.fontSize+5);
        }
        //释放图形上下文，释放图形使用的资源
        graphics.dispose();
        return vcodeImage;
    }

    /**
     * 获得旋转字体的验证码图片
     * @param vcode 要生成的验证码
     * @param drawline  是否要画干扰线
     * @return 验证图片
     */
    public BufferedImage generatorRotateVCodeImage(String vcode, boolean drawline){
        //创建验证码图片
        BufferedImage rotateVcodeImage = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotateVcodeImage.createGraphics();
        //填充背景颜色
        g2d.setColor(new Color(246, 240, 250));
        g2d.fillRect(0,0,this.width,this.height);
        //画干扰线
        if(drawline){
            drawDisturbLine(g2d);
        }
        //画验证码
        for(int i=0; i<vcode.length(); i++){
            //将字母/数字旋转
            BufferedImage rotateImage = getRotateImage(vcode.charAt(i));
            //在图片上绘制验证码
            g2d.drawImage(rotateImage, null, (int) (this.height * 0.7) * i, 0);
        }
        g2d.dispose();
        return rotateVcodeImage;
    }

    /**
     * 获取旋转字符后的图片
     * @param c  字符
     * @return  旋转字符后的图片
     */
    public BufferedImage getRotateImage(char c) {
        BufferedImage rotateImage = new BufferedImage(this.height,this.height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotateImage.createGraphics();
        //设置透明度为0
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, this.height, this.height);
        Random ran = new Random();
        g2d.setFont(new Font(this.fontNames[ran.nextInt(this.fontNames.length)],
                this.fontStyles[ran.nextInt(this.fontStyles.length)], this.fontSize));
        g2d.setColor(getRandomColor());
        double theta = getTheta();
        //旋转图片
        g2d.rotate(theta, height/2, height/2);
        g2d.drawString(Character.toString(c), (this.height-this.fontSize)/2, this.fontSize+5);
        g2d.dispose();
        return rotateImage;
    }

    /**
     * 旋转角度
     * @return
     */
    private double getTheta() {
        return ((int) (Math.random()*1000) % 2 == 0 ? -1 : 1)*Math.random();
    }


    /**
     * 获取随机颜色
     * @return  随机颜色
     */
    private Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(220), ran.nextInt(220), ran.nextInt(220));
    }

    /**
     * 画干扰线
     * @param graphics
     */
    private void drawDisturbLine(Graphics graphics) {
        Random random = new Random();
        for(int i=0; i<this.disturbline; i++){
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            graphics.setColor(getRandomColor());
            //在（x1,y1),(x2,y2)间画一条当前颜色的线
            graphics.drawLine(x1,y1,x2,y2);
        }
    }

    /**
     * 获取验证码的长度
     * @return
     */
    public Integer getVcodeLen() {
        return vcodeLen;
    }

    /**
     * 设置验证码的长度
     * @param vcodeLen
     */
    public void setVcodeLen(Integer vcodeLen) {
        this.width = (this.fontSize+3)*vcodeLen+10;
        this.vcodeLen = vcodeLen;
    }

    /**
     * 获取字体大小
     * @return
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * 设置字体大小
     * @param fontSize
     */
    public void setFontSize(Integer fontSize) {
        this.width = (this.fontSize+3)*vcodeLen+10;
        this.height = this.fontSize+15;
        this.fontSize = fontSize;
    }

    /**
     * 获取图片宽度
     * @return
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 设置图片宽度
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 获取图片高度
     * @return
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 设置图片高度
     * @param height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * 获取干扰线条数
     * @return
     */
    public Integer getDisturbline() {
        return disturbline;
    }

    /**
     * 设置干扰线条数
     * @param disturbline
     */
    public void setDisturbline(Integer disturbline) {
        this.disturbline = disturbline;
    }
}
