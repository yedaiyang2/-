package com.bw.arp.moni_demo.presenter;

import com.bw.arp.moni_demo.bean.ShoppingBean;

/**
 * Created by ARP on 2018/5/29.
 */

public interface IShopCarPresenter {
    void Show(ShoppingBean shoppingBean);
    void detachView();
}
