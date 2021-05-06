package com.example.mapapp.ui.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapapp.R;
import com.example.mapapp.database.DatabaseManager;
import com.example.mapapp.ui.bookmark.BookmarkAdapter;
import com.example.mapapp.ui.bookmark.BookmarkFragment;
import com.example.mapapp.ui.map.MapFragment;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SettingFragment extends Fragment {

    private ThreadPoolExecutor threadPoolExecutor;
    private BookmarkFragment.UiHandler uiHandler;
    private DatabaseManager dbManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        this.dbManager = DatabaseManager.getInstance(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        LinearLayout ll = view.findViewById(R.id.delete_all_btn);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete whole history?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                BookmarkFragment bookmarkFragment = (BookmarkFragment) fragmentManager.findFragmentByTag("bookmark");
                                if (bookmarkFragment != null) {
                                    uiHandler = bookmarkFragment.uiHandler;
                                    dbManager.setUiHandler(uiHandler);
                                    dbManager.setTask(4);
                                } else {
                                    dbManager.setTask(5);
                                }
                                threadPoolExecutor.execute(dbManager);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Switch switchButton = getActivity().findViewById(R.id.switch3);
        int nightmode = AppCompatDelegate.getDefaultNightMode();
        switchButton.setChecked(nightmode == AppCompatDelegate.MODE_NIGHT_YES);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                    );
                } else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}