package com.jiang.medical.platform.enums;

/**
 * @description: 性别枚举类
 * @author: zhantuo.jiang
 * @create: 2019-11-20 19:56
 */
public enum GenderEnum {

    Male("男性"),
    Woman("女性"),
    Both("两者都");

    private String name;
    private GenderEnum(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

}
