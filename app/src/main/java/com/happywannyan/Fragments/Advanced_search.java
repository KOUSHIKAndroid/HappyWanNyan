package com.happywannyan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Adapter.Adapter_Petlist_Dialog;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.CustomRangeBar.RangeSeekBar;
import com.happywannyan.Events;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.PetService;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLocationProvider;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.LocationListener.MyLocalLocationManager;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.constants.LogType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Advanced_search extends Fragment implements AppLocationProvider.AddressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "basicdat";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "@@ ADV_SRC";
    RangeSeekBar<Long> seekBar;
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

    AlertDialog Dialog;
    ImageView IMG_SERVICE;
    SFNFTextView TXT_SERVICENAME;
    Adapter_Petlist_Dialog adapter_petlist;

    public Advanced_search() {
        // Required empty public constructor
    }

    public static Advanced_search newInstance(String param1, String param2) {
        Advanced_search fragment = new Advanced_search();
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

        TXT_petType = (SFNFTextView) view.findViewById(R.id.TXT_petType);

        try {
            if (mParam1.getJSONArray("allPetDetails").length() > 0) {
                TXT_petType.setText(mParam1.getJSONArray("allPetDetails").getJSONObject(0).getString("name"));
            }
            TXT_petType.setTag(mParam1.getJSONArray("allPetDetails").getJSONObject(0).getString("id"));

            Loger.MSG("mParam_allPetDetails", "" + mParam1.getJSONArray("allPetDetails").getJSONObject(0).getString("id"));

            new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "pet_type_info?pet_type_id=" + mParam1.getJSONArray("allPetDetails").getJSONObject(0).getString("id") + "&langid=" + AppContsnat.Language,
                    new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
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

                            }
                        }

                        @Override
                        public void OnError(String Error, String Response) {

                        }

                        @Override
                        public void OnError(String Error) {

                        }
                    });


        } catch (Exception e) {

        }


        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });
        LL_Calender = (LinearLayout) view.findViewById(R.id.LL_Calender);

        try {
            IMG_SERVICE = (ImageView) view.findViewById(R.id.IMG_SERVICE);
            TXT_SERVICENAME = (SFNFTextView) view.findViewById(R.id.TXT_SERVICENAME);

            Glide.with(getActivity()).load(mParam1.getString("selected_image")).into(IMG_SERVICE);
            (TXT_SERVICENAME).setText(mParam1.getString("name"));

            TXT_Loction.setText(mParam1.getString("LocationName"));

            if (mParam1.getString("LocationName").trim().length() > 0)
                IMG_erase_location.setVisibility(View.VISIBLE);

            if (EndDate.length() > 0)
                TXT_DateRange.setText(StartDate + "  to   " + EndDate);
            else
                TXT_DateRange.setText(StartDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }


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

                    new MYAlert(getActivity()).AlertTextLsit(getString(R.string.chose_pettype), mParam1.getJSONArray("allPetDetails"), "name"
                            , new MYAlert.OnSignleListTextSelected() {
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
                                        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "pet_type_info?&pet_type_id=" + jsonObject.getString("id") + "&langid=" + AppContsnat.Language,
                                                new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
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

                                                        }
                                                    }

                                                    @Override
                                                    public void OnError(String Error, String Response) {

                                                    }

                                                    @Override
                                                    public void OnError(String Error) {

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
                    GPS = true;
                    MyLocalLocationManager.setLogType(LogType.GENERAL);
                    ((BaseActivity) getActivity()).getLocation(new Events() {
                        @Override
                        public void UpdateLocation(Location location) {
                            Loger.MSG("@@@ LAT", "--" + location.getLatitude() + location.getLongitude());
                            new AppLocationProvider().OnGetAddress(getActivity(), location, Advanced_search.this);
                        }
                    });
                }

            }
        });
        view.findViewById(R.id.RL_Serach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIntentShow();
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
                    TXT_DateRange.setText(StartDate + "  to   " + EndDate);

                    break;
                case PLACE_AUTOCOMPLETE_REQUEST_CODE:
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
            TXT_Loction.setText(Adreess);
            IMG_erase_location.setVisibility(View.VISIBLE);
        }
    }


    public void dialogChooseCause() {

        final RecyclerView Rec_petlist_dailog;
        final ArrayList<PetService> ArrayPetService = new ArrayList<>();

        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_choose_cause, null);

        Rec_petlist_dailog = (RecyclerView) LayView.findViewById(R.id.Rec_petlist_dailog);
        Rec_petlist_dailog.setLayoutManager(new LinearLayoutManager(getActivity()));

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(getResources().getString(R.string.choose_cause_type));

        Button BTN_OK = (Button) LayView.findViewById(R.id.BTN_OK);
        Button BTN_CANCEL = (Button) LayView.findViewById(R.id.BTN_CANCEL);


        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object = new JSONObject(Result);

                    JSONArray PetService = object.getJSONArray("serviceCatList");
                    for (int i = 0; i < PetService.length(); i++) {
                        JSONObject OBJE = PetService.getJSONObject(i);
                        com.happywannyan.POJO.PetService petService = new PetService();
                        petService.setId(OBJE.getString("id"));
                        petService.setName(OBJE.getString("name"));
                        petService.setDefault_image(OBJE.getString("default_image"));
                        petService.setSelected_image(OBJE.getString("selected_image"));
                        petService.setTooltip_name(OBJE.getString("tooltip_name"));
                        petService.setJsondata(OBJE);
                        petService.setTick_value(false);

                        ArrayPetService.add(petService);
                    }

