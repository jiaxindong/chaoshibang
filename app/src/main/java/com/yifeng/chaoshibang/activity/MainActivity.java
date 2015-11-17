package com.yifeng.chaoshibang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.yifeng.chaoshibang.ChaoshibangApplication;
import com.yifeng.chaoshibang.R;
import com.yifeng.chaoshibang.fragment.ChaoshiFragment;
import com.yifeng.chaoshibang.fragment.DiscoveryFragment;
import com.yifeng.chaoshibang.fragment.OrderFragment;
import com.yifeng.chaoshibang.fragment.PersonalFragment;
import com.yifeng.chaoshibang.service.BaiduLocationService;
import com.yifeng.chaoshibang.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donggua on 2015/11/10.
 */
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private BaiduLocationService locationService;
    private TextView LocationResult;
    private Button startLocation;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabHost tabHost;
    private int[] titles = { R.string.tab_title_0, R.string.tab_title_1, R.string.tab_title_2, R.string.tab_title_3 };

    public static int TAB_INDEX_CHAOSHI = 0;
    public static int TAB_INDEX_ORDER = 1;
    public static int TAB_INDEX_DISCOVERY = 2;
    public static int TAB_INDEX_PERSONAL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LocationResult = (TextView) findViewById(R.id.textView1);
//        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
//        startLocation = (Button) findViewById(R.id.addfence);

        initViewPager();
        initTabHost();
    }

    /**
     * 初始化viewpager，用于滑动改变页面
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), initFragments());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(4);
    }

    /**
     * 初始化需要的四个Fragment
     */
    private List<Fragment> initFragments() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new ChaoshiFragment());
        list.add(new OrderFragment());
        list.add(new DiscoveryFragment());
        list.add(new PersonalFragment());
        return list;
    }

    /**
     * 初始化TabHost
     */
    private void initTabHost() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        //去掉分割线
        tabHost.getTabWidget().setDividerDrawable(null);
        //在bottom上面加一条分割线，背景设为divider，每个tab_indicator设置margin_top
        int color = getResources().getColor(R.color.divider);
        tabHost.getTabWidget().setBackgroundColor(color);

        //增加4个tab
        addTab(getString(titles[0]), R.drawable.selector_tab_0);
        addTab(getString(titles[1]), R.drawable.selector_tab_1);
        addTab(getString(titles[2]), R.drawable.selector_tab_2);
        addTab(getString(titles[3]), R.drawable.selector_tab_3);

        //注册Tab页切换事件
        tabHost.setOnTabChangedListener(this);
    }

    //添加tab页选项
    private void addTab(String title, int iconId) {
        //TabSpec表示Tab中的一页
        TabHost.TabSpec spec = tabHost.newTabSpec(title);
        View indicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView textView = (TextView) indicator.findViewById(R.id.indicator_title);
        textView.setText(title);
        ImageView imageView = (ImageView) indicator.findViewById(R.id.indicator_icon);
        imageView.setImageResource(iconId);
        spec.setIndicator(indicator);
        spec.setContent(new MyTabContentFactory(this));
        tabHost.addTab(spec);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((ChaoshibangApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        /*
        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
                    locationService.start();// 定位SDK
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText(getString(R.string.stoplocation));
                } else {
                    locationService.stop();
                    startLocation.setText(getString(R.string.startlocation));
                    LocationResult.setText("");
                }
            }
        });
        */
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (LocationResult != null) {
                LocationResult.setText(str);
                LogUtil.v("MainActivity", "onReceiveLocation " + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 设置滑动的同时Tab页的切换
     */
    @Override
    public void onPageSelected(int position) {
        int pos = viewPager.getCurrentItem();
        tabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击切换Tab页的响应
     */
    @Override
    public void onTabChanged(String tabId) {
        int pos = tabHost.getCurrentTab();
        viewPager.setCurrentItem(pos, false);

        //TODO
        //根据不同的页面设置不同的标题内容
        if(pos == TAB_INDEX_CHAOSHI) {

        } else if(pos == TAB_INDEX_ORDER) {

        } else if(pos == TAB_INDEX_DISCOVERY) {

        } else if(pos == TAB_INDEX_PERSONAL) {

        }
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                LogUtil.v("MainActivity", "onReceiveLocation");
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                logMsg(sb.toString());
            }
        }
    };

    /**
     * 自定义PagerAdapter
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    private class MyTabContentFactory implements TabHost.TabContentFactory {
        private Context context;

        public MyTabContentFactory(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View view = new View(context);
            view.setMinimumHeight(0);
            view.setMinimumWidth(0);
            return view;
        }
    }
}
