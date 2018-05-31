package com.bw.arp.moni_demo.presenter;

import com.bw.arp.moni_demo.bean.ShoppingBean;
import com.bw.arp.moni_demo.model.ShopCarModel;
import com.bw.arp.moni_demo.view.IShopCarView;

/**
 * Created by ARP on 2018/5/29.
 */

public class ShopCarPresenter implements IShopCarPresenter{
    private ShopCarModel shopCarModel;
    private IShopCarView iShopCarView;

    public ShopCarPresenter(IShopCarView iShopCarView) {
        this.iShopCarView = iShopCarView;
        this.shopCarModel = new ShopCarModel();
    }


    public void getShopCarDatas(String uid,String source){
        shopCarModel.getshopcardata(uid,source,this);
    }

    @Override
    public void Show(ShoppingBean shoppingBean) {
        iShopCarView.onSuccess(shoppingBean);
    }

    @Override
    public void detachView() {
        if (iShopCarView!=null){
            iShopCarView = null;
        }
    }
}
