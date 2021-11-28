package com.example.expensetrackersystem.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.ui.fragments.AboutFragment;
import com.example.expensetrackersystem.ui.fragments.Expenses.ExpensesFragment;
import com.example.expensetrackersystem.ui.fragments.HomeFragment;
import com.example.expensetrackersystem.ui.fragments.SettingsFragment;
import com.example.expensetrackersystem.ui.fragments.StarredFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView navigationIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        navigationIcon=findViewById(R.id.navigationIcon);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, new HomeFragment()).commit();


        navigationDrawer(navigationView);

        navigationIcon.setOnClickListener(view->{
        drawerLayout.openDrawer(GravityCompat.START);
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void navigationDrawer(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment=null;

        switch(menuItem.getItemId()){
            case R.id.nav_home: fragment=new HomeFragment();

                break;
            case R.id.nav_all_expenses: fragment=new ExpensesFragment();

                break;
            case R.id.nav_settings: fragment=new SettingsFragment();
                break;
            case R.id.nav_about: fragment=new AboutFragment();
                break;
            case R.id.nav_starred: fragment=new StarredFragment();
                break;
            default:fragment=new HomeFragment();
            break;



        }
        menuItem.setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout_main, fragment).commit();

        // Highlight the selected item has been done by NavigationView

        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();

    }


}