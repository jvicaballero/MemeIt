package com.example.memeit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memeit.MemesAdapter;
import com.example.memeit.Memes;
import com.example.memeit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MemeFragment extends Fragment {

    public static final String TAG= "MemeFragment";
    private RecyclerView rvMemes;
    protected List<Memes> allMemes;
    protected MemesAdapter adapter;

    public MemeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMemes = view.findViewById(R.id.rvMemes);
        allMemes= new ArrayList<>();
        adapter= new MemesAdapter(getContext(), allMemes);
        
        rvMemes.setAdapter(adapter);
        rvMemes.setLayoutManager(new LinearLayoutManager(getContext()));
        getMemes();
    }

    private void getMemes() {
        ParseQuery<Memes> query = ParseQuery.getQuery(Memes.class);
//        query.addDescendingOrder(Memes.);
        query.findInBackground(new FindCallback<Memes>() {
            @Override
            public void done(List<Memes> memes, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue: Meme query");
                    return;
                }
                for(Memes meme : memes){
                    Log.i(TAG, "Name: "+ meme.getmemeName() + ", URL: " + meme.getMemeURL());
                }
                adapter.clear();
                adapter.addAll(memes);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
