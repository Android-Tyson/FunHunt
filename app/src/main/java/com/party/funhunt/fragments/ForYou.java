package com.party.funhunt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.party.funhunt.R;
import com.party.funhunt.activities.LoginActivity;
import com.party.funhunt.adapter.ForYou_Adapter;
import com.party.funhunt.adapter.UpComing_Adapter;
import com.party.funhunt.appinterface.RequestInterface;
import com.party.funhunt.model.EventList;
import com.party.funhunt.model.Events;
import com.party.funhunt.model.VenueData;
import com.party.funhunt.utils.Api;
import com.party.funhunt.utils.Pref;

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

/**
 * Created by Ermike on 1/16/2017.
 */

public class ForYou extends Fragment implements View.OnClickListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    @InjectView(R.id.tv_loading)
    TextView tv_loading;

    @InjectView(R.id.tv_login)
    TextView tv_login;

    List<EventList> eventsList;
    ForYou_Adapter adapter;
    Pref pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.for_you, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = new Pref(getActivity());
        if (!pref.getPreference("userId").isEmpty()) {
            loadEventResponse(pref.getPreference("userId"));
        } else {
            progressBar.setVisibility(View.GONE);
            tv_loading.setText("Please Login to view the events according to your choice.");
            tv_loading.setVisibility(View.VISIBLE);
            tv_login.setVisibility(View.VISIBLE);
        }
        tv_login.setOnClickListener(this);
    }

    private void loadEventResponse(String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Events> call = request.getFavVenues(userId);
        call.enqueue(new Callback<Events>() {
                         @Override
                         public void onResponse(Call<Events> call, Response<Events> response) {
//                             Log.d("response", response.body().toString());
                             if (response.isSuccessful()) {
                                 tv_loading.setVisibility(View.GONE);
                                 progressBar.setVisibility(View.GONE);
                                 tv_login.setVisibility(View.GONE);
                                 eventsList = response.body().getEvents();
                                 recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                 adapter = new ForYou_Adapter(getActivity(), eventsList);
                                 recyclerView.setAdapter(adapter);
                                 adapter.notifyDataSetChanged();
                             }
                         }

                         @Override
                         public void onFailure(Call<Events> call, Throwable t) {
                             Log.d("Error", t.getMessage());
                             progressBar.setVisibility(View.GONE);
                             tv_login.setVisibility(View.GONE);
                             tv_loading.setText("Try Again Later..");
                             tv_loading.setVisibility(View.VISIBLE);
                         }
                     }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}