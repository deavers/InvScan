package com.example.invscan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invscan.adapters.InvItemAdapter;
import com.example.invscan.domain.enteties.InventoryItem;
import com.example.invscan.interfaces.OnInvItemListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SearchFragment extends Fragment {

/*    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;*/
    private InvItemAdapter adapter;
    private SearchViewModel viewModel;
    List<InventoryItem> listItems;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static String SELECTED_NUM = "";
    private static String CLASSROOM_KEY = "classroom";

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
        Log.d("tag", SELECTED_NUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        SearchView searchlay = (SearchView) view.findViewById(R.id.search_field);
        Button clearbut = (Button) view.findViewById(R.id.clearfilter);
        TextView datenow = (TextView) view.findViewById(R.id.textviewdate);
        TextView hoursnow = (TextView) view.findViewById(R.id.texthours);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        Button countobj = (Button) view.findViewById(R.id.countobjectss);

        searchlay.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals("")){
                    clearbut.setVisibility(Button.VISIBLE);
                    clearbut.setActivated(true);
                } else {
                    clearbut.setVisibility(Button.INVISIBLE);
                    clearbut.setActivated(false);
                }
                onFilterItems(newText);
                return true;
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
                searchlay.setQuery("",false);
            }
        });

        recyclerView.setHasFixedSize(true);
        adapter = new InvItemAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemCheckListener(new OnInvItemListener() {
            @Override
            public void onInvItemClick(@NonNull InventoryItem inventoryItem, boolean checked) {
                Log.d("tag", ""+inventoryItem.getInventory_num()+" ="+checked);
            }
        });

        listItems = new ArrayList<>();
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

    private void onFilterItems(String newText) {
        List<InventoryItem> listFilter = new ArrayList<>();
        if(!newText.equals("")){
            for (InventoryItem item:listItems){
                if (item.getInventory_num().contains(newText)){
                    listFilter.add(item);
                }
            }
            adapter.submitList(listFilter);
        } else {
            adapter.submitList(listItems);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getItemsByClassRoomNum(SELECTED_NUM);
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                adapter.submitList(inventoryItems);
                listItems.clear();
                listItems.addAll(inventoryItems);
            }
        });
    }
}