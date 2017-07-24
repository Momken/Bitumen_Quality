package com.example.erfan.bitumen_quality;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.erfan.bitumen_quality.Db.Bitumen;
import com.example.erfan.bitumen_quality.Db.BitumenDAO;

/**
 * Created by Erfan on 30.06.2017.
 */

public class MainActivity extends AppCompatActivity{

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private BitumenDAO dataSource;


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

        setupSettings_Bitumen();
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
            return true;
        }
        else if(id == R.id.action_Bitumen){
            Toast.makeText(this, "You have selected Bitumen Menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), measure.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
