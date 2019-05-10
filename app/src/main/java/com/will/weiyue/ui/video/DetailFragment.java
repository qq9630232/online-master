package com.will.weiyue.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.will.weiyue.R;
import com.will.weiyue.bean.Item;
import com.will.weiyue.component.ApplicationComponent;
import com.will.weiyue.component.DaggerHttpComponent;
import com.will.weiyue.ui.adapter.VideoDetailAdapter;
import com.will.weiyue.ui.base.BaseFragment;
import com.will.weiyue.ui.video.contract.VideoContract;
import com.will.weiyue.ui.video.presenter.VideoPresenter;
import com.will.weiyue.utils.AppUtil;
import com.will.weiyue.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
public class DetailFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    public static final String TYPEID = "typeId";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    private List<Item> itemList;
    private VideoDetailAdapter detailAdapter;
    private int pageNum = 1;
    private String typeId;
    private int page = 1;
    private int mode = 2;
    private boolean isRefresh;
    private boolean hasMore=true;
    private String deviceId;
    private int lastVisibleItem;
    public static DetailFragment newInstance() {
        Bundle args = new Bundle();
//        args.putCharSequence(TYPEID, typeId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNum = 1;
                if (mPresenter != null) {
                    isRefresh=true;
//                    mPresenter.getVideoDetails(pageNum, "list", typeId);
                    mPresenter.getVideoChannel(pageNum, mode, "0", deviceId, "0");

                }
            }
        });
        itemList =  new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new VideoDetailAdapter(getActivity(), R.layout.item_detail_video, itemList));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                pageNum++;
//                mPresenter.getVideoDetails(pageNum, "list", typeId);
                mPresenter.getVideoChannel(pageNum, mode, "0", deviceId, "0");


            }
        }, mRecyclerView);

    }

    @Override
    public void initData() {
//        if (getArguments() == null) return;
//        typeId = getArguments().getString(TYPEID);
        if (mPresenter != null) {
            deviceId = AppUtil.getDeviceId(getContext());
            mPresenter.getVideoChannel(page, mode, "0", deviceId, "0");
        }
    }

    @Override
    public void onRetry() {
        initData();
    }

//    @Override
//    public void loadVideoDetails(List<VideoDetailBean> detailBean) {
//        if (detailBean == null) {
//            showFaild();
//            return;
//        }
//        pageNum++;
//        detailAdapter.setNewData(detailBean.get(0).getItem());
//        mPtrFrameLayout.refreshComplete();
//        showSuccess();
//    }
//
//    @Override
//    public void loadMoreVideoDetails(List<VideoDetailBean> detailBean) {
//        if (detailBean == null) {
//            detailAdapter.loadMoreEnd();
//            return;
//        }
//        pageNum++;
//        detailAdapter.addData(detailBean.get(0).getItem());
//        detailAdapter.loadMoreComplete();
//    }
//
//    @Override
//    public void loadVideoChannel(List<VideoChannelBean> channelBean) {
//
//    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void updateListUI(List<Item> itemList) {
        if(itemList!=null){
            if(isRefresh){
//                detailAdapter.set(itemList);
                mPtrFrameLayout.refreshComplete();
                detailAdapter.setNewData(itemList);
            }else {

                detailAdapter.addData(itemList);
                detailAdapter.loadMoreComplete();
                detailAdapter.loadMoreEnd();
            }
            showSuccess();
        }else {
            showFaild();
            return;
        }

//        if (itemList == null) {
//
//        }

    }
}
