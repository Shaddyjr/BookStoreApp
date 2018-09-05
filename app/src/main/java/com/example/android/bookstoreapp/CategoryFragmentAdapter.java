package com.example.android.bookstoreapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    public CategoryFragmentAdapter(Context c, FragmentManager fm) {
        super(fm);
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new AllItemsFragment();
            case 1:
                return new ByNameFragment();
            case 2:
                return new ByPriceFragment();
            case 3:
                return new ByQuantityFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return mContext.getString(R.string.category_title_allProducts).toUpperCase();
            case 1:
                return mContext.getString(R.string.category_title_productsByName).toUpperCase();
            case 2:
                return mContext.getString(R.string.category_title_productsByPrice).toUpperCase();
            case 3:
                return mContext.getString(R.string.category_title_productsByQuantity).toUpperCase();
            default:
                return null;
        }
    }
}
