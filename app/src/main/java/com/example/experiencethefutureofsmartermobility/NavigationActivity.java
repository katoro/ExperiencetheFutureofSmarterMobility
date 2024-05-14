package com.example.experiencethefutureofsmartermobility;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.experiencethefutureofsmartermobility.databinding.ActivityNavigationBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class NavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());

        setSupportActionBar(binding.appBarNavigation.toolbar);

        binding.appBarNavigation.collapsingToolbarLayout.setCollapsedTitleTextSize(50.0f);
        binding.appBarNavigation.collapsingToolbarLayout.setTitle(" ");
        binding.appBarNavigation.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        binding.appBarNavigation.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
            {
                //  Collapsed
                binding.appBarNavigation.collapsingToolbarLayout.setTitle("이 콘텐츠는 Amazon Bedrock을 사용하여 생성되었으므로 일부 콘텐츠는 사실과 다를 수 있으며 상업적 목적으로 활용할 수 없습니다.");

            }
            else
            {
                //Expanded
                binding.appBarNavigation.collapsingToolbarLayout.setTitle(" ");

            }
        });


        binding.appBarNavigation.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}