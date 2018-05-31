package com.bw.arp.moni_demo.greendemo;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.bw.arp.moni_demo.bean.KeywordBean;

import com.bw.arp.moni_demo.greendemo.KeywordBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig keywordBeanDaoConfig;

    private final KeywordBeanDao keywordBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        keywordBeanDaoConfig = daoConfigMap.get(KeywordBeanDao.class).clone();
        keywordBeanDaoConfig.initIdentityScope(type);

        keywordBeanDao = new KeywordBeanDao(keywordBeanDaoConfig, this);

        registerDao(KeywordBean.class, keywordBeanDao);
    }
    
    public void clear() {
        keywordBeanDaoConfig.clearIdentityScope();
    }

    public KeywordBeanDao getKeywordBeanDao() {
        return keywordBeanDao;
    }

}
