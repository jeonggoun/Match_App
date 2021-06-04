package com.example.match_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.adapter.ChatAdpter;
import com.example.match_app.dto.ChattingDTO;
import com.example.match_app.dto.MetaDTO;
import com.example.match_app.dto.PostDTO;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.match_app.MainActivity.user;
import static com.example.match_app.adapter.ChatListAdapter.dto;
public class ChattingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChattingDTO> ChattingDTOList;
    private String userName ,userToken;
    private String chat_name , chatToken;
    private MetaDTO chatMeta;
    private String path = "matchapp/ChatList";
    private String metaPath = "matchapp/ChatMeta";
    private EditText edt_chat;
    private Button btn_send, matchConfirm, matchCancel;
    private Context context = this;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private DatabaseReference toRef;
    private DatabaseReference toRefMeta, userMeta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);

        checkDangerousPermissions();
        userToken = user.getIdToken();
        Intent intent = getIntent();
        chatMeta = (MetaDTO) intent.getSerializableExtra("meta");
        chatToken = chatMeta.getChatToken();
        userName = intent.getStringExtra("userName");
        chat_name = chatMeta.getRecent().getNickname();

        btn_send = findViewById(R.id.btn_send);
        edt_chat = findViewById(R.id.edt_chat);

        matchConfirm = findViewById(R.id.chat_activity_match_confirm);
        matchCancel = findViewById(R.id.chat_activity_match_cancel);
        matchConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchCancel.setVisibility(View.VISIBLE);
                matchConfirm.setVisibility(View.GONE);
                setPost("disable");
            }
        });
        matchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchConfirm.setVisibility(View.VISIBLE);
                matchCancel.setVisibility(View.GONE);
                setPost("enable");
            }
        });
        if(chatMeta.getPostConfirm().equals("disable")){
            matchCancel.setVisibility(View.VISIBLE);
            matchConfirm.setVisibility(View.GONE);
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_chat.getText().toString();

                if (msg != null){
                    ChattingDTO dto = new ChattingDTO();
                    dto.setNickname(userName);
                    dto.setMsg(msg);
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("hh:mm:aa");
                    String getTime = simpleDate.format(mDate);
                    dto.setDate(getTime);
                    toRefMeta.child("recent").setValue(dto);
                    myRef.push().setValue(dto);
                    toRef.push().setValue(dto);
                    edt_chat.setText("");
                }
            }
        });

        mRecyclerView = findViewById(R.id.rcview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ChattingDTOList = new ArrayList<>();
        mAdapter = new ChatAdpter(ChattingDTOList , ChattingActivity.this , userName);
        mRecyclerView.setAdapter(mAdapter);


        myRef = database.getReference(path+"/"+userToken+"/"+chatToken);
        toRef = database.getReference(path+"/"+chatToken+"/"+userToken);
        toRefMeta = database.getReference(metaPath+"/"+chatToken+"/"+userToken);
        userMeta = database.getReference(metaPath+"/"+userToken+"/"+chatToken);


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChattingDTO dto = snapshot.getValue(ChattingDTO.class);
                ((ChatAdpter) mAdapter).addChat(dto);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {            }
        });
    }

    private void setPost(String set) {
        DatabaseReference post = database.getReference("matchapp/Post");
        post.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals(chatMeta.getPostToken())){
                    ArrayMap<String,String> map2 = new ArrayMap<>();
                    map2.put("matchConfirm" , set);
                    post.child(chatMeta.getPostToken()).updateChildren(Collections.unmodifiableMap(map2));
                    map2.clear();
                    map2.put("postConfirm" , set);
                    toRefMeta.updateChildren(Collections.unmodifiableMap(map2));//"postConfirm" : set
                    userMeta.updateChildren(Collections.unmodifiableMap(map2));//"postConfirm" : set
                    dto.setPostConfirm(set);
                    Toast.makeText(context, set, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}