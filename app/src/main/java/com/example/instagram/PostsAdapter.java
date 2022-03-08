package com.example.instagram;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> posts){
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivLike;
        private TextView tvLikes;
        private TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(Post post) {
            String username = post.getUser().getUsername();
            tvDescription.setText(Html.fromHtml("<b>@" + username + "</b> "+post.getDescription()));
            tvUsername.setText(username);
            tvDate.setText(post.getCreatedAt().toString());
            ParseFile image = post.getImage();
            if (image != null ){
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivImage);
            }
            tvLikes.setText(post.getLikes()+" likes");
            ivLike.setOnClickListener(v->{
                try {
                    List<String> likedPosts = ParseUser.getCurrentUser().fetch().getList("likedPosts");
                    if(!likedPosts.contains(post.getObjectId())){
                        int new_likes = post.getLikes()+1;
                        post.setLikes(new_likes);
                        post.saveInBackground(e -> {
                            if (e!=null){
                                Log.e(TAG,"Save failed",e);
                                Toast.makeText(context,"Error while saving", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.i(TAG,"saved post");
                            tvLikes.setText(new_likes+" likes");
                            ivLike.setImageResource(R.drawable.ufi_heart_active);
                            likedPosts.add(post.getObjectId());
                            ParseUser.getCurrentUser().put("likedPosts",likedPosts);
                            ParseUser.getCurrentUser().saveInBackground(err->{
                                if(err != null){
                                    Log.e(TAG,"User Like save failed",e);
                                    return;
                                }
                                Log.i(TAG,"User like saved");
                            });
                        });
                    }
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

            });
            try {
                if (ParseUser.getCurrentUser().fetch().getList("likedPosts").contains(post.getObjectId())) {
                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                    ivLike.setOnClickListener(null);
                }
            } catch (ParseException parseException){
                parseException.printStackTrace();
            }
        }
    }
}
