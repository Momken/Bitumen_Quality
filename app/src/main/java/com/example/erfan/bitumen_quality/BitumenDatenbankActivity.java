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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erfan.bitumen_quality.DAO.HerstellerDAO;
import com.example.erfan.bitumen_quality.DAO.LieferungDAO;
import com.example.erfan.bitumen_quality.DAO.ProbeDAO;
import com.example.erfan.bitumen_quality.DAO.SorteDAO;
import com.example.erfan.bitumen_quality.DB.Hersteller;
import com.example.erfan.bitumen_quality.DB.Lieferung;
import com.example.erfan.bitumen_quality.DB.Sorte;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
        getMenuInflater().inflate(R.menu.menu_bitumen_datenbank, menu);
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
            spec.setIndicator("Deliverd Items");
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec("Sort");
            spec.setContent(R.id.Sort);
            spec.setIndicator("Sort Type");
            host.addTab(spec);

            //Tab 3
            spec = host.newTabSpec("Provider");
            spec.setContent(R.id.Provider);
            spec.setIndicator("Provider");
            host.addTab(spec);


            showAllListEntries(rootView);
            makeSpinner(rootView);
            activateAddButton(rootView);

            initializeContextualActionBarSorte(rootView);
            initializeContextualActionBarHersteller(rootView);
            initializeContextualActionBarLieferung(rootView);

            super.onCreate(savedInstanceState);
            Log.d("MainActivity", "activity_bitumen2: Measure");

            return rootView;
        }

        private void showAllListEntries(View rootView){

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
        }

        private void makeSpinner(View rootView) {

            Spinner My_spinner = (Spinner) rootView.findViewById(R.id.lieferungFirmaSpinner);
            ArrayAdapter<String> my_Adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, getTableValuesHersteller());

            My_spinner.setAdapter(my_Adapter);


            Spinner My_spinner_Sample = (Spinner) rootView.findViewById(R.id.spinnerHerstellerSorte);
            ArrayAdapter<String> my_Adapter_Sample = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_item, getTableValuesSorte());

            My_spinner_Sample.setAdapter(my_Adapter_Sample);



        }
        public ArrayList<String> getTableValuesHersteller() {
            ArrayList<String> my_array = new ArrayList<String>();
            try {
                dataHersteller.open();

                List<Hersteller> list =  dataHersteller.getAllHersteller();


                for(int i = 0; i<list.size() ;i++){
                    dataSource6.open();
                    String SorteName = "";
                    List<Sorte> lists = dataSource6.getAllSorte();
                    for (int j =  0 ; j < lists.size(); j++){
                        if(lists.get(j).getId() == list.get(i).getSortenId()){
                            SorteName = lists.get(j).getBezeichnung();
                            break;
                        }
                    }
                        String NAME = list.get(i).getName() + " " + SorteName;
                        my_array.add(NAME);
                }

                    dataSource6.close();

                dataHersteller.close();

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error encountered.",
                        Toast.LENGTH_LONG);
            }
            return my_array;
        }
        public ArrayList<String> getTableValuesSorte() {
            ArrayList<String> my_array = new ArrayList<String>();
            try {
                dataSource6.open();

                List<Sorte> list =  dataSource6.getAllSorte();

                for(int i = 0; i<list.size() ;i++){
                    String NAME = list.get(i).getBezeichnung();
                    my_array.add(NAME);
                }
                dataSource6.close();

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error encountered.",
                        Toast.LENGTH_LONG);
            }
            return my_array;
        }



        private void initializeContextualActionBarSorte(final View rootview) {

            final ListView listView = (ListView) rootview.findViewById(R.id.listview_Sorte);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    AppCompatActivity a = (AppCompatActivity)getActivity();
                    a.getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.cab_delete:
                            SparseBooleanArray touchedShoppingMemosPositions = listView.getCheckedItemPositions();
                            for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                                boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                                if(isChecked) {
                                    int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                    HashMap<Integer, Object> temp = (HashMap<Integer, Object>) listView.getItemAtPosition(postitionInListView);
                                    dataSource6.open();
                                    dataSource6.deleteSorte(dataSource6.getAllSorte().get(postitionInListView));
                                    dataSource6.close();

                                }
                            }
                            showAllListEntries(rootview);
                            mode.finish();
                            return true;

                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        private void initializeContextualActionBarHersteller(final View rootview) {

            final ListView listView = (ListView) rootview.findViewById(R.id.listview_Provider);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    AppCompatActivity a = (AppCompatActivity)getActivity();
                    a.getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.cab_delete:
                            SparseBooleanArray touchedShoppingMemosPositions = listView.getCheckedItemPositions();
                            for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                                boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                                if(isChecked) {
                                    int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                    HashMap<Integer, Object> temp = (HashMap<Integer, Object>) listView.getItemAtPosition(postitionInListView);
                                    dataHersteller.open();
                                    dataHersteller.deleteHersteller(dataHersteller.getAllHersteller().get(postitionInListView));
                                    dataHersteller.close();

                                }
                            }
                            showAllListEntries(rootview);
                            mode.finish();
                            return true;

                        default:
                            return false;
                    }
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

                private void initializeContextualActionBarLieferung(final View rootview) {

                    final ListView listView = (ListView) rootview.findViewById(R.id.listview_FaktorySampel);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

                    listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                        @Override
                        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                        }

                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            AppCompatActivity a = (AppCompatActivity)getActivity();
                            a.getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.cab_delete:
                                    SparseBooleanArray touchedShoppingMemosPositions = listView.getCheckedItemPositions();
                                    for (int i=0; i < touchedShoppingMemosPositions.size(); i++) {
                                        boolean isChecked = touchedShoppingMemosPositions.valueAt(i);
                                        if(isChecked) {
                                            int postitionInListView = touchedShoppingMemosPositions.keyAt(i);
                                            HashMap<Integer, Object> temp = (HashMap<Integer, Object>) listView.getItemAtPosition(postitionInListView);
                                            dataLieferung.open();
                                            dataLieferung.deleteLieferung(dataLieferung.getAllLieferung().get(postitionInListView));
                                            dataLieferung.close();

                                        }
                                    }
                                    showAllListEntries(rootview);
                                    mode.finish();
                                    return true;

                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                    });
                }




        private void showAllListEntriesSorte (View rootview) {

            List<Sorte> memoList = dataSource6.getAllSorte();

            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            for (int i = 0 ; i < memoList.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();
                Sorte sorte = memoList.get(i);

                map.put("FIRST_COLUMN",  ""+ sorte.getBezeichnung() );
                map.put("SECOND_COLUMN"," " );
                map.put("THIRD_COLUMN"," "+  sorte.getBeschreibung());
                map.put("FOURTH_COLUMN"," " );
                mylist.add(map);
            }


            Log.d("MainActivity", "count mylist:"+ mylist.size());
            //TODO make an base adapter

            ListView memosListView =  (ListView) rootview.findViewById(R.id.listview_Sorte);
            ListViewAdapter adapter= new ListViewAdapter(getActivity(), mylist);
            memosListView.setAdapter(adapter);

        }

        private void showAllListEntriesHersteller (View rootview) {

            List<Hersteller> memoList = dataHersteller.getAllHersteller();

            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            dataSource6.open();
            List<Sorte> list =  dataSource6.getAllSorte();
            dataSource6.close();
            String SorteName="";
            for (int i = 0 ; i < memoList.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();
                Hersteller hersteller = memoList.get(i);

                for (int j =  0 ; j < list.size(); j++){
                    if(list.get(j).getId() == hersteller.getSortenId()){
                        SorteName = list.get(j).getBezeichnung();
                        break;
                    }
                }
                map.put("FIRST_COLUMN",  hersteller.getName() );
                map.put("SECOND_COLUMN"," "+ SorteName );
                map.put("THIRD_COLUMN"," "+ hersteller.getBeschreibung() );
                map.put("FOURTH_COLUMN"," " );
                mylist.add(map);
            }

            //TODO make an base adapter

            ListView memosListView =  (ListView) rootview.findViewById(R.id.listview_Provider);
            ListViewAdapter adapter= new ListViewAdapter(getActivity(), mylist);
            memosListView.setAdapter(adapter);

        }



        private void showAllListEntriesLieferung (View rootview) {

            List<Lieferung> memoList = dataLieferung.getAllLieferung();

            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            dataHersteller.open();
            List<Hersteller> list =  dataHersteller.getAllHersteller();
            dataHersteller.close();
            String herstellerName="";
            for (int i = 0 ; i < memoList.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();
                Lieferung lieferung = memoList.get(i);

                for (int j =  0 ; j < list.size(); j++){
                    if(list.get(j).getId() == lieferung.getHerstllerId()){
                        herstellerName = list.get(j).getName();
                        break;
                    }
                }
                map.put("FIRST_COLUMN",  lieferung.getBezeichnung() );
                map.put("SECOND_COLUMN"," "+ herstellerName );
                map.put("THIRD_COLUMN"," "+ lieferung.getBeschreibung() );
                map.put("FOURTH_COLUMN"," "+ lieferung.getDate());
                mylist.add(map);
            }

            //TODO make an base adapter

            ListView memosListView =  (ListView) rootview.findViewById(R.id.listview_FaktorySampel);
            ListViewAdapter adapter= new ListViewAdapter(getActivity(), mylist);
            memosListView.setAdapter(adapter);

        }



        private void activateAddButton(final View rootview) {

            /*
            Delivery Save
             */

            Button buttonAddDelivery = (Button) rootview.findViewById(R.id.deliverdFaktorySave);
            final EditText editTextInfo = (EditText) rootview.findViewById(R.id.BescheibungLieferung);
            final EditText editTextName = (EditText) rootview.findViewById(R.id.BezeichnungLieferung);
            final EditText editTextDate = (EditText) rootview.findViewById(R.id.DateLieferung);
            final Spinner editTextLHersteller = (Spinner) rootview.findViewById(R.id.lieferungFirmaSpinner);



            buttonAddDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String info = editTextInfo.getText().toString();
                    String name = editTextName.getText().toString();
                    String date = editTextDate.getText().toString();
                    String lieferungHersteller = editTextLHersteller.getSelectedItem().toString();
                    String[] segs = lieferungHersteller.split("(\\s|\\p{Punct})+");


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
                    if(TextUtils.isEmpty(lieferungHersteller)) {
                        //todo error
                        return;
                    }

                    dataHersteller.open();
                    dataLieferung.open();

                    Long id = dataHersteller.getAllHersteller(segs[0]).get(0).getId();
                    Date dTemp = Date.valueOf(date);

Log.d("MainActivity", "onClick: date:"+ dTemp.toString());
                    dataLieferung.createLieferung(id, dTemp, name, segs[1]+" "+info);
                    dataLieferung.close();
                    dataHersteller.close();

                    InputMethodManager inputMethodManager;
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }

                    showAllListEntries(rootview);
                    makeSpinner(rootview);
                }
            });

            /*

                    Provider save
             */


            Button buttonAddProvider = (Button) rootview.findViewById(R.id.ProviderSave);
            final EditText editTextPInfo = (EditText) rootview.findViewById(R.id.providerInfo);
            final EditText editTextPName = (EditText) rootview.findViewById(R.id.providerName);
            final Spinner editTextPSorte = (Spinner) rootview.findViewById(R.id.spinnerHerstellerSorte);



            buttonAddProvider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String info = editTextPInfo.getText().toString();
                    String name = editTextPName.getText().toString();
                    String herstellerSorte = editTextPSorte.getSelectedItem().toString();


                    if(TextUtils.isEmpty(info)) {
                        editTextPInfo.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(name)) {
                        editTextPName.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(herstellerSorte)) {
                        //todo error
                        return;
                    }

                    dataSource6.open();
                    Long id = dataSource6.getAllSorte(herstellerSorte).get(0).getId();
                    dataSource6.close();
                    dataHersteller.open();
                    dataHersteller.createHersteller(id, name, info);
                    dataHersteller.close();

                    InputMethodManager inputMethodManager;
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }

                    showAllListEntries(rootview);
                    makeSpinner(rootview);
                }
            });


            Button buttonAddSort = (Button) rootview.findViewById(R.id.SorteSave);
            final EditText editTextSInfo = (EditText) rootview.findViewById(R.id.sorteBeschreibung);
            final EditText editTextSName = (EditText) rootview.findViewById(R.id.sorteAlgemeineBezeichnung);


            buttonAddSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String info = editTextSInfo.getText().toString();
                    String name = editTextSName.getText().toString();


                    if(TextUtils.isEmpty(info)) {
                        editTextInfo.setError(getString(R.string.output_errorMessage));
                        return;
                    }
                    if(TextUtils.isEmpty(name)) {
                        editTextName.setError(getString(R.string.output_errorMessage));
                        return;
                    }

                    dataSource6.open();

                    dataSource6.createSorte(name, info);
                    dataSource6.close();

                    InputMethodManager inputMethodManager;
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    if(getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }

                    showAllListEntries(rootview);
                    makeSpinner(rootview);
                }
            });

            
            
            
            
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
