package com.mmotiy.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmotiy on 2018/3/27.
 */
public class POJOUtil {
    public static Obj build(Class clas){

        return new Obj(clas);
    }

    static public class Obj{

        private Object target;

        public Obj(Class tar){
            try {
                this.target = tar.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public Obj set(String names,Object... objects){
            String[] split = names.split(",");
            for(int i=split.length;i-->0;set(split[i],objects[i])){}
            return this;
        }

        public Obj set(String name,Object param) {
            //获取所有的字段
            Field[] declaredFields = target.getClass().getDeclaredFields();
            try{
                for(Field field:declaredFields){
                    field.setAccessible(true);
                    if(field.getName().equals(name)){
                        //判断数据类型是否一致 判断参数类型是不是 目标类型的子类或者实现
                        if(isParentOrBrother(field.getType(),param.getClass())){
                            try {
                                field.set(this.target,field.getType().cast(param));
                                break;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return this;
        }

        public <T> T over(){
            return (T) this.target;
        }
    }

    /**
     *  判断param是否是tar的实现
     * @param tar
     * @param param
     * @return
     */
    private static boolean isParentOrBrother(Class tar,Class param){
        Class temp = param;
        while(temp!=null){
            if(temp.getName().equals(tar.getName())){
                return true;
            }
            temp = temp.getSuperclass();
        }
        Class[] interfaces = param.getInterfaces();
        for(Class itf:interfaces){
            if(itf.getName().equals(tar.getName())){
                return true;
            }
        }
        return false;
    }
}
