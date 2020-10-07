package com.example.paint;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assignar variável
        mDrawerLayout = findViewById(R.id.drawer);
    }

    public void ClickMenu(View view){
        //Open Drawer
        openDrawer(mDrawerLayout);
    }

    private static void openDrawer(DrawerLayout mDrawerLayout){
        //Open Drawer layout
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close Drawer
        closeDrawer(mDrawerLayout);
    }

    private static void closeDrawer(DrawerLayout mDrawerLayout) {
        //Close drawer layout
        //Check condition
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open...
            //Close drawer
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Recreate activity
        recreate();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(mDrawerLayout);
    }
}