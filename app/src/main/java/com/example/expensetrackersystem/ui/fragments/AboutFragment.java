package com.example.expensetrackersystem.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expensetrackersystem.BuildConfig;
import com.example.expensetrackersystem.R;

public class AboutFragment extends Fragment {
    View view;
    Context context;
    TextView version;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.about_fragment_layout,container,false);
        version=view.findViewById(R.id.version);
        version.setText("version "+BuildConfig.VERSION_NAME);
        return view;
    }
}
