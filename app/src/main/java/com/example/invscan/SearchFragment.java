package com.example.invscan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SearchFragment extends Fragment {

    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static String CLASSROOM_KEY = "classroom";

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String classroom) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(CLASSROOM_KEY, classroom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText searchlay = (EditText) view.findViewById(R.id.search_field);
        Button clearbut = (Button) view.findViewById(R.id.clearfilter);
        TextView datenow = (TextView) view.findViewById(R.id.textviewdate);
        TextView hoursnow = (TextView) view.findViewById(R.id.texthours);

        Button countobj = (Button) view.findViewById(R.id.countobjectss);

        searchlay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearbut.setVisibility(Button.VISIBLE);
                clearbut.setActivated(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String sUsername = searchlay.getText().toString();
                if (sUsername.matches("")) {
                    clearbut.setVisibility(Button.INVISIBLE);
                    clearbut.setActivated(false);
                }
            }
        });

        countobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        clearbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchlay.setText("");
            }
        });

        // Сегодняшняя дата
        long date = System.currentTimeMillis();
        long hours = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String dateString = sdf.format(date);
        String dateString1 = sdf1.format(hours);
        datenow.setText(dateString);
        hoursnow.setText(dateString1);
    }

}