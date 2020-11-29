package com.example.paint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, PaletteFragment.OnPaletteFragmentListener {
    private static final String canvasFragmentBundleKey = "kdoud8hcvduc";

    // Handling objects (Drawer layout...)
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    // Custom Toolbar - androidx one
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CanvasFragment canvasFragment;
    private PaletteFragment paletteFragment;
    private SettingsFragment settingsFragment;
    private AboutFragment aboutFragment;

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

        settingsFragment = new SettingsFragment();
        fragmentTransaction.add(R.id.main_body, settingsFragment).hide(settingsFragment);

        aboutFragment = new AboutFragment();
        fragmentTransaction.add(R.id.main_body, aboutFragment).hide(aboutFragment);

        // Load Canvas and Palette Fragment
        paletteFragment = new PaletteFragment();
        if (savedInstanceState != null) {
            canvasFragment = (CanvasFragment) getSupportFragmentManager().getFragment(savedInstanceState, canvasFragmentBundleKey);
            if (canvasFragment == null) {
                throw new IllegalArgumentException("Bundle returned null CanvasFragment.");
            }
        } else {
            canvasFragment = new CanvasFragment();
            fragmentTransaction.add(R.id.main_body, canvasFragment); //Container do Canvas Fragment
        }

        // You need to indicate the container fragment where they need to appear

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.add(R.id.palette_fragment, paletteFragment); //Container do Palette fragment
            fragmentTransaction.hide(settingsFragment).hide(aboutFragment);
        }

        // Needs to be committed to be loaded into the app
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, canvasFragmentBundleKey, canvasFragment);
    }

    @Override
    public boolean onNavigationItemSelected(final @NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.map){
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to change to map drawing?")
                    .setMessage("You will lose all unsaved drawing")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent switch_to_main = new Intent(getApplicationContext(), MapActivity.class);
                            startActivityForResult(switch_to_main, 1);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }else {
            // Close the menu everytime you click on a menu item
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (menuItem.getItemId() == R.id.home) {
                fragmentTransaction.hide(settingsFragment).hide(aboutFragment);
                fragmentTransaction.show(canvasFragment);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fragmentTransaction.show(paletteFragment);
                } else {
                    fragmentTransaction.hide(paletteFragment);
                }
            } else if (menuItem.getItemId() == R.id.about) {
                fragmentTransaction.hide(settingsFragment).hide(canvasFragment).hide(paletteFragment);
                fragmentTransaction.show(aboutFragment);
            } else if (menuItem.getItemId() == R.id.settings) {
                fragmentTransaction.hide(aboutFragment).hide(canvasFragment).hide(paletteFragment);
                fragmentTransaction.show(settingsFragment);
            } else {
                throw new IllegalStateException("MenuItem return invalid ItemId.");
            }
            fragmentTransaction.commit();
        }
        return true;
    }


    @Override
    public void messageCanvas(int color) {
        canvasFragment.changeCanvasColor(color);
    }

    @Override
    public void eraserCanvas() {
        canvasFragment.eraseCanvas();
    }

    @Override
    public void undo() {
        canvasFragment.undo();
    }

    public CanvasFragment getCanvasFragment() {
        return canvasFragment;
    }
}