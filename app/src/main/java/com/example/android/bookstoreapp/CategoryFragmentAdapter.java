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
        return new AllItemsFragment();
//        if (position == 0) {
//            return new AllItemsFragment();
//        }
//        else if (position == 1){
//            return  new AllItemsFragment();
//        } else if (position == 2) {
//            return  new AllItemsFragment();
//        } else{
//            return  new AllItemsFragment();
//        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_title_allProducts).toUpperCase();
        } else if (position == 1) {
            return mContext.getString(R.string.category_title_productsByName).toUpperCase();
        } else if (position == 2) {
            return mContext.getString(R.string.category_title_productsByPrice).toUpperCase();
        } else {
            return mContext.getString(R.string.category_title_productsByQuantity).toUpperCase();
        }
    }
}
