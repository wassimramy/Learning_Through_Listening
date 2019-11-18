package com.wrkhalil.learningthroughlistening;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ItemViewHolder> {

    private Context context;
    private List<Video> list;
    private OnItemAdapterItemClickListener onMyAdapterItemClickListener;

    //Instantiate the ItemAdapter
    VideoAdapter(Context context, List<Video> list, OnItemAdapterItemClickListener onMyAdapterItemClickListener) {
        this.list = list;
        this.context = context;
        this.onMyAdapterItemClickListener = onMyAdapterItemClickListener;
    }

    //Inflate row.xml for each recycleView item
    @NonNull
    @Override
    public VideoAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ItemViewHolder(view);
    }

    //Called to display item details in the recycler view
    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ItemViewHolder holder, final int position) {
        holder.itemTitleTextView.setText(list.get(position).getTitle()); //Set the itemTitleTextView in the row layout to "Image " + position to enumerate the pictures

        holder.itemDescriptionTextView.setText(list.get(position).plays + " Plays"); //Set the itemDateAndTime in the row layout to the formatted date and time
        RequestOptions requestOptions = new RequestOptions(); //Set the options of for the displayed picture
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Picture displayed when the app is fetching the picture
        requestOptions.error(R.drawable.ic_launcher_background); //Picture displayed when the picture is not fetched
        requestOptions.circleCrop(); //Display the picture in a circle view
        requestOptions.override(600, 600); //Set the resolution of the picture
        Glide.with(context)
                .load(Uri.parse(list.get(position).getThumbnailURL())) // Uri of the picture
                .apply(requestOptions) // Set the options
                .into(holder.itemImage); // The container where the picture is displayed
        holder.itemView.setOnClickListener(v -> onMyAdapterItemClickListener.onItemClicked(position)); //Return the position of the item when it is clicked by the user
    }

    //Returns the count of the items displayed in the recyclerView
    public int getItemCount() {
        return list.size();
    }

    //Captures the objects from the row layout
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitleTextView, itemDescriptionTextView;
        ImageView itemImage;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemTitleTextView = itemView.findViewById(R.id.itemTitleTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemTimeAndDateTextView);
        }
    }
}
