package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class SelectListings extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText listingTitle;
    private EditText listingDesc;
    private ImageView listingImage;
    private Button selectImg;
    private Button uploadListing;


    private static final int IMAGE_PICK_GALLERY_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private String[] cameraPermissions;
    private ProgressDialog progressDialog;
    private String title;
    private String desc;
    private Uri imageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_listings);

        //Action Bar CODES//
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        actionBar.setTitle("Select Image to Upload");
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Action Bar CODES//


        listingTitle = findViewById(R.id.editTextListingTitle);
        listingDesc = findViewById(R.id.editTextListingDesc);
        listingImage = findViewById(R.id.listingImage);
        selectImg = findViewById(R.id.selectImage);
        uploadListing = findViewById(R.id.uploadListing);

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);

        //camera permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        selectImg.setOnClickListener(this);
        uploadListing.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectImage:
                videoPickDialog();
                break;

            case R.id.uploadListing:
                title = listingTitle.getText().toString().trim();
                desc = listingDesc.getText().toString().trim();
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(SelectListings.this, "Title is required for the Listing", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(desc)){
                    Toast.makeText(SelectListings.this, "Description is required for the Listing", Toast.LENGTH_SHORT).show();
                }
                else if (imageURI==null){
                    //video is not picked
                    Toast.makeText(SelectListings.this, "Please choose a Image to upload", Toast.LENGTH_SHORT).show();
                }
                else{
                    //upload video to firebase
                    uploadVideoFirebase();
                }
                break;
        }
    }

    private void uploadVideoFirebase() {
        //show progress bar
        progressDialog.show();

        //timestamp
        String timestamp = "" + System.currentTimeMillis();

        //filepath and name in storage
        String filePathAndName = "Listings/" + "listing_" + timestamp;

        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        //upload video of any file type
        storageReference.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //video uploaded, get url of vid
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            //url of uploaded vid is received

                            //add details to vid in firebase
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", "" + timestamp);
                            hashMap.put("title", "" + title);
                            hashMap.put("desc", "" + desc);
                            hashMap.put("imageUrl", "" + downloadUri);

                            DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
                            reference.child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //videos added to db
                                            progressDialog.dismiss();
                                            Toast.makeText(SelectListings.this, "Video has been uploaded to Database", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //video details failed to add to db
                                            progressDialog.dismiss();
                                            Toast.makeText(SelectListings.this, "Image details failed to upload to Database", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading to storage
                        progressDialog.dismiss();
                        Toast.makeText(SelectListings.this, "Image failed to upload to Database", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void videoPickDialog() {
        String[] options = {"Camera", "Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    //camera clicked
                    if (!checkCameraPermission()){
                        //camera permission not allowed
                        requestCameraPermission();
                    }
                    else{
                        //permission allowed, take pic
                        imagePickCamera();
                    }
                }
                else if (i == 1){
                    //gallery clicked
                    imagePickGallery();
                }
            }
        }).show();
    }

    private void requestCameraPermission(){
        //request camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }

    private void imagePickGallery(){
        //pick image from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"), IMAGE_PICK_GALLERY_CODE);
    }

    private void imagePickCamera(){
        //pick image from camera - intent
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void setImageToImageView(){
        listingImage.setImageURI(imageURI);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    //check permission allowed or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        //both permissions allowed
                        imagePickCamera();
                    }
                    else{
                        //both or one not allowed
                        Toast.makeText(this, "Camera & Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called after picking image from camera/gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                imageURI = data.getData();
                //show picked image in imageview
                setImageToImageView();
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                Log.d("Camera", "gg");
                imageURI = data.getData();
                //show picked image in imageview
                setImageToImageView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); // go back previous activity
        finish();
        return super.onSupportNavigateUp();
    }
}