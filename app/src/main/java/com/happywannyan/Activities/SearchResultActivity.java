package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.SearchListFragment;
import com.happywannyan.Fragments.SearchMapFragment;
import com.happywannyan.Fragments.SearchTinderFragment;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetSearchData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {

    public static final String SEARCHKEY = "@Data";
    private static final String TAG = "@@ SRCH_RESULT";

    FragmentTransaction fragmentTransaction;

    public ArrayList<SetGetSearchData> ListARRY;
    JSONObject SearchKeys;
    public double ne_lng, ne_lat, sw_lng, sw_lat;
    AppLoader appLoader;
    ImageView fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        appLoader = new AppLoader(this);

        fab = (ImageView) findViewById(R.id.fab);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Loger.MSG(getClass().getName(), "Refreshed token: " + refreshedToken);


        HashMap<String, String> Params = new HashMap<>();
        Params.put("user_id", AppConstant.UserId);
        Params.put("anorid_device_id", refreshedToken + "");

        Loger.MSG("@user_id-->",AppConstant.UserId);
        Loger.MSG("@anorid_device_id-->",refreshedToken + "");

        new CustomJSONParser().APIForPostMethod2(SearchResultActivity.this,AppConstant.BASEURL + "users_device_update", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                Loger.MSG("@userDevice","Updated");


            }

            @Override
            public void OnError(String Error, String Response) {

            }

            @Override
            public void OnError(String Error) {
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(SearchResultActivity.this,Error,Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (AppConstant.alwaysRedirectAfterLogin) {
            try {
                JSONObject SEARCHPARAMS = new JSONObject();
                    /*
                     @@ Make JSONARRY for Next Page Serach
                     */

                JSONArray Searchkeyinfor = new JSONArray();
                JSONObject data = new JSONObject();
                data.put("name", "start_date");
                data.put("value", "");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "end_date");
                data.put("value", "");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "serviceCat");
                data.put("value", "3");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "pet_type");
                data.put("value", "1");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "high_price");
                data.put("value", "");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "low_price");
                data.put("value", "");
                Searchkeyinfor.put(data);


                data = new JSONObject();
                data.put("name", "srch_lon");
                data.put("value", "139.691706");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "srch_lat");
                data.put("value", "35.689487");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "ne_lng");
                data.put("value", "139.910202");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "ne_lat");
                data.put("value", "35.817813");
                Searchkeyinfor.put(data);


                data = new JSONObject();
                data.put("name", "sw_lng");
                data.put("value", "139.510574");
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "sw_lat");
                data.put("value", "35.528873");
                Searchkeyinfor.put(data);


                SEARCHPARAMS.put("LocationName", "Tokyo");
                SEARCHPARAMS.put("Address", "");
                SEARCHPARAMS.put("keyinfo", Searchkeyinfor);

                SearchKeys = SEARCHPARAMS;
                ((SFNFTextView) findViewById(R.id.PAGE_Titile)).setText(SearchKeys.getString("LocationName"));
                Loger.MSG("@@ SEARCH KEY", SearchKeys.toString());

            } catch (JSONException e) {
                Loger.Error("@@", "Error" + e.getMessage());
            }

        } else {
            try {
                SearchKeys = new JSONObject(getIntent().getStringExtra(SEARCHKEY));
                ((SFNFTextView) findViewById(R.id.PAGE_Titile)).setText(SearchKeys.getString("LocationName"));
                Loger.MSG("@@ SEARCH KEY", SearchKeys.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        ListARRY = new ArrayList<>();

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.onBackPressCheckerForBaseActivity=true;
                onBackPressed();
            }
        });


        SearchLoading();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Container_result, new SearchMapFragment());
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
                ((ImageView) findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
                findViewById(R.id.fab).setVisibility(View.GONE);
                findViewById(R.id.list).setVisibility(View.GONE);
                findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.fab_plus)).setTag("1");
            }
        });

        findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Container_result, new SearchListFragment());
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
                ((ImageView) findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
                findViewById(R.id.fab).setVisibility(View.GONE);
                findViewById(R.id.list).setVisibility(View.GONE);
                findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.fab_plus)).setTag("1");
            }
        });
        findViewById(R.id.IMG_Tinderr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Container_result, new SearchTinderFragment());
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
                ((ImageView) findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
                findViewById(R.id.fab).setVisibility(View.GONE);
                findViewById(R.id.list).setVisibility(View.GONE);
                findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.fab_plus)).setTag("1");
            }
        });

        findViewById(R.id.IMG_Filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConstant.alwaysRedirectAfterLogin) {
                    AppConstant.alwaysRedirectAfterLogin = false;
                    try {
                        JSONObject latalng = new JSONObject();
                        JSONObject SearchJSONSitter = new JSONObject();

                        latalng.put("lat", "35.689487");
                        latalng.put("lng", "139.691706");

                        JSONObject ViewPort = new JSONObject();
                        ViewPort.put("southwest_LAT", "35.528873");
                        ViewPort.put("southwest_LNG", "139.510574");

                        ViewPort.put("northeast_LAT", "35.817813");
                        ViewPort.put("northeast_LNG", "139.910202");

                        SearchJSONSitter.put("LocationName", "Tokyo");
                        SearchJSONSitter.put("latlng", latalng);
                        SearchJSONSitter.put("viewport", ViewPort);
                        SearchJSONSitter.put("Address", "Tokyo");
                        SearchJSONSitter.put("StartDate", "");
                        SearchJSONSitter.put("EndDate", "");

                        Intent intent = new Intent(SearchResultActivity.this, BaseActivity.class);
                        AppConstant.SearchJSONSitter=SearchJSONSitter.toString();
                        startActivity(intent);
                        finish();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    public void SearchLoading() {

        appLoader.Show();

        ArrayList<SetGetAPIPostData> PostData = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        PostData.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("langid");
        setGetAPIPostData.setValues(AppConstant.Language);
        PostData.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        PostData.add(setGetAPIPostData);


        try {
            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("search_location");
            setGetAPIPostData.setValues(SearchKeys.getString("LocationName"));
            PostData.add(setGetAPIPostData);

            for (int i = 0; i < SearchKeys.getJSONArray("keyinfo").length(); i++) {
                JSONObject object = SearchKeys.getJSONArray("keyinfo").getJSONObject(i);
                setGetAPIPostData = new SetGetAPIPostData();
                setGetAPIPostData.setPARAMS(object.getString("name"));
                setGetAPIPostData.setValues(object.getString("value"));
                PostData.add(setGetAPIPostData);
                if (object.getString("name").equals("ne_lng"))
                    ne_lng = Double.parseDouble(object.getString("value"));
                if (object.getString("name").equals("ne_lat"))
                    ne_lat = Double.parseDouble(object.getString("value"));
                if (object.getString("name").equals("sw_lng"))
                    sw_lng = Double.parseDouble(object.getString("value"));
                if (object.getString("name").equals("sw_lat"))
                    sw_lat = Double.parseDouble(object.getString("value"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new CustomJSONParser().APIForPostMethod(SearchResultActivity.this, AppConstant.BASEURL + "search_setter", PostData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                try {
                    findViewById(R.id.Container_result).setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_empty).setVisibility(View.GONE);

                    JSONObject object = new JSONObject(Result);
                    JSONArray ARRA = object.getJSONArray("results");

                    for (int i = 0; i < ARRA.length(); i++) {
                        JSONObject jjj = ARRA.getJSONObject(i);
                        SetGetSearchData setGetSearchData = new SetGetSearchData();
                        setGetSearchData.setSearcItem(jjj);

                        ListARRY.add(setGetSearchData);
                    }
                    if (AppConstant.alwaysRedirectAfterLogin) {
                        fab.performClick();
                    } else {
                        fragmentTransaction.replace(R.id.Container_result, new SearchListFragment());
                        fragmentTransaction.disallowAddToBackStack();
                        fragmentTransaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    if (jsonObject.getInt("next_data") == 0 && jsonObject.getInt("start_form") == 0) {

                        findViewById(R.id.Container_result).setVisibility(View.GONE);
                        findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);

//                        new MYAlert(SearchResultActivity.this).AlertOnly(getResources().getString(R.string.app_name), Error, new MYAlert.OnlyMessage() {
//                            @Override
//                            public void OnOk(boolean res) {
//
//                            }
//                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                    Toast.makeText(SearchResultActivity.this, Error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (AppConstant.alwaysRedirectAfterLogin) {
            Intent intent = new Intent(SearchResultActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
            AppConstant.alwaysRedirectAfterLogin = false;
        } else {
            Loger.MSG("AdvanceSearchBack-->",""+"Yes");
            Intent intent = new Intent();
            setResult(RESULT_FIRST_USER, intent);
            finish();
        }
    }
}
