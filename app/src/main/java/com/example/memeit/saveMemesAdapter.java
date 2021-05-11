package com.example.memeit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class saveMemesAdapter extends RecyclerView.Adapter<saveMemesAdapter.ViewHolder> {

    Context context;
    List<Memes> savedMemes;

    public saveMemesAdapter(Context context, List<Memes> memes){
        this.context= context;
        this.savedMemes= memes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_save_memes, parent, false);
        return new ViewHolder(view);
    }

    public void clearThis(){
        Log.i("SaveMemesComp", "Inside clearThis()");
        savedMemes.clear();
        //notifyDataSetChanged();
    }

    public void addAll(List<Memes> meme){
        savedMemes.addAll(meme);
//        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memes meme = savedMemes.get(position);
        holder.bind(meme);
    }

    @Override
    public int getItemCount() {
        return savedMemes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView saveMemeTitle;
        ImageView saveMemeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saveMemeTitle = itemView.findViewById(R.id.ivSaveMemesTitle);
            saveMemeImage = itemView.findViewById(R.id.ivSaveMemes);
        }

        public void bind(Memes meme) {
            Log.i("SaveMemesComp", "on bind save memes url: " + meme.getMemeURL());
            Log.i("SaveMemesComp", "on bind save memes name: " + meme.getmemeName());

            saveMemeTitle.setText(meme.getmemeName());

            Glide.with(context).asGif().load(meme.getMemeURL()).into(saveMemeImage);

        }
    }
}
