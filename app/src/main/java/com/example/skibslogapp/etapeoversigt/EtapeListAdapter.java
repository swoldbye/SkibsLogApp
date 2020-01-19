package com.example.skibslogapp.etapeoversigt;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skibslogapp.GlobalContext;
import com.example.skibslogapp.R;
import com.example.skibslogapp.model.Etape;
import com.example.skibslogapp.postOversigt.PostActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EtapeListAdapter extends RecyclerView.Adapter<EtapeListAdapter.EtapeListViewHolder> {

    private List<Etape> etaper;

    EtapeListAdapter(List<Etape> etaper){
        this.etaper = etaper;
    }
/*
    // Add an Etape to the list
    void addEtape(Etape etape){
        etaper.add(etape);
        notifyItemInserted(etaper.size()-1);
    }*/

    void updateEtapeList(List<Etape> etaper){
        this.etaper = etaper;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    @NonNull
    @Override
    public EtapeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        EtapeListViewHolder viewHolder;
        if( viewType == 0 ){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.etape_list_destination, parent, false);
            viewHolder = new EtapeListViewHolder(view, false);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.etape_list_card, parent, false);
            viewHolder = new EtapeListViewHolder(view, true);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EtapeListViewHolder holder, int position) {

        if( getItemViewType(position) == 0){
            Etape etape = etaper.get((position)/2);
            holder.setDestination( etape.getStartDestination(), etape.getStartDate(), position==0);
        }else{
            int etapePosition = (position-1)/2;
            holder.position = etapePosition;
            holder.setEtape(etaper.get(etapePosition), etapePosition+1);
        }
    }

    @Override
    public int getItemCount() {
        return etaper.size()*2;
    }


    class EtapeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Etape etape;
        private View view;
        private boolean isEtapeCard;
        private int position;

        EtapeListViewHolder(View view, boolean isEtapeCard){
            super(view);
            this.view = view;
            this.isEtapeCard = isEtapeCard;
            //this.etape = etaper.
            if(isEtapeCard){
                view.setOnClickListener(this);
            }
        }


        // Updates the etape of the ViewHolder, changes the date to fit the Etape
        void setEtape(Etape etape, int listIndex){
            this.etape = etape;
            String indexString = Integer.toString(listIndex);
            ((TextView) view.findViewById(R.id.etape_number_text)).setText(indexString);

            // Change of card (entire card color, number text color and number container (number card) color)
            Resources res = GlobalContext.get().getResources();
            if( etape.getStatus() == Etape.Status.ACTIVE ){
                ((CardView) view.findViewById(R.id.etape_card)).setCardBackgroundColor(res.getColor(R.color.colorAccent));
                ((CardView) view.findViewById(R.id.etape_list_number_card)).setCardBackgroundColor(res.getColor(R.color.colorPrimary));
                ((TextView) view.findViewById(R.id.etape_number_text)).setTextColor(res.getColor(R.color.white));
            }else{
                ((CardView) view.findViewById(R.id.etape_card)).setCardBackgroundColor(res.getColor(R.color.offWhite));
                ((CardView) view.findViewById(R.id.etape_list_number_card)).setCardBackgroundColor(res.getColor(R.color.offWhite));
                ((TextView) view.findViewById(R.id.etape_number_text)).setTextColor(res.getColor(R.color.colorPrimary));
            }
        }

        void setDestination(String destination, Date date, boolean isFirst){
            if( isFirst ){
                (view.findViewById(R.id.etape_list_destination_line)).setVisibility(View.GONE);
            }else{
                (view.findViewById(R.id.etape_list_destination_line)).setVisibility(View.VISIBLE);
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String dateString = String.format( Locale.US, "%02d:%02d  %02d/%02d",
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH)+1
            );

            ((TextView) view.findViewById(R.id.etape_destination_text)).setText(destination == null ? "-" : destination);
            ((TextView) view.findViewById(R.id.etape_dato_text)).setText(dateString);
        }


        @Override
        public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragContainer, new PostActivity(getEtape(), position))
                        .addToBackStack(null)
                        .commit();

        }

        private Etape getEtape(){
            return etape;
        }
    }
}
