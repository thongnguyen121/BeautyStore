package com.example.beautystore.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Product_Detail;
import com.example.beautystore.activity.Activity_add_Brands;
import com.example.beautystore.fragments.Fragment_warehouse_list;
import com.example.beautystore.model.Customer;
import com.example.beautystore.model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class RecyclerView_Rating extends RecyclerView.Adapter<RecyclerView_Rating.RatingViewHolder> {
    private ArrayList<Rating> data;
    private Context context;

    private int resource;
    private DatabaseReference databaseReference;
    private String id_producst = "";
    private String id_user = "";
    EditText edtComment_dialog;
    AppCompatButton btnCancel;
    AppCompatButton btnSave;
    RatingBar ratingDialog;
    float  currentNumberStar = 0;
    String customer_id;
    public RecyclerView_Rating(ArrayList<Rating> data, Context context, int resource) {
        this.data = data;
        this.context = context;
        this.resource = resource;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View ratingView = inflater.inflate(R.layout.layout_item_review, parent, false);
        return new RatingViewHolder(ratingView);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {

        Rating rating = data.get(position);
        String userid = rating.getCustomer_id();
        loadInformation_user(holder, userid);
        holder.tvComment.setText(rating.getComment());
        holder.tvCreateAt.setText(rating.getCreate_at());
        holder.rbRating.setRating(Float.parseFloat(rating.getStartNumber().trim()));
        id_producst = rating.getProduct_id();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                id_producst = rating.getProduct_id();
                id_user = rating.getCustomer_id();
                customer_id = FirebaseAuth.getInstance().getUid();
                if (id_user.equals(customer_id))
                {
                    showPopupMenu(v);
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadInformation_user(RatingViewHolder holder, String user_id)
    {
        databaseReference.child("Customer").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    if (customer != null) {
                        holder.tvUserName.setText(customer.getUsername());
                        Glide.with(context.getApplicationContext()).load(customer.getProfileImage()).into(holder.ivUserAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_function_edit_delete, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.Edit) {
                    openDialog_edit_review(Gravity.CENTER);
                } else if (id == R.id.Delete) {
                    AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
                    myDialog.setTitle("Question");
                    myDialog.setMessage("Bạn có chắc muốn xóa bài đánh giá này không?");
                    myDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           deleteReview(id_producst, customer_id);
                        }
                    });
                    myDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    myDialog.create().show();
                }


                return true;
            }
        });


    }
    private void openDialog_edit_review(int gravity) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_review);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity)
        {
            dialog.setCancelable(false);
        }
//        String UserID = FirebaseAuth.getInstance().getUid();
        edtComment_dialog = dialog.findViewById(R.id.edt_comment_dialog);
        btnCancel = dialog.findViewById(R.id.btn_cancel_dialog);
        btnSave = dialog.findViewById(R.id.btn_save_dialog);
        ratingDialog = dialog.findViewById(R.id.rbRating_dialog_edit);

        getData_intent_dialog(id_producst, customer_id);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReview();

            }
        });
        ratingDialog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    currentNumberStar = rating;
                    Log.d("tag", "onRatingChanged_1: " + currentNumberStar);
                }
            }
        });

        dialog.show();

    }

    private void getData_intent_dialog(String products_id, String id_user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Rating").child(products_id).child(id_user);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    if (rating != null) {
                        edtComment_dialog.setText(rating.getComment());
                        ratingDialog.setRating(Float.parseFloat(rating.getStartNumber().trim()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editReview() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String savedate = simpleDateFormat.format(calendar.getTime());
        String UserID = FirebaseAuth.getInstance().getUid();

        String commentText = edtComment_dialog.getText().toString().trim();
        if (!TextUtils.isEmpty(commentText) || currentNumberStar != 0) {
            DatabaseReference userRatingRef = FirebaseDatabase.getInstance().getReference("Rating").child(id_producst).child(UserID);
            userRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        if (currentNumberStar != 0 || !TextUtils.isEmpty(commentText)) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rating").child(id_producst).child(UserID);

                            final HashMap<String, Object> ratinglist = new HashMap<>();
                            Rating rating = new Rating(id_producst, UserID, edtComment_dialog.getText().toString(), String.valueOf(currentNumberStar), savedate);

                            ratinglist.put("product_id", id_producst);
                            ratinglist.put("customer_id", UserID);
                            ratinglist.put("comment", rating.getComment());
                            ratinglist.put("startNumber", rating.getStartNumber());
                            ratinglist.put("create_at", savedate);

                            reference.setValue(ratinglist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    edtComment_dialog.setText("");
                                    Toast.makeText(context, "Đánh giá đã được chỉnh sửa", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Lưu không thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {

                            Toast.makeText(context, "Không có gì để chỉnh sửa", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(context, "Không tìm thấy đánh giá để chỉnh sửa", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(context, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteReview(String id_producst, String id_user) {

        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Rating").child(id_producst).child(id_user);

        reviewRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(context, "Đánh giá đã được xóa", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "Không thể xóa đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static class RatingViewHolder extends RecyclerView.ViewHolder{

        ImageView ivUserAvatar;
        TextView tvUserName;
        TextView tvCreateAt;
        TextView tvComment;
        RatingBar rbRating;
        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCreateAt = itemView.findViewById(R.id.tvCreateAt);
            tvComment = itemView.findViewById(R.id.tvUserComment);
            rbRating = itemView.findViewById(R.id.rbUserRating);
        }

    }
}
