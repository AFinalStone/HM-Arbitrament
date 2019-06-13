package com.hm.arbitrament.business.progress.view;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;

public class BackMoneyDetailAdapter extends BaseQuickAdapter<BackMoneyDetailAdapter.IBackMoneyItem, BaseViewHolder> {

    public interface IBackMoneyItem {

        String getTitle();

        String getMoney();

    }

    public BackMoneyDetailAdapter() {
        super(R.layout.arbitrament_item_money_back);
    }

    @Override
    protected void convert(BaseViewHolder helper, IBackMoneyItem item) {
        helper.setText(R.id.tv_progress_title, item.getTitle());
        helper.setText(R.id.tv_progress_amount, item.getMoney());
    }
}
