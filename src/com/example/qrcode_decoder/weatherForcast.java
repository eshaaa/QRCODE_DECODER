package com.example.qrcode_decoder;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

 
import org.json.JSONObject;

import com.example.qrcode_decoder.GPSTracker;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
public class weatherForcast extends Activity implements LocationListener {
  private TextView textView;
  protected LocationManager locationManager;
  protected LocationListener locationListener;
  protected Context context;
  JSONObject reader,coord,reader_2;
  String  longitude;
  String  lat ;
  JSONObject country ;
 String  count;
  			
  JSONObject main;
 Double  temperature ;
  Location location;
 
	GPSTracker gps;
  String Uri;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.weather);
    textView = (TextView) findViewById(R.id.TextView01);
    gps = new GPSTracker( this);
    if(gps.canGetLocation()){
    	
    	int latitude =  (int)gps.getLatitude();
     
    	int longitude =(int) gps.getLongitude();
    	Uri="http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=44db6a862fba0b067b1930da0d769e98";
    	// \n is for new line
    	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
    }else{
    	// can't get location
    	// GPS or Network is not enabled
    	// Ask user to enable GPS/network in settings
    	gps.showSettingsAlert();
    }

  }

  private class DownloadWebPageTask extends AsyncTask<String, Void, String>  {
    @Override
    protected String doInBackground(String... urls) {
    	
      String response = "";
      for (String url : urls) {
    	  HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
          HttpResponse execute = client.execute(httpGet);
          InputStream content = execute.getEntity().getContent();

          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
          String s = "";
          while ((s = buffer.readLine()) != null) {
            response += s;
          }
           reader = new JSONObject(response);
           
           coord  = reader.getJSONObject("coord");
            longitude= coord.getString("lon");
            lat = coord.getString("lat");
           country  = reader.getJSONObject("sys");
           count = country.getString("country");
          			
           main  = reader.getJSONObject("main");
         temperature = Double.parseDouble(main.getString("temp"))-273.00;
          
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return response;
    }

    @Override
    protected void onPostExecute(String result) {
      textView.setText("\tCountry:"+count+"\t\n"+"\tLongitude:\t"+longitude+"\t\n"+"\tLatitude:\t"+lat+"\t\n"+"\tTemperature:\t"+temperature+" deg C");
    }

 
  }

  public void onClick(View view) {
    DownloadWebPageTask task = new DownloadWebPageTask();
    task.execute(new String[] { Uri });

  }

@Override
public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub

}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}
} 