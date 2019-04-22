package com.example.mapapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.mapapplication.R;
import com.example.mapapplication.model.Place;
import com.example.mapapplication.util.RvListener;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * places adapter for autocomplete text view place suggestions.
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<Place> placeArrayList;
    private RvListener mRvListener;
    private Filter filter;

    public PlacesAdapter(ArrayList<Place> placeArrayList, RvListener mRvListener) {
        this.placeArrayList = placeArrayList;
        this.mRvListener=mRvListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.layout_place_viewholder, viewGroup, false);
        return new PlacesAdapter.PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        PlacesAdapter.PlaceViewHolder placeViewHolder = (PlacesAdapter.PlaceViewHolder) viewHolder;
        placeViewHolder.tvPlace.setText(placeArrayList.get(i).getDescription());

        placeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRvListener.onRvItemClick(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }


    @Override
    public Filter getFilter() {
        return new CustomFilter(this);
    }

    public void updatePlaces(ArrayList<Place> placeArrayList){
        this.placeArrayList=placeArrayList;
        notifyDataSetChanged();
    }

    /**
     * place view holder in the suggestion recycler view.
     */
    class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlace;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tvPlaceRec);
        }
    }

    /**
     * custom filter class to filter the results.
     */
    class CustomFilter extends Filter{
        private PlacesAdapter placesAdapter;

        public CustomFilter(PlacesAdapter placesAdapter) {
            this.placesAdapter = placesAdapter;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults=new FilterResults();
            if(constraint==null || constraint.length()==0){
                filterResults.values=new ArrayList<Place>();
            }
            else{

            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count>0){
                notifyDataSetChanged();
            }else{

            }
        }
    }
}
