package com.happywannyan.Fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Activities.profile.ProfileDetails;
import com.happywannyan.Font.SFNFBoldTextView;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.provider.RatingColor;

import org.json.JSONException;
import org.json.JSONObject;

import static com.happywannyan.Constant.ApplicationClass.TAG;

public class SearchMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LatLngBounds PresetLatBounds;
    GoogleMap Map;
    public SearchMap() {
        // Required empty public constructor
    }


    public static SearchMap newInstance(String param1, String param2) {
        SearchMap fragment = new SearchMap();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapsInitializer.initialize(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ((SearchResult)getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
        ((SearchResult)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);

//        ((SearchResult)getActivity()). findViewById(R.id.fab_plus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).getTag().toString().equalsIgnoreCase("1")) {
//                    ((ImageView) ((SearchResult)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_minus);
////                    ((SearchResult)getActivity()). findViewById(R.id.fab).setVisibility(View.VISIBLE);
//                    ((SearchResult)getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((SearchResult)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
//                        }
//                    },200);
//                    ((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).setTag("0");
//                }else {
//                    ((ImageView) ((SearchResult)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
////                    ((SearchResult)getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
//                    ((SearchResult)getActivity()).findViewById(R.id.list).setVisibility(View.GONE);
//                    ((SearchResult)getActivity()). findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
//                    ((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).setTag("1");
//                }
//            }
//        });

    }

    @Override
    public void onCameraIdle() {
        Loger.MSG("@@ Zoom"," "+Map.getCameraPosition().zoom);
    }

    @Override
    public void onCameraMove() {
//        Loger.MSG("@@ ViewPost"," "+Map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
//
//        if(Map.getCameraPosition().zoom>5 && Map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude<
//                PresetLatBounds.northeast.latitude && Map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude> PresetLatBounds.southwest.latitude
//                )
//        {
//            Map.getUiSettings().setScrollGesturesEnabled(true);
//        }else {
////            Map.getUiSettings().setScrollGesturesEnabled(false);
//            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(PresetLatBounds.getCenter(),Map.getCameraPosition().zoom) );
//        }

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       marker.showInfoWindow();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map=googleMap;
        Map.setOnCameraIdleListener(this);
        Map.setOnCameraMoveListener(this);
        Map.setMinZoomPreference((float) 4.99);
        Map.getUiSettings().setRotateGesturesEnabled(false);
        Map.getUiSettings().setScrollGesturesEnabled(true);
        Map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file info_window_layout
                View v = getActivity().getLayoutInflater().inflate(R.layout.map_popup_time, null);
                v.setLayoutParams(new RelativeLayout.LayoutParams(700,280));
                LatLng latLng = marker.getPosition();
                try {
                    JSONObject jsonObject=new JSONObject(marker.getTitle()+"");
                    Loger.MSG("@@ MAR",""+jsonObject);
                    ((SFNFBoldTextView)v.findViewById(R.id.tv_name)).setText(""+jsonObject.getString("service_name"));
                    ((SFNFTextView)v.findViewById(R.id.tv_place)).setText(""+jsonObject.getString("whole_address"));
                    ((SFNFTextView)v.findViewById(R.id.tv_time)).setText(""+jsonObject.getString("unit"));
                    ((SFNFTextView)v.findViewById(R.id.TXT_sitter)).setText(""+jsonObject.getString("sitter_name"));
                    ((RatingBar)v.findViewById(R.id.Rating)).setNumStars(Integer.parseInt(jsonObject.getString("ave_rating")));
                    LayerDrawable stars = (LayerDrawable)((RatingBar)v.findViewById(R.id.Rating)).getProgressDrawable();
                    RatingColor.SETRatingColor(stars);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return v;

            }
        });
        Map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();

//                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)getActivity(), holder.img_view, "cardimage");
                Intent intent = new Intent(getActivity(), ProfileDetails.class);
                            intent.putExtra("data", "" + ((SearchResult)getActivity()).ListARRY.get((Integer) marker.getTag()).getSearcItem());
                startActivity(intent);
            }
        });


//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(new LatLng(((SearchResult)getActivity()).ne_lat,((SearchResult)getActivity()).ne_lng));
//        builder.include(new LatLng(((SearchResult)getActivity()).sw_lat,((SearchResult)getActivity()).sw_lng));
//        PresetLatBounds=builder.build();


        try {
            LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
            for (int i = 0; i <((SearchResult)getActivity()). ListARRY.size(); i++) {


                try {
                    LatLng latLng=new LatLng(Double.parseDouble(((SearchResult)getActivity()).ListARRY.get(i).getSearcItem().getString("lat")),Double.parseDouble(((SearchResult)getActivity()).ListARRY.get(i).getSearcItem().getString("long")));
                    MarkerOptions markerOptions = new MarkerOptions().
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon)).
                            position(latLng).zIndex(0.0f);
//                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                    Marker lomarakar = Map.addMarker(markerOptions);
                    lomarakar.setTitle(((SearchResult)getActivity()). ListARRY.get(i).getSearcItem()+"");
                    builder2.include(markerOptions.getPosition());
                    lomarakar.setTag(i);
                }catch (Exception ee){
                    Loger.Error(TAG,"3 "+ee.getMessage());
                }






            }

            LatLngBounds bounds = builder2.build();
            PresetLatBounds=builder2.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(PresetLatBounds.getCenter(), 15);
//            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(PresetLatBounds.getCenter(), 15);
            Map.animateCamera(cu);
        }catch (Exception e)
        {
            Loger.Error(TAG," "+e.getMessage());
        }

//        int padding = 100; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(camrearset, padding);
//        Map.animateCamera(cu);
    }
}
