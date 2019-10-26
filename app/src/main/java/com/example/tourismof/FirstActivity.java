package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tourismof.Fragments.ChatFragment;
import com.example.tourismof.Fragments.FavoriteFragment;
import com.example.tourismof.Fragments.Fragment_mypost;
import com.example.tourismof.Fragments.Fragment_post;
import com.example.tourismof.Fragments.Fragment_setting;
import com.example.tourismof.Fragments.HomeFragment;
import com.example.tourismof.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.tourismof.LoginActivity.MY_PREFS_NAME;

public class FirstActivity extends AppCompatActivity {

    private TextView mTextMessage;
     String Host;
     int first;
     int a;
  public static   BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(first==2)
            {
                switch (item.getItemId()) {
                    case R.id.post:
                        FragmentManager fmhh = getSupportFragmentManager();
                        FragmentTransaction fth = fmhh.beginTransaction();
                        Fragment fragmenth = new Fragment_post();
                        fth.replace(R.id.frame, fragmenth);
                        fth.addToBackStack(null);
                        fth.commit();
                        return true;

                    case R.id.my_posts:
//                    mTextMessage.setText(R.string.title_notifications);
                        FragmentManager fmp = getSupportFragmentManager();
                        FragmentTransaction ftp = fmp.beginTransaction();
                        Fragment fragmentpost = new Fragment_mypost();
                        ftp.replace(R.id.frame, fragmentpost);
                        ftp.addToBackStack(null);
                        ftp.commit();
                        return true;

                    case R.id.msgs:
//                    mTextMessage.setText(R.string.title_notifications);
                        FragmentManager fmm = getSupportFragmentManager();
                        FragmentTransaction ftm = fmm.beginTransaction();
                        Fragment fragmentchats = new ChatFragment();
                        ftm.addToBackStack(null);
                        ftm.replace(R.id.frame, fragmentchats);

                        ftm.commit();
                        return true;

                        case R.id.nav_Profile:
//                    mTextMessage.setText(R.string.title_profile);
                        FragmentManager fmn = getSupportFragmentManager();
                        FragmentTransaction ftn = fmn.beginTransaction();
                        Fragment fragmentprofile = new ProfileFragment();
                        ftn.replace(R.id.frame, fragmentprofile);
                            ftn.addToBackStack(null);
                            ftn.commit();
                        return true;
                    case R.id.nav_settings:
//                    mTextMessage.setText(R.string.title_profile);
                        FragmentManager fms = getSupportFragmentManager();
                        FragmentTransaction fts = fms.beginTransaction();
                        Fragment fragmentsetting = new Fragment_setting();
                        fts.replace(R.id.frame, fragmentsetting);
                        fts.addToBackStack(null);
                        fts.commit();
                        return true;


                }

            }

               else if (first==1)
            {
                switch (item.getItemId()) {

                    case R.id.Home_button:

                        FragmentManager fmh = getSupportFragmentManager();
                        FragmentTransaction fth = fmh.beginTransaction();
                        Fragment fragmenth = new HomeFragment();
                        fth.replace(R.id.frame, fragmenth);
                        fth.addToBackStack(null);

                        fth.commit();
                        return true;

                    case R.id.favourite_posts_button:
//                    mTextMessage.setText(R.string.title_notifications);
                        FragmentManager fmp = getSupportFragmentManager();
                        FragmentTransaction ftp = fmp.beginTransaction();
                        Fragment fragmentpost = new FavoriteFragment();
                        ftp.replace(R.id.frame, fragmentpost);
                        ftp.addToBackStack(null);

                        ftp.commit();
                        return true;


                    case R.id.msgs:
//                    mTextMessage.setText(R.string.title_notifications);
                        FragmentManager fmm = getSupportFragmentManager();
                        FragmentTransaction ftm = fmm.beginTransaction();
                        Fragment fragmentchats = new ChatFragment();
                        ftm.replace(R.id.frame, fragmentchats);
                        ftm.addToBackStack(null);

                        ftm.commit();
                        return true;
                    case R.id.profile_button:
//                    mTextMessage.setText(R.string.title_profile);
                        FragmentManager fmn = getSupportFragmentManager();
                        FragmentTransaction ftn = fmn.beginTransaction();
                        Fragment fragmentprofile = new ProfileFragment();
                        ftn.replace(R.id.frame, fragmentprofile);
                        ftn.addToBackStack(null);

                        ftn.commit();
                        return true;
                    case R.id.setting_button:
//                    mTextMessage.setText(R.string.title_profile);
                        FragmentManager fms = getSupportFragmentManager();
                        FragmentTransaction fts = fms.beginTransaction();
                        Fragment fragmentset = new Fragment_setting();
                        fts.replace(R.id.frame, fragmentset);
                        fts.addToBackStack(null);

                        fts.commit();
                        return true;


                }

            }
            return false;


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        mTextMessage = (TextView) findViewById(R.id.message);
        FragmentManager fmh = getSupportFragmentManager();
        FragmentTransaction fth = fmh.beginTransaction();

        if (bd != null) {
            Host = bd.get("hostt").toString();
            if (Host.matches("12")) {
                setContentView(R.layout.activity_second);
                navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                Fragment fragmenth = new Fragment_post();
                fth.replace(R.id.frame, fragmenth);
                fth.commit();
                first = 2;
                navigation.setSelectedItemId(R.id.post);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }
            else if (Host.matches("14")) {
                setContentView(R.layout.activity_first);
                navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                navigation.setSelectedItemId(R.id.Home_button);
                Fragment fragmenth = new HomeFragment();
                fth.replace(R.id.frame, fragmenth);
                fth.commit();
                first = 1;
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }

            else if (Host.matches("profile_fragment")) { //to go to the guest profile
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                Integer name = prefs.getInt("First", 1);//"No name defined" is the default value.
                if (name % 2 != 0) {
                    setContentView(R.layout.activity_first);
                    navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                    first = 1;
                    Fragment fragmenth = new ProfileFragment();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    navigation.setSelectedItemId(R.id.profile_button);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                }
                else { // to go to the Host profile
                    setContentView(R.layout.activity_second);
                    navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                    first = 2;
                    Fragment fragmenth = new ProfileFragment();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    navigation.setSelectedItemId(R.id.profile_button);

                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                }

            }
            else if (Host.matches("account")) { //to go to the guest profile
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                Integer name = prefs.getInt("First", 1);//"No name defined" is the default value.
                if (name % 2 != 0) {
                    setContentView(R.layout.activity_first);
                    navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                    first = 1;
                    Fragment fragmenth = new Fragment_setting();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    navigation.setSelectedItemId(R.id.setting_button);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                }
                else { // to go to the Host profile
                    setContentView(R.layout.activity_second);
                    navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                    first = 2;
                    Fragment fragmenth = new Fragment_setting();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    navigation.setSelectedItemId(R.id.setting_button);

                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                }

            }
            else if (Host.matches("my_post")) {//to go to the guest profile
                    setContentView(R.layout.activity_second);
                    navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
                    first = 2;
                    Fragment fragmenth = new Fragment_mypost();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    navigation.setSelectedItemId(R.id.my_posts);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            }

        }
            else {

            setContentView(R.layout.activity_first);
            navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
            Fragment fragmenth = new HomeFragment();
            fth.replace(R.id.frame, fragmenth);
            fth.commit();
            first = 1;
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        }

      //  setvisibilty();

    }

    private static void setvisibilty() {

        navigation.setVisibility(View.GONE);
    }
}
