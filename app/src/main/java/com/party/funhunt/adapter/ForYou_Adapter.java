package com.party.funhunt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.party.funhunt.R;
import com.party.funhunt.activities.EventDetailsActivity;
import com.party.funhunt.model.EventList;
import com.party.funhunt.utils.Api;
import com.party.funhunt.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ermike on 4/8/2017.
 */

public class ForYou_Adapter extends RecyclerView.Adapter<ForYou_Adapter.ForYouViewHolder> {

    Context context;
    List<EventList> itemList = new ArrayList<>();

    public ForYou_Adapter(Context context, List<EventList> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ForYou_Adapter.ForYouViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_foryou_events, viewGroup, false);
        return new ForYou_Adapter.ForYouViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForYou_Adapter.ForYouViewHolder holder, int i) {

        final EventList eventList = itemList.get(i);

        holder.tv_venueName.setText(eventList.getVenueName());
        holder.tv_eventDate.setText(Utilities.getDateWithFormat("MMM d", eventList.getDate()) + ", " + eventList.getTime());
        holder.tv_eventDescription.setText(eventList.getDescription());
        holder.tv_artistName.setText(eventList.getArtistName());
        holder.tv_eventName.setText(eventList.getName());
        Glide.with(context).load(Api.BASE_URL + "images/event_" + eventList.getPhoto()).into(holder.iv_eventImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("eventId", eventList.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();

    }

    static class ForYouViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_eventImage)
        ImageView iv_eventImage;

        @InjectView(R.id.tv_venueName)
        TextView tv_venueName;

        @InjectView(R.id.tv_eventDate)
        TextView tv_eventDate;

        @InjectView(R.id.tv_eventDescription)
        TextView tv_eventDescription;

        @InjectView(R.id.tv_artistName)
        TextView tv_artistName;

        @InjectView(R.id.tv_eventName)
        TextView tv_eventName;

        public ForYouViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
