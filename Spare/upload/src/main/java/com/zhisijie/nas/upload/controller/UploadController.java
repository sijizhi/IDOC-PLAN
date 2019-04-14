package com.zhisijie.nas.upload.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;
import java.util.UUID;

/**
 * @Author Sijie.Zhi
 * @title: UploadController
 * @projectName upload
 * @description: TODO
 * @date 2019/4/12 14:38
 */
@RestController
public class UploadController {
    /**
     * 通过mapping注入值
     */
    @Value("${web.file-mnt}")
    private  String  mnt ;

    @Value("${web.file-temporary}")
    private  String  temporary ;
    /**
     * 
     * @Author Sijie.zhi
     * @Description //TODO 上传文件
     * @Date 17:48 2019/4/13
     * @Param [file]
     * @return java.lang.String
     **/
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            //获取原始名字
            String fileName=file.getOriginalFilename();
            System.out.println("文件名为："+fileName);
            int i=fileName.indexOf(".");
            String suffixName="";
            if(i!=-1){
                suffixName=fileName.substring(i);
            }
            System.out.println("文件后缀名是："+suffixName);
            Calendar now = Calendar.getInstance();
            String  uploadpath=temporary+"/"+now.get(Calendar.YEAR)
                    + "/"+(now.get(Calendar.MONTH) + 1)
                    +"/"+now.get(Calendar.DAY_OF_MONTH)
                    +"/"+now.get(Calendar.HOUR_OF_DAY)
                    +"/"+now.get(Calendar.MINUTE)+"/"
                    +"db_name/";
            File filepath=new File(uploadpath);
            filepath.mkdirs();
            System.out.println("目录是："+uploadpath);
            fileName= UUID.randomUUID()+suffixName;
            System.out.println("修改后的名字："+fileName);
            File upload=new File(uploadpath+fileName);
            try {
                file.transferTo(upload);
                return  uploadpath+fileName;
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "bad";
    }


    /**
     * 
     * @Author Sijie.zhi
     * @Description //TODO 复制文件
     * @Date 23:36 2019/4/13
     * @Param [filePath]
     * @return java.lang.String
     **/
    @RequestMapping("/copyFile")
    public String copyFile(String filePath) throws IOException{
        Calendar now = Calendar.getInstance();
        File source=new File(filePath);
        String[] arr= filePath.split("/");
        String filepath1=arr[arr.length-1];
        System.out.println("filepath1>>"+filepath1);
        String path="/"+mnt+"/db_name/"+now.get(Calendar.YEAR)+"/"+(now.get(Calendar.MONTH) + 1)+"/"+now.get(Calendar.DAY_OF_MONTH)+"/";
        File f=new File(path);
        f.mkdirs();
        path+=filepath1;
        f=new File(path);
        f.createNewFile();
        File dest=new File(path);
        InputStream input =  new FileInputStream(source);
        OutputStream output = new FileOutputStream(dest);
        try {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
        return path;
    }


    /**
     * 
     * @Author Sijie.zhi
     * @Description //TODO 删除文件夹(两个小时前的文件夹-可设为定时)
     * @Date 15:19 2019/4/14
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/deleteisDirectory")
    public String deleteisDirectory(){
        Calendar now = Calendar.getInstance();
        String  targetPath=temporary+"/"+now.get(Calendar.YEAR)
                + "/"+(now.get(Calendar.MONTH) + 1)
                +"/"+now.get(Calendar.DAY_OF_MONTH)
                +"/"+(now.get(Calendar.HOUR_OF_DAY)-2);
        System.out.println(targetPath);
        UploadController.delFolder(targetPath);
        return "ok";
    }

    /**
     *
     * @Author Sijie.zhi
     * @Description //TODO 删除文件夹 param folderPath 文件夹完整绝对路径
     * @Date 15:55 2019/4/14
     * @Param [folderPath]
     * @return void
     **/
    public static void delFolder(String folderPath) {
        try {
            //删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            //删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @Author Sijie.zhi
     * @Description //TODO 删除指定文件夹下所有文件 param path 文件夹完整绝对路径
     * @Date 15:55 2019/4/14
     * @Param [path]
     * @return boolean
     **/
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

}
