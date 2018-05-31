package com.bw.arp.moni_demo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bw.arp.moni_demo.R;
import com.bw.arp.moni_demo.bean.ShoppingBean;

import java.util.List;

/**
 * Created by ARP on 2018/5/29.
 */

public class MyElvAdapter extends BaseExpandableListAdapter {
    private List<ShoppingBean.DataBean> list;
    private Context context;
    private Button btn_commodity_delete;
    private String pid;

    public void setList(List<ShoppingBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public MyElvAdapter(Context context) {
        this.context = context;
    }
    //接收是否处于编辑状态的一个boolean值
    private boolean isEditor;

    private CheckGroupItemListener checkGroupItemListener;
    private ModifyGoodsItemNumberListener modifyGoodsItemNumberListener;
    //删除商品
    private DeleteItemLinister deleteItemLinister;
    //更新购物车
    private UpdateItemLinister updateItemLinister;

    public void setUpdateItemLinister(UpdateItemLinister updateItemLinister) {
        this.updateItemLinister = updateItemLinister;
    }

    public void setDeleteItemLinister(DeleteItemLinister deleteItemLinister) {
        this.deleteItemLinister = deleteItemLinister;
    }

    public void setCheckGroupItemListener(CheckGroupItemListener checkGroupItemListener) {
        this.checkGroupItemListener = checkGroupItemListener;
    }

    public void setModifyGoodsItemNumberListener(ModifyGoodsItemNumberListener modifyGoodsItemNumberListener) {
        this.modifyGoodsItemNumberListener = modifyGoodsItemNumberListener;
    }

    //是否显示删除按钮
    public void showDeleteButton(boolean isEditor){
        this.isEditor = isEditor;
        //刷新适配器
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return list != null && list.get(i).getList() != null ? list.get(i).getList().size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_group_item,viewGroup,false);
        }
        CheckBox ck_group_choose = view.findViewById(R.id.ck_group_choose);
        //设置商家的CheckBox
        if (list.get(i).isGroupChoosed()){
            ck_group_choose.setChecked(true);
        }else {
            ck_group_choose.setChecked(false);
        }

        //点击
        ck_group_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGroupItemListener.checkGroupItem(i,((CheckBox)view).isChecked());
            }
        });

        //最后赋值
        ck_group_choose.setText(list.get(i).getSellerName());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        pid = list.get(i).getList().get(i1).getPid();
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_child_item,viewGroup,false);
        }
        //商品选择
        CheckBox ck_child_choosed = convertView.findViewById(R.id.ck_child_choose);
        //商品图片
        ImageView iv_show_pic = convertView.findViewById(R.id.iv_show_pic);
        //商品主标题
        TextView tv_commodity_name = convertView.findViewById(R.id.tv_commodity_name);
        //商品副标题
        TextView tv_commodity_attr = convertView.findViewById(R.id.tv_commodity_attr);
        //商品价格
        TextView tv_commodity_price = convertView.findViewById(R.id.tv_commodity_price);
        //商品数量
        TextView tv_commodity_num = convertView.findViewById(R.id.tv_commodity_num);
        //商品减
        TextView iv_sub = convertView.findViewById(R.id.iv_sub);
        //商品加减中的数量变化
        final TextView tv_commodity_show_num = convertView.findViewById(R.id.tv_commodity_show_num);
        //商品加
        TextView iv_add = convertView.findViewById(R.id.iv_add);
        //删除按钮
        btn_commodity_delete = convertView.findViewById(R.id.btn_commodity_delete);

        //设置文本信息
        tv_commodity_name.setText(list.get(i).getList().get(i1).getTitle());
        tv_commodity_attr.setText(list.get(i).getList().get(i1).getSubhead());
        tv_commodity_price.setText("￥"+list.get(i).getList().get(i1).getPrice());
        tv_commodity_num.setText("x"+list.get(i).getList().get(i1).getNum());
        tv_commodity_show_num.setText(list.get(i).getList().get(i1).getNum()+"");
        //图片分割
        String images = list.get(i).getList().get(i1).getImages();
        String[] urls = images.split("\\|");
        //加载图片
        Glide.with(context)
                .load(urls[0])
                .crossFade()
                .into(iv_show_pic);
        //商品加
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyGoodsItemNumberListener.doIncrease(i,i1,tv_commodity_show_num);
            }
        });
        //商品减
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyGoodsItemNumberListener.doDecrease(i,i1,tv_commodity_show_num);
            }
        });

        //商品复选框是否被选中
        ck_child_choosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGroupItemListener.checkChildItem(i,i1,((CheckBox)view).isChecked());
            }
        });
        //处理商品的选中状态
        if (list.get(i).getList().get(i1).isChildChoosed()){
            ck_child_choosed.setChecked(true);
        }else {
            ck_child_choosed.setChecked(false);
        }
        //控制删除按钮的隐藏与显示
        if (isEditor){
            btn_commodity_delete.setVisibility(View.VISIBLE);
        }else {
            btn_commodity_delete.setVisibility(View.GONE);
        }
        btn_commodity_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("确定删除此商品吗？");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteItemLinister.doDelete(pid,view,i,i1);
                        notifyDataSetChanged();
//                        btn_commodity_delete.setVisibility(View.GONE);
                    }
                });
                //显示！！！
                builder.show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }




    //购物车逻辑实现

    /**
     * 商家的复选框以及商品的复选框是否被选中的接口
     */
    /**
     * 商家复选框以及商品复选框是否被选中接口
     */
    public interface CheckGroupItemListener{
        //商家的条目的复选框监听
        void checkGroupItem(int groupPosition,boolean isChecked);
        //商品的
        void checkChildItem(int groupPosition,int childPosition,boolean isChecked);


    }

    /**
     * 商品加减接口
     */
    public interface ModifyGoodsItemNumberListener{

        //商品添加操作
        void doIncrease(int groupPosition,int childPosition,View view);
        //商品减少操作
        void doDecrease(int groupPosition,int childPosition,View view);

    }

    /**
     * 商品删除接口
     */
    public interface DeleteItemLinister{
        //删除
        void doDelete(String pid,View view,int i,int i1);
    }
    /**
     * 商品删除接口
     */
    public interface UpdateItemLinister{
        //更新购物车
        void doUpdate(String sellerid,String pid,String selected,String num,View view,int i,int i1);
    }
}
