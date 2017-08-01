package com.happywannyan.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.happywannyan.Adapter.Adapter_message;
import com.happywannyan.Constant.AppContsnat;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.APIPOSTDATA;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.JSONPerser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Message_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Message_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Message_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static String TAGNAME="";
    public static String MESSAGECODE="";
    RecyclerView recyclerView;
    AppLoader appLoader;
    ArrayList<JSONObject> AllMessage;
    SFNFTextView tv_all_message,tv_unread_message,tv_unResponded_message,tv_reservation_message;
    View view_between_all_unread_message,view_between_unread_unResponded_message,view_unResponded_reservation_message;

    HorizontalScrollView scrollView_horizontal;
    ArrayList<APIPOSTDATA> Params ;
    Adapter_message adapter_message;
    String type;
    private OnFragmentInteractionListener mListener;

    public Message_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Message_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Message_Fragment newInstance(String param1, String param2) {
        Message_Fragment fragment = new Message_Fragment();
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
        new AppContsnat(getActivity());
        Params = new ArrayList<>();
        appLoader=new AppLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AllMessage = new ArrayList<>();

        return inflater.inflate(R.layout.fragment_message_, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_all_message= (SFNFTextView) view.findViewById(R.id.tv_all_message);
        tv_unread_message= (SFNFTextView) view.findViewById(R.id.tv_unread_message);
        tv_unResponded_message= (SFNFTextView) view.findViewById(R.id.tv_unResponded_message);
        tv_reservation_message= (SFNFTextView) view.findViewById(R.id.tv_reservation_message);
        scrollView_horizontal=(HorizontalScrollView)view.findViewById(R.id.scrollView_horizontal);
        view_between_all_unread_message=view.findViewById(R.id.view_between_all_unread_message);
        view_between_unread_unResponded_message=view.findViewById(R.id.view_between_unread_unResponded_message);
        view_unResponded_reservation_message=view.findViewById(R.id.view_unResponded_reservation_message);


        recyclerView = (RecyclerView) view.findViewById(R.id.Rec_MSG);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        APIPOSTDATA apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("start_form");
        apipostdata.setValues("0");
        Params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_id");
        apipostdata.setValues(AppContsnat.UserId);
        Params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("lang_id");
        apipostdata.setValues(AppContsnat.Language);
        Params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("per_page");
        apipostdata.setValues("10");
        Params.add(apipostdata);
        apipostdata = new APIPOSTDATA();
        apipostdata.setPARAMS("user_timezone");
        apipostdata.setValues("");
        Params.add(apipostdata);

        tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
        view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));

        type="all_message_list";
        loadList("0");
        TAGNAME=tv_all_message.getText().toString();



        tv_all_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllMessage = new ArrayList<>();
                TAGNAME=tv_all_message.getText().toString();
                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_unread_unResponded_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                type="all_message_list";
                loadList("0");
            }
        });
        tv_unread_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TAGNAME=tv_unread_message.getText().toString();

                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                scrollView_horizontal.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_unread_unResponded_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                AllMessage = new ArrayList<>();
                type="generalinquiry_message_list";
                loadList("0");
            }
        });
        tv_unResponded_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TAGNAME=tv_unResponded_message.getText().toString();
                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));

                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_between_unread_unResponded_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));

                AllMessage = new ArrayList<>();
                type="meetgreet_message_list";
                loadList("0");
            }
        });
        tv_reservation_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAGNAME=tv_reservation_message.getText().toString();
                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
                scrollView_horizontal.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
                view_between_unread_unResponded_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));

                AllMessage = new ArrayList<>();
                type="reservation_message_list";
                loadList("0");
            }
        });





    }

    public void CallDetailsPage(Intent intent) {
        startActivityForResult(intent,111);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    public void loadList(final String start_from){
        appLoader.Show();
        Params.get(0).setValues(start_from);

        new JSONPerser().API_FOR_GET(AppContsnat.BASEURL+type+"?", Params, new JSONPerser.JSONRESPONSE() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject jsonObject=new JSONObject(Result);
                    JSONArray all_message=jsonObject.getJSONArray("all_message");

                    int next_data=jsonObject.getInt("next_data");
                    Loger.MSG("next_data",""+next_data);

                    for(int i=0;i<all_message.length();i++)
                    {
                        AllMessage.add(all_message.getJSONObject(i));
                    }
                    if(start_from.equals("0")) {
                        adapter_message = new Adapter_message(getActivity(),Message_Fragment.this, AllMessage);
                        recyclerView.setAdapter(adapter_message);
                    }
                    else
                    {
                        adapter_message.nextData=next_data;
                        adapter_message.notifyDataSetChanged();
                    }
                    appLoader.Dismiss();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                if(start_from.equals("0"))
                {
                    recyclerView.setAdapter(null);
                    new MYAlert(getActivity()).AlertOnly("" + TAGNAME, "" + getString(R.string.no_data_found), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {

                        }
                    });
                }

            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111)
        {
            loadList("0");
        }

    }
}
