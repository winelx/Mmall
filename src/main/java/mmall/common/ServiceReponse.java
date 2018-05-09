package mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
//注解是为了解决在不返回字段问题
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)

public class ServiceReponse<T> implements Serializable {
    private int stauts;
    private String msg;
    private T data;
    //单个构造参数
    private ServiceReponse(int stauts){
        this.stauts=stauts;
    }

    //两个构造参数
      private ServiceReponse(int stauts,String msg){
        this.stauts=stauts;
        this.msg=msg;
    }

    private ServiceReponse(int stauts,T data){
        this.stauts = stauts;
        this.data=data;
    }
    //三个构造参数
    private ServiceReponse(int stauts,String msg,T data){
        this.stauts=stauts;
        this.msg=msg;
        this.data=data;
    }


    //注解作用:使之不在json序列化结果当中
    @JsonIgnore
    //获取状态吗
    public boolean isSuccess(){
        return this.stauts==ResponseCode.SUCCESS.getCode();
    }
    //获取状态吗
    public int getStauts(){
        return stauts;
    }
    //返回内容
    public String  getMsg(){
        return msg;
    }
    //数据
    public T getData(){
        return data;
    }
    //请求方法只返回状态吗
    public static <T> ServiceReponse<T> createBySuccess(){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode());
    }
    //请求后返回状态码和说明
    public static <T> ServiceReponse<T> createBySuccess(String msg){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    //请求只返回数据
    public static <T> ServiceReponse<T> createBySuccess(T Data){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode(),Data);
    }
    //请求返回状态码，说明，内容
    public static <T> ServiceReponse<T> createBySuccess(String msg,T Data){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode(),msg,Data);
    }

    //请求失败
    public static <T> ServiceReponse<T> createByError(int code, String s){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.ERROR.getDesc());
    }
    //请求失败
    public static <T> ServiceReponse<T> createByError(){
        return new ServiceReponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.ERROR.getDesc());
    }
    //返回错误原因
    public static <T> ServiceReponse<T> createByErrorMessage(String errorMessage){
            return new ServiceReponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    //参数错误
    public static <T> ServiceReponse<T> createByMessage(int errorcode,String errormessage){
        return new ServiceReponse<T>(errorcode,errormessage);
    }

}
