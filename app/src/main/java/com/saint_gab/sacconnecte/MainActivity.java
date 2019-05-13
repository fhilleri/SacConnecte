package com.saint_gab.sacconnecte;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private EquipmentFragment fragmentEquipment;
    private Fragment fragmentBluetooth;
    private Fragment fragmentSettings;

    //FOR DATAS
    private static final int FRAGMENT_BACKPACK = 0;
    private static final int FRAGMENT_TIMETABLE = 1;

    //FOR TIMETABLE
    private Timetable mTimetable;

    //FOR BACKPACK CONTENT
    private BackpackContent mBackpackContent;

    public void createEquipment(String id)
    {
        showEquipmentFragment();
        fragmentEquipment.newEquipmentFromId(id);
    }

    public void reloadSettingFragment()
    {
        fragmentSettings = null;
        showSettingsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("MainActivity", "onCreate ");
        super.onCreate(savedInstanceState);

        Log.i("MainActivity", "super.onCreate");
        configureLanguage();

        createNotificationChannel();
        setContentView(R.layout.activity_main);

        //Configuration de tous les views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureTimeTable();
        this.configureBackpackContent();
        //startService();

        showBackpackFragment();//Affichage de la page backpack au lancement de l'appli
    }

    private void configureLanguage()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String languageCode = pref.getString("language", "fr");
        Log.i("Language", "language is : " + languageCode);
        LanguageHelper.changeLocale(getResources(), languageCode);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BACKPACK CONTENT CHANNEL";
            String description = "Channel to display bakcpack content";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.saint_gab.sacconnecte", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
            case R.id.activity_main_drawer_equipment :
                showEquipmentFragment();
                break;
            case R.id.activity_main_drawer_bluetooth :
                showBluetoothFragment();
                break;
            case R.id.activity_main_drawer_settings :
                showSettingsFragment();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //création de chaque page et affichage de celles ci
    private void showBackpackFragment()
    {
        toolbar.setTitle(getString(R.string.menu_backpack));
        if (this.fragmentBackpack == null) this.fragmentBackpack = BackpackFragment.newInstance(mBackpackContent, mTimetable, this);
        this.startTransactionFragment(this.fragmentBackpack);
    }

    private void showTimetableFragment()
    {
        toolbar.setTitle(getString(R.string.menu_timetable));
        if (this.fragmentTimetable == null) this.fragmentTimetable = TimetableFragment.newInstance(mTimetable);
        this.startTransactionFragment(this.fragmentTimetable);

    }

    private void showSubjectFragment()
    {
        toolbar.setTitle(getString(R.string.menu_subjects));
        if (this.fragmentSubject == null) this.fragmentSubject = SubjectFragment.newInstance(mTimetable);
        this.startTransactionFragment(this.fragmentSubject);

    }

    private void showEquipmentFragment()
    {
        toolbar.setTitle(getString(R.string.menu_equipments));
        if (this.fragmentEquipment == null) this.fragmentEquipment = EquipmentFragment.newInstance(mTimetable);
        this.startTransactionFragment(this.fragmentEquipment);
    }

    private void showBluetoothFragment()
    {
        toolbar.setTitle(getString(R.string.menu_bluetooth));
        if (this.fragmentBluetooth == null) this.fragmentBluetooth = BluetoothFragment.newInstance(mBackpackContent);
        this.startTransactionFragment(this.fragmentBluetooth);
    }

    private void showSettingsFragment()
    {
        toolbar.setTitle(getString(R.string.menu_settings));
        if (this.fragmentSettings == null) this.fragmentSettings = SettingsFragment.newInstance(mTimetable, this);
        this.startTransactionFragment(this.fragmentSettings);
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
        getSupportActionBar().setTitle(getString(R.string.menu_backpack));
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
        mTimetable = new Timetable(getApplicationContext());
    }

    private void configureBackpackContent()
    {
        mBackpackContent = new BackpackContent(mTimetable, this, getResources());
    }
}
