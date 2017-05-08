# android-poisearch-demo
AMap 检索的逻辑模块。出行类app在进行目的地检索时可以直接利用此组件，以达到实现功能并缩短开发时间的目的。
其中AMapPoiSearchModule是功能组件，AMapPoiSearchDemo是demo

## 前述 ##
- [高德官网申请Key](http://lbs.amap.com/dev/#/).
- 阅读[参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html).
- 工程基于高德地图Android地图SDK实现

### 配置搭建AndroidSDK工程 ###
- [Android Studio工程搭建方法](http://lbs.amap.com/api/android-sdk/guide/creat-project/android-studio-creat-project/#add-jars).
- [通过maven库引入SDK方法](http://lbsbbs.amap.com/forum.php?mod=viewthread&tid=18786).

###使用场景###
AMapPoiSearchModule 提供了目的地检索的组件。

###组件截图###

![Screenshot](https://github.com/amap-demo/android-poisearch-demo/blob/master/search_ui_host.png)
![Screenshot](https://github.com/amap-demo/android-poisearch-demo/blob/master/search_ui_choose_city.png)


###使用方法###
此处以MainActivity为例进行介绍：

step1. 在onCreate中初始化Widget和ModuleDelegte<br />
```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);

        mSearchModuelDeletage = new SearchModuleDelegate();
        mSearchModuelDeletage.bindParentDelegate(mSearchModuleParentDelegate);
        contentView.addView(mSearchModuelDeletage.getWidget(this));
    }
```

step2. 在SearchModuleDelegate.IParentDelegate进行回调逻辑<br />
```java
private SearchModuleDelegate.IParentDelegate mSearchModuleParentDelegate = new IParentDelegate() {
        @Override
        public void onChangeCityName() {
            showToast("选择城市");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CityChooseActivity.class);
            intent.putExtra(CityChooseActivity.CURR_CITY_KEY, mSearchModuelDeletage.getCurrCity().getCity());
            MainActivity.this.startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE);
        }
......
```
