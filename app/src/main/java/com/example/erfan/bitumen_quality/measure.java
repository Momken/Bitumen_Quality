package com.example.erfan.bitumen_quality;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TabHost;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class measure extends AppCompatActivity {

    private WebView webView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitumen2);
        Log.d("MainActivity", "activity_bitumen2: Measure");

        makeTheTabs();
        makeTheWebView();
        Log.d("MainActivity", "activity_bitumen2: make the Tabs and make the web view");




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeTheTabs(){


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Bitumen Daten");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Bitumen Scann");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Bitumen ??");
        host.addTab(spec);
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
                "<div id=\"gauge_div\" style=\"width:600px; height: 600px;\"></div>\n" +
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

}
