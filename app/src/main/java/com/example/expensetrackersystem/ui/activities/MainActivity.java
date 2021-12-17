package com.example.expensetrackersystem.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetrackersystem.R;
import com.example.expensetrackersystem.database.entities.User;
import com.example.expensetrackersystem.ui.activities.login.LoginActivity;
import com.example.expensetrackersystem.ui.activities.profile.ProfileActivity;
import com.example.expensetrackersystem.ui.fragments.AboutFragment;

import com.example.expensetrackersystem.ui.fragments.expenses.ExpensesFragment;
import com.example.expensetrackersystem.ui.fragments.expenses.ExpensesListener;
import com.example.expensetrackersystem.ui.fragments.home.HomeFragment;

import com.example.expensetrackersystem.utils.DateHelper;
import com.example.expensetrackersystem.utils.db.DbHelper;
import com.example.expensetrackersystem.utils.db.UserDbListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.Locale;




public class MainActivity extends AppCompatActivity implements ExpensesListener.AllExpenseListener {

    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    TextView toolbarTitle;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView navigationIcon;
    TextView myProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbHelper.getInstance().isLoggedIn(this, new UserDbListener.onAuthListener() {
            @Override
            public void onSuccess() {
                init();//initializing the xml elements with their respective IDs.
                //getting instance of database which tells the application that who the current user is
                DbHelper.getInstance().getCurrentUser(MainActivity.this, new UserDbListener.onGetCurrentUserListener() {
                    @Override
                    public void onSuccess(User user) {
                        //setting text user's name 's first letter which will display as an image to the end user
                            myProfileIcon.setText(user.getFirstName().substring(0,1).toUpperCase(Locale.ROOT));
                    }

                    @Override
                    public void onFailure(String msg) {
                        //if instance of database fails to do what user have asked to, then a toast would be displayed
                        //to the end user that user not logged in
                        Toast.makeText(MainActivity.this, "User not logged in.", Toast.LENGTH_SHORT).show();
                        //start activity will first initialize the intent and then allow the user to go from Main activity
                        //to Login activity by clearing all the flags
                        startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finishAffinity();
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void init() {
        initElements();//this method initializes the xml elements and link it to the activity
        initListeners();////setting listeners on the xml elements
    }


    private void initElements() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        myProfileIcon = findViewById(R.id.myProfileIcon);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationIcon = findViewById(R.id.navigationIcon);
        toolbarTitle=findViewById(R.id.toolbarTitle);
        //drawer layout is the base layout for navigation view, so here adding drawer listener to actionbar toggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //setting sync state to toggle
        actionBarDrawerToggle.syncState();
        //navigation view will call the get Menu method to have all the items inside it
        navigationView.getMenu().getItem(0).setChecked(true);
        selectDrawerItem(R.id.nav_home, null);
    }

    private void initListeners() {
        //setting a listener to navigation, so that every time user clicks on navigation,
        // some action needs to be perform as soon as possible
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem.getItemId(), null);
                    return true;
                });
        //setting an on click listener to navigation icon
        navigationIcon.setOnClickListener(view -> {
            //drawer layout will be open from the start position
            drawerLayout.openDrawer(GravityCompat.START);
        });
        //when user clicks on MyProfile icon, an intent will be start
        // and let the user go to the ProfileActivity
        myProfileIcon.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

    }

    //setting up a boolean function to the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectDrawerItem(int menuItemId, Date createdDate) {

        //initializing fragment to the activity so that user can have one activity but many fragments
        Fragment fragment = null;
        //switch method will hold the menu item id to make the further decisions
        switch (menuItemId) {
            //case will pass if menu item id is home otherwise it will go to next case
            case R.id.nav_home:
                //if created date is null then toolbar will set its text to expense tracker
                if (createdDate == null){
                    toolbarTitle.setText("Expense Tracker");
                    //setting home fragment to the fragment
                    fragment = new HomeFragment();
                }
                else
                {
                    //toolbar will set its text to expense tracker
                    toolbarTitle.setText("Expense Tracker");
                    //setting home fragment passing a parameter createdDate to the fragment
                    fragment = new HomeFragment(createdDate);
                }
                break;
            //case will pass if menu item id is all expenses otherwise it will go to next case
            case R.id.nav_all_expenses:
                //toolbar title will set its text to All expenses
                toolbarTitle.setText("All Expenses");
                //setting expense fragment to the fragment
                fragment = new ExpensesFragment(this);

                break;
            //case will pass if menu item id is home otherwise it will go to next case
            case R.id.nav_about:
                //toolbar title will set its text to About fragment
                toolbarTitle.setText("About");
                //setting about fragment to the fragment
                fragment = new AboutFragment();
                break;

            case R.id.nav_logout:
                //if user clicks on logout item from menu the an
                // alert dialog builder will shown up to the user
                AlertDialog.Builder logoutBuilder=new AlertDialog.Builder(this);
                //setting title to the dialog builder
                logoutBuilder.setTitle("Logout Confirmation");
                //setting message to the dialog builder
                logoutBuilder.setMessage("Are you sure, you want to logout?");
                //setting positive button to the dialog
                logoutBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbHelper.getInstance().logoutUser(false, MainActivity.this, new UserDbListener.onAuthListener() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finishAffinity();
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                //setting positive button to the dialog
                logoutBuilder.setNegativeButton("Cancel",null);
                logoutBuilder.create().show();

                break;
            default:
                //by default, toolbar title will remain expense tracker and user will stay at the home fragment only
                toolbarTitle.setText("Expense Tracker");
                fragment = new HomeFragment();
                break;


        }
        if(fragment!=null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout_main, fragment).commit();
        }

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