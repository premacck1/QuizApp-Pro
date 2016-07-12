package com.dikcoder.prem.quizapp;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bookmarks extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bookmarks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_help:
                Dialog d1 = new Dialog(this);
                d1.setContentView(R.layout.help);
                d1.setTitle("Help");
                d1.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ExpandableListAdapter listAdapter;
        ExpandableListView expandableListView;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;
        DatabaseHolder dbHandler;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);

            expandableListView = (ExpandableListView) rootView.findViewById(R.id.bookmarks_expandableListView);
            prepareListData();
            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
            expandableListView.setAdapter(listAdapter);
            expandableListView.setLayoutAnimation(
                    new LayoutAnimationController(
                            AnimationUtils.loadAnimation(getContext(), R.anim.float_in_expandable_listview)
                            , 0.2F
                    )
            );


            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        private void prepareListData() {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();

            dbHandler = new DatabaseHolder(getContext());
            dbHandler.open();
            Cursor bookmarkedQuestions = dbHandler.returnBookmarkedQuestion(getField());
            bookmarkedQuestions.moveToNext();

            int location = 0;

//            POPULATING THE expandableListView
            while (!bookmarkedQuestions.isAfterLast()) {
                // Adding header data
                listDataHeader.add(bookmarkedQuestions.getString(bookmarkedQuestions.getColumnIndex("question")));

                // Adding child data
                List<String> q1 = new ArrayList<String>();
                q1.add(bookmarkedQuestions.getString(bookmarkedQuestions.getColumnIndex("answer")));

                listDataChild.put(listDataHeader.get(location), q1); // Header, Child data
                location++;
                bookmarkedQuestions.moveToNext();
            }
            dbHandler.close();
        }
        public String getField(){
            String field = null;
            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    field = "iOS";
                    break;
                case 2:
                    field = "Java";
                    break;
                case 3:
                    field = "HTML";
                    break;
                case 4:
                    field = "Javascript";
                    break;
                default:
                    break;
            }
            return field;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "iOS";
                case 1:
                    return "Java";
                case 2:
                    return "HTML";
                case 3:
                    return "JavaScript";
            }
            return null;
        }
    }
}
