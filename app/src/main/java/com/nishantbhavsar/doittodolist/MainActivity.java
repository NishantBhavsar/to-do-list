package com.nishantbhavsar.doittodolist;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    public static final String MyCatPref = "catprefs" ;
    public static final String CATEGORIES = "categories";
    SharedPreferences sharedpreferences;

    String categoriesText = null;
    String categoriesTextPre = null;
    MenuItem mPreviousMenuItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title= getSupportActionBar().getTitle().toString();
                Intent intent = new Intent(MainActivity.this, add_task.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setCategoryListFromPref();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        setCategoryListFromPref();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        item.setCheckable(true);
        item.setChecked(true);

        if (mPreviousMenuItem != null && mPreviousMenuItem != item) {
            mPreviousMenuItem.setChecked(false);
        }
        mPreviousMenuItem = item;
        String title = item.getTitle().toString();
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        if (!title.equals("Create new Category") && !title.equals("Settings") ) {
            bundle.putString("whichlist",item.getTitle().toString() );
            fragment = new TaskFragment();
            fragment.setArguments(bundle);
        }else if (title.equals("Create new Category")) {
            Intent intent = new Intent(MainActivity.this, AddCategory.class);
            startActivity(intent);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.linearlayout_container, fragment).commit();
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setCategoryListFromPref(){
        sharedpreferences = getSharedPreferences(MyCatPref, Context.MODE_PRIVATE);
        categoriesText = sharedpreferences.getString(CATEGORIES, null);



            if (categoriesText != null) {
                List<String> categories = Arrays.asList(categoriesText.split(";"));
                Collections.sort(categories);
                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.lists_item_container);
                SubMenu submenu = menuItem.getSubMenu();
                submenu.removeGroup(R.id.lists_group);
                for(int i = 0; i < categories.size() ; i++){
                    //submenu.add(R.id.lists_group, Menu.NONE,1, cateories[i]);
                    submenu.add(R.id.lists_group, Menu.NONE, 1, categories.get(i)).setIcon(R.drawable.ic_category_24dp);
                }
            }


    }
}
