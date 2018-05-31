package com.bw.arp.moni_demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.arp.moni_demo.adapter.MyElvAdapter;
import com.bw.arp.moni_demo.bean.DeleteBean;
import com.bw.arp.moni_demo.bean.ShoppingBean;
import com.bw.arp.moni_demo.presenter.DeletePresenter;
import com.bw.arp.moni_demo.presenter.ShopCarPresenter;
import com.bw.arp.moni_demo.view.IDeleteView;
import com.bw.arp.moni_demo.view.IShopCarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public class ShopCarActivity extends AppCompatActivity implements IShopCarView, MyElvAdapter.CheckGroupItemListener, MyElvAdapter.ModifyGoodsItemNumberListener, IDeleteView {
    @BindView(R.id.shop_car_elv)
    ExpandableListView mShopCarElv;
    @BindView(R.id.tv_redact)
    TextView mTvRedact;
    @BindView(R.id.ck_All)
    CheckBox mCkAll;
    @BindView(R.id.sumprice)
    TextView mSumprice;
    @BindView(R.id.account)
    TextView mAccount;
    @BindView(R.id.swip)
    SwipeRefreshLayout mSwip;
    //默认是false
    private boolean flag;
    //购买商品的总价
    private double totalPrice = 0.00; //15.55  15  0.55亿
    //购买商品的总数量
    private int totalNum = 0;
    private ShopCarPresenter shopCarPresenter;
    private MyElvAdapter adapter;
    private List<ShoppingBean.DataBean> list = new ArrayList<>();
    private DeletePresenter deletePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        //实例化购物车的P层
        shopCarPresenter = new ShopCarPresenter(this);
        shopCarPresenter.getShopCarDatas("10234", "android");
        adapter = new MyElvAdapter(this);
        mShopCarElv.setAdapter(adapter);
        //去除默认显示器
        mShopCarElv.setGroupIndicator(null);
        //加减
        adapter.setModifyGoodsItemNumberListener(this);
        //全选
        adapter.setCheckGroupItemListener(this);
        mCkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChoosedAll(((CheckBox) view).isChecked());
                //总价
                statisticsPrice();
            }
        });
        mTvRedact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {//编辑 -> 完成\
                    flag = true;
                    mTvRedact.setText("完成");
                    adapter.showDeleteButton(flag);
                } else {
                    flag = false;
                    mTvRedact.setText("编辑");
                    adapter.showDeleteButton(flag);
                }
            }
        });
        //实例化删除P层
        deletePresenter = new DeletePresenter(this);

        //结算按钮
        mAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShopCarActivity.this,"结算成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSuccess(ShoppingBean shoppingBean) {
        list.addAll(shoppingBean.getData());
        adapter.setList(list);
        defaultExpand();
        adapter.setDeleteItemLinister(new MyElvAdapter.DeleteItemLinister() {
            @Override
            public void doDelete(String pid, View view, int i, int i1) {
                deletePresenter.getDeleteData("10234", pid);
                adapter.notifyDataSetChanged();
            }
        });
        //下拉更新购物车
        mSwip.setColorSchemeColors(Color.GRAY);
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwip.setRefreshing(false);
                list.clear();
                shopCarPresenter.getShopCarDatas("10234", "android");
                Toast.makeText(ShopCarActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onSuccess(DeleteBean deleteBean) {
        Toast.makeText(this, deleteBean.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(Exception e) {

    }

    //二级列表默认展开
    private void defaultExpand() {

        for (int i = 0; i < adapter.getGroupCount(); ++i) {
            mShopCarElv.expandGroup(i);
        }

    }

    @Override
    public void checkGroupItem(int groupPosition, boolean isChecked) {
        //商家javabean
        ShoppingBean.DataBean dataBean = list.get(groupPosition);
        dataBean.setGroupChoosed(isChecked);
        //遍历商家里面的商品，将其置为选中状态
        for (ShoppingBean.DataBean.ListBean listBean : dataBean.getList()) {
            listBean.setChildChoosed(isChecked);
        }

        //底部结算那个checkbox状态(全选)
        if (isCheckAll()) {
            mCkAll.setChecked(true);
        } else {
            mCkAll.setChecked(false);
        }

        //刷新适配器
        adapter.notifyDataSetChanged();

        //计算价格
        statisticsPrice();
    }

    @Override
    public void checkChildItem(int groupPosition, int childPosition, boolean isChecked) {
        ShoppingBean.DataBean dataBean = list.get(groupPosition);//商家那一层
        List<ShoppingBean.DataBean.ListBean> listBeans = dataBean.getList();
        ShoppingBean.DataBean.ListBean listBean = listBeans.get(childPosition);

        //你点击商家的商品条目将其选中状态记录
        listBean.setChildChoosed(isChecked);

        //检测商家里面的每一个商品是否被选中，如果被选中，返回boolean
        boolean result = isGoodsCheckAll(groupPosition);
        if (result) {
            dataBean.setGroupChoosed(result);
        } else {
            dataBean.setGroupChoosed(result);
        }

        //底部结算那个checkbox状态(全选)
        if (isCheckAll()) {
            mCkAll.setChecked(true);
        } else {
            mCkAll.setChecked(false);
        }


        //刷新适配器
        adapter.notifyDataSetChanged();

        //计算总价
        statisticsPrice();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View view) {
        ShoppingBean.DataBean.ListBean listBean = list.get(groupPosition).getList().get(childPosition);
        //取出当前的商品数量
        int currentNum = listBean.getNum();
        //商品++
        currentNum++;
        //将商品数量设置javabean上
        listBean.setNum(currentNum);

        //刷新适配器
        adapter.notifyDataSetChanged();


        //计算商品价格
        statisticsPrice();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View view) {
        ShoppingBean.DataBean.ListBean listBean = list.get(groupPosition).getList().get(childPosition);
        //取出当前的商品数量
        int currentNum = listBean.getNum();
        //直接结束这个方法
        if (currentNum == 1) {
            Toast.makeText(this, "商品最小数量为1", Toast.LENGTH_SHORT).show();
            return;
        }

        currentNum--;
        listBean.setNum(currentNum);
        //刷新适配器
        adapter.notifyDataSetChanged();

        //计算商品价格
        statisticsPrice();
    }


//逻辑实行


    /**
     * 检测某个商家的商品是否都选中，如果都选中的话，商家CheckBox应该是选中状态
     *
     * @param groupPosition
     * @return
     */
    private boolean isGoodsCheckAll(int groupPosition) {
        List<ShoppingBean.DataBean.ListBean> listBeans = this.list.get(groupPosition).getList();
        //遍历某一个商家的每个商品是否被选中
        for (ShoppingBean.DataBean.ListBean listBean : listBeans) {
            if (listBean.isChildChoosed()) {//是选中状态
                continue;
            } else {
                return false;
            }

        }

        return true;
    }

    //购物车商品是否全部选中
    private boolean isCheckAll() {

        for (ShoppingBean.DataBean dataBean : list) {
            if (!dataBean.isGroupChoosed()) {
                return false;
            }
        }
        return true;
    }

    //全选与反选
    private void isChoosedAll(boolean isChecked) {

        for (ShoppingBean.DataBean dataBean : list) {
            dataBean.setGroupChoosed(isChecked);
            for (ShoppingBean.DataBean.ListBean listBean : dataBean.getList()) {
                listBean.setChildChoosed(isChecked);
            }
        }
        //刷新适配器
        adapter.notifyDataSetChanged();

    }


    /**
     * 计算你所选中的商品总价
     */
    private void statisticsPrice() {
        //初始化值
        totalNum = 0;

        totalPrice = 0.00;

        for (ShoppingBean.DataBean dataBean : list) {

            for (ShoppingBean.DataBean.ListBean listBean : dataBean.getList()) {
                if (listBean.isChildChoosed()) {
                    totalNum++;
                    System.out.println("number : " + totalNum);
                    totalPrice += listBean.getNum() * listBean.getPrice();
                }
            }
        }
        //设置文本信息 合计、结算的商品数量
        mSumprice.setText("合计:￥" + totalPrice);
        mAccount.setText("结算(" + totalNum + ")");
    }
    //内存优化
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shopCarPresenter!=null){
            shopCarPresenter.detachView();
            shopCarPresenter=null;
        }
        //解绑
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.unsubscribe();
    }
}
