package net.suntrans.tenement.api;

import net.suntrans.tenement.bean.Ameter;
import net.suntrans.tenement.bean.ChannelAreaEntity;
import net.suntrans.tenement.bean.ChannelControlMessage;
import net.suntrans.tenement.bean.ChannelEntity;
import net.suntrans.tenement.bean.CompanyEntity;
import net.suntrans.tenement.bean.DeviceEntity;
import net.suntrans.tenement.bean.ElePayInfo;
import net.suntrans.tenement.bean.EnergyHis;
import net.suntrans.tenement.bean.EnergyListInfo;
import net.suntrans.tenement.bean.EnergyListItem;
import net.suntrans.tenement.bean.EnvInfo;
import net.suntrans.tenement.bean.LoginInfo;
import net.suntrans.tenement.bean.Monitor;
import net.suntrans.tenement.bean.MonitorEntity;
import net.suntrans.tenement.bean.NoticeEntity;
import net.suntrans.tenement.bean.PayOrder;
import net.suntrans.tenement.bean.ProfileWraper;
import net.suntrans.tenement.bean.ResultBody;
import net.suntrans.tenement.bean.RoomChannel;
import net.suntrans.tenement.bean.SceneEntity;
import net.suntrans.tenement.bean.SceneImage;
import net.suntrans.tenement.bean.SceneItemlEntity;
import net.suntrans.tenement.bean.SensusEntity;
import net.suntrans.tenement.bean.Stuff;
import net.suntrans.tenement.bean.StuffEntity;
import net.suntrans.tenement.bean.Updater;
import net.suntrans.tenement.bean.UploadInfo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Completable;
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


    /**
     * @param id 房间号
     * @return
     */
    @FormUrlEncoded
    @POST("energy/show")
    Observable<ResultBody<EnergyHis>> getEnergyDetail(@Field("id") String id);

    @POST("user/info")
    Observable<ResultBody<ProfileWraper>> loadUserInfo();

    @FormUrlEncoded
    @POST("sensus/show")
    Observable<ResultBody<SensusEntity.SixDetailData>> loadSensusInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("energy/current")
    Observable<ResultBody<Ameter>> loadAmeter(@Field("sno") String sno);

    @POST("monitor/index")
    Observable<ResultBody<List<Monitor>>> loadMonitorIndex();


    @FormUrlEncoded
    @POST("monitor/channel")
    Observable<ResultBody<MonitorEntity>> loadMonitorRoomChannel(@Field("id") String id);

    @POST("energy/area")
    Observable<ResultBody<List<EnergyListInfo>>> loadEnergyArea();

    @POST("manager/owner/index")
    Observable<ResultBody<CompanyEntity>> loadCompany();

    @POST("energy/summary")
    Observable<ResultBody<Map<String, String>>> loadEnergySummary();

    @FormUrlEncoded
    @POST("switch/slc/area")
    Observable<ResultBody<Map<String, String>>> switchSlcArea(@FieldMap Map<String, String> map);

    @POST("notice/index")
    Observable<ResultBody<NoticeEntity>> loadNoticeList();

    @FormUrlEncoded
    @POST("notice/store")
    Observable<ResultBody> storeNotice(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user/password")
    Observable<ResultBody> modifyPassword(@FieldMap Map<String, String> map);

    @Multipart
    @POST("notice/upload")
    Observable<ResultBody<Map<String, String>>> uploadNoticeFile(
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("app/check")
    Observable<Updater> checkUpdate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user/feedback")
    Observable<ResultBody> postSuggestion(@FieldMap Map<String, String> map);

    /**
     * 物业缴费房间号电费详情
     *
     * @param area_id
     * @param created_at
     * @return
     */
    @FormUrlEncoded
    @POST("pay/getOrder")
    Observable<ResultBody<ElePayInfo>> getOrder(@Field("area_id") String area_id, @Field("created_at") String created_at);

    /**
     * 获取租户的所有房间
     *
     * @return
     */
    @POST("pay/rooms")
    Observable<ResultBody<List<Map<String, String>>>> getPayRoom();

    @FormUrlEncoded
    @POST("pay/weixinPay")
    Observable<ResultBody<PayOrder>> getWXOrder(@Field("pay_sn") String pay_sn,
                                               @Field("total_money") String total_money,
                                               @Field("mobiletype") String mobiletype,
                                               @Field("type") String type);
}
