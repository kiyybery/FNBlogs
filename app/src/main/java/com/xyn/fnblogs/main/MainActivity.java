package com.xyn.fnblogs.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.xyn.fnblogs.R;
import com.xyn.fnblogs.base.BaseActivity;
import com.xyn.fnblogs.base.BaseSwipeListFragment;
import com.xyn.fnblogs.blogs.BlogListFragment;
import com.xyn.fnblogs.image.ZImage;
import com.xyn.fnblogs.lib.ZDisplay;
import com.xyn.fnblogs.news.NewsListFragment;
import com.xyn.fnblogs.util.ConfigConstant;
import com.xyn.fnblogs.util.DBHelper;
import com.xyn.fnblogs.util.HtmlHelper;
import com.xyn.fnblogs.util.SQLiteHelper;

public class MainActivity extends BaseActivity implements INavigatorCallback{

    private static final int STATE_NEWS = 1;
    private static final int STATE_BLOG = 2;
    private DrawerLayout mDrawerLayout;
    private View mNavigatorView;
    private boolean mIsNightMode = false;
    private int currentState = -1;
    private Fragment currentFragment;
    private SparseArray<Fragment> fragmentArray;
    private boolean mIsNavigatorOpened;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mIsNightMode = getApp().isNightMode();
        fragmentArray = new SparseArray<>(2);

        addFragment(STATE_BLOG);
        addFragment(STATE_NEWS);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mNavigatorView = findViewById(R.id.main_navigator);
        NavigatorFragment navigatorFragment = (NavigatorFragment) getSupportFragmentManager().findFragmentById(R.id.main_navigator);
        navigatorFragment.InitDrawToggle(mDrawerLayout);

        ZDisplay.init(getApp());

        if (getApp().getScreenWidth() == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            getApp().setScreenWidth(dm.widthPixels);
            getApp().setScreenHeight(dm.heightPixels);
            int screenWidth = ZDisplay.getInstance().Px2Dp(dm.widthPixels);
            getApp().setScreenWidthInDP(screenWidth);
        }

        ZImage.init(getApp());
        DBHelper.init(getApp().getFileRootDir());
        SQLiteHelper.Init(getApp());
        HtmlHelper.init(getApp());

        getApp().autoCleanCache(ConfigConstant.CACHE_AVAILABLE_DAYS);
    }

    private void addFragment(int state) {
        if (fragmentArray.get(state) == null) {
            Fragment fragment = null;

            switch (state) {
                case STATE_NEWS:
                    fragment = NewsListFragment.newInstance();
                    break;
                case STATE_BLOG:
                    fragment = BlogListFragment.newInstance();
                    break;
                default:
                    throw new IllegalArgumentException("指定类型 " + state + " 的fragment的类型没用命中，出错位置MainActivity=>addFragment()");
            }
            fragmentArray.put(state, fragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsNightMode != getApp().isNightMode()) {
            recreateOnResume();
        }
    }

    @Override
    public void openNavigator() {
        mDrawerLayout.openDrawer(mNavigatorView);
        mIsNavigatorOpened = true;
    }

    @Override
    public void closeNavigator() {
        mDrawerLayout.closeDrawer(mNavigatorView);
        mIsNavigatorOpened = false;
    }

    public void switchNavigator() {
        if (mIsNavigatorOpened)
            closeNavigator();
        else
            openNavigator();

        mIsNavigatorOpened = !mIsNavigatorOpened;
    }

    @Override
    public void onClickNews() {
        if (currentState == STATE_NEWS) {

            Toast.makeText(MainActivity.this, "click again", Toast.LENGTH_LONG).show();
            return;
        } else {

            currentState = STATE_NEWS;
            Fragment fragment = fragmentArray.get(STATE_NEWS);
            ReplaceFragment(fragment);
        }
    }

    @Override
    public void onClickBlog() {
        if (currentState == STATE_BLOG)
            return;

        currentState = STATE_BLOG;
        Fragment fragment = fragmentArray.get(STATE_BLOG);
        ReplaceFragment(fragment);
    }

    public void ReplaceFragment(Fragment fragment) {
        if (currentFragment != null && currentFragment instanceof BaseSwipeListFragment)
            ((BaseSwipeListFragment) currentFragment).cancelLoadingTask();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
        currentFragment = fragment;
    }
}
