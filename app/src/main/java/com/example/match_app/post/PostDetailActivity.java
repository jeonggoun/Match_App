package com.example.match_app.post;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.match_app.ChattingActivity;
import com.example.match_app.JoinActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.asynctask.post.PostDetail;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.fragment.SearchFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.MainActivity.user;
public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "main:PostDetailActivity";

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ImageView ivDetailImage, ivDetailBack;
    PostDTO dto;
    TextView tvDetailNickname, tvDetailTitle, tvDetailGame, tvDetailPlace, tvDetailTime, tvDetailContent, tvDetailFee, tvDetailEnd;

    /*???
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myMarker;
    LatLng myLoc;
    FragmentTransaction detailMap = getSupportFragmentManager().beginTransaction();

    FrameLayout frameMap;*/

    LinearLayout popupLayout;

    public final static String path = "matchapp/ChatMeta";

    public static Context postDetailActivityContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);



        postDetailActivityContext = this;

        ivDetailImage = findViewById(R.id.ivDetailImage);
        ivDetailBack = findViewById(R.id.ivDetailBack);

        tvDetailNickname = findViewById(R.id.tvDetailNickname);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailGame = findViewById(R.id.tvDetailGame);
        tvDetailPlace = findViewById(R.id.tvDetailPlace);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailFee = findViewById(R.id.tvDetailFee);
        tvDetailEnd = findViewById(R.id.tvDetailEnd);

        /*???
        frameMap = findViewById(R.id.frameMap);*/

        popupLayout = findViewById(R.id.popupLayout);



            findViewById(R.id.btnChat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (memberDTO.isAddrAuth() == true) {
                        startChatting(dto);
                    } else {
                        Toast.makeText(postDetailActivityContext, "??????????????? ?????? ????????????!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        Intent intent = getIntent();
        dto = (PostDTO) intent.getSerializableExtra("post");

        //https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F
        // 040ccb4d-ac06-4cf2-80d9-eb744a63ea28.jpg
        // ?alt=media


        // back ??????
        ivDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String filePath = "https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F" + dto.getImgPath() + "?alt=media";
        Glide.with(this).load(filePath).into(ivDetailImage);

        tvDetailTitle.setText(dto.getTitle());
        tvDetailGame.setText(dto.getGame());
        tvDetailContent.setText(dto.getContent());
        tvDetailNickname.setText(dto.getWriter());
        tvDetailPlace.setText(dto.getPlace());
        tvDetailTime.setText(dto.getTime());

        if(dto.getFee().equals("0"))
            tvDetailFee.setText("????????? ??????");
        else
            tvDetailFee.setText("????????? : " + dto.getFee() + "???");

        /*            ???
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @SuppressLint("MissingPermission")


            @Override
            public void onMapReady(GoogleMap googleMap) {

                map = googleMap;
                try{
                    map.setMyLocationEnabled(true);
                }catch (SecurityException e){

                }
                Location targetLocation = new Location("");
                targetLocation.setLatitude(Double.parseDouble(dto.getLatitude()));
                targetLocation.setLongitude(Double.parseDouble(dto.getLongitude())); //???????????? ?????? ?????? ??????
                showMyLocationMarker(targetLocation);  //????????????
                showCurrentLocation(targetLocation);  //????????? ??????
            }
        });

        // ????????? ?????????
        MapsInitializer.initialize(this);
*/


        // ????????? ?????????
        if(dto.getImgPath() == null) {
            ivDetailImage.setVisibility(View.GONE);
        }

        /*???
        // ????????? ?????? ????????? ??????????????? ?????????
//        Log.d(TAG, "onCreate: " + dto.getLatitude());
//        Log.d(TAG, "onCreate: " + dto.getLongitude());
        if(dto.getLatitude().equals("0.0") || dto.getLongitude().equals("0.0")){
            frameMap.setVisibility(View.GONE);
        }*/

        // ??? ?????? ?????? ??????/?????? ?????? ????????? ??? ?????? ??????, ?????? ??? ?????????
        if(user.getIdToken().equals(dto.getWriterToken())) {
            popupLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.btnChat).setVisibility(View.INVISIBLE);
        }

        if(dto.getMatchConfirm().equals("enable")){
            tvDetailEnd.setVisibility(View.GONE);
        }else if(dto.getMatchConfirm().equals("disable")){
            tvDetailEnd.setVisibility(View.VISIBLE);
        }

    }

    public void onPopupButtonClick(View button) {
        //PopupMenu ?????? ??????.
        PopupMenu popup = new PopupMenu(this, button);

        //????????? popup XML??? inflate.
        popup.getMenuInflater().inflate(R.menu.menu_post, popup.getMenu());

        //???????????? ?????? ??? ?????????
        // xml??? onClick ???????????? ????????? ?????????
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*??? ??????*/
                    case R.id.update:
                        /* update??? ???????????? ??? ????????? ?????? ?????? ?????? */
                        Intent intent = new Intent(PostDetailActivity.this, PostUpdateActivity.class);
                        intent.putExtra("post", dto);
                        startActivity(intent);

                        break;

                    /*??? ??????*/
                    case R.id.delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
                            builder.setTitle("?????? ?????????????????????????")        // ?????? ??????
                                    .setCancelable(false)        // ?????? ?????? ????????? ?????? ?????? ??????
                                    .setPositiveButton("??????", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int whichButton){

//                                            Log.d(TAG, "onClick: " + firebaseDatabase.getReference().child(dto.getPostKey()));

                                            //?????????1     databaseReference = firebaseDatabase.getReference("matchapp/Post");
                                            //                                            databaseReference.child(dto.getPostKey()).removeValue().

                                            //?????????2     firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().

                                            firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(PostDetailActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PostDetailActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("??????", new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int whichButton){

                                        }
                                    });

                            AlertDialog dialog = builder.create();    // ????????? ?????? ??????
                            dialog.show();    // ????????? ?????????
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void startChatting(PostDTO postDTO){
        MetaDTO meta = new MetaDTO();
        meta.setTitle(postDTO.getTitle());
        meta.setDate(postDTO.getTime());
        meta.setGame(postDTO.getGame());
        meta.setChatToken(user.getIdToken());
        meta.setPostToken(postDTO.getPostKey());
        meta.setNoty("0");
        ChattingDTO chattingDTO = new ChattingDTO();
        chattingDTO.setNickname(user.getNickName());
        meta.setRecent(chattingDTO);
        databaseReference2 = firebaseDatabase.getReference(path+"/"+postDTO.getWriterToken());
        databaseReference2.child(user.getIdToken()+postDTO.getPostKey()).setValue(meta);
        meta.setChatToken(postDTO.getWriterToken());
        chattingDTO.setNickname(postDTO.getWriter());
        databaseReference = firebaseDatabase.getReference(path+"/"+user.getIdToken());  // user??? ???????????? ?????? ???????????? ??????
        databaseReference.child(postDTO.getWriterToken()+postDTO.getPostKey()).setValue(meta);

        Intent intent = new Intent(this , ChattingActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

/*    ???
    private void showCurrentLocation(Location location) {
        LatLng curPoint =
                new LatLng(location.getLatitude(), location.getLongitude());
        // ?????? ????????? ??????????????? ??????
        myLoc = curPoint;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));
    }

    private void showMyLocationMarker(Location location){

        if(myMarker == null){
            myMarker = new MarkerOptions();
            myMarker.position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("??????");
            myMarker.icon
                    (BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myMarker);
        }else{
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            map.clear();
            map.addMarker(myMarker);
        }

    }*/


}

