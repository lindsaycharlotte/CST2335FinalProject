package com.example.cst2335finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class EmptyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("EmptyActivity V1.0");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        Bundle dataToPass = getIntent().getExtras();

        DetailsFragment dFragment = new DetailsFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();
    }

    /**
     *
     * @param item The selected item
     * @return
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
        }
        else if (id == R.id.favourites) {
            Intent favourites = new Intent(this, Favourites.class);
            startActivity(favourites);
        }
        else if (id == R.id.exit) {
            finishAffinity();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    /**
     *
     * @param item The menu item that was selected.
     *
     * @return
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String message = null;
        int id = item.getItemId();
        if (id == R.id.help) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.help)
                    .setMessage(R.string.help_details)
                    .create().show();
        }
        return true;
    }

    /**
     *
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}