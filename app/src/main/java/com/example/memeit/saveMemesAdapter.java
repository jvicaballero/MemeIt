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
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

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
        Button removeMeme;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saveMemeTitle = itemView.findViewById(R.id.ivSaveMemesTitle);
            saveMemeImage = itemView.findViewById(R.id.ivSaveMemes);
            removeMeme = itemView.findViewById(R.id.btnRemove);

        }

        public void bind(Memes meme) {
            Log.i("SaveMemesComp", "on bind save memes url: " + meme.getMemeURL());
            Log.i("SaveMemesComp", "on bind save memes name: " + meme.getmemeName());

            saveMemeTitle.setText(meme.getmemeName());

            Glide.with(context).asGif().load(meme.getMemeURL()).into(saveMemeImage);

            removeMeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   //Now also have to change it in the user query, search for the object id in the array and remove that.
                    ParseQuery<ParseUser> savedMemesQuery = ParseUser.getQuery();
                    savedMemesQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser savedMemesDB, ParseException e) {
                            if(e == null){

//                                savedMemesDB.remove("savedMemes", meme);
//                                savedMemesDB.removeAll("savedMemes", meme);
                                Object savedMemes = savedMemesDB.getJSONArray("savedMemes").remove(getAdapterPosition());
//                                savedMemesDB.
//                                Log.i("RemoveMemesComp" , savedMemes.toString());
//
//                                savedMemes.remove(getAdapterPosition());

//                                savedMemesDB.remove("savedMemes");

                                Log.i("RemoveMemesComp" , "Meme Successfully Removed to parseobject! " + savedMemes.toString());

                                savedMemesDB.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.i("RemoveMemesComp" , "Inside saving");

                                        if(e == null){
                                            Toast.makeText(context, "Meme Removed!", Toast.LENGTH_SHORT).show();;
                                            Log.i("RemoveMemesComp" , "Meme Successfully Saved to user account!");
                                        }
                                        else{
                                            Log.i("RemoveMemesComp", "Error P2 in saving meme " + e);
                                        }
                                    }
                                });
                            }
                            else{
                                Log.e("RemoveMemesComp" , "Something went wrong saving " + e + savedMemesDB);
                            }

                        }
                    });

                    savedMemes.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

//    private Memes removeMemeFromDB(Object objectID){
//        Memes resultMemeQuery = new Memes();
//
//        Log.i("SaveMemesComp", "in function: " + objectID);
//        ParseQuery<Memes> query = ParseQuery.getQuery("memes");
//        query.whereEqualTo("objectId" , objectID);
//        query.getFirstInBackground(new GetCallback<Memes>() {
//            @Override
//            public void done(Memes result, ParseException e) {
//                if(e == null){
//                    String memeName = result.getmemeName();
//                    String memeURL = result.getMemeURL();
//
//                    resultMemeQuery.setMemeURL(memeURL);
//                    resultMemeQuery.setmemeName(memeName);
//                    Log.i("SaveMemesComp", "In function search memes: " + resultMemeQuery.getmemeName() + ' ' + resultMemeQuery.getMemeURL());
//                    memesList.add(resultMemeQuery);
//
//                    notifyDataSetChanged();
//                }
//                else{
//                    Log.e("SaveMemesComp", "Error retrieving saved memes: " + e);
//                }
//            }
//        });
//
//        return resultMemeQuery;
//    }
}
