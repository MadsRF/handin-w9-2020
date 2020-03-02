package com.example.firebasejon.viewHolder;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasejon.R;
import com.example.firebasejon.storage.MemoryStorage;

// is the functionality of the RecyclerView
public class ViewHolder extends RecyclerView.ViewHolder {

    int rowNumber = 0;
    TextView textView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        LinearLayout linearLayout = (LinearLayout)itemView;
        textView = linearLayout.findViewById(R.id.textViewInList);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Log.i("info", "you clicked " + (position+1));

            }
        });

    }

    public void setPosition(int position) {
        rowNumber = position;
        textView.setText(MemoryStorage.notes.get(position).getHead());
        Log.i("info", "note headline" + MemoryStorage.notes.get(position).getHead());

    }










}