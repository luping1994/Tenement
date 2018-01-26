package net.suntrans.tenement.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.tenement.App;
import net.suntrans.tenement.converter.MyGsonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Looney on 2016/12/15.
 */

public class RetrofitHelper {
    public static String BASE_URL = "http://gzfhq.suntrans-cloud.com/api/v1/";
    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mOkHttpClient2;

    static {
        initOkHttpClient();
    }

    public static Api getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    public static Api getPgyApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.pgyer.com/apiv2/")
//                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }


    private static void initOkHttpClient() {
        Interceptor netInterceptor =
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String header = App.Companion.getMySharedPreferences().getString("token", "-1");
                        net.suntrans.common.utils.LogUtil.INSTANCE.i(header);
                        Request original = chain.request();

                        RequestBody newBody = original.body();

//                        if (original.body() instanceof FormBody) {
//                            newBody = addParamsToFormBody((FormBody) original.body());
//                        } else {
//                            newBody =    addParamsToFormBody();
//                        }
                        Request newRequest = original.newBuilder()
                                .header("Authorization", "Bearer " + header)
                                .method(original.method(), newBody)
                                .build();

                        Response response = chain.proceed(newRequest);
                        return response;
                    }
                };


        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
                            .addNetworkInterceptor(new StethoInterceptor())
                            .connectTimeout(8, TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        if (mOkHttpClient2 == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient2 == null) {
                    mOkHttpClient2 = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
                            .cookieJar(new JavaNetCookieJar(cookieManager))
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /**
     * 为FormBody类型请求体添加参数
     *
     * @param body
     * @return
     */
    private static FormBody addParamsToFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        String header = App.Companion.getSharedPreferences().getString("token", "");
        builder.add("token", header);
        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }
        return builder.build();
    }

    /**
     * 为FormBody类型请求体添加参数
     *
     * @return
     */
    private static FormBody addParamsToFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        String header = App.Companion.getSharedPreferences().getString("token", "");
        LogUtil.i("token", header);
        builder.add("token", header);
        return builder.build();
    }


}
