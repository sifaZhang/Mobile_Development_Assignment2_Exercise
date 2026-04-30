package com.group1.exercise;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseExerciseActivity extends AppCompatActivity {
    EditText edtFirstName, edtLastName, edtAge, edtEmail;
    Button btnAdd, btnUpdate, btnDelete, btnSearch;
    RecyclerView recyclerView;

    DatabaseReference dbRef;
    StudentAdapter adapter;
    List<Student> studentList = new ArrayList<>();

    String selectedId = null; // update/delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAge = findViewById(R.id.edtAge);
        edtEmail = findViewById(R.id.edtEmail);

        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView = findViewById(R.id.recyclerView);

        // Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("students");

        // RecyclerView
        adapter = new StudentAdapter(studentList, student -> {
            selectedId = student.id;
            edtFirstName.setText(student.firstName);
            edtLastName.setText(student.lastName);
            edtAge.setText(String.valueOf(student.age));
            edtEmail.setText(student.email);
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        loadAllStudents();

        btnAdd.setOnClickListener(v -> addStudent());
        btnUpdate.setOnClickListener(v -> updateStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnSearch.setOnClickListener(v -> searchStudent());
    }

    private void loadAllStudents() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Student s = child.getValue(Student.class);
                    s.id = child.getKey();
                    studentList.add(s);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void addStudent() {
        if (edtAge.getText().toString().isEmpty()
                || edtFirstName.getText().toString().isEmpty()
                || edtLastName.getText().toString().isEmpty()
                || edtEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = dbRef.push().getKey();
        Student s = new Student(
                id,
                edtFirstName.getText().toString(),
                edtLastName.getText().toString(),
                Integer.parseInt(edtAge.getText().toString()),
                edtEmail.getText().toString()
        );
        dbRef.child(id).setValue(s);
        clearFields();
        Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
    }

    private void updateStudent() {
        if (selectedId == null) {
            Toast.makeText(this, "Please select a student", Toast.LENGTH_SHORT).show();
            return;
        }

        Student s = new Student(
                selectedId,
                edtFirstName.getText().toString(),
                edtLastName.getText().toString(),
                Integer.parseInt(edtAge.getText().toString()),
                edtEmail.getText().toString()
        );
        dbRef.child(selectedId).setValue(s);
        clearFields();
        Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteStudent() {
        if (selectedId == null) {
            Toast.makeText(this, "Please select a student", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(selectedId).removeValue();
        clearFields();
        Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        edtFirstName.setText("");
        edtLastName.setText("");
        edtAge.setText("");
        edtEmail.setText("");
    }

    private void searchStudent() {
        String name = edtFirstName.getText().toString();
        if (name.isEmpty()){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.orderByChild("firstName").equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        studentList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Student s = child.getValue(Student.class);
                            studentList.add(s);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        clearFields();
    }
}