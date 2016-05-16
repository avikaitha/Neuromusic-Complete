package ark.neuromusic.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import ark.neuromusic.R;
import ark.neuromusic.generics.GenericActivity;
import ark.neuromusic.utils.Database;

public class LoginActivity extends GenericActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    Database myDB;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    public static final String FB_NAME = "ark.neuromusic.fb_name";
    public static final String FB_DP = "ark.neuromusic.fb_dp";
    public static final String FB_COVER = "ark.neuromusic.fb_cover";
    public static final String FB_EMAIL = "ark.neuromusic.fb_email";
    public static final String FB_NONEURO = "ark.neuromusic.fb_noneuro";
    boolean isNeuroskyConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recv_intent = getIntent();
        if(recv_intent.hasExtra(ConnectActivity.NO_NEUROSKY)) {
            isNeuroskyConnected = false;
        }
        myDB = new Database(getApplicationContext());
        myDB.getWritableDatabase();
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ark.neuromusic",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        callbackManager = CallbackManager.Factory.create();



        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email",
                "user_likes","user_actions.music"));
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("LoginActivity", "Login Successful");

                Log.d("LoginActivity","Code Reached");
                loginButton.setVisibility(View.INVISIBLE);
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("Perms", loginResult.getAccessToken().getPermissions().toString());
                                Log.d("Declined Perms", loginResult.getAccessToken().getDeclinedPermissions().toString());
                                Log.d("LoginActivity", object.toString());

                                // Application code
                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String dp_url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String coverpic_url = object.getJSONObject("cover").getString("source");

                                    ContentValues values = new ContentValues();
                                    values.put("EMAIL",email);
                                    values.put("NAME",name);
                                    values.put("PICTURE", dp_url);
                                    values.put("COVER", coverpic_url);
//                                    values.put("ISNEUROSKYCONN",isNeuroskyConnected?1:0);
                                    Log.d("LoginActivity", values.get("EMAIL").toString() + values.get("NAME").toString() + values.get("PICTURE").toString() + values.get("COVER").toString());
                                    myDB.insertValues(values, "USERS");

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra(FB_NAME, name);
                                    intent.putExtra(FB_DP,dp_url);
                                    intent.putExtra(FB_COVER,coverpic_url);
                                    intent.putExtra(FB_EMAIL,email);
                                    intent.putExtra(FB_NONEURO,isNeuroskyConnected);
                                    startActivity(intent);
//                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,music,picture,cover");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        // If already logged in show the home view
        if (accessToken != null) {

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            HashMap<String,String> fbData = myDB.getFBData();
            intent.putExtra(FB_NAME, fbData.get("NAME"));
            intent.putExtra(FB_DP,fbData.get("PICTURE"));
            intent.putExtra(FB_COVER,fbData.get("COVER"));
            intent.putExtra(FB_EMAIL,fbData.get("EMAIL"));
            intent.putExtra(FB_NONEURO,isNeuroskyConnected);
            startActivity(intent);
            finish();
        }



}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.login_layout));
        System.gc();

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
