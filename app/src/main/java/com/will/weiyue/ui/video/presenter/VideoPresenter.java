package com.will.weiyue.ui.video.presenter;

import com.will.weiyue.bean.Item;
import com.will.weiyue.bean.Result;
import com.will.weiyue.net.VideoApi;
import com.will.weiyue.ui.base.BasePresenter;
import com.will.weiyue.ui.video.contract.VideoContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {
    private VideoApi mVideoApi;

    @Inject
    VideoPresenter(VideoApi mVideoApi) {
        this.mVideoApi = mVideoApi;
    }

    @Override
    public void getVideoChannel(int page, int model, String pageId, String deviceId, String createTime) {

        mVideoApi.getVideoDetail(page,model,pageId,deviceId,createTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result.Data<List<Item>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result.Data<List<Item>> listData) {
                        int size = listData.getDatas().size();
                        if(size>0){
                            mView.updateListUI(listData.getDatas());
                        }else {
                            mView.showNoMore();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getVideoDetails(final int page, String listType, String typeId) {
//        mNewsApi.getVideoDetail(page, listType, typeId)
//                .compose(RxSchedulers.<List<VideoDetailBean>>applySchedulers())
//                .compose(mView.<List<VideoDetailBean>>bindToLife())
//                .subscribe(new Observer<List<VideoDetailBean>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull List<VideoDetailBean> videoDetailBean) {
//                        if (page > 1) {
//                            mView.loadMoreVideoDetails(videoDetailBean);
//                        } else {
//                            mView.loadVideoDetails(videoDetailBean);
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        // Log.i(TAG, "onError: "+e.getMessage().toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }
}
