package com.example.erfan.bitumen_quality;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erfan.bitumen_quality.Db.Bitumen;
import com.example.erfan.bitumen_quality.Db.BitumenDAO;

/**
 * Created by Erfan on 30.06.2017.
 */

public class MainActivity extends AppCompatActivity{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private BitumenDAO dataSource;
    UsbCommunicationManager usb = null;
    private WebView webView;
    RatingBar rateBitumen;
    TextView bitumenRateText;
    Float userRating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_measure);

        Bitumen testBitumen = new Bitumen(1,"70/100","schlecht");
        Log.d(LOG_TAG, "Inhalt der Testmemo: " + testBitumen.toString());

        dataSource = new BitumenDAO(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
        Log.d(LOG_TAG, "onCreate: Create USB");

        usb = new UsbCommunicationManager(this);
        usb.connect();

        makeTheWebView();

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





        setupSettings_Bitumen();

    }

    private void setupSettings_USB() {
        Log.d(LOG_TAG, "setupSettings_USB: Is getting startet ");

        StringBuilder data = new StringBuilder();

        usb.read(data);


           //  Toast.makeText(this, "setupSettings_USB: Data", Toast.LENGTH_SHORT).show();

      //  Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "setupSettings_USB_return to main activity: "+ data.toString());
    }




    private void setupSettings_Bitumen() {
        Button BitumenSettings = (Button) findViewById(R.id.action_Bitumen);

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
        else if(id == R.id.action_Bitumen){
            Toast.makeText(this, "You have selected Bitumen Menu", Toast.LENGTH_SHORT).show();
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



}
