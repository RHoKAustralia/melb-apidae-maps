package com.example.apidae_maps;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	
	private GoogleMap map;
	private double latitude = -37.8136;
	private double longitude = 144.9631;
	public MarkerOptions marker;
	public LocationClient loccli;
	public Location myLoc;
	public Bitmap b;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		Drawable d=(Drawable) getResources().getDrawable(R.drawable.community_icon);
		BitmapDrawable bd=(BitmapDrawable) d.getCurrent();
		b=bd.getBitmap();
		
		loccli = new LocationClient(this, this, this);
		
		if (map == null) {
			Toast.makeText(getApplicationContext(), "Unable to create map", Toast.LENGTH_SHORT).show();
		}
		else {		
			marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("We will Rhok you!");
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		if(!loccli.isConnected() && !loccli.isConnecting()) {
        	loccli.connect();
        }
	}
	@Override
    protected void onResume() {
        super.onResume();
        if(!loccli.isConnected() && !loccli.isConnecting()) {
        	loccli.connect();
        }
    }
	protected void onStop() {
        // Disconnecting the client invalidates it.
        loccli.disconnect();
        super.onStop();
    }
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		myLoc = loccli.getLastLocation();
		Bitmap bresize=Bitmap.createScaledBitmap(b, b.getWidth()/6, b.getHeight()/6, false);
		marker.icon(BitmapDescriptorFactory.fromBitmap(bresize));  
		marker.snippet("Melbourne: #01");
		map.addMarker(marker);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(myLoc.getLatitude(), myLoc.getLongitude())).zoom(9).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				Log.i("Marker", "Clicked");
				LinearLayout infobar = (LinearLayout) (findViewById(R.id.infobar));
				TextView title = (TextView) (findViewById(R.id.title));
				TextView info = (TextView) (findViewById(R.id.info));
				infobar.setVisibility(android.view.View.VISIBLE);
				title.setVisibility(android.view.View.VISIBLE);
				info.setVisibility(android.view.View.VISIBLE);
				infobar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,NextActivity.class);
						startActivity(intent);
					}
				});
				return false;
			}
		});
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}	
}
