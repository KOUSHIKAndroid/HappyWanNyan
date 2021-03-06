package com.happywannyan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Activities.SearchResultActivity;
import com.happywannyan.Adapter.PetListDialogAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.CustomRangeBar.RangeSeekBar;
import com.happywannyan.Events;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetPetService;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.AppLocationProvider;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.LocationListener.MyLocalLocationManager;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.constants.LogType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AdvancedSearchFragment extends Fragment implements AppLocationProvider.AddressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "basicdat";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "@@ ADV_SRC";
    RangeSeekBar<Long> seekBar;
    AppLoader appLoader;
    // TODO: Rename and change types of parameters
    private JSONObject mParam1;
    private JSONObject Pet_size_age;
    private String mParam2;
    LinearLayout LL_PriceRange;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int CALL_CALENDER = 12;
    SFNFTextView TXT_Loction, TXT_DateRange, TXT_highestRange;
    SFNFTextView TXT_petType;
    SFNFTextView TXT_LoestRange;
    private String StartDate = "";
    private String EndDate = "";
    LinearLayout LL_Calender, LL_PetSizeValue, LL_Pet_Age, LL_OtherOption;
    Place place = null;
    String MaxPrice = "", MinPrice = "";
    String LowPrice = "", HighPrice = "";
    ImageView IMG_erase_location, IMG_Location;
    boolean GPS = false;
    RelativeLayout RL_Search;
    ScrollView SCROLLL;

    AlertDialog Dialog;
    ImageView IMG_SERVICE;
    SFNFTextView TXT_SERVICENAME;
    PetListDialogAdapter adapter_petlist;

    ArrayList<SetGetPetService> arraySetGetPetService;

    JSONObject AllPetJsonObject = null;
    JSONObject Geo;

    public AdvancedSearchFragment() {
        // Required empty public constructor
    }

    public static AdvancedSearchFragment newInstance(String param1, String param2) {
        AdvancedSearchFragment fragment = new AdvancedSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {

                Loger.MSG("mParam1",""+getArguments().getString(ARG_PARAM1));
                mParam1 = new JSONObject(getArguments().getString(ARG_PARAM1));

                Loger.MSG("@@ PARAM", mParam1.toString());

                if (mParam1.has("StartDate")) {
                    StartDate = mParam1.getString("StartDate");
                }
                if (mParam1.has("EndDate")) {
                    EndDate = mParam1.getString("EndDate");
                }

//                StartDate = mParam1.getString("StartDate");
//                EndDate = mParam1.getString("EndDate");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_search, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LL_PriceRange = (LinearLayout) view.findViewById(R.id.LL_PriceRange);
        LL_PetSizeValue = (LinearLayout) view.findViewById(R.id.LL_PetSizeValue);
        LL_Pet_Age = (LinearLayout) view.findViewById(R.id.LL_Pet_Age);
        LL_OtherOption = (LinearLayout) view.findViewById(R.id.LL_OtherOption);
        TXT_Loction = (SFNFTextView) view.findViewById(R.id.TXT_Loction);
        TXT_DateRange = (SFNFTextView) view.findViewById(R.id.TXT_DateRange);
        TXT_LoestRange = (SFNFTextView) view.findViewById(R.id.TXT_LoestRange);
        TXT_highestRange = (SFNFTextView) view.findViewById(R.id.TXT_highestRange);
        IMG_Location = (ImageView) view.findViewById(R.id.ImgMyLocation);
        IMG_erase_location = (ImageView) view.findViewById(R.id.IMG_erase_location);
        RL_Search = (RelativeLayout) view.findViewById(R.id.RL_Search);
        SCROLLL = (ScrollView) view.findViewById(R.id.SCROLLL);
        TXT_petType = (SFNFTextView) view.findViewById(R.id.TXT_petType);
        IMG_SERVICE = (ImageView) view.findViewById(R.id.IMG_SERVICE);
        TXT_SERVICENAME = (SFNFTextView) view.findViewById(R.id.TXT_SERVICENAME);

        appLoader = new AppLoader(getActivity());

        arraySetGetPetService = new ArrayList<>();

        try {
            appLoader.Show();

            new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "parent_service?langid=en&user_id=" + AppConstant.UserId, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                @Override
                public void OnSuccess(String Result) {
                    try {
                        JSONObject object = new JSONObject(Result);

                        AllPetJsonObject = object;

                        JSONArray PetService = object.getJSONArray("serviceCatList");
                        for (int i = 0; i < PetService.length(); i++) {
                            JSONObject OBJE = PetService.getJSONObject(i);
                            SetGetPetService setGetPetService = new SetGetPetService();
                            setGetPetService.setId(OBJE.getString("id"));
                            setGetPetService.setName(OBJE.getString("name"));
                            setGetPetService.setDefault_image(OBJE.getString("default_image"));
                            setGetPetService.setSelected_image(OBJE.getString("selected_image"));
                            setGetPetService.setTooltip_name(OBJE.getString("tooltip_name"));
                            setGetPetService.setJsondata(OBJE);
                            setGetPetService.setTick_value(false);

                            arraySetGetPetService.add(setGetPetService);
                        }
//                    arraySetGetPetService.get(0).setTick_value(true);

                        if (AllPetJsonObject.getJSONArray("allPetDetails").length() > 0) {
                            TXT_petType.setText(AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("name"));
                            TXT_petType.setTag(AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("id"));
                            Loger.MSG("mParam_allPetDetails", "" + AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("id"));
                        }


                        if (!AllPetJsonObject.getJSONArray("serviceCatList").getJSONObject(0).getString("selected_image").trim().equals("")) {
                            Glide.with(getActivity()).load(AllPetJsonObject.getJSONArray("serviceCatList").getJSONObject(0).getString("selected_image")).into(IMG_SERVICE);
                        }
                        (TXT_SERVICENAME).setText(AllPetJsonObject.getJSONArray("serviceCatList").getJSONObject(0).getString("name"));
                        TXT_SERVICENAME.setTag(AllPetJsonObject.getJSONArray("serviceCatList").getJSONObject(0).getString("id"));


                        TXT_Loction.setText(mParam1.getString("LocationName"));

                        if (mParam1.getString("LocationName").trim().length() > 0)
                            IMG_erase_location.setVisibility(View.VISIBLE);

                        if (EndDate.length() > 0)
                            TXT_DateRange.setText(StartDate + "  to   " + EndDate);
                        else
                            TXT_DateRange.setText(StartDate);


                        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "pet_type_info?pet_type_id=" + AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("id") + "&langid=" + AppConstant.Language, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                try {
                                    appLoader.Dismiss();

                                    Pet_size_age = new JSONObject(Result);
                                    MaxPrice = Pet_size_age.getString("max_price_default").split("\\.")[0];
                                    MinPrice = Pet_size_age.getString("min_price_default").split("\\.")[0];
                                    TXT_LoestRange.setText(MinPrice);
                                    TXT_highestRange.setText(MaxPrice);
                                    if (Pet_size_age.getJSONArray("petSizeDet").length() > 0) {
                                        for (int j = 0; j < Pet_size_age.getJSONArray("petSizeDet").length(); j++) {
                                            CheckBox Chkbox = new CheckBox(getActivity());
                                            Chkbox.setText(Pet_size_age.getJSONArray("petSizeDet").getJSONObject(j).getString("option_name"));
                                            LL_PetSizeValue.addView(Chkbox);
                                        }
                                    }

                                    if (Pet_size_age.getJSONArray("petAgeDet").length() > 0)
                                        for (int i = 0; i < Pet_size_age.getJSONArray("petAgeDet").length(); i++) {
                                            CheckBox Chkbox = new CheckBox(getActivity());
                                            Chkbox.setText(Pet_size_age.getJSONArray("petAgeDet").getJSONObject(i).getString("option_name"));
                                            LL_Pet_Age.addView(Chkbox);
                                        }

                                    if (Pet_size_age.getJSONArray("exp_medical_opt").length() > 0)
                                        for (int i = 0; i < Pet_size_age.getJSONArray("exp_medical_opt").length(); i++) {
                                            CheckBox Chkbox = new CheckBox(getActivity());
                                            Chkbox.setText(Pet_size_age.getJSONArray("exp_medical_opt").getJSONObject(i).getString("option_name"));
                                            LL_OtherOption.addView(Chkbox);
                                        }

                                    LL_PriceRange.removeAllViewsInLayout();
                                    seekBar = new RangeSeekBar<Long>(Long.parseLong(MinPrice), Long.parseLong(MaxPrice), getContext());
                                    LL_PriceRange.addView(seekBar);
                                    seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Long>() {
                                        @Override
                                        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long maxValue) {
                                            LowPrice = minValue.toString();
                                            HighPrice = maxValue.toString();

                                            TXT_LoestRange.setText(minValue.toString());
                                            TXT_highestRange.setText(maxValue.toString());
                                        }

                                        @Override
                                        public void onRangeSeekBarValuesChanging(RangeSeekBar<?> bar, int minValue, int maxValue) {
//                min.setText(minValue + "");
//                max.setText(maxValue + "");
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    appLoader.Dismiss();
                                }
                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                            }

                            @Override
                            public void OnError(String Error) {
                                appLoader.Dismiss();
                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                        appLoader.Dismiss();
                    }
                }

                @Override
                public void OnError(String Error, String Response) {
                    appLoader.Dismiss();
                }

                @Override
                public void OnError(String Error) {
                    appLoader.Dismiss();
                    if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                        Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });
        LL_Calender = (LinearLayout) view.findViewById(R.id.LL_Calender);

        view.findViewById(R.id.RL_Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(getActivity()), PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }

                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("JP")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                                    .build(getActivity());

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Loger.MSG("@@ SERVICE", " " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Loger.MSG("@@ SERVICE", " 2 " + e.getMessage());
                }
            }
        });

        view.findViewById(R.id.RL_Service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Loger.MSG("onBackPressed", "onBackPressed");
                //getActivity().onBackPressed();
                dialogChooseCause();
            }
        });

        view.findViewById(R.id.RL_petType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    new MYAlert(getActivity()).AlertTextLsit(getString(R.string.chose_pettype), AllPetJsonObject.getJSONArray("allPetDetails"), "name"
                            , new MYAlert.OnSingleListTextSelected() {
                                @Override
                                public void OnSelectedTEXT(JSONObject jsonObject) {
                                    Loger.MSG("@@ SelevetPet", "" + jsonObject);
                                    try {
                                        TXT_petType.setText(jsonObject.getString("name"));
                                        TXT_petType.setTag(jsonObject.getString("id"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "pet_type_info?&pet_type_id=" + jsonObject.getString("id") + "&langid=" + AppConstant.Language,
                                                new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                                                    @Override
                                                    public void OnSuccess(String Result) {
                                                        try {
                                                            Pet_size_age = new JSONObject(Result);
                                                            MaxPrice = Pet_size_age.getString("max_price_default").split("\\.")[0];
                                                            MinPrice = Pet_size_age.getString("min_price_default").split("\\.")[0];
                                                            TXT_LoestRange.setText(MinPrice);
                                                            TXT_highestRange.setText(MaxPrice);
                                                            LL_PetSizeValue.removeAllViews();
                                                            LL_Pet_Age.removeAllViews();
                                                            LL_OtherOption.removeAllViews();

                                                            if (Pet_size_age.getJSONArray("petSizeDet").length() > 0) {
                                                                for (int j = 0; j < Pet_size_age.getJSONArray("petSizeDet").length(); j++) {
                                                                    CheckBox Chkbox = new CheckBox(getActivity());
                                                                    Chkbox.setText(Pet_size_age.getJSONArray("petSizeDet").getJSONObject(j).getString("option_name"));
                                                                    LL_PetSizeValue.addView(Chkbox);
                                                                }
                                                            }

                                                            if (Pet_size_age.getJSONArray("petAgeDet").length() > 0)
                                                                for (int i = 0; i < Pet_size_age.getJSONArray("petAgeDet").length(); i++) {
                                                                    CheckBox Chkbox = new CheckBox(getActivity());
                                                                    Chkbox.setText(Pet_size_age.getJSONArray("petAgeDet").getJSONObject(i).getString("option_name"));
                                                                    LL_Pet_Age.addView(Chkbox);
                                                                }

                                                            if (Pet_size_age.getJSONArray("exp_medical_opt").length() > 0)
                                                                for (int i = 0; i < Pet_size_age.getJSONArray("exp_medical_opt").length(); i++) {
                                                                    CheckBox Chkbox = new CheckBox(getActivity());
                                                                    Chkbox.setText(Pet_size_age.getJSONArray("exp_medical_opt").getJSONObject(i).getString("option_name"));
                                                                    LL_OtherOption.addView(Chkbox);
                                                                }

                                                            LL_PriceRange.removeAllViewsInLayout();
                                                            seekBar = new RangeSeekBar<Long>(Long.parseLong(MinPrice), Long.parseLong(MaxPrice), getContext());
                                                            LL_PriceRange.addView(seekBar);
                                                            seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Long>() {
                                                                @Override
                                                                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long maxValue) {
                                                                    LowPrice = minValue.toString();
                                                                    HighPrice = maxValue.toString();
                                                                    TXT_LoestRange.setText(minValue.toString());
                                                                    TXT_highestRange.setText(maxValue.toString());
                                                                }

                                                                @Override
                                                                public void onRangeSeekBarValuesChanging(RangeSeekBar<?> bar, int minValue, int maxValue) {
//                min.setText(minValue + "");
//                max.setText(maxValue + "");
                                                                }

                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void OnError(String Error, String Response) {

                                                    }

                                                    @Override
                                                    public void OnError(String Error) {
                                                        if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                                                            Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        LL_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CalenderActivity.class), CALL_CALENDER);
            }
        });

        view.findViewById(R.id.IMG_erase_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXT_Loction.setText("");
                GPS = false;
                IMG_erase_location.setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.ImgMyLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TXT_Loction.getText().toString().trim().equals("")) {
                    TXT_Loction.setText("");
                    GPS = false;
                    IMG_erase_location.setVisibility(View.GONE);
                } else {
                    mParam1 = null;
                    place = null;
                    GPS = true;
                    MyLocalLocationManager.setLogType(LogType.GENERAL);
                    ((BaseActivity) getActivity()).getLocation(new Events() {
                        @Override
                        public void UpdateLocation(Location location) {
                            Loger.MSG("@@@ LAT", "--" + location.getLatitude() + location.getLongitude());
                            new AppLocationProvider().OnGetAddress(getActivity(), location, AdvancedSearchFragment.this);
                        }
                    });
                }
            }
        });
        view.findViewById(R.id.RL_Serach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TXT_petType.getText().toString().trim().equals("")) {
                    TXT_petType.setHintTextColor(Color.RED);
                    SCROLLL.scrollTo(0, TXT_petType.getBottom());
                } else {
                    nextIntentShow();
                }
            }
        });

        view.findViewById(R.id.IMG_Filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntentShow();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CALL_CALENDER:
                    StartDate = data.getStringExtra("startdate");
                    EndDate = data.getStringExtra("enddate");

                    Loger.MSG("@@ START", StartDate);
                    Loger.MSG("@@ END", EndDate);
                    TXT_DateRange.setText(StartDate + " " + getActivity().getResources().getString(R.string.to) + " " + EndDate);

                    break;
                case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    mParam1 = null;
                    Geo = null;
                    place = PlacePicker.getPlace(getActivity(), data);
                    Loger.MSG("@@ PLACE", "" + place.getLatLng());
                    Loger.MSG("@@ PLACE", "- " + place.getName());
                    GPS = false;
                    String Location = "" + place.getName();
                    TXT_Loction.setText(Location);
                    break;
            }

        } else if (requestCode == 101) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void OnAdresss(String Adreess, JSONObject geo) {
        if (GPS) {
            Loger.MSG("Address-->", Adreess);
            TXT_Loction.setText(Adreess);
            IMG_erase_location.setVisibility(View.VISIBLE);
            this.Geo = geo;
        }
    }

    public void dialogChooseCause() {

        final RecyclerView Rec_petlist_dailog;

        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_choose_cause, null);

        Rec_petlist_dailog = (RecyclerView) LayView.findViewById(R.id.Rec_petlist_dailog);
        Rec_petlist_dailog.setLayoutManager(new LinearLayoutManager(getActivity()));

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(getResources().getString(R.string.choose_cause_type));

        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);
        Button BTN_CANCEL = (Button) LayView.findViewById(R.id.BTN_CANCEL);

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();

        adapter_petlist = new PetListDialogAdapter(AdvancedSearchFragment.this, getActivity(), arraySetGetPetService);
        Rec_petlist_dailog.setAdapter(adapter_petlist);

        Dialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < arraySetGetPetService.size(); i++) {

                    if (arraySetGetPetService.get(i).isTick_value()) {

                        Loger.MSG("position", "" + i);
                        Loger.MSG("isTick-", "" + arraySetGetPetService.get(i).getId());
                        Loger.MSG("name-", arraySetGetPetService.get(i).getName());
                        Loger.MSG("Selected_image-", arraySetGetPetService.get(i).getSelected_image());

                        refreshPage(
                                arraySetGetPetService.get(i).getId(),
                                arraySetGetPetService.get(i).getName(),
                                arraySetGetPetService.get(i).getSelected_image());
                        Dialog.dismiss();
                        break;
                    } else if (!arraySetGetPetService.get(i).isTick_value() && i == arraySetGetPetService.size() - 1) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.select_at_least_one_pet_service), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        BTN_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });
    }

    public void refreshPage(String id, String name, String ImgSelectedName) {

        try {
            if (AllPetJsonObject.getJSONArray("allPetDetails").length() > 0) {
                TXT_petType.setText(AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("name"));
                TXT_petType.setTag(AllPetJsonObject.getJSONArray("allPetDetails").getJSONObject(0).getString("id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        LL_PetSizeValue.removeAllViews();
        LL_Pet_Age.removeAllViews();
        LL_OtherOption.removeAllViews();

        if (!ImgSelectedName.trim().equals("")) {
            Glide.with(getActivity()).load(ImgSelectedName.trim()).into(IMG_SERVICE);
        }
        (TXT_SERVICENAME).setText(name);
        (TXT_SERVICENAME).setTag(id);

        try {
            new CustomJSONParser().APIForGetMethod(getActivity(), AppConstant.BASEURL + "pet_type_info?pet_type_id=" + 1 + "&langid=" + AppConstant.Language,
                    new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                        @Override
                        public void OnSuccess(String Result) {
                            try {
                                Pet_size_age = new JSONObject(Result);
                                MaxPrice = Pet_size_age.getString("max_price_default").split("\\.")[0];
                                MinPrice = Pet_size_age.getString("min_price_default").split("\\.")[0];
                                TXT_LoestRange.setText(MinPrice);
                                TXT_highestRange.setText(MaxPrice);
                                if (Pet_size_age.getJSONArray("petSizeDet").length() > 0) {
                                    for (int j = 0; j < Pet_size_age.getJSONArray("petSizeDet").length(); j++) {
                                        CheckBox Chkbox = new CheckBox(getActivity());
                                        Chkbox.setText(Pet_size_age.getJSONArray("petSizeDet").getJSONObject(j).getString("option_name"));
                                        LL_PetSizeValue.addView(Chkbox);
                                    }
                                }

                                if (Pet_size_age.getJSONArray("petAgeDet").length() > 0)
                                    for (int i = 0; i < Pet_size_age.getJSONArray("petAgeDet").length(); i++) {
                                        CheckBox Chkbox = new CheckBox(getActivity());
                                        Chkbox.setText(Pet_size_age.getJSONArray("petAgeDet").getJSONObject(i).getString("option_name"));
                                        LL_Pet_Age.addView(Chkbox);
                                    }


                                if (Pet_size_age.getJSONArray("exp_medical_opt").length() > 0)
                                    for (int i = 0; i < Pet_size_age.getJSONArray("exp_medical_opt").length(); i++) {
                                        CheckBox Chkbox = new CheckBox(getActivity());
                                        Chkbox.setText(Pet_size_age.getJSONArray("exp_medical_opt").getJSONObject(i).getString("option_name"));
                                        LL_OtherOption.addView(Chkbox);
                                    }

                                LL_PriceRange.removeAllViewsInLayout();
                                seekBar = new RangeSeekBar<Long>(Long.parseLong(MinPrice), Long.parseLong(MaxPrice), getContext());
                                LL_PriceRange.addView(seekBar);
                                seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Long>() {
                                    @Override
                                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long maxValue) {
                                        LowPrice = minValue.toString();
                                        HighPrice = maxValue.toString();

                                        TXT_LoestRange.setText(minValue.toString());
                                        TXT_highestRange.setText(maxValue.toString());
                                    }

                                    @Override
                                    public void onRangeSeekBarValuesChanging(RangeSeekBar<?> bar, int minValue, int maxValue) {
//                min.setText(minValue + "");
//                max.setText(maxValue + "");
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void OnError(String Error, String Response) {

                        }

                        @Override
                        public void OnError(String Error) {
                            if (Error.equalsIgnoreCase(getActivity().getResources().getString(R.string.please_check_your_internet_connection))) {
                                Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextIntentShow() {

        Intent intent = new Intent(new Intent(getActivity(), SearchResultActivity.class));
        try {

            if (TXT_Loction.getText().toString().trim().equals("")) {
                TXT_Loction.setHintTextColor(Color.RED);
            } else {

                JSONObject SEARCHPARAMS = new JSONObject();

                    /*
                     @@ Make JSONARRY for Next Page Serach
                     */

                JSONArray Searchkeyinfor = new JSONArray();
                JSONObject data = new JSONObject();
                data.put("name", "start_date");
                data.put("value", StartDate);
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "end_date");
                data.put("value", EndDate);
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "serviceCat");
                data.put("value", "" + TXT_SERVICENAME.getTag());
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "pet_type");
                data.put("value", TXT_petType.getTag());
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "high_price");
                data.put("value", HighPrice);
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "low_price");
                data.put("value", LowPrice);
                Searchkeyinfor.put(data);


                if (place != null) {

                    data = new JSONObject();
                    data.put("name", "srch_lon");
                    data.put("value", place.getLatLng().longitude);
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "srch_lat");
                    data.put("value", place.getLatLng().latitude);
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lng");
                    data.put("value", place.getViewport().northeast.longitude);
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lat");
                    data.put("value", place.getViewport().northeast.latitude);
                    Searchkeyinfor.put(data);


                    data = new JSONObject();
                    data.put("name", "sw_lng");
                    data.put("value", place.getViewport().southwest.longitude);
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "sw_lat");
                    data.put("value", place.getViewport().southwest.latitude);
                    Searchkeyinfor.put(data);


                    SEARCHPARAMS.put("LocationName", place.getName());

                    SEARCHPARAMS.put("Address", place.getAddress());
                } else if (mParam1 != null) {
                    data = new JSONObject();
                    data.put("name", "srch_lon");
                    data.put("value", mParam1.getJSONObject("latlng").getString("lng"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "srch_lat");
                    data.put("value", mParam1.getJSONObject("latlng").getString("lat"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lng");
                    data.put("value", mParam1.getJSONObject("viewport").getString("northeast_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lat");
                    data.put("value", mParam1.getJSONObject("viewport").getString("northeast_LAT"));
                    Searchkeyinfor.put(data);


                    data = new JSONObject();
                    data.put("name", "sw_lng");
                    data.put("value", mParam1.getJSONObject("viewport").getString("southwest_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "sw_lat");
                    data.put("value", mParam1.getJSONObject("viewport").getString("southwest_LAT"));
                    Searchkeyinfor.put(data);

                    SEARCHPARAMS.put("Address", mParam1.getString("Address"));
                    SEARCHPARAMS.put("LocationName", mParam1.getString("LocationName"));

                } else {

                    data = new JSONObject();
                    data.put("name", "srch_lon");
                    data.put("value", Geo.getJSONObject("location").getString("lng"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "srch_lat");
                    data.put("value", Geo.getJSONObject("location").getString("lat"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lng");
                    data.put("value", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lng") + "");
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lat");
                    data.put("value", Geo.getJSONObject("viewport").getJSONObject("northeast").getString("lat") + "");
                    Searchkeyinfor.put(data);


                    data = new JSONObject();
                    data.put("name", "sw_lng");
                    data.put("value", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lng") + "");
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "sw_lat");
                    data.put("value", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lat") + "");
                    Searchkeyinfor.put(data);

                    SEARCHPARAMS.put("Address", TXT_Loction.getText());
                    SEARCHPARAMS.put("LocationName", TXT_Loction.getText());


                }


                SEARCHPARAMS.put("keyinfo", Searchkeyinfor);

                intent.putExtra(SearchResultActivity.SEARCHKEY, SEARCHPARAMS.toString());
                startActivityForResult(intent, 101);
            }

        } catch (JSONException e) {
            Loger.Error(TAG, " " + e.getMessage());
        }
    }
}
