package com.example.firebasejon;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.firebasejon.adapter.MyRecycleViewAdapter;
import com.example.firebasejon.note.Note;
import com.example.firebasejon.storage.MemoryStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //database firebase
    private final static String notes = "notes";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UI View
    private MyRecycleViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recycler with adapter
        recyclerView = findViewById(R.id.recyclerViewer);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecycleViewAdapter();
        recyclerView.setAdapter(adapter);

        // add headline UI
        button = findViewById(R.id.addHeadlineButton);
        editText = findViewById(R.id.headlineEditText);

        startNoteListener();

        // methods to test server with
        // editNote();
        // deleteNote();
    }

    public void editNote() {
        //edit
        DocumentReference docRef = db.collection(notes).document("8K2cP9EnkTpZ5xNTlawm");
        Map<String,String> map = new HashMap<>();
        map.put("head", "changed head");
        map.put("body", "changed body");
        docRef.set(map);
    }

    public void deleteNote() {
        // delete:
        DocumentReference docRef = db.collection(notes).document("MMc3yCJqpl3ebmCELsFs");
        docRef.delete();
    }

    // Goes through database collection and adds them to our list of notes aka the note object.
    public void startNoteListener() {
        db.collection(notes).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                MemoryStorage.notes.clear();
                for (DocumentSnapshot snap: values.getDocuments()) {
                    Log.i("all", "read from FB " + snap.getId() + " " + snap.get("body").toString());
                    MemoryStorage.notes.add(new Note(snap.getId(), snap.get("head").toString(), snap.get("body").toString()));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addNewNote(View view) {

        // access the database with DocumentReference
        DocumentReference docRef = db.collection(notes).document();
        // database uses a key and value so we use a HashMap for that
        final Map<String,String> map = new HashMap<>();

        // placeholder for headline input
        String noteName = editText.getText().toString();

        map.put("head", noteName);
        map.put("body", "This is a test");

        // clears input after adding
        editText.getText().clear();

        // Lastly we set the Map as the data to send.
        docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("all", "added successfully " + map);
            }
        });
    }
}