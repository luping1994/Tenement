package net.suntrans.tenement.api;

import net.suntrans.tenement.bean.ChannelAreaEntity;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.DeviceEntity;
import net.suntrans.tenement.bean.EnergyHis;
import net.suntrans.tenement.bean.EnergyListItem;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.LoginInfo;
import net.suntrans.tenement.bean.ProfileWraper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneImage;
import net.suntrans.tenement.bean.SceneItem;
import net.suntrans.tenement.bean.SceneItemlEntity;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.bean.StuffEntity;
import net.suntrans.tenement.bean.UploadInfo;
import net.suntrans.tenement.persistence.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("company/area")
    Observable<ResultBody<ChannelAreaEntity>> getHomeChannel();

    @FormUrlEncoded
    @POST("company/channel")
    Observable<ResultBody<ChannelAreaEntity>> getChannelById(@Field("id") String id);


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
    @POST("scene/channel/store")
    Observable<ResultBody> addSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/channel/delete")
    Observable<ResultBody> deleteSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/channel/update")
    Observable<ResultBody> updateSceneChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("scene/delete")
    Observable<ResultBody> deleteScene(@Field("ids") String id);


    @POST("scene/index")
    Observable<ResultBody<SceneEntity>> getScene();

    @FormUrlEncoded
    @POST("scene/channel")
    Observable<ResultBody<SceneItemlEntity>> getSceneChannel(@Field("id") String id);

    @POST("device/index")
    Observable<ResultBody<DeviceEntity>> getMyDevices();

    @POST("scene/image")
    Observable<ResultBody<SceneImage>> getSceneDefaultImg();

    @FormUrlEncoded
    @POST("device/channel")
    Observable<ResultBody<ChannelEntity>> getDeviceChannel(@Field("id") String id);

    @POST("scene/device")
    Observable<ResultBody<ChannelEntity>> getScebeChooseChannel();

    @FormUrlEncoded
    @POST("device/channel/update")
    Observable<ResultBody> updateChannel(@FieldMap Map<String, String> map);

    /**
     * 添加员工
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("company/user/store")
    Observable<ResultBody> addStuff(@FieldMap Map<String, String> map);

    @POST("company/user")
    Observable<ResultBody<StuffEntity>> getMyStuff();


    @FormUrlEncoded
    @POST("company/user/edit")
    Observable<ResultBody<Stuff>> getStuffProfile(@Field("id") String id);


    @FormUrlEncoded
    @POST("user/profile")
    Observable<ResultBody> updateProfile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("company/user/delete")
    Observable<ResultBody> deleteStuff(@Field("id") String id);

    @FormUrlEncoded
    @POST("company/user/update")
    Observable<ResultBody> updateStuffProfile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user/feedback")
    Observable<ResultBody> feedBack(@FieldMap Map<String, String> map);


    @Multipart
    @POST("user/cover/upload")
    Observable<ResultBody<UploadInfo>> upload(
            @Part MultipartBody.Part image);

    @POST("energy/index")
    Observable<ResultBody<List<EnergyListItem>>> energyList();


    @FormUrlEncoded
    @POST("energy/show")
    Observable<ResultBody<EnergyHis>> getEnergyDetail(@Field("id") String id);

    @POST("user/info")
    Observable<ResultBody<ProfileWraper>> loadUserInfo();

}
