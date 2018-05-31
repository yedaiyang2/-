package com.bw.arp.moni_demo.util;

import com.bw.arp.moni_demo.bean.DeleteBean;
import com.bw.arp.moni_demo.bean.ShoppingBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ARP on 2018/5/29.
 */

public interface RetrofitService {
    //查询购物车数据
    @GET("product/getCarts")
    Observable<ShoppingBean> shoppingcar(@Query("uid") String uid,
                                         @Query("source") String source
                                         );
    //删除购物车
    @GET("product/deleteCart")
    Observable<DeleteBean> delete(@Query("uid") String uid,
                                  @Query("pid") String pid
                                  );
}
