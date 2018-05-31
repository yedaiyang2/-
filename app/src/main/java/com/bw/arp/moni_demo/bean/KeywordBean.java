package com.bw.arp.moni_demo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ARP on 2018/5/30.
 */
@Entity
public class KeywordBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    @Generated(hash = 138641661)
    public KeywordBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1737222118)
    public KeywordBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
