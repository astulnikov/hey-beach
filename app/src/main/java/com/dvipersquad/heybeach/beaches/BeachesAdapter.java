package com.dvipersquad.heybeach.beaches;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dvipersquad.heybeach.R;
import com.dvipersquad.heybeach.data.Beach;

import java.util.ArrayList;
import java.util.List;

public class BeachesAdapter extends RecyclerView.Adapter<BeachesAdapter.ViewHolder> {

    private List<Beach> beaches;

    public BeachesAdapter(List<Beach> beaches) {
        this.beaches = beaches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.beaches_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Beach beach = beaches.get(position);

        if (!TextUtils.isEmpty(beach.getUrl())) {
            //TODO: Load beach image
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
        ImageView imgBeachPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            imgBeachPhoto = itemView.findViewById(R.id.imgBeachPhoto);
        }
    }

}
