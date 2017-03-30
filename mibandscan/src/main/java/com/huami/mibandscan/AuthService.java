package com.huami.mibandscan;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.huami.mibandscan.FormBean.BindingsBean;
import com.huami.mibandscan.FormBean.opEventsBean;
import com.huami.mibandscan.FormBean.opEventsResponseBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 其实在本分之中应当去掉Auth相关代码，吐出所有的扫描到手环的数据
 */
// TODO: 西暦17/03/29  移除网络认证部分代码
class AuthService {

    private MiBandScanConfig miBandScanConfig;
    private MiBandScanCallBack miBandScanCallBack;
    private boolean onService = false;
    private int heartBeatsErrorCount = 0;

    public AuthService(MiBandScanConfig miBandScanConfig, MiBandScanCallBack miBandScanCallBack) {
        this.miBandScanConfig = miBandScanConfig;
        this.miBandScanCallBack = miBandScanCallBack;
    }

    // 用于创建认证服务
    private static <S> S createService(Class<S> serviceClass, String username, String password) {
        // TODO: 16-9-26 正式上线时请将url改为 https://api-beacon.huami-inc.com
        String API_BASE_URL = "https://api-beacon.huami-inc.com";

        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic = "Basic " +
                    Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        //httpClient.addInterceptor(interceptor);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    // 认证服务登录接口
    private interface LoginService {
        @POST("/hbox/{hbox_id}/heartbeats")
        Call<ResponseBody> basicLogin(@Body Map<String, String> body, @Path("hbox_id") String hbox_id);
    }

    // 发送数据到认证服务器
    public void auth() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("timestamp", Long.toString(System.currentTimeMillis() / 1000));
        LoginService loginService = createService(LoginService.class,
                miBandScanConfig.getUserName(),
                miBandScanConfig.getPassword());
        Call<ResponseBody> call = loginService.basicLogin(requestBody, miBandScanConfig.getHboxId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                //Log.d("auth", response.message());
                if (response.isSuccessful()) {
                    // sdk继续为用户提供服务
                    onService = true;
                    heartBeatsErrorCount = 0;
                } else {
                    if (response.code() == 401) {
                        // sdk停止为用户提供服务
                        onService = false;
                        miBandScanCallBack.onStatus(MiBandScanStatus.SERVICE_NOT_WORK);
                    } else {
                        // 如果用户连续24次发送失败心跳包，即超时24小时，则sdk停止服务
                        if(heartBeatsErrorCount < 24) {
                            heartBeatsErrorCount++;
                            miBandScanCallBack.onStatus(MiBandScanStatus.SERVICE_NOT_WORK);
                        } else {
                            onService = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 如果用户连续24次发送失败心跳包，则sdk停止服务
                if(heartBeatsErrorCount < 24) {
                    heartBeatsErrorCount++;
                    miBandScanCallBack.onStatus(MiBandScanStatus.NETWORK_ERROR);
                } else {
                    onService = false;
                }

                if (!onService) {
                    miBandScanCallBack.onStatus(MiBandScanStatus.SERVICE_NOT_WORK);
                    onService = false;
                }
            }
        });
    }


    // 认证服务登录接口
    private interface CheckService {
        @GET("/thirdparties/-/bindings")
        Call<List<BindingsBean>> checkOpenId(@Query("openId") List<String> openIds);
    }

    // 发送数据到认证服务器
    /*public void checkMac() {
        final opEventsBean requestBody = new opEventsBean();
        requestBody.setType("swipe");
        requestBody.setEventId(UUID.randomUUID().toString());

        List<opEventsBean.DataBean.AdvertiserBean> advertiserBeanList = new ArrayList<>();
        for (String bandMac : miBandScanConfig.getTempAccessOpenId()) {
            opEventsBean.DataBean.AdvertiserBean advertiserBean = new opEventsBean.DataBean.AdvertiserBean();
            advertiserBean.setMac(bandMac);
            advertiserBeanList.add(advertiserBean);
        }

        opEventsBean.DataBean dataBean = new opEventsBean.DataBean();
        dataBean.setScanner(miBandScanConfig.getHboxId());
        dataBean.setAdvertiser(advertiserBeanList);
        List<opEventsBean.DataBean> dataBeanList = new ArrayList<>();
        dataBeanList.add(dataBean);
        requestBody.setData(dataBeanList);
        CheckService checkMacService = createService(CheckService.class,
                miBandScanConfig.getUserName(),
                miBandScanConfig.getPassword());
        Call<opEventsResponseBean> call = checkMacService.checkMac(requestBody, miBandScanConfig.getHboxId());
        call.enqueue(new Callback<opEventsResponseBean>() {
            @Override
            public void onResponse(Call<opEventsResponseBean> call, retrofit2.Response<opEventsResponseBean> response) {
                if (response.isSuccessful()) {
                    List<String> accessBandList =  new ArrayList<>();
                    for (opEventsResponseBean.DevicesBean device : response.body().getDevices()) {
                        Log.d("Mac", device.getMac());
                        accessBandList.add(device.getMac());
                    }
                    miBandScanConfig.setAccessBands(accessBandList);
                    miBandScanCallBack.onStatus(MiBandScanStatus.CHECK_OPENID_COMPLETE);
                } else {
                    if (response.code() == 401) {
                        // sdk停止为用户提供服务
                        onService = false;
                        miBandScanCallBack.onStatus(MiBandScanStatus.SERVICE_NOT_WORK);
                    } else {
                        miBandScanCallBack.onStatus(MiBandScanStatus.NETWORK_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<opEventsResponseBean> call, Throwable t) {
                Log.d("response", "Failure");
                miBandScanCallBack.onStatus(MiBandScanStatus.NETWORK_ERROR);
            }
        });
    }*/

    // 认证openId并换取mac地址
    public void checkOpenId() {
        final opEventsBean requestBody = new opEventsBean();
        requestBody.setType("swipe");
        requestBody.setEventId(UUID.randomUUID().toString());

        CheckService checkOpenIdService = createService(CheckService.class,
                miBandScanConfig.getUserName(),
                miBandScanConfig.getPassword());
        List<String> openIds = new ArrayList<>();
        for (String openId : miBandScanConfig.getTempAccessOpenId()) {
            openIds.add(openId);
        }
        Call<List<BindingsBean>> call = checkOpenIdService.checkOpenId(openIds);
        call.enqueue(new Callback<List<BindingsBean>>() {
            @Override
            public void onResponse(Call<List<BindingsBean>> call, retrofit2.Response<List<BindingsBean>> response) {
                if (response.isSuccessful()) {
                    List<HashMap<Integer, String>> accessList =  new ArrayList<>();
                    for (BindingsBean device : response.body()) {
                        Log.d("CheckOpenId", "Mac:" + device.getMacAddress() + " OpenId:" + device.getOpenId());
                        HashMap<Integer, String> item = new HashMap<>();
                        item.put(MiBandScanConfig.ACCESS_MAC, device.getMacAddress());
                        item.put(MiBandScanConfig.ACCESS_OPENID, device.getOpenId());
                        accessList.add(item);
                    }

                    HashMap<Integer, String> item = new HashMap<>();
                    item.put(MiBandScanConfig.ACCESS_MAC, "880f10eaf71b");
                    item.put(MiBandScanConfig.ACCESS_OPENID, "999");
                    accessList.add(item);
                    miBandScanConfig.setAccessList(accessList);
                    miBandScanCallBack.onStatus(MiBandScanStatus.CHECK_OPENID_COMPLETE);
                } else {
                    if (response.code() == 401) {
                        // sdk停止为用户提供服务
                        onService = false;
                        miBandScanCallBack.onStatus(MiBandScanStatus.SERVICE_NOT_WORK);
                    } else {
                        miBandScanCallBack.onStatus(MiBandScanStatus.NETWORK_ERROR);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BindingsBean>> call, Throwable t) {
                Log.d("response", "Failure");
                miBandScanCallBack.onStatus(MiBandScanStatus.NETWORK_ERROR);
            }
        });
    }


    /**
     * 在此版本中我们移除了网络验证，所以永远返回true
     * @return
     */
    public boolean isOnService() {
        return true;
    }

}
