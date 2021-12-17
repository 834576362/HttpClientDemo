package org.zfjava.app.dto.dtojson;

import lombok.Data;
import org.zfjava.app.dto.User;

/**
 * @auther zhangfen
 * @date 2021/12/17 15:47
 */
@Data
public class UserEnhance {
    private String sex;
    private String color;
    private User user;

    public UserEnhance(){}

    public UserEnhance(String sex, String color, User user) {
        this.sex = sex;
        this.color = color;
        this.user = user;
    }
}
