package com.bw.arp.moni_demo.presenter;

import com.bw.arp.moni_demo.bean.DeleteBean;
import com.bw.arp.moni_demo.model.DeleteModel;
import com.bw.arp.moni_demo.view.IDeleteView;

/**
 * Created by ARP on 2018/5/30.
 */

public class DeletePresenter implements IDeletePresenter{
    private IDeleteView iDeleteView;
    private DeleteModel deleteModel;

    public DeletePresenter(IDeleteView iDeleteView) {
        this.iDeleteView = iDeleteView;
        this.deleteModel = new DeleteModel();
    }

    public void getDeleteData(String uid,String pid){
        deleteModel.getdeletedata(uid,pid,this);
    }

    @Override
    public void Show(DeleteBean deleteBean) {
        iDeleteView.onSuccess(deleteBean);
    }

    @Override
    public void deView() {
        if (iDeleteView!=null){
            iDeleteView = null;
        }
    }
}
