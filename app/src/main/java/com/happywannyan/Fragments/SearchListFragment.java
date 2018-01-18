package com.happywannyan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happywannyan.Activities.SearchResultActivity;
import com.happywannyan.Adapter.SearchPetsAdapter;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.R;
import com.happywannyan.Utils.Loger;

public class SearchListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recycler_view;
    public SearchPetsAdapter searchPetsAdapter;

    public SearchListFragment() {
        // Required empty public constructor
    }


    public static SearchListFragment newInstance(String param1, String param2) {
        SearchListFragment fragment = new SearchListFragment();
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
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchPetsAdapter=new SearchPetsAdapter(getActivity(),SearchListFragment.this, ((SearchResultActivity) getActivity()).ListARRY);

        recycler_view.setAdapter(searchPetsAdapter);

        ((SearchResultActivity) getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
        ((SearchResultActivity) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);


//        ((SearchResultActivity)getActivity()). findViewById(R.id.fab_plus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(((ImageView) ((SearchResultActivity)getActivity()).findViewById(R.id.fab_plus)).getTag().toString().equalsIgnoreCase("1")) {
//                    ((ImageView) ((SearchResultActivity)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_minus);
//                    ((SearchResultActivity)getActivity()). findViewById(R.id.fab).setVisibility(View.VISIBLE);
////                    ((SearchResultActivity)getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((SearchResultActivity)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
//                        }
//                    },200);
//                    ((ImageView) ((SearchResultActivity)getActivity()).findViewById(R.id.fab_plus)).setTag("0");
//                }else {
//                    ((ImageView) ((SearchResultActivity)getActivity()). findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
//                    ((SearchResultActivity)getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
////                    ((SearchResultActivity)getActivity()).findViewById(R.id.list).setVisibility(View.GONE);
//                    ((SearchResultActivity)getActivity()). findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
//                    ((ImageView) ((SearchResultActivity)getActivity()).findViewById(R.id.fab_plus)).setTag("1");
//                }
//            }
//        });
    }

    public void lazyLoad(int startPage){
        ((SearchResultActivity) getActivity()).start_form=startPage;
        ((SearchResultActivity) getActivity()).searchLoadingLazy();
    }
}
