package com.mpvillafranca.quotesapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class QuoteActivity extends AppCompatActivity implements TypeTextFragment.TypeTextListener {

    RelativeLayout parent;
    private Button ImageButton;
    private Button DoneButton;

    static Bitmap cpture_bmp = null;
    View v1;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int EXTERNAL_STORAGE_CAMERA_REQUEST = 3;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 4;

    private static final String IMAGE_DIRECTORY_NAME = "Quotes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());


        final String[] items = new String[] {"Camera", "Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    // Camera
                    if (Build.VERSION.SDK_INT >= 23){
                        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                                callCameraApp();
                        } else{
                            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                Toast.makeText(getApplicationContext(), "External storage and camera permission required to save images", Toast.LENGTH_SHORT).show();
                            }
                            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_STORAGE_CAMERA_REQUEST);
                        }
                    } else {
                        callCameraApp();
                    }
                    dialog.cancel();
                }else{
                    // Gallery
                    if (Build.VERSION.SDK_INT >= 23){
                        if(ContextCompat.checkSelfPermission(QuoteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                        } else {
                            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                Toast.makeText(getApplicationContext(), "External storage permission required to save images", Toast.LENGTH_SHORT).show();
                            }
                            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
                        }
                    } else {
                        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                    }
                }
            }
        });
        final AlertDialog dialog = builder.create();

        parent = (RelativeLayout)findViewById(R.id.parent_rel);
        ImageButton = (Button) findViewById(R.id.image_but);
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        DoneButton = (Button) findViewById(R.id.done_but);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1 = parent.findViewById(R.id.mainlay_rel);
                v1.setDrawingCacheEnabled(true);
                v1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());
                v1.buildDrawingCache();
                cpture_bmp = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false); // Limpiar la cache de dibujado

                startActivity(new Intent(QuoteActivity.this, FinalImage.class));
            }
        });
    }

    private void callCameraApp(){
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Something goes wrong while taking photos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createQuote(String quote, String author){
        PictureFragment picFragment = (PictureFragment) getSupportFragmentManager().findFragmentById(R.id.picture_fragment);
        picFragment.setQuoteText(quote, author);
    }

    private void createImage(Bitmap bitmap){
        PictureFragment picFragment = (PictureFragment) getSupportFragmentManager().findFragmentById(R.id.picture_fragment);
        picFragment.setImage(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap imageBitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    createImage(imageBitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something goes wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap imageBitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    createImage(imageBitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something goes wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == EXTERNAL_STORAGE_CAMERA_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callCameraApp();
            } else {
                Toast.makeText(getApplicationContext(), "External write permission or camera permission has not been granted, cannot open camera and save images", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "External read permission has not been granted, cannot read images", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
