package com.example.invscan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ScanActivity extends AppCompatActivity {

    private ImageView captureIV;
    private TextView resultIV;
    private Button photoBtn,sendBtn;
    private Bitmap imageBitmap;
    private EditText res;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;

    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        captureIV = findViewById(R.id.PhotoCaptureImage);
        resultIV = findViewById(R.id.DetectedText);
        photoBtn = findViewById(R.id.NewPhoto);
        sendBtn = findViewById(R.id.PutInvNum);
        res = findViewById(R.id.search_field);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectText();
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ScanActivity.this,
                            new String[]{
                                    Manifest.permission.CAMERA,
                            },
                            REQUEST_CODE_PERMISSION
                    );
                } else {
                    dispatchCaptureImageIntent();
                }
            }
        });
    }

    private void dispatchCaptureImageIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException exception) {
                Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(
                        this,
                        "com.example.invscan.fileprovider",
                        imageFile
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,REQUEST_CODE_CAPTURE_IMAGE);
            }
        }

    }

    private File createImageFile() throws IOException {
        String fileName = "IMAGE_"
                + new SimpleDateFormat(
                        "yyyy_MM_dd_HH_mm_ss", Locale.getDefault()
        ).format(new Date());

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                fileName,
                ".jpg",
                directory
        );
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchCaptureImageIntent();
            }else {
                Toast.makeText(this, "Not all permissions granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            try {
                captureIV.setImageBitmap(getScaledBitmap(captureIV));

                File capturedImageFile = new File(currentImagePath);

            } catch (Exception exception) {
                Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getScaledBitmap(ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int scaleFactor = Math.min(
                options.outWidth / imageView.getWidth(),
                options.outHeight / imageView.getHeight()
        );

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        return BitmapFactory.decodeFile(currentImagePath, options);
    }

    private void detectText() {

    }


}