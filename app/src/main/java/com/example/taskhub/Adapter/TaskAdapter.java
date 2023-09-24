package com.example.taskhub.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskhub.AddNewTaskActivity;
import com.example.taskhub.Fragments.HomeFragment;
import com.example.taskhub.Model.ToDoModel;
import com.example.taskhub.Utils.DataBaseHelper;
import com.example.taskhub.databinding.ItemRowBinding;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    //init views
    private List<ToDoModel> mList;
    private Context context;
    private DataBaseHelper myDatabase;


    public TaskAdapter(DataBaseHelper myDatabase, Context fragment){
        this.context = fragment;
        this.myDatabase = myDatabase;
    }


    //like inner class in kotlin
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemRowBinding binding;
        public MyViewHolder(@NonNull ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowBinding binding = ItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);

        //binding data to view in recycler
        holder.binding.mcheckbox.setText(item.getTask());
        holder.binding.mcheckbox.setChecked(toBoolean(item.getStatus()));
        holder.binding.mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    myDatabase.updateStatus(item.getId(),1);

                }
                else {
                    myDatabase.updateStatus(item.getStatus(),0);
                }
            }
        });
    }
    public Context getContext(){
        return context;
    }
    public void setTasks(List<ToDoModel>mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        myDatabase.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

        public void editItem(int position){
            ToDoModel item = mList.get(position);
            Bundle bundle  = new Bundle();
            bundle.putInt("id",item.getId());
            bundle.putString("task",item.getTask());

            AddNewTaskActivity task = new AddNewTaskActivity();
            task.setArguments(bundle);
            task.show(task.getChildFragmentManager(), task.getTag());
    }
    //make the status boolean
    public boolean toBoolean(int number){
        return number!=0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
