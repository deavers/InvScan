package com.example.invscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.zip.Inflater;

public class ScanActivity extends AppCompatActivity {

    private ImageView captureIV;
    private TextView resultIV;
    private Button photoBtn,sendBtn;
    private Bitmap imageBitmap;
    private EditText res;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_PEQUEST_CODE = 102;

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
                sendText();
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                detectText();
            }
        });
    }

    private void sendText() {
    }

    private void detectText() {
    }

    private void captureImage() {
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_PEQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PEQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            captureIV.setImageBitmap(image);
        }
    }
}