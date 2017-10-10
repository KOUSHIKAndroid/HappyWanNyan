package com.happywannyan.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.MessageDetailsPageActivity;
import com.happywannyan.Adapter.AdapterMessage;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.POJO.SetGetMessageDataType;
import com.happywannyan.R;
import com.happywannyan.SURAJ.PlaceCustomListAdapterDialog;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.MethodsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextWatcher myTextWatcher;

    public static String TAGNAME = "";
    public static String MESSAGECODE = "";
    RecyclerView recyclerView;
    AppLoader appLoader;
    boolean isMemberExecute = false;
    ArrayList<SetGetMessageDataType> AllMessage;
    PopupWindow popupWindow;
    PlaceCustomListAdapterDialog placeCustomListAdapterDialog = null;

    SFNFTextView tv_all_message, tv_unread_message, tv_reservation_message;
    View view_between_all_unread_message, view_unResponded_reservation_message;
    EditText edt_search;
    ImageView searchbar, search;
    boolean ISPLAY = false;
    RelativeLayout editlayout;

    HorizontalScrollView scrollView_horizontal;
    ArrayList<SetGetAPIPostData> Params;
    AdapterMessage adapter_message;

    String type;
    private OnFragmentInteractionListener mListener;
    private Paint p = new Paint();


    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
        new AppConstant(getActivity());
        Params = new ArrayList<>();
        appLoader = new AppLoader(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AllMessage = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_message_, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_all_message = (SFNFTextView) view.findViewById(R.id.tv_all_message);
        tv_unread_message = (SFNFTextView) view.findViewById(R.id.tv_unread_message);
//        tv_unResponded_message= (SFNFTextView) view.findViewById(R.id.tv_unResponded_message);
        tv_reservation_message = (SFNFTextView) view.findViewById(R.id.tv_reservation_message);
        scrollView_horizontal = (HorizontalScrollView) view.findViewById(R.id.scrollView_horizontal);
        view_between_all_unread_message = view.findViewById(R.id.view_between_all_unread_message);
        view_unResponded_reservation_message = view.findViewById(R.id.view_unResponded_reservation_message);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        searchbar = (ImageView) view.findViewById(R.id.searchbar);
        search = (ImageView) view.findViewById(R.id.search);
        editlayout = (RelativeLayout) view.findViewById(R.id.editlayout);

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });


        editlayout.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.Rec_MSG);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("start_form");
        setGetAPIPostData.setValues("0");
        Params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        Params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        Params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("per_page");
        setGetAPIPostData.setValues("10");
        Params.add(setGetAPIPostData);
        setGetAPIPostData = new SetGetAPIPostData();
        TimeZone tz = TimeZone.getDefault();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues(tz.getID());
        Params.add(setGetAPIPostData);


        tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
        view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

        type = "all_message_list";
        TAGNAME = tv_all_message.getText().toString();

        tv_all_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllMessage = new ArrayList<>();
                TAGNAME = tv_all_message.getText().toString();
                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
//                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));

                type = "all_message_list";
                loadList("0");
            }
        });
        tv_unread_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TAGNAME = tv_unread_message.getText().toString();

                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
//                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                scrollView_horizontal.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                AllMessage = new ArrayList<>();
                type = "generalinquiry_message_list";
                loadList("0");
            }
        });
//        tv_unResponded_message.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                TAGNAME=tv_unResponded_message.getText().toString();
//                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
//                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
//                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.Black));
//                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
//
//                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
//                view_between_unread_unResponded_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
//                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Black));
//
//                AllMessage = new ArrayList<>();
//                type="meetgreet_message_list";
//                loadList("0");
//            }
//        });
        tv_reservation_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAGNAME = tv_reservation_message.getText().toString();
                tv_all_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
                tv_unread_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextDarkGray));
//                tv_unResponded_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_dark_gray));
                tv_reservation_message.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                scrollView_horizontal.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                view_between_all_unread_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                view_unResponded_reservation_message.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

                AllMessage = new ArrayList<>();
                type = "reservation_message_list";
                loadList("0");
            }
        });


        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ISPLAY) {
                    ISPLAY = false;
                    searchbar.setImageResource(R.drawable.ic_search_list);
                    editlayout.setVisibility(View.GONE);
                    scrollView_horizontal.setVisibility(View.VISIBLE);
                    if (isMemberExecute) {

                        Params.clear();

                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("start_form");
                        setGetAPIPostData.setValues("0");
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_id");
                        setGetAPIPostData.setValues(AppConstant.UserId);
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("lang_id");
                        setGetAPIPostData.setValues(AppConstant.Language);
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("per_page");
                        setGetAPIPostData.setValues("10");
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        TimeZone tz = TimeZone.getDefault();
                        setGetAPIPostData.setPARAMS("user_timezone");
                        setGetAPIPostData.setValues(tz.getID());
                        Params.add(setGetAPIPostData);

                        edt_search.removeTextChangedListener(myTextWatcher);

                        AllMessage = new ArrayList<>();
                        loadList("0");
                        edt_search.setText("");
                        isMemberExecute = false;
                        MethodsUtils.hideSoftKeyboard(getActivity());
                    }
                } else {
                    ISPLAY = true;
                    searchbar.setImageResource(R.drawable.ic_close_list);
                    scrollView_horizontal.setVisibility(View.GONE);
                    editlayout.setVisibility(View.VISIBLE);
                    edt_search.addTextChangedListener(myTextWatcher);
                }
            }
        });

        myTextWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // your logic here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // your logic here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // your logic here
                searchFunction(s.toString().trim());
            }
        };

        ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setColorSchemeResources(
                R.color.colorRefreshProgress_1,
                R.color.colorRefreshProgress_2,
                R.color.colorRefreshProgress_3);

        ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AllMessage = new ArrayList<>();
                loadList("0");
                ((SwipeRefreshLayout) view.findViewById(R.id.swipeContainer)).setRefreshing(false);
            }
        });

        if (AppConstant.go_to.trim().equals("message_all")) {
            try {
                JSONObject jsonObject = new JSONObject(AppConstant.message_object_string);
                try {
                    Intent intent = new Intent(getActivity(), MessageDetailsPageActivity.class);
                    intent.putExtra("message_id", jsonObject.getString("message_id").trim());
                    if (!AppConstant.UserId.equals(jsonObject.getString("receiver_id").trim()))
                        intent.putExtra("receiver_id", jsonObject.getString("receiver_id").trim());
                    else
                        intent.putExtra("receiver_id", jsonObject.getString("sender_id").trim());
                    intent.putExtra("usersname", jsonObject.getString("usersname").trim());
                    intent.putExtra("usersimage", jsonObject.getString("usersimage").trim());
                    startActivityForResult(intent, 111);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AppConstant.go_to = "";
                AppConstant.message_object_string = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            loadList("0");
        }
    }

    public void CallDetailsPage(JSONObject jsonObject) {
        try {
            MESSAGECODE = jsonObject.getString("message_type_code").trim();
            MessageFragment.TAGNAME = jsonObject.getString("message_type").trim();
            Intent intent = new Intent(getActivity(), MessageDetailsPageActivity.class);
            intent.putExtra("message_id", jsonObject.getString("message_id").trim());
            if (!AppConstant.UserId.equals(jsonObject.getString("receiver_id").trim()))
                intent.putExtra("receiver_id", jsonObject.getString("receiver_id").trim());
            else
                intent.putExtra("receiver_id", jsonObject.getString("sender_id").trim());
            intent.putExtra("usersname", jsonObject.getString("usersname").trim());
            intent.putExtra("usersimage", jsonObject.getString("usersimage").trim());
            startActivityForResult(intent, 111);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadList(final String start_from) {
        appLoader.Show();
        Params.get(0).setValues(start_from);

        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + type + "?", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    final JSONArray all_message = jsonObject.getJSONArray("all_message");

                    int next_data = jsonObject.getInt("next_data");
                    Loger.MSG("next_data", "" + next_data);

                    for (int i = 0; i < all_message.length(); i++) {
                        SetGetMessageDataType setGetMessageDataType = new SetGetMessageDataType();
                        setGetMessageDataType.setJsonObject(all_message.getJSONObject(i));
                        setGetMessageDataType.setScrooll(false);
                        AllMessage.add(setGetMessageDataType);
                    }
                    if (start_from.equals("0")) {
                        adapter_message = new AdapterMessage(getActivity(), MessageFragment.this, AllMessage);
                        recyclerView.setAdapter(adapter_message);
                    } else {
                        adapter_message.nextData = next_data;
                        adapter_message.notifyDataSetChanged();
                    }

                    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                            Bitmap icon;
                            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {


                                Loger.MSG("@# Swipe X- ", dX + "");
                                Loger.MSG("@# Swipe Y- ", dY + "");

//                                if(dX<-325)
//                                {
//                                    int position = viewHolder.getAdapterPosition();
//                                    AllMessage.get(position).setScrooll(true);
//                                    adapter_message.notifyDataSetChanged();
//                                }

                                View itemView = viewHolder.itemView;
                                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                                float width = height / 3;

                                if (dX > 0) {
                                    p.setColor(Color.parseColor("#388E3C"));
                                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                                    c.drawRect(background, p);
                                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_vector_favourite_delete_white);
                                    RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                                    c.drawBitmap(icon, null, icon_dest, p);
                                } else {
                                    p.setColor(Color.parseColor("#D32F2F"));
                                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                                    c.drawRect(background, p);
                                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                                    c.drawBitmap(icon, null, icon_dest, p);
                                }
                            }

                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            //Toast.makeText(getActivity(), "on Swiped ", Toast.LENGTH_SHORT).show();
                            //Remove swiped item from list and notify the RecyclerView
                            int position = viewHolder.getAdapterPosition();

                            DeleteMethodCall(position);

                        }
                    };

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                    appLoader.Dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    appLoader.Dismiss();
                }
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                if (start_from.equals("0")) {
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

    private void DeleteMethodCall(final int position) {
        new MYAlert(getActivity()).AlertAccept_Cancel(getString(R.string.delete), getString(R.string.delete_message), new MYAlert.OnOkCancel() {
            @Override
            public void OnOk() {

                appLoader.Show();

                try {
                    String MessageID = AllMessage.get(position).getJsonObject().getString("message_id");
                    String ReciverId = AllMessage.get(position).getJsonObject().getString("receiver_id");
                    new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "message_deleted_API?user_id=" + AppConstant.UserId + "&message_id=" + MessageID + "&receiver_id=" + ReciverId,
                            new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
                                @Override
                                public void OnSuccess(String Result) {

                                    AllMessage.remove(position);
                                    adapter_message.notifyDataSetChanged();
                                    appLoader.Dismiss();

                                }

                                @Override
                                public void OnError(String Error, String Response) {
                                    try {
                                        appLoader.Dismiss();
                                        new MYAlert(getActivity()).AlertForAPIRESPONSE(getString(R.string.delete), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void OnError(String Error) {
                                    try {
                                        appLoader.Dismiss();
                                        new MYAlert(getActivity()).AlertForAPIRESPONSE(getString(R.string.delete), Error, new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnCancel() {
                adapter_message.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            AllMessage = new ArrayList<>();
            loadList("0");
        }

    }

    public void searchFunction(String s) {
        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("user_id", AppConstant.UserId);
        Params.put("search_name", s);
        Params.put("lang_id", AppConstant.Language);

        appLoader.Show();

        new CustomJSONParser().APIForPostMethod2(AppConstant.BASEURL + "user_memberlist" + "?", Params, new CustomJSONParser.JSONResponseInterface() {
            @Override

            public void OnSuccess(String Result) {
                try {
                    JSONObject jsonObject = new JSONObject(Result);
                    final JSONArray all_member = jsonObject.getJSONArray("all_member");

                    Loger.MSG("memberListSize", "" + all_member);
                    appLoader.Dismiss();
                    showDialog(editlayout, all_member);

                } catch (Exception e) {
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
            }
        });
    }

    private void showDialog(View v, JSONArray PredictionsJsonArray) {

        if (popupWindow == null) {
            popupWindow = new PopupWindow(getActivity());
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            View dailogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_show_place, null);

            RecyclerView rcv_ = (RecyclerView) dailogView.findViewById(R.id.rcv_);
            rcv_.setLayoutManager(new LinearLayoutManager(getActivity()));

            placeCustomListAdapterDialog = new PlaceCustomListAdapterDialog(getActivity(), PredictionsJsonArray, new onOptionSelected() {

                @Override
                public void onItemPassed(int position, JSONObject value) {
                    popupWindow.dismiss();
                    Loger.MSG("value", "" + value);
                    try {
                        Params.clear();

                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("start_form");
                        setGetAPIPostData.setValues("0");
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_id");
                        setGetAPIPostData.setValues(AppConstant.UserId);
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("lang_id");
                        setGetAPIPostData.setValues(AppConstant.Language);
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("per_page");
                        setGetAPIPostData.setValues("10");
                        Params.add(setGetAPIPostData);
                        setGetAPIPostData = new SetGetAPIPostData();
                        TimeZone tz = TimeZone.getDefault();
                        setGetAPIPostData.setPARAMS("user_timezone");
                        setGetAPIPostData.setValues(tz.getID());
                        Params.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("member_id");
                        setGetAPIPostData.setValues(value.getString("member_id"));
                        Params.add(setGetAPIPostData);

                        isMemberExecute = true;
                        placeCustomListAdapterDialog = null;

                        AllMessage = new ArrayList<>();
                        loadList("0");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });

            rcv_.setAdapter(placeCustomListAdapterDialog);
            // some other visual settings
            popupWindow.setFocusable(false);
            popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // set the list view as pop up window content
            popupWindow.setContentView(dailogView);
            popupWindow.showAsDropDown(v, -5, 0);
        } else {
            popupWindow.showAsDropDown(v, -5, 0);
            placeCustomListAdapterDialog.setRefresh(PredictionsJsonArray);
        }

    }

    public interface onOptionSelected {
        void onItemPassed(int position, JSONObject value);
    }

}