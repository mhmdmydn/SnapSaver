package com.ghodel.snapsaver.activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.fragment.PhotoFragment;
import com.ghodel.snapsaver.fragment.VideosFragment;
import com.ghodel.snapsaver.utils.MainUtil;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private MeowBottomNavigation meowBottomNavigation;
    private FrameLayout container;

    private final static int ID_PHOTOS = 1;
    private final static int ID_VIDEOS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initLogic();
        initListener();
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);


        meowBottomNavigation = findViewById(R.id.meow_bottom_navigation);
        container = findViewById(R.id.container);

    }

    @Override
    public void initLogic() {

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        navigationView.getMenu().getItem(0).setChecked(true);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PhotoFragment()).commit();

        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_PHOTOS, R.drawable.ic_photos));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(ID_VIDEOS, R.drawable.ic_videos));

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragmentSelected = null;

                switch (item.getId()){
                    case ID_PHOTOS:
                        fragmentSelected = new PhotoFragment();
                        break;
                    case ID_VIDEOS:
                        fragmentSelected = new VideosFragment();
                        break;
                }

                assert fragmentSelected != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSelected).commit();
            }
        });

        meowBottomNavigation.show(ID_PHOTOS, true);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:

                break;

            case R.id.nav_account:

                break;

            case R.id.nav_notification:

                break;

            case R.id.nav_settings:

                break;

            case R.id.nav_view:

                break;
        }

        return true;
    }
}