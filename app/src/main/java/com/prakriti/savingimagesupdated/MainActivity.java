package com.prakriti.savingimagesupdated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
    }

    public void saveImage(View v) {
        // bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable(); // to access image inside imageview
        Bitmap bitmap = bitmapDrawable.getBitmap();

        OutputStream outputStream; // to write data to some location / save image to external storage
            // accepts output bytes and sends them to sink

        // version check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android Q and above
            ContentResolver contentResolver = getContentResolver(); // to access data in content provider i.e send data requests
            ContentValues contentValues = new ContentValues(); // rows of data in content resolver

            // pass image name, type, dir path as key-value pairs
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image" + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "MyImages");

            // access image uri -> identifies data in provider, similar to url for files stored on android external storage
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            try {
                outputStream = contentResolver.openOutputStream(imageUri); // in case it throws exception -> null file
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
}