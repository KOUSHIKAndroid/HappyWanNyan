package com.happywannyan.Utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by apple on 19/05/17.
 */

public class CustomJSONParser {

    public static String ImageParamse = "petimg";

    public interface JSONRESPONSE {
        void OnSuccess(String Result);

        void OnError(String Error, String Response);

        void OnError(String Error);
    }


    public void API_FOR_GET(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse) {

        Loger.MSG("URLGet", URL);

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception = null;
            String PARAMS = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (apipostdata != null && apipostdata.size() > 0) {
                    PARAMS = "&";
                    for (APIPOSTDATA data : apipostdata) {
                        PARAMS = PARAMS + data.getPARAMS() + "=" + data.getValues() + "&";
                    }

                    Loger.MSG("url", "" + URL + PARAMS);
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL + PARAMS).build();
                        Response response = client.newCall(request).execute();

                        respose = response.body().string();
                        new JSONObject(respose);

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {


                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                    }


                } else {
                    jsonresponse.OnSuccess(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void API_FOR_POST(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse) {

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception = null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Loger.MSG("@@ POST URL- ", URL);
                for (APIPOSTDATA data : apipostdata) {
                    Loger.MSG("@@ POST PARAMS- ", "" + data.getPARAMS() + " - " + data.getValues());
                    buildernew.addFormDataPart("" + data.getPARAMS(), data.getValues());
                }

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL).method("POST", RequestBody.create(null, new byte[0]))
                                .post(requestBody).build();
                        Response response = client.newCall(request).execute();

                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {

                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void API_FOR_POST_2(final String URL, final HashMap<String, String> apipostdata, final JSONRESPONSE jsonresponse) {


        new AsyncTask<Void, Void, Void>() {
            private String respose = null;
            private Exception exception = null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Loger.MSG("@@ POST URL- ", URL);

                Iterator myVeryOwnIterator = apipostdata.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = (String) apipostdata.get(key);
                    Loger.MSG(key, value);
                    buildernew.addFormDataPart(key, value);
                }


            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        Request request = new Request.Builder().url(URL).method("POST", RequestBody.create(null, new byte[0]))
                                .post(requestBody).build();
                        Response response = client.newCall(request).execute();

                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {

                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void API_FOR_With_Photo_POST(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final ArrayList<File> Photos, final JSONRESPONSE jsonresponse) {

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception = null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (APIPOSTDATA data : apipostdata) {
                    buildernew.addFormDataPart("" + data.getPARAMS(), data.getValues());
                }

                for (File file : Photos) {
                    if (file != null)
                        buildernew.addFormDataPart("" + ImageParamse, file.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, file));
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL).method("POST", RequestBody.create(null, new byte[0]))
                                .post(requestBody).build();
                        Response response = client.newCall(request).execute();


                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {

                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                    }


                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void API_FOR_With_Photo_POST_2(final String URL, final HashMap<String, String> apipostdata, final HashMap<String, File> Photos, final JSONRESPONSE jsonresponse) {

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception = null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Iterator myVeryOwnIterator = apipostdata.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = (String) apipostdata.get(key);
                    Loger.MSG(key, value);
                    buildernew.addFormDataPart(key, value);
                }

                myVeryOwnIterator = Photos.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    File value = (File) Photos.get(key);
                    {
                        if (value != null)
                            buildernew.addFormDataPart("" + key, value.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, value.getName()));

                    }
                }


            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL).method("POST", RequestBody.create(null, new byte[0]))
                                .post(requestBody).build();
                        Response response = client.newCall(request).execute();


                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {

                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void GET_STRIPE_CUSTIMERID(final String StripeToken, final JSONRESPONSE jsonresponse) {

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MINUTES).build();
                        Request request = new Request.Builder().url("https://api.stripe.com/v1/customers")
                                .addHeader("authorization", "Bearer " + AppContsnat.STRIPE_SECRATE_KEY)
                                .addHeader("source", "" + StripeToken)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .get()
                                .build();
                        Response response = client.newCall(request).execute();


                        respose = response.body().string();

                        Loger.MSG("response", "respose_::" + respose);
                        Loger.MSG("response", "respose_ww_message::" + response.message());
                        Loger.MSG("response", "respose_ww_headers::" + response.headers());
                        Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                    }
                } catch (Exception e) {
                    this.exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception == null) {
                    jsonresponse.OnSuccess(respose);

                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    public void postDataUsingHttp(final String URL, final ArrayList<APIPOSTDATA> apiPostData,final JSONRESPONSE jsonresponse){

       new AsyncTask<String, Void, String>() {

           private String respose = null;
           private Exception exception = null;

            protected void onPreExecute(){

            }

            protected String doInBackground(String... arg0) {

                try{
                    URL url = new URL(URL);
                    JSONObject postDataParams = new JSONObject();

                    for (int j=0;j<apiPostData.size();j++){
                        postDataParams.put(apiPostData.get(j).getPARAMS(),apiPostData.get(j).getValues());
                    }


                    Log.e("params",postDataParams.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode=conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";

                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }

                        in.close();
                        respose=sb.toString();
                        return sb.toString();

                    }
                    else {
                        return ""+responseCode;
                    }
                }
                catch(Exception e){
                    this.exception=e;
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                if (!isCancelled() && exception == null && !result.equalsIgnoreCase("200")) {

                    try {
                        if (new JSONObject(respose).getBoolean("response")) {
                            jsonresponse.OnSuccess(respose);
                        } else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message") + "", respose);
                        }
                    } catch (Exception e) {
                    }


                } else {
                    jsonresponse.OnError(exception.getMessage() + "");
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
