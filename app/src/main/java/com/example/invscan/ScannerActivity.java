package com.example.invscan;

import static com.example.invscan.utils.ConstsKt.getImgIdByCategory;
import static com.example.invscan.utils.ConstsKt.getNameByCategory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.invscan.domain.enteties.InventoryItem;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ScannerActivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;
    Integer counting = 1;
    String out;

    private ScanViewModel viewModel;
    private static final int CAMERA_REQ_CODE = 200;
    private static final int STORAGE_REQ_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;


    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getFoundedItem().observe(this, new Observer<InventoryItem>() {
            @Override
            public void onChanged(InventoryItem inventoryItem) {
                if (counting == 1) {
                    alertDialog(inventoryItem);
                }
            }
        });
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final SearchFragment myFragment = new SearchFragment();

        viewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        mResultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIV);

        Button capturePhoto = findViewById(R.id.NewPhotoBT);
        Button galleryPhoto = findViewById(R.id.GalleryBT);
        Button realtimeObj = findViewById(R.id.realtimeobj);

        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCameraPermission()) {
                    // Denied
                    requestCameraPermission();
                }
                else {
                    // Accept
                    pickCamera();
                }
            }
        });

        galleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    // Denied
                    requestStoragePermission();
                }
                else {
                    // Accept
                    pickGallery();
                }
            }
        });

        realtimeObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCameraPermission()) {
                    // Denied
                    requestCameraPermission();
                }
                else {
                    // Accept
                    pickCamera();
                }
            }
        });

        // Camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQ_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQ_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQ_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQ_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(this).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    counting = 1;
                    viewModel.getItemByNum(sb.toString());
                    mResultEt.setText(sb.toString());
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toast.makeText(this,""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    AlertDialog alertDialogg;
    @SuppressLint("SetTextI18n")
    private void alertDialog(InventoryItem foundedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        if (foundedItem == null){
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_error_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
            );
            builder.setView(view);
            ((TextView) view.findViewById(R.id.textTitle)).setText("Объект не найден");
            ((TextView) view.findViewById(R.id.textMessage)).setText("Инв. номер не найден " +
                    "\nОбласть обрезки не правильна " +
                    "\nОшибка определения");
            ((Button) view.findViewById(R.id.buttonAction)).setText("ОК");
            ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

            alertDialogg = builder.create();

            view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogg.cancel();
                }
            });

            if (alertDialogg.getWindow() != null) {
                alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            counting = 0;
            alertDialogg.show();

            ViewFlipper viewflip = findViewById(R.id.viewFlipper);
            viewflip.setVisibility(View.INVISIBLE);
        } else {

            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_warning_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
            );
            builder.setView(view);
            ((TextView) view.findViewById(R.id.textTitle)).setText("Объект найден!");
            ((TextView) view.findViewById(R.id.textMessage)).setText("Это - "
                    + getNameByCategory(foundedItem.getCategory_id()).substring(0, getNameByCategory(foundedItem.getCategory_id()).length() - 1) + "\n Хотите добавить в поиск?");
            ((Button) view.findViewById(R.id.buttonNo)).setText("Нет");
            ((Button) view.findViewById(R.id.buttonYes)).setText("Да");
            ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);

            alertDialogg = builder.create();

            view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogg.cancel();
                }
            });

            view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialogg.cancel();
                    SearchFragment.SELECTED_ITEM_INVENTORY = foundedItem;
                    finish();
                }
            });

            if (alertDialogg.getWindow() != null) {
                alertDialogg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            counting = 0;
            alertDialogg.show();

            ViewFlipper viewflip = findViewById(R.id.viewFlipper);
            viewflip.setVisibility(View.INVISIBLE);
            mPreviewIv.setImageResource(getImgIdByCategory(foundedItem.getCategory_id()));
        }
    }

}