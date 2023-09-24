package com.example.taskhub.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskhub.Adapter.RecyclerViewTouchHelper;
import com.example.taskhub.Adapter.TaskAdapter;
import com.example.taskhub.AddNewTaskActivity;
import com.example.taskhub.MainActivity;
import com.example.taskhub.Model.ToDoModel;
import com.example.taskhub.OnDialogCloseListener;
import com.example.taskhub.R;
import com.example.taskhub.Utils.DataBaseHelper;
import com.example.taskhub.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements OnDialogCloseListener {
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private TaskAdapter adapter;
    private static final int REFRESH_INTERVAL = 1000; // Refresh every 1 second
    private Handler handler = new Handler();
    private Runnable refreshRunnable;
    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myDB = new DataBaseHelper(HomeFragment.this.requireContext());
        mList = new ArrayList<>();
        adapter = new TaskAdapter(myDB,HomeFragment.this.requireContext());
        startAutoRefresh();
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(HomeFragment.this.requireContext()));
        binding.recyclerview.setAdapter(adapter);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTaskActivity.newInstance().show(getChildFragmentManager(),AddNewTaskActivity.TAG);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerview);
    }
    private void stopAutoRefresh() {
        // Remove the refresh callback
        handler.removeCallbacks(refreshRunnable);
    }
    private void startAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Fetch new data from the database
                mList = myDB.getAllTasks();
                Collections.reverse(mList);

                // Update the adapter
                adapter.setTasks(mList);
                adapter.notifyDataSetChanged();

                // Schedule the next refresh
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        };

        // Start the refresh loop
        handler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop the auto refresh when the view is destroyed
        stopAutoRefresh();
    }
}