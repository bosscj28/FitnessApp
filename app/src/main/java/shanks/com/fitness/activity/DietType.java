package shanks.com.fitness.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import shanks.com.fitness.Interfaces.Communicator;
import shanks.com.fitness.R;
import shanks.com.fitness.Utils.Utils;

public class DietType extends AppCompatActivity implements Communicator {

    Button break_fast,lunch,dinner;
    CustomViewPager viewPager;

    TabLayout tabLayout;
    String dataforADDFood = "0";

    @Override public void respond(int data) {

        dataforADDFood = String.valueOf(data);
        init();
        /*AddFoodFragment frag = new AddFoodFragment();
        frag.change(data,DietType.this);*/
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_type_scheduler);
        init();
    }

    private void init() {
        viewPager = (CustomViewPager) findViewById(R.id.viewpagermain);
        setupViewPager(viewPager);
        viewPager.disableScroll(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

    }

    private void setupViewPager(ViewPager viewPager) {
       /* ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MydietFragment(), "My Diet");
        adapter.addFragment(new AddFoodFragment(), "Add Food");
        viewPager.setAdapter(adapter);*/

        List<Fragment> fragments = getFragments();
        List<String> Title = getTitleArray();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments, Title);
        viewPager.setAdapter(adapter);
    }
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(MydietFragment.newInstance("nothing"));
        fList.add(AddFoodFragment.newInstance(dataforADDFood));

        return fList;
    }
    private List<String> getTitleArray() {
        List<String> fList = new ArrayList<String>();
        fList.add("My Meal");
        fList.add("Add Food");


        return fList;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> mFragmentTitleList;

        public ViewPagerAdapter(FragmentManager manager,List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
            super(manager);
            this.mFragmentList = mFragmentList;
            this.mFragmentTitleList = mFragmentTitleList;

        }

        @Override
        public Fragment getItem(int position) {return mFragmentList.get(position);}

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
