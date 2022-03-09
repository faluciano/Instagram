package com.example.instagram;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    public static final String TAG = "CommentAdapter";
    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> comments){
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserComment=itemView.findViewById(R.id.tvUserComment);

        }

        public void bind(Comment comment) {
            String username = null;
            try {
                username = comment.getUser().fetchIfNeeded().getUsername();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            tvUserComment.setText(Html.fromHtml("<b>@" + username + "</b> "+comment.getContent()));
            Log.i(TAG,comment.getContent());
        }
    }
}
