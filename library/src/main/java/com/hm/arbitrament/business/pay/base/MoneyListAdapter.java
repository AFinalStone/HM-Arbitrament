package com.hm.arbitrament.business.pay.base;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.arbitrament.R;

/**
 * Created by syl on 2019/6/10.
 */

public class MoneyListAdapter extends BaseQuickAdapter<IMoneyItem, BaseViewHolder> {

    public MoneyListAdapter() {
        super(R.layout.arbitrament_item_pay_money_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, IMoneyItem item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setGone(R.id.iv_warn, !TextUtils.isEmpty(item.getWarnDialogContent()));
    }
}
