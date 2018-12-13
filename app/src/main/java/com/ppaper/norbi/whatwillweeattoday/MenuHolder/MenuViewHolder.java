package com.ppaper.norbi.whatwillweeattoday.MenuHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppaper.norbi.whatwillweeattoday.Interface.ItemClickListener;
import com.ppaper.norbi.whatwillweeattoday.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName = (TextView) itemView.findViewById(R.id.menu_text);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
    itemClickListener.onClick(v, getAdapterPosition(),false);
    }
}
