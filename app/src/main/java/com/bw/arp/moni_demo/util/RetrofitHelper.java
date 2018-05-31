package com.bw.arp.moni_demo.util;

import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ARP on 2018/5/29.
 */

public class RetrofitHelper {
    public Context mCntext;

    //初始化
    OkHttpClient interceptor = new OkHttpClient();

    //gson工厂
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    //单例模式
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);
        }
        return instance;
    }
    private RetrofitHelper(Context mContext){
        mCntext = mContext;

        init();
    }


    private void init() {
        resetApp();
    }


    private void resetApp() {

//拦截器

        HttpLoggingInterceptor interceptor1 = new HttpLoggingInterceptor();
        interceptor1.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //初始化retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://120.27.23.105/")
                .client(interceptor)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }
}
