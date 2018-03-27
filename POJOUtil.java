package com.upsoft.sep.eps.eeces.tools;

import java.lang.reflect.Field;

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

        public Obj set(String name,Object param){
            //获取所有的字段
            Field[] declaredFields = target.getClass().getDeclaredFields();
            for(Field field:declaredFields){
                field.setAccessible(true);
                if(field.getName().equals(name)){
                    //判断数据类型是否一致
                    if(field.getType() == param.getClass()){
                        try {
                            field.set(this.target,param);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return this;
        }

        public <T> T over(){
            return (T) this.target;
        }
    }

    public static void main(String[] args) {
        A over = POJOUtil.build(A.class).set("leap", false).set("a", "hello mmotiy!").over();
        System.out.println(over);
    }
}

class A{
    boolean leap;
    String a;

    @Override
    public String toString() {
        return "A{" +
                "leap=" + leap +
                ", a='" + a + '\'' +
                '}';
    }
}

