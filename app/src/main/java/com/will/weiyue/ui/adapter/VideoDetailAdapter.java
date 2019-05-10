package com.will.weiyue.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.will.weiyue.R;
import com.will.weiyue.bean.Item;
import com.will.weiyue.utils.ImageLoaderUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/10 .
 */
public class VideoDetailAdapter extends BaseQuickAdapter<Item, BaseViewHolder> {
    Context mContext;

    public VideoDetailAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<Item> itemList) {
        super(layoutResId, itemList);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, Item itemBean) {
        // viewHolder.setText(R.id.tv_title, itemBean.getTitle());
        JzvdStd jcVideoPlayerStandard = viewHolder.getView(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(itemBean.getVideo()
                , itemBean.getTitle(),Jzvd.SCREEN_NORMAL);

//        JCVideoPlayer.setJcUserAction(new JCUserAction() {
//            @Override
//            public void onEvent(int type, String s, int i1, Object... objects) {
//                switch (type) {
//                    case JCUserAction.ON_CLICK_START_ICON:
//                        viewHolder.getView(R.id.tv_videoduration).setVisibility(View.GONE);
//                        break;
//                }
//            }
//        });
//        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
//                , "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);
//        jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
        ImageLoaderUtil.LoadImage(mContext, itemBean.getThumbnail(), jcVideoPlayerStandard.thumbImageView);

//        viewHolder.setText(R.id.tv_videoduration, conversionTime(itemBean.getDuration()));
//        if (!TextUtils.isEmpty(itemBean.getPlayTime())) {
//            viewHolder.setText(R.id.tv_playtime, conversionPlayTime(Integer.valueOf(itemBean.getPlayTime())));
//        }
    }

    private String conversionTime(int duration) {
        int minutes = duration / 60;
        int seconds = duration - minutes * 60;
        String m = sizeOf(minutes) > 1 ? String.valueOf(minutes) : "0" + minutes;
        String s = sizeOf(seconds) > 1 ? String.valueOf(seconds) : "0" + seconds;
        return m + ":" + s;
    }

    private String conversionPlayTime(int playtime) {
        if (sizeOf(playtime) > 4) {
            return accuracy(playtime, 10000, 1) + "万";
        } else {
            return String.valueOf(playtime);
        }
    }

    public static String accuracy(double num, double total, int digit) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(digit);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total;
        return df.format(accuracy_num);
    }

    /**
     * 判断是几位数字
     *
     * @param size
     * @return
     */
    private int sizeOf(int size) {
        final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
                99999999, 999999999, Integer.MAX_VALUE};
        for (int i = 0; ; i++)
            if (size <= sizeTable[i])
                return i + 1;
    }
}
