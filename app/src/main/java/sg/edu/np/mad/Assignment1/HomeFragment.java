package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.edu.np.mad.Assignment1.databinding.FragmentVideoBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    //Initialising variables
    Button chatButton, mapButton, helpButton,  likesButton, eventButton;

    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Assigning layout ID's
        chatButton = (Button) view.findViewById(R.id.button);
        helpButton = (Button) view.findViewById(R.id.button4);
        mapButton = (Button) view.findViewById(R.id.buttonMap);
        likesButton = (Button) view.findViewById(R.id.likesButton);
        eventButton = (Button) view.findViewById(R.id.buttonEvent);

        //Set onClickListener for respective buttons in Home Page
        chatButton.setOnClickListener(this);

        helpButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        likesButton.setOnClickListener(this);
        eventButton.setOnClickListener(this);

        return view;
    }

    //Calling method from MainActivity when Button is clicked
    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).onButtonSelected(v);
    }
}