package com.example.test.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.test.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter2 extends FragmentPagerAdapter {

    //@StringRes
    //private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter2(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new com.example.test.ProductsTab();
                break;
            case 1:
                fragment = new com.example.test.ShippingTab();
                break;
            case 2:
                fragment = new com.example.test.ImagesTab();
                break;
            case 3:
                fragment = new com.example.test.SimilarProductsTab();
                break;
        }
        return fragment;
    }



    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Product";
            case 1:
                return "Shipping";
            case 2:
                return "Images";
            case 3:
                return "similar";
        }
        return null;
    }
}