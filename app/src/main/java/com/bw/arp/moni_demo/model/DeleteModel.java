package com.bw.arp.moni_demo.model;

import android.content.Context;
import android.util.Log;

import com.bw.arp.moni_demo.bean.DeleteBean;
import com.bw.arp.moni_demo.presenter.IDeletePresenter;
import com.bw.arp.moni_demo.util.DataManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ARP on 2018/5/30.
 */

public class DeleteModel {
    private Context context;
    public void getdeletedata(String uid, String pid, final IDeletePresenter iDeletePresenter){
        DataManager manager = new DataManager(context);
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(
                manager.getdelete(uid, pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onCompleted","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteBean deleteBean) {
                        Log.e("onNext","onNext");
                        iDeletePresenter.Show(deleteBean);
                    }
                })
        );
    }
}
