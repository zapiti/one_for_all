package com.dev.nathan.testtcc.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.inicializador.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import id.zelory.compressor.Compressor;


public class NewPostFragment extends Fragment {


    private ImageView newPostImage;
    private EditText newPostDesc;
   // private TextInputLayout newPostDescContent;
    private Spinner newPostAbuseDesc;

    private Uri postImageUri = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private EditText mAddress;
    private LinearLayout load;

    private String current_user_id;

    private Bitmap compressedImageFile;
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;

    Geocoder geocoder;
    List<Address> addresses;


    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        final Context context = view.getContext();
        load = view.findViewById(R.id.avi2content);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();



        newPostImage =view. findViewById(R.id.attestant_post_image);
        newPostDesc = view.findViewById(R.id.attestant_post_desc);
     //   newPostDescContent = view.findViewById(R.id.newPostDescContent);

        Button newPostBtn = view.findViewById(R.id.attestant_post_btn);
        newPostProgress = view.findViewById(R.id.attestant_post_progress);
        newPostAbuseDesc = view.findViewById(R.id.spinner);

        Button mGetCurrentUserLocatios = view.findViewById(R.id.attestant_post_getcurrent_location_btn);



        mGetCurrentUserLocatios .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
            }
        });


        mAddress = view.findViewById(R.id.attestant_post_address_edittext);

        ArrayList<String > typesAbuse = new ArrayList<>();
        typesAbuse.add(getString(R.string.assEscolar));
        typesAbuse.add(getString(R.string.assMoral));
        typesAbuse.add(getString(R.string.assSexual));
        typesAbuse.add(getString(R.string.Bullying));
        typesAbuse.add(getString(R.string.assDom));
        typesAbuse.add(getString(R.string.assTrab));
        typesAbuse.add(getString(R.string.assSex));
        typesAbuse.add(getString(R.string.outros_tipos_de_abuso));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_dropdown_item,typesAbuse);
        newPostAbuseDesc.setAdapter( spinnerArrayAdapter );






        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 200)
                        .setMaxCropResultSize(720,200)
                        .setAspectRatio(1, 1)
                        .start(Objects.requireNonNull(getActivity()),NewPostFragment.this);

            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setVisibility(View.VISIBLE);

                final String desc = newPostDesc.getText().toString();
                final String danger =   newPostAbuseDesc.getSelectedItem().toString();
                final String adress = mAddress.getText().toString();

                if(!TextUtils.isEmpty(desc) ){



                    newPostProgress.setVisibility(View.VISIBLE);
                    if (postImageUri != null) {
                        final String randomName = UUID.randomUUID().toString();

                        // PHOTO UPLOAD
                        File newImageFile = new File(postImageUri.getPath());
                        try {

                            compressedImageFile = new Compressor(context)
                                    .setMaxHeight(720)
                                    .setMaxWidth(720)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageData = baos.toByteArray();

                        // PHOTO UPLOAD

                        UploadTask filePath = storageReference.child("post_images").child(randomName + ".jpg").putBytes(imageData);
                        filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                                final String downloadUri = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                                if (task.isSuccessful()) {

                                    File newThumbFile = new File(postImageUri.getPath());
                                    try {

                                        compressedImageFile = new Compressor(context)
                                                .setMaxHeight(100)
                                                .setMaxWidth(100)
                                                .setQuality(1)
                                                .compressToBitmap(newThumbFile);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData = baos.toByteArray();

                                    UploadTask uploadTask = storageReference.child("post_images/thumbs")
                                            .child(randomName + ".jpg").putBytes(thumbData);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            String downloadthumbUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

                                            addPostToFirebase(downloadthumbUri, downloadUri, desc,danger,adress, context);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            //Error handling

                                        }
                                    });


                                } else {

                                    newPostProgress.setVisibility(View.INVISIBLE);

                                }

                            }
                        });

                    }else {


                        addPostToFirebase("", "", desc,danger,adress, context);



                    }
                }else {
                   Toast.makeText(context,"Descreva o acessedio ocorrido!",Toast.LENGTH_LONG).show();
                    load.setVisibility(View.GONE);
                }

            }
        });

 return view;
    }

    private void addPostToFirebase(String downloadthumbUri, String downloadUri, String desc,String danger,String address, final Context context) {
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image_url", downloadUri);
        postMap.put("image_thumb", downloadthumbUri);
        postMap.put("desc", desc);
        postMap.put("address",address);
        postMap.put("danger",danger);
        postMap.put("user_id", current_user_id);
        postMap.put("dhUpload", FieldValue.serverTimestamp());

        firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if (task.isSuccessful()) {
                    load.setVisibility(View.GONE);
                    Intent mainIntent = new Intent(getContext(),MainActivity.class);
                    startActivity(mainIntent);
                    Objects.requireNonNull(getActivity()).finish();
                    Toast.makeText(context, "Denuncia Salva", Toast.LENGTH_LONG).show();


                }
                newPostProgress.setVisibility(View.INVISIBLE);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {

                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            double latti = 0;
            double longi = 0;

            if (location != null) {
                latti = location.getLatitude();
                 longi = location.getLongitude();
            } else if(location1 != null ){
                latti = location1.getLatitude();
                longi = location1.getLongitude();
            }else if(location2 != null){
                latti = location2.getLatitude();
                longi = location2.getLongitude();
            }

                try {
                    addresses = geocoder.getFromLocation(latti, longi, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        mAddress.setText(address);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }




        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Por favor, ligue a sua conexão GPS")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}





