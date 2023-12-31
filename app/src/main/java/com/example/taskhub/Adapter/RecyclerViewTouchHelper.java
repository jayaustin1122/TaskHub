package com.example.taskhub.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskhub.MainActivity;
import com.example.taskhub.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TaskAdapter adapter;

    public RecyclerViewTouchHelper(TaskAdapter adapter) {
        super(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder =  new AlertDialog.Builder(adapter.getContext());
             builder.setTitle("Delete Task");
             builder.setMessage("Are you sure ?");
             builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     adapter.deleteTask(position);
                     Toast.makeText(adapter.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                 }
             });
             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     adapter.notifyItemChanged(position);
                 }
             });
             AlertDialog dialog =  builder.create();
             dialog.show();
        }
        else {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .setSwipeLeftActionIconTint(ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark))
                .addSwipeLeftActionIcon(R.drawable.baseline_edit_24)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftLabel("Delete")
                .addSwipeLeftLabel("Edit")
                .addSwipeLeftBackgroundColor(Color.GREEN)
                .addSwipeRightActionIcon(R.drawable.baseline_delete_24)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);



    }
}
