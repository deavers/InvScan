package com.example.invscan;

import static com.example.invscan.utils.ConstsKt.getNameByCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.invscan.adapters.CategoryAdapter;
import com.example.invscan.domain.enteties.CategoryWithCount;
import com.example.invscan.domain.enteties.InvItemChecked;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    TextView totalselected, basecount;
    private Button otchot;

    private CategoryAdapter adapter;

    private Integer countItems;
    private Integer countChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        initView();
        otchot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Формирование отчёта

                // Поделиться отчётом
            }
        });

        Intent intent = getIntent();
        ArrayList<InvItemChecked> list = intent.getParcelableArrayListExtra(LIST_KEY);
        countChecked = intent.getIntExtra(CHECKED_COUNT_KEY,-1);
        countItems = intent.getIntExtra(COUNT_KEY,-1);
        totalselected.setText(countChecked.toString());
        basecount.setText(countItems.toString());

        recyclerViewList.setHasFixedSize(true);
        adapter = new CategoryAdapter();
        recyclerViewList.setAdapter(adapter);
        setListOnAdapter(list);
    }

    List<CategoryWithCount> categoryWithCounts;

    private void setListOnAdapter(ArrayList<InvItemChecked> list) {
        categoryWithCounts = new ArrayList<>();
        Integer curCount;
        for (int i=1;i<=7; i++){
            curCount = 0;
            for (InvItemChecked itemChecked:list){
                if (itemChecked.getItem().getCategory_id() == i && itemChecked.getChecked()){
                    curCount++;
                }
            }
            if (curCount != 0)
            categoryWithCounts.add(new CategoryWithCount(i,getNameByCategory(i),curCount));
        }
        adapter.submitList(categoryWithCounts);
    }


    private void initView() {
        recyclerViewList = findViewById(R.id.recyclerView);
        totalselected = findViewById(R.id.totalselected);
        basecount = findViewById(R.id.basecount);
        otchot = findViewById(R.id.otchot);
    }


    private static final String LIST_KEY = "list";
    private static final String CHECKED_COUNT_KEY = "count_checked";
    private static final String COUNT_KEY = "count";

    public static Intent newInstance(Context context, List<InvItemChecked> list, Integer count, Integer checkedCount){
        Intent intent = new Intent(context, CartListActivity.class);
        intent.putParcelableArrayListExtra(LIST_KEY, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(COUNT_KEY,count);
        intent.putExtra(CHECKED_COUNT_KEY,checkedCount);
        return intent;
    }

}