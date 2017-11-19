package com.example.erfan.bitumen_quality;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.erfan.bitumen_quality.DAO.HerstellerDAO;
import com.example.erfan.bitumen_quality.DAO.LieferungDAO;
import com.example.erfan.bitumen_quality.DAO.ProbeDAO;
import com.example.erfan.bitumen_quality.DAO.SorteDAO;
import com.example.erfan.bitumen_quality.DB.Hersteller;
import com.example.erfan.bitumen_quality.DB.Lieferung;
import com.example.erfan.bitumen_quality.DB.Sorte;

import java.sql.Date;
import java.util.List;

public class BitumenDatenbankActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static TabHost host;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitumen_datenbank);
        Log.d("MainActivity", "activity_bitumen_datenbank loded");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bitumen_datenban, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private SorteDAO dataSource6;
        private HerstellerDAO dataHersteller;
        private LieferungDAO dataLieferung;



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
            View rootView = inflater.inflate(R.layout.resived_sample_tabhost, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            host = (TabHost) rootView.findViewById(R.id.tabHostResivedSample);
            host.setup();

            Log.d("MainActivity", "set tabs");


            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec("Delivery");
            spec.setContent(R.id.Delivery);
            spec.setIndicator("Delivery");
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec("Sort");
            spec.setContent(R.id.Sort);
            spec.setIndicator("Sort");
            host.addTab(spec);

            //Tab 3
            spec = host.newTabSpec("Provider");
            spec.setContent(R.id.Provider);
            spec.setIndicator("Provider");
            host.addTab(spec);

            dataSource6 = new SorteDAO(getContext());
            dataSource6.open();
            showAllListEntriesSorte(rootView);
            dataSource6.close();


            dataHersteller = new HerstellerDAO(getContext());
            dataHersteller.open();
            showAllListEntriesHersteller(rootView);
            dataHersteller.close();


            dataLieferung = new LieferungDAO(getContext());
            dataLieferung.open();
            showAllListEntriesLieferung(rootView);
            dataLieferung.close();


            super.onCreate(savedInstanceState);
            Log.d("MainActivity", "activity_bitumen2: Measure");

            return rootView;
        }

        private void showAllListEntriesSorte (View rootview) {
            List<Sorte> memoList = dataSource6.getAllSorte();

            ArrayAdapter<Sorte> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                    rootview.getContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    memoList);

            //LinearLayout layout = (LinearLayout) findViewById(R.id.sorteLinearLayout);
            ListView memosListView = (ListView) rootview.findViewById(R.id.listview_Sorte);
            memosListView.setAdapter(shoppingMemoArrayAdapter);

        }

        private void showAllListEntriesHersteller (View rootview) {
            List<Hersteller> memoList = dataHersteller.getAllHersteller();

            ArrayAdapter<Hersteller> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                    rootview.getContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    memoList);

            //LinearLayout layout = (LinearLayout) findViewById(R.id.sorteLinearLayout);
            ListView memosListView = (ListView) rootview.findViewById(R.id.listview_Provider);
            memosListView.setAdapter(shoppingMemoArrayAdapter);

        }


        private void showAllListEntriesLieferung (View rootview) {
            List<Lieferung> memoList = dataLieferung.getAllLieferung();

            ArrayAdapter<Lieferung> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                    rootview.getContext(),
                    android.R.layout.simple_list_item_multiple_choice,
                    memoList);

            //LinearLayout layout = (LinearLayout) findViewById(R.id.sorteLinearLayout);
            ListView memosListView = (ListView) rootview.findViewById(R.id.listview_FaktorySampel);
            memosListView.setAdapter(shoppingMemoArrayAdapter);

        }

        private void activateAddButton(View rootview) {

            /*
                Delivery Save
             */

            Button buttonAddDelivery = (Button) rootview.findViewById(R.id.deliverdFaktorySave);
            final EditText editTextInfo = (EditText) rootview.findViewById(R.id.BescheibungLieferung);
            final EditText editTextName = (EditText) rootview.findViewById(R.id.BezeichnungLieferung);
            final EditText editTextDate = (EditText) rootview.findViewById(R.id.DateLieferung);
            final Spinner editTextLSorte = (Spinner) rootview.findViewById(R.id.lieferungSorteSpinner);
            final Spinner editTextLHersteller = (Spinner) rootview.findViewById(R.id.lieferungFirmaSpinner);


/*
            //todo save butten Programmieren
            buttonAddDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String info = editTextInfo.getText().toString();
                    String name = editTextName.getText().toString();
                    String date = editTextDate.getText().toString();
                    String lieferungSorte = editTextLSorte.getSelectedItem().toString();

                    if(TextUtils.isEmpty(info)) {
                        editTextInfo.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(name)) {
                        editTextName.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(date)) {
                        editTextDate.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(lieferungSorte)) {
                        //todo error
                        return;
                    }

                    long herstllerId, Date date, String bezeichnung, String beschreibung) {
                        ContentValues values = new ContentValues()

                    dataLieferung.createLieferung(
                            dataHersteller.getAllHersteller(editTextLHersteller).get(0).getId() , Date.valueOf(date) ,name, info);


                    InputMethodManager inputMethodManager;
                    inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    showAllListEntriesSampel();
                }
            });*/

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
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }

}
