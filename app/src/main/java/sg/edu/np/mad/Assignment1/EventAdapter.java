package sg.edu.np.mad.Assignment1;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

        holder.event_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog builder = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView((R.layout.event_details_popup));

                ImageView imgV = builder.findViewById(R.id.event_popup_image);
                TextView title = builder.findViewById(R.id.event_popup_title);
                TextView time = builder.findViewById(R.id.event_popup_time);
                TextView desc = builder.findViewById(R.id.event_popup_desc);
                Button bClose = builder.findViewById(R.id.event_popup_cancelB);

                // img
                Picasso.get().load(eventList.getImg_url()).into(imgV);

                title.setText(eventList.getEvent_info());
                time.setText(eventList.getTime());
                desc.setText(eventList.getDescription());

                bClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });

                builder.show();
            }
        });
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
        private LinearLayout event_ll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });


            event_img = itemView.findViewById(R.id.event_img);
            time = itemView.findViewById(R.id.event_time);
            event_info = itemView.findViewById(R.id.event_info);
            event_ll = itemView.findViewById(R.id.event_layout);
        }

    }
}