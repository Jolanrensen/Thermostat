package nl.tue.student.thermostat;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import org.thermostatapp.util.HeatingSystem;

import java.util.Timer;
import java.util.TimerTask;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,NavigationView.OnNavigationItemSelectedListener {


    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    Switch manualSwitch;
    NavigationView navigationView;

    String getParam = "";

    TimerTask uiUpdateTask;
    TimerTask secondaryThreadTask;

    Thread secondaryThread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Homepage"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(this);

        //adding header of hamburgermenu
        final View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);
        //Adding the manual switch
        manualSwitch = (Switch) headerLayout.findViewById(R.id.manualSwitch);


        manualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manualSwitch.isChecked()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HeatingSystem.put("weekProgramState", "off");
                            } catch (Exception e) {
                                System.err.println("Error from getdata "+e);
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HeatingSystem.put("weekProgramState", "on");
                            } catch (Exception e) {
                                System.err.println("Error from getdata "+e);
                            }
                        }
                    }).start();
                }
            }
        });




        secondaryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getParam = HeatingSystem.get("weekProgramState");
                } catch (Exception e) {
                    System.err.println("Error from getdata "+e);
                }
            }
        });
        secondaryThreadTask = new TimerTask() {
            @Override
            public void run() {
                secondaryThread.start();
            }
        };
        Timer timer2 = new Timer();
        timer2.schedule(secondaryThreadTask, 0, 100);


        uiUpdateTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getParam.equals("on")) {
                            manualSwitch.setChecked(false);
                        } else if (getParam.equals("off")){
                            manualSwitch.setChecked(true);
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(uiUpdateTask, 0, 100);



    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                //do something helpful
                Toast toast = Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_first_fragment) {

        } else if (id == R.id.nav_second_fragment) {

        } else if (id == R.id.nav_third_fragment) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewPager.getCurrentItem() == 1) { //move back to homepage
            viewPager.setCurrentItem(0);
        }else {
            super.onBackPressed();
        }
    }


}
