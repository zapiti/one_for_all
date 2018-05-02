package com.dev.nathan.testtcc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.nathan.testtcc.R;

import com.dev.nathan.testtcc.controler.WebViewActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;
import com.dev.nathan.testtcc.model.Url;


import java.util.ArrayList;
import java.util.List;

import java.util.Objects;



public class NetworkWebViewAdapter extends RecyclerView.Adapter<NetworkWebViewAdapter.ViewHolder> {


    private List<Url> url_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private AVLoadingIndicatorView mVLoading;
    private ImageView webViewListImage;
    private TextView webViewListTitle;
    private TextView webViewListContent;
    private FrameLayout webViewNewsColor;

    public NetworkWebViewAdapter(List<Url> url_list){

        this.url_list = url_list;

    }

    @NonNull
    @Override
    public NetworkWebViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_network, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new NetworkWebViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NetworkWebViewAdapter.ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);



        final String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final ArrayList<String> goUrl = new ArrayList<>();
        final String[] title = new String[1];

        //User Data will be retrieved here...
        firebaseFirestore.collection("WebView").document("Web"+position).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    Log.d("rabanada",task.getResult().getString("url"));

                    goUrl.add(task.getResult().getString("url")) ;
                    String thumbImageWebView = task.getResult().getString("image");
                     title[0] = task.getResult().getString("title");
                    String content = task.getResult().getString("content");
                    String type = task.getResult().getString("type");
                      holder.setUrl(thumbImageWebView, title[0],content,type);



                }


            }
        });






        holder.webViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent webIntent = new Intent(context, WebViewActivity.class);
                webIntent.putExtra("go_url", goUrl);
                webIntent.putExtra("go_title", title[0]);
                context.startActivity(webIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return url_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;


        private WebView mWebView;


        private CardView webViewList;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            webViewList = mView.findViewById(R.id.list_item_network_container);



        }



        @SuppressLint("CheckResult")
        void setUrl(String image,String title, String content,String type){


            webViewListImage = mView.findViewById(R.id.web_view_list_image);
            webViewListTitle = mView.findViewById(R.id.web_view_title);
            webViewListContent = mView.findViewById(R.id.web_view_content);
            webViewNewsColor = mView.findViewById(R.id.web_view_frame);


            webViewListTitle.setText(title);
            webViewListContent.setText(content);
            
            switch (type) {
                case "noticia":
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.noticiaChart));
                    break;

                case "entreterimento":
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.entreterimentoChart));
                    break;

                case "saude":
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.saudeChart));
                    break;


                case "ong":
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.ongsChart));
                    break;



                case "ajuda":
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.ajudaChart));
                    break;

                default:
                   webViewNewsColor.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            }
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.place_net);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(webViewListImage);





        }





    }


}
