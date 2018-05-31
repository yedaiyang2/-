package com.bw.arp.moni_demo.util;

import android.content.Context;

import com.bw.arp.moni_demo.bean.DeleteBean;
import com.bw.arp.moni_demo.bean.ShoppingBean;

import rx.Observable;

/**
 * Created by ARP on 2018/5/29.
 */

public class DataManager {
    private RetrofitService mRetrofitService;

    //构造器里获取RetrofitService
    public DataManager(Context context) {
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    //查询购物车
    public Observable<ShoppingBean> getshoppingcar(String uid,String source){
        return mRetrofitService.shoppingcar(uid, source);
    }
    //删除购物车条目
    public Observable<DeleteBean> getdelete(String uid,String pid){
        return mRetrofitService.delete(uid,pid);
    }
}
