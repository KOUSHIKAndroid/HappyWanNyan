package com.happywannyan.Activities.Booking;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happywannyan.Activities.ResetPasswordActivity;
import com.happywannyan.Constant.AppConstant;
import com.happywannyan.Font.SFNFTextView;
import com.happywannyan.POJO.SetGetAPIPostData;
import com.happywannyan.R;
import com.happywannyan.Utils.AppLoader;
import com.happywannyan.Utils.CustomJSONParser;
import com.happywannyan.Utils.ImageFilePath;
import com.happywannyan.Utils.Loger;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_WRITE_PERMISSION1 = 3000;
    private static final int REQUEST_WRITE_PERMISSION2 = 4000;
    final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private int PICK_IMAGE_REQUEST = 100;
    private int CAMERA_CAPTURE = 200;
    File photofile = null;
    AlertDialog Dialog;
    String BookingId = "";
    AppLoader appLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);
        BookingId = getIntent().getStringExtra("B_ID");
        Loger.MSG("B_ID-->", "--" + BookingId);
        appLoader = new AppLoader(this);

        findViewById(R.id.IMG_icon_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        Loger.MSG("@@ CAM SERVICEID-", getIntent().getStringExtra("SERVICEID") + "\n USERID-" + getIntent().getStringExtra("USERID"));
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
                    Glide.with(AddReviewActivity.this).load(selectedImageURI).into((ImageView) findViewById(R.id.img_upload_review));
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
                Glide.with(AddReviewActivity.this).load(selectedImageURI).into((ImageView) findViewById(R.id.img_upload_review));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.image_error), Toast.LENGTH_SHORT).show();
        }
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


    private void showPhotoDialog() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_Attach:
                showPhotoDialog();
                break;
            case R.id.BTN_Confirm:
                if (((RatingBar) findViewById(R.id.RATINGID)).getRating() == 0.0) {
                    Toast.makeText(AddReviewActivity.this, getResources().getString(R.string.please_give_your_ratting), Toast.LENGTH_SHORT).show();
                } else if (((EditText) findViewById(R.id.EDX_message)).getText().toString().trim().equals("")) {
                    ((EditText) findViewById(R.id.EDX_message)).setHint(R.string.please_enter_message);
                    ((EditText) findViewById(R.id.EDX_message)).setHintTextColor(Color.RED);
                    ((EditText) findViewById(R.id.EDX_message)).requestFocus();
                } else {
                    SUBMIT_Review();
                }
        }
    }

    private void SUBMIT_Review() {
        appLoader.Show();
        new AppConstant(this);


        ArrayList<SetGetAPIPostData> Params = new ArrayList<>();
        SetGetAPIPostData setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("booking_id");
        setGetAPIPostData.setValues(BookingId);
        Params.add(setGetAPIPostData);
        Loger.MSG("booking_id-->", "" + BookingId);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("langid");
        setGetAPIPostData.setValues(AppConstant.Language);
        Params.add(setGetAPIPostData);
        Loger.MSG("langid-->", "" + AppConstant.Language);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_id");
        setGetAPIPostData.setValues(AppConstant.UserId);
        Params.add(setGetAPIPostData);
        Loger.MSG("user_id-->", "" + AppConstant.UserId);

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("rating_input");
        setGetAPIPostData.setValues(((RatingBar) findViewById(R.id.RATINGID)).getRating() + "");
        Params.add(setGetAPIPostData);
        Loger.MSG("rating_input-->", "" + ((RatingBar) findViewById(R.id.RATINGID)).getRating() + "");

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("exp_message");
        setGetAPIPostData.setValues(((EditText) findViewById(R.id.EDX_message)).getText() + "");
        Params.add(setGetAPIPostData);
        Loger.MSG("exp_message-->", "" + ((EditText) findViewById(R.id.EDX_message)).getText() + "");

        setGetAPIPostData = new SetGetAPIPostData();
        setGetAPIPostData.setPARAMS("user_timezone");
        setGetAPIPostData.setValues(TimeZone.getDefault().getID());
        Params.add(setGetAPIPostData);
        Loger.MSG("user_timezone-->", "" + TimeZone.getDefault().getID());

        CustomJSONParser.ImageParam = "msg_attachment";

        ArrayList<File> PhotoFiles = new ArrayList<>();
        PhotoFiles.add(photofile);
        Loger.MSG("@@", " SIZE-" + PhotoFiles.size() + "");

        new CustomJSONParser().APIForWithPhotoPostMethod(AddReviewActivity.this,AppConstant.BASEURL + "app_add_review", Params, PhotoFiles, new CustomJSONParser.JSONResponseInterface() {
            @Override
            public void OnSuccess(String Result) {
                appLoader.Dismiss();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Result", Result);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void OnError(String Error, String Response) {
                appLoader.Dismiss();
                try {
                    Toast.makeText(AddReviewActivity.this, "" + new JSONObject(Response).getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void OnError(String Error) {
                appLoader.Dismiss();
                if (Error.equalsIgnoreCase(getResources().getString(R.string.please_check_your_internet_connection))){
                    Toast.makeText(AddReviewActivity.this,Error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
