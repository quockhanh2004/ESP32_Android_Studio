package com.example.pjmk;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Blynk {

    public static String getTokenFromSharedPreferences(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE);
//            System.out.println(sharedPreferences.getString("token", ""));
            return sharedPreferences.getString("token", "");
        } catch (Exception e) {
            // Xử lý ngoại lệ (nếu cần)
            System.out.println(e);
            return "0";
        }
    }
    private String TOKEN = "";
    public void getToken(Context context){
        TOKEN = "token=" + getTokenFromSharedPreferences(context);
    }

    private static final String BASE_URL = "https://blynk.cloud/external/api/";

    private static Blynk instance;
    private RequestQueue requestQueue;
    private final Context context;

    public interface DataCallback {
        void onSuccess(String data);
        void onError(String errorMessage);
    }
    public Blynk(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized Blynk getInstance(Context context) {
        if (instance == null) {
            instance = new Blynk(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void fetchData(int VNumber, final DataCallback callback) {
        String apiUrl = BASE_URL + "get?" + TOKEN + "&V" + VNumber;

        // Gửi dữ liệu thành công
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                callback::onSuccess,
                error -> {
                    // Xử lý lỗi
                    callback.onError(error.getMessage());
                });

        requestQueue.add(stringRequest);
    }

    public void sendData(int VNumber, String data) {
        String apiUrl = BASE_URL + "update?" + TOKEN + "&V" + VNumber + "=" + data;
        System.out.println(apiUrl);
        // Xử lý phản hồi từ server (nếu cần)
        // Xử lý lỗi (nếu có)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                System.out::println,
                System.out::println);

        requestQueue.add(stringRequest);
    }

    public void isOnline(final DataCallback callback) {
        String apiUrl = BASE_URL + "isHardwareConnected?" + TOKEN;
//        System.out.println(apiUrl);

        // Gửi dữ liệu thành công
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                callback::onSuccess,
                error -> {
                    // Xử lý lỗi
                    callback.onError(error.getMessage());
                });
        requestQueue.add(stringRequest);
    }

}
