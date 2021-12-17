package org.zfjava.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.zfjava.app.dto.User;
import org.zfjava.app.dto.dtojson.UserEnhance;

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
}
