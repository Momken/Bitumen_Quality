package com.example.erfan.bitumen_quality;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;

import java.nio.ByteBuffer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;

import com.example.erfan.bitumen_quality.DAO.AlterungszustandDAO;
import com.example.erfan.bitumen_quality.DAO.LieferungDAO;
import com.example.erfan.bitumen_quality.DAO.ProbeDAO;
import com.example.erfan.bitumen_quality.DAO.SorteDAO;
import com.example.erfan.bitumen_quality.DB.Alterungszustand;
import com.example.erfan.bitumen_quality.DB.Bitumen;
import com.example.erfan.bitumen_quality.DAO.BitumenDAO;
import com.example.erfan.bitumen_quality.DB.Lieferung;
import com.example.erfan.bitumen_quality.DB.Probe;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.util.SparseBooleanArray;
import android.view.ActionMode;


/**
 * Created by Erfan on 30.06.2017.
 */

public class MainActivity extends AppCompatActivity  implements Runnable{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private BitumenDAO dataSource;
    private SorteDAO dataSorte = new SorteDAO(this);
    private LieferungDAO dataLieferung = new LieferungDAO(this);
    private ProbeDAO dataProbe =  new ProbeDAO(this);;
    private AlterungszustandDAO dataAlterungszustand = new AlterungszustandDAO(this);
    UsbCommunicationManager usb = null;
    private WebView webView;
    RatingBar rateBitumen;
    TextView bitumenRateText;
    Float userRating;


    static final String ACTION_USB_PERMISSION = "com.example.erfan.bitumen_quality";

    UsbManager usbManager;
    UsbDevice usbDevice = null;
    UsbInterface usbCdcInterface = null;
    UsbInterface usbHidInterface = null;
    UsbEndpoint usbCdcRead = null;
    UsbEndpoint usbCdcWrite = null;
    UsbDeviceConnection usbCdcConnection;
    Thread readThread = null;
    volatile boolean readThreadRunning = true;
    PendingIntent permissionIntent;
    Context context;


    StringBuilder receiveddata;
    StringBuilder stringData;

    byte[] readBytes = new byte[256];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*********GUI 1
         * main_measure
         */

        /*********GUI 2
         * content_measure_lab
         */



        setContentView(R.layout.content_measure_lab);

        Bitumen testBitumen = new Bitumen(1,"70/100","schlecht");



        dataSource = new BitumenDAO(this);

        dataSource.open();
       // showAllListEntriesBitumen();


        //Bitumen shoppingMemo = dataSource.createBitumen("Testprodukt", 2);
        /*
        dataSorte = new SorteDAO(this);
        dataSorte.open();
        dataSorte.close();

        HerstellerDAO dataSource3 = new HerstellerDAO(this);
        dataSource3.open();
        Hersteller herstellerMemo = dataSource3.createHersteller(2, "ÖMV", "schlecht");
        Log.d(LOG_TAG, "dataSource3:");
        Log.d(LOG_TAG, "Hersteller: " + herstellerMemo.getId() + ", Inhalt: " + herstellerMemo.toString());
        dataSource3.close();
        Log.d(LOG_TAG,"*********************************************");

        LieferungDAO dataSource4 = new LieferungDAO(this);
        dataSource4.open();
        Lieferung lieferungMemo = dataSource4.createLieferung(1,new Date(2017,7,11), "Test","vortlaufend");
        Log.d(LOG_TAG, "dataSource4:");
        Log.d(LOG_TAG, "Lieferung: " + lieferungMemo.getId() + ", Inhalt: " + lieferungMemo.toString());
        dataSource4.close();
        Log.d(LOG_TAG,"*********************************************");
        */



        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();







        Log.d(LOG_TAG, "onCreate: Create USB");

        usbCommunicationManager(this);
        String usbStatus = connect();



        /*********GUI 1
         *

         makeTheWebView();
         makeBitumenrate();
         */

        /*********GUI 2
         *
         */
        makeTheTabs();



        setupSettings_Bitumen();




