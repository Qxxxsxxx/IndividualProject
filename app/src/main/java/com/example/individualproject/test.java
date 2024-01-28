package com.example.individualproject;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.individualproject.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class test {
    private DatabaseReference d;
    private View groupView;
    private ListView  listView;
    private  ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> groupList = new ArrayList<>();

    void test() {

        listView = (ListView)  groupView.findViewById(R.id.group_list);
        arrayAdapter = new ArrayAdapter<String>(arrayAdapter.getContext(), android.R.layout.simple_list_item_1,groupList);
        listView.setAdapter(arrayAdapter);
        Set<String> set = new HashSet<>();
        set.add("a");

        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator i = snapshot.getChildren().iterator();
                set.add(((DataSnapshot)i.next()).getKey());


                groupList.clear();
                groupList.addAll(set);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
