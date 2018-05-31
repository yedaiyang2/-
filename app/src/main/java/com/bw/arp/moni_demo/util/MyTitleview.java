package com.bw.arp.moni_demo.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.bw.arp.moni_demo.R;

/**
 * Created by ARP on 2018/5/30.
 */

public class MyTitleview extends RelativeLayout {

    private EditText edit_sou;
    private Button main_bt_sou;

    public MyTitleview(Context context) {
        this(context, null);
    }

    public MyTitleview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTitleview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        View inflate = View.inflate(context, R.layout.sousuo_item, this);
        edit_sou = inflate.findViewById(R.id.edit_sou);
        main_bt_sou = inflate.findViewById(R.id.main_bt_sou);

        main_bt_sou.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords = edit_sou.getText().toString().trim();
//                Toast.makeText(context,keywords+"",Toast.LENGTH_SHORT).show();

                listened.toString(keywords);
                edit_sou.setText("");
            }
        });

    }
    Listened listened;
    public void setListened(Listened listened){

        this.listened=listened;
    }
    public interface Listened{
        void toString(String editText);
    }
}
