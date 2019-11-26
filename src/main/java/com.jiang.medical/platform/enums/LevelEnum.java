package com.jiang.medical.platform.enums;

/**
 * @description: ${description}
 * @author: zhantuo.jiang
 * @create: 2019-11-25 12:04
 */
public enum LevelEnum {
    Admin("管理员"),
    User("用户");

    private String name;
    private LevelEnum(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
