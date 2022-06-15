package com.example.invscan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.invscan.adapters.InvItemAdapter;
import com.example.invscan.domain.enteties.InvItemChecked;
import com.example.invscan.domain.enteties.InventoryItem;
import com.example.invscan.interfaces.OnInvItemListener;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private InvItemAdapter adapter;
    private SearchViewModel viewModel;
    List<InvItemChecked> itemsCheckedList;
    List<InvItemChecked> itemsCheckedList2;
    Integer count1 = 0;


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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }
    SearchView searchlay;
    RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchlay = (SearchView) view.findViewById(R.id.search_field);
        Button clearbut = (Button) view.findViewById(R.id.clearfilter);
        TextView datenow = (TextView) view.findViewById(R.id.textviewdate);
        TextView hoursnow = (TextView) view.findViewById(R.id.texthours);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        Button countobj = (Button) view.findViewById(R.id.countobjectss);
        Button scannerbt = (Button) view.findViewById(R.id.btnScan);

        scannerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            }
        });

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
                //Log.d("tag", ""+inventoryItem.getInventory_num()+" ="+checked);
                for(InvItemChecked itemChecked: itemsCheckedList){
                    if (itemChecked.getItem().getInventory_num().equals(inventoryItem.getInventory_num())){
                        itemChecked.setChecked(checked);
                        if (checked == true) {
                            count1++;
                        }
                        if (checked == false) {
                            count1--;
                        }
                        countobj.setText(count1.toString());
                        break;
                    }

                }
            }
        });

        countobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CartListActivity.newInstance(
                        getActivity(),
                        itemsCheckedList,
                        itemsCheckedList.size(),
                        getCountOfCheck()
                ));
            }
        });

        //listItems = new ArrayList<>();
        itemsCheckedList = new ArrayList<>();
        itemsCheckedList2 = new ArrayList<>();
        // Сегодняшняя дата
        long date = System.currentTimeMillis();
        long hours = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String dateString = sdf.format(date);
        String dateString1 = sdf1.format(hours);
        datenow.setText(dateString);
        hoursnow.setText(dateString1);

        viewModel.getItemsByClassRoomNum(SELECTED_NUM);
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                //listItems.clear();
                if (searchOption == 1){
                    countobj.setVisibility(View.VISIBLE);
                    itemsCheckedList.clear();
                    adapter.setOption(InvItemAdapter.OPTION_DEFAULT);
                    for (InventoryItem item:inventoryItems){
                        itemsCheckedList.add(new InvItemChecked(item,false));
                    }
                    adapter.setData(itemsCheckedList);
                } else {
                    countobj.setVisibility(View.INVISIBLE);
                    itemsCheckedList2.clear();
                    adapter.setOption(InvItemAdapter.OPTION_WITHOUT_SWITCH);
                    for (InventoryItem item:inventoryItems){
                        itemsCheckedList2.add(new InvItemChecked(item,false));
                    }
                    adapter.setData(itemsCheckedList2);
                }
                //listItems.addAll(inventoryItems);
            }
        });

    }

    private Integer getCountOfCheck(){
        Integer count = 0;
        for(InvItemChecked itemChecked: itemsCheckedList){
            if (itemChecked.getChecked()){
                count++;
            }
        }
        return count;
    }

    private Integer searchOption = 1;

    private void onFilterItems(String newText) {
        List<InvItemChecked> listFilter = new ArrayList<>();
        if (searchOption == 1){
            if(!newText.equals("")){
                for (InvItemChecked item: itemsCheckedList){
                    if (item.getItem().getInventory_num().contains(newText)){
                        listFilter.add(item);
                    }
                }
                if (listFilter.size() != 0){
                    adapter.setData(listFilter);
                } else {
                    searchOption = 2;
                    viewModel.getAllItems();
                }
            } else {
                adapter.setData(itemsCheckedList);
            }
        } else {
            if(!newText.equals("")){
                for (InvItemChecked item: itemsCheckedList2){
                    if (item.getItem().getInventory_num().contains(newText)){
                        listFilter.add(item);
                    }
                }
                if (listFilter.size() != 0){
                    adapter.setData(listFilter);
                } else {
                    showDialog();
                }
            } else {
                adapter.setData(itemsCheckedList2);
            }
        }

    }

    private void showDialog() {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Предмет не найден в глобальном поиске!",Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchlay.setQuery("",true);
                        searchOption = 1;
                        count1 = 0;
                        viewModel.getItemsByClassRoomNum(SELECTED_NUM);
                    }
                }).show();
        /*AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle("Результат поиска");
        alertDialog.setMessage("");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int which){
                searchOption = 1;
                viewModel.getItemsByClassRoomNum(SELECTED_NUM);
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();*/
    }
}