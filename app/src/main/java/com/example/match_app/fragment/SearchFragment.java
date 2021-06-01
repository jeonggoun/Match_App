package com.example.match_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.PostAdapter;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostWriteActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "main: SearchFragment";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    MainActivity activity;

    //ListItem용
    RecyclerView recyclerView;
    PostAdapter adapter;
    ArrayList<PostDTO> dtos;

    Button btnWrite;

    //콤보박스용 items
    String[] items = {"전체", "테니스", "축구", "야구", "이스포츠"};
    Spinner spinner;
    String item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        databaseReference = firebaseDatabase.getReference("matchapp/Post");

        activity = (MainActivity) getActivity();
        ////////ListItem용
        // 반드시 생성해서 어댑터에 넘겨야 함
        dtos = new ArrayList<>();

        recyclerView = viewGroup.findViewById(R.id.recyclerView);
//
//        // 리사이클러뷰에서 반드시 초기화 시켜야함
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
//
//        // 어댑터 객체를 생성한다
        adapter = new PostAdapter(dtos, getContext());
//
//        // 어댑터에 있는 ArrayList에 dto를 5개 추가한다
//        ListItemDTO dto0 = new ListItemDTO(0, "테니스", "테니스 치실 분", "2021/5/26", "농성테니스장", "무료", "#");
//        adapter.addDto(dto0);    //어디 DTO 받아올 건지 물어볼 예정
//        databaseReference.push().setValue(dto0);
//
//        // 만든 어댑터를 리스트뷰에 붙인다
//        recyclerView.setAdapter(adapter);
        ////////

        //글작성 버튼 클릭시 화면전환
        btnWrite = viewGroup.findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostWriteActivity.class);
                startActivity(intent);
            }
        });

        //스피너 찾아주기
        spinner = viewGroup.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        //스피너에 어댑터 설정
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // 받은 어댑터에서 야구만 있는 어댑터를 만들어서 그어댑터를 setAdapter ?!
                item = items[position];

            }
            //////////////////왜 안 될까

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showPostList();
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();

        addItemsOnSpinner();
    }

    public void addItemsOnSpinner() {

    }

    private void showPostList() {
//     리스트 어댑터 생성 및 세팅
        final PostAdapter adapter
                = new PostAdapter(dtos, getContext());
        recyclerView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PostDTO dto = dataSnapshot.getValue(PostDTO.class);
                if(!item.isEmpty() && !item.equals("전체")){
                    if (item.equals(dto.getGame()))
                        adapter.addDto(dto);
                }else
                    adapter.addDto(dto);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }
}