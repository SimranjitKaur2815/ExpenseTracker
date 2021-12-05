package com.example.expensetrackersystem.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.DatabaseClient;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.ui.fragments.AboutFragment;

import com.example.expensetrackersystem.ui.fragments.expenses.ExpensesFragment;
import com.example.expensetrackersystem.ui.fragments.home.HomeFragment;

import com.example.expensetrackersystem.ui.fragments.SettingsFragment;
import com.example.expensetrackersystem.ui.fragments.StarredFragment;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView navigationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbHelper.getInstance().isLoggedIn(this, new UserDbListener.AuthListener() {
            @Override
            public void onSuccess() {
                init();
            }

            @Override
            public void onFailure(String msg) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void init() {
        initElements();
        initListeners();
    }


    private void initElements() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationIcon = findViewById(R.id.navigationIcon);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);
        selectDrawerItem(R.id.nav_home);
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem.getItemId());
                    return true;
                });

        navigationIcon.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectDrawerItem(int menuItemId) {


        Fragment fragment = null;
        switch (menuItemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();

                break;
            case R.id.nav_all_expenses:
                fragment = new ExpensesFragment();

                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;
            case R.id.nav_starred:
                fragment = new StarredFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;


        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, fragment).commit();

        // Highlight the selected item has been done by NavigationView

        // Set action bar title
        // Close the navigation drawer
        drawerLayout.closeDrawers();

    }


}