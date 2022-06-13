package com.example.invscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class CartListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    TextView totalselected, basecount;
    private Button otchot;

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
    }

    private void initView() {
        recyclerViewList = findViewById(R.id.recyclerView);
        totalselected = findViewById(R.id.totalselected);
        basecount = findViewById(R.id.basecount);
        otchot = findViewById(R.id.otchot);
    }

}