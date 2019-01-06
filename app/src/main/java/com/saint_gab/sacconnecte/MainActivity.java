package com.saint_gab.sacconnecte;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    //identification de chaque fragment par un nombre
    private Fragment fragmentBackpack;
    private Fragment fragmentTimetable;
    private Fragment fragmentSubject;

    //FOR DATAS
    private static final int FRAGMENT_BACKPACK = 0;
    private static final int FRAGMENT_TIMETABLE = 1;

    //FOR TIMETABLE
    private Timetable mTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configuration de tous les views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureTimeTable();
        showBackpackFragment();//Affichage de la page backpack au lancement de l'appli
    }

    @Override
    public void onBackPressed()
    {
        // 5 - Ferme le menu si le bouton retour est pressé
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch(id)
        {
            case R.id.activity_main_drawer_backpack :
                //showFragment(FRAGMENT_BACKPACK);
                showBackpackFragment();
                break;
            case R.id.activity_main_drawer_timetable :
                //showFragment(FRAGMENT_TIMETABLE);
                showTimetableFragment();
                break;
            case R.id.activity_main_drawer_subjects :
                showSubjectFragment();
                break;
            case R.id.activity_main_drawer_RFID :
                break;
            case R.id.activity_main_drawer_settings :
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




    // FRAGMENTS

    // - 5 Show fragment according an Identifier

    /*private void showFragment(int fragmentIdentifier)
    {
        switch (fragmentIdentifier)
        {
            case FRAGMENT_BACKPACK:
                toolbar.setTitle("Sac à dos");
                this.showBackpackFragment();
                break;
            case FRAGMENT_TIMETABLE:
                toolbar.setTitle("Emploi du temps");
                this.showTimetableFragment();
                break;
            case FRAGMENT_SUBJECTS:
                toolbar.setTitle("Matières");
                this.showTimetableFragment();
                break;
            default:
                break;
        }
    }
*/
    //création de chaque page et affichage de celles ci
    private void showBackpackFragment()
    {
        toolbar.setTitle("Sac à dos");
        if (this.fragmentBackpack == null) this.fragmentBackpack = BackpackFragment.newInstance();
        this.startTransactionFragment(this.fragmentBackpack);
    }

    private void showTimetableFragment()
    {
        toolbar.setTitle("Emploi du temps");
        if (this.fragmentTimetable == null) this.fragmentTimetable = TimetableFragment.newInstance(mTimetable);
        this.startTransactionFragment(this.fragmentTimetable);

    }

    private void showSubjectFragment()
    {
        toolbar.setTitle("Matières");
        if (this.fragmentSubject == null) this.fragmentSubject = SubjectFragment.newInstance(mTimetable);
        this.startTransactionFragment(this.fragmentSubject);

    }

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment)
    {
        if (!fragment.isVisible())
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }




    //CONFIGURATION

    // 1 - Configuration de ToolBar
    private void configureToolBar()
    {
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sac à dos");
    }

    // 2 - Configuration de Drawer Layout
    private void configureDrawerLayout()
    {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configuration de NavigationView
    private void configureNavigationView()
    {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureTimeTable()
    {
        mTimetable = new Timetable();
    }
}
