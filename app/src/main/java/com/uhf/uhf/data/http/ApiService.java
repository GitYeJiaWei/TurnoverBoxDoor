package com.uhf.uhf.data.http;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.bean.FindBean;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.bean.LoginBean;
import com.uhf.uhf.bean.ReturnBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableError;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {
    //String BASE_URL = "http://192.168.66.3:8112/";
    //String BASE_URL = "http://mall.ioter-e.com:8112/";
    String BASE_URL = "http://39.100.19.127:8081/";

    //token为方法名，基类中不能加入方法名
    @FormUrlEncoded
    @POST("token")//登录
    Observable<LoginBean> login(@FieldMap Map<String ,String> params);

    //更改密码
    @FormUrlEncoded
    @POST("/api/User/ChangePassword")
    Observable<BaseBean<String>> setting(@FieldMap Map<String,String> params);

    //获取租赁客户信息
    @GET("/api/Customer/GetCustomer")
    Observable<BaseBean<LeaseBean>> leaseid(@QueryMap Map<String,String> params);

    //获取押金规则
    @GET("/api/User/GetLoginData")
    Observable<BaseBean<FeeRule>> rulelist(@QueryMap Map<String,String> params);

    //生成租赁记录
    @FormUrlEncoded
    @POST("/api/Rent/CreateRentRecord")
    Observable<BaseBean<String>> createRent (@FieldMap Map<String,String> params);

    //生成报废记录
    @FormUrlEncoded
    @POST("/api/Damage/CreateDamageRecord")
    Observable<BaseBean<String>> createDamage (@FieldMap Map<String,String> params);

    //生成归还记录
    @FormUrlEncoded
    @POST("/api/Return/CreateReturnRecord")
    Observable<BaseBean<String>> createReturn(@FieldMap Map<String,String> params);

    //根据EPC列表获取费用小计
    @FormUrlEncoded
    @POST("/api/Return/GetReturnFeeSubtotal")
    Observable<BaseBean<List<ReturnBean>>> getReturn(@FieldMap Map<String,String> params);

    //扫码查询
    @GET("/api/ProductEpc/QueryEpcInfo")
    Observable<BaseBean<FindBean>> find(@QueryMap Map<String,String> params);
}
