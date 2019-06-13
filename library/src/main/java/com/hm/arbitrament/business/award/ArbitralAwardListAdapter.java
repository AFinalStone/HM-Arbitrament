package com.hm.arbitrament.business.award;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;

public class ArbitralAwardListAdapter extends BaseQuickAdapter<ArbitralAwardListAdapter.IArbitralAwardListItem, BaseViewHolder> {

    public interface IArbitralAwardListItem {

        String getTime();

        String getStatus();

        String getName();

        String getMobile();

        String getAddress();

    }

    public ArbitralAwardListAdapter() {
        super(R.layout.arbitrament_item_arbitral_award);
    }

    @Override
    protected void convert(BaseViewHolder helper, IArbitralAwardListItem item) {
        helper.setText(R.id.tv_award_time, item.getTime());
        helper.setText(R.id.tv_award_status, item.getStatus());
        helper.setText(R.id.tv_award_name, item.getName());
        helper.setText(R.id.tv_award_mobile, item.getMobile());
        helper.setText(R.id.tv_award_address, item.getAddress());
    }

}
