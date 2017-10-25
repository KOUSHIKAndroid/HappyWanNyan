package com.happywannyan.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.CalenderActivity;
import com.happywannyan.Activities.SearchResultActivity;
import com.happywannyan.Adapter.PetListAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Events;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetPetService;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLocationProvider;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.LocationListener.MyLocalLocationManager;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.constants.LogType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class SearchBasicFragment extends Fragment implements AppLocationProvider.AddressListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int CALL_CALENDER = 12;
    SFNFTextView TXT_Loction, TXT_DateRange;
    private String StartDate = "";
    private String EndDate = "";
    LinearLayout LL_Calender;
    LinearLayout LL_PetServiceList;
    RecyclerView Rec_petlist;
    ImageView IMG_erase_location;
    ArrayList<SetGetPetService> arraySetGetPetService;
    PetListAdapter adapter_petList;
    Place place;
    boolean GPS = false;
    JSONObject JSONFULLDATA, Geo;
    JSONObject SearchJSONSitter;

    public SearchBasicFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SearchBasicFragment newInstance(String param1, String param2) {
        SearchBasicFragment fragment = new SearchBasicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arraySetGetPetService = new ArrayList<>();

        TXT_Loction = (SFNFTextView) view.findViewById(R.id.TXT_Loction);
        TXT_DateRange = (SFNFTextView) view.findViewById(R.id.TXT_DateRange);
        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });
        LL_Calender = (LinearLayout) view.findViewById(R.id.LL_Calender);
        LL_PetServiceList = (LinearLayout) view.findViewById(R.id.LL_PetServiceList);
        LL_PetServiceList.setVisibility(View.VISIBLE);
        Rec_petlist = (RecyclerView) view.findViewById(R.id.Rec_petlist);
        IMG_erase_location = (ImageView) view.findViewById(R.id.IMG_erase_location);
        Rec_petlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "parent_service?langid="+AppConstant.Language+"&user_id=" + AppConstant.UserId, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object = new JSONObject(Result);
                    JSONFULLDATA = object;

                    SharedPreferences pref = getActivity().getSharedPreferences("unread_msg_count", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    Loger.MSG("total_message_count", "" + object.getInt("total_message_count"));
                    editor.putInt("count", object.getInt("total_message_count"));
                    editor.apply();
                    editor.commit();
                    Loger.MSG("message_count", "" + pref.getInt("count", 0));


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

                    adapter_petList = new PetListAdapter(SearchBasicFragment.this, getActivity(), arraySetGetPetService);
                    Rec_petlist.setAdapter(adapter_petList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error, String Response) {

            }

            @Override
            public void OnError(String Error) {

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
//                IMG_erase_location.setImageResource(R.drawable.ic_my_location_white);
//                for (SetGetPetService setGetPetService : arraySetGetPetService)
//                    setGetPetService.setTick_value(false);
//                arraySetGetPetService.get(0).setTick_value(true);
//                adapter_petList.notifyDataSetChanged();
            }
        });


        view.findViewById(R.id.RL_Location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        view.findViewById(R.id.ImgMyLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TXT_Loction.getText().toString().trim().equals("")) {
                    TXT_Loction.setText("");
                    GPS = false;
                    IMG_erase_location.setVisibility(View.GONE);
                    for (SetGetPetService setGetPetService : arraySetGetPetService)
                        setGetPetService.setTick_value(false);
                    arraySetGetPetService.get(0).setTick_value(true);
                    adapter_petList.notifyDataSetChanged();
//                    ((ImageView) view.findViewById(R.id.ImgMyLocation)).setImageResource(R.drawable.ic_my_location_white);
                } else {
                    GPS = true;
                    place=null;
                    MyLocalLocationManager.setLogType(LogType.GENERAL);
                    ((BaseActivity) getActivity()).getLocation(new Events() {
                        @Override
                        public void UpdateLocation(Location location) {
                            Loger.MSG("@@@ LAT/LNG", "-->" + location.getLatitude() +"/"+ location.getLongitude());
                            new AppLocationProvider().OnGetAddress(((BaseActivity) getActivity()), location, SearchBasicFragment.this);
                        }
                    });
                }

            }
        });


        if (AppLocationProvider.GPS(getActivity())) {
            GPS=true;
            Loger.MSG("## " + getClass().getName(), " Yewsssss");
            MyLocalLocationManager.setLogType(LogType.GENERAL);
            ((BaseActivity) getActivity()).getLocation(new Events() {
                @Override
                public void UpdateLocation(Location location) {
                    Loger.MSG("@@@ LAT", "--" + location.getLatitude() + location.getLongitude());
                    new AppLocationProvider().OnGetAddress(getActivity(), location, SearchBasicFragment.this);

                }
            });
        } else {
            Loger.MSG("## " + getClass().getName(), " Noooo");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_basic, container, false);
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
                    if (EndDate.length() > 0)
                        TXT_DateRange.setText(StartDate + " " +getResources().getString(R.string.to)+" "+ EndDate);
                    else
                        TXT_DateRange.setText(StartDate);

                    break;
                case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    place = PlacePicker.getPlace(getActivity(), data);
                    Loger.MSG("@@ PLACE", "" + place.getLatLng());
                    Loger.MSG("@@ ViewPosrt", "- " + place.getViewport().toString());
                    GPS = false;
                    String Location = "" + place.getName();
                    TXT_Loction.setText(Location);
                    LL_PetServiceList.setVisibility(View.VISIBLE);
                    IMG_erase_location.setVisibility(View.VISIBLE);

                    searchAndIntent();

                    break;
                case 101:
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.Base_fargment_layout, AdvancedSearchFragment.newInstance(SearchJSONSitter.toString(), null));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
            }
        }
    }

    public void GotoAdvancedSearched(JSONObject jsondata) {
        SearchJSONSitter = jsondata;
        searchAndIntent();
    }

    @Override
    public void OnAdresss(String Address, JSONObject geo) {
        if (GPS) {
            Loger.MSG("Address-->",Address);
            TXT_Loction.setText(Address);
            IMG_erase_location.setVisibility(View.VISIBLE);
            LL_PetServiceList.setVisibility(View.VISIBLE);
            this.Geo = geo;
        }
    }

    public void searchAndIntent() {

        if (TXT_Loction.getText().toString().trim().equals("")) {
            TXT_Loction.setHintTextColor(Color.RED);
        } else if (SearchJSONSitter == null) {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.please_choose_a_type), Toast.LENGTH_SHORT).show();
        } else {

            /////////
            JSONObject latalng = new JSONObject();
            try {
                if (place != null) {
                    latalng.put("lat", place.getLatLng().latitude + "");
                    latalng.put("lng", place.getLatLng().longitude + "");

                    JSONObject ViewPort = new JSONObject();
                    ViewPort.put("southwest_LAT", place.getViewport().southwest.latitude + "");
                    ViewPort.put("southwest_LNG", place.getViewport().southwest.longitude + "");

                    ViewPort.put("northeast_LAT", place.getViewport().northeast.latitude + "");
                    ViewPort.put("northeast_LNG", place.getViewport().northeast.longitude + "");

                    SearchJSONSitter.put("LocationName", place.getName());
                    SearchJSONSitter.put("latlng", latalng);
                    SearchJSONSitter.put("viewport", ViewPort);
                    SearchJSONSitter.put("Address", place.getAddress());
                    SearchJSONSitter.put("StartDate", StartDate);
                    SearchJSONSitter.put("EndDate", EndDate);
                    SearchJSONSitter.put("allPetDetails", JSONFULLDATA.getJSONArray("allPetDetails"));
                } else if (GPS) {

                    JSONObject ViewPort = new JSONObject();
                    ViewPort.put("southwest_LAT", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lat") + "");
                    ViewPort.put("southwest_LNG", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lng") + "");

                    ViewPort.put("northeast_LAT", Geo.getJSONObject("viewport").getJSONObject("northeast").getString("lat") + "");
                    ViewPort.put("northeast_LNG", Geo.getJSONObject("viewport").getJSONObject("northeast").getString("lat") + "");

                    SearchJSONSitter.put("LocationName", TXT_Loction.getText());
                    SearchJSONSitter.put("latlng", Geo.getJSONObject("location"));
                    SearchJSONSitter.put("viewport", ViewPort);
                    SearchJSONSitter.put("Address", TXT_Loction.getText());
                    SearchJSONSitter.put("StartDate", StartDate);
                    SearchJSONSitter.put("EndDate", EndDate);
                    SearchJSONSitter.put("allPetDetails", JSONFULLDATA.getJSONArray("allPetDetails"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loger.MSG("@@", "" + SearchJSONSitter.toString());
            ////////


            Intent intent = new Intent(new Intent(getActivity(), SearchResultActivity.class));
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
                data.put("value", SearchJSONSitter.getString("id"));
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
                    data.put("value", SearchJSONSitter.getJSONObject("latlng").getString("lng"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "srch_lat");
                    data.put("value", SearchJSONSitter.getJSONObject("latlng").getString("lat"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lng");
                    data.put("value", SearchJSONSitter.getJSONObject("viewport").getString("northeast_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lat");
                    data.put("value", SearchJSONSitter.getJSONObject("viewport").getString("northeast_LAT"));
                    Searchkeyinfor.put(data);


                    data = new JSONObject();
                    data.put("name", "sw_lng");
                    data.put("value", SearchJSONSitter.getJSONObject("viewport").getString("southwest_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "sw_lat");
                    data.put("value", SearchJSONSitter.getJSONObject("viewport").getString("southwest_LAT"));
                    Searchkeyinfor.put(data);

                    SEARCHPARAMS.put("Address", SearchJSONSitter.getString("Address"));
                    SEARCHPARAMS.put("LocationName", SearchJSONSitter.getString("LocationName"));

                }


                SEARCHPARAMS.put("keyinfo", Searchkeyinfor);

                intent.putExtra(SearchResultActivity.SEARCHKEY, SEARCHPARAMS.toString());
                startActivityForResult(intent, 101);

            } catch (JSONException e) {
                Loger.Error("@@", "Error" + e.getMessage());
            }
        }
    }
}
