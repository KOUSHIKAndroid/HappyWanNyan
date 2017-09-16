package com.happywannyan.Activities.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.happywannyan.SitterBooking.BookingOneActivity;
import com.happywannyan.Activities.profile.fragmentPagerAdapter.ProfileFragPagerAdapter;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.provider.RatingColor;
import com.happywannyan.Utils.provider.AppTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by bodhidipta on 22/05/17.
 */

public class ProfileDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private ProfileFragPagerAdapter pagerAdapter = null;
    private LinearLayout reservation = null;
    public JSONObject PrevJSON;
    RatingBar Rating;
    String SitterId;
    String UserData;
    AppLoader appLoader;
    public String JSONRESPONSESTRING;
    int block_user_status = 0;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        appLoader = new AppLoader(this);
        appLoader.Show();
        Rating = (RatingBar) findViewById(R.id.Rating);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        reservation = (LinearLayout) findViewById(R.id.reservation);

        try {
            new AppContsnat(this);
            UserData = AppContsnat.UserId;
            this.PrevJSON = new JSONObject(getIntent().getStringExtra("data"));
            Loger.MSG("@@@ PROFILE DATA ", "" + PrevJSON);
            if (PrevJSON.has("sitter_user_id")) {
                SitterId = PrevJSON.getString("sitter_user_id");
            } else if (PrevJSON.has("sitter_users_id")) {
                SitterId = PrevJSON.getString("sitter_users_id");
            } else {
                SitterId = PrevJSON.getString("id");
            }

            findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f", Double.parseDouble(PrevJSON.getString("lat")), Double.parseDouble(PrevJSON.getString("long")), 17, Double.parseDouble(PrevJSON.getString("lat")), Double.parseDouble(PrevJSON.getString("long")));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String Data = getIntent().getStringExtra("data");
        Loger.MSG("@@ Profile", " Intent Data- " + Data);

        ArrayList<APIPOSTDATA> Paramas = new ArrayList<>();
        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("sitter_user_id");
        apipostdata.setValues(SitterId);
//        apipostdata.setValues(""+3);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(UserData);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("langid");
        apipostdata.setValues(AppContsnat.Language);
        Paramas.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_timezone");
        apipostdata.setValues(AppTimeZone.GetTimeZone());
        Paramas.add(apipostdata);


        new CustomJSONParser().API_FOR_POST(AppContsnat.BASEURL + "app_users_sitterinfo", Paramas, new CustomJSONParser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                JSONRESPONSESTRING = Result;
                Loger.MSG("@@ SITTER", "- " + Result);
                try {
                    final JSONObject BasicInfo = new JSONObject(Result).getJSONObject("info_array").getJSONObject("basic_info");
                    block_user_status = BasicInfo.getInt("block_user_status");
                    Glide.with(ProfileDetailsActivity.this).load(BasicInfo.getString("sittersimage")).into((ImageView) findViewById(R.id.IMG_Profile));
                    Rating.setRating(Float.parseFloat(BasicInfo.getString("ave_rating")));
                    Rating.setIsIndicator(true);
                    LayerDrawable stars = (LayerDrawable) Rating.getProgressDrawable();
                    RatingColor.SETRatingColor(stars);
                    ((SFNFTextView) findViewById(R.id.UserName)).setText(BasicInfo.getString("nickname"));
                    ((SFNFTextView) findViewById(R.id.Bussinessname)).setText(BasicInfo.getString("businessname"));
                    ((SFNFTextView) findViewById(R.id.Location)).setText(BasicInfo.getString("place_sitter"));
                    if (BasicInfo.getInt("favourite_status") == 0) {

//                        ((ImageView)findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.ic_favorite_border);
                        ((ImageView) findViewById(R.id.IMG_FAV)).setTag("0");
                        ((ImageView) findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.profile_ic_favorite_white);


                    } else {
//                        ((ImageView) findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.profile_ic_favorite_blue);
                        ((ImageView) findViewById(R.id.IMG_FAV)).setTag("1");
                        ((ImageView) findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.profile_ic_favorite_blue);
                    }


                    ((SFNFTextView) findViewById(R.id.ReviewNo)).setText(BasicInfo.getString("no_of_review") + " " + getResources().getString(R.string.review));
                    findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f", Double.parseDouble(BasicInfo.getString("lat")), Double.parseDouble(BasicInfo.getString("long")), 17, Double.parseDouble(BasicInfo.getString("lat")), Double.parseDouble(BasicInfo.getString("long")));
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pagerAdapter = new ProfileFragPagerAdapter(getSupportFragmentManager());
                viewpager.setAdapter(pagerAdapter);
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });

        findViewById(R.id.Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = getLayoutInflater();
                View popupView = layoutInflater.inflate(R.layout.profile_menu, null);
                popupWindow = new PopupWindow(popupView, ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);

                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(findViewById(R.id.ancher));
            }
        });


        viewpager.setAdapter(pagerAdapter);
        findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((View) findViewById(R.id.div1)).setBackgroundColor(Color.TRANSPARENT);
                ((View) findViewById(R.id.div2)).setBackgroundColor(Color.TRANSPARENT);
                ((View) findViewById(R.id.div3)).setBackgroundColor(Color.TRANSPARENT);
                ((View) findViewById(R.id.div4)).setBackgroundColor(Color.TRANSPARENT);
                reservation.setVisibility(View.VISIBLE);
                ((SFNFTextView) findViewById(R.id.TXTab1)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView) findViewById(R.id.TXTab2)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView) findViewById(R.id.TXTab3)).setTextColor(Color.parseColor("#666565"));
                ((SFNFTextView) findViewById(R.id.TXTab4)).setTextColor(Color.parseColor("#666565"));

                switch (position) {
                    case 0:
                        ((View) findViewById(R.id.div1)).setBackgroundColor(Color.parseColor("#bf3e49"));
                        ((SFNFTextView) findViewById(R.id.TXTab1)).setTextColor(Color.BLACK);

                        break;
                    case 1:
                        ((View) findViewById(R.id.div2)).setBackgroundColor(Color.parseColor("#bf3e49"));
                        ((SFNFTextView) findViewById(R.id.TXTab2)).setTextColor(Color.BLACK);
                        reservation.setVisibility(View.GONE);
                        break;
                    case 2:
                        ((View) findViewById(R.id.div3)).setBackgroundColor(Color.parseColor("#bf3e49"));
                        ((SFNFTextView) findViewById(R.id.TXTab3)).setTextColor(Color.BLACK);
                        break;
                    case 3:
                        ((View) findViewById(R.id.div4)).setBackgroundColor(Color.parseColor("#bf3e49"));
                        ((SFNFTextView) findViewById(R.id.TXTab4)).setTextColor(Color.BLACK);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (block_user_status == 0) {
                    Intent intent = new Intent(ProfileDetailsActivity.this, BookingOneActivity.class);
                    try {
                        intent.putExtra("LIST", "" + new JSONObject(JSONRESPONSESTRING).getJSONObject("info_array").getJSONArray("servicelist"));
                        intent.putExtra("ItemDetails", "" + PrevJSON);
                        intent.putExtra("Single", false);
                        JSONArray ARRYA = new JSONObject(JSONRESPONSESTRING).getJSONObject("info_array").getJSONArray("servicelist");
                        for (int j = 0; j < ARRYA.length(); j++) {
                            JSONObject OBJECT = ARRYA.getJSONObject(j);
                            if (OBJECT.getString("service_id").equals(PrevJSON.getString("manage_service_id"))) {
                                intent.putExtra("SELECT", "" + OBJECT);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    new MYAlert(ProfileDetailsActivity.this).AlertOnly(getResources().getString(R.string.request_reservation), getResources().getString(R.string.unable_to_make_reservation_request), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {

                        }
                    });
                }
            }
        });

        ((SFNFTextView) findViewById(R.id.TXTab1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(0);
            }
        });
        ((SFNFTextView) findViewById(R.id.TXTab2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        });
        ((SFNFTextView) findViewById(R.id.TXTab3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(2);
            }
        });
        ((SFNFTextView) findViewById(R.id.TXTab4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(3);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ID_MitUp:
                popupWindow.dismiss();
                Intent inten = new Intent(this, MeetUpWannyanActivity.class);
                inten.putExtra("DATA", SitterId);
                startActivity(inten);
                break;
            case R.id.Contact:
                popupWindow.dismiss();
                inten = new Intent(this, ContactMsgActivity.class);
                inten.putExtra("DATA", SitterId);
                startActivity(inten);
                break;
            case R.id.IMG_FAV:
                String IDtemp = "";
                if (((ImageView) findViewById(R.id.IMG_FAV)).getTag().equals("1"))
                    IDtemp = "0";
                else
                    IDtemp = "1";


                new CustomJSONParser().API_FOR_GET(AppContsnat.BASEURL + "app_favourite_sitters?user_id=" + AppContsnat.UserId + "" +
                        "&sitter_user_id=" + SitterId + "&fav_status=" + IDtemp, new ArrayList<APIPOSTDATA>(), new CustomJSONParser.JSONRESPONSE() {
                    @Override
                    public void OnSuccess(String Result) {
                        try {
                            JSONObject jsonObject = new JSONObject(Result);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            if (((ImageView) findViewById(R.id.IMG_FAV)).getTag().equals("1")) {
                                ((ImageView) findViewById(R.id.IMG_FAV)).setTag("0");
                                ((ImageView) findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.profile_ic_favorite_white);
                            } else {
                                ((ImageView) findViewById(R.id.IMG_FAV)).setTag("1");
                                ((ImageView) findViewById(R.id.IMG_FAV)).setImageResource(R.drawable.profile_ic_favorite_blue);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error, String Response) {
                        try {
                            JSONObject jsonObject = new JSONObject(Response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void OnError(String Error) {

                    }
                });
        }
    }
}
