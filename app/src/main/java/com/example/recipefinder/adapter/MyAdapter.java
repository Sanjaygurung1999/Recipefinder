package com.example.recipefinder.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipefinder.frags.RecipeEquip;
import com.example.recipefinder.frags.RecipeIngre;
import com.example.recipefinder.frags.RecipeInst;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    String recipeId;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs,String recipeId) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.recipeId=recipeId;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("recipeid",recipeId);
        switch (position) {
            case 0:
                RecipeInst recipeInst = new RecipeInst();
                recipeInst.setArguments(bundle);
                return recipeInst;
            case 1:
                RecipeIngre recipeIngre= new RecipeIngre();
                recipeIngre.setArguments(bundle);
                return recipeIngre;
            case 2:
                RecipeEquip recipeEquip= new RecipeEquip();
                recipeEquip.setArguments(bundle);
                return recipeEquip;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
