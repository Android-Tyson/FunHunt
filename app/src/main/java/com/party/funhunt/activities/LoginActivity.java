package com.party.funhunt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.party.funhunt.MainActivity;
import com.party.funhunt.R;
import com.party.funhunt.appinterface.RequestInterface;
import com.party.funhunt.model.ServerResponse;
import com.party.funhunt.model.User;
import com.party.funhunt.model.Venues;
import com.party.funhunt.utils.Api;
import com.party.funhunt.utils.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.login_button)
    LoginButton loginButton;

    @InjectView(R.id.tv_skip)
    TextView tv_skip;

    Pref pref;
    ProgressDialog progressDialog;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        pref = new Pref(this);
        ButterKnife.inject(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");


        tv_skip.setOnClickListener(this);

        loginButton.setLoginBehavior(LoginBehavior.SSO_WITH_FALLBACK);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String id = object.optString("id");
                        String name = object.optString("name");
                        User user = new User();
                        user.setId(id);
                        user.setName(name);
                        postRegistrationProcess(id, name);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d("Error in login :",e.toString());
            }
        });
    }

    private void postRegistrationProcess(final String id, final String username) {
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ServerResponse> call = request.postRegister(id, username);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body().getSuccess()) {
                    pref.setPreferences("userId", id);
                    pref.setPreferences("userName", username);
                    Log.d("response is : ", response.body().getResult() + " & " + response.body().getSuccess());
                    startActivity(new Intent(LoginActivity.this, FavSelectionActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Please Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
