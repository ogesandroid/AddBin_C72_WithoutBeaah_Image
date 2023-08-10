package com.gpd.gpdimg.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.BitmapCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cocosw.favor.FavorAdapter;
import com.gpd.gpdimg.BuildConfig;
import com.gpd.gpdimg.R;
import com.gpd.gpdimg.bin.AddBeahCode;
import com.gpd.gpdimg.bin.adapter.CurrencyRateMasterAdapter;
import com.gpd.gpdimg.bin.data.model.CurrencyConvertModel;
import com.gpd.gpdimg.bin.info.Account;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ImageCapture extends AppCompatActivity {
    private Dialog dialog;
    Account  accountCurrentBinDetails;
    private String str_from_country_code;
    private String str_from_currency_code;


    public class ThankYouReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("back.from.thankyou.screen")) {
                finish();
            }
        }
    }


    boolean disableBack = true;
    private ThankYouReceiver thankYouReceiver;

    ImageView ivAccidentCamera, ivAccidentImage;
    Uri file;
    File f;
    SharedPreferences sharedReg;
    private String note;
    Button ivCheckListItemImage;
    RelativeLayout rlAddDetails;
    AlertDialog alertDialogSaveData;
    TextView tvAccidentDetails,tvAccidentToReport,remarks;
    EditText tvSkipAccidentToReport;
    LinearLayout remarkll;
    Typeface typefaceDin;
    ArrayList<PreIgnitionCheckedListModel> accidentCheckedList;
    ArrayList<PreIgnitionCheckedListModel> preIgnitionArrayList;
    ArrayList<TireInspectionModel> tireInspectionArraylist;
    public static final String RegPref = "RegPref";
    public static final String Note = "Note";

    ArrayList<CurrencyConvertModel> currency_array = new ArrayList<>();
    CurrencyRateMasterAdapter currencyRateMasterAdapter;

    @Override
    protected void onDestroy() {
        // Unregister the logout receiver
        unregisterReceiver(thankYouReceiver);
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture2);


        thankYouReceiver = new ThankYouReceiver();
        // Register the thankYouReceiver receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("back.from.thankyou.screen");
        registerReceiver(thankYouReceiver, intentFilter);

        // Received from previous activity
        preIgnitionArrayList = (ArrayList<PreIgnitionCheckedListModel>) getIntent().getSerializableExtra("ArrayList_preIgnition");
        tireInspectionArraylist = (ArrayList<TireInspectionModel>) getIntent().getSerializableExtra("ArrayList_chooseWheelbase");


        accidentCheckedList = new ArrayList<>();

//        typefaceDin= Typeface.createFromAsset(getAssets(), "fonts/din_black_italic.otf");

        tvAccidentToReport = (TextView) findViewById(R.id.tv_report_label_AccidentToReport);
        remarks = (TextView) findViewById(R.id.remarks);
        sharedReg = getSharedPreferences(RegPref, MODE_PRIVATE);
        accountCurrentBinDetails = new FavorAdapter.Builder(ImageCapture.this).build().create(Account.class);
        tvSkipAccidentToReport = (EditText) findViewById(R.id.purpose);
        remarkll = (LinearLayout) findViewById(R.id.tv_skip_AccidentToReport);



        tvSkipAccidentToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    remarkspopup(1);


            }
        });






//        tvAccidentToReport.setTypeface(typefaceDin);
//
//        tvSkipAccidentToReport.setTypeface(typefaceDin);

//        tvSkipAccidentToReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                creatVehicleMechanismChecklist("accident_details", "null" );
////                creatVehicleMechanismChecklist("bin_image", "null" );
////
////                // Set to Application class
////                final AppControllerAddbin globalVariable = (AppControllerAddbin) getApplicationContext();
////                globalVariable.setaccidentCheckedList(accidentCheckedList);
////
////                finish();
//
//                Intent intent = new Intent(ImageCapture.this, AddBeahCode.class);
////                Intent intent = new Intent(ImageCapture.this, PreviewBinDetails.class);
//
////                intent.putExtra("ArrayList_preIgnition", preIgnitionArrayList);
////                intent.putExtra("ArrayList_chooseWheelbase", tireInspectionArraylist);
//
////                intent.putExtra("bin_image", f);
//
//                startActivity(intent);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//            }
//        });

//        tvAccidentDetails = (TextView) findViewById(R.id.tv_accident_image_container_AccidentToReport);

        rlAddDetails = (RelativeLayout) findViewById(R.id.container_button_AccidentToReport);

        ivAccidentImage = (ImageView) findViewById(R.id.iv_accident_image_container_AccidentToReport);

        ivAccidentCamera = (ImageView) findViewById(R.id.iv_camera_AccidentToReport);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ivAccidentCamera.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        ivCheckListItemImage = (Button) findViewById(R.id.iv_check_mark_AccidentToReport);

        ivCheckListItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(tvAccidentDetails.getText().toString().length() == 0 ){
//                    Toast.makeText(ImageCapture.this, " Please enter accident details"  , Toast.LENGTH_LONG).show();
//                }else {
//
//                    creatVehicleMechanismChecklist("accident_details", tvAccidentDetails.getText().toString());

//                    if (f == null){
//                        creatVehicleMechanismChecklist("bin_image ", "null");
//                    }else {
//                        creatVehicleMechanismChecklist("bin_image", f.toString());
//                    }

//                    for (int i=0;i<accidentCheckedList.size(); i++){
//                        Log.d("VcrApp", "name " + accidentCheckedList.get(i).getImage_Name() + " stateValue " + accidentCheckedList.get(i).getImage_SelectionValue() );
//                    }

                    // Set to Application class
//                    final AppControllerAddbin globalVariable = (AppControllerAddbin) getApplicationContext();
//                    globalVariable.setaccidentCheckedList(accidentCheckedList);

//                    finish();
                if(tvSkipAccidentToReport.isShown() &&  tvSkipAccidentToReport.getText().toString().equals("")){
                    tvSkipAccidentToReport.setError("This field is mandatory");
                    Toast.makeText(ImageCapture.this, "Please Enter The Remarks", Toast.LENGTH_SHORT).show();
                }else {
                    note = tvSkipAccidentToReport.getText().toString().trim();
                    Log.e("remarks", note);
                    SharedPreferences.Editor editor = sharedReg.edit();
                    editor.putString(Note, note);
                    editor.commit();
                    Log.e("remarksin:", Note);
                    Intent intent = new Intent(ImageCapture.this, AddBeahCode.class);
//                    Intent intent = new Intent(ImageCapture.this, PreviewBinDetails.class);
//                    intent.putExtra("bin_image", f.toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);


                }
//                } // else of text

            }
        });

        ivAccidentCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();

            }
        });

//        rlAddDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String currentAccidentDetails =  tvAccidentDetails.getText().toString();
//                showAddDetailsPopup(currentAccidentDetails);
//
//            }
//        });


    } // onCreate

    private void remarkspopup(final int i) {


        currency_array.clear();
//        Typeface osr = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
//        Typeface osb = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        dialog = new Dialog(ImageCapture.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.country_popup);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setCancelable(true);

        final RecyclerView recycler_country = (RecyclerView) dialog.findViewById(R.id.recycler_country);
        final AutoCompleteTextView auto_country = (AutoCompleteTextView) dialog.findViewById(R.id.auto_country);
        final ProgressBar pb = (ProgressBar) dialog.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
//        auto_country.setVisibility(View.VISIBLE);
//        if(country_check ==3)
//        {
//            auto_country.setVisibility(View.GONE);
//        }

        setCountryList(recycler_country, "", pb, "array", i);

        auto_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count != 1)
                    filterRemarks(s.toString());
                else
                    filterRemarks("");
            }

            @Override
            public void afterTextChanged(Editable s) {
//
            }
        });

        dialog.show();

    }

    private void filterRemarks(String text) {
        ArrayList<CurrencyConvertModel> filteredList = new ArrayList<>();
        for (CurrencyConvertModel item : currency_array) {
//            if ((item.getCurrency_master_name_en() + " (" + item.getCurrency_master_code() + ")").toUpperCase().startsWith(text.toUpperCase(Locale.ROOT))) {
            if (item.getCurrency_master_name_en().toUpperCase().startsWith(text.toUpperCase(Locale.ROOT))) {
//            if (item.getCurrency_master_name_en().toUpperCase().contains(text.toUpperCase())) {
                filteredList.add(item);
            }
        }
        Log.e("size1", "size=" + filteredList.size());
        currencyRateMasterAdapter.filterList(filteredList);
    }


    public void setCountryFromrate(String currency_name, String currency_code) {
        dialog.dismiss();
        tvSkipAccidentToReport.setText(currency_name);
//        str_from_country_code = country_code;
        str_from_currency_code = currency_code;
        if(tvSkipAccidentToReport.getText().toString() != null){
            tvSkipAccidentToReport.setError(null);
        }
    }

    public void setCountryTorate(String currency_name, String currency_code) {
        dialog.dismiss();
        tvSkipAccidentToReport.setText(currency_name);
//        str_from_country_code = country_code;
        str_from_currency_code = currency_code;
        if(tvSkipAccidentToReport.getText().toString() != null){
            tvSkipAccidentToReport.setError(null);
        }
    }



