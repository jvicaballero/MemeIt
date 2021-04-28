package com.example.memeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MemesAdapter extends RecyclerView.Adapter<MemesAdapter.ViewHolder> {

    private Context context;
    private List<Memes> memes;

    public MemesAdapter(Context context, List<Memes> memes){
        this.context= context;
        this.memes= memes;
    }

    @NonNull
    @Override
    public MemesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.meme_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memes meme= memes.get(position);
        holder.bind(meme);
    }

    @Override
    public int getItemCount() {
        return memes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivMemePost;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivMemePost= itemView.findViewById(R.id.ivMemePost);
        }

        public void bind(Memes meme) {
            if(meme.getMemeURL() != null)
                Glide.with(context).load(meme.getMemeURL()).into(ivMemePost);
        }


    }

}
