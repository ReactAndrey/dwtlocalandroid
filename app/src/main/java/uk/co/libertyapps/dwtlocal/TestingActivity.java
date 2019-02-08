package uk.co.libertyapps.dwtlocal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;

import java.util.ArrayList;
import java.util.Date;

public class TestingActivity extends AppCompatActivity {
    static ArrayList<String> SPINNER = new ArrayList<>();
    static ArrayList<String> PARAMETERS = new ArrayList<>();
    static ImageView guidance;
    Spinner spinner;
    public static InfiniteViewPager pager;
    MyFragmentPagerAdapter pagerAdapter;
    InfiniteViewpagerAdapter adapter;

    static int minteger = 0;

    static int start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_test);

        pager = findViewById(R.id.view_pager);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("800g White");
        setTitle("800g White");

        minteger = getIntent().getIntExtra("minteger", 1);

        SPINNER = new ArrayList<String>() {{
            add("Code Clarity - Wax");
            add("Code Present - Wax");
            add("Flavour and Aroma");
            add("Left Seal - Wax");
            add("Right Seal - Wax");
            add("Code Position - Wax");
            add("Reg. Position - Wax");
            add("Base Seal - Wax");
            add("Weight (no packaging) Grams - 800g");
            add("Shape - White Bread");

        }};


        PARAMETERS = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("10");
            add("11");
        }};

        setupViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_shot:

                Date now = new Date();

                Intent intent = new Intent(this, FeedbackActivity.class);
                intent.putExtra("Groupshot", true);
                intent.putExtra("GroupFilter", "/groupshot" + now.getTime() + "/");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupViewPager() {
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        adapter = new InfiniteViewpagerAdapter(pagerAdapter);

        pager.setAdapter(adapter);

        //    pager.setOffscreenPageLimit(0);
        pager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == 0 && previousState == 2) {
                            fromPager = true;
                        }
                        previousState = state;
                    }
                }
        );
    }

    public class InfiniteViewpagerAdapter extends InfinitePagerAdapter {
        public InfiniteViewpagerAdapter(PagerAdapter adapter) {
            super(adapter);
        }

        @Override
        public int getCount() {
            return 30000;
        }
    }


    int previousState = 0;
    boolean fromPager = true;

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        int position; //DO NOT DELETE THIS LOADS FRAGMENT NUMBER
        int parameter = 0; // THIS FILTERS IMAGES
        Button addImageButton, presetButton;
        public Spinner spinner;

        int showingPosition;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance() {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            addImageButton = rootView.findViewById(R.id.addImageButton);
            presetButton = rootView.findViewById(R.id.presetButton);
            TextView counterLabel = rootView.findViewById(R.id.counterLabel);
            spinner = rootView.findViewById(R.id.spinner);

            counterLabel.setText("" + showingPosition);

            addImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), FeedbackActivity.class);
                    startActivity(intent);
                }
            });

            presetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PresetActivity.class);
                    startActivity(intent);
                }
            });

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootView.getContext(),
                    R.layout.spinner_item, SPINNER);

            spinner.setAdapter(dataAdapter);
            spinner.setSelection(position, false); // must be false

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Log.d("onItemSelected", "onItemSelected = " + i);
                    Log.d("onItemSelected", "pager.getCurrentItem() = " + pager.getCurrentItem());
                    int pick = (i + 1) / minteger;
                    Log.d("onItemSelected", "pick = " + pick);
                    Log.d("onItemSelected", "i = " + i);

                    if (pager.getCurrentItem() != pick) {
                        Log.d("onItemSelected", "UPDATE");
                        pager.setCurrentItem(pick);

                        spinner.setSelection(i, false);
                        parameter = i;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d("onNothingSelected", "onNothingSelected");
                }
            });

            guidance = rootView.findViewById(R.id.guidance);
            int guide = R.drawable.aaa_touch_fix;
            guidance.setVisibility(View.VISIBLE);

            parameter = Integer.parseInt(PARAMETERS.get(position));

            // Pick guidance image
            if (parameter > 0) {
                guide = getResources().getIdentifier("param_" + parameter, "drawable", getContext().getPackageName());
            }

            if (parameter == 4 || parameter == 5) {
                guide = R.drawable.param_4;
            }

            if (parameter == 97 || parameter == 98 || parameter == 99 || parameter == 100 || parameter == 101 || parameter == 102 || parameter == 103) {
                guide = R.drawable.param_97;
            }

            if (parameter == 132 || parameter == 134 || parameter == 136 || parameter == 138 || parameter == 140 || parameter == 142) {
                guide = R.drawable.param_133;
            }

            if (parameter == 133 || parameter == 135 || parameter == 137 || parameter == 139 || parameter == 141 || parameter == 143) {
                guide = R.drawable.param_132;
            }

            if (parameter > 123 && parameter < 128) {
                guide = R.drawable.param_128;
            }

            if (parameter > 127 && parameter < 132) {
                guide = R.drawable.param_124;
            }

            if (guide == R.drawable.aaa_touch_fix) {
                guidance.getLayoutParams().height = 1;
            }

            if (guide > 0) {
                guidance.setImageDrawable(getResources().getDrawable(guide));
            }

            return rootView;
        }

        @Override
        public void onPause() {
            super.onPause();
        }

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = PlaceholderFragment.newInstance();

            int real = position / minteger;
            int showing = (position % minteger) + 1;

            //needn't , remove it!!
            start = (position % minteger) + 1;


            // Log.d("getItem start", "" + start);
            // Log.d("getItem real", "" + real);
            ((PlaceholderFragment) fragment).position = real;
            ((PlaceholderFragment) fragment).showingPosition = showing;
            return fragment;
        }

        @Override
        public int getCount() {
            int val = minteger * SPINNER.size();
            // Log.d("get count", "" + val);
            return val;
        }
    }

}