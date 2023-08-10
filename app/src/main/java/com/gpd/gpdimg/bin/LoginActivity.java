package com.gpd.gpdimg.bin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cocosw.favor.FavorAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.gpd.gpdimg.R;
import com.gpd.gpdimg.bin.data.remote.ApiUtils;
import com.gpd.gpdimg.bin.info.Account;
import com.gpd.gpdimg.bin.info.ConnectivityReceiver;
import com.gpd.gpdimg.bin.info.ImageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements AddBin{

    Button btn_login;
    EditText edt_username,edt_password;
    ProgressDialog loading;
    String username,password;
    private String mService;
    Context context;
    TinyDB tinyDB;
    //private String LOGIN_URL = "http://www.gpduae.com/Addbin/get_oges_api.php?";
    private String LOGIN_URL = "http://ogesinfotech.com/Add_bin/get_oges_api.php?";
    //    private String LOGIN_URL = "http://192.168.10.111:8081/Anjitha/Gpd_technology/get_oges_api.php?";
    private static final String TAG = "AddBin";
    Account accountCompanyID;

    private ImageView img_reset_updates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText)findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        img_reset_updates = (ImageView) findViewById(R.id.img_reset_updates);

        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            } else {

            }
        }
        accountCompanyID = new FavorAdapter.Builder(LoginActivity.this).build().create(Account.class);
        tinyDB=new TinyDB(this);

        mService = tinyDB.preference.getString("base_url", "") + "get_oges_apiv2.php?";
        Log.e("mservice",mService);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=edt_username.getText().toString();
                password=edt_password.getText().toString();
//                checkConnection();
//                login_post();
                logincall();

            }
        });
        img_reset_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
                Log.d(TAG, "logout: ");

                builder.setMessage("Are you sure want to change URL?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tinyDB.resetSetting();
                                finish();
                                Intent i = new Intent(LoginActivity.this, NetworkSettingActivity.class);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                builder.show();

            }
        });

    }

    private void logincall() {
//        callprogressDialog();
        loading = ProgressDialog.show(LoginActivity.this, "Checking", "Please wait...", false, false);
//        Log.d(TAG, "inside login_post");

        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://ogesinfotech.com/Add_bin/get_oges_apiv2.php?" , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDoalog.dismiss();
                loading.dismiss();
                Log.e("response", " " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")){

                        ImageManager imageManager = new ImageManager();
                        JSONArray detailsjsonArray = jsonObject.getJSONArray("Company_Details");
                        for (int i=0;i<detailsjsonArray.length();i++) {
                            JSONObject company_obj = detailsjsonArray.getJSONObject(i);
                            String company_id = company_obj.getString("id");
                            int company_flag = company_obj.getInt("company_flag");
                            accountCompanyID.setCompanyLogo(company_obj.getString("company_logo"));
                            accountCompanyID.setCompanyID(company_id);
                            if (company_flag==1) {
                                imageManager.volley_download_icon(LoginActivity.this, ApiUtils.ICON_URL + company_id + "/" + company_obj.getString("company_logo"),company_id);
//                        imageManager.volley_download_icon(LoginActivity.this, mService+"data_folder/company/logo/" + company_id + "/" + company_obj.getString("company_logo"),company_id);
                                Log.e("company_logo","= "+company_obj.getString("company_logo"));
                            }
                            accountCompanyID.setCompanyFlag(String.valueOf(company_flag));

                            tinyDB.setString("company_id", company_id);

//                    accountCompanyID.setGovernarateTitle("City");
//                    accountCompanyID.setGovernarateStatus("1");
//                    accountCompanyID.setWillayathTitle("Zone");
//                    accountCompanyID.setWillayathStatus("1");

                            if(jsonObject.getInt("governorate_status")==1){
                                accountCompanyID.setGovernarateTitle(company_obj.getString("company_governate_label"));
                                accountCompanyID.setGovernarateStatus("1");
                            }else{
                                accountCompanyID.setGovernarateTitle("Govornarate");
                                accountCompanyID.setGovernarateStatus("0");
                            }
                            if(jsonObject.getInt("williyat_status")==1){
                                accountCompanyID.setWillayathTitle(company_obj.getString("company_williyat_label"));
                                accountCompanyID.setWillayathStatus("1");
                            }else{
                                accountCompanyID.setWillayathTitle("Willayat");
                                accountCompanyID.setWillayathStatus("0");
                            }

                        }

//                Intent i = new Intent(LoginActivity.this, ImageCapture.class);
                        Intent i = new Intent(LoginActivity.this,SelectGovernorate.class);
                        startActivity(i);
                        finish();



                    }else {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, " Invalid email or password!!!", Toast.LENGTH_SHORT).show();
                    }
