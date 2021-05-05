package com.example.mapapp.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mapapp.MainActivity;
import com.example.mapapp.R;
import com.example.mapapp.database.DatabaseManager;
import com.example.mapapp.ui.bookmark.BookmarkItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.ThreadPoolExecutor;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private final double clickLat;
    private final double clickLong;
    private ThreadPoolExecutor threadPoolExecutor;
    private DatabaseManager dbManager;

    public BottomSheetDialog(double lat, double lon, ThreadPoolExecutor threadPoolExecutor) {
        clickLat = lat;
        clickLong = lon;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_save_location, container, false);
        TextView place = view.findViewById(R.id.save_location_modal_place_txt_view);
        EditText placeName = view.findViewById(R.id.save_location_modal_name_edit_txt);
        Button saveBtn = view.findViewById(R.id.save_location_modal_save_btn);
        saveBtn.setOnClickListener(view1 -> {
            dbManager.setTask(2);
            dbManager.setLocation(new BookmarkItem(placeName.getText().toString(), clickLat, clickLong));
            threadPoolExecutor.execute(dbManager);
            dismiss();
        });
        place.setText("Save Location " + "(" + String.format("\"%.2f\"", clickLat) + ", " + String.format("\"%.2f\"", clickLong) + ")" + " ?");
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.dbManager = DatabaseManager.getInstance(getContext());
    }
}
