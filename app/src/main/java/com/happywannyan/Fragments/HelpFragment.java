package com.happywannyan.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppLoader appLoader;
    WebView webView;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        webView = ((WebView) view.findViewById(R.id.Web));
        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        ////////////////////Improve WebVIew Performance Load Faster with WebSettings ///////////////////
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }
            //Show loader on url load
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Then show progress  Dialog
                // in standard case YourActivity.this
                if (appLoader == null) {
                    appLoader = new AppLoader(getActivity());
                    appLoader.Show();
                }
            }
            // Called when all page resources loaded
            public void onPageFinished(WebView view, String url) {
                try {
                    // Close progressDialog
                    if (appLoader.isShowing()) {
                        appLoader.Dismiss();
                        appLoader = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // Return the app name after finish loading
            }
        });

        webView.loadUrl(AppConstant.BASEURL+"help-page?lang_id="+AppConstant.Language);


//        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "help?lang_id=" + AppConstant.Language, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
//            @Override
//            public void OnSuccess(String Result) {
//                appLoader.Dismiss();
//                Loger.MSG("Result", Result);
//                try {
//                    if (new JSONObject(Result).getBoolean("response")) {
//                        //Toast.makeText(getActivity(),new JSONObject(Result).getString("page"),Toast.LENGTH_SHORT).show();
//                        webView.loadUrl(new JSONObject(Result).getString("page")+"?lang_id="+AppConstant.Language);
//                        //webView.loadUrl(AppConstant.BASEURL+""+new JSONObject(Result).getString("page_new")+"?lang_id="+AppConstant.Language);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void OnError(String Error, String Response) {
//                appLoader.Dismiss();
////                try {
////                    if (new JSONObject(Response).getBoolean("response")) {
////                        Toast.makeText(getActivity(),new JSONObject(Response).getString("page"),Toast.LENGTH_SHORT).show();
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//            }
//
//            @Override
//            public void OnError(String Error) {
//                appLoader.Dismiss();
//            }
//        });
    }
}

