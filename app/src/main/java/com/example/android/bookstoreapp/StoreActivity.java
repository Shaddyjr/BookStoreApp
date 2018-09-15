package com.example.android.bookstoreapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StoreActivity extends AppCompatActivity {
    CategoryFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_store_activity);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        mAdapter = new CategoryFragmentAdapter(this,getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(mAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remember to add intent activity to the Manifest
                Intent intent = new Intent(StoreActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }
}
