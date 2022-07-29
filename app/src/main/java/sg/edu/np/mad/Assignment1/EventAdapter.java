package sg.edu.np.mad.Assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<EventList> eventLists;
    private final Context context;


    public EventAdapter(List<EventList> eventLists, Context context) {
        this.eventLists = eventLists;
        this.context = context;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {

        EventList eventList = eventLists.get(position);

        Picasso.get().load(eventList.getImg_url()).into(holder.event_img);

        holder.event_info.setText(eventList.getEvent_info());
        holder.time.setText(eventList.getTime());

    }

    public void updateData(List<EventList> eventLists) {
        this.eventLists = eventLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView event_img;
        private TextView time, event_info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            event_img = itemView.findViewById(R.id.event_img);
            time = itemView.findViewById(R.id.event_time);
            event_info = itemView.findViewById(R.id.event_info);

        }

    }
}