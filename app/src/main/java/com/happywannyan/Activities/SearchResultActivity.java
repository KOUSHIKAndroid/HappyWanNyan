package com.happywannyan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    public static final String SEARCHKEY = "@Data";
    private static final String TAG = "@@ SRCH_RESULT";

    FragmentTransaction fragmentTransaction;

    public ArrayList<SetGetSearchData> ListARRY;
    JSONObject SearchKeys;
    public double ne_lng, ne_lat, sw_lng, sw_lat;
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        appLoader = new AppLoader(this);


        try {
            SearchKeys = new JSONObject(getIntent().getStringExtra(SEARCHKEY));
            ((SFNFTextView) findViewById(R.id.PAGE_Titile)).setText(SearchKeys.getString("LocationName"));
            Loger.MSG("@@ SEARCH KEY", SearchKeys.toString());

        } catch (JSONException e) {
        } catch (Exception e) {
        }


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ListARRY = new ArrayList<>();

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_FIRST_USER, intent);
                finish();
            }
        });


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


        new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "search_setter", PostData, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                try {

                    JSONObject object = new JSONObject(Result);
                    JSONArray ARRA = object.getJSONArray("results");

                    for (int i = 0; i < ARRA.length(); i++) {
                        JSONObject jjj = ARRA.getJSONObject(i);
                        SetGetSearchData setGetSearchData = new SetGetSearchData();
                        setGetSearchData.setSearcItem(jjj);

                        ListARRY.add(setGetSearchData);

                    }


                    fragmentTransaction.replace(R.id.Container_result, new SearchListFragment());
                    fragmentTransaction.disallowAddToBackStack();
                    fragmentTransaction.commit();


                } catch (Exception e) {

                }

            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    if (jsonObject.getInt("next_data") == 0 && jsonObject.getInt("start_form") == 0) {
                        new MYAlert(SearchResultActivity.this).AlertOnly(getResources().getString(R.string.app_name), Error, new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {

                            }
                        });
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void OnError(String Error) {

                appLoader.Dismiss();
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
