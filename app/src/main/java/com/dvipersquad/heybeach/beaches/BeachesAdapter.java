package com.dvipersquad.heybeach.beaches;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvipersquad.heybeach.Injection;
import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.util.BeachUrlBuilder;
import com.dvipersquad.heybeach.util.imageloading.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class BeachesAdapter extends RecyclerView.Adapter<BeachesAdapter.ViewHolder> {

    private List<Beach> beaches;

    private ImageLoader imageLoader;

    public BeachesAdapter(List<Beach> beaches) {
        this.beaches = beaches;
        imageLoader = Injection.provideImageLoader();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.beaches_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Beach beach = beaches.get(position);

        if (beach.getUrl() != null && !beach.getUrl().isEmpty()) {
            imageLoader.loadImage(BeachUrlBuilder.generate(beach.getUrl()), new ImageLoader.LoadImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    holder.imgBeachPhoto.setImageBitmap(bitmap);
                    holder.imgBeachPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.imgBeachPhoto.setVisibility(View.VISIBLE);
                    holder.imgPlaceholderIcon.setVisibility(View.GONE);
                }

                @Override
                public void onImageNotAvailable() {
                    holder.imgBeachPhoto.setVisibility(View.GONE);
                    holder.imgPlaceholderIcon.setVisibility(View.VISIBLE);
                }
            });
        }

        if (beach.getTitle() != null && !beach.getTitle().isEmpty()) {
            holder.viewImageOverlay.setVisibility(View.VISIBLE);
            holder.txtBeachTitle.setText(beach.getTitle());
            holder.txtBeachTitle.setVisibility(View.VISIBLE);
        } else {
            holder.viewImageOverlay.setVisibility(View.GONE);
            holder.txtBeachTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return beaches.size();
    }

    void replaceData(List<Beach> beaches) {
        if (this.beaches == null) {
            this.beaches = new ArrayList<>();
        }
        this.beaches.clear();
        this.beaches.addAll(beaches);
        notifyDataSetChanged();
    }

    void addData(List<Beach> beaches) {
        if (this.beaches == null) {
            this.beaches = new ArrayList<>();
        }
        this.beaches.addAll(beaches);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View layout;
        ImageView imgPlaceholderIcon;
        ImageView imgBeachPhoto;
        View viewImageOverlay;
        TextView txtBeachTitle;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            imgBeachPhoto = itemView.findViewById(R.id.imgBeachPhoto);
            imgPlaceholderIcon = itemView.findViewById(R.id.imgPlaceholderIcon);
            viewImageOverlay = itemView.findViewById(R.id.viewImageOverlay);
            txtBeachTitle = itemView.findViewById(R.id.txtBeachTitle);
        }
    }

}
