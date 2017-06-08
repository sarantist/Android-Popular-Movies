package com.example.akis.popularmoviez.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import com.example.akis.popularmoviez.fragments.DetailedFragment;
import com.example.akis.popularmoviez.R;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        Bundle args = new Bundle();
        args.putParcelable("selected_item", intent.getParcelableExtra("selected_item"));

        DetailedFragment newFragment = new DetailedFragment();
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_detailed, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_detailed, menu);
        return true;
    }

}
