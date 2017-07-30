package shanks.com.fitness.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Logger;
import shanks.com.fitness.Utils.Session;

public class MainDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout linear_profile,linear_diet_chart,linear_steps,linear_meal_counter
            ,linear_calorie_burn;
    Session session;

    private void init(){
        session = Session.getSession(MainDashboard.this);
        Logger.log("user id:::"+session.getUserId());

       /* linear_profile = (LinearLayout)findViewById(R.id.linear_profile);
        linear_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,Profile.class));
            }
        }); */

        linear_diet_chart = (LinearLayout)findViewById(R.id.linear_diet_chart);
        linear_diet_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,DietType.class));
            }
        });

        linear_steps = (LinearLayout)findViewById(R.id.linear_steps);
        linear_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,Steps.class));
            }
        });


        linear_meal_counter = (LinearLayout)findViewById(R.id.linear_meal_counter);
        linear_meal_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,MealTypeScheduler.class));
            }
        });
        /*
        linear_calorie_burn = (LinearLayout)findViewById(R.id.linear_calorie_burn);
        linear_calorie_burn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDashboard.this,CalorieBurn.class));
            }
        }); */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        synchronized (this){
            init();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        TextView name = (TextView)header.findViewById(R.id.name);
        TextView email = (TextView)header.findViewById(R.id.email_id);

        name.setText(session.getUsername());
        email.setText(session.getUserEmail());


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(MainDashboard.this,Profile.class));
        } else if (id == R.id.nav_diet) {
            startActivity(new Intent(MainDashboard.this,DietType.class));
        } else if (id == R.id.nav_steps) {
            startActivity(new Intent(MainDashboard.this,Steps.class));
        } else if (id == R.id.nav_meal) {
            startActivity(new Intent(MainDashboard.this,MealTypeScheduler.class));
        } /*else if (id == R.id.nav_calorie) {
            startActivity(new Intent(MainDashboard.this,CalorieBurn.class));
        } */else if (id == R.id.nav_logout) {
            session.ClearSession(MainDashboard.this);
            startActivity(new Intent(MainDashboard.this,Login.class));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
