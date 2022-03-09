package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private EditText etComment;
    private Button btnComment;
    private RecyclerView rvComment;
    private CommentAdapter adapter;
    private List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        etComment = findViewById(R.id.etComment);
        btnComment = findViewById(R.id.btnComment);
        rvComment = findViewById(R.id.rvComment);

        comments = new ArrayList<>();

        adapter = new CommentAdapter(this,comments);

        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        String postId = getIntent().getStringExtra("id");

        btnComment.setOnClickListener(v->{
            String comm = etComment.getText().toString();
            saveComment(comm,postId);
        });

        queryComments(postId);
    }

    private void saveComment(String content, String id){
        Comment comment = new Comment();
        comment.setPost(id);
        comment.setContent(content);
        comment.setUser(ParseUser.getCurrentUser());

        comment.saveInBackground(e->{
            if(e!= null){
                Log.e(TAG,"Error saving comment",e);
                return;
            }
            Log.i(TAG,"Comment saved succesfully");
            comments.add(comment);
            adapter.notifyDataSetChanged();
        });
    }

    private void queryComments(String id){
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo(Comment.KEY_POST,id);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground((comments,e)->{
            if(e!=null){
                Log.e(TAG,"Couldn't query",e);
                return;
            }
            for (Comment comment:comments) {
                String user = null;
                try {
                    user = comment.getUser().fetchIfNeeded().getUsername();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                Log.i(TAG,"Comment: "+comment.getContent()+" "+user);
            }
            adapter.clear();
            adapter.addAll(comments);
        });
    }
}