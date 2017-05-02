 package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */
public class FavAddressItemWidget extends RelativeLayout {

    private ImageView mIconIV;
    private TextView titleTV;
    private TextView subTitleTV;

    private String subTitle = "";
    private int mType = 0;

    public static final String HOME_DEF_SUB_TITLE = "设置家的地址";
    public static final String COMP_DEF_SUB_TITLE = "设置公司地址";

    public FavAddressItemWidget(Context context) {
        super(context);
        init();
    }

    public FavAddressItemWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavAddressItemWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public boolean isAddressEmpty(){
        String oriStr = "";
        if (mType == FavAddressWidget.HOME_TYPE) {
            oriStr = HOME_DEF_SUB_TITLE;
        } else {
            oriStr = COMP_DEF_SUB_TITLE;
        }

        return oriStr.equals(this.subTitle.toString());
    }


    public void setType(int type) {
        mType = type;

        if (FavAddressWidget.HOME_TYPE == type) {
            mIconIV.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.home));
            titleTV.setText("家");

        } else {
            mIconIV.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.comp));
            titleTV.setText("公司");

        }

        refreshSubTitle();
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        refreshSubTitle();
    }

    public void refreshSubTitle() {
        if (FavAddressWidget.HOME_TYPE == mType) {
            if (TextUtils.isEmpty(subTitle)) {
                this.subTitle = HOME_DEF_SUB_TITLE;
            }
            subTitleTV.setText(subTitle);

        } else {
            if (TextUtils.isEmpty(subTitle)) {
                this.subTitle = COMP_DEF_SUB_TITLE;
            }
            subTitleTV.setText(subTitle);
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_fav_address_item, this);

        mIconIV = (ImageView)findViewById(R.id.icon);
        titleTV = (TextView)findViewById(R.id.title_tv);
        subTitleTV = (TextView)findViewById(R.id.value_tv);
    }
}
