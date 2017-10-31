package com.happywannyan.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.BookingFragment;
import com.happywannyan.Fragments.ContactUsFragment;
import com.happywannyan.Fragments.FavouriteFragment;
import com.happywannyan.Fragments.HelpFragment;
import com.happywannyan.Fragments.MessageFragment;
import com.happywannyan.Fragments.MyPaymentsFragment;
import com.happywannyan.Fragments.MyPetsFragments;
import com.happywannyan.Fragments.MyProfileFragment;
import com.happywannyan.Fragments.PastSitterFragment;
import com.happywannyan.Fragments.SearchBasicFragment;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CircleTransform;
import com.happywannyan.Utils.ColorCircleDrawable;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.LocationListener.LocationBaseActivity;
import com.happywannyan.Utils.LocationListener.LocationConfiguration;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.constants.ProviderType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class BaseActivity extends LocationBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    AppLoader appLoader;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;

    ImageView UserImage;
    SFNFTextView txt_login_label ,UserName;

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(getClass().getName(), "Refreshed token: " + refreshedToken);
        new AppConstant(this);

        appLoader = new AppLoader(BaseActivity.this);

        HashMap<String, String> Params = new HashMap<>();
        Params.put("user_id", AppConstant.UserId);
        Params.put("anorid_device_id", refreshedToken + "");


        new CustomJSONParser().APIForPostMethod2(BaseActivity.this,AppConstant.BASEURL + "users_device_update", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {

            }

            @Override
            public void OnError(String Error, String Response) {

            }

            @Override
            public void OnError(String Error) {

            }
        });

        /*
        @Koushik
        Navigation Control respect of UserCredintial
         */
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        UserImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        UserName = (SFNFTextView) navigationView.getHeaderView(0).findViewById(R.id.TXT_UserName);
        txt_login_label = (SFNFTextView) navigationView.getHeaderView(0).findViewById(R.id.TXT_loginlabel);


        navigationView.findViewById(R.id.LL_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MessageFragment search_basic = new MessageFragment();
                fragmentTransaction.replace(R.id.Base_fargment_layout, search_basic);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_Switch_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MYAlert(BaseActivity.this).AlertOkCancel("", getResources().getString(R.string.want_to_change_language),
                        getResources().getString(R.string.ok),
                        getResources().getString(R.string.cancel),
                        new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);

                        sharedPreferences = getSharedPreferences("AutoLanguage", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        Configuration config = getBaseContext().getResources().getConfiguration();
                        Locale locale;

                        if (sharedPreferences.getString("shortLanguage", "NO").equalsIgnoreCase("en")) {
                            editor.putString("shortLanguage", "ja");
                            AppConstant.Language = "ja";
                            locale = new Locale("ja");
                        } else {
                            editor.putString("shortLanguage", "en");
                            AppConstant.Language = "en";
                            locale = new Locale("en");
                        }

                        editor.apply();
                        editor.commit();

                        Locale.setDefault(locale);
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        onConfigurationChanged(config);

                        ((SFNFTextView) findViewById(R.id.TXT_UserName)).setHint(getResources().getString(R.string.guest));
                        ((SFNFTextView) findViewById(R.id.TXT_loginlabel)).setHint(getResources().getString(R.string.login_signup));

                        ((SFNFTextView) findViewById(R.id.tv_nav_search)).setText(getResources().getString(R.string.search));
                        ((SFNFTextView) findViewById(R.id.tv_nav_message)).setText(getResources().getString(R.string.nav_messages));
                        ((SFNFTextView) findViewById(R.id.tv_nav_booking)).setText(getResources().getString(R.string.nav_booking));
                        ((SFNFTextView) findViewById(R.id.tv_nav_your_pet)).setText(getResources().getString(R.string.nav_yourpet));
                        ((SFNFTextView) findViewById(R.id.tv_nav_payment)).setText(getResources().getString(R.string.nav_payment));
                        ((SFNFTextView) findViewById(R.id.tv_nav_profile)).setText(getResources().getString(R.string.nav_profile));
                        ((SFNFTextView) findViewById(R.id.tv_nav_favoritesitter)).setText(getResources().getString(R.string.nav_favoritesitter));
                        ((SFNFTextView) findViewById(R.id.tv_nav_pastsitter)).setText(getResources().getString(R.string.nav_pastsitter));
                        ((SFNFTextView) findViewById(R.id.tv_nav_help)).setText(getResources().getString(R.string.nav_help));
                        ((SFNFTextView) findViewById(R.id.tv_about_us)).setText(getResources().getString(R.string.nav_contact_us));


                        if (AppConstant.Language.equals("en")) {
                            ((SFNFTextView) findViewById(R.id.tv_nav_switch_language)).setText("日本語");
                        } else {
                            ((SFNFTextView) findViewById(R.id.tv_nav_switch_language)).setText("English");
                        }


                        ((SFNFTextView) findViewById(R.id.tv_nav_logout)).setText(getResources().getString(R.string.nav_logout));


                        if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof MessageFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            MessageFragment search_basic = new MessageFragment();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, search_basic);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof SearchBasicFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            SearchBasicFragment search_basicFragment = new SearchBasicFragment();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, search_basicFragment);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof BookingFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, new BookingFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof PastSitterFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, new PastSitterFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof MyProfileFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, new MyProfileFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof MyPetsFragments) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, MyPetsFragments.newInstance(null, null));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof MyPaymentsFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, MyPaymentsFragment.newInstance(null, null));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof FavouriteFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, FavouriteFragment.newInstance(null, null));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof ContactUsFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, ContactUsFragment.newInstance(null, null));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if ((getSupportFragmentManager().findFragmentById(R.id.Base_fargment_layout)) instanceof HelpFragment) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.Base_fargment_layout, HelpFragment.newInstance(null, null));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    }
                    @Override
                    public void OnCancel() {

                    }
                });
            }
        });


        ////////////////Set Login User Details///////////Or Host///////////////////
        setProfileLoginUserDetails();
        ///////////////////END///////////////////////////////////////////////////

        navigationView.findViewById(R.id.LL_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, ContactUsFragment.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_Logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MYAlert(BaseActivity.this).AlertOkCancel(getResources().getString(R.string.nav_logout), getResources().getString(R.string.are_you_sure_want_to_log_out),
                        getResources().getString(R.string.ok),
                        getResources().getString(R.string.cancel),
                        new MYAlert.OnOkCancel() {
                    @Override
                    public void OnOk() {
                        appLoader.Show();
                        String URL = AppConstant.BASEURL + "app_logout?user_id=" + AppConstant.UserId + "&anorid_status=1";
                        new CustomJSONParser().APIForGetMethod(BaseActivity.this,URL, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                Loger.MSG("Result", Result);

                                try {
                                    if (new JSONObject(Result).getBoolean("response")) {

                                        AppConstant.UserEmail="";
                                        AppConstant.UserName="";

                                        new AppConstant(BaseActivity.this).logOutClearAllData();
                                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                        drawer.closeDrawer(GravityCompat.START);
                                        startActivity(new Intent(BaseActivity.this, LoginChooserActivity.class));
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                    }

                    @Override
                    public void OnCancel() {

                    }
                });

            }
        });


        navigationView.findViewById(R.id.LL_Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                SearchBasicFragment search_basicFragment = new SearchBasicFragment();
                fragmentTransaction.replace(R.id.Base_fargment_layout, search_basicFragment);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_Booking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(BaseActivity.this,BookingOneActivity.class));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, new BookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        navigationView.findViewById(R.id.LL_Past_Favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(BaseActivity.this,BookingOneActivity.class));
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, new PastSitterFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        navigationView.findViewById(R.id.LL_Help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, HelpFragment.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                // startActivity(new Intent(BaseActivity.this, HelpActivity.class));
            }
        });


        navigationView.findViewById(R.id.LL_Profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, new MyProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_yourPets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, MyPetsFragments.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_Payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, MyPaymentsFragment.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        navigationView.findViewById(R.id.LL_Favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, FavouriteFragment.newInstance(null, null));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        if (getIntent().getExtras() != null) {
            //do here
            AppConstant.go_to = getIntent().getStringExtra("go_to");
            AppConstant.message_object_string = getIntent().getStringExtra("object");
            if (AppConstant.go_to.trim().equals("message_all")) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MessageFragment search_basic = new MessageFragment();
                fragmentTransaction.replace(R.id.Base_fargment_layout, search_basic);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, new BookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        } else {

            if(AppConstant.login_status.equals("1")){
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                SearchBasicFragment search_basicFragment = new SearchBasicFragment();
                fragmentTransaction.add(R.id.Base_fargment_layout, search_basicFragment);
                fragmentTransaction.commit();
            }else {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Base_fargment_layout, new MyProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        if (AppConstant.Language.equals("en")) {
            ((SFNFTextView) findViewById(R.id.tv_nav_switch_language)).setText("日本語");
        } else {
            ((SFNFTextView) findViewById(R.id.tv_nav_switch_language)).setText("English");
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Menu_Drawer() {
        /////////////////Set Login User Details in Left Drawable///////////////////
        setProfileLoginUserDetails();
        ////////////////////////////////////END///////////////////////////////

        SharedPreferences pref = getSharedPreferences("unread_msg_count", MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Loger.MSG("menu_open_message_count-->", "" + pref.getInt("count", 0));

        if (pref.getInt("count", 0) == 0) {
            Loger.MSG("count", "zero");
            ((SFNFTextView) findViewById(R.id.TXT_MSG_STATUS_NAV)).setVisibility(View.GONE);
            ((SFNFTextView) findViewById(R.id.TXT_MSG_STATUS_NAV)).setText(String.valueOf(0));
        } else {
            Loger.MSG("count", "more than zero");
            ((SFNFTextView) findViewById(R.id.TXT_MSG_STATUS_NAV)).setBackground(new ColorCircleDrawable(ResourcesCompat.getColor(getResources(), R.color.colorBtnRed, null)));
            findViewById(R.id.TXT_MSG_STATUS_NAV).setVisibility(View.VISIBLE);
            ((SFNFTextView) findViewById(R.id.TXT_MSG_STATUS_NAV)).setText(String.valueOf(pref.getInt("count", 0)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.openDrawer(navigationView);

        editor.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                LocationRequest();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        } else {
//            locationManager.requestLocationUpdates(provider, INTERVAL, 1, this);
        }
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {

        return new LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage(getResources().getString(R.string.would_you_mind_to_turn_gps_on))
                .setRationalMessage(getResources().getString(R.string.give_the_permission));
//        return null;
    }

    @Override
    public void onLocationFailed(int failType) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Loger.MSG("@@@ LAT", "" + location.getLatitude() + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Loger.MSG("@@ Provider enable", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Loger.MSG("@@ Provider Disable", s);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setProfileLoginUserDetails(){
        new AppConstant(BaseActivity.this).getShareData(AppDataHolder.UserData, new AppDataHolder.AppSharePreferenceDataInterface() {
            @Override
            public void available(boolean available, JSONObject data) {
                try {
                    Loger.MSG("@@ DADAD", "" + data);

                    if(!data.getJSONObject("info_array").getString("image_path").trim().equals("")){
                        Glide.with(BaseActivity.this).load(data.getJSONObject("info_array").getString("image_path").trim()).placeholder(R.drawable.ic_user).transform(new CircleTransform(BaseActivity.this)).into(UserImage);
                    }

                    UserName.setText(data.getJSONObject("info_array").getString("firstname")+" "+data.getJSONObject("info_array").getString("lastname"));
                    txt_login_label.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                navigationView.getHeaderView(0).findViewById(R.id.RL_HEADER).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);

                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.Base_fargment_layout, new MyProfileFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }

            @Override
            public void notAvailable(String Error) {
                navigationView.findViewById(R.id.LL_message).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Booking).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_yourPets).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Payment).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Profile).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Logout).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Favorite).setVisibility(View.GONE);
                navigationView.findViewById(R.id.LL_Past_Favorite).setVisibility(View.GONE);

                navigationView.getHeaderView(0).findViewById(R.id.RL_HEADER).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(BaseActivity.this, LoginChooserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
