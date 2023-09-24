package com.example.taskhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskhub.Model.ToDoModel;
import com.example.taskhub.Utils.DataBaseHelper;
import com.example.taskhub.Utils.NotificationHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTaskActivity extends BottomSheetDialogFragment {

    public static final  String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveBtkn;

    private DataBaseHelper myDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.add_newtask,container,false);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = view.findViewById(R.id.etRfid);
        mSaveBtkn = view.findViewById(R.id.button_save);

        myDatabase = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle!= null){
            isUpdate =true;
            String task = bundle.getString("task");
            mEditText.setText(task);
            
            if (task.length() >0){
                mSaveBtkn.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    mSaveBtkn.setEnabled(false);
                    mSaveBtkn.setBackgroundColor(Color.BLUE);
                }
                else{
                    mSaveBtkn.setEnabled(true);
                    mSaveBtkn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveBtkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();

                if (finalIsUpdate){
                    myDatabase.updateTask(bundle.getInt("id"),text);
                    Toast.makeText(getActivity(), "Task Updated", Toast.LENGTH_SHORT).show();
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDatabase.insertTask(item);
                    Toast.makeText(getActivity(), "Task Saved", Toast.LENGTH_SHORT).show();
                    NotificationHelper.sendNotification(getActivity(), "New Task Added",text);
                }
                dismiss();
            }
        });

    }
    public static AddNewTaskActivity newInstance(){
        return new AddNewTaskActivity();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}