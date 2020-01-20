package com.example.skibslogapp.postOversigt;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.example.skibslogapp.GlobalContext;
import com.example.skibslogapp.model.Logpunkt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TODO use FragmentStatePagerAdapter because we will be working with dynamic no. of tabs
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs; // The total number of tabs
    Fragment fragment = null;
    List<List<Logpunkt>> etapper;
    PostOversigt postOversigt;
    //List<PostOversigt> fragmentList = new ArrayList<>();
    private Map<Integer, Fragment> fragmentMap;



    public PageAdapter(FragmentManager fragmentManager, int numOfTabs, List<List<Logpunkt>> etapper){
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
        this.etapper = etapper;
        fragmentMap = new HashMap<Integer, Fragment>();
        System.out.println("Count is : " + getCount());
    }



    /**
     * This decides which fragments to call from which tab
     *
     * @param position the position of the tab
     *
     * @return The fragment you want to change
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        PostOversigt postOversigt = new PostOversigt(etapper.get(position));
        fragmentMap.put(position, postOversigt);
        return postOversigt;
    }



    /**
     * Returns the total number of tabs in the tab layout
     */
    @Override
    public int getCount() {
        return etapper.size();
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_UNCHANGED;
//    }

//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        PostOversigt postOversigt = (PostOversigt) super.instantiateItem(container, position);
//        //fragmentList.add(postOversigt);
//        fragmentMap.put(position, postOversigt);
//        return postOversigt;
//    }

    public Fragment getFragment (int position){
        //return fragmentList.get(position);
        Fragment returnFragment = fragmentMap.get(position);
        if(returnFragment == null){
            return null;
        }
        return returnFragment;
    }

    /**
     * Set a title for each tab with a position
     *
     * @param position The position of the tab
     * @return  the title
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "E-" + (position + 1);
    }


    public void updateList(List<List<Logpunkt>> newEtapper, int position, FragmentActivity FA){
        this.etapper = newEtapper;

        //PostOversigt postOversigt = (PostOversigt) getFragment(position);
        //postOversigt.updateAdapter(newEtapper.get(position));

        //this.getItem(position);

        PostOversigt existing = (PostOversigt) getFragment(position);
        RecyclerAdapter recyclerAdapter = (RecyclerAdapter) existing.getAdapter();
        recyclerAdapter.updateData(newEtapper.get(position));
        existing.postRecyclerView.smoothScrollToPosition(etapper.get(position).size()-1);
//        final FragmentTransaction FT = FA.getSupportFragmentManager().beginTransaction();
//        FT.detach(existing);
//        FT.attach(existing);
//        FT.commit();
//        this.notifyDataSetChanged();
        //recyclerAdapter.notifyItemInserted(newEtapper.get(position).size() - 1);
        //this.notifyDataSetChanged();
        //postOversigt.updatemAdapter(etapper.get(position));
    }



}