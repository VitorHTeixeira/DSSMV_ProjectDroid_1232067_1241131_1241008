package com.lvhm.covertocover.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lvhm.covertocover.service.BookListFragment;

public class BookTabAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public BookTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return BookListFragment.newInstance(BookListFragment.TYPE_WANT_TO_READ);
            case 1:
                return BookListFragment.newInstance(BookListFragment.TYPE_READING);
            case 2:
                return BookListFragment.newInstance(BookListFragment.TYPE_READ);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
