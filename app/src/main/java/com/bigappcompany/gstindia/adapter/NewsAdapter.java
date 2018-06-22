package com.bigappcompany.gstindia.adapter;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigappcompany.gstindia.R;
import com.bigappcompany.gstindia.activity.GSTActivity;
import com.bigappcompany.gstindia.activity.WebActivity;
import com.bigappcompany.gstindia.model.NewsModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sam on 27/2/17.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
	private final ArrayList<NewsModel> mItemList;

	public NewsAdapter() {
		mItemList = new ArrayList<>();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		final NewsModel model = mItemList.get(position);
		holder.featuredIV.getViewTreeObserver()
		    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			    @Override
			    public void onGlobalLayout() {
				    holder.featuredIV.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				    Picasso.with(holder.featuredIV.getContext())
					.load(model.getFeatureImageUrl())
					.resize(holder.featuredIV.getWidth(), holder.featuredIV.getHeight())
					.centerCrop()
					.into(holder.featuredIV, new Callback() {
						@Override
						public void onSuccess() {
							holder.featuredCLPB.setVisibility(View.GONE);
						}

						@Override
						public void onError() {

						}
					});
			    }
		    });

		holder.dateTV.setText(model.getDate());
		holder.titleTV.setText(model.getTitle());
		holder.authorTV.setText(holder.authorTV.getResources().getString(R.string.prefix_author, model.getAuthor()));
		holder.briefDescTV.setText(model.getShortDesc());
	}

	@Override
	public int getItemCount() {
		return mItemList.size();
	}

	public void clear() {
		mItemList.clear();
		notifyDataSetChanged();
	}

	public void addItem(NewsModel item) {
		mItemList.add(item);
		notifyItemInserted(mItemList.size() - 1);
	}

	public interface OnItemClickListener {

	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		ImageView featuredIV, featuredCLPB;
		TextView dateTV, titleTV, authorTV, briefDescTV;
		AppCompatButton readMoreBtn;

		ViewHolder(View itemView) {
			super(itemView);

			featuredIV = (ImageView) itemView.findViewById(R.id.iv_featured);
			featuredCLPB = (ImageView) itemView.findViewById(R.id.clpb_featured);

			dateTV = (TextView) itemView.findViewById(R.id.tv_date);
			titleTV = (TextView) itemView.findViewById(R.id.tv_title);
			authorTV = (TextView) itemView.findViewById(R.id.tv_author);
			briefDescTV = (TextView) itemView.findViewById(R.id.tv_brief_desc);
			readMoreBtn = (AppCompatButton) itemView.findViewById(R.id.btn_read_more);

			readMoreBtn.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(dateTV.getContext(), WebActivity.class);
			intent.putExtra(GSTActivity.EXTRA_HTML, mItemList.get(getAdapterPosition()).getLongDesc());
			intent.putExtra(GSTActivity.EXTRA_IS_URL, false);
			dateTV.getContext().startActivity(intent);
		}
	}
}
