package com.will.weiyue.net;

import android.support.annotation.StringDef;

import com.will.weiyue.bean.Item;
import com.will.weiyue.bean.Result;
import com.will.weiyue.utils.TimeUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Observable;


/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
public class VideoApi {

    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    @StringDef({ACTION_DEFAULT,ACTION_DOWN,ACTION_UP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Actions{

    }

    public static VideoApi sInstance;

    private VideoApiService mService;

    public VideoApi(VideoApiService newsApiService) {
        this.mService = newsApiService;
    }

    public static VideoApi getInstance(VideoApiService newsApiService) {
        if (sInstance == null)
            sInstance = new VideoApi(newsApiService);
        return sInstance;
    }


    /**
     * 视频列表
     * @param page
     * @param model
     * @param pageId
     * @param deviceId
     * @param createTime
     * @return
     */

    public Observable<Result.Data<List<Item>>> getVideoDetail(int page, int model, String pageId, String deviceId, String createTime){
        return mService.getList("api","getList",page,model,pageId,createTime,"android","1.3.0", TimeUtil.getCurrentSeconds(), deviceId,1);
    }

}
