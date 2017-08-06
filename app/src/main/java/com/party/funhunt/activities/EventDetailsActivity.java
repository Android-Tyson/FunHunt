package com.party.funhunt.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.party.funhunt.R;
import com.party.funhunt.appinterface.RequestInterface;
import com.party.funhunt.model.SingleEvent;
import com.party.funhunt.model.SingleEventDetails;
import com.party.funhunt.utils.Api;
import com.party.funhunt.utils.Utilities;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ermike on 4/9/2017.
 */
public class EventDetailsActivity extends AppCompatActivity {

    @InjectView(R.id.main_content)
    CoordinatorLayout main_content;

    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @InjectView(R.id.iv_eventImage)
    ImageView iv_eventImage;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.tv_noData)
    TextView tv_noData;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @InjectView(R.id.tv_eventDate)
    TextView tv_eventDate;

    @InjectView(R.id.tv_eventTime)
    TextView tv_eventTime;

    @InjectView(R.id.tv_eventBody)
    TextView tv_eventBody;

    @InjectView(R.id.tv_eventDescription)
    TextView tv_eventDescription;

    @InjectView(R.id.map)
    MapView mapView;

    @InjectView(R.id.tv_venueName)
    TextView tv_venueName;

    @InjectView(R.id.tv_venueAddress)
    TextView tv_venueAddress;

    SingleEventDetails singleEventDetails;
    String eventId;
    GoogleMap googleMap;
    Double lat, lng;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.inject(this);
        Bundle intent = getIntent().getExtras();
        eventId = intent.getString("eventId");
        singleEventDetails = new SingleEventDetails();

        main_content.setVisibility(View.GONE);
        loadingEventDetails();
        mapView.onCreate(savedInstanceState);
    }

    private void loadingEventDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<SingleEvent> call = request.getEventDetails(eventId);
        call.enqueue(new Callback<SingleEvent>() {
                         @Override
                         public void onResponse(Call<SingleEvent> call, Response<SingleEvent> response) {
                             Log.d("response", response.body().toString());
                             if (response.isSuccessful()) {
                                 tv_noData.setVisibility(View.GONE);
                                 progressBar.setVisibility(View.GONE);
                                 main_content.setVisibility(View.VISIBLE);
                                 singleEventDetails = response.body().getResult();
                                 populateResponse(singleEventDetails);
                             }
                         }

                         @Override
                         public void onFailure(Call<SingleEvent> call, Throwable t) {
                             Log.d("Error", t.getMessage());
                             tv_noData.setText("Try Again Later..");
                         }
                     }
        );
    }

    private void populateResponse(SingleEventDetails singleEventDetails) {
        collapsingToolbarLayout.setTitle(singleEventDetails.getName());
        toolbar.setNavigationIcon(R.drawable.md_nav_back);
        toolbar.inflateMenu(R.menu.menu_single_events);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(getApplicationContext()).load(Api.BASE_URL + "images/event_" + singleEventDetails.getPhoto()).into(iv_eventImage);
        tv_eventDescription.setText(singleEventDetails.getDescription());
        tv_eventDate.setText(Utilities.getDateWithFormat("MMM d", singleEventDetails.getDate()));
        tv_eventTime.setText(", " + singleEventDetails.getTime());
        tv_eventBody.setText(Html.fromHtml(singleEventDetails.getBody()));
        tv_venueName.setText(singleEventDetails.getVenueName());
        tv_venueAddress.setText(singleEventDetails.getStreet() + ", " + singleEventDetails.getCity());
        lat = Double.valueOf(singleEventDetails.getLatitude());
        lng = Double.valueOf(singleEventDetails.getLongitude());

        if (mapView != null) {
            googleMap = mapView.getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps))
                    .anchor(0.0f, 1.0f)
                    .position(new LatLng(lat, lng)));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(lat, lng));
            final LatLngBounds bounds = builder.build();
            // Updates the location and zoom of the MapView
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                    googleMap.moveCamera(cameraUpdate);
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_fav) {
            Toast.makeText(EventDetailsActivity.this, "Fav Icon selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


}
