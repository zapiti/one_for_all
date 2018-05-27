package com.dev.nathan.testtcc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.nathan.testtcc.controler.CommentsActivity;
import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.model.AttestantPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AttestantRecyclerAdapter extends RecyclerView.Adapter<AttestantRecyclerAdapter.ViewHolder> {
    //Declaração de variaveis
    private List<AttestantPost> post_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    //Construtor deste adaptador
    public AttestantRecyclerAdapter(List<AttestantPost> blog_list){
        this.post_list = blog_list;
    }
    //Inicializando a listagem
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inicializando variaveis
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attestant_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        //Ordenando os itens da listagem
        final String blogPostId = post_list.get(position).BlogPostId;
        final String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        String desc_data = post_list.get(position).getDesc();
        String danger_data = post_list.get(position).getDanger();
        String address_data = post_list.get(position).getAddress();

        holder.setDescText(desc_data,danger_data,address_data);

        String image_url = post_list.get(position).getImage_url();
        String thumbUri = post_list.get(position).getImage_thumb();
        holder.setBlogImage(image_url);


        String user_id = post_list.get(position).getUser_id();


        try {
            long millisecond = post_list.get(position).getDhUpload().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "ERRO: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        //Pegando denuncia de postagem
        firebaseFirestore.collection("Posts/" + blogPostId + "/Delations").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {

                  //  holder.mainBlogItem.setVisibility(View.GONE);
                      // todo holder.mainBlogItem.setVisibility(View.VISIBLE);

                    } else {

                        holder.mainBlogItem.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        holder.attestantDangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,holder.attestantDangerButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.list_item_attestant_bottom_menu, popup.getMenu());
                popup.show();//showing popup menu

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_attestant_list_menu_help:


                                return true;

            case R.id.action_attestant_list_menu_report:
                                new AlertDialog.Builder(context)
                        .setTitle("Denunciar post")
                        .setMessage("A denuncia desse post fara com que ele nâo seja mais exibido para você e estaremos avaliando sua denuncia.Deseja continuar?")

                        .setPositiveButton("sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firebaseFirestore.collection("Posts/" + blogPostId + "/Delations").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if(!task.getResult().exists()){

                                                    Map<String, Object> delationsMap = new HashMap<>();
                                                    delationsMap.put("dhUpload", FieldValue.serverTimestamp());

                                                    firebaseFirestore.collection("Posts/" + blogPostId + "/Delations").document(currentUserId).set(delationsMap);

                                                } else {

                                                    firebaseFirestore.collection("Posts/" + blogPostId + "/Delations").document(currentUserId).delete();

                                                }

                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("não", null)
                        .show();

                return true;



            default:
                return false;


                        }
                    }
                });





            }
        });

        holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private TextView dangerView;
        private ImageView blogImageView;
        private TextView blogDate;

        private ImageView attestantDangerImage;

        private ImageView attestantDangerButton;
        private CardView mainBlogItem;

        private TextView blogDelationCount;
        private TextView attestantAddress;

        private ImageView blogCommentBtn;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            attestantDangerButton = mView.findViewById(R.id.attestant_danger_btn);
            attestantDangerButton.isClickable();

            mainBlogItem =mView.findViewById(R.id.main_blog_post);
            blogCommentBtn = mView.findViewById(R.id.attestant_comment_icon);





        }

        @SuppressLint("SetTextI18n")
        void setDescText(String descText, String dangerText, String address){

            attestantAddress = mView.findViewById(R.id.attestant_address);
            attestantAddress.setText("Local : "+address);

            descView = mView.findViewById(R.id.attestant_desc);
            descView.setText(descText);
            dangerView = mView.findViewById(R.id.attestant_danger);
            dangerView .setText(dangerText);

            attestantDangerImage = mView.findViewById(R.id.attestant_danger_image);

            switch (dangerText) {
                case "Assédio Escolar":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.notVeryUrgent));
                    break;

                case "Assédio Moral ou Mobbing":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.veryUrgent));
                    break;

                case "Assédio Sexual":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.emerging));
                    break;

                case "Bullyng":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.notVeryUrgent));
                    break;

                case "Violência Domiciliar":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.emerging));
                    break;

                case "Violência no Trabalho":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.veryUrgent));
                    break;

                case "Violência Sexual":
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.emerging));
                    break;

                default:
                    attestantDangerImage.setImageTintList(ContextCompat.getColorStateList(context, R.color.notUrgent));


            }



    }

    @SuppressLint("CheckResult")
    void setBlogImage(String downloadUri){



        blogImageView = mView.findViewById(R.id.attestant_image);
        if(downloadUri.equals("") ) {
            blogImageView.setVisibility(View.GONE);
        }else{
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(downloadUri)
            ).into(blogImageView);


            blogImageView.setVisibility(View.VISIBLE);

        }

    }

    public void setTime(String date) {

        blogDate = mView.findViewById(R.id.attestant_date);
        blogDate.setText(date);

    }




    @SuppressLint("SetTextI18n")
    public void updateDelationsCount(int count){

        blogDelationCount = mView.findViewById(R.id.attestant_danger_count);
        blogDelationCount.setText(count + " Delations");

    }

}

}
