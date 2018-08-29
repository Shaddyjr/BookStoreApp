package com.example.android.bookstoreapp;

import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllItemsFragment extends CategoryFragment{


    public AllItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_items_activity, container, false);
        Resources res = getResources();

        // setting up list adapter
//        CategoryCursorFragmentPagerAdapter categoryAdapter= new LocationAdapter(getActivity(), locations);

        ListView listView = rootView.findViewById(R.id.category_items_container);

//        listView.setAdapter(locationAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
