package com.dev.nathan.testtcc.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.adapter.NetworkWebViewAdapter;
import com.dev.nathan.testtcc.helper.BottomNavigationViewHelper;
import com.dev.nathan.testtcc.model.Url;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class WebViewFragment extends Fragment {
    float rainfall[] = {20f, 40f, 30f, 10f, 300f};
    String type[] = {"Noticias  ", "Ajuda ", "Saude ", "ONGs ", "Entreterimento"};


    private RecyclerView url_list_view;
    private List<Url> url_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private NetworkWebViewAdapter networkWebViewAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    Button myButton;
    View myView;
    boolean isUp;

    public WebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web, container, false);

        myView = view.findViewById(R.id.infoLegendWeb);
        myButton = view.findViewById(R.id.infoButtonLegendy);

        // initialize as invisible (could also do in xml)
        myView.setVisibility(View.GONE);
        myButton.setText("Slide up");
        isUp = false;


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp) {
                    //   slideDown(myView);
                    myButton.setText("Slide up");
                    myView.animate()
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    myView.setVisibility(View.GONE);
                                }
                            });

                } else {
                    //   slideUp(myView);
                    myView.animate()
                            .translationY(0).alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    myView.setVisibility(View.VISIBLE);
                                    myView.setAlpha(0.0f);
                                }
                            });
                    myButton.setText("Slide down");
                }
                isUp = !isUp;
            }
        });


        url_list = new ArrayList<>();
        url_list_view = view.findViewById(R.id.url_list_view);

        firebaseAuth = FirebaseAuth.getInstance();

        networkWebViewAdapter = new NetworkWebViewAdapter(url_list);
        url_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        url_list_view.setAdapter(networkWebViewAdapter);
        url_list_view.setHasFixedSize(true);

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            url_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        //    loadMorePost();

                    }

                }
            });

            Query firstQuery = firebaseFirestore.collection("WebView").orderBy("url", Query.Direction.DESCENDING).limit(3);

            firstQuery.addSnapshotListener(Objects.requireNonNull(getActivity()), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {

                            if (isFirstPageFirstLoad) {

                                lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                                url_list.clear();

                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String urlId = doc.getDocument().getId();
                                    Url url = doc.getDocument().toObject(Url.class);

                                    if (isFirstPageFirstLoad) {

                                        url_list.add(url);

                                    } else {

                                        url_list.add(0, url);

                                    }


                                    networkWebViewAdapter.notifyDataSetChanged();

                                }
                            }

                            isFirstPageFirstLoad = false;

                        }
                    }

                }

            });

        }

        // Inflate the layout for this fragment
        return view;
    }
}