//                    progressDoalog.dismiss();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDoalog.dismiss();
//                Toast.makeText(LoginActivity.this, "unable_to_connect"+error.toString(), Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this, "check your internet",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.unauthorized_access),
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error),
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.parse_error),
                            Toast.LENGTH_SHORT).show();

//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("company_email", username);
                params.put("company_password", password);
                params.put("p", "6");


                Log.e("params", "" + params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            // if internet is present upload , no problem

            login_post();

            message = "Connected to Internet";
            color = Color.WHITE;

            Snackbar snackbar = Snackbar.make(findViewById(R.id.container_login), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();

        } else {

            message = "Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.container_login), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    } // showSnack
    public void login_post() {
        ServerRequest serverRequest = new ServerRequest(LoginActivity.this,context);
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("p", "6");
        values.put("company_email",username);
        values.put("company_password",password);

        try {
            serverRequest.postData(values,mService,1);
//            serverRequest.postData(values,LOGIN_URL,1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        loading = ProgressDialog.show(LoginActivity.this, "Checking", "Please wait...", false, false);
        Log.d(TAG, "inside login_post");




    } // login_post

    @Override
    public void serverResponseData(String data) {
        try {
            Log.e(TAG, "serverResponseData: "+data);
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getInt("code")==1){
                loading.dismiss();
                ImageManager imageManager = new ImageManager();
                JSONArray detailsjsonArray = jsonObject.getJSONArray("Company_Details");
                for (int i=0;i<detailsjsonArray.length();i++) {
                    JSONObject company_obj = detailsjsonArray.getJSONObject(i);
                    String company_id = company_obj.getString("id");
                    int company_flag = company_obj.getInt("company_flag");
                    accountCompanyID.setCompanyLogo(company_obj.getString("company_logo"));
                    accountCompanyID.setCompanyID(company_id);
                    if (company_flag==1) {
                        imageManager.volley_download_icon(LoginActivity.this, ApiUtils.ICON_URL + company_id + "/" + company_obj.getString("company_logo"),company_id);
//                        imageManager.volley_download_icon(LoginActivity.this, mService+"data_folder/company/logo/" + company_id + "/" + company_obj.getString("company_logo"),company_id);
                        Log.e("company_logo","= "+company_obj.getString("company_logo"));
                    }
                    accountCompanyID.setCompanyFlag(String.valueOf(company_flag));

                    tinyDB.setString("company_id", company_id);

//                    accountCompanyID.setGovernarateTitle("City");
//                    accountCompanyID.setGovernarateStatus("1");
//                    accountCompanyID.setWillayathTitle("Zone");
//                    accountCompanyID.setWillayathStatus("1");

                    if(jsonObject.getInt("governorate_status")==1){
                        accountCompanyID.setGovernarateTitle(company_obj.getString("company_governate_label"));
                        accountCompanyID.setGovernarateStatus("1");
                    }else{
                        accountCompanyID.setGovernarateTitle("Govornarate");
                        accountCompanyID.setGovernarateStatus("0");
                    }
                    if(jsonObject.getInt("williyat_status")==1){
                        accountCompanyID.setWillayathTitle(company_obj.getString("company_williyat_label"));
                        accountCompanyID.setWillayathStatus("1");
                    }else{
                        accountCompanyID.setWillayathTitle("Willayat");
                        accountCompanyID.setWillayathStatus("0");
                    }

                }

//                Intent i = new Intent(LoginActivity.this, ImageCapture.class);
                Intent i = new Intent(LoginActivity.this,SelectGovernorate.class);
                startActivity(i);
                finish();



            }else {
                loading.dismiss();
                Toast.makeText(LoginActivity.this, " Invalid email or password!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "serverResponseData: "+e );
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();

    }

    private void dismissProgressDialog() {
        if(loading != null && loading.isShowing())
            loading.dismiss();
    }

}