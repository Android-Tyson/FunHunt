package com.party.funhunt.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.party.funhunt.MainActivity;
import com.party.funhunt.MyApplication;
import com.party.funhunt.R;
import com.party.funhunt.adapter.UpComing_Adapter;
import com.party.funhunt.appinterface.RequestInterface;
import com.party.funhunt.model.Events;
import com.party.funhunt.model.ServerResponse;
import com.party.funhunt.model.VenueData;
import com.party.funhunt.model.VenueList;
import com.party.funhunt.model.Venues;
import com.party.funhunt.utils.Api;
import com.party.funhunt.utils.Pref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavSelectionActivity extends AppCompatActivity implements View.OnClickListener {

//    @InjectView(R.id.ll_selectionView)
//    LinearLayout ll_selectionView;

    @InjectView(R.id.flex_layout)
    FlexboxLayout ll_selectionView;

    @InjectView(R.id.card_btn_continue)
    CardView card_btn_continue;

    @InjectView(R.id.tv_loading)
    TextView tv_loading;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @InjectView(R.id.tv_userName)
    TextView userName;

    List<VenueList> venueLists = new ArrayList<>();
    Realm realm;
    Pref pref;
    ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_selection);
        ButterKnife.inject(this);
        pref = new Pref(this);
        realm = Realm.getDefaultInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");

        userName.setText(pref.getPreference("userName"));
        loadEventResponse();

        card_btn_continue.setOnClickListener(this);


    }

    private void loadEventResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Venues> call = request.getVenueList();
        call.enqueue(new Callback<Venues>() {
                         @Override
                         public void onResponse(Call<Venues> call, Response<Venues> response) {
                             progressBar.setVisibility(View.GONE);
                             if (response.isSuccessful()) {
                                 venueLists = response.body().getResult();
                                 tv_loading.setVisibility(View.GONE);

                                 populateFavItem(venueLists);
                             } else {
                                 tv_loading.setText("Try again later");
                                 tv_loading.setVisibility(View.VISIBLE);
                             }
                         }

                         @Override
                         public void onFailure(Call<Venues> call, Throwable t) {
                             tv_loading.setText("Try again later");
                             Log.d("Error", t.getMessage());
                         }
                     }
        );
    }

    private void populateFavItem(final List<VenueList> venueLists) {

        for (int i = 0; i < venueLists.size(); i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_venue_fav, ll_selectionView, false);
            final TextView textView = (TextView) view.findViewById(R.id.tv_venueName);
            textView.setText(venueLists.get(i).getName());
            ll_selectionView.addView(view);
            final int finalI = i;
            VenueData results = realm.where(VenueData.class).equalTo("id", venueLists.get(i).getId()).findFirst();
            if (results != null) {
                textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                textView.setTextColor(getResources().getColor(R.color.h_white_1000));
                view.setSelected(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!view.isSelected()) {
                        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        textView.setTextColor(getResources().getColor(R.color.h_white_1000));
                        v.setSelected(true);
                        realm.beginTransaction();
                        VenueData venueData = new VenueData(venueLists.get(finalI).getId());
                        realm.copyToRealmOrUpdate(venueData);
                        realm.commitTransaction();

                    } else {
                        textView.setBackgroundColor(getResources().getColor(R.color.h_white_1000));
                        textView.setTextColor(getResources().getColor(R.color.h_black_1000));
                        v.setSelected(false);
                        realm.beginTransaction();
                        RealmQuery<VenueData> query = realm.where(VenueData.class);
                        RealmResults<VenueData> results = query.equalTo("id", venueLists.get(finalI).getId()).findAll();
                        results.deleteFirstFromRealm();
                        realm.commitTransaction();
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {

        progressDialog.show();
        RealmResults<VenueData> venueDatas = realm.where(VenueData.class).findAll();
        Log.d("params is", venueDatas.toString());
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < venueDatas.size(); i++) {
            data.add(venueDatas.get(i).id);
        }
        if (data.size() != 0) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            Call<ServerResponse> call = requestInterface.postFavVenues(pref.getPreference("userId"), data);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.body().getSuccess()) {
                        startActivity(new Intent(FavSelectionActivity.this, MainActivity.class));
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Toast.makeText(FavSelectionActivity.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            startActivity(new Intent(FavSelectionActivity.this, MainActivity.class));
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}