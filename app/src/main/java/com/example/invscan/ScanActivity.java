package com.example.invscan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ScanActivity extends AppCompatActivity {

    private ImageView captureIV;
    private TextView resultIV;
    private Button photoBtn,sendBtn;
    private EditText res;
    private static final String lang = "eng";
    String result = "";

    Uri uri;

    private TessBaseAPI tessBaseApi;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2;

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/tesseract/";
    private static final String TESSDATA = "tessdata";

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
                doOCR();
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
                    CropImage.startPickImageActivity(ScanActivity.this);
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

    private void CropImage(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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

    private void doOCR() {
        prepareTesseract();
        startOCR();
    }

    private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Created directory " + path, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyTessDataFiles(TESSDATA);
    }

    private void copyTessDataFiles(String path) {
        try {
            String[] fileList = getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Toast.makeText(this, "Copied " + fileName + "to tessdata", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Unable to copy files to tessdata ", Toast.LENGTH_SHORT).show();
        }
    }

    private void startOCR() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath, options);

            result = extractText(bitmap);
            resultIV.setText(result);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private String extractText(Bitmap bitmap) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Toast.makeText(this, "TessBaseAPI is null. TessFactory not returning tess object.", Toast.LENGTH_SHORT).show();
            }
        }

        tessBaseApi.init(DATA_PATH, lang);

        Toast.makeText(this, "Training file loaded", Toast.LENGTH_SHORT).show();
        tessBaseApi.setImage(bitmap);
        String extractedText = "empty img";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Toast.makeText(this, "Error in recognizing text.", Toast.LENGTH_SHORT).show();
        }
        tessBaseApi.end();
        return extractedText;
    }

}