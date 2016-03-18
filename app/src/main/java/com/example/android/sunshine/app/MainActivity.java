package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ForecastFragment.Callback{

    private String mLocation;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.weather_detail_container)!=null){
            mTwoPane = true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }else{
            mTwoPane = false;
        }

        ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
        ff.setUseTodayLayout(!mTwoPane);
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
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if(ff!=null) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if(df != null){
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
        super.onResume();
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setData(contentUri);
            startActivity(intent);
        }
    }
}
