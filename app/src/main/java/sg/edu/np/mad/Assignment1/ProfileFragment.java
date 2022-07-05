package sg.edu.np.mad.Assignment1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    //Initialising variables
    private TextView username, email;
    private Uri imageURI;
    private Uri picURI;
    private ImageView profilePic;
    private static final int IMAGE_PICK_GALLERY_CODE = 100;
    private ProgressDialog progressDialog;


    Button changeUsernamebtn, changePasswordbtn, signoutButton, uploadProfilePicbtn;

    DatabaseReference mDatabase;

    public ProfileFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        //Get current user that is Logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //setup progress dialog
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);

        //Assigning layout ID's
        changeUsernamebtn = (Button) view.findViewById(R.id.button5);
        changePasswordbtn = (Button) view.findViewById(R.id.button6);
        uploadProfilePicbtn = (Button) view.findViewById(R.id.addPicture);
        signoutButton = (Button) view.findViewById(R.id.button7);
        profilePic = (ImageView) view.findViewById(R.id.imageView5);
        username = (TextView) view.findViewById(R.id.textView8);
        email = (TextView) view.findViewById(R.id.textView9);

        //Setting current user Email Address to TextView
        email.setText(user.getEmail());

        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    username.setText("Username: " + String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });

        //Finding Picture in Realtime Database through current User Email Address
        mDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                }
            }
        });

        //If ChangeUsername button is clicked
        changeUsernamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to ChangeUsername Activity
                Intent intent = new Intent(view.getContext(), ChangeUsername.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //If ChangePassword button is clicked
        changePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to ChangePassword Activity
                Intent intent = new Intent(view.getContext(), ChangePassword.class);
                intent.putExtra("Email", user.getEmail());
                startActivity(intent);
            }
        });

        //If SignOut button is clicked
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log current user out of application to prevent auto login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartUpPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        uploadProfilePicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickGallery();
            }
        });

        return view;
    }

    private void imagePickGallery(){
        //pick image from gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"), IMAGE_PICK_GALLERY_CODE);
    }

    private void setImageToImageView(){
        profilePic.setImageURI(imageURI);
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called after picking image from camera/gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                imageURI = data.getData();
                uploadProfilePic();
                //show picked image in imageview
                setImageToImageView();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Method to upload Listings
    private void uploadProfilePic() {
        //show progress bar
        progressDialog.show();

        //filepath and name in storage
        String filePathAndName = "ProfilePic/" + "pic_";

        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        //upload image of any file type
        storageReference.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image uploaded, get url of img
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            //url of uploaded image is received

                            //add details to image in firebase
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("profilepicUrl", "" + downloadUri);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String currentEmail = user.getEmail().toString().replace(".", "").trim();;

                            //Get Realtime Database instance
                            DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
                            reference.child(currentEmail)
                                    .updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //listing added to db
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Listing has been uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //listing details failed to add to db
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Listing details failed to upload. Try again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Failed to upload to Database", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}