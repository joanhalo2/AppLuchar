package org.grupovialibre.dev.appluchar.tabs;

/**
 * Created by joan on 20/10/16.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.grupovialibre.dev.appluchar.AccountActivity;
import org.grupovialibre.dev.appluchar.CurrentReportsMapsActivity;
import org.grupovialibre.dev.appluchar.R;
import org.grupovialibre.dev.appluchar.entities.Denunciation;
import org.grupovialibre.dev.appluchar.entities.Media;
import org.grupovialibre.dev.appluchar.entities.Report;
import org.grupovialibre.dev.appluchar.mListView.HHRRCustomAdapter;
import org.grupovialibre.dev.appluchar.mListView.MediaCustomAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private static final int GALLERY_REQUEST = 1;

    private static final int CAMERA_REQUEST = 2;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseHHRR;
    private DatabaseReference mDatabaseMedia;
    private StorageReference mStorage;

    private Query mQueryCurrentReportHHRR;
    private Query mQueryCurrentReportMedia;

    private ArrayList<Denunciation> denunciations = new ArrayList<Denunciation>();
    private ArrayList<Media> mediaObjects = new ArrayList<Media>();
    private HHRRCustomAdapter hhrrAdapter;
    private MediaCustomAdapter mediaAdapter;
    private EditText mLocationLat;
    private EditText mLocationLong;
    private EditText mLocationPlace;
    private Spinner mTypeSpinner;
    private ArrayAdapter typeAdapter;
    private TextView spinnerDialogText;
    private TextInputEditText mTypeEdit;
    private TextInputEditText mActors;
    private TextInputEditText mSection;
    private TextInputEditText mFechaEdit;
    private TextInputLayout mFechaLabel;
    private TextInputLayout mTypeLabel;
    private TextInputEditText mTagsEdit;
    private TextInputLayout mTagsLabel;
    private TextInputEditText mDescEdit;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private RelativeLayout relSpinner;
    private FloatingActionButton galleryFab;


    private ImageButton mSelectImage;
    private Uri imageURI;

    private TextView denSpinnerDialogText;

    private int day;
    private int month;
    private int year;
    private static final int DIALOG_TYPE = 0;
    private static DatePickerDialog.OnDateSetListener listenerDateSelector;

    private static final String ARG_SECTION_NUMBER = "section_number";


    private HashMap<String, CheckBox> mapChecks;
    private ScrollView scrollView;


    private String currentUserName;

    private String mImageFileLocation = "";

    File photoFile = null;

    Report currentReport;


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

        if (reportLocation != null) {
            args.putDouble("latitude", reportLocation.latitude);
            args.putDouble("longitude", reportLocation.longitude);
        }

        if (report != null) {
            args.putParcelable("newReport", report);
        }


        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentReport = null;

        Double latitude = 0.0;

        Double longitude = 0.0;

        if (getArguments().getDouble("latitude") != 0.0 && getArguments().getDouble("longitude") != 0.0) {
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");
        }
        if (getArguments().getParcelable("newReport") != null) {
            currentReport = getArguments().getParcelable("newReport");
        }

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //System.out.println(dataSnapshot.getValue());
                Map<String, String> map = (Map) dataSnapshot.getValue();
                String u_name = map.get("username");
                setCurrentUserName(u_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            Log.e("SECTION_2", "Ingresa seccion 2");

            final View rootView = inflater.inflate(R.layout.fragment_report_sub_page_hhrr, container, false);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabaseHHRR = FirebaseDatabase.getInstance().getReference().child("HHRR");
            progressDialog = new ProgressDialog(rootView.getContext());

            final ListView listView = (ListView) rootView.findViewById(R.id.hhrrListView);
            hhrrAdapter = new HHRRCustomAdapter(this.getActivity(), getDenunciations(currentReport));
            listView.setAdapter(hhrrAdapter);


            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.newDenunciationButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showNewDenunciationDialog(inflater, rootView);
                }
            });
            //hhrrAdapter.notifyDataSetChanged();
            return rootView;


        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
            Log.e("SECTION_3", "Ingresa seccion 3");

            final View rootView = inflater.inflate(R.layout.fragment_report_sub_page_resources, container, false);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabaseMedia = FirebaseDatabase.getInstance().getReference().child("Media");
            progressDialog = new ProgressDialog(rootView.getContext());

            final ListView listView = (ListView) rootView.findViewById(R.id.mediaListView);
            mediaAdapter = new MediaCustomAdapter(this.getActivity(), getMedia(currentReport));
            listView.setAdapter(mediaAdapter);

            FloatingActionButton cameraFab = (FloatingActionButton) rootView.findViewById(R.id.newCameraMediaButton);
            cameraFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                    startActivityForResult(intent, CAMERA_REQUEST);

                    //showNewDenunciationDialog(inflater, rootView);
                }
            });

            galleryFab = (FloatingActionButton) rootView.findViewById(R.id.newGalleryMediaButton);


            galleryFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showSubmitMediaDialog(inflater, rootView);
                }
            });


            /*FloatingActionButton audioFab = (FloatingActionButton) rootView.findViewById(R.id.newAudioMediaButton);
            audioFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //showNewDenunciationDialog(inflater, rootView);
                }
            });*/

            return rootView;


        } else {

            Log.e("SECTION_1", "Ingresa seccion 1");

            final View rootView = inflater.inflate(R.layout.fragment_report, container, false);


            typeAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.action_type_spinner_options, android.R.layout.simple_spinner_item);
            mTypeSpinner = (Spinner) rootView.findViewById(R.id.typeSpinnerDialog);
            mTypeSpinner.setAdapter(typeAdapter);
            mTypeSpinner.setOnItemSelectedListener(this);

            relSpinner = (RelativeLayout) rootView.findViewById(R.id.relSpinner);


            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            progressDialog = new ProgressDialog(rootView.getContext());

            mLocationLat = (EditText) rootView.findViewById(R.id.locationLat);
            mLocationLong = (EditText) rootView.findViewById(R.id.locationLng);
            mLocationPlace = (EditText) rootView.findViewById(R.id.LocationPlaceEdit);
            mActors = (TextInputEditText) rootView.findViewById(R.id.actorsEdit);
            //mAttendess = (EditText) rootView.findViewById(R.id.attendeesEdit);
            mSection = (TextInputEditText) rootView.findViewById(R.id.sectionEdit);
            mSection.setKeyListener(null);

            mFechaLabel = (TextInputLayout) rootView.findViewById(R.id.labelDate);
            mTypeEdit = (TextInputEditText) rootView.findViewById(R.id.typeEdit);
            mTypeLabel = (TextInputLayout) rootView.findViewById(R.id.labelType);
            mFechaEdit = (TextInputEditText) rootView.findViewById(R.id.dateEdit);
            mFechaEdit.setKeyListener(null);

            mTagsEdit = (TextInputEditText) rootView.findViewById(R.id.editTags);
            mTagsLabel = (TextInputLayout) rootView.findViewById(R.id.labelTags);

            mDescEdit = (TextInputEditText) rootView.findViewById(R.id.descriptionEdit);

            //mStatus = (EditText) rootView.findViewById(R.id.statusEdit);
            requestQueue = Volley.newRequestQueue(rootView.getContext());



            if (latitude != 0.0 && longitude != 0.0) {
                mLocationLat.setText("" + latitude);
                mLocationLong.setText("" + longitude);


                JsonObjectRequest request = new JsonObjectRequest(
                        "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                                + latitude
                                + ","
                                + longitude
                                + "&key="
                                + getResources().getString(R.string.google_maps_geocode_key)
                        ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                    mLocationPlace.setText(address);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mLocationPlace.setText("ERROR GPS");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLocationPlace.setText("ERROR GPS");
                    }
                }

                );
                requestQueue.add(request);


            }

            ImageButton openMap = (ImageButton) rootView.findViewById(R.id.openMapButton);

            openMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentReport!=null && currentReport.getReportID()!=null){
                        Intent mIntent = new Intent(rootView.getContext(), CurrentReportsMapsActivity.class);
                        ArrayList<Report> reports = new ArrayList<Report>();
                        reports.add(currentReport);
                        mIntent.putParcelableArrayListExtra("locations",reports);
                        startActivity(mIntent);
                    }

                }
            });

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.saveReportButton);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.setMessage(getResources().getString(R.string.saving_report));
                    progressDialog.show();


                    boolean validation = true;

                    String location = mLocationLat.getText().toString() + "," + mLocationLong.getText().toString();
                    String place = mLocationPlace.getText().toString();
                    String actors = mActors.getText().toString();
                    String type = "";
                    if (spinnerDialogText!=null && spinnerDialogText.getText() != null) {
                        type = spinnerDialogText.getText().toString();
                    }

                    //int attendees = new Integer(mAttendess.getText().toString());
                    String section = mSection.getText().toString();
                    //String status = mStatus.getText().toString();
                    String dateTime = mFechaEdit.getText().toString();
                    String tags = mTagsEdit.getText().toString();
                    String desc = mDescEdit.getText().toString();

                    //Se aplica validacion de campos requeridos



                    if(TextUtils.isEmpty(type)){
                        TextView errorText = (TextView)mTypeSpinner.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText(getResources().getString(R.string.required_field));
                        validation = false;
                    }

                    if(TextUtils.isEmpty(section)){
                        mSection.setError(getResources().getString(R.string.required_field));
                        validation = false;
                    }

                    if(TextUtils.isEmpty(dateTime)){
                        mFechaEdit.setError(getResources().getString(R.string.required_field));
                        validation = false;
                    }

                    if(TextUtils.isEmpty(desc)){
                        mDescEdit.setError(getResources().getString(R.string.required_field));
                        validation = false;
                    }

                    if(TextUtils.isEmpty(tags)){
                        mTagsEdit.setError(getResources().getString(R.string.required_field));
                        validation = false;
                    }

                    if(validation){

                        if (currentReport != null && currentReport.getReportID() != null) {
                            currentReport.updateFields(
                                    mAuth.getCurrentUser().getUid(),
                                    location,
                                    place,
                                    actors,
                                    type,
                                    section,
                                    dateTime,
                                    tags,
                                    desc,
                                    currentUserName
                            );
                            saveAndUpdateReport(currentReport, view);
                        } else {
                            Report report = new Report(mAuth.getCurrentUser().getUid(),
                                    location,
                                    place,
                                    actors,
                                    type,
                                    section,
                                    dateTime,
                                    tags,
                                    desc,
                                    currentUserName
                            );
                            saveAndUpdateReport(report, view);
                        }
                    }else{
                        Toast.makeText(view.getContext(), getResources().getString(R.string.complete_report_fields), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }




                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });


            FloatingActionButton fabDel = (FloatingActionButton) rootView.findViewById(R.id.deleteReportButton);
            fabDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Displays an alert dialog
                    AlertDialog diaBox = AskOption(view);
                    diaBox.show();
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            updateDisplay();

            listenerDateSelector = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int mYear, int mMonth, int dayOfMonth) {
                    year = mYear;
                    month = mMonth;
                    day = dayOfMonth;
                    updateDisplay();
                }
            };


            mFechaEdit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // pointer goes down

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // pointer goes up
                        DatePickerDialog d = new DatePickerDialog(getActivity(),
                                R.style.AppTheme, listenerDateSelector, year, month, day);
                        d.show();

                    }
                    // also let the framework process the event
                    return false;

                }
            });


            mSection.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // pointer goes down

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //Creates the multiple select check
                        String[] selectedCategories;
                        if (scrollView == null) {
                            createDialogCheckBoxes(v);
                        }
                        if (mSection.getText() != null) {
                            selectedCategories = mSection.getText().toString().split(",");
                            if (selectedCategories != null && selectedCategories.length > 0) {
                                fillCategoriesPopUp(v, selectedCategories);
                            }

                        } else {
                            fillCategoriesPopUp(v, null);
                        }

                    }
                    // also let the framework process the event
                    return false;

                }
            });


            if (currentReport != null) {
                String[] location = currentReport.getLocation().split(",");

                mLocationLat.setText(location[0]);
                mLocationLong.setText(location[1]);
                mLocationPlace.setText(currentReport.getPlace());
                mActors.setText(currentReport.getActors());

                int spinnerPosition = typeAdapter.getPosition(currentReport.getType());
                mTypeSpinner.setSelection(spinnerPosition);

                //mAttendess.setText(String.valueOf(currentReport.getAttendees()));
                mSection.setText(currentReport.getSection());
                //mStatus.setText(currentReport.getStatus());

                if (!mAuth.getCurrentUser().getUid().equals(currentReport.getUserID())) {
                    //Gone some objects
                    relSpinner.setVisibility(View.GONE);
                    mTypeEdit.setText(currentReport.getType());
                    fab.setVisibility(View.GONE);
                    fabDel.setVisibility(View.GONE);

                    disableEditText(mLocationLat, mLocationLong, mActors, mSection, mFechaEdit, mDescEdit, mTagsEdit, mLocationPlace, mTypeEdit);
                    //disableSpinners(mTypeSpinner);
                } else {
                    fabDel.setVisibility(View.VISIBLE);
                    relSpinner.setVisibility(View.VISIBLE);
                    mTypeEdit.setVisibility(View.GONE);
                    mTypeLabel.setVisibility(View.GONE);
                }

                mTagsEdit.setText(currentReport.getTags());
                mDescEdit.setText(currentReport.getDescription());

            } else {
                relSpinner.setVisibility(View.VISIBLE);
                mTypeEdit.setVisibility(View.GONE);
                mTypeLabel.setVisibility(View.GONE);
                fabDel.setVisibility(View.GONE);
            }

            return rootView;
        }


    }


    private ArrayList<Media> getMedia(Report currentReport) {

        if (currentReport != null && currentReport.getReportID() != null) {
            mQueryCurrentReportMedia = mDatabaseMedia.orderByChild("reportID").equalTo(currentReport.getReportID());

            mQueryCurrentReportMedia.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    getAllMedia(dataSnapshot);
                    if (mediaAdapter != null) {
                        mediaAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    getAllMedia(dataSnapshot);
                    if (mediaAdapter != null) {
                        mediaAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        return mediaObjects;
    }

    private ArrayList<Denunciation> getDenunciations(Report currentReport) {

        if (currentReport != null && currentReport.getReportID() != null) {
            mQueryCurrentReportHHRR = mDatabaseHHRR.orderByChild("reportID").equalTo(currentReport.getReportID());


            mQueryCurrentReportHHRR.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    getAllDenunciations(dataSnapshot);
                    if (hhrrAdapter != null) {
                        hhrrAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    getAllDenunciations(dataSnapshot);
                    if (hhrrAdapter != null) {
                        hhrrAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        return denunciations;
    }

    private void getAllMedia(DataSnapshot dataSnapshot) {
        String mediaID = dataSnapshot.getKey();

        Map<String, Object> map = (Map) dataSnapshot.getValue();

        String u_id = (String) map.get("userID");

        String report_id = (String) map.get("reportID");

        String username = (String) map.get("username");

        String description = (String) map.get("description");

        String dateTime = (String) map.get("dateTime");

        String imageURI = (String) map.get("imageURI");

        String audioURI = (String) map.get("audioURI");


        Media rescuedMedia = new Media(
                report_id,
                u_id,
                username,
                description,
                imageURI,
                audioURI,
                dateTime
        );

        rescuedMedia.setMediaID(mediaID);

        mediaObjects.add(rescuedMedia);
    }

    private void getAllDenunciations(DataSnapshot dataSnapshot) {

        //for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
        String denunciationID = dataSnapshot.getKey();

        Map<String, Object> map = (Map) dataSnapshot.getValue();

        Log.w("Denunciation KEY", denunciationID);
        Log.w("Denunciation MAP", map.toString());

        String report_id = (String) map.get("reportID");

        String u_id = (String) map.get("userID");

        String type = (String) map.get("title");

        String dateTime = (String) map.get("dateTime");

        String username = (String) map.get("username");

        String cuerpo = (String) map.get("corpse");

        Denunciation rescuedDenunciation = new Denunciation(
                denunciationID,
                report_id,
                u_id,
                username,
                type,
                cuerpo,
                dateTime
        );

        denunciations.add(rescuedDenunciation);

    }

    private AlertDialog AskOption(final View view) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(view.getContext())
                //set message, title, and icon
                .setTitle(getResources().getString(R.string.delete_report_title))
                .setMessage(getResources().getString(R.string.delete_report_ask))
                //.setIcon(R.drawable.delete)

                .setPositiveButton(getResources().getString(R.string.delete_report_ok), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteReport(currentReport, view);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(getResources().getString(R.string.delete_report_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private void createDialogCheckBoxes(View v) {
        final CheckBox checkBox1 = new CheckBox(v.getContext());
        checkBox1.setText(R.string.category_ambientalismo);

        final CheckBox checkBox2 = new CheckBox(v.getContext());
        checkBox2.setText(R.string.category_sindical);

        final CheckBox checkBox3 = new CheckBox(v.getContext());
        checkBox3.setText(R.string.category_educacion);

        final CheckBox checkBox4 = new CheckBox(v.getContext());
        checkBox4.setText(R.string.category_movimientos_politicos);

        final CheckBox checkBox5 = new CheckBox(v.getContext());
        checkBox5.setText(R.string.category_animalismo);

        final CheckBox checkBox6 = new CheckBox(v.getContext());
        checkBox6.setText(R.string.category_movimientos_indigenas);

        final CheckBox checkBox7 = new CheckBox(v.getContext());
        checkBox7.setText(R.string.category_comunidades_afro);

        final CheckBox checkBox8 = new CheckBox(v.getContext());
        checkBox8.setText(R.string.category_paz);

        final CheckBox checkBox9 = new CheckBox(v.getContext());
        checkBox9.setText(R.string.category_mujeres);

        final CheckBox checkBox10 = new CheckBox(v.getContext());
        checkBox10.setText(R.string.category_lgbti);

        final CheckBox checkBox11 = new CheckBox(v.getContext());
        checkBox11.setText(R.string.category_campesinado);

        final CheckBox checkBox12 = new CheckBox(v.getContext());
        checkBox12.setText(R.string.category_movs_urbanos);

        final CheckBox checkBox13 = new CheckBox(v.getContext());
        checkBox13.setText(R.string.category_victimas);

        final CheckBox checkBox14 = new CheckBox(v.getContext());
        checkBox14.setText(R.string.category_presos_politicos);

        final CheckBox checkBox15 = new CheckBox(v.getContext());
        checkBox15.setText(R.string.category_antimilitarismo);

        final CheckBox checkBox16 = new CheckBox(v.getContext());
        checkBox16.setText(R.string.category_brutalidad_policial);

        final CheckBox checkBox17 = new CheckBox(v.getContext());
        checkBox17.setText(R.string.category_antifascismo);

        final CheckBox checkBox18 = new CheckBox(v.getContext());
        checkBox18.setText(R.string.category_transportes);

        mapChecks = new HashMap<String, CheckBox>();

        mapChecks.put(checkBox1.getText().toString(), checkBox1);
        mapChecks.put(checkBox2.getText().toString(), checkBox2);
        mapChecks.put(checkBox3.getText().toString(), checkBox3);
        mapChecks.put(checkBox4.getText().toString(), checkBox4);
        mapChecks.put(checkBox5.getText().toString(), checkBox5);
        mapChecks.put(checkBox6.getText().toString(), checkBox6);
        mapChecks.put(checkBox7.getText().toString(), checkBox7);
        mapChecks.put(checkBox8.getText().toString(), checkBox8);
        mapChecks.put(checkBox9.getText().toString(), checkBox9);
        mapChecks.put(checkBox10.getText().toString(), checkBox10);
        mapChecks.put(checkBox11.getText().toString(), checkBox11);
        mapChecks.put(checkBox12.getText().toString(), checkBox12);
        mapChecks.put(checkBox13.getText().toString(), checkBox13);
        mapChecks.put(checkBox14.getText().toString(), checkBox14);
        mapChecks.put(checkBox15.getText().toString(), checkBox15);
        mapChecks.put(checkBox16.getText().toString(), checkBox16);
        mapChecks.put(checkBox17.getText().toString(), checkBox17);
        mapChecks.put(checkBox18.getText().toString(), checkBox18);

        scrollView = new ScrollView(v.getContext());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout linearLayout = new LinearLayout(v.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(checkBox1);
        linearLayout.addView(checkBox2);
        linearLayout.addView(checkBox3);
        linearLayout.addView(checkBox4);
        linearLayout.addView(checkBox5);
        linearLayout.addView(checkBox6);
        linearLayout.addView(checkBox7);
        linearLayout.addView(checkBox8);
        linearLayout.addView(checkBox9);
        linearLayout.addView(checkBox10);
        linearLayout.addView(checkBox11);
        linearLayout.addView(checkBox12);
        linearLayout.addView(checkBox13);
        linearLayout.addView(checkBox14);
        linearLayout.addView(checkBox15);
        linearLayout.addView(checkBox16);
        linearLayout.addView(checkBox17);
        linearLayout.addView(checkBox18);

        scrollView.addView(linearLayout);
    }

    private void fillCategoriesPopUp(View v, String[] selectedCategories) {

        //checkboxes are checked
        if (selectedCategories != null && selectedCategories.length > 0) {

            for (String category : selectedCategories) {

                if (mapChecks.get(category) != null) {
                    mapChecks.get(category).setChecked(true);
                }

            }
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        //alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder.setView(scrollView);

        //alertDialogBuilder.setTitle(R.string.category_dialog_title);
        alertDialogBuilder.setMessage(R.string.category_dialog_mensaje);


        alertDialogBuilder.setPositiveButton(R.string.category_dialog_OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                StringBuilder builder = new StringBuilder();

                for (CheckBox checkBox : mapChecks.values()) {
                    if (checkBox.isChecked()) {
                        builder.append(checkBox.getText());
                        builder.append(",");
                    }
                }
                if (builder.length() > 1) {
                    builder.deleteCharAt(builder.length() - 1);
                }

                mSection.setText(builder.toString());
            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void updateDisplay() {
        GregorianCalendar c = new GregorianCalendar(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        mFechaEdit.setText(sdf.format(c.getTime()));
    }// updateDisplay

    private String getActualTimeDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(calendar.getTime());
        return formattedDate;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.typeSpinnerDialog) {
            if (position > 0) {
                spinnerDialogText = (TextView) view;
            }
        } else if (spinner.getId() == R.id.typeDDHHSpinnerDialog) {
            denSpinnerDialogText = (TextView) view;
            denSpinnerDialogText.setTextSize(10);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void disableEditText(EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.setFocusable(false);
            //editText.setEnabled(false);
            //editText.setCursorVisible(false);
            editText.setKeyListener(null);
            editText.setOnClickListener(null);
            editText.setOnTouchListener(null);
            editText.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    private void disableSpinners(Spinner... spinners) {

        for (Spinner spinner : spinners) {
            spinner.setVisibility(View.INVISIBLE);
            spinner.setFocusable(false);
            //spinner.setEnabled(false);
            spinner.setOnItemSelectedListener(null);
            //spinner.setOnClickListener(null);
            //spinner.setOnTouchListener(null);
            spinner.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    private void saveAndUpdateMediaPost(Media newMedia, final View view) {
        String key = "";

        if (newMedia.getMediaID() != null && !newMedia.getMediaID().isEmpty()) {
            key = newMedia.getMediaID();
        } else {
            key = mDatabase.child("Media").push().getKey();
        }

        Map<String, Object> postValues = newMedia.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Media/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //startActivity(new Intent(view.getContext(),AccountActivity.class));
                    Snackbar.make(view, getResources().getString(R.string.saving_report), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }


    private void saveAndUpdateDenunciation(Denunciation newDen, final View view) {
        String key = "";

        if (newDen.getDenunciationID() != null && !newDen.getDenunciationID().isEmpty()) {
            key = newDen.getDenunciationID();
        } else {
            key = mDatabase.child("HHRR").push().getKey();
        }

        Map<String, Object> postValues = newDen.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/HHRR/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //startActivity(new Intent(view.getContext(),AccountActivity.class));
                    Snackbar.make(view, getResources().getString(R.string.saving_report), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }


    private void saveAndUpdateReport(Report newReport, final View view) {

        String key = "";

        if (newReport.getReportID() != null && !newReport.getReportID().isEmpty()) {
            key = newReport.getReportID();
        } else {
            key = mDatabase.child("Reports").push().getKey();
        }

        Map<String, Object> postValues = newReport.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Reports/" + key, postValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(view.getContext(), AccountActivity.class));
                    Snackbar.make(view, getResources().getString(R.string.saving_report), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

    }

    private void deleteReport(Report report, final View view) {
        String key = report.getReportID();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Reports/" + key, null);

        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(view.getContext(), AccountActivity.class));
                    Snackbar.make(view, getResources().getString(R.string.deleting_report), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    private void showSubmitMediaDialog(LayoutInflater inflater, View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View dialogView = inflater.inflate(R.layout.post_new_media, null);
        dialogBuilder.setView(dialogView);
        progressDialog = new ProgressDialog(dialogView.getContext());
        final EditText mImageDesc = (EditText) dialogView.findViewById(R.id.mediaDesctEditText);

        mSelectImage = (ImageButton) dialogView.findViewById(R.id.postMediaImageButton);

        if (imageURI != null) {
            Log.e("camara URI", imageURI.toString());
            mSelectImage.setImageURI(imageURI);
            mSelectImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            Log.e("camara URI", "VACIA");
        }

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        dialogBuilder.setPositiveButton(getResources().getString(R.string.boton_denuncia_agregar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Save image
                progressDialog.setMessage("uploading image ..");
                progressDialog.show();
                final String imageDesc = mImageDesc.getText().toString().trim();


                if (!TextUtils.isEmpty(imageDesc) && imageURI != null) {

                    if(currentReport!=null){
                        StorageReference filePath = mStorage.child("ReportsMedia").child(imageURI.getLastPathSegment());
                        filePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String reportID = currentReport.getReportID();
                                String userID = mAuth.getCurrentUser().getUid();
                                String userName = getCurrentUserName();
                                Uri downloadURI = taskSnapshot.getDownloadUrl();
                                String datetime = getActualTimeDate();


                                Media media = new Media(reportID, userID, userName, imageDesc, downloadURI.toString(), null, datetime);
                                saveAndUpdateMediaPost(media, dialogView);
                                mediaAdapter.notifyDataSetChanged();
                                imageURI = null;

                                progressDialog.dismiss();
                            }
                        });
                    }else{
                        Toast.makeText(dialogView.getContext(), getResources().getString(R.string.image_save_first), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }



                } else {
                    Toast.makeText(dialogView.getContext(), getResources().getString(R.string.image_desc_requiered), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }


            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.boton_filtro_actores_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Log.e("-->", "INGRESA galeria");
            imageURI = data.getData();
            if (mSelectImage != null) {
                mSelectImage.setImageURI(imageURI);
                mSelectImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.e("-->", "INGRESA CAMARA");

            //imageURI = Uri.parse(mImageFileLocation);
            //Log.e("-->DATA",data.toString());

            imageURI = Uri.fromFile(photoFile);


            if (galleryFab != null) {
                galleryFab.performClick();
            }

        }

    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "APPLUCHAR_IMG_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        return image;

    }

    private void showNewDenunciationDialog(LayoutInflater inflater, View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View dialogView = inflater.inflate(R.layout.new_denunciation_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.denunciationCorpseMultilineText);

        Spinner mDenSpinner;
        ArrayAdapter denAdapter;


        denAdapter = ArrayAdapter.createFromResource(dialogView.getContext(), R.array.denunciation_type_spinner_options, android.R.layout.simple_spinner_item);

        mDenSpinner = (Spinner) dialogView.findViewById(R.id.typeDDHHSpinnerDialog);
        mDenSpinner.setAdapter(denAdapter);
        mDenSpinner.setOnItemSelectedListener(this);
        mDenSpinner.setSelection(0);


        dialogBuilder.setPositiveButton(getResources().getString(R.string.boton_denuncia_agregar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Save denunciation

                if(currentReport!=null){
                    String tipoDenuncia = denSpinnerDialogText.getText().toString();
                    String textoDenuncia = edt.getText().toString();
                    String reporteID = currentReport.getReportID();
                    String userID = mAuth.getCurrentUser().getUid();
                    String userName = getCurrentUserName();
                    String datetime = getActualTimeDate();

                    Denunciation den = new Denunciation(reporteID, userID, userName, tipoDenuncia, textoDenuncia, datetime);
                    saveAndUpdateDenunciation(den, dialogView);
                    hhrrAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(dialogView.getContext(), getResources().getString(R.string.image_save_first), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }



            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.boton_filtro_actores_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
