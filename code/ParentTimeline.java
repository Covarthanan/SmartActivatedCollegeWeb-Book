package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Glenn on 4/5/2018.
 */
public class ParentTimeline extends AppCompatActivity {


    RecyclerView mRecyclerView;
    DatabaseReference titleAndDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timeline);

        titleAndDesc = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        timelineStart();

    }

    public void timelineStart()
    {
        FirebaseRecyclerAdapter<CardDetails,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CardDetails, BlogViewHolder>(
                CardDetails.class,
                R.layout.card,
                BlogViewHolder.class,
                titleAndDesc
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, CardDetails model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
// Set the layout manager to your recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

}