        makeSpinner();


        //ToDo: sauber alle 3 butten richten
        activateAddButton();
        showAllListEntriesSampel();
        showAllListEntriesAlterung();
        initializeContextualActionBarAlterung();
        initializeContextualActionBarProbe();
    }


    private void initializeContextualActionBarAlterung() {

        final ListView listView = (ListView) findViewById(R.id.listview_Bitumen_memos);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
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
                                Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + temp.toString()+ temp.size());
                                dataAlterungszustand.open();
                                dataAlterungszustand.deleteAlterungszustand(dataAlterungszustand.getAllAlterungzustand().get(postitionInListView));
                                dataAlterungszustand.close();

                            }
                        }
                        showAllListEntries();
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

    private void initializeContextualActionBarProbe() {

        final ListView listView = (ListView) findViewById(R.id.listview_Sample);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
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
                                Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + temp.toString()+ temp.size());
                                dataProbe.open();
                                dataProbe.deleteProbe(dataProbe.getAllProbe().get(postitionInListView));
                                dataProbe.close();

                            }
                        }
                        showAllListEntries();
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


    private void makeSpinner() {

        Spinner My_spinner = (Spinner) findViewById(R.id.Scann_SpinnerProbe);
        ArrayAdapter<String> my_Adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, getTableValuesProbe());

        My_spinner.setAdapter(my_Adapter);


        Spinner My_spinner_Sample = (Spinner) findViewById(R.id.spinner_SampleDeliverd);
        ArrayAdapter<String> my_Adapter_Sample = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, getTableValuesLieferung());

        My_spinner_Sample.setAdapter(my_Adapter_Sample);



    }

    private void makeBitumenrate() {
        bitumenRateText = (TextView) findViewById(R.id.textRatingbarB);

        rateBitumen = (RatingBar) findViewById(R.id.ratingBarBitumen);
        rateBitumen.setRating(1);
        rateBitumen.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = rateBitumen.getRating();

                if(userRating == 0){
                    ratingBar.setRating(1);
                }
                if(userRating == 1){
                    bitumenRateText.setText("Sehr schlecht (1 Stern)");
                }
                if(userRating == 2){
                    bitumenRateText.setText("Schlecht (2 Stern)");
                }

                if(userRating == 3){
                    bitumenRateText.setText("Genügend (3 Stern)");
                }
                if(userRating == 4){
                    bitumenRateText.setText("Gut (4 Stern)");
                }
                if(userRating == 5){
                    bitumenRateText.setText("Sehr Gut (5 Stern)");
                }

            }
        });
    }

    private void showAllListEntries(){
        showAllListEntriesSampel();
        showAllListEntriesAlterung();
    }

    private void showAllListEntriesBitumen () {
        List<Bitumen> shoppingMemoList = dataSource.getAllBitumens();

        ArrayAdapter<Bitumen> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingMemoList);

        ListView shoppingMemosListView = (ListView) findViewById(R.id.listview_Bitumen_memos);
        shoppingMemosListView.setAdapter(shoppingMemoArrayAdapter);
    }

    private void showAllListEntriesSampel () {

        dataProbe.open();
        List<Probe> memoList = dataProbe.getAllProbe();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        dataLieferung.open();
        List<Lieferung> list =  dataLieferung.getAllLieferung();
        dataLieferung.close();
        String lieferungName="";
        for (int i = 0 ; i < memoList.size(); i++){
            HashMap<String, String> map = new HashMap<String, String>();
            Probe probe = memoList.get(i);

            for (int j =  0 ; j < list.size(); j++){
                if(list.get(j).getId() == probe.getLieferungId()){
                    lieferungName = list.get(j).getBezeichnung();
                    break;
                }
            }
            map.put("FIRST_COLUMN",  lieferungName );
            map.put("SECOND_COLUMN"," "+ probe.getBezeichnung() );
            map.put("THIRD_COLUMN"," "+ probe.getDate() );
            map.put("FOURTH_COLUMN"," "+ probe.getBeschreibung());
            mylist.add(map);
        }

        Log.d(LOG_TAG," count mylist: : "+mylist.size());
        //TODO make an base adapter

        ListView memosListView =  (ListView) findViewById(R.id.listview_Sample);
        ListViewAdapter adapter= new ListViewAdapter(this, mylist);
        memosListView.setAdapter(adapter);

        dataProbe.close();
    }

    private void showAllListEntriesSampel2 () {

        dataProbe.open();
        List<Probe> memoList = dataProbe.getAllProbe();

        ArrayAdapter<Probe> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                memoList);

        ListView memosListView = (ListView) findViewById(R.id.listview_Sample);
        memosListView.setAdapter(shoppingMemoArrayAdapter);
        dataProbe.close();
    }



    private void showAllListEntriesAlterung2 () {
        dataAlterungszustand.open();
        List<Alterungszustand> memoList = dataAlterungszustand.getAllAlterungzustand();

        ArrayAdapter<Alterungszustand> shoppingMemoArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                memoList);

        ListView memosListView = (ListView) findViewById(R.id.listview_Bitumen_memos);
        memosListView.setAdapter(shoppingMemoArrayAdapter);
        dataAlterungszustand.close();
    }
    private void showAllListEntriesAlterung () {
        dataAlterungszustand.open();
        List<Alterungszustand> memoList = dataAlterungszustand.getAllAlterungzustand();

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

        for (int i = 0 ; i < memoList.size(); i++){
            HashMap<String, String> map = new HashMap<String, String>();
            Alterungszustand alterungszustand = memoList.get(i);
            map.put("FIRST_COLUMN",alterungszustand.getProbenId()+ "\t"+alterungszustand.getBezeichnung());
            map.put("SECOND_COLUMN"," "+alterungszustand.getDate());
            map.put("THIRD_COLUMN"," "+alterungszustand.getMessungsfaktoren());
            map.put("FOURTH_COLUMN"," "+alterungszustand.getMessung());
            mylist.add(map);
        }
        Log.d(LOG_TAG," count mylist: : "+mylist.size());
/*
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("FIRST_COLUMN","a");
        map.put("SECOND_COLUMN","b");
        map.put("THIRD_COLUMN","c");
        map.put("FOURTH_COLUMN","d");
        mylist.add(map);

*/
        //TODO make an base adapter
        ListView memosListView =  (ListView) findViewById(R.id.listview_Bitumen_memos);
        ListViewAdapter adapter= new ListViewAdapter(this, mylist);
        memosListView.setAdapter(adapter);
        dataAlterungszustand.close();

    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void activateAddButton() {
        /*
                Butten Save Sample
        */
                Button buttonAddProductSample = (Button) findViewById(R.id.sampleSave);
                final EditText editTextInfo = (EditText) findViewById(R.id.sampleInfo);
                final EditText editTextName = (EditText) findViewById(R.id.sampleName);
                final EditText editTextDate = (EditText) findViewById(R.id.Sample_Date);
                final Spinner editTextLieferung = (Spinner) findViewById(R.id.spinner_SampleDeliverd);



                buttonAddProductSample.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String info = editTextInfo.getText().toString();
                        String name = editTextName.getText().toString();
                        String date = editTextDate.getText().toString();
                        String lieferung = editTextLieferung.getSelectedItem().toString();

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
                        if(TextUtils.isEmpty(lieferung)) {
                            //todo error
                            return;
                        }


                        dataProbe.open();
                        dataLieferung.open();
                        Log.d(LOG_TAG, lieferung);
                        Log.d(LOG_TAG, dataLieferung.getAllLieferung(lieferung).toString());

                        Long id = dataLieferung.getAllLieferung(lieferung).get(0).getId();
                        Date dTemp = Date.valueOf(date);
                        dataProbe.createProbe(
                                id , dTemp ,name, info);
                        dataLieferung.close();
                        dataProbe.close();

                        InputMethodManager inputMethodManager;
                        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if(getCurrentFocus() != null) {
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }

                showAllListEntriesSampel();
            }
        });
        /*      ENDE
                Butten Save Sample
        */

        /*

                Butten Save Scan
         */
        Button buttonAddScann = (Button) findViewById(R.id.ScannSave);
        final EditText editTextScann_Info = (EditText) findViewById(R.id.Scann_Description);
        final EditText editTextScann_Name = (EditText) findViewById(R.id.Scann_Name);
        final EditText editTextScann_InternID = (EditText) findViewById(R.id.Scann_intern_number_id);
        final Spinner editTextSample = (Spinner) findViewById(R.id.Scann_SpinnerProbe);
        final TextView ed_messungsfaktoren = (TextView) findViewById(R.id.Info);
        final TextView ed_messung1 = (TextView) findViewById(R.id.textViewQ1_result);
        final TextView ed_messung2 = (TextView) findViewById(R.id.textViewQ2_result);

                buttonAddScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = editTextScann_Info.getText().toString();
                String name = editTextScann_Name.getText().toString();
                String internID = editTextScann_InternID.getText().toString();
                String sample ="1";// editTextSample.getSelectedItem().toString();


                //todo if empty Textview

                if(TextUtils.isEmpty(info)) {
                    editTextInfo.setError(getString(R.string.output_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(name)) {
                    editTextName.setError(getString(R.string.output_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(internID)) {
                    editTextScann_InternID.setError(getString(R.string.output_errorMessage));
                    return;
                }


                dataAlterungszustand.open();
                dataAlterungszustand.createAlterungszustand
                        (0, new java.util.Date(), internID+" "+name+" "+info, ed_messungsfaktoren.getText().toString(), ed_messung1.getText().toString()
                                + " " + ed_messung2.getText().toString()  );
                dataAlterungszustand.close();


                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                showAllListEntriesAlterung();
                Toast.makeText(context, "Scanned data Saved", Toast.LENGTH_LONG).show();

            }
        });



        /*
            Connect Button
         */
        Button buttonConnect = (Button) findViewById(R.id.Scann_ConnectButton);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSettings_USB();

            }
        });


    }


    public ArrayList<String> getTableValuesProbe() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            dataProbe.open();

            List<Probe> list =  dataProbe.getAllProbe();

                for(int i = 0; i<list.size() ;i++){
                        String NAME = list.get(i).getBezeichnung();
                        my_array.add(NAME);
                }
            dataProbe.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }

    public ArrayList<String> getTableValuesLieferung() {
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            dataLieferung.open();

            List<Lieferung> list =  dataLieferung.getAllLieferung();

            if(list.isEmpty()){
                list.add(new Lieferung(0,0,null,"Empty",null));

            }

            for(int i = 0; i<list.size() ;i++){
                String NAME = list.get(i).getBezeichnung();
                my_array.add(NAME);
            }

            dataLieferung.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error encountered.",
                    Toast.LENGTH_LONG);
        }
        return my_array;
    }



    private void setupSettings_USB() {

        Log.d(LOG_TAG, "setupSettings_USB: Is getting startet ");

        StringBuilder data = new StringBuilder();



        if(read(data).equals("starting usb listening thread")) {
            TextView info = (TextView) findViewById(R.id.Info);
            info.setText("Connected");
        }
           //  Toast.makeText(this, "setupSettings_USB: Data", Toast.LENGTH_SHORT).show();

      //  Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "setupSettings_USB_return to main activity: "+ data.toString());
    }




    private void setupSettings_Bitumen() {
        Button BitumenSettings = (Button) findViewById(R.id.action_Sample);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //made menu_measure
        getMenuInflater().inflate(R.menu.menu_measure, menu);
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
            setupSettings_USB();
            return true;
        }
        else if(id == R.id.action_Sample){
            Toast.makeText(this, "You have selected Details Menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), BitumenDatenbankActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void makeTheWebView(){

        webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        String customHtml = makeHTMLChart(10);
        webView.loadData(customHtml, "text/html", "UTF-8");

    }

    private String makeHTMLChart(int prozent){
        return  "<html>\n" +
                "<style>\n" +
                "    body > div {\n" +
                "   position: absolute;\n" +
                "    left: 50%;\n" +
                "    top: 50%;\n" +
                "    transform: translate(-50%,-50%);\n" +
                "            }\n" +
                " </style>\n" +
                "\n" +
                "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "<div id=\"gauge_div\" style=\"width:550px; height: 550px;\"></div>\n" +
                "\n" +
                "<script>\n" +
                "    google.charts.load('current', {'packages':['gauge']});\n" +
                "    google.charts.setOnLoadCallback(drawGauge);\n" +
                "\n" +
                "\n" +
                "var gaugeOptions = {min: 0, max: 100, yellowFrom: 37.5, yellowTo: 74,\n" +
                "redFrom: 0, redTo: 37.5, greenFrom: 74, greenTo:100,  minorTicks: 5};\n" +
                "var gauge;\n" +
                "\n" +
                "      function drawGauge() {\n" +
                "      gaugeData = new google.visualization.DataTable();\n" +
                "      gaugeData.addColumn('number', 'Quality %');\n" +
                "      gaugeData.addRows(1);\n" +
                "      gaugeData.setCell(0, 0, "+ prozent +");\n" +
                "\n" +
                "      gauge = new google.visualization.Gauge(document.getElementById('gauge_div'));\n" +
                "      gauge.draw(gaugeData, gaugeOptions);\n" +
                "    }\n" +
                "\n" +
                "    function changeTemp(dir) {\n" +
                "      gaugeData.setValue(0, 0, gaugeData.getValue(0, 0) + dir * 25);\n" +
                "      gaugeData.setValue(0, 1, gaugeData.getValue(0, 1) + dir * 20);\n" +
                "      gauge.draw(gaugeData, gaugeOptions);\n" +
                "    }\n" +
                "\n" +
                "</script>\n" +
                "\n" +
                "\n" +
                "</html>\n";
    }

    private void makeTheTabs(){
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Scan");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Scanned Data");
        host.addTab(spec);

/*
        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tranche");
        host.addTab(spec);
*/
        //Tab 4
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Sample Properties");
        host.addTab(spec);
    }






    /**
     *  USB communication */
    public void usbCommunicationManager(Context context)
    {
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        // ask permission from user to use the usb device
        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(usbReceiver, filter);
    }
    public String connect()
    {
        Log.d("MainActivity", "Connect: Start");
        // check if there's a connected usb device
        if(usbManager.getDeviceList().isEmpty())
        {
            Log.d("MainActivity", "No connected devices");
            return "No connected devices";
        }

        // get the first (only) connected device
        Toast.makeText(context, "USB connected", Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "i got an USB connected");
        usbDevice = usbManager.getDeviceList().values().iterator().next();
        //Log.d("MainActivity", usbDevice.toString());
   /*     runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView info = (TextView) findViewById(R.id.Result);
                info.setText("Connected");
            }
        });*/

        // user must approve of connection if not in the /res/usb_device_filter.xml file
        usbManager.requestPermission(usbDevice, permissionIntent);

        Log.d("MainActivity", "Connect: Ende");
        return "Scanning..";
    }

    public void stop()
    {
        usbDevice = null;
        usbCdcInterface = null;
        usbHidInterface = null;
        usbCdcRead = null;
        usbCdcWrite = null;

        context.unregisterReceiver(usbReceiver);
    }


    /*
        Wird wohl nicht verwendet ist aber zur sicherheit Programmiert
     */
    public String write(String data)
    {
        if(usbDevice == null)
        {
            return "no usb device selected";
        }

        int sentBytes = 0;
        if(!data.equals(""))
        {
            synchronized(this)
            {
                // send data to usb device
                byte[] bytes = data.getBytes();
                sentBytes = usbCdcConnection.bulkTransfer(usbCdcWrite, bytes, bytes.length, 1000);
            }
        }

        return Integer.toString(sentBytes);
    }

    public String read(StringBuilder dest)
    {
        receiveddata = dest;
        if(usbCdcRead == null)
        {
            Log.d("MainActivity", "not connected to a device");
            return "not connected to a device";

        }

        String state = "";

        if(readThread != null && readThread.isAlive())
        {
            readThreadRunning = false;
            state = "stopping usb listening thread";
        }
        else
        {
            readThreadRunning = true;
            readThread = new Thread(this);
            readThread.start();
            state = "starting usb listening thread";
            Toast.makeText(context, "starting usb listening thread", Toast.LENGTH_LONG).show();
        }

        Log.d("MainActivity", "read: "+ state);

        return state;

    }

    private void setupConnection()
    {

        // find the right interface
        for(int i = 0; i < usbDevice.getInterfaceCount(); i++)
        {     Log.d("MainActivity", "check device: "+ i);

            // communications device class (CDC) type device
            if(usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_CDC_DATA)
            {
                usbCdcInterface = usbDevice.getInterface(i);

                // find the endpoints
                for(int j = 0; j < usbCdcInterface.getEndpointCount(); j++)
                {
                    Log.d("MainActivity", "check endpoint: " + j);
                    if(usbCdcInterface.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                    {
                        if(usbCdcInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT)
                        {
                            // from host to device
                            usbCdcWrite = usbCdcInterface.getEndpoint(j);
                            Log.d("MainActivity", "DIR_OUT");

                        }

                        if(usbCdcInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN)
                        {
                            // from device to host
                            usbCdcRead = usbCdcInterface.getEndpoint(j);
                            Log.d("MainActivity", "DIR_in:" + usbCdcRead.toString());
                        }
                    }
                }
            }
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            Log.d("MainActivity", "im in BroadcastReceiver");

            String action = intent.getAction();
            if(ACTION_USB_PERMISSION.equals(action))
            {
                // broadcast is like an interrupt and works asynchronously with the class, it must be synced just in case
                synchronized(this)
                {
                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        // fetch all the endpoints
                        Log.d("MainActivity", "setupConnection");
                        setupConnection();

                        // open and claim interface
                        usbCdcConnection = usbManager.openDevice(usbDevice);
                        usbCdcConnection.claimInterface(usbCdcInterface, true);

                        // set dtr to true (ready to accept data)
                        usbCdcConnection.controlTransfer(0x21, 0x22, 0x1, 0, null, 0, 0);

                        // set flow control to 8N1 at 9600 baud
						/* int baudRate = 9600; byte stopBitsByte = 1; byte
						 * parityBitesByte = 0; byte dataBits = 8; byte[] msg =
						 * { (byte) (baudRate & 0xff), (byte) ((baudRate >> 8) &
						 * 0xff), (byte) ((baudRate >> 16) & 0xff), (byte)
						 * ((baudRate >> 24) & 0xff), stopBitsByte,
						 * parityBitesByte, (byte) dataBits }; */

                        //Log.d("trebla", "Flow: " + connection.controlTransfer(0x21, 0x20, 0, 0, new byte[] {(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08}, 7, 0));

                        //connection.controlTransfer(0x21, 0x20, 0, 0, msg, msg.length, 5000);
                    }
                    else
                    {
                        Log.d("trebla", "Permission denied for USB device");
                    }
                }
            }
            else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                if(usbDevice != null)
                {
                    usbCdcConnection.releaseInterface(usbCdcInterface);
                    usbCdcConnection.close();
                    usbCdcConnection = null;
                    usbDevice = null;
                    Log.d("MainActivity", "USB connection closed");
                }
            }
        }
    };

    @Override
    public void run()
    {
        Log.d("MainActivity", "Started the usb linstener");
        ByteBuffer buffer = ByteBuffer.allocate(255);
        UsbRequest request = new UsbRequest();
        request.initialize(usbCdcConnection, usbCdcRead);

        //  char dataByte, data ;
        int packetState = 0;

        while(readThreadRunning)
        {

            //   Log.d("MainActivity", " wait for a status event");
            // queue a request on the interrupt endpoint
            request.queue(buffer, buffer.capacity());
            // wait for status event
            //Log.d("MainActivity", " run.request: " + request.toString());

            if(usbCdcConnection.requestWait() == request)
            {
                // there is no way to know how many bytes are coming, so simply forward the non-null values

                for(int i = 0; i < buffer.capacity() && buffer.get(i) != 0 ; i++)
                {
                    //  Log.d("MainActivity", " run.request: " + "/" + (char)buffer.get(i) +"/" + " receiveddata: " + receiveddata.length() + " capacity:" + buffer.capacity());
                    receiveddata.append( (char) buffer.get(i));

                    if((char) buffer.get(i) == '\n')
                    {
                        // Toast toast = Toast.makeText(context, receiveddata.toString(), Toast.LENGTH_LONG);
                        //Log.d("MainActivity", "Hallo rabbit... " );

                        // toast.show();
/*
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                        Pattern pResult = Pattern.compile("RESULT: Q1: (.+) -- Q2: (.+); absolute values: (.+) -- (.+) -- (.+); AMP: (.+)");
                        Pattern pOther = Pattern.compile("(INFO|ERROR): (.+)");

                        String msg = receiveddata.toString().trim();

                        final Matcher mResult = pResult.matcher(msg);
                        final Matcher mOther = pOther.matcher(msg);

                        if (mResult.find()) {
                            final float q1 = Float.parseFloat(mResult.group(1));
                            final float q2 = Float.parseFloat(mResult.group(2));
                            final float v1 = Float.parseFloat(mResult.group(3));
                            final float v2 = Float.parseFloat(mResult.group(4));
                            final float v3 = Float.parseFloat(mResult.group(5));
                            final float amp = Float.parseFloat(mResult.group(6));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    TextView resultQ1 = (TextView)findViewById(R.id.textViewQ1_result);
                                    TextView resultQ2 = (TextView)findViewById(R.id.textViewQ2_result);
                                    TextView info = (TextView)findViewById(R.id.Info);

//TODO erfan
                                    String msg = "Raw Values: V1: "+ v1 +" V2: "+ v2 +" V3: "+ v3 +" AMP: " +amp;
                                    resultQ1.setText(q1+"");
                                    resultQ2.setText(q2+"");
                                    info.setText(msg);
                                    TextView result = (TextView)findViewById(R.id.Result);
                                    result.setText("");
                                }
                            });

                        }
                        else if (mOther.find()) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView resultQ1 = (TextView)findViewById(R.id.textViewQ1_result);
                                    TextView resultQ2 = (TextView)findViewById(R.id.textViewQ2_result);
                                    TextView info = (TextView)findViewById(R.id.Info);
                                    info.setText("");
                                    TextView result = (TextView)findViewById(R.id.Result);
                                    if ("ERROR".equals(mOther.group(1))) {
                                        resultQ1.setText("");
                                        resultQ2.setText("");
                                        result.setTextColor(Color.RED);   // rot!
                                    }else {
                                        result.setTextColor(Color.BLACK);
                                        resultQ1.setText("Scanning...");
                                        resultQ2.setText("Scanning...");
                                    }

                                   Log.d("MainActivity mOther: ", mOther.group(2)) ;
                                    result.setText(mOther.group(2));
                                }
                            });

                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }






                        receiveddata.delete(0, receiveddata.length()-1);

                        break;
                    }



                }

                if(packetState == 2)
                {
                    // send data to client
   /*                 Intent intent = new Intent();
                    intent.setAction("USB:result");
                    intent.putExtra("data", data);
                    context.sendBroadcast(intent);
                    Log.d("MainActivity", " run.request: " + data.toString());
                    receiveddata.append(data.toString());

                    // reset packet
                    packetState = 0;
                    receiveddata.delete(0,receiveddata.length());*/
                }
            }
            else
            {
                Log.e("MainActivity", "Was not able to read from USB device, ending listening thread");
                readThreadRunning = false;
                break;
            }
        }
    }
}
