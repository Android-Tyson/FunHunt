package com.party.funhunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.party.funhunt.R;
import com.party.funhunt.adapter.UpComing_Adapter;
import com.party.funhunt.appinterface.RequestInterface;
import com.party.funhunt.model.EventList;
import com.party.funhunt.model.Events;
import com.party.funhunt.utils.Api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ermike on 1/16/2017.
 */

public class UpComing extends Fragment {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.tv_loading)
    TextView tv_loading;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    List<EventList> eventsList = new ArrayList<>();
    UpComing_Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.up_coming, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadEventResponse();
    }

    private void loadEventResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Events> call = request.getEventList();
        call.enqueue(new Callback<Events>() {
                         @Override
                         public void onResponse(Call<Events> call, Response<Events> response) {
                             if (response.isSuccessful()) {
                                 tv_loading.setVisibility(View.GONE);
                                 progressBar.setVisibility(View.GONE);
                                 eventsList = response.body().getEvents();
                                 recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                 adapter = new UpComing_Adapter(getActivity(), eventsList);
                                 recyclerView.setAdapter(adapter);
                                 adapter.notifyDataSetChanged();
                             }
                         }

                         @Override
                         public void onFailure(Call<Events> call, Throwable t) {
                             Log.d("Error", t.getMessage());
                             tv_loading.setText("Try Again Later..");
                         }
                     }
        );
    }
}