//                    ArrayPetService.get(0).setTick_value(true);

                    adapter_petlist = new Adapter_Petlist_Dialog(Advanced_search.this, getActivity(), ArrayPetService);
                    Rec_petlist_dailog.setAdapter(adapter_petlist);

                } catch (JSONException e) {

                }
            }

            @Override
            public void OnError(String Error, String Response) {

            }

            @Override
            public void OnError(String Error) {

            }
        });


        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < ArrayPetService.size(); i++) {

                    if (ArrayPetService.get(i).isTick_value()) {

                        Loger.MSG("position", "" + i);
                        Loger.MSG("isTick-", "" + ArrayPetService.get(i).getId());
                        Loger.MSG("name-", ArrayPetService.get(i).getName());
                        Loger.MSG("Selected_image-", ArrayPetService.get(i).getSelected_image());

                        refreshPage(
                                ArrayPetService.get(i).getId(),
                                ArrayPetService.get(i).getName(),
                                ArrayPetService.get(i).getSelected_image());
                        Dialog.dismiss();
                        break;
                    } else if (!ArrayPetService.get(i).isTick_value() && i == ArrayPetService.size() - 1) {
                        Toast.makeText(getActivity(), "Select at least one option", Toast.LENGTH_SHORT).show();
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

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }

    public void refreshPage(String id, String name, String ImgSelectedName) {

        LL_PetSizeValue.removeAllViews();
        LL_Pet_Age.removeAllViews();
        LL_OtherOption.removeAllViews();

        Glide.with(getActivity()).load(ImgSelectedName).into(IMG_SERVICE);
        (TXT_SERVICENAME).setText(name);

        try {
            new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "pet_type_info?pet_type_id=" + 1 + "&langid=" + AppContsnat.Language,
                    new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
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

                            }
                        }

                        @Override
                        public void OnError(String Error, String Response) {

                        }

                        @Override
                        public void OnError(String Error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextIntentShow() {

        Intent intent = new Intent(new Intent(getActivity(), SearchResult.class));
        try {
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
            data.put("value", mParam1.getString("id"));
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
            } else {
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

            }


            SEARCHPARAMS.put("keyinfo", Searchkeyinfor);

            intent.putExtra(SearchResult.SEARCHKEY, SEARCHPARAMS.toString());
            startActivityForResult(intent, 101);

        } catch (JSONException e) {
            Loger.Error(TAG, " " + e.getMessage());
        }
    }
}
