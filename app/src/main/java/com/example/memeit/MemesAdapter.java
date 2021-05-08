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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    public void clear(){
        memes.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Memes> meme){
        memes.addAll(meme);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivMemePost;
        private TextView title;
        private TextView memenumbers;
        private LikeButton memelikebutton;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivMemePost= itemView.findViewById(R.id.ivMemePost);
            title= itemView.findViewById(R.id.memeTitle);
            memenumbers = itemView.findViewById(R.id.memenumbers);
            memelikebutton = itemView.findViewById(R.id.memelikebutton);
        }

        public void bind(Memes meme) {
            Log.i("Adapter", "Name: "+ meme.getmemeName() + ", URL: " + meme.getMemeURL());
            title.setText(meme.getmemeName());
            memenumbers.setText(String.valueOf(meme.voteVal()));
            Glide.with(context).asGif().load(meme.getMemeURL()).into(ivMemePost);
            memelikebutton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    meme.setvoteVal(meme.voteVal()+1);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("memes");
                    query.getInBackground(meme.getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            object.put("votes", meme.voteVal());
                            object.saveInBackground();
                        }
                    });
                    memenumbers.setText(String.valueOf(meme.voteVal()));
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    meme.setvoteVal(meme.voteVal()-1);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("memes");
                    query.getInBackground(meme.getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            object.put("votes", meme.voteVal());
                            object.saveInBackground();
                        }
                    });
                    memenumbers.setText(String.valueOf(meme.voteVal()));
                }
            });
        }
    }
}
