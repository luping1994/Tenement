package net.suntrans.tenement.api;

import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.LoginInfo;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneItem;
import net.suntrans.tenement.bean.SceneItemlEntity;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2017/11/16.
 * Des:
 */

public interface Api {
    /**
     * 登录
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("login/token")
    Observable<ResultBody<LoginInfo>> login(@Field("username") String username,
                                            @Field("password") String password);

    @POST("sensus/home")
    Observable<ResultBody<EnvInfo>> getHomeEnv();

    @POST("company/channel")
    Observable<ResultBody<ChannelEntity>> getHomeChannel();


    @FormUrlEncoded
    @POST("switch/slc/channel")
    Observable<ResultBody<ChannelControlMessage>> switchChannel(@Field("id") String id, @Field("cmd") String cmd);

    @FormUrlEncoded
    @POST("switch/slc/scene")
    Observable<ResultBody> switchScene(@Field("id") String id);

    @FormUrlEncoded
    @POST("scene/update")
    Observable<ResultBody> updateSceneInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/store")
    Observable<ResultBody> createScene(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/delete")
    Observable<ResultBody> deleteScene(@Field("ids") String id);


    @POST("scene/index")
    Observable<ResultBody<SceneEntity>> getScene();

    @FormUrlEncoded
    @POST("scene/channel")
    Observable<ResultBody<SceneItemlEntity>> getSceneChannel(@Field("id") String id);
}
