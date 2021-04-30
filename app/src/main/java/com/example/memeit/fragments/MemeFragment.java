package com.example.memeit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memeit.MemesAdapter;
import com.example.memeit.Memes;
import com.example.memeit.R;

import java.util.ArrayList;
import java.util.List;

public class MemeFragment extends Fragment {

    public static final String TAG= "MemeFragment";
    private RecyclerView rvPosts;
    protected List<Memes> allMemes;
    protected MemesAdapter adapter;


    public MemeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts= view.findViewById(R.id.rvMemes);
        allMemes= new ArrayList<>();
    }
}
