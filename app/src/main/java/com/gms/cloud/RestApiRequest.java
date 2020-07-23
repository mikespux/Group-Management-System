package com.gms.cloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RestApiRequest {


    private final String TAG;
    private String _URL;
    private String _TOKEN;
    private Context _context;
    private String _licenseKey;
    SharedPreferences mSharedPrefs;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public RestApiRequest(Context ctx) {
        _URL = null;
        _TOKEN = null;

        TAG = "RestApiRequest";

        _context = ctx;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
       prefs = PreferenceManager.getDefaultSharedPreferences(_context);

      // _URL = mSharedPrefs.getString("portalURL", null);

       /* if (this.mSharedPrefs.getString("internetAccessModes", null).equals("WF")) {
            _URL = mSharedPrefs.getString("portalURL", null);

        }else{
            _URL = mSharedPrefs.getString("mdportalURL", null);

        }*/

        _TOKEN = mSharedPrefs.getString("token", null);
        _licenseKey = mSharedPrefs.getString("licenseKey", null);
        _URL = "http://108.61.123.225/";
        //_licenseKey = "QMZ1Y46KD3";

    }
    public static String getToken(){

        try {
            String result = null;
            Response response=null;
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "Grant_type=password&username=Admin&password=passmenot");
            Request request = new Request.Builder()
                    .url("http://192.168.0.21/token")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            response = client.newCall(request).execute();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            result = responseBodyCopy.string();
            return  result;
        } catch (IOException e) {
            e.printStackTrace();
            return null ;
        }
    }



    public String Register( String s_fullname, String s_nationalid,String s_email, String s_mobileno, String s_password) {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("fullname", s_fullname)
                    .addFormDataPart("national_id", s_nationalid)
                    .addFormDataPart("email", s_email)
                    .addFormDataPart("phone", s_mobileno)
                    .addFormDataPart("password", s_password)
                    .addFormDataPart("password_confirm", s_password)
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"api/member")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("response", response.code());
            edit.commit();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();


        } catch (IOException ex) {
            ex.printStackTrace();
            String Server="-8080";
            Log.e("SoapApiRequest", ex.toString());
            Log.e("Server Response", Server);

            return Server;
        }
    }
    public String UpdateUser( String id,String s_fullname, String s_nationalid,String s_email, String s_mobileno, String s_password) {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("fullname", s_fullname)
                    .addFormDataPart("national_id", s_nationalid)
                    .addFormDataPart("email", s_email)
                    .addFormDataPart("phone", s_mobileno)
                    .addFormDataPart("password", s_password)
                    .addFormDataPart("password_confirm", s_password)
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"api/member/"+id)
                    .method("PUT", body)
                    .build();
            Response response = client.newCall(request).execute();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("response", response.code());
            edit.commit();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();


        } catch (IOException ex) {
            ex.printStackTrace();
            String Server="-8080";
            Log.e("SoapApiRequest", ex.toString());
            Log.e("Server Response", Server);

            return Server;
        }
    }
    public String verify (String id,String verification_code ) {

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("id", id)
                    .addFormDataPart("verification_code", verification_code)
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"api/member/verify")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("response", response.code());
            edit.commit();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("RestApiRequest", ex.toString());
            return ex.getMessage();
        }
    }
    public String Login(String Username,String password) {

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("membership_code", Username)
                    .addFormDataPart("email", Username)
                    .addFormDataPart("phone", Username)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"api/member/login")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("response", response.code());
            edit.commit();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("RestApiRequest", ex.toString());
            return ex.getMessage();
        }
    }
    public String ifExists (String email,String phone ,String membership_code) {

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("membership_code", membership_code)
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"api/member/exists")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("response", response.code());
            edit.commit();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("RestApiRequest", ex.toString());
            return ex.getMessage();
        }
    }
    public String CloseOutgrowersPurchasesBatch(int BatchIndex,String SignOffInfo) {

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, SignOffInfo);
            Request request = new Request.Builder()
                    .url(_URL+"SignOffBatch?Id="+BatchIndex)
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer "+_TOKEN)
                    .addHeader("Content-Type", "text/plain")
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();


        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("SoapApiRequest", ex.toString());
            return ex.getMessage();
        }
    }



    public String SendReceiptSMS(String ReceiptNo,String tKg) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(_URL+"Sendsms?Receipt="+ReceiptNo+"&Kg="+tKg)
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer "+_TOKEN)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("RestApiRequest", ex.toString());
            return ex.getMessage();
        }
    }


    public String postWeighment(String Id, String WeightInfo) {

        try {
            Log.i("ServerBatchID",Id+" WeightInfo "+WeightInfo);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, WeightInfo);
            Request request = new Request.Builder()
                    .url(_URL+"Weighment?Id="+Id)
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer "+_TOKEN)
                    .addHeader("Content-Type", "text/plain")
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",response.body().string());
            return responseBodyCopy.string();

        } catch (IOException var5) {
            Log.e("SoapOperations", "Error posting weighment");
            var5.printStackTrace();
            //var1 = (String)var3;
            Id = "-8080";;
            return Id;
        }


    }

    public String VerifyOutgrowerRecord(String verifyWeighment) {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(_URL+"Weighment?Info="+verifyWeighment)
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer "+_TOKEN)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            Log.i("RestApiRequest",responseBodyCopy.string());
            return response.body().string();

        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("RestApiRequest", ex.toString());
            String Server="-8080";
            return  ex.toString();
        }
    }



}
