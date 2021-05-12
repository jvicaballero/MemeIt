package com.example.memeit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.lang.reflect.Array;
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
        private Button saveMeme;
        private TextView memenumbers;
        private LikeButton memelikebutton;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivMemePost= itemView.findViewById(R.id.ivMemePost);
            title= itemView.findViewById(R.id.memeTitle);
            saveMeme = itemView.findViewById(R.id.btnSaveMeme);
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

            saveMeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseQuery<ParseUser> savedMemesQuery = ParseUser.getQuery();
                    savedMemesQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser savedMemesDB, ParseException e) {
                            if(e == null){
                                Memes storeMeme = new Memes();

                                storeMeme.setmemeName(meme.getmemeName());
                                storeMeme.setMemeURL(meme.getMemeURL());

                                savedMemesDB.add("savedMemes", storeMeme);
                                Log.i("SaveMemesComp" , "Meme Successfully Saved to parseobject! " + savedMemesDB);

                                savedMemesDB.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.i("SaveMemesComp" , "Inside saving");

                                        if(e == null){
                                            Toast.makeText(context, "Meme Saved!", Toast.LENGTH_SHORT).show();;
                                            Log.i("SaveMemesComp" , "Meme Successfully Saved to user account!");
                                        }
                                        else{
                                            Log.i("SaveMemesComp", "Error P2 in saving meme " + e);
                                        }
                                    }
                                });
                            }
                            else{
                                Log.e("SaveMemesComp" , "Something went wrong saving " + e + savedMemesDB);
                            }

                        }
                    });
                }
            });
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
