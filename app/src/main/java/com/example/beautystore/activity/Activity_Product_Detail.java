package com.example.beautystore.activity;

import static androidx.fragment.app.FragmentManager.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beautystore.LoginActivity;
import com.example.beautystore.R;
import com.example.beautystore.adapter.RecyclerViewCategories;
import com.example.beautystore.adapter.RecyclerViewProducts;
import com.example.beautystore.adapter.RecyclerView_Rating;

import com.example.beautystore.model.Cart;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.adapter.RecyclerView_search_products;
import com.example.beautystore.model.Brands;
import com.example.beautystore.model.Categories;
import com.example.beautystore.model.Order;
import com.example.beautystore.model.OrderStatus;
import com.example.beautystore.model.Products;
import com.example.beautystore.model.Rating;
import com.example.beautystore.model.WishList;
import com.example.beautystore.notification.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_Product_Detail extends AppCompatActivity {

    Boolean isOnWishList;
    String productId = "", cate_id = "", imgProduct1, imgProduct2, imgProduct3, uid, price, autoId_rating, autoID;
    double total = 0;
    int productQty = 1, maxQty;

    String UID;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerViewProducts recyclerViewProducts;
    private RecyclerView_Rating recyclerViewRating;
    Button btnAddCart, btnBuyNow;
    ImageView ivComment, ivMessenger, ivDecreaseQty, ivIncreaseQty, ivAddWishList, ivBack, ivProductBig, ivProductSmall1, ivProductSmall2, ivProductSmall3;
    TextView tvProductName, tvProductPrice, tvProductQty, tvProductDesc, tvMaxQty, tvCartExist;
    RatingBar rbProductRating, rbUserRating;
    EditText edtComment;
    private ArrayList<WishList> wishLists;
    RecyclerView_Rating ratingAdapter; //Adapter=
    boolean productExist = false;
    private RecyclerView ratingRecyclerView, rcDSlienquan;
    private ArrayList<Rating> data_ratings = new ArrayList<>();
    float numberStar = 0;
    public static final String SHARE_PREFS = "sharedPrefs";
    private ArrayList<Products> data_products = new ArrayList<>();

    private String user_id = "";
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setScreenElement();
        setAddWishListButton();

        changeBigProductImage();


        productId = getIntent().getStringExtra("products_id");
        uid = FirebaseAuth.getInstance().getUid();
        cate_id = getIntent().getStringExtra("categories_id");
        user_id = getIntent().getStringExtra("user_id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        getDataFromFireBase(productId);

//        setScreenData();
        increaseProductQty();
        decreaseProductQty();
        getData_DSLienquan(cate_id);
        createRatingsList();
        checkProductExistInCart();

        UID = FirebaseAuth.getInstance().getUid();
        checkOrderStatusForRating(productId, uid);

        addOrRemoveProductToWishList();
        //intent_getData(productId);
        if (isUserLoggedin()) {
            ivMessenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Messenger.class);
                    intent.putExtra("products_id", productId);
                    intent.putExtra("user_id", UID);
                    startActivity(intent);
                }
            });
        } else {
            ivMessenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginDialog();
                }
            });

        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(uid);
            }
        });
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyNow();
            }
        });
    }

    private void checkProductExistInCart() {
        if (isUserLoggedin()){
            DatabaseReference reference = firebaseDatabase.getReference("Cart").child(uid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Cart cart = snapshot.getValue(Cart.class);
                        List<CartDetail> cartDetails = cart.getItems();
                        for (CartDetail detail : cartDetails){
                            if (detail.getProduct_id().equals(productId)){
                                tvCartExist.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else{
                        tvCartExist.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            tvCartExist.setVisibility(View.GONE);
        }
    }

    private void buyNow() {
        if (isUserLoggedin()) {
            if (maxQty == 0) {
                Toast.makeText(this, "Sản phẩm này hiện tại đã hết", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Activity_Product_Detail.this, Activity_Order.class);
                intent.putExtra("buynow", true);
                intent.putExtra("productId", productId);
                intent.putExtra("price", price);
                intent.putExtra("productQty", String.valueOf(productQty));
                startActivity(intent);
            }
        } else {
            loginDialog();
        }
    }

    private void addToCart(String uid) {
        if (isUserLoggedin()) {
            if (maxQty == 0) {
                Toast.makeText(this, "Sản phẩm này hiện tại đã hết", Toast.LENGTH_SHORT).show();
            } else {
                CartDetail cartDetail = new CartDetail(productId, price, String.valueOf(productQty));
                DatabaseReference reference = firebaseDatabase.getReference("Cart").child(uid);
                Double total = Double.valueOf(price) * productQty;
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cart cart = snapshot.getValue(Cart.class);
                        if (cart == null) {
                            cart = new Cart(uid, new ArrayList<>(), String.valueOf(total));
                        }
                        for (CartDetail item : cart.getItems()) {
                            if (item.getProduct_id().equals(cartDetail.getProduct_id())) {
                                int currentQty = Integer.parseInt(item.getQty());
                                if (currentQty < maxQty) {
                                    int epxQty = productQty + currentQty;
                                    if (epxQty >= maxQty){
                                        item.setQty(String.valueOf(maxQty));
                                    }
                                    else{
                                        item.setQty(String.valueOf(currentQty + productQty));
                                    }

                                    productExist = true;
                                } else {
                                    Toast.makeText(Activity_Product_Detail.this, "Sản phẩm đã đạt số lượng tối đa trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                break;
                            }
                        }
                        if (!productExist) {
                            cart.getItems().add(cartDetail);
                        }
                        updateTotalPrice(reference, uid);
                        reference.setValue(cart, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(Activity_Product_Detail.this, "thanh cong ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Activity_Product_Detail.this, "loi " + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            loginDialog();
        }

    }
    public static void updateTotalPrice(DatabaseReference reference, String uid) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cart cart = snapshot.getValue(Cart.class);
                int total = 0;
                if (cart != null) {
                    List<CartDetail> items = cart.getItems();
                    for (CartDetail cartDetail : items) {
                        double productPrice = Double.parseDouble(cartDetail.getPrice());
                        double productQty = Double.parseDouble(cartDetail.getQty());
                        total += productPrice * productQty;

                    }
                    cart.setTotal(String.valueOf(total));
                    reference.setValue(cart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAddWishListButton() {
        if (isUserLoggedin()) {
            wishLists = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("WishList");
            databaseReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(productId).exists()) {
                        isOnWishList = true;
                        ivAddWishList.setImageResource(R.drawable.baseline_favorite_24);
                    } else {
                        isOnWishList = false;
                        ivAddWishList.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            isOnWishList = false;
            ivAddWishList.setImageResource(R.drawable.baseline_favorite_border_24);

        }
    }

    private void getDataFromFireBase(String productId) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    tvProductName.setText(products.getProducts_name());
                    tvProductDesc.setText(products.getDescription());
                    price = products.getPrice();
                    maxQty = Integer.parseInt(products.getQuantity());
                    tvProductPrice.setText(decimalFormat.format(Integer.valueOf(products.getPrice().trim())) + " Đ");
                    imgProduct1 = products.getImgProducts_1();
                    imgProduct2 = products.getImgProducts_2();
                    imgProduct3 = products.getImgProducts_3();
                    tvMaxQty.setText(products.getQuantity());
                    if (maxQty == 0){
                        productQty = 0;
                    }
                    else {
                        productQty = 1;

                    }
                    tvProductQty.setText(String.valueOf(productQty));
                    Glide.with(getApplicationContext())
                            .load(products.getImgProducts_1())
                            .into(ivProductBig);
                    Glide.with(getApplicationContext())
                            .load(products.getImgProducts_1())
                            .into(ivProductSmall1);
                    Glide.with(getApplicationContext())
                            .load(products.getImgProducts_2())
                            .into(ivProductSmall2);
                    Glide.with(getApplicationContext())
                            .load(products.getImgProducts_3())
                            .into(ivProductSmall3);
                    calculateAverageRating();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void increaseProductQty() {
        ivIncreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQty < maxQty) {
                    productQty++;
                    tvProductQty.setText(String.valueOf(productQty));
                    ivIncreaseQty.setEnabled(productQty < maxQty);
                    ivDecreaseQty.setEnabled(productQty > 1);
                }
            }
        });
    }

    private void decreaseProductQty() {
        ivDecreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQty > 1) {
                    productQty--;
                    tvProductQty.setText(String.valueOf(productQty));
                    ivDecreaseQty.setEnabled(productQty > 1);
                    ivIncreaseQty.setEnabled(productQty < maxQty);
                }
            }
        });
    }

    private void createRatingsList() {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        String ratingPath = "Rating/" + productId + "/" + currentUserId;
        recyclerViewRating = new RecyclerView_Rating(data_ratings, this, R.layout.layout_item_review);
        GridLayoutManager layoutManager1 = new GridLayoutManager(this, 1);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        ratingRecyclerView.setLayoutManager(layoutManager1);
        ratingRecyclerView.setAdapter(recyclerViewRating);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rating").child(productId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (data_ratings != null) {
                    data_ratings.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);

                    data_ratings.add(rating);
                }
                recyclerViewRating.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý trường hợp có lỗi khi truy vấn dữ liệu
            }
        });
    }

    private void intent_getData(String products_id) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
        databaseReference.child(products_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);
                tvProductName.setText(products.getProducts_name());
                tvProductQty.setText("1");
                tvProductPrice.setText(products.getPrice());
                tvProductDesc.setText(products.getDescription());
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_1()).into(ivProductBig);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_1()).into(ivProductSmall1);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_2()).into(ivProductSmall2);
                Glide.with(Activity_Product_Detail.this).load(products.getImgProducts_3()).into(ivProductSmall3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setScreenData() {
        tvProductQty.setText(String.valueOf(productQty));
    }

    protected void changeBigProductImage() {
        ivProductSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivProductBig.setImageDrawable(ivProductSmall1.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct1).into(ivProductBig);
            }
        });

        ivProductSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivProductBig.setImageDrawable(ivProductSmall2.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct2).into(ivProductBig);
            }
        });
        ivProductSmall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivProductBig.setImageDrawable(ivProductSmall3.getDrawable());
                Glide.with(Activity_Product_Detail.this).load(imgProduct3).into(ivProductBig);
            }
        });
    }

    protected void addOrRemoveProductToWishList() {
        if (isUserLoggedin()) {
            ivAddWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isOnWishList) {
                        databaseReference = firebaseDatabase.getReference().child("WishList");
                        WishList wishList = new WishList(UID, productId, "");
                        databaseReference.child(UID).child(productId).setValue(wishList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Activity_Product_Detail.this, "Add Wish List Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        isOnWishList = true;
                        ivAddWishList.setImageResource(R.drawable.baseline_favorite_24);
                    } else {
                        firebaseDatabase.getReference().child("WishList").child(uid).child(productId).removeValue();
                        isOnWishList = false;
                        ivAddWishList.setImageResource(R.drawable.baseline_favorite_border_24);
                    }
                }
            });
        } else {
            ivAddWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginDialog();
                }
            });
        }
    }

    private void loginDialog() {
        AlertDialog.Builder loginDialog = new AlertDialog.Builder(Activity_Product_Detail.this);
        loginDialog.setTitle("Thông báo");
        loginDialog.setMessage("Bạn cần phải đăng nhập để thực hiện hành động này");
        loginDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Activity_Product_Detail.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        loginDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        loginDialog.create().show();
    }

    protected void setScreenElement() {

        //Recyclerview:

        ivBack = findViewById(R.id.ivProductDetailBack);
        ivProductBig = findViewById(R.id.ivProductDetailBigProduct);
        ivProductSmall1 = findViewById(R.id.ivProductDetailSmallProduct1);
        ivProductSmall2 = findViewById(R.id.ivProductDetailSmallProduct2);
        ivProductSmall3 = findViewById(R.id.ivProductDetailSmallProduct3);

        tvProductName = findViewById(R.id.tvProductDetailProductName);
        ivAddWishList = findViewById(R.id.ivProductDetailAddWishList);

        tvProductPrice = findViewById(R.id.tvProductDetailProductPrice);
        ivDecreaseQty = findViewById(R.id.ivProductDetailDecreaseQty);
        tvProductQty = findViewById(R.id.tvProductDetailProductQty);
        ivIncreaseQty = findViewById(R.id.ivProductDetailIncreaseQty);

        rbProductRating = findViewById(R.id.rbProductDetailProductRating);

        tvProductDesc = findViewById(R.id.tvProductDetailProductDesc);
        tvCartExist = findViewById(R.id.tvProductExistInCart);
        tvMaxQty = findViewById(R.id.tvMaxQty);

        btnAddCart = findViewById(R.id.btnProductDetailAddCart);
        btnBuyNow = findViewById(R.id.btnProductDetailBuyNow);

        rbUserRating = findViewById(R.id.rbProductDetailUserRating);

        ivComment = findViewById(R.id.ivProductDetailAddComment);
        edtComment = findViewById(R.id.edtProductDetailComment);

        ivMessenger = findViewById(R.id.ivProductDetailMessenger);

        rcDSlienquan = findViewById(R.id.rcDSSlienquan);
        ratingRecyclerView = findViewById(R.id.reviewList);
    }

    private void reView_products() {
        if (isUserLoggedin()) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String savedate = simpleDateFormat.format(calendar.getTime());
            String UserID = FirebaseAuth.getInstance().getUid();
            rbUserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    numberStar = ratingBar.getRating();
                }
            });
            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numberStar == 0 || TextUtils.isEmpty(edtComment.getText())) {
                        showRating_empty();
                    } else {

                        DatabaseReference userRatingRef = FirebaseDatabase.getInstance().getReference("Rating").child(productId).child(UserID);
                        userRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    rbUserRating.setRating(0);
                                    edtComment.setText("");
                                    showAlreadyReviewedDialog();
                                } else {

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rating").child(productId).child(UserID);
                                    final HashMap<String, Object> ratinglist = new HashMap<>();
                                    Rating rating = new Rating(productId, UserID, edtComment.getText().toString(), String.valueOf(numberStar), savedate);
                                    ratinglist.put("product_id", productId);
                                    ratinglist.put("customer_id", UserID);
                                    ratinglist.put("comment", rating.getComment());
                                    ratinglist.put("startNumber", rating.getStartNumber());
                                    ratinglist.put("create_at", savedate);

                                    reference.setValue(ratinglist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            rbUserRating.setRating(0);
                                            edtComment.setText("");
                                            Toast.makeText(Activity_Product_Detail.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Activity_Product_Detail.this, "Lưu không thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            });
        } else {
            rbUserRating.setVisibility(View.GONE);
            edtComment.setVisibility(View.GONE);
            ivComment.setVisibility(View.GONE);
        }

    }
    private void checkOrderStatusForRating(String productId, String uid) {
        DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference("OrderStatus");

        orderStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderStatusSnapshot : snapshot.getChildren()) {
                    OrderStatus orderStatus = orderStatusSnapshot.getValue(OrderStatus.class);
                    if (orderStatus.getStatus().equals("4") || orderStatus.getStatus().equals("6")) {
                        checkOrderForRating(orderStatus.getOrder_id(), productId, uid);
                    }
                    else {
                        ivComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Activity_Product_Detail.this, "Sảm phẩm này bạn chưa mua", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private void checkOrderForRating(String order_id, String productId, String uid) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order");
        orderRef.child(order_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order order = snapshot.getValue(Order.class);
                if (order != null && order.getCustomer_id().equals(uid)) {
                    if (order.getOrder_id() != null) {
                        for (CartDetail item : order.getItems()) {
                            if (item != null && item.getProduct_id().equals(productId)) {

                                reView_products();
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void calculateAverageRating() {
        DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference("Rating").child(productId);

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float totalStars = 0;
                    int totalRatings = 0;

                    for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                        Rating rating = ratingSnapshot.getValue(Rating.class);
                        if (rating != null) {
                            totalStars += Float.parseFloat(rating.getStartNumber());
                            totalRatings++;
                        }
                    }
                    if (totalRatings > 0) {
                        float averageRating = totalStars / totalRatings;
                        rbProductRating.setRating(averageRating);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


//    private void updateProductAverageRating(float averageRating) {
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(productId);
//        productRef.child("averageRating").setValue(averageRating);
//    }

    private void showAlreadyReviewedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Chỉ được chỉnh sửa không được đánh giá 2 lần");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showRating_empty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn cần nhập đầy đủ thông tin để đánh giá");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showRating_byOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Xin lỗi bạn chưa mua sản phẩm này");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public void getIDrating() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("Rating");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dsUser = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dsUser.add(dataSnapshot.getKey());
                }
                String[] temp = dsUser.get(dsUser.size() - 1).split("RT");
                String id = "";
                int idNumber = Integer.parseInt(temp[1]) + 1;
                if (idNumber < 10) {
                    id = "RT0" + idNumber;
                } else {
                    id = "RT" + idNumber;
                }
                autoId_rating = id;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isUserLoggedin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("check", "");
        return check.equals("true");
    }

    private void getData_DSLienquan(String categories_id) {
        recyclerViewProducts = new RecyclerViewProducts(this, R.layout.layout_item_products, data_products);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcDSlienquan.setLayoutManager(layoutManager);
        rcDSlienquan.setAdapter(recyclerViewProducts);
        Query queryProducts = databaseReference.orderByChild("categories_id").equalTo(categories_id);
        queryProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (data_products != null) {
                    data_products.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    if (!products.getProducts_id().equals(productId)) {
                        data_products.add(products);
                    }
                }
                recyclerViewProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}