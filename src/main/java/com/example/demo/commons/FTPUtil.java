package com.example.demo.commons;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FTPUtil {

    private static String ip;

    private static String port;

    private static String filepath;

    private static String username;

    private static String password;

    private static Logger LOGGER = LoggerFactory.getLogger(FTPUtil.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmSSS");//yyyyMMddHHmmSSS

    private static FTPClient ftpClient = new FTPClient();

    /**
     * 上传文件
     *
     * @param tabName
     * @param inputStream
     * @param map
     * @return
     */
    public static  String uploadFile(String tabName, InputStream inputStream, Map<String, String> map){
        ip = map.get("ip");
        port = map.get("port");
        filepath = map.get("filepath");
        username = map.get("username");
        password = map.get("password");
        /*ip = "172.16.235.60";
        port = "21";
        filepath = "/";
        username = "apache";
        password = "password";*/
        //定义返回地址
        String newName = null;
        try {
            // 连接FTP服务器
            ftpClient.connect(ip, Integer.parseInt(port));
            //设置编码格式
            ftpClient.setControlEncoding("GBK");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            // 登录
            ftpClient.login(username, password);
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                LOGGER.info("连接服务器失败！");
                return null;
            }
            if (!ftpClient.changeWorkingDirectory(filepath)) {//判断是否有模块目录  /upload/images
                //创建模块文件夹
                String[] mkds = filepath.split("/");
                for (int i = 0; i < mkds.length; i++) {
                    if (StringUtils.isNotBlank(mkds[i]))
                        //添加目录
                        ftpClient.makeDirectory(mkds[i]);
                    //添加完目录后 必须进入目录 才能在创建目录
                    ftpClient.changeWorkingDirectory(mkds[i]);
                }
            }
            //切换到文件保存目录
            ftpClient.changeWorkingDirectory(filepath);
            //更改文件保存名称
            newName = SDF.format(new Date())+ ".xlsx";
            String newName2 = new String(newName.getBytes("UTF-8"),"iso-8859-1");

            //保存文件
            ftpClient.storeFile(newName2,inputStream);
            //拼接文件地址
            //imageStorePath = "http://180.167.162.154:8888"+"/"+filepath+"/"+newName;
            //关闭资源
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            LOGGER.info("上传文件失败！");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return newName;
    }

    /**
     * 测试
     */
   /* public static void main(String[] args) throws IOException {
        HashMap<String, String> map2 = new HashMap<>();
        ip = "172.16.235.60";
        port = "21";
        filepath = "/excelFiles";
        username = "ctftp";
        password = "123456";
        map2.put("ip",ip);
        map2.put("port",port);
        map2.put("filepath",filepath);
        map2.put("basepath","");
        map2.put("username",username);
        map2.put("password",password);

        File file = new File("D:\\wazk.xlsx");
        InputStream inputStream = new FileInputStream(file);
        String s = FTPUtil.uploadFile(inputStream,map2);
        System.out.println(s);
    }*/
}

