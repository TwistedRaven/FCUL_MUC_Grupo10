package com.example.paint;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaletteFragment.OnPaletteFragmentListener {
    private static final String canvasFragmentBundleKey = "kdoud8hcvduc";

    // Handling objects (Drawer layout...)
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    // Custom Toolbar - androidx one
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CanvasFragment cFragment;
    private PaletteFragment pFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        // This is in regards to the implements NavigationView.OnNavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener(this);

        // Inside this actionbarToggle - Passing parameters that are essential!
        // Passing context where the activity where we want the menu to appear
        // We need to pass the objects and the strings for the open/close

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        // Listener needs to be added for this toggle on the drawerLayout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // Enabling the hamburger sign
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        // FragmentManager is used to create the instances of a fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Will perform in the Fragment - Adding, Replacing, Removing
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // These serve to load the Fragment into this MainActivity

        // Load Canvas and Palette Fragment
        pFragment = new PaletteFragment();
        if (savedInstanceState != null) {
            cFragment = (CanvasFragment) getSupportFragmentManager().getFragment(savedInstanceState, canvasFragmentBundleKey);
            if (cFragment == null) {
                throw new IllegalArgumentException("Bundle returned null CanvasFragment.");
            }
        } else {
            cFragment = new CanvasFragment();
            fragmentTransaction.add(R.id.canvas_fragment, cFragment); //Container do Canvas Fragment
        }

        // You need to indicate the container fragment where they need to appear

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.add(R.id.palette_fragment, pFragment); //Container do Palette fragment
        }

        // Needs to be committed to be loaded into the app
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, canvasFragmentBundleKey, cFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Close the menu everytime you click on a menu item
        drawerLayout.closeDrawer(GravityCompat.START);

        //Now we use the IDs of the Menu Item to identify which menu was clicked
        if (menuItem.getItemId() == R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            cFragment = new CanvasFragment();
            pFragment = new PaletteFragment();
            // Replace the fragment so it doesn't keep stacking on top of itself
            fragmentTransaction.add(R.id.canvas_fragment, cFragment); //Container do Canvas Fragment

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fragmentTransaction.add(R.id.palette_fragment, pFragment); //Container do Palette fragment
            }

            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.about) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AboutFragment aFragment = new AboutFragment();
            fragmentTransaction.replace(R.id.canvas_fragment, aFragment);
            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SettingsFragment sFragment = new SettingsFragment();
            fragmentTransaction.replace(R.id.canvas_fragment, sFragment);
            fragmentTransaction.commit();
        }
        return true;
    }


    @Override
    public void messageCanvas(int color) {
        cFragment.changeCanvasColor(color);
    }

    @Override
    public void eraserCanvas() { cFragment.eraseCanvas(); }

    @Override
    public void undo() { cFragment.undo(); }
}