﻿package com.tyt.bbs.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyt.bbs.R;
import com.tyt.bbs.entity.ArticleItem;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.utils.PubDateParser;

public class ArticelAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context contxt;
	Boolean hasQuote;
	private ArrayList<ArticleItem> listArticels;
	private ItemViewHolder holder;
	private ArticleItem  articleItem;
	private Boolean isWrite;
	private final Handler handler = new Handler(){
		//接收到消息后处理
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				//					holder.textView_article.setText(Html.fromHtml(articleItem.getArticle(),imgGetter,null));
				break;
			}
		}
	};

	public ArticelAdapter(Context context,ArrayList<ArticleItem> mArrayList) {
		// TODO Auto-generated constructor stub
		this.contxt = context;
		hasQuote =Property.getPreferences(contxt).getBoolean(Property.Quote, true);
		this.inflater = LayoutInflater.from(contxt);
		this.listArticels=mArrayList;
		isWrite=false;
	}

	public ArticelAdapter(Context context,ArrayList<ArticleItem> mArrayList,Boolean isWrite) {
		// TODO Auto-generated constructor stub
		this.contxt = context;
		hasQuote =Property.getPreferences(contxt).getBoolean(Property.Quote, true);
		this.inflater = LayoutInflater.from(contxt);
		this.listArticels=mArrayList;
		this.isWrite =isWrite;

	}


	public int getCount() {
		// TODO Auto-generated method stub
		return listArticels.size();
	}

	public ArticleItem getItem(int position) {
		// TODO Auto-generated method stub
		return listArticels.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		holder = new ItemViewHolder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_articlelist, null);
			holder.textView_author=(TextView)convertView.findViewById(R.id.textView_author);
			holder.textView_article=(TextView)convertView.findViewById(R.id.textView_article);
			holder.textView_time=(TextView)convertView.findViewById(R.id.textView_time);
			if(isWrite) 	{
				holder.textView_article.setTextColor(Color.WHITE);
				convertView.setBackgroundColor(color.transparent);
			}
			else {
				holder.textView_article.setTextColor(Color.BLACK);
				convertView.setBackgroundResource(R.drawable.bg_words);
			}
			convertView.setTag(holder);
		} else{
			holder = (ItemViewHolder)convertView.getTag();
		
		}
		if(listArticels.isEmpty()||getCount()==0)  return convertView;
		articleItem=getItem(position);

		String article= articleItem.getArticle();
		if(!hasQuote){
			if(article.contains("大作中提到: 】")){
				article = article.substring(0,article.indexOf("【 在"));
			}
		}
		holder.textView_article.setText(Html.fromHtml(DelRe(article),imgGetter,null));
		holder.textView_author.setText(articleItem.getAuthor());
		holder.textView_time.setText(articleItem.getTime());
		//		holder.textView_time.setText(PubDateParser.parse(articleItem.getTime()));

		return convertView;

	}

	private String DelRe(String article){
		if(article.contains(Property.ReplayMark))
			article = article.substring(article.indexOf("<br/>")+5,article.length());
		return article;
	}

	protected static class ItemViewHolder {
		private TextView textView_time;
		private TextView textView_article;
		private TextView textView_author;

	}

	public void clear(){
		listArticels.clear();
	}


	/**
	 * @return the listArticels
	 */
	public ArrayList<ArticleItem> getListArticels() {
		return listArticels;
	}


	/**
	 * @param listArticels the listArticels to set
	 */
	public void setListArticels(ArrayList<ArticleItem> listArticels) {
		this.listArticels = listArticels;
	}

	/**
	 * 处理<image>
	 */

	private ImageGetter imgGetter=new ImageGetter(){

		private Drawable     drawable;
		@Override
		public Drawable getDrawable(String source) {
			// TODO Auto-generated method stub
			String imageUrl=source.contains("http")?source:Property.Base_URL+source;
			//			Log.i("==ImageCache()==", articleItem.getImageCache().size()+"");
			if(articleItem.getImageCache().containsKey(imageUrl)) {
				drawable=articleItem.getImageCache().get(imageUrl);
				if(drawable!=null){
					drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*Property.Ratio) , (int)(drawable.getIntrinsicHeight()*Property.Ratio ));					
				}else{
					Log.i("Image", "=====Drawable is null ! =====");
				}
			}
			return drawable;
		}

	};

}
