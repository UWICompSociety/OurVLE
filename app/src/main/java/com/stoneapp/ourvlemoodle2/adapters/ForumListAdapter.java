/*
 * Copyright 2016 Matthew Stone and Romario Maxwell.
 *
 * This file is part of OurVLE.
 *
 * OurVLE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OurVLE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OurVLE.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.stoneapp.ourvlemoodle2.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.stoneapp.ourvlemoodle2.R;
import com.stoneapp.ourvlemoodle2.activities.DiscussionActivity;
import com.stoneapp.ourvlemoodle2.models.MoodleForum;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ForumViewHolder> {
    public static class ForumViewHolder extends RecyclerView.ViewHolder{
        TextView forum_name;
        TextView forum_description;
        ImageView forum_img;


        public ForumViewHolder(View itemView) {
            super(itemView);
            forum_name = (TextView)itemView.findViewById(R.id.forum_name);
            forum_description = (TextView)itemView.findViewById(R.id.forum_desc);
            forum_img = (ImageView)itemView.findViewById(R.id.forum_img);
        }
    }

    Context ctxt;
    List<MoodleForum> forums;
    String token;

    public ForumListAdapter(Context ctxt, List<MoodleForum> forums, String token) {
        this.ctxt = ctxt;
        this.forums = new ArrayList<>(forums);
        this.token = token;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_forum_item, parent, false);
        final ForumViewHolder forumViewHolder = new ForumViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = forumViewHolder.getAdapterPosition();
                if(position>=0)
                {
                    MoodleForum forum = forums.get(position);
                    String forumname = forum.getName();
                    String forumid = forum.getForumid() + "";
                    Intent intent = new Intent(ctxt, DiscussionActivity.class);
                    intent.putExtra("forumid", forums.get(position).getForumid());
                    intent.putExtra("forumname", forums.get(position).getName());
                    intent.putExtra("token", token);
                    ctxt.startActivity(intent);
                }

            }
        });
        return forumViewHolder;
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {
        final MoodleForum forum = forums.get(position);

        if (forum.getName() == null)
            holder.forum_name.setText("");
        else
            holder.forum_name.setText(forum.getName());

        if (forum.getIntro() == null)
            holder.forum_description.setText("");
        else
            holder.forum_description.setText(Html.fromHtml(forum.getIntro()).toString().trim()); // converts html to normal string

        String forumname = forum.getName();
        char firstLetter = forumname.toUpperCase().charAt(0);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(forumname);
        TextDrawable drawable2 = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter + "", color2);

        holder.forum_img.setImageDrawable(drawable2);
    }

    public void updateForumList(List<MoodleForum> newForums) {
        this.forums = new ArrayList<>(newForums);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }
}
