package org.zfjava.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zfjava.app.dto.User;

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

    @PostMapping("/doPostTestTwo")
    public User doPostTestTwo(User user){
        System.out.println(user.toString());
        return user;
    }

}
