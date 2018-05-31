package com.bw.arp.moni_demo.view;

import com.bw.arp.moni_demo.bean.ShoppingBean;

/**
 * Created by ARP on 2018/5/29.
 */

public interface IShopCarView {
    //成功
    void onSuccess(ShoppingBean shoppingBean);
    //失败
    void onFailed(Exception e);
}
