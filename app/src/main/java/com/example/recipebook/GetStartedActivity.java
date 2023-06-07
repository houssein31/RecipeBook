package com.example.recipebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recipebook.DAOs.UserDAO;
import com.example.recipebook.Entities.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class GetStartedActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton facebook_icon, google_icon, twitter_icon;
    float v=0;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleButton;

    public static UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Initialize userDAO
        userDAO = DatabaseConnect.getDBInstance(this).getUserDAO();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        facebook_icon = findViewById(R.id.fab_facebook);
        google_icon = findViewById(R.id.fab_google);
        twitter_icon = findViewById(R.id.fab_twitter);

        googleButton = google_icon;

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        facebook_icon.setTranslationY(300);
        google_icon.setTranslationY(300);
        twitter_icon.setTranslationY(300);
        tabLayout.setTranslationY(300);


        facebook_icon.setAlpha(v);
        google_icon.setAlpha(v);
        twitter_icon.setAlpha(v);
        tabLayout.setAlpha(v);

        facebook_icon.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google_icon.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twitter_icon.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed for this implementation
            }
        });
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            String personName = acct.getDisplayName();
            String personEmail= acct.getEmail();

//            Toast.makeText(this, String.valueOf(userDAO.userExists(personEmail)), Toast.LENGTH_SHORT).show();

            if(!userDAO.userExists(personEmail)) {
                User newUser = new User(personName, personEmail, null);
                Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();

                userDAO.insertUser(newUser);
            }

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

     void navigateToSecondActivity() {
        Intent intent = new Intent(GetStartedActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
