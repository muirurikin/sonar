package chitchat.com.chitchat.views.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import chitchat.com.chitchat.R;
import chitchat.com.chitchat.utils.ActivityUtils;
import chitchat.com.chitchat.views.LoginActivity;
import chitchat.com.chitchat.views.rooms.RoomsFragment;

public class MainActivity extends AppCompatActivity{
    private static final String MAINACTIVITY_TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ImageView userImage;
    private TextView username, userEmail;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.mainact_fab);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // set up the navigation drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.mainactivity_drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimary);
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        if(navigationView != null){
            setUpDrawerContents(navigationView);
            userImage = (ImageView)navigationView.findViewById(R.id.userimg_nav_img);
            userEmail = (TextView)navigationView.findViewById(R.id.useremail_nav_id);
            username = (TextView)navigationView.findViewById(R.id.username_nav_id);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                    //User is signed in, get credentials and set them to UI views
                    String userData = user.getDisplayName() + " "+ user.getEmail()+ " "+ user.getUid() + " " + user.getPhotoUrl();
                    Log.d(MAINACTIVITY_TAG, "AuthStateChanged:SignedIn" +userData);
/*                    userEmail.setText((user.getEmail() != null) ? user.getEmail() : " ");
                    username.setText(user.getDisplayName());

                    // if photo url is not null, load it into the Image with Glide
                    if(user.getPhotoUrl() != null){
                        Glide.with(MainActivity.this)
                                .load(user.getPhotoUrl())
                                .into(userImage);
                    }else{
                        userImage.setImageDrawable(
                                ContextCompat.getDrawable(MainActivity.this,
                                        R.drawable.ic_profile));
                    }*/
                }
            }
        };


        //create the fragment
        RoomsFragment roomsFragment = (RoomsFragment) getSupportFragmentManager().findFragmentById(R.id.mainactivity_contentFrame);
        if (roomsFragment ==null){
            roomsFragment = RoomsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), roomsFragment, R.id.mainactivity_contentFrame);
        }
    }

    /**Sets up the contents of the DrawerLayout*/
    private void setUpDrawerContents(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile_menu_item:
                                //open profile screen
                                break;
                            case R.id.people_menu_item:
                                //open people screen
                                break;

                            case R.id.settings_menu_item:
                                //open settings screen
                                break;
                            case R.id.sign_out_menu_item:
                                //sign out the user with firebase
                                FirebaseAuth.getInstance().signOut();
                                //return user to login screen
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                break;
                            default:
                                break;
                        }
                        //close the navigation drawer when an item is clicked
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
                //TODO: open settings
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    /**START THIS PARTICULAR ACTIVITY*/
    public static void start(Context c) {
        c.startActivity(new Intent(c, MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
