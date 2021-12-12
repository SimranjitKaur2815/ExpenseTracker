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
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.ui.activities.profile.ProfileActivity;
import com.example.expensetrackersystem.ui.fragments.AboutFragment;

import com.example.expensetrackersystem.ui.fragments.expenses.ExpensesFragment;
import com.example.expensetrackersystem.ui.fragments.expenses.ExpensesListener;
import com.example.expensetrackersystem.ui.fragments.home.HomeFragment;

import com.example.expensetrackersystem.ui.fragments.StarredFragment;
import com.example.expensetrackersystem.utils.DateHelper;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ExpensesListener.AllExpenseListener {

    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    TextView toolbarTitle;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView navigationIcon, myProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbHelper.getInstance().isLoggedIn(this, new UserDbListener.onAuthListener() {
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
        myProfileIcon = findViewById(R.id.myProfileIcon);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationIcon = findViewById(R.id.navigationIcon);
        toolbarTitle=findViewById(R.id.toolbarTitle);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);
        selectDrawerItem(R.id.nav_home, null);
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem.getItemId(), null);
                    return true;
                });

        navigationIcon.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        myProfileIcon.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectDrawerItem(int menuItemId, Date createdDate) {


        Fragment fragment = null;
        switch (menuItemId) {
            case R.id.nav_home:
                if (createdDate == null){
                    toolbarTitle.setText("Expense Tracker");
                    fragment = new HomeFragment();
                }
                else
                {
                    toolbarTitle.setText("Expense Tracker");
                    fragment = new HomeFragment(createdDate);
                }
                break;
            case R.id.nav_all_expenses:
                toolbarTitle.setText("All Expenses");
                fragment = new ExpensesFragment(this);

                break;

            case R.id.nav_about:
                toolbarTitle.setText("About");
                fragment = new AboutFragment();
                break;
            case R.id.nav_starred:
                toolbarTitle.setText("Starred");
                fragment = new StarredFragment();
                break;
            default:
                toolbarTitle.setText("Expense Tracker");
                fragment = new HomeFragment();
                break;


        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, fragment).commit();

        // Highlight the selected item has been done by NavigationView

        // Close the navigation drawer
        drawerLayout.closeDrawers();

    }

    // Edit all expenses
    @Override
    public void onEdit(String createdDate) {
        // Highlight the selected item by NavigationView
        navigationView.getMenu().getItem(0).setChecked(true);
        selectDrawerItem(R.id.nav_home, DateHelper.convertStringToDate(createdDate));
    }
}