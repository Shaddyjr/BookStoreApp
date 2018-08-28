package com.example.android.bookstoreapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllItemsFragment extends Fragment {

    public AllItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_items_activity, container, false);
        Resources res = getResources();

        // setting up list adapter
//        LocationAdapter locationAdapter = new LocationAdapter(getActivity(), locations);
//
//        ListView listView = rootView.findViewById(R.id.list_items_container);
//
//        listView.setAdapter(locationAdapter);
//
        return rootView;
    }
}
