package com.bw.arp.moni_demo.view;

import com.bw.arp.moni_demo.bean.DeleteBean;

/**
 * Created by ARP on 2018/5/30.
 */

public interface IDeleteView {
    //成功
    void onSuccess(DeleteBean deleteBean);
    //失败
    void onFailed(Exception e);
}
