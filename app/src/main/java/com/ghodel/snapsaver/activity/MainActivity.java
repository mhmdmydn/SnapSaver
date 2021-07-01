package com.ghodel.snapsaver.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.ghodel.snapsaver.service.PhoneClipBoard;
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

        // clipboard listener
        Intent svc = new Intent(MainActivity.this, PhoneClipBoard.class);
        startService(svc);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

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
        Fragment fragmentSelected = null;
        switch (item.getItemId()){
            case R.id.nav_photo:
                drawer.closeDrawers();
                fragmentSelected = new PhotoFragment();
                meowBottomNavigation.show(ID_PHOTOS, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSelected).commit();
                break;

            case R.id.nav_video:
                drawer.closeDrawers();
                fragmentSelected = new VideosFragment();
                meowBottomNavigation.show(ID_VIDEOS, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSelected).commit();
                break;

            case R.id.nav_directmsg:
                drawer.closeDrawers();
                Intent go = new Intent(Intent.ACTION_VIEW);
                go.setClass(MainActivity.this, DirectMessageActivity.class);
                startActivity(go);
                break;

            case R.id.nav_stiker_maker:
                startActivity(new Intent(Intent.ACTION_VIEW).setClass(this, StickerMakerActivity.class));
                drawer.closeDrawers();
                break;

            case R.id.nav_share_app:
                MainUtil.shareApplication(getApplicationContext());
                drawer.closeDrawers();
                break;

            case R.id.nav_rate_app:
                drawer.closeDrawers();
                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                }
                break;
            case R.id.nav_more_app:
                drawer.closeDrawers();
                String devName = "Ghodel Dev";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:"+devName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:"+devName)));
                }
                break;

            default:

        }

        return true;
    }
}