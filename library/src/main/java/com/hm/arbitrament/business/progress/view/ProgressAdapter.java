package com.hm.arbitrament.business.progress.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;
import com.hm.iou.tools.StringUtil;

public class ProgressAdapter extends BaseQuickAdapter<ProgressAdapter.IProgressItem, BaseViewHolder> {

    public interface IProgressItem {

        String getTitle();

        String getTime();

        String getSubTitle();

        String getLink();

        int getCheckedIcon();

        /**
         *
         * @return 1-表示第一条数据，2-表示最后一条数据，0及其他-表示在中间位置，
         */
        int getPositionFlag();
    }

    private int mSubTitleColor;

    public ProgressAdapter(Context context) {
        super(R.layout.arbitrament_item_progress);
        mSubTitleColor = context.getResources().getColor(R.color.uikit_function_remind);
    }

    @Override
    protected void convert(BaseViewHolder helper, IProgressItem item) {
        String title = StringUtil.getUnnullString(item.getTitle());
        if (TextUtils.isEmpty(item.getSubTitle())) {
            helper.setText(R.id.tv_progress_title, title);
        } else {
            String subTitle = item.getSubTitle();
            SpannableString spanStr = new SpannableString(String.format("%s（%s）", title, subTitle));
            int s = title.length() + 1;
            int e = s + subTitle.length();
            spanStr.setSpan(new ForegroundColorSpan(mSubTitleColor), s, e, 0);
            helper.setText(R.id.tv_progress_title, spanStr);
        }
        helper.setText(R.id.tv_progress_time, item.getTime());
        helper.setImageResource(R.id.iv_progress_checked, item.getCheckedIcon());

        helper.addOnClickListener(R.id.tv_progress_title);
        helper.setTag(R.id.tv_progress_title, item.getLink());

        int posFlag = item.getPositionFlag();
        helper.setVisible(R.id.view_progress_line_top, true);
        helper.setVisible(R.id.view_progress_line_bottom, true);

        if (mData != null && mData.size() == 1) {
            helper.setVisible(R.id.view_progress_line_top, false);
            helper.setVisible(R.id.view_progress_line_bottom, false);
        } else {
            if (posFlag == 1) {
                helper.setVisible(R.id.view_progress_line_top, false);
            } else if (posFlag == 2){
                helper.setVisible(R.id.view_progress_line_bottom, false);
            }
        }
    }

}
