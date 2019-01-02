package com.egeuni.earthquake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

public class EarthquakeDataAdapter extends RecyclerView.Adapter<EarthquakeDataAdapter.ViewHolder> {

    private ArrayList<Event> mDataset;
    private final EarthquakeAdapterOnClickHandler mClickHandler;
    @Inject
    Context appContext;

    public interface EarthquakeAdapterOnClickHandler {
        void onClick(Event currentEarthquake);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView iconTextView;
        public final TextView placeTextView;
        public final TextView timeTextView;
        public final TextView feltTextView;

        public ViewHolder (View v) {
            super(v);
            iconTextView = (TextView) v.findViewById(R.id.tv_icon);
            placeTextView = (TextView) v.findViewById(R.id.tv_place);
            timeTextView = (TextView) v.findViewById(R.id.tv_time);
            feltTextView = (TextView) v.findViewById(R.id.tv_felt);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Event currentEarthquake = mDataset.get(adapterPosition);
            mClickHandler.onClick(currentEarthquake);
        }
    }

    public EarthquakeDataAdapter (EarthquakeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        MainActivity.getMyComponent().inject(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        appContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.earthquake_list_item;
        LayoutInflater inflater = LayoutInflater.from(appContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Formatter formatter = new Formatter();
        holder.iconTextView.setText(mDataset.get(position).getMag());
        holder.placeTextView.setText(mDataset.get(position).getPlace());
        holder.timeTextView.setText(mDataset.get(position).getDate() + "\n" + mDataset.get(position).getHour());
        holder.feltTextView.setText(mDataset.get(position).getDepth());

        int shapeCode = formatter.getColorId(Double.parseDouble(mDataset.get(position).getMag()));
        holder.iconTextView.setBackground(appContext.getDrawable(shapeCode));
    }

    @Override
    public int getItemCount() {
        if (mDataset.size()==0) return 0;
        return mDataset.size();
    }

    public void setEarthquakeData(ArrayList<Event> nData) {
        mDataset = nData;
        notifyDataSetChanged();
    }


}
