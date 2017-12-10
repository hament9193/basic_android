package com.droidrank.sample.models;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hamentchoudhary on 24/11/17.
 */

public class FetchImageTask extends AsyncTask<Integer, Void, Response> {

    private final Context mContext;
    private final OnLoadListener mListener;

    public FetchImageTask(Context context, OnLoadListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Response doInBackground(Integer... params) {
        BufferedReader reader = null;
        Integer offset = params[0];
        try {

            URL myUrl = new URL("http://hackertest.wynk.in/fetch.php?offset="+offset);

            HttpURLConnection conn = (HttpURLConnection) myUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            int statusCode = conn.getResponseCode();
            if (statusCode != 200){
                return null;
            }

            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            String response = buffer.toString();

            return deserializeServiceResponse(response);

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);

        if (response != null) {
            mListener.onLoadComplete(response);
        }
    }

    private Response deserializeServiceResponse(String response){

        Response serviceResponse = null;

        try {
            JSONObject responseJson = new JSONObject(response);

            String success = responseJson.getString("success");
            String message = responseJson.getString("message");
            JSONArray imagesJson = responseJson.getJSONArray("images");

            ArrayList<ImageModel> images = new ArrayList<>();

            for (int i = 0; i < imagesJson.length(); i++) {
                JSONObject imageJson = imagesJson.getJSONObject(i);
                String imageUrl = imageJson.getString("imageUrl");
                String imageDescription = imageJson.getString("imageDescription");

                ImageModel imageModel = new ImageModel(imageUrl, imageDescription);
                images.add(imageModel);
            }

            serviceResponse = new Response(images, success, message);
        } catch (JSONException e){
            e.printStackTrace();

            mListener.onError();
        }

        return serviceResponse;
    }

    public interface OnLoadListener {

        void onLoadComplete(Response response);

        void onError();

    }
}