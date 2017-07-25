package com.happywannyan.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Adapter.SearchPets_Adapter;
import com.happywannyan.R;

public class SearchList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recycler_view;
    public SearchList() {
        // Required empty public constructor
    }


    public static SearchList newInstance(String param1, String param2) {
        SearchList fragment = new SearchList();
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
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view=(RecyclerView)view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(new SearchPets_Adapter(getActivity(),((SearchResult)getActivity()).ListARRY));
        ((SearchResult)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
        ((SearchResult)getActivity()). findViewById(R.id.fab).setVisibility(View.VISIBLE);
//
//        ((SearchResult)getActivity()). findViewById(R.id.fab_plus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).getTag().toString().equalsIgnoreCase("1")) {
//                    ((ImageView) ((SearchResult)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_minus);
//                    ((SearchResult)getActivity()). findViewById(R.id.fab).setVisibility(View.VISIBLE);
////                    ((SearchResult)getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((SearchResult)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
//                        }
//                    },200);
//                    ((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).setTag("0");
//                }else {
//                    ((ImageView) ((SearchResult)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
//                    ((SearchResult)getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
////                    ((SearchResult)getActivity()).findViewById(R.id.list).setVisibility(View.GONE);
//                    ((SearchResult)getActivity()). findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
//                    ((ImageView) ((SearchResult)getActivity()).findViewById(R.id.fab_plus)).setTag("1");
//                }
//            }
//        });
    }
}
