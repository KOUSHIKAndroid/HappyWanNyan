package com.happywannyan.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.helper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by su on 6/20/17.
 */

public class AppLocationProvider {

    public interface AddressListener {
        void OnAdresss(String Adreess, JSONObject geo);
    }


    public static boolean GPS(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void OnGetAddress(Context context, final Location location, final AddressListener addressListener) {
        if (NetworkUtil.getInstance().isNetworkAvailable(context)) {
            new AsyncTask<Void, Void, String>() {

                private String respose = null;
                private Exception exception = null;
                String PARAMS = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    PARAMS = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location.getLatitude() + "," + location.getLongitude() + "&key=AIzaSyDAS-0Wh-K3QII2h7DgO8bd-f1dSy4lW3M" + "&language=" + AppConstant.Language;
                }

                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        if (!isCancelled()) {

                            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(6000, TimeUnit.MILLISECONDS).build();
                            Request request = new Request.Builder().url(PARAMS).build();
                            Response response = client.newCall(request).execute();

                            respose = response.body().string();
                            Loger.MSG("response", "respose_::" + respose);
                            Loger.MSG("response", "respose_ww_message::" + response.message());
                            Loger.MSG("response", "respose_ww_headers::" + response.headers());
                            Loger.MSG("response", "respose_ww_isRedirect::" + response.isRedirect());
//                       Loger.MSG("response", "respose_ww_body::" + response.body().string());
                            return respose;

                        }
                    } catch (Exception e) {
                        this.exception = e;
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String aVoid) {
                    super.onPostExecute(aVoid);
                    if (!isCancelled() && exception == null) {
                        try {
                            JSONObject jsonObject = new JSONObject(aVoid);
                            JSONArray array = jsonObject.getJSONArray("results");
                            if (array.length() > 0)
                                addressListener.OnAdresss(array.getJSONObject(0).getString("formatted_address"), array.getJSONObject(0).getJSONObject("geometry"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Loger.MSG("NetworkFail-->", "Yes");
            Toast.makeText(context, context.getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
