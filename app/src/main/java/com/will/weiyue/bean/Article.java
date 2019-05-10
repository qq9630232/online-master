package com.will.weiyue.bean;

import cn.bmob.v3.BmobArticle;

/**
 * Created by SDC on 2019/5/9.
 */

public class Article extends BmobArticle {
    private String objId;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    private NewsArticleBean articleBean;

    public NewsArticleBean getArticleBean() {
        return articleBean;
    }

    public void setArticleBean(NewsArticleBean articleBean) {
        this.articleBean = articleBean;
    }
}
