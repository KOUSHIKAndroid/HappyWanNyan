package com.happywannyan.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.happywannyan.Adapter.MessageAdapter;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.Fragments.MessageFragment;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.ImageFilePath;
import com.happywannyan.Utils.Loger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MessageDetailsPageActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView LL_UserInfo;
    MessageAdapter messageAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    RelativeLayout RL_ButtomSheet;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    SFNFTextView PAGE_Titile, TXT_UserName, TXT_JoinTime, USerName, TXT_StartNAme, TXT_EndDate;
    SFNFTextView TXT_DropTime, TXT_PickTime, TXT_MessageType;
    ImageView IMGE_FROM, USER_IMAGE;
    EditText EDX_Text;
    AppLoader appLoader;
    LinearLayout LL_USER_TIME;
    File photofile;
    JSONArray ARRAy;
    private static String ReceverId = "";
    private static String MessageId = "";

    private static final int REQUEST_WRITE_PERMISSION1 = 3000;
    private static final int REQUEST_WRITE_PERMISSION2 = 4000;
    final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private int PICK_IMAGE_REQUEST = 100;
    private int CAMERA_CAPTURE = 200;

    private BroadcastReceiver Localreceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppConstant(this);
        setContentView(R.layout.activity_message_details_page);
        LL_UserInfo = (RecyclerView) findViewById(R.id.List_Recy);
        IMGE_FROM = (ImageView) findViewById(R.id.IMGE_FROM);
        USER_IMAGE = (ImageView) findViewById(R.id.USER_IMAGE);
        RL_ButtomSheet = (RelativeLayout) findViewById(R.id.RL_ButtomSheet);
        appLoader = new AppLoader(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        LL_UserInfo.setLayoutManager(mLinearLayoutManager);
        PAGE_Titile = (SFNFTextView) findViewById(R.id.PAGE_Titile);
        TXT_UserName = (SFNFTextView) findViewById(R.id.TXT_UserName);
        TXT_JoinTime = (SFNFTextView) findViewById(R.id.TXT_JoinTime);
        USerName = (SFNFTextView) findViewById(R.id.USerName);
        TXT_StartNAme = (SFNFTextView) findViewById(R.id.TXT_StartNAme);
        TXT_EndDate = (SFNFTextView) findViewById(R.id.TXT_EndDate);
        TXT_DropTime = (SFNFTextView) findViewById(R.id.TXT_DropTime);
        TXT_PickTime = (SFNFTextView) findViewById(R.id.TXT_PickTime);
        TXT_MessageType = (SFNFTextView) findViewById(R.id.TXT_MessageType);
        EDX_Text = (EditText) findViewById(R.id.EDX_Text);
        LL_USER_TIME = (LinearLayout) findViewById(R.id.LL_USER_TIME);

        Localreceiver = mMessageReceiver;
        IntentFilter filter = new IntentFilter();
        filter.addAction("CONNECT_MESSAGE_LIVE");
        LocalBroadcastManager.getInstance(this).registerReceiver(Localreceiver, filter);


        RL_ButtomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RL_ButtomSheet.setVisibility(View.GONE);
            }
        });


        ReceverId = getIntent().getStringExtra("receiver_id");
        MessageId = getIntent().getStringExtra("message_id");

        FetCh();
        TXT_MessageType.setText(MessageFragment.TAGNAME);


    }

    private void FetCh() {

        appLoader.Show();
        TimeZone tz = TimeZone.getDefault();
        String URL = "messagedetails?user_id=" + AppConstant.UserId + "&message_id=" + getIntent().getStringExtra("message_id") + "&lang_id=" + AppConstant.Language
                + "&user_timezone=" + tz.getID();

        new CustomJSONParser().APIForGetMethod(AppConstant.BASEURL + URL, new ArrayList<SetGetAPIPostData>(), new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                try {
                    JSONObject OBJ = new JSONObject(Result);
                    PAGE_Titile.setText(OBJ.getString("message_to"));
                    TXT_UserName.setText(OBJ.getString("message_to"));
                    USerName.setText(OBJ.getString("message_from"));
                    if (OBJ.has("message_startdate")) {
                        TXT_StartNAme.setText(OBJ.getString("message_startdate"));
                        TXT_EndDate.setText(OBJ.getString("message_enddate"));
                        TXT_DropTime.setText(OBJ.getString("message_droptime"));
                        TXT_PickTime.setText(OBJ.getString("message_picktime"));
                    } else
                        LL_USER_TIME.setVisibility(View.GONE);
                    TXT_JoinTime.setText(getString(R.string.join) + OBJ.getString("message_joined"));

                    if (OBJ.getInt("block_user_status") == 1) {
                        findViewById(R.id.LL_Buttom).setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.LL_Buttom).setVisibility(View.VISIBLE);
                    }

                    Glide.with(MessageDetailsPageActivity.this).load(OBJ.getString("message_to_image")).placeholder(R.drawable.ic_msg_placeholder).into(IMGE_FROM);

                    Glide.with(MessageDetailsPageActivity.this).load(OBJ.getString("message_to_image")).placeholder(R.drawable.ic_msg_placeholder).into(USER_IMAGE);
                    ARRAy = OBJ.getJSONArray("all_message_details");
//                    if (messageAdapter == null) {
                    messageAdapter = new MessageAdapter(MessageDetailsPageActivity.this, ARRAy);
                    LL_UserInfo.setAdapter(messageAdapter);
//                    } else
//                        messageAdapter.notifyDataSetChanged();
                    appLoader.Dismiss();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IMG_icon_back:
                onBackPressed();
                break;

            case R.id.RL_User_ClickBar:
                if (findViewById(R.id.LL_UserInfo_body).getVisibility() == View.GONE)
                    findViewById(R.id.LL_UserInfo_body).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.LL_UserInfo_body).setVisibility(View.GONE);
                break;

            case R.id.IMAGE_Attach:

                RL_ButtomSheet.setVisibility(View.VISIBLE);

                break;
            case R.id.IMG_Send:
                if (EDX_Text.getText().toString().trim().equals("")) {

                } else {
                    appLoader.Show();
                    ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
                    SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("message");
                    setGetAPIPostData.setValues(EDX_Text.getText() + "");
                    Params.add(setGetAPIPostData);
                    TimeZone tz = TimeZone.getDefault();
                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("user_timezone");
                    setGetAPIPostData.setValues(tz.getID() + "");
                    Params.add(setGetAPIPostData);

                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("message_type");
                    setGetAPIPostData.setValues(MessageFragment.MESSAGECODE + "");
                    Params.add(setGetAPIPostData);

                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("user_id");
                    setGetAPIPostData.setValues("" + AppConstant.UserId);
                    Params.add(setGetAPIPostData);

                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("message_id");
                    setGetAPIPostData.setValues(MessageId + "");
                    Params.add(setGetAPIPostData);

                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("receiver_id");
                    setGetAPIPostData.setValues(ReceverId + "");
                    Params.add(setGetAPIPostData);

                    setGetAPIPostData = new SetGetAPIPostData();
                    setGetAPIPostData.setPARAMS("lang_id");
                    setGetAPIPostData.setValues(AppConstant.Language);
                    Params.add(setGetAPIPostData);

                    new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "reply_message", Params, new CustomJSONParser.JSONResponseInterface() {
                        @Override
                        public void OnSuccess(String Result) {
//                            try {
//                                ARRAy.getJSONObject(ARRAy.length()-1).getJSONArray("info").put(new JSONObject(Result).getJSONObject("info"));
//                                messageAdapter = new MessageAdapter(MessageDetailsPageActivity.this, ARRAy);
//                                LL_UserInfo.setAdapter(messageAdapter);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }


                            FetCh();
                            EDX_Text.setText("");
                            appLoader.Dismiss();
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


//                Send Text To Srever
                break;
            case R.id.IMG_GALLERY:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION1);
                } else {
                    openImageGallery();
                }
                break;

            case R.id.IMG_CAMERA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION2);
                } else {
                    openCamera();
                }
                break;
            case R.id.IMG_LOCATION:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(this), PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    RL_ButtomSheet.setVisibility(View.GONE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    Loger.MSG("@@ SERVICE"," "+e.getMessage());
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Loger.MSG("@@ SERVICE"," 2 "+e.getMessage());
//                }
                break;


        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        if (photofile != null) {
            outState.putString(BITMAP_STORAGE_URL, photofile.getAbsolutePath().toString());
            Log.i("@@", "onSaveInstanceState : " + photofile.getAbsolutePath().toString());
        }
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (photofile == null) {

                photofile = new File(savedInstanceState.getString(BITMAP_STORAGE_URL));
            }
            Log.i("@@", "onRestoreInstanceState : " + photofile.getAbsolutePath().toString());
        } catch (NullPointerException e) {
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE && resultCode == RESULT_OK && data != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Uri selectedImageURI = data.getData();
                try {
                    photofile = new File(ImageFilePath.getPath(getApplicationContext(), selectedImageURI));
                    FileUpload();
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
                FileUpload();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(MessageDetailsPageActivity.this, data);
            Loger.MSG("@@ PLACE", "" + place.getLatLng());
            String MAPIMGAEURL = "https://maps.googleapis.com/maps/api/staticmap?center=" + place.getLatLng().latitude + "," + place.getLatLng().longitude + "&zoom=13&size=1000x1000&markers=" + place.getLatLng().latitude + "," + place.getLatLng().longitude + "&key=AIzaSyDAS-0Wh-K3QII2h7DgO8bd-f1dSy4lW3M";
            appLoader.Show();
            ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
            SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("message");
            setGetAPIPostData.setValues(EDX_Text.getText() + "");
            Params.add(setGetAPIPostData);
            TimeZone tz = TimeZone.getDefault();
            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("user_timezone");
            setGetAPIPostData.setValues(tz.getID() + "");
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("user_id");
            setGetAPIPostData.setValues("" + AppConstant.UserId);
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("message_id");
            setGetAPIPostData.setValues(MessageId + "");
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("receiver_id");
            setGetAPIPostData.setValues(ReceverId + "");
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("lang_id");
            setGetAPIPostData.setValues(AppConstant.Language);
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("msg_lat");
            setGetAPIPostData.setValues(place.getLatLng().latitude + "");
            Params.add(setGetAPIPostData);
            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("message_type");
            setGetAPIPostData.setValues(MessageFragment.MESSAGECODE + "");
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("msg_long");
            setGetAPIPostData.setValues(place.getLatLng().longitude + "");
            Params.add(setGetAPIPostData);

            setGetAPIPostData = new SetGetAPIPostData();
            setGetAPIPostData.setPARAMS("url_location");
            setGetAPIPostData.setValues(MAPIMGAEURL);
            Params.add(setGetAPIPostData);

            new CustomJSONParser().APIForPostMethod(AppConstant.BASEURL + "reply_message", Params, new CustomJSONParser.JSONResponseInterface() {
                @Override
                public void OnSuccess(String Result) {
                    FetCh();
                    EDX_Text.setText("");
                    appLoader.Dismiss();
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
        } else {
//            Toast.makeText(this, "Image Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void FileUpload() {
        appLoader.Show();
        ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("message");
        setGetAPIPostData.setValues("");
        Params.add(setGetAPIPostData);
        TimeZone tz = TimeZone.getDefault();
        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues(tz.getID() + "");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues("" + AppConstant.UserId);
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("message_id");
        setGetAPIPostData.setValues(MessageId + "");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("message_type");
        setGetAPIPostData.setValues(MessageFragment.MESSAGECODE + "");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("receiver_id");
        setGetAPIPostData.setValues(ReceverId + "");
        Params.add(setGetAPIPostData);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("lang_id");
        setGetAPIPostData.setValues(AppConstant.Language);
        Params.add(setGetAPIPostData);
        ArrayList<File> Files = new ArrayList<>();
        Files.add(photofile);
        CustomJSONParser.ImageParam = "msg_attachment";

        new CustomJSONParser().APIForWithPhotoPostMethod(AppConstant.BASEURL + "reply_message", Params, Files, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                FetCh();
                EDX_Text.setText("");
                appLoader.Dismiss();
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

    private void storeImage(Bitmap image) {
        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FREEWILDER");
        if (pictureFile == null) {
            Loger.MSG("@@",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            Loger.MSG("@@", "PIPATH" + pictureFile.getAbsolutePath());
            String uriSting = (pictureFile.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
            photofile = new File(uriSting);
            FileOutputStream fos = new FileOutputStream(uriSting);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            FileUpload();
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
        RL_ButtomSheet.setVisibility(View.GONE);
    }

    private void openCamera() {
        try {
            photofile = createImageFile();
            Log.d("image path--", String.valueOf(photofile));
            RL_ButtomSheet.setVisibility(View.GONE);

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

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(Localreceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("MSG_DATA");
            Log.d("receiver", "Got message: " + message);
            Log.d("receiver", "ID message: " + MessageId);

            try {
                if (MessageId.equals(new JSONObject(message).getJSONObject("message_info").getString("parent_id"))) {
                    ARRAy.getJSONObject(ARRAy.length() - 1).getJSONArray("info").put(new JSONObject(message).getJSONObject("message_info"));
                    messageAdapter = new MessageAdapter(MessageDetailsPageActivity.this, ARRAy);
                    LL_UserInfo.setAdapter(messageAdapter);
                    Log.d("receiver", "Got message:2 " + message);
                } else {
                    /*
                    Integrate Notification Message if needed
                     */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
