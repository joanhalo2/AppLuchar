package org.grupovialibre.dev.appluchar;

/**
 * Created by joan on 20/10/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private EditText mLocation;
    private EditText mActors;
    private EditText mType;
    private EditText mAttendess;
    private EditText mSection;
    private EditText mStatus;
    private ProgressDialog progressDialog;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, Report report, LatLng reportLocation) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        if(reportLocation!=null){
            args.putDouble("latitude",reportLocation.latitude);
            args.putDouble("longitude",reportLocation.longitude);
        }

        if(report!=null){
            args.putParcelable("newReport",report);
        }


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Report currentReport = null;

        Double latitude = 0.0;

        Double longitude = 0.0;

        if(getArguments().getDouble("latitude")!=0.0 && getArguments().getDouble("longitude")!=0.0){
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");
        }
        if(getArguments().getParcelable("newReport")!=null){
            currentReport = getArguments().getParcelable("newReport");
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER)==2){
            View rootView = inflater.inflate(R.layout.fragment_report_sub_page_hhrr, container, false);
            return rootView;
        }else if (getArguments().getInt(ARG_SECTION_NUMBER)==3){
            View rootView = inflater.inflate(R.layout.fragment_report_sub_page_resources, container, false);
            return rootView;
        }else{

            View rootView = inflater.inflate(R.layout.fragment_report, container, false);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            progressDialog = new ProgressDialog(rootView.getContext());

            mLocation = (EditText) rootView.findViewById(R.id.locationEdit);
            mActors = (EditText) rootView.findViewById(R.id.actorsEdit);
            mType = (EditText) rootView.findViewById(R.id.typeEdit);
            mAttendess = (EditText) rootView.findViewById(R.id.attendeesEdit);
            mSection = (EditText) rootView.findViewById(R.id.sectionEdit);
            mStatus = (EditText) rootView.findViewById(R.id.statusEdit);


            if(latitude!=0.0 && longitude!=0.0){
                mLocation.setText(""+latitude+","+longitude);
            }

            if(currentReport!=null){
                mLocation.setText(currentReport.getLocation());
                mActors.setText(currentReport.getActors());
                mType.setText(currentReport.getType());
                mAttendess.setText(String.valueOf(currentReport.getAttendees()));
                mSection.setText(currentReport.getSection());
                mStatus.setText(currentReport.getStatus());


                if(!mAuth.getCurrentUser().getUid().equals(currentReport.getUserID())){
                    disableEditText(mLocation,mActors,mType,mAttendess,mSection,mStatus);
                }

            }


            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.saveReportButton);
            fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getResources().getString(R.string.saving_report));
                progressDialog.show();
                String location = mLocation.getText().toString();
                String actors = mActors.getText().toString();
                String type = mType.getText().toString();
                int attendees = new Integer(mAttendess.getText().toString());
                String section = mSection.getText().toString();
                String status = mStatus.getText().toString();
                String dateTime = (new Date()).toString();

                Report report = new Report(mAuth.getCurrentUser().getUid(),
                        location,
                        actors,
                        type,
                        attendees,
                        section,
                        status,
                       dateTime);

                saveAndUpdateReport(report,view);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            });






            return rootView;
        }




    }

    private void disableEditText(EditText... editTexts) {

        for(EditText editText : editTexts){
            editText.setFocusable(false);
            editText.setEnabled(false);
            editText.setCursorVisible(false);
            editText.setKeyListener(null);
            editText.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    private void saveAndUpdateReport(Report newReport,final View view){
        String key = mDatabase.child("Reports").push().getKey();


        Map<String, Object> postValues = newReport.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Reports/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(view.getContext(),AccountActivity.class));
                    Snackbar.make(view, getResources().getString(R.string.saving_report), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

    }
}
