package com.moha.alcchallenge2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {
    ImageView imageView;
    EditText titleET, messageET, priceET;
    Button button;

    String message, title, imageUrl;
    int price;
    private static final int RC_PHOTO_PICKER = 1;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotosStorageReference;
    private DatabaseReference mDatabaseRefereence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotosStorageReference = mFirebaseStorage.getReference().child("photos");
        mDatabaseRefereence = mFirebaseDatabase.getReference().child("Travelmantics");

        imageView = findViewById(R.id.imageView);
        titleET = findViewById(R.id.titleET);
        messageET = findViewById(R.id.messageET);
        priceET = findViewById(R.id.priceET);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem adminMenuItem = menu.findItem(R.id.admin);
        adminMenuItem.setVisible(false);
        MenuItem signOutMenuItem = menu.findItem(R.id.sign_out_menu);
        signOutMenuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (validateNonEmptyValues()) {
                    Travelmantics newTravelmantics = new Travelmantics(title, message, imageUrl, price);
                    mDatabaseRefereence.push().setValue(newTravelmantics);
                    emptyViews();
                }
                return true;
            case R.id.homeActivity:
                startActivity(new Intent(AdminActivity.this, UserActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean validateNonEmptyValues() {
        title = titleET.getText().toString().trim();
        message = messageET.getText().toString().trim();
        price = Integer.valueOf(priceET.getText().toString().trim());

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, R.string.title_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, R.string.message_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(priceET.toString().trim())) {
            Toast.makeText(this, R.string.price_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, R.string.image_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void emptyViews() {
        message = "";
        title = "";
        imageUrl = "";
        price = 0;

        messageET.setText("");
        priceET.setText("");
        titleET.setText("");
        Glide.with(imageView.getContext())
                .load("")
                .into(imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Glide.with(imageView.getContext())
                    .load(selectedImageUri)
                    .into(imageView);
            final StorageReference photoRef = mPhotosStorageReference.child(selectedImageUri.getLastPathSegment());


            UploadTask uploadTask = photoRef.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUrl = downloadUri.toString();


                    } else {
                        Toast.makeText(AdminActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
