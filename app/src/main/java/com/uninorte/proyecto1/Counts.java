package com.uninorte.proyecto1;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by LauryV on 27/05/2017.
 */

public class Counts {

    public int countClass(String Class){
        final int[] cont = {0};
        FirebaseDatabase.getInstance().getReference("noterubric").child(Class).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snap: dataSnapshot.getChildren()) cont[0]++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return cont[0];
    }
}
