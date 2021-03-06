package com.will.weiyue.ui.video.contract;

import com.will.weiyue.bean.Item;
import com.will.weiyue.ui.base.BaseContract;

import java.util.List;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
public interface VideoContract {

    interface View extends BaseContract.BaseView {

//        void loadVideoChannel(List<VideoChannelBean> channelBean);
//
//        void loadVideoDetails(List<VideoDetailBean> detailBean);
//
//        void loadMoreVideoDetails(List<VideoDetailBean> detailBean);
        void showNoData();
        void showNoMore();
        void updateListUI(List<Item> itemList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        /**
         * 获取视频频道列表
         */
        void getVideoChannel(int page, int model, String pageId, String deviceId, String createTime);

        /**
         * 获取视频列表
         *
         * @param page     页码
         * @param listType 默认list
         * @param typeId   频道id
         */
        void getVideoDetails(int page, String listType, String typeId);

    }
}
