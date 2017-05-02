package com.amap.poisearchdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.amap.api.maps.MapView;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class BaseMapActivity extends Activity {

    protected MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout decorView = (FrameLayout)getWindow().getDecorView();

        mapView = new MapView(this);
        mapView.onCreate(savedInstanceState);
        decorView.addView(mapView, 0, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

}
