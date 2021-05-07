package com.binaryblenders.pocketassistant;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewpageAdapter extends FragmentStateAdapter {

    String userId;

    public MyViewpageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String userId) {
        super(fragmentManager, lifecycle);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new BalanceFragment(userId);
            case 1: return new TransactionsFragment(userId);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
