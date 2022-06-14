package com.example.invscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.invscan.interfaces.MenuListener;

public class MainActivity extends AppCompatActivity implements MenuListener {
    // Инциализация навигационной панели
    MeowBottomNavigation bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment1 = new SearchFragment();

        if (fragment1 == new SearchFragment()) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value = extras.getString("key");
                extras.putString("key1",value);
                fragment1.setArguments(extras);
            }
        }

        // Переменная навигационной панели
        bottomNav = findViewById(R.id.bottomNav);

        // Объекты навигационной панели
        bottomNav.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        bottomNav.add(new MeowBottomNavigation.Model(2,R.drawable.ic_search));
        bottomNav.add(new MeowBottomNavigation.Model(3,R.drawable.ic_filter));


        // Показать нав. панель
        bottomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // Инициализация переменной вывода определённой страницы
                Fragment fragment = null;
                if (item.getId()==1) {
                    fragment = new HomeFragment();
                }else if (item.getId()==2) {
                    fragment = new SearchFragment();
                }else if (item.getId()==3) {
                    fragment = new SettingsFragment();
                }

                // Вывод страницы
                loadFragment(fragment);
            };
        });

        // Инициализация выбранного объекта
        bottomNav.show(1,true);
        // По нажатию на меню
        bottomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // Вывод красивого выбранного объекта
                /*Toast.makeText(getApplicationContext(),"You clicked "+item.getId(), Toast.LENGTH_SHORT).show();*/
            }
        });

        // При пере выборе объектов
        bottomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                /*Toast.makeText(getApplicationContext(),"You clicked "+item.getId(), Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        // Замена страниц
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container,fragment, null)
                .commit();
    }

    @Override
    public void onMenuClick(int menuOption) {
        if (menuOption == 2) {
            ////loadFragment(SearchFragment.newInstance(classroomNum));
            //// Инициализация выбранного объекта
            bottomNav.show(2,true);
        }
    }
}