package com.example.pc.pillgood;

import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    private OkHttpClient client;
    private Request request;
    private Callback callback;

    public void sendforautoCompletion(String pillName) {
        if (pillName == null) {
            pillName = "";
        }

        //String URL = "http://ec2-13-125-254-64.ap-northeast-2.compute.amazonaws.com/api/pill/find?item_name=" + pillName;

        client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host("ec2-13-125-254-64.ap-northeast-2.compute.amazonaws.com")
                        .addPathSegment("api/pill/find")
                        .addQueryParameter("item_name", pillName)
                        .build();

        request = new Request.Builder()
                    .url(url)
                    .build();
    }

//    public String sendforDetails(String pillName) {
//
//    }

    /**
     *
     * Method to get pill list that contains pill names
     * which is similar to the word user write down.
     *
     * */
    public List<String> receivePillList(String pRecvServerPage) {
        List<String> pillList = new ArrayList<>();
        final String[] resultReceived = new String[1];
        JSONObject obj = null;
        JSONArray pillData = null;
        int size;

        callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Callback Error", "fail to receive data");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resultReceived[0] = response.body().string();
            }
        };

        // convert string to json
        try {
            obj = new JSONObject(resultReceived[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // extract list
        try {
            pillData = obj.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // create pill list that contains similar pill name
        size = pillData.length();
        String pillName = null;
        for (int i=0; i<size; i++) {
            try {
                obj = pillData.getJSONObject(i);
                pillName = obj.getString("item_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            pillList.add(pillName);
        }

        return pillList;
    }
}
