package com.egeuni.earthquake;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonDataAdapter extends RecyclerView.Adapter<PersonDataAdapter.ViewHolder> {

    private ArrayList<TaskUser> mDataSet;

    private final PersonAdapterOnClickHandler mClickHandler;
    private Activity mActivity;
    @Inject
    Context mContext;

    public interface PersonAdapterOnClickHandler {
        void onClick(TaskUser currentUser);
    }

    public PersonDataAdapter(PersonAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        MainActivity.getMyComponent().inject(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPersonImage;
        private TextView mName;
        private TextView mSurname;
        private TextView mRelation;
        private TextView mPlace;
        private Button mButton;

        public ViewHolder(@NonNull View v) {
            super(v);
            mPersonImage = (ImageView) v.findViewById(R.id.iv_person);
            mName = (TextView) v.findViewById(R.id.tv_name);
            mSurname =(TextView) v.findViewById(R.id.tv_surname);
            mRelation = (TextView) v.findViewById(R.id.tv_relation);
            mPlace = (TextView) v.findViewById(R.id.tv_place);
            mButton = (Button) v.findViewById(R.id.btn_delete);
            v.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TaskUser currentUser = mDataSet.get(adapterPosition);
            mClickHandler.onClick(currentUser);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.person_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TaskUser user = mDataSet.get(position);
        if(user.getGender().equals(mContext.getString(R.string.person_gender_female))) {
            holder.mPersonImage.setImageDrawable(mContext.getDrawable(R.drawable.icon_female));
        } else {
            holder.mPersonImage.setImageDrawable(mContext.getDrawable(R.drawable.icon_male));
        }
        holder.mName.setText(mDataSet.get(position).getName());
        holder.mSurname.setText(mDataSet.get(position).getSurname());
        holder.mRelation.setText(mDataSet.get(position).getRelation());
        holder.mPlace.setText(mDataSet.get(position).getPlace());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataSet.remove(position);
                setPersonData(mDataSet);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mDataSet.size() == 0)
            return 0;
        return mDataSet.size();
    }

    public void setPersonData(ArrayList<TaskUser> nData) {
        mDataSet = nData;
        notifyDataSetChanged();
    }

    public ArrayList<TaskUser> getPersonData() {
        return mDataSet;
    }

    public void setActivity(Activity a) {
        mActivity = a;
    }

}
