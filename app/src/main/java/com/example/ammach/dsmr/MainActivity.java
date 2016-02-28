package com.example.ammach.dsmr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    ImageView imageView;
    Button share;
    CallbackManager callbackManager;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        textView= (TextView) findViewById(R.id.txt);
        imageView= (ImageView) findViewById(R.id.img);
        share= (Button) findViewById(R.id.share);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
               GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                try {
                                    String last_name=object.getString("last_name");
                                    String first_name=object.getString("first_name");
                                    String name=object.getString("name");
                                    String id=object.getString("id");
                                    Toast.makeText(MainActivity.this,id, Toast.LENGTH_LONG).show();
                                    textView.setText(name);
                                    URL url = new URL("https://graph.facebook.com/"+id+"/picture?type=large");
                                    Picasso.with(MainActivity.this).load(url.toString()).into(imageView);
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                textView.setText("cancel");
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("error");
            }

            });



        share.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        //startActivity(new Intent(this, ActivitySharing.class));
       Bundle params = new Bundle();
        params.putString("message", "This is a test message");
        params.putString("link", "https://hiringsolved.com/blog/assets/HTML5-developer.jpg");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/556140364545291/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject=response.getJSONObject();
                        if (jsonObject==null){
                            Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {
                                String id=jsonObject.getString("id");
                                Toast.makeText(MainActivity.this,id, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        ).executeAsync();
    }
}
