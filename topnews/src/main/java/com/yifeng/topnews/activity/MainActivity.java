package com.yifeng.topnews.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.topnews.R;
import com.yifeng.topnews.adapter.NewsFragmentPagerAdapter;
import com.yifeng.topnews.bean.NewsClassify;
import com.yifeng.topnews.fragment.NewsFragment;
import com.yifeng.topnews.tools.Constants;
import com.yifeng.topnews.view.ColumnHorizontalScrollView;
import com.yifeng.topnews.tools.BaseTools;

import java.util.ArrayList;

/**
 * Created by donggua on 2015/11/10.
 */
public class MainActivity extends FragmentActivity {
    /** ×Ô¶¨ÒåHorizontalScrollView */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ImageView button_more_columns;
    /** ÐÂÎÅ·ÖÀàÁÐ±í*/
    private ArrayList<NewsClassify> newsClassify=new ArrayList<NewsClassify>();
    /** µ±Ç°Ñ¡ÖÐµÄÀ¸Ä¿*/
    private int columnSelectIndex = 0;
    /** ×óÒõÓ°²¿·Ö*/
    public ImageView shade_left;
    /** ÓÒÒõÓ°²¿·Ö */
    public ImageView shade_right;
    /** ÆÁÄ»¿í¶È */
    private int mScreenWidth = 0;
    /** Item¿í¶È */
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 7;// Ò»¸öItem¿í¶ÈÎªÆÁÄ»µÄ1/7
        initView();
    }
    /** ³õÊ¼»¯layout¿Ø¼þ*/
    private void initView() {
        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        setChangelView();
    }
    /**
     *  µ±À¸Ä¿Ïî·¢Éú±ä»¯Ê±ºòµ÷ÓÃ
     * */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }
    /** »ñÈ¡ColumnÀ¸Ä¿ Êý¾Ý*/
    private void initColumnData() {
        newsClassify = Constants.getData();
    }

    /**
     *  ³õÊ¼»¯ColumnÀ¸Ä¿Ïî
     * */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  newsClassify.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);
            localTextView.setText(newsClassify.get(i).getTitle());
            //localTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(localTextView, i ,params);
        }
    }
    /**
     *  Ñ¡ÔñµÄColumnÀïÃæµÄTab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //ÅÐ¶ÏÊÇ·ñÑ¡ÖÐ
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }
    /**
     *  ³õÊ¼»¯Fragment
     * */
    private void initFragment() {
        int count =  newsClassify.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", newsClassify.get(i).getTitle());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }
    /**
     *  ViewPagerÇÐ»»¼àÌý·½·¨
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
