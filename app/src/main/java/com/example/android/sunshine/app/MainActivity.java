package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private String mLocation;
    private static final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            return true;
        }else if(id==R.id.action_map){
            openPrefferedLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPrefferedLocationInMap(){
        String location = Utility.getPreferredLocation(this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q",location)
                .build();
        intent.setData(geoLocation);
        //intent.setPackage("com.google.android.apps.maps");
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
    }

    @Override
    protected void onResume() {
        String location = Utility.getPreferredLocation(this);
        if(location!=null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if(ff!=null) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
        super.onResume();
    }
}
