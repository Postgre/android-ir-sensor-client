package es.alvaroweb.ircamerareader;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public class HttpConnection {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final AResponse aResponse;

    public HttpConnection(AResponse aResponse){
        this.aResponse = aResponse;
    }


    public void getCamsInfo(String url) throws Exception {
        Request request = new Request.Builder()
                .url("http://" + url + "/cams")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                CamerasInfo camerasInfo = gson.fromJson(response.body().charStream(), CamerasInfo.class);
                aResponse.getResponse(camerasInfo);

                System.out.println(response.body().string());
            }
        });
    }

    public interface AResponse{
        void getResponse(CamerasInfo camerasInfo);
    }
}
