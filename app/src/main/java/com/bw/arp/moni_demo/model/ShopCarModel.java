package com.bw.arp.moni_demo.model;

import android.content.Context;
import android.util.Log;

import com.bw.arp.moni_demo.bean.ShoppingBean;
import com.bw.arp.moni_demo.presenter.IShopCarPresenter;
import com.bw.arp.moni_demo.util.DataManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ARP on 2018/5/29.
 */

public class ShopCarModel {
    private Context context;
    public void getshopcardata(String uid, String source, final IShopCarPresenter iShopCarPresenter){
        DataManager manager = new DataManager(context);
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(
                manager.getshoppingcar(uid,source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShoppingBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onCompleted","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(ShoppingBean shoppingBean) {
                        Log.e("onNext","onNext");
                        iShopCarPresenter.Show(shoppingBean);
                    }
                })
        );
    }
}
