package com.happywannyan.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.happywannyan.Activities.SearchResult;
import com.happywannyan.Adapter.TinderViewAdapter;
import com.happywannyan.R;
import com.happywannyan.Utils.cardstack.SwipeDeck;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

public class SearchTinder extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int TotalNo = 0;
    private SwipePlaceHolderView mSwipeView;
    SwipeDeck cardStack;

    public SearchTinder() {
        // Required empty public constructor
    }


    public static SearchTinder newInstance(String param1, String param2) {
        SearchTinder fragment = new SearchTinder();
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
        return inflater.inflate(R.layout.fragment_search_tinder, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardStack = (SwipeDeck) view.findViewById(R.id.swipe_deck);


        TotalNo = ((SearchResult) getActivity()).ListARRY.size();
        if (TotalNo <= 1) {
            cardStack.NUMBER_OF_CARDS = 1;
        } else if (TotalNo < 3) {
            cardStack.NUMBER_OF_CARDS = 2;
        } else {
            cardStack.NUMBER_OF_CARDS = 3;
        }


        final TinderViewAdapter adapter = new TinderViewAdapter(((SearchResult) getActivity()).ListARRY, getActivity());
        cardStack.setAdapter(adapter);
        cardStack.setLeftImage(R.id.right_image);
        cardStack.setRightImage(R.id.left_image);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                TotalNo--;
                if (TotalNo == 0) {
//                    ((ImageView) view.findViewById(R.id.IMG_Left)).setVisibility(View.GONE);
//                    ((ImageView) view.findViewById(R.id.IMG_Right)).setVisibility(View.GONE);
                    cardStack.setAdapter(adapter);
                    TotalNo = ((SearchResult) getActivity()).ListARRY.size();
                }
                if (TotalNo <= 1) {
                    cardStack.NUMBER_OF_CARDS = 1;
                    adapter.notifyDataSetChanged();
                } else if (TotalNo < 3) {
                    cardStack.NUMBER_OF_CARDS = 2;
                } else {
                    cardStack.NUMBER_OF_CARDS = 3;
                }
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                TotalNo--;
                if (TotalNo == 0) {
//                    ((ImageView) view.findViewById(R.id.IMG_Left)).setVisibility(View.GONE);
//                    ((ImageView) view.findViewById(R.id.IMG_Right)).setVisibility(View.GONE);
                    cardStack.setAdapter(adapter);
                    TotalNo = ((SearchResult) getActivity()).ListARRY.size();
                }
                if (TotalNo <= 1) {
                    cardStack.NUMBER_OF_CARDS = 1;
                    adapter.notifyDataSetChanged();
                } else if (TotalNo < 3) {
                    cardStack.NUMBER_OF_CARDS = 2;
                } else {
                    cardStack.NUMBER_OF_CARDS = 3;
                }
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });


        ((ImageView) view.findViewById(R.id.IMG_Left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopCardLeft(170);
            }
        });


        ((ImageView) view.findViewById(R.id.IMG_Right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStack.swipeTopCardRight(170);
            }
        });


//        mSwipeView = (SwipePlaceHolderView)view.findViewById(R.id.swipeView);
//
//        mSwipeView.getBuilder()
//                .setDisplayViewCount(3)
//                .setSwipeDecor(new SwipeDecor()
//                        .setPaddingTop(20)
//                        .setViewGravity(Gravity.TOP)
//                        .setRelativeScale(0.01f)
//                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
//                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
//
//
//        for(SearchData profile : ((SearchResult)getActivity()).ListARRY){
//            mSwipeView.addView(new TinderCard(getActivity(), profile, mSwipeView));
//        }

//        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSwipeView.doSwipe(false);
//            }
//        });
//
//        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSwipeView.doSwipe(true);
//            }
//        });

        ((SearchResult) getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
        ((SearchResult) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);
//        ((SearchResult) getActivity()).findViewById(R.id.fab_plus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (((ImageView) ((SearchResult) getActivity()).findViewById(R.id.fab_plus)).getTag().toString().equalsIgnoreCase("1")) {
//                    ((ImageView) ((SearchResult) getActivity()).findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_minus);
//                    ((SearchResult) getActivity()).findViewById(R.id.list).setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((SearchResult) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);
//                        }
//                    }, 200);
////                    ((SearchResult)getActivity()).findViewById(R.id.IMG_Tinderr).setVisibility(View.VISIBLE);
//                    ((ImageView) ((SearchResult) getActivity()).findViewById(R.id.fab_plus)).setTag("0");
//                } else {
//                    ((ImageView) ((SearchResult) getActivity()).findViewById(R.id.fab_plus)).setImageResource(R.drawable.ic_fab_plus);
//                    ((SearchResult) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
//                    ((SearchResult) getActivity()).findViewById(R.id.list).setVisibility(View.GONE);
////                    ((SearchResult)getActivity()). findViewById(R.id.IMG_Tinderr).setVisibility(View.GONE);
//                    ((ImageView) ((SearchResult) getActivity()).findViewById(R.id.fab_plus)).setTag("1");
//                }
//            }
//        });

    }
}
