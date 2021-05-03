package com.example.mapapp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final String inputString;

    public BottomSheetDialog(String input) {
        inputString = input;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_save_location, container, false);
        TextView place = view.findViewById(R.id.save_location_modal_place_txt_view);
        Button saveBtn = view.findViewById(R.id.save_location_modal_save_btn);
        saveBtn.setOnClickListener(view1 -> {
            /*
            //TODO: add on click listener for save button here
             */
        });
        place.setText(inputString);
        return view;
    }
}
