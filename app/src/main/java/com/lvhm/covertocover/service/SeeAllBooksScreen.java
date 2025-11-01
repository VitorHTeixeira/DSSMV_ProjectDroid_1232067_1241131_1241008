package com.lvhm.covertocover.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lvhm.covertocover.R;
import com.lvhm.covertocover.adapter.BookTabAdapter;
import com.lvhm.covertocover.adapter.OnBookClickListener;


public class SeeAllBooksScreen extends Fragment {

    private BookTabAdapter book_list_tab_adapter;
    private ViewPager2 view_pager;
    private TabLayout tab_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_see_all, container, false);

        view_pager = view.findViewById(R.id.view_pager);
        tab_layout = view.findViewById(R.id.tab_layout);

        System.out.println("Creating BookTabAdapter");
        book_list_tab_adapter = new BookTabAdapter(this);

        System.out.println("Setting adapter to ViewPager2");
        view_pager.setAdapter(book_list_tab_adapter);

        
        new TabLayoutMediator(tab_layout, view_pager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Want to Read");
                            break;
                        case 1:
                            tab.setText("Reading");
                            break;
                        case 2:
                            tab.setText("Read");
                            break;
                    }
                }
        ).attach(); 

        return view;
    }
}
