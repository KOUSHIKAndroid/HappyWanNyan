package com.happywannyan.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;
import com.happywannyan.Utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText EDX_new_email, EDX_confirm_email, EDX_old_password, EDX_new_password, EDX_confirm_password;
    AppLoader appLoader;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        return inflater.inflate(R.layout.fragment_account_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        Loger.MSG("AppConstant.UserEmail", "-->" + AppConstant.UserEmail);

        ((SFNFTextView) view.findViewById(R.id.tv_email_show)).setText(getActivity().getResources().getString(R.string.your_current_email) + ": " + AppConstant.UserEmail);

        appLoader = new AppLoader(getActivity());

        EDX_new_email = (EditText) view.findViewById(R.id.EDX_new_email);
        EDX_confirm_email = (EditText) view.findViewById(R.id.EDX_confirm_email);
        EDX_old_password = (EditText) view.findViewById(R.id.EDX_old_password);
        EDX_new_password = (EditText) view.findViewById(R.id.EDX_new_password);
        EDX_confirm_password = (EditText) view.findViewById(R.id.EDX_confirm_password);

        view.findViewById(R.id.tv_change_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
            }
        });

        view.findViewById(R.id.tv_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

    }

    public void changeEmail() {
        if (EDX_new_email.getText().toString().trim().equals("")) {
            EDX_new_email.setHintTextColor(Color.RED);
            EDX_new_email.setHint(getString(R.string.please_enter_new_email));
            EDX_new_email.requestFocus();
        } else {
            if (!Validation.isValidEmail(EDX_new_email.getText())) {
                new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.Please_enter_valid_email), new MYAlert.OnlyMessage() {
                    @Override
                    public void OnOk(boolean res) {
                        if (res) {
                            EDX_new_email.requestFocus();
                        }
                    }
                });
            } else {
                if (EDX_confirm_email.getText().toString().trim().equals("")) {
                    EDX_confirm_email.setHintTextColor(Color.RED);
                    EDX_confirm_email.setHint(getString(R.string.please_enter_new_email));
                    EDX_confirm_email.requestFocus();
                } else {
                    if (!EDX_new_email.getText().toString().trim().equals(EDX_confirm_email.getText().toString().trim())) {
                        new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.email_miss_match), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                                if (res) {
                                    EDX_confirm_email.requestFocus();
                                }
                            }
                        });
                    } else {

                        appLoader.Show();
                        ArrayList<SetGetAPIPostData> apiPostData=new ArrayList<>();

                        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("user_id");
                        setGetAPIPostData.setValues(AppConstant.UserId);
                        apiPostData.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("langid");
                        setGetAPIPostData.setValues(AppConstant.Language);
                        apiPostData.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("emailid");
                        setGetAPIPostData.setValues(EDX_confirm_email.getText().toString().trim());
                        apiPostData.add(setGetAPIPostData);

                        setGetAPIPostData = new SetGetAPIPostData();
                        setGetAPIPostData.setPARAMS("conf_emailid");
                        setGetAPIPostData.setValues(EDX_confirm_email.getText().toString().trim());
                        apiPostData.add(setGetAPIPostData);

//                        HashMap<String, String> Params = new HashMap<>();
//                        Params.put("user_id", AppConstant.UserId);
//                        Params.put("langid", AppConstant.Language);
//                        Params.put("emailid", EDX_confirm_email.getText().toString().trim());
//                        Params.put("conf_emailid", EDX_confirm_email.getText().toString().trim());

                        new CustomJSONParser().APIForPostMethod(getActivity(), AppConstant.BASEURL + "app_users_changeemail", apiPostData, new CustomJSONParser.JSONResponseInterface() {
                            @Override
                            public void OnSuccess(String Result) {
                                appLoader.Dismiss();
                                try {
                                    AppConstant.UserEmail=EDX_confirm_email.getText().toString().trim();

                                    /////////////////////update Share Preference (Login credential)////////////////////////////////////
                                    new AppConstant(getActivity()).upDateShareDATAEmail(AppDataHolder.UserData,AppConstant.UserEmail);
                                    ///////////////////////////////////////////////////////END//////////////////////////////////

                                    new MYAlert(getActivity()).AlertOnly(getString(R.string.change_email_address), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
                                        @Override
                                        public void OnOk(boolean res) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void OnError(String Error, String Response) {
                                appLoader.Dismiss();
                                try {
                                    new MYAlert(getActivity()).AlertOnly(getString(R.string.change_email_address), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
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
                                appLoader.Dismiss();
                                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                    Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        }
    }


    public void changePassword() {
        if (EDX_old_password.getText().toString().trim().equals("")) {
            EDX_old_password.setHintTextColor(Color.RED);
            EDX_old_password.setHint(getString(R.string.please_enter_old_password));
            EDX_old_password.requestFocus();
        } else {
            if (EDX_new_password.getText().toString().trim().equals("")) {
                EDX_new_password.setHintTextColor(Color.RED);
                EDX_new_password.setHint(getString(R.string.please_enter_new_password));
                EDX_new_password.requestFocus();
            } else {
                if (EDX_new_password.getText().toString().trim().length() < 4) {
                    new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.change_password_checkingtext), new MYAlert.OnlyMessage() {
                        @Override
                        public void OnOk(boolean res) {
                            if (res) {
                                EDX_new_password.requestFocus();
                            }
                        }
                    });
                } else {
                    if (EDX_confirm_password.getText().toString().trim().equals("")) {
                        EDX_confirm_password.setHintTextColor(Color.RED);
                        EDX_confirm_password.setHint(getString(R.string.please_enter_confirm_password));
                        EDX_confirm_password.requestFocus();
                    } else {
                        if (!EDX_new_password.getText().toString().trim().equals(EDX_confirm_password.getText().toString().trim())) {

                            new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.LoginAlertTitle), getResources().getString(R.string.password_miss_match), new MYAlert.OnlyMessage() {
                                @Override
                                public void OnOk(boolean res) {
                                    if (res) {
                                        EDX_confirm_password.requestFocus();
                                    }
                                }
                            });

                        } else {

                            appLoader.Show();

                            HashMap<String, String> Params = new HashMap<>();
                            Params.put("user_id", AppConstant.UserId);
                            Params.put("lang_id", AppConstant.Language);
                            Params.put("old_pass", EDX_old_password.getText().toString().trim());
                            Params.put("new_pass", EDX_new_password.getText().toString().trim());
                            Params.put("conf_password", EDX_confirm_password.getText().toString().trim());

                            new CustomJSONParser().APIForPostMethod2(getActivity(), AppConstant.BASEURL + "change_password", Params, new CustomJSONParser.JSONResponseInterface() {
                                @Override
                                public void OnSuccess(String Result) {
                                    appLoader.Dismiss();
                                    try {
                                        new MYAlert(getActivity()).AlertOnly(getString(R.string.change_password), new JSONObject(Result).getString("message"), new MYAlert.OnlyMessage() {
                                            @Override
                                            public void OnOk(boolean res) {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void OnError(String Error, String Response) {
                                    appLoader.Dismiss();
                                    try {
                                        new MYAlert(getActivity()).AlertOnly(getString(R.string.change_password), new JSONObject(Response).getString("message"), new MYAlert.OnlyMessage() {
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
                                    appLoader.Dismiss();
                                    if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))) {
                                        Toast.makeText(getActivity(), Error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
