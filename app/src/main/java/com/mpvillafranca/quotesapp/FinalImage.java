package com.mpvillafranca.quotesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FinalImage extends AppCompatActivity {

    private String tempimagepath;
    Button btn_save,btn_share;
    ImageView finalimag;
    private Boolean saveRequested = false;
    private Boolean shareRequested = false;

    private static final int EXTERNAL_STORAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_image);

        btn_save=(Button)findViewById(R.id.save_but);
        btn_share=(Button)findViewById(R.id.share_but);
        finalimag=(ImageView)findViewById(R.id.final_img);
        finalimag.setImageBitmap(QuoteActivity.cpture_bmp);

        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareRequested = true;
                if (Build.VERSION.SDK_INT >= 23){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        startShare();
                    } else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getApplicationContext(), "External storage permission required to save images", Toast.LENGTH_SHORT).show();
                        }
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
                    }
                } else {
                    startShare();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveRequested = true;
                if (Build.VERSION.SDK_INT >= 23){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        startSave();
                    } else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getApplicationContext(), "External storage permission required to save images", Toast.LENGTH_SHORT).show();
                        }
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST);
                    }
                } else {
                    startSave();
                }
            }
        });

    }

    private void startShare(){
        File file = new File(saveImageFoShare(getString(R.string.app_name), 100, QuoteActivity.cpture_bmp));
        boolean flag = file.exists();
        Uri uri = null;
        if (flag)
        {
            uri = Uri.fromFile(file);
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
        shareRequested = false;
    }

    private void startSave(){
        File file = new File(saveImageFoShare(getString(R.string.app_name), 100, QuoteActivity.cpture_bmp));
        boolean flag = file.exists();
        if(flag)
        {
            Toast.makeText(FinalImage.this, "Saved", Toast.LENGTH_SHORT).show();
        }
        saveRequested = false;
    }

    private String saveImageFoShare(String s, int i, Bitmap bitmap)
    {
        String s1;
        s1 = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append(File.separator).append(s).append(File.separator).toString();
        (new File(s1)).mkdirs();
        File file;
        boolean flag;
        (new android.graphics.BitmapFactory.Options()).inSampleSize = 5;
        String s2 = UUID.randomUUID().toString();
        tempimagepath = (new StringBuilder(String.valueOf(s1))).append(s2).append(".jpg").toString();
        file = new File(tempimagepath);
        flag = file.exists();
        String as[] = null;
        try
        {
            if (flag)
            {
                file.delete();
                file.createNewFile();
            }
            else
            {
                file.createNewFile();
            }

            FileOutputStream fileoutputstream = new FileOutputStream(file);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, i, bufferedoutputstream);
            bufferedoutputstream.flush();
            bufferedoutputstream.close();
            as = new String[1];
            as[0] = file.toString();
        }
        catch (NullPointerException nullpointerexception) { }

        catch (IOException e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this, as, null, new android.media.MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String s1, Uri uri)
            {
            }
        });
        return tempimagepath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(saveRequested)
                    startSave();
                if(shareRequested)
                    startShare();
            } else {
                Toast.makeText(getApplicationContext(), "External write permission has not been granted, cannot save images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
