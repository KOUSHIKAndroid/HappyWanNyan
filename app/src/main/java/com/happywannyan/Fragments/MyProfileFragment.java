package com.happywannyan.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.happywannyan.Activities.BaseActivity;
import com.happywannyan.Activities.LoginActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppDataHolder;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CircleTransform;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.ImageFilePath;
import com.happywannyan.Utils.Loger;
import com.happywannyan.Utils.MYAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_WRITE_PERMISSION1 = 3000;
    private static final int REQUEST_WRITE_PERMISSION2 = 4000;
    final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private int PICK_IMAGE_REQUEST = 100;
    private int CAMERA_CAPTURE = 200;
    AlertDialog Dialog;
    File photofile = null;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView ProfileImg;
    SFNFTextView TXT_Loction;
    private OnFragmentInteractionListener mListener;
    JSONObject UserInfo;
    Place place;
    CheckBox Check;
    AppLoader appLoader;

    View Mview;

    public MyProfileFragment() {

        // Required empty public constructor
    }

    private void showPhotoDialog() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View LayView = inflater.inflate(R.layout.alert_dialog_image_taker, null);

        ImageView img_gallery = (ImageView) LayView.findViewById(R.id.img_gallery);
        ImageView img_camera = (ImageView) LayView.findViewById(R.id.img_camera);

        SFNFTextView TXTTitle = (SFNFTextView) LayView.findViewById(R.id.Title);
        TXTTitle.setText(getResources().getString(R.string.add_image));

        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION1);
                } else {
                    openImageGallery();
                }
                Dialog.dismiss();
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION2);
                } else {
                    openCamera();
                }

                Dialog.dismiss();
            }
        });

        alertbuilder.setView(LayView);
        Dialog = alertbuilder.create();
        Dialog.show();
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppConstant(getActivity());
        appLoader = new AppLoader(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


    @Override
    public void onViewCreated(final View Mview, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(Mview, savedInstanceState);

        this.Mview = Mview;
        Mview.findViewById(R.id.IMG_icon_drwaer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).Menu_Drawer();
            }
        });

        Check = (CheckBox) Mview.findViewById(R.id.Checked);
        ProfileImg = (ImageView) Mview.findViewById(R.id.IMG_Profile);
        ProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoDialog();
            }
        });
        TXT_Loction = (SFNFTextView) Mview.findViewById(R.id.TXT_Address);
        TXT_Loction.setOnClickListener(new View.OnClickListener() {
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

        loadPage();


        Mview.findViewById(R.id.Card_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((EditText) Mview.findViewById(R.id.EDX_FNAME)).getText().toString().trim().length() > 0) {
                    if (((EditText) Mview.findViewById(R.id.EDX_Lname)).getText().toString().trim().length() > 0) {

                        if (((EditText) Mview.findViewById(R.id.EDX_F_FName)).getText().toString().trim().length() > 0) {
                            if (((EditText) Mview.findViewById(R.id.EDX_F_LName)).getText().toString().trim().length() > 0) {

                                if (((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).getText().toString().trim().length() > 0) {
                                    if (((EditText) Mview.findViewById(R.id.EDX_Phone)).getText().toString().trim().length() > 0) {
                                        SUBMITDETAILS();
                                    } else {
                                        ((EditText) Mview.findViewById(R.id.EDX_Phone)).setHintTextColor(Color.RED);
                                        ((EditText) Mview.findViewById(R.id.EDX_Phone)).requestFocus();
                                    }
                                } else {
                                    ((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).setHintTextColor(Color.RED);
                                    ((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).setHint(getString(R.string.pleasenteraddress));
                                    ((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).requestFocus();
                                }
                            } else {
                                ((EditText) Mview.findViewById(R.id.EDX_F_LName)).setHintTextColor(Color.RED);
                                ((EditText) Mview.findViewById(R.id.EDX_F_LName)).requestFocus();
                            }
                        } else {
                            ((EditText) Mview.findViewById(R.id.EDX_F_FName)).setHintTextColor(Color.RED);
                            ((EditText) Mview.findViewById(R.id.EDX_F_FName)).requestFocus();
                        }
                    } else {
                        ((EditText) Mview.findViewById(R.id.EDX_Lname)).setHintTextColor(Color.RED);
                        ((EditText) Mview.findViewById(R.id.EDX_Lname)).requestFocus();
                    }

                } else {
                    ((EditText) Mview.findViewById(R.id.EDX_FNAME)).setHintTextColor(Color.RED);
                    ((EditText) Mview.findViewById(R.id.EDX_FNAME)).requestFocus();
                }
            }
        });
        Mview.findViewById(R.id.img_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserInfo!=null && UserInfo.has("sign_up_info")) {
                    try {
                        new MYAlert(getActivity()).AlertOnly(getResources().getString(R.string.info), UserInfo.getString("sign_up_info"), new MYAlert.OnlyMessage() {
                            @Override
                            public void OnOk(boolean res) {
                            }
                        });
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

    }

    private void SUBMITDETAILS() {
        if (Check.isChecked()) {
            appLoader.Show();
            try {
                Iterator it = UserInfo.keys(); //gets all the keys
                while (it.hasNext()) {
                    String key = (String) it.next(); // get key
                    String value = (String) UserInfo.get(key); // get value
                    switch (key) {
                        case "firstname":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_FNAME)).getText());
                            break;
                        case "lastname":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_Lname)).getText());
                            break;
                        case "firstname_phonetic":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_F_FName)).getText());
                            break;
                        case "lastname_phonetic":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_F_LName)).getText());
                            break;
                        case "address":
                            UserInfo.put(key, "" + ((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).getText());
                            break;
                        case "lat_addr":
                            if (place != null && !value.equals("" + place.getLatLng().latitude))
                                UserInfo.put(key, "" + place.getLatLng().latitude);
                            break;
                        case "long_addr":
                            if (place != null && !value.equals("" + place.getLatLng().longitude))
                                UserInfo.put(key, "" + place.getLatLng().longitude);
                            break;
                        case "optional_promo_code":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_optional_promo_code)).getText());
                            break;
                        case "mobilenum":
                            UserInfo.put(key, "" + ((EditText) Mview.findViewById(R.id.EDX_Phone)).getText());
                            break;

                    }

                }

                ArrayList<SetGetAPIPostData> Params = new ArrayList<SetGetAPIPostData>();
                SetGetAPIPostData Post = new SetGetAPIPostData();
                Post.setPARAMS("user_id");
                Post.setValues(AppConstant.UserId);
                Params.add(Post);
                Post = new SetGetAPIPostData();
                Post.setPARAMS("lang_id");
                Post.setValues(AppConstant.Language);
                Params.add(Post);
                Post = new SetGetAPIPostData();
                Post.setPARAMS("user_info");
                Post.setValues(new JSONObject().put("users_information", UserInfo).toString());
                Params.add(Post);
                ArrayList<File> Photos = new ArrayList<File>();
                Photos.add(photofile);

                CustomJSONParser.ImageParam = "photo";

                new CustomJSONParser().APIForWithPhotoPostMethod(AppConstant.BASEURL + "app_users_edit", Params, Photos, new CustomJSONParser.JSONResponseInterface() {
                    @Override
                    public void OnSuccess(String Result) {
                        appLoader.Dismiss();
                        loadPage();
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

                Loger.MSG("@@ 888", new JSONObject().put("users_information", UserInfo).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//                    Write Alert Validation
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK && data != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Uri selectedImageURI = data.getData();
                try {
                    photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                    Loger.MSG("photofile",""+photofile);
                    Glide.with(getApplicationContext()).load(photofile).transform(new CircleTransform(getActivity())).error(R.drawable.ic_profile).into(ProfileImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                storeImage(photo);
            }

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && photofile != null) {
            Uri selectedImageURI = data.getData();
            try {
                photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                Loger.MSG("photofile",""+photofile);
                Glide.with(getApplicationContext()).load(photofile).transform(new CircleTransform(getActivity())).error(R.drawable.ic_profile).into(ProfileImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            place = PlacePicker.getPlace(getActivity(), data);
            Loger.MSG("@@ PLACE", "" + place.getLatLng());
            Loger.MSG("@@ ViewPosrt", "- " + place.getViewport().toString());
            String Location = "" + place.getName();
            TXT_Loction.setText(Location);
        } else {
//            Toast.makeText(getActivity(), "Image Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FREEWILDER");
        if (pictureFile == null) {
            Loger.MSG("@@", "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            Loger.MSG("@@", "PIPATH" + pictureFile.getAbsolutePath());
            String uriSting = (pictureFile.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
            photofile = new File(uriSting);
            FileOutputStream fos = new FileOutputStream(uriSting);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Glide.with(getApplicationContext()).load(photofile).transform(new CircleTransform(getActivity())).error(R.drawable.ic_profile).into(ProfileImg);
        } catch (FileNotFoundException e) {
            Loger.MSG("@@", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Loger.MSG("@@", "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageGallery();
        }
        if (requestCode == REQUEST_WRITE_PERMISSION2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    private void openImageGallery() {
        try {
            photofile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


        Dialog.dismiss();
    }

    private void openCamera() {
        try {
            photofile = createImageFile();
            Log.d("image path--", String.valueOf(photofile));
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

        } catch (Exception e) {
            Log.v("Catch Exception", e.toString());
        }
    }

    File createImageFile() throws IOException {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());///new approach
        String imagefilename = "IMAGE_" + timestamp + "_";///new approach
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imagefilename, ".jpg", storageDirectory);/////new approach
        return image;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadPage() {
        appLoader.Show();
        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + "app_users_about?user_id=" + AppConstant.UserId, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    appLoader.Dismiss();
                    JSONObject Ob = new JSONObject(Result);
                    UserInfo = Ob.getJSONObject("users_information");


                    /////////////////////update Share Preference (Login credential)////////////////////////////////////
                    new AppConstant(getActivity()).upDateShareDATA(AppDataHolder.UserData,UserInfo.getString("photo"),UserInfo.getString("firstname"),UserInfo.getString("lastname"));
                    ///////////////////////////////////////////////////////END//////////////////////////////////


                    /////////////////Set Login User Details in Left Drawable///////////////////
                    ((BaseActivity)getActivity()).setProfileLoginUserDetails();
                    ////////////////////////////////////END///////////////////////////////

                    Glide.with(getActivity()).load(UserInfo.getString("photo")).transform(new CircleTransform(getActivity())).error(R.drawable.ic_profile).into(ProfileImg);
                    ((EditText) Mview.findViewById(R.id.EDX_FNAME)).setText(UserInfo.getString("firstname"));
                    ((EditText) Mview.findViewById(R.id.EDX_Lname)).setText(UserInfo.getString("lastname"));
                    ((EditText) Mview.findViewById(R.id.EDX_F_FName)).setText(UserInfo.getString("firstname_phonetic"));
                    ((EditText) Mview.findViewById(R.id.EDX_F_LName)).setText(UserInfo.getString("lastname_phonetic"));
                    ((EditText) Mview.findViewById(R.id.EDX_Phone)).setText(UserInfo.getString("mobilenum"));

                    if (UserInfo.getString("optional_promo_code").trim().equals("")) {
                        Mview.findViewById(R.id.RL_promo_code).setVisibility(View.VISIBLE);
                        Mview.findViewById(R.id.view_promo_code).setVisibility(View.VISIBLE);
//                        ((EditText) Mview.findViewById(R.id.EDX_optional_promo_code)).setText(UserInfo.getString("optional_promo_code"));

                    } else {
                        Mview.findViewById(R.id.RL_promo_code).setVisibility(View.GONE);
                        Mview.findViewById(R.id.view_promo_code).setVisibility(View.GONE);
                    }

                    ((SFNFTextView) Mview.findViewById(R.id.TXT_Address)).setText(UserInfo.getString("address"));
                    ((SFNFTextView) Mview.findViewById(R.id.TXT_Country_Code)).setText(UserInfo.getString("phone_code"));
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
