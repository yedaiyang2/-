package com.bw.arp.moni_demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.arp.moni_demo.bean.KeywordBean;
import com.bw.arp.moni_demo.greendemo.DaoMaster;
import com.bw.arp.moni_demo.greendemo.DaoSession;
import com.bw.arp.moni_demo.greendemo.KeywordBeanDao;
import com.bw.arp.moni_demo.util.MyTitleview;
import com.fynn.fluidlayout.FluidLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_mytitle)
    MyTitleview mMainMytitle;
    @BindView(R.id.fluidLayout)
    FluidLayout mFluidLayout;
    @BindView(R.id.btn_all)
    TextView mBtnAll;
    private KeywordBeanDao keywordBeanDao;
    private List<KeywordBean> stringlist = new ArrayList<>();
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //实例化GreenDao
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "pwk.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        keywordBeanDao = daoSession.getKeywordBeanDao();

        mMainMytitle.setListened(new MyTitleview.Listened() {
            @Override
            public void toString(String editText) {
//                Toast.makeText(MainActivity.this,editText+"",Toast.LENGTH_SHORT).show();

                if (!TextUtils.isEmpty(editText)) {
                    keywordBeanDao.insertOrReplaceInTx(new KeywordBean(null, editText));
                    genTag();
                    startActivity(new Intent(MainActivity.this,ShopCarActivity.class));
//                    mBtnAll.setVisibility(View.VISIBLE);
                }else
                    Toast.makeText(MainActivity.this,"输入内容为空",Toast.LENGTH_SHORT).show();
            }
        });

        mBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示！");
                builder.setMessage("确定要删除历史记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        keywordBeanDao.deleteAll();

                        genTag();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        genTag();
    }

    private void genTag() {
        List<KeywordBean> beans = keywordBeanDao.loadAll();

        stringlist.clear();
        stringlist.addAll(beans);

        mFluidLayout.removeAllViews();

        for (int x = 0; x < beans.size(); x++) {
            TextView tv = new TextView(MainActivity.this);
            tv.setText(stringlist.get(x).getName());
            tv.setTextSize(13);

            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(12, 12, 12, 12);

            mFluidLayout.addView(tv, params);
        }
    }
    //用户退出程序 1
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("注意")
//                        .setMessage("确定要退出程序吗？")
//                        .setNegativeButton("取消",null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        })
//                        .show();
//        }
//
//        return false;
//    }

    //用户退出程序 2
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
