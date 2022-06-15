package com.example.invscan;

import static com.example.invscan.utils.ConstsKt.getItemsNames;
import static com.example.invscan.utils.ConstsKt.getNameByCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invscan.adapters.CategoryAdapter;
import com.example.invscan.domain.enteties.CategoryWithCount;
import com.example.invscan.domain.enteties.InvItemChecked;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CartListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewList;
    TextView totalselected, basecount;
    private Button otchot;

    private CategoryAdapter adapter;

    private Integer countItems;
    private Integer countChecked;

    String dateString;
    ArrayList<InvItemChecked> listItems;

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/InvScaner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        initView();

        // Сегодняшняя дата
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd");
        dateString = sdf.format(date);

        Intent intent = getIntent();
        listItems = intent.getParcelableArrayListExtra(LIST_KEY);
        countChecked = intent.getIntExtra(CHECKED_COUNT_KEY,-1);
        countItems = intent.getIntExtra(COUNT_KEY,-1);
        totalselected.setText(countChecked.toString());
        basecount.setText(countItems.toString());

        recyclerViewList.setHasFixedSize(true);
        adapter = new CategoryAdapter();
        recyclerViewList.setAdapter(adapter);
        setListOnAdapter(listItems);

        File dir = new File(path);
        dir.mkdir();

        otchot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Формирование отчёта
                formOtchot();
                // Поделиться отчётом

            }
        });
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

    public void formOtchot() {
        File file = new File (path + "/" + dateString + ".txt");

        Toast.makeText(getApplicationContext(), "Отчёт сохранён", Toast.LENGTH_SHORT).show();

        Save (file, totalselected.getText().toString(),basecount.getText().toString());

    }

    public void Save(File file, String data, String data1) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e ) { e.printStackTrace();}

        try {
            try {

                for (int i=1;i<=7; i++){
                    fos.write(getNameByCategory(i).getBytes(StandardCharsets.UTF_8));
/*                    for (int l=1; l<=Integer.parseInt(data1); l++) {
                        fos.write(inventarnumbers);
                    }*/
                    //listItems
                    fos.write(getItemsNames(i,listItems).getBytes(StandardCharsets.UTF_8));
                    fos.write("\n".getBytes(StandardCharsets.UTF_8));
                }

                fos.write("Выбранных объектов - ".getBytes(StandardCharsets.UTF_8));
                fos.write(data.getBytes(StandardCharsets.UTF_8));
                fos.write("\nВ базе находиться - ".getBytes(StandardCharsets.UTF_8));
                fos.write(data1.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException e) { e.printStackTrace(); }
        }
        finally {
            try {
                fos.close();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
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