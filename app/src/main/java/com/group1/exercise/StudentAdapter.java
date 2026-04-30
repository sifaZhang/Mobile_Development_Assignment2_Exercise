package com.group1.exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> list;
    private OnStudentClickListener listener;

    public StudentAdapter(List<Student> list, OnStudentClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<Student> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(v);
    }

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student s = list.get(position);
        holder.txtFirstName.setText("FirstName: " + s.firstName);
        holder.txtLastName.setText("LastName: " + s.lastName);
        holder.txtAge.setText(String.valueOf("Age: " + s.age));
        holder.txtEmail.setText("Email: " + s.email);


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStudentClick(s);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtFirstName, txtLastName, txtAge, txtEmail;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstName = itemView.findViewById(R.id.txtFirstName);
            txtLastName = itemView.findViewById(R.id.txtLastName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }
}

