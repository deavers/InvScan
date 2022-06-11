package com.example.invscan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchlay = (EditText) v.findViewById(R.id.search_field);
        Button clearbut = (Button) v.findViewById(R.id.clearfilter);

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


        // Сегодняшняя дата
        TextView datenow = (TextView) v.findViewById(R.id.textviewdate);
        TextView hoursnow = (TextView) v.findViewById(R.id.texthours);

        long date = System.currentTimeMillis();
        long hours = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String dateString = sdf.format(date);
        String dateString1 = sdf1.format(hours);
        datenow.setText(dateString);
        hoursnow.setText(dateString1);

        return v;
    }




}