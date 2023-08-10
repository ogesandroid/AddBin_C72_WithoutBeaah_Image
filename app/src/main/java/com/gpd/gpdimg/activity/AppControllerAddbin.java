package com.gpd.gpdimg.activity;

import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gpd.gpdimg.bin.db.DaoMaster;
import com.gpd.gpdimg.bin.db.DaoSession;
import com.gpd.gpdimg.bin.info.ConnectivityReceiver;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

public class AppControllerAddbin extends MultiDexApplication {

    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
    public static final String TAG = AppControllerAddbin.class
            .getSimpleName();
    private RequestQueue mRequestQueue;

    private static AppControllerAddbin mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"vcrApp-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        mInstance = this;

        ///// Using the below lines of code we can toggle ENCRYPTED to true or false in other to use either an encrypted database or not.
//      DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "users-db");
//      Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
//      daoSession = new DaoMaster(db).newSession();
    }

    public static synchronized AppControllerAddbin getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    // Storing one session of vcr data


    private String name;
    private String email;
    private String jsonPlainString;
    private String checkinStatus;

    // for vcr
    ArrayList<PreIgnitionCheckedListModel> preIgnitionCheckedList;
    ArrayList<TireInspectionModel> tireInspectionCheckedList;
    ArrayList<PreIgnitionCheckedListModel> accidentCheckedList;
    ArrayList<PreIgnitionCheckedListModel> postIgnitionCheckedList;
    ArrayList<PreIgnitionCheckedListModel> vehicleMechanismCheckedList;

    ArrayList<FailureReasonModel> failureReasonCheckedList;

    // for vcr
    public void setpreIgnitionCheckedList(ArrayList<PreIgnitionCheckedListModel> pre_IgnitionCheckedList) {
        this.preIgnitionCheckedList = pre_IgnitionCheckedList;
    }

    public ArrayList<PreIgnitionCheckedListModel> getpreIgnitionCheckedList() {
        return preIgnitionCheckedList;
    }

    public void settireInspectionCheckedList(ArrayList<TireInspectionModel> tire_InspectionCheckedList) {
        this.tireInspectionCheckedList = tire_InspectionCheckedList;
    }

    public ArrayList<TireInspectionModel> gettireInspectionCheckedList() {
        return tireInspectionCheckedList;
    }

    public void setaccidentCheckedList(ArrayList<PreIgnitionCheckedListModel> accident_CheckedList) {
        this.accidentCheckedList = accident_CheckedList;
    }

    public ArrayList<PreIgnitionCheckedListModel> getaccidentCheckedList() {
        return accidentCheckedList;
    }


    public void setpostIgnitionCheckedList(ArrayList<PreIgnitionCheckedListModel> post_IgnitionCheckedList) {
        this.postIgnitionCheckedList = post_IgnitionCheckedList;
    }

    public ArrayList<PreIgnitionCheckedListModel> getpostIgnitionCheckedList() {
        return postIgnitionCheckedList;
    }

    public void setVehicleMechanismCheckedList(ArrayList<PreIgnitionCheckedListModel> vehicleMechanism_CheckedList) {
        this.vehicleMechanismCheckedList = vehicleMechanism_CheckedList;
    }

    public ArrayList<PreIgnitionCheckedListModel> getVehicleMechanismCheckedList() {
        return vehicleMechanismCheckedList;
    }

    public void setFailureReasonCheckedList(ArrayList<FailureReasonModel> failureReason_CheckedList) {
        this.failureReasonCheckedList = failureReason_CheckedList;
    }

    public ArrayList<FailureReasonModel> getFailureReasonCheckedList() {
        return failureReasonCheckedList;
    }

    public String getJsonPlainString() {

        return jsonPlainString;
    }

    public void setJsonPlainString(String json_PlainString) {

        jsonPlainString = json_PlainString;

    }

    public String getCheckinStatusString() {

        return checkinStatus;
    }

    public void setCheckinStatusString(String checkin_Status) {

        checkinStatus = checkin_Status;

    }


    public void  clearAllArraylist(){

        if (preIgnitionCheckedList != null){

            preIgnitionCheckedList.clear();
        }

        if (tireInspectionCheckedList != null){
            tireInspectionCheckedList.clear();
        }

        if (accidentCheckedList != null){
            accidentCheckedList.clear();
        }

        if (postIgnitionCheckedList != null){
            postIgnitionCheckedList.clear();
        }

        if (vehicleMechanismCheckedList != null){
            vehicleMechanismCheckedList.clear();
        }

        checkinStatus = "";
    }


}
