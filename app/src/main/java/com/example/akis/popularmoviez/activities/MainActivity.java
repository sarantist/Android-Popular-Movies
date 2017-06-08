package com.example.akis.popularmoviez.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.akis.popularmoviez.fragments.GridFragment;
import com.example.akis.popularmoviez.classes.Movie;
import com.example.akis.popularmoviez.R;


public class MainActivity extends AppCompatActivity implements GridFragment.OnListFragmentInteractionListener {
    Fragment gridFragment;
    private FragmentRefreshListener fragmentRefreshListener;
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private Integer selected_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            gridFragment = getSupportFragmentManager().getFragment(savedInstanceState, "gridFragment");
            selected_item = savedInstanceState.getInt("selection");
        } else {
            selected_item = 0;
            gridFragment = new GridFragment();
            // Begin the transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, gridFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "gridFragment", gridFragment);
        // Save menu selection
        outState.putInt("selection", selected_item);
    }

    @Override
    public void onListFragmentInteraction(Movie item) {
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("selected_item", item);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        // Enable all options
        menu.setGroupEnabled(0, true);
        // Disable selected option
        menu.getItem(selected_item).setEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.most_popular:
                    selected_item = 0;
                    getFragmentRefreshListener().onRefresh("popular");
                return true;
            case R.id.top_rated:
                    selected_item = 1;
                    getFragmentRefreshListener().onRefresh("top_rated");
                return true;
            case R.id.favorite:
                    selected_item = 2;
                    getFragmentRefreshListener().onRefresh("favorite");
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public interface FragmentRefreshListener {
        void onRefresh(String selection);
    }



}
