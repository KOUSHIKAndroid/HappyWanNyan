package com.happywannyan.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.POJO.APIPOSTDATA;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by apple on 19/05/17.
 */

public class JSONPerser {

    public static String ImageParamse="petimg";

        public interface  JSONRESPONSE{
            void OnSuccess( String Result);
            void OnError(String Error,String Response);
            void OnError(String Error);
        }


    public void API_FOR_GET(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse){

        Loger.MSG("URLGet",URL);

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            String PARAMS="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            if(apipostdata!=null && apipostdata.size()>0) {
                PARAMS="&";
                for (APIPOSTDATA data : apipostdata) {
                    PARAMS = PARAMS + data.getPARAMS() + "=" + data.getValues()+"&";
                }

                Loger.MSG("url", "" + URL + PARAMS);
            }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(5000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL+PARAMS).build();
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
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {


                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"",respose);
                        }
                    }catch (Exception e){}


                }else {
                    jsonresponse.OnSuccess(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void API_FOR_POST(final String URL, final ArrayList<APIPOSTDATA> apipostdata, final JSONRESPONSE jsonresponse){


        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Loger.MSG("@@ POST URL- ",URL);
                for(APIPOSTDATA data : apipostdata){
                    Loger.MSG("@@ POST PARAMS- ",""+data.getPARAMS()+" - "+data.getValues());
                    buildernew.addFormDataPart(""+data.getPARAMS(), data.getValues());
                }

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(6000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL) .method("POST", RequestBody.create(null, new byte[0]))
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
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {

                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"",respose);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    jsonresponse.OnError(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void API_FOR_POST_2(final String URL, final HashMap<String,String> apipostdata, final JSONRESPONSE jsonresponse){


        new AsyncTask<Void, Void, Void>() {
            private String respose = null;
            private Exception exception=null;
            MultipartBody.Builder buildernew;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Loger.MSG("@@ POST URL- ",URL);

                Iterator myVeryOwnIterator = apipostdata.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();
                    String value=(String)apipostdata.get(key);
                    Loger.MSG(key,value);
                    buildernew.addFormDataPart(key, value);
                }



            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {
                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60000, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
                        Request request = new Request.Builder().url(URL) .method("POST", RequestBody.create(null, new byte[0]))
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
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {

                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"",respose);
                        }
                    }catch (Exception e){}



                }else {
                    jsonresponse.OnError(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    public void API_FOR_With_Photo_POST( final String URL, final ArrayList<APIPOSTDATA> apipostdata, final ArrayList<File> Photos, final JSONRESPONSE jsonresponse){

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for(APIPOSTDATA data : apipostdata){
                    buildernew.addFormDataPart(""+data.getPARAMS(), data.getValues());
                }

                for (File file: Photos)
                {if(file!=null)
                    buildernew.addFormDataPart(""+ImageParamse, file.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, file));

                }


            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL) .method("POST", RequestBody.create(null, new byte[0]))
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
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {

                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"",respose);
                        }
                    }catch (Exception e){}



                }else {
                    jsonresponse.OnError(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void API_FOR_With_Photo_POST_2( final String URL, final HashMap<String,String> apipostdata, final HashMap<String,File> Photos, final JSONRESPONSE jsonresponse){

        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        new AsyncTask<Void, Void, Void>() {

            private String respose = null;
            private Exception exception=null;
            MultipartBody.Builder buildernew;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                Iterator myVeryOwnIterator = apipostdata.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();
                    String value=(String)apipostdata.get(key);
                    Loger.MSG(key,value);
                    buildernew.addFormDataPart(key, value);
                }

                myVeryOwnIterator = Photos.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key=(String)myVeryOwnIterator.next();
                    File value=(File) Photos.get(key);
                    {if(value!=null)
                        buildernew.addFormDataPart(""+key, value.getName() + "", RequestBody.create(MEDIA_TYPE_PNG, value.getName()));

                    }                }



            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!isCancelled()) {

                        MultipartBody requestBody = buildernew.build();
                        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60000, TimeUnit.MILLISECONDS).build();
                        Request request = new Request.Builder().url(URL) .method("POST", RequestBody.create(null, new byte[0]))
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
                    this.exception=e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isCancelled() && exception==null) {

                    try{
                        if(new JSONObject(respose).getBoolean("response"))
                        {
                            jsonresponse.OnSuccess(respose);
                        }else {
                            jsonresponse.OnError(new JSONObject(respose).getString("message")+"",respose);
                        }
                    }catch (Exception e){}



                }else {
                    jsonresponse.OnError(exception.getMessage()+"");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


public void GET_STRIPE_CUSTIMERID(final String StripeToken, final JSONRESPONSE jsonresponse)
{

    new AsyncTask<Void, Void, Void>() {

        private String respose = null;
        private Exception exception=null;
        MultipartBody.Builder buildernew;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!isCancelled()) {

//                    MultipartBody requestBody = buildernew.build();
                    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(6000, TimeUnit.MILLISECONDS).build();
                    Request request = new Request.Builder().url("https://api.stripe.com/v1/customers")
//                            .method("POST", RequestBody.create(null, new byte[0]))
                            .addHeader("authorization", "Bearer "+ AppContsnat.STRIPE_SECRATE_KEY)
                            .addHeader("source", ""+StripeToken)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
//                            .post(requestBody)
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
                this.exception=e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled() && exception==null) {
                        jsonresponse.OnSuccess(respose);

            }else {
                jsonresponse.OnError(exception.getMessage()+"");
            }
        }
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

}


}
