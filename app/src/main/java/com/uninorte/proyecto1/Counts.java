package com.uninorte.proyecto1;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by LauryV on 27/05/2017.
 */

public class Counts {

    public int countClass(String Class){
        final int[] count = new int[1];
        FirebaseDatabase.getInstance().getReference("noterubric").child(Class).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Long numChildren= dataSnapshot.getChildrenCount();

                count[0] =Integer.parseInt(String.valueOf(numChildren));
                Log.d("count", "onDataChange: "+count[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return count[0];
    }
}
