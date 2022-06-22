package sg.edu.np.mad.Assignment1;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class UploadFragment extends Fragment implements View.OnClickListener{

    public UploadFragment(){
        // require a empty public constructor
    }

    Button selectVidBtn;
    Button createListingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        selectVidBtn = (Button) view.findViewById(R.id.selectVid);
        createListingBtn = (Button) view.findViewById(R.id.createListing);

        selectVidBtn.setOnClickListener(this);
        createListingBtn.setOnClickListener(this);



        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectVid:
                Intent myIntent = new Intent(v.getContext(), SelectVideo.class);
                startActivity(myIntent);
                break;

            case R.id.createListing:
                Intent intent = new Intent(v.getContext(), SelectListings.class);
                startActivity(intent);
                break;
        }

    }
}