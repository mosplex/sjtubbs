﻿package com.tyt.bbs.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyt.bbs.R;
import com.tyt.bbs.entity.ArticleItem;
import com.tyt.bbs.entity.PostItem;
import com.tyt.bbs.utils.PubDateParser;

public class PostListAdapter  extends BaseAdapter{
	ArrayList<PostItem> post;
	ArrayList<PostItem> repeat;

	private LayoutInflater mLayoutInflater; 

	public PostListAdapter(Context context,ArrayList<PostItem> post)
	{
		mLayoutInflater=LayoutInflater.from(context);
		this.post=post;
		Collections.sort(post, comparator);
		repeat =new ArrayList<PostItem>();
	}

	@Override
	public int getCount() { 
		return post.size();
	}  

	@Override
	public PostItem getItem(int pos) {  
		return post.get(pos); 
	}  

	@Override
	public long getItemId(int pos) {  
		return pos;  
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(getCount()==0) return null;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_postlist, null);
			viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.posttitle);
			viewHolder.postTimeTextView = (TextView)convertView.findViewById(R.id.posttime);
			viewHolder.postAuthorTextView = (TextView)convertView.findViewById(R.id.postauthorid);
			viewHolder.postIndexTextView=(TextView)convertView.findViewById(R.id.postindex);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		PostItem item =getItem(pos);
		viewHolder.titleTextView.setText(Html.fromHtml(item.getTitle()));
		viewHolder.postTimeTextView.setText(item.getTime());
		viewHolder.postAuthorTextView.setText(item.getAuthor());
		viewHolder.postIndexTextView.setText(item.getPostIndex());

		if(getItem(pos).getIsbottom()){
			viewHolder.postIndexTextView.setTextColor(Color.LTGRAY);
		}else{
			viewHolder.postIndexTextView.setTextColor(Color.RED);
		}

		return convertView;
	}

	public static class ViewHolder
	{
		public TextView titleTextView;
		public TextView postTimeTextView;
		public TextView postAuthorTextView;
		public TextView postIndexTextView;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost(ArrayList<PostItem> post) {
		repeat.clear();
		Collections.sort(post, comparator);
		if(repeat.size()!=0)
			for(PostItem pitem:repeat){
				post.remove(post.indexOf(pitem));
			}
		this.post = post;
		this.notifyDataSetChanged();
	}


	private Comparator<PostItem> comparator = new Comparator<PostItem>(){

		@Override
		public int compare(PostItem f1, PostItem f2) {
			// TODO Auto-generated method stub
			int r =f2.getPostIndex().compareToIgnoreCase(f1.getPostIndex());
			int t =f2.getTitle().compareToIgnoreCase(f1.getTitle());
			if(r==0&&t==0){
				repeat.add(f1);
			}
			return r;
		}

	};
}
