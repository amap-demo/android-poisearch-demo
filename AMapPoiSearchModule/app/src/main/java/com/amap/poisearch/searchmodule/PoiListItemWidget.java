 package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class PoiListItemWidget extends RelativeLayout {
    public PoiListItemWidget(Context context) {
        super(context);
        init();
    }

    public PoiListItemWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoiListItemWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_poi_list_item, this);
    }
}
