package com.happywannyan.Fragments;

import android.content.Intent;
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
import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Adapter.Adapter_petlist;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Events;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.POJO.PetService;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLocationProvider;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.LocationListener.MyLocalLocationManager;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.constants.LogType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class Search_Basic extends Fragment implements AppLocationProvider.AddressListener {
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
    ImageView IMG_erase_location, IMG_Location;
    ArrayList<PetService> ArrayPetService;
    Adapter_petlist adapter_petlist;
    Place place;
    boolean GPS = false;
    JSONObject JSONFULLDATA, Geo;
    JSONObject SearchJSON;

    public Search_Basic() {
    }

    // TODO: Rename and change types and number of parameters
    public static Search_Basic newInstance(String param1, String param2) {
        Search_Basic fragment = new Search_Basic();
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
        ArrayPetService = new ArrayList<>();

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
        IMG_Location = (ImageView) view.findViewById(R.id.ImgMyLocation);
        IMG_erase_location = (ImageView) view.findViewById(R.id.IMG_erase_location);
        Rec_petlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL + "parent_service", new ArrayList<APIPOSTDATA>(), new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject object = new JSONObject(Result);
                    JSONFULLDATA = object;
                    JSONArray PetService = object.getJSONArray("serviceCatList");
                    for (int i = 0; i < PetService.length(); i++) {
                        JSONObject OBJE = PetService.getJSONObject(i);
                        PetService petService = new PetService();
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

                    adapter_petlist = new Adapter_petlist(Search_Basic.this, getActivity(), ArrayPetService);
                    Rec_petlist.setAdapter(adapter_petlist);
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
                for (PetService petService : ArrayPetService)
                    petService.setTick_value(false);
                ArrayPetService.get(0).setTick_value(true);
                adapter_petlist.notifyDataSetChanged();
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
                    for (PetService petService : ArrayPetService)
                        petService.setTick_value(false);
                    ArrayPetService.get(0).setTick_value(true);
                    adapter_petlist.notifyDataSetChanged();
//                    ((ImageView) view.findViewById(R.id.ImgMyLocation)).setImageResource(R.drawable.ic_my_location_white);
                } else {
                    GPS = true;
                    MyLocalLocationManager.setLogType(LogType.GENERAL);
                    ((BaseActivity) getActivity()).getLocation(new Events() {
                        @Override
                        public void UpdateLocation(Location location) {
                            Loger.MSG("@@@ LAT", "--" + location.getLatitude() + location.getLongitude());
                            new AppLocationProvider().OnGetAddress(getActivity(), location, Search_Basic.this);
                        }
                    });
                }

            }
        });


        if (AppLocationProvider.GPS(getActivity())) {

            Loger.MSG("## " + getClass().getName(), " Yewsssss");
            MyLocalLocationManager.setLogType(LogType.GENERAL);
            ((BaseActivity) getActivity()).getLocation(new Events() {
                @Override
                public void UpdateLocation(Location location) {
                    Loger.MSG("@@@ LAT", "--" + location.getLatitude() + location.getLongitude());
                    new AppLocationProvider().OnGetAddress(getActivity(), location, Search_Basic.this);

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
                        TXT_DateRange.setText(StartDate + "  to   " + EndDate);
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

                    break;
                case 101:
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.Base_fargment_layout, Advanced_search.newInstance(SearchJSON.toString(), null));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
            }
        }
    }

    public void GotoAdvancedSearched(JSONObject jsondata) {

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

                jsondata.put("LocationName", place.getName());
                jsondata.put("latlng", latalng);
                jsondata.put("viewport", ViewPort);
                jsondata.put("Address", place.getAddress());
                jsondata.put("StartDate", StartDate);
                jsondata.put("EndDate", EndDate);
                jsondata.put("allPetDetails", JSONFULLDATA.getJSONArray("allPetDetails"));
            } else if (GPS) {

                JSONObject ViewPort = new JSONObject();
                ViewPort.put("southwest_LAT", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lat") + "");
                ViewPort.put("southwest_LNG", Geo.getJSONObject("viewport").getJSONObject("southwest").getString("lng") + "");

                ViewPort.put("northeast_LAT", Geo.getJSONObject("viewport").getJSONObject("northeast").getString("lat") + "");
                ViewPort.put("northeast_LNG", Geo.getJSONObject("viewport").getJSONObject("northeast").getString("lat") + "");

                jsondata.put("LocationName", TXT_Loction.getText());
                jsondata.put("latlng", Geo.getJSONObject("location"));
                jsondata.put("viewport", ViewPort);
                jsondata.put("Address", TXT_Loction.getText());
                jsondata.put("StartDate", StartDate);
                jsondata.put("EndDate", EndDate);
                jsondata.put("allPetDetails", JSONFULLDATA.getJSONArray("allPetDetails"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SearchJSON = jsondata;
        Loger.MSG("@@", "" + SearchJSON.toString());
        searchAndIntent();
    }

    @Override
    public void OnAdresss(String Adreess, JSONObject geo) {
        if (GPS) {
            TXT_Loction.setText(Adreess);
            IMG_erase_location.setVisibility(View.VISIBLE);
            LL_PetServiceList.setVisibility(View.VISIBLE);
            this.Geo = geo;
        }
    }

    public void searchAndIntent(){

        if (TXT_Loction.getText().toString().trim().equals("")) {
            TXT_Loction.setHintTextColor(Color.RED);
        } else if (SearchJSON == null) {
            Toast.makeText(getActivity(), "Please Choose an type", Toast.LENGTH_SHORT).show();
        } else {


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
                data.put("value", SearchJSON.getString("id"));
                Searchkeyinfor.put(data);

                data = new JSONObject();
                data.put("name", "pet_type");
                data.put("value", "");
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
                    data.put("value", SearchJSON.getJSONObject("latlng").getString("lng"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "srch_lat");
                    data.put("value", SearchJSON.getJSONObject("latlng").getString("lat"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lng");
                    data.put("value", SearchJSON.getJSONObject("viewport").getString("northeast_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "ne_lat");
                    data.put("value", SearchJSON.getJSONObject("viewport").getString("northeast_LAT"));
                    Searchkeyinfor.put(data);


                    data = new JSONObject();
                    data.put("name", "sw_lng");
                    data.put("value", SearchJSON.getJSONObject("viewport").getString("southwest_LNG"));
                    Searchkeyinfor.put(data);

                    data = new JSONObject();
                    data.put("name", "sw_lat");
                    data.put("value", SearchJSON.getJSONObject("viewport").getString("southwest_LAT"));
                    Searchkeyinfor.put(data);

                    SEARCHPARAMS.put("Address", SearchJSON.getString("Address"));
                    SEARCHPARAMS.put("LocationName", SearchJSON.getString("LocationName"));

                }


                SEARCHPARAMS.put("keyinfo", Searchkeyinfor);

                intent.putExtra(SearchResult.SEARCHKEY, SEARCHPARAMS.toString());
                startActivityForResult(intent, 101);

            } catch (JSONException e) {
                Loger.Error("@@", "Error" + e.getMessage());
            }
        }
    }

    public interface OnFragmentInteractionListener {

    }
}
