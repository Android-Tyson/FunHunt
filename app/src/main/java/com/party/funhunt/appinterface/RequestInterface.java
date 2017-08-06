package com.party.funhunt.appinterface;

import android.support.annotation.ArrayRes;

import com.party.funhunt.model.Events;
import com.party.funhunt.model.ServerResponse;
import com.party.funhunt.model.SingleEvent;
import com.party.funhunt.model.User;
import com.party.funhunt.model.VenueData;
import com.party.funhunt.model.Venues;
import com.party.funhunt.utils.Pref;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ermike on 3/28/2017.
 */

public interface RequestInterface {

    @GET("api/events")
    Call<Events> getEventList();

    @GET("api/venues")
    Call<Venues> getVenueList();

    @GET("api/user-events/{user_id}")
    Call<Events> getFavVenues(@Path("user_id") String id);

    @GET("api/event/{id}")
    Call<SingleEvent> getEventDetails(@Path("id") String id);

//    @FormUrlEncoded
//    @POST("api/reveller/")
//    Call<User> postRegister(@Field("user_id") String userId);
//
//    @POST("api/reveller/")
//    Call<String> postUserId(@Body String body);

//    @FormUrlEncoded
//    @POST("/api/reveller")
//    Call<User> postLoginRequest(
//            @Field("user_id") String id);


    @FormUrlEncoded
    @POST("api/reveller")
    Call<ServerResponse> postRegister(@Field("user_id") String userId, @Field("name") String userName);

    @FormUrlEncoded
    @POST("api/fav-venues/{user_id}")
    Call<ServerResponse> postFavVenues(@Path("user_id") String id, @Field("venue_ids[]") ArrayList<String> data);
}