//    public void setCountryTorate(String currency_name, String currency_code, String country_code,String country_2_code) {
//        dialog.dismiss();
//        tx_amount_to.setText(currency_name);
//        String currencyncode = currency_name + " (" + currency_code + ")";
//        tx_country2.setText(currencyncode);
//        str_to_country_code = country_code;
//        str_to_currency_code = currency_code;
//
////        convertCurrency();
//        if(!tx_country2.getText().toString().equals("")){
//            tx_country2.setError(null);
//        }
//
//
////        Picasso.with(CurrencyCashRate.this)
////                .load(Config.flag_url1 + country_2_code.toLowerCase() + ".png")
////                .placeholder(R.drawable.default_image)   // optional
////                .error(R.drawable.default_image)      // optional
////                .into(flag2);
//    }



    public void setCountryList(final RecyclerView recycler_country, final String search, final ProgressBar pb, final String from, final int i) {
        currency_array.clear();
        pb.setVisibility(View.VISIBLE);
        if (from.equals("search")) {
            recycler_country.setVisibility(View.GONE);
        }
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String dateToStr = format.format(today);

        final RequestQueue requestQueue = Volley.newRequestQueue(ImageCapture.this);
        String url = "https://ogesinfotech.com/Add_bin/get_oges_apiv2.php?p=33&company_id=1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.trim();
                if (response != null) {
                    try {
                        response = new String(response.getBytes(), "UTF-8");
                        response = Html.fromHtml(response).toString();
                        Log.e("response", response);
                        currency_array.clear();
                        pb.setVisibility(View.GONE);
                        recycler_country.setVisibility(View.VISIBLE);
                        try {
//                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CurrencyConvertModel currencyModel = new CurrencyConvertModel();
                                    JSONObject currencyObject = jsonArray.getJSONObject(i);
//
                                    currencyModel.setCurrency_master_name_en(currencyObject.getString("remarks_name"));
                                    currencyModel.setCurrency_master_code(currencyObject.getString("remarks_id"));
//                                    currencyModel.setGe_countries_3_code(currencyObject.getString("countryCode"));
//                                    currencyModel.setGe_countries_2_code(currencyObject.getString("countryCode2"));
                                    currency_array.add(currencyModel);
//                                    }
//                                    if (currencyObject.getString("currency_master_code").equals("USD")) {
//                                        if (usd_status == 0) {
//                                            currencyModel.setId(currencyObject.getString("id"));
//                                            currencyModel.setCurrency_master_country_id(currencyObject.getString("currency_master_country_id"));
//                                            currencyModel.setCurrency_master_name_en(currencyObject.getString("currency_master_name_en"));
//                                            currencyModel.setCurrency_master_code(currencyObject.getString("currency_master_code"));
//                                            currencyModel.setGe_countries_3_code(currencyObject.getString("ge_countries_3_code"));
//                                            currencyModel.setGe_countries_2_code(currencyObject.getString("ge_countries_c_code"));
//                                            currency_array.add(currencyModel);
//                                            usd_status = 1;
//                                        }
//                                    }
                                }
                                if (!currency_array.equals(null)) {
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ImageCapture.this, LinearLayoutManager.VERTICAL, false);
                                    recycler_country.setLayoutManager(layoutManager);
                                    currencyRateMasterAdapter = new CurrencyRateMasterAdapter(ImageCapture.this, currency_array, i);
                                    recycler_country.setAdapter(currencyRateMasterAdapter);
                                    currencyRateMasterAdapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
//                progressDoalog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);

//                progressDoalog.dismiss();
                Toast.makeText(ImageCapture.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                    params2.put("p", "33");
                    params2.put("company_id", accountCurrentBinDetails.getCompanyID());
                    Log.e("comid", accountCurrentBinDetails.getCompanyID());
                return new JSONObject(params2).toString().getBytes();
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                if (Config.auth_value.equals("1")) {
//                    params.put("Authorization", "Bearer " + access_token);
//                }
//                return params;
//            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }






    public void takePicture() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
////        file = FilePcrovider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createImageFile());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivityForResult(intent, 100);
        f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", f));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, 100);

    }


    public void showAddDetailsPopup(String currentAccidentDetails){

//        LayoutInflater factory = LayoutInflater.from(this);
//
//        final View deleteDialogView = factory.inflate(R.layout.popup_add_details, null);
//
//        alertDialogSaveData = new AlertDialog.Builder(this).create();
//
//        alertDialogSaveData.setCancelable(false);
//        alertDialogSaveData.setCanceledOnTouchOutside(false);
//
//        alertDialogSaveData.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        alertDialogSaveData.setView(deleteDialogView);
//
//        final EditText etAdddDetails = (EditText) deleteDialogView.findViewById(R.id.et_enter_details_popup_add_details);
//
//        etAdddDetails.setText(currentAccidentDetails);
//
//        deleteDialogView.findViewById(R.id.bt_go_to_update_page_popup_add_details).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                String details = etAdddDetails.getText().toString();
//
//                saveAccidentDetails(details);
//
//                alertDialogSaveData.dismiss();
//
//            }
//        });
//
//        deleteDialogView.findViewById(R.id.bt_cancel_update_page_popup_add_details).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //your business logic
//                alertDialogSaveData.dismiss();
//            }
//        });
//
//        alertDialogSaveData.show();
    }


    public void saveAccidentDetails(String details){

        tvAccidentDetails.setText(details);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

//                .setImageBitmap(bitmap);
                //  ivAccidentImage.setImageURI(file);

                    String filePath = f.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                    ivAccidentImage.setVisibility(View.VISIBLE);
                    Picasso.with(ImageCapture.this).load(f).into(ivAccidentImage);
                    remarkll.setVisibility(View.VISIBLE);
//                    tvSkipAccidentToReport.setVisibility(View.VISIBLE);
//                    remarks.setVisibility(View.VISIBLE);

                    ivAccidentCamera.setVisibility(View.GONE);
                    if(bitmap != null){
                    int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                    int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                    if (bitmapByteCount < ConfigConstants.byteCount) {
                        Cache.getInstance().getLru().put("image1", bitmap);
                    } else {
                        Cache.getInstance().getLru().put("image1", scaled);
                    }




                }else{

                }
//                try{
//                    final InputStream imageStream = getContentResolver().openInputStream(file);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    String encodedImage = encodeImage(selectedImage);
//
//                    Log.d("VcrApp", "file path " + file + "  Base 64 encoded image " + encodedImage);
//
//                }catch (FileNotFoundException e){
//                    Log.d("VcrApp", e.toString());
//                }


            }
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                ivAccidentCamera.setEnabled(true);

            }
        }
    } // onRequestPermissionsResult

    public void creatVehicleMechanismChecklist(String key , String stateValue){


        PreIgnitionCheckedListModel imageModel = new PreIgnitionCheckedListModel();
        imageModel.setImage_Name(key);
        imageModel.setImage_SelectionValue(stateValue);
        accidentCheckedList.add(imageModel);

    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


}
