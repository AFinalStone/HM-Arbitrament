package com.hm.arbitrament.business.award;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;

import java.util.List;

public class ArbitralAwardListAdapter extends BaseQuickAdapter<ArbitralAwardListAdapter.IArbitralAwardListItem, BaseViewHolder> {

    public interface IArbitralAwardListItem {

        String getApplyId();

        String getTime();

        int getStatus();

        String getStatusStr();

        int getStatusColor();

        String getName();

        String getMobile();

        String getAddress();

        String getDesc();

    }

    public ArbitralAwardListAdapter() {
        super(R.layout.arbitrament_item_arbitral_award);
    }

    public void removeData(String applyPaperId) {
        List<IArbitralAwardListItem> list = getData();
        if (list != null) {
            int pos = -1;
            for (int i = 0; i < list.size(); i++) {
                if (applyPaperId.equals(list.get(i).getApplyId())) {
                    pos = i;
                    break;
                }
            }
            if (pos != -1) {
                remove(pos);
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, IArbitralAwardListItem item) {
        helper.setText(R.id.tv_award_time, item.getTime());
        helper.setText(R.id.tv_award_status, item.getStatusStr());
        helper.setTextColor(R.id.tv_award_status, item.getStatusColor());
        helper.setText(R.id.tv_award_name, item.getName());
        helper.setText(R.id.tv_award_mobile, item.getMobile());
        helper.setText(R.id.tv_award_address, item.getAddress());

        if (item.getStatus() == 1) {             //申请成功
            helper.setGone(R.id.ll_award_desc, true);
            helper.setGone(R.id.ll_award_pay, false);
            helper.setText(R.id.tv_award_desc, item.getDesc());
        } else if (item.getStatus() == 2) {     //已发货
            helper.setGone(R.id.ll_award_desc, true);
            helper.setGone(R.id.ll_award_pay, false);
            helper.setText(R.id.tv_award_desc, item.getDesc());
        } else {
            helper.setGone(R.id.ll_award_desc, false);
            helper.setGone(R.id.ll_award_pay, true);
            helper.addOnClickListener(R.id.tv_award_cancel);
            helper.addOnClickListener(R.id.tv_award_pay);
        }
    }

}
