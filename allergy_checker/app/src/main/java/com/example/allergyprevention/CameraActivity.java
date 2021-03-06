package com.example.allergyprevention;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;

    ImageView image_view;
    Button capture_btn;
    Button detect_btn;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri saveUri;

    String userid;
    String userallergy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("userId");
        userallergy = bundle.getString("userAllergy");

        image_view = (ImageView) findViewById(R.id.picture);
        capture_btn = (Button) findViewById(R.id.capture);
        detect_btn = (Button) findViewById(R.id.detection);

        image_view.setVisibility(View.INVISIBLE);
        detect_btn.setVisibility(View.INVISIBLE);

        capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_btn.setVisibility(View.GONE);
                captureCamera();
            }
        });

        detect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), OcrActivity.class);
                intent2.putExtra("userId", userid);
                intent2.putExtra("userAllergy", userallergy);
                intent2.putExtra("uri", imageUri);
                startActivity(intent2);
            }
        });
        checkPermission();
    }

    public void backhome_clicked(View v){
        Intent backintent = new Intent(getApplicationContext(), ContentActivity.class);
        backintent.putExtra("userId", userid);
        backintent.putExtra("userAllergy", userallergy);
        startActivity(backintent);
    }

    private void captureCamera(){
        String state = Environment.getExternalStorageState();
        // ?????? ????????? ??????
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ?????? ????????? ??????

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile??? ??? ?????? ????????? Manifest provier??? authorites??? ???????????? ???

                    Uri providerURI = FileProvider.getUriForFile(this, "com.example.allergyprevention", photoFile);
                    imageUri = providerURI;

                    // ???????????? ????????? ?????? FileProvier??? Return?????? content://??????!!, providerURI??? ?????? ????????? ???????????? ?????? ??????
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "??????????????? ?????? ???????????? ???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "capture");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = Environment.getExternalStorageDirectory() + "/Pictures/capture/tempimg.jpg";
        saveUri = Uri.fromFile(imageFile);

        return imageFile;
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // ?????? ????????? ?????? ????????? ?????????(?????? ????????? ???????????? ????????? ???????????? ??? ???)
        File f = new File(mCurrentPhotoPath);
        if (!f.exists()){
            f.delete();
        }
        File n_f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(n_f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        image_view.setImageURI(saveUri);
        image_view.setVisibility(View.VISIBLE);
        detect_btn.setVisibility(View.VISIBLE);
        Toast.makeText(this, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i("REQUEST_TAKE_PHOTO", "OK");

                        galleryAddPic();
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                } else {
                    Toast.makeText(CameraActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // ?????? ???????????? if()?????? ????????? false??? ?????? ??? -> else{..}??? ???????????? ?????????
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("??????")
                        .setMessage("????????? ????????? ?????????????????????. ????????? ???????????? ???????????? ?????? ????????? ?????? ??????????????? ?????????.")
                        .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : ????????? ????????? 0, ????????? ????????? -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(CameraActivity.this, "?????? ????????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // ??????????????? ??? ????????????..

                break;
        }
    }
}
