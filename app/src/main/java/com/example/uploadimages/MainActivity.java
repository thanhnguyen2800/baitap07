package com.example.uploadimages;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnChoose, btnUpload;
    ImageView imageViewChoose, imageViewUpload;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    private String userId;

    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = MainActivity.class.getName();

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        return;
                    }
                    mUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                        imageViewChoose.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getStringExtra("USER_ID");

        AnhXa();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait upload....");

        btnChoose.setOnClickListener(v -> CheckPermission());

        btnUpload.setOnClickListener(v -> {
            if (mUri != null) {
                UploadImage();
            }
        });
    }

    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewUpload = findViewById(R.id.imgMultipart);
        imageViewChoose = findViewById(R.id.imgChoose);
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }

        // Reading external storage permission is sufficient for gallery access.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, permissions(), MY_REQUEST_CODE);
        }
    }

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    public static String[] storge_permissions = {
            // Write permission is not needed for reading from gallery.
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivityResultLauncher.launch(intent);
    }

    private void UploadImage() {
        mProgressDialog.show();

        try {
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);

            // ** SOLUTION: Robustly read file from URI without using RealPathUtil **
            InputStream inputStream = getContentResolver().openInputStream(mUri);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = byteStream.toByteArray();
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileBytes);
            MultipartBody.Part partbodyavatar = MultipartBody.Part.createFormData("avatar", "upload.jpg", requestFile);

            ServiceAPI.serviceapi.updateImage(id, partbodyavatar).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Message message = response.body();
                        if (message != null) {
                            String newAvatarUrl = message.getMessage();
                            Toast.makeText(MainActivity.this, "Tải ảnh lên thành công!", Toast.LENGTH_LONG).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.USER_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("avatar_uri", newAvatarUrl);
                            editor.apply();

                            finish(); // Go back to ProfileActivity
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Tải ảnh lên thất bại (server error)", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Log.e(TAG, "Upload error: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Gọi API thất bại", Toast.LENGTH_LONG).show();
                }
            });

        } catch (IOException e) {
            mProgressDialog.dismiss();
            Log.e(TAG, "File reading error: " + e.getMessage());
            Toast.makeText(this, "Không thể đọc file ảnh", Toast.LENGTH_SHORT).show();
        }
    }
}
