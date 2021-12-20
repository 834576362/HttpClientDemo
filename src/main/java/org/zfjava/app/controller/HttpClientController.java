package org.zfjava.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zfjava.app.dto.User;
import org.zfjava.app.dto.dtojson.UserEnhance;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @auther zhangfen
 * @date 2021/12/16 15:18
 */
@RestController
public class HttpClientController {

    @GetMapping("/doGetControllerOne")
    public String doGetControllerOne(){
        System.out.println("--- doGetControllerOne() ---");
        return "123,你好啊";
    }
    @GetMapping("/doGetTestTwo")
    public User doGetTestTwo(User user){
        System.out.println("--- doGetTestTwo() ---");
        System.out.println("姓名-"+user.getName()+"，年龄"+user.getAge());
        return user;
    }
    @GetMapping("/doGetTestWayOne")
    public User doGetTestWayOne(User user){
        System.out.println("--- doGetTestWayOne() ---");
        return user;
    }

    @PostMapping("/doPostTestOne")
    public String doPostTestOne(){
        return "你好呀";
    }

    @PostMapping("/doPostTestFour")
    public User doPostTestFour(User user){
        return user;
    }

    //接收的时JSON字符串所以需要使用@RequestBody 来进行修复型参
    @PostMapping("/doPostTestTwo")
    public User doPostTestTwo(@RequestBody User user){
        System.out.println(user.toString());
        return user;
    }

    //接收的时JSON字符串所以需要使用@RequestBody 来进行修复型参
    @PostMapping("/doPostTestThree")
    public UserEnhance doPostTestThree(@RequestParam("color")String c, String sex, @RequestBody User user){
        System.out.println(user.toString());
        System.out.println("性别"+sex);
        System.out.println("颜色"+c);
        return new  UserEnhance(sex,c,user);
    }


    /**
     * httpclient传文件测试
     *
     * 注: 即multipart/form-data测试。
     * 注:多文件的话，可以使用数组MultipartFile[]或集合List<MultipartFile>来接收
     * 注:单文件的话，可以直接使用MultipartFile来接收
     */
    @PostMapping(value = "/file")
    public String fileControllerTest(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("files") List<MultipartFile> multipartFiles)
            throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(64);
        // 防止中文乱码
        sb.append("\n");
        sb.append("name=").append(name).append("\tage=").append(age);

        String fileName =null;
        for (MultipartFile file : multipartFiles) {
            sb.append("\n文件信息:\n");
            fileName = file.getOriginalFilename();

            if (fileName == null) {
                continue;
            }

            // 防止中文乱码
            // 在传文件时，将文件名URLEncode，然后在这里获取文件名时，URLDecode。就能避免乱码问题。
            fileName = URLDecoder.decode(fileName, "utf-8");
            sb.append("\t文件名: ").append(fileName);
            sb.append("\n\t文件大小: ").append(file.getSize() * 1.0 / 1024).append("KB");
            sb.append("\n\tContentType: ").append(file.getContentType());
            sb.append("\n");
            try {
                //接受传送的文件到指定位置
                File file1 = new File("E:\\"+fileName);
                file.transferTo(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  sb.toString();
    }

    /**
     * HttpClient流测试
     */
    @PostMapping(value="/sendStream")
    public String sendStream(@RequestParam("name") String name, InputStream is) {
        System.out.println("----------------------sendStream()---------------------");
        System.out.println(name);
        StringBuilder sb = new StringBuilder(64);
        try {
            sb.append("姓名：").append(URLDecoder.decode(name, "UTF-8"));
            sb.append("\n输入流内容：");
            BufferedReader bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(is, StandardCharsets.UTF_8));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
