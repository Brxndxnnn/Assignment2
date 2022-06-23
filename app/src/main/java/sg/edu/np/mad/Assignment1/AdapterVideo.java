package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.HolderVideo> {

    private Context context;

    private ArrayList<ModelVideos> videosArrayList;


    public AdapterVideo(Context context, ArrayList<ModelVideos> videosArrayList) {
        this.context = context;
        this.videosArrayList = videosArrayList;
    }


    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listedvideos, parent, false);

        return new HolderVideo(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {

        Log.d("Viewholder", "Set videos");

        ModelVideos modelVideos = videosArrayList.get(position);

        String timestamp = modelVideos.getTimestamp();

        String test = modelVideos.videoUrl;

        //format timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String formattedDateTime = DateFormat.format("dd/MM/yyyy hh:mm", calendar).toString();


        //set data
        holder.videoTitle.setText(modelVideos.title);
        holder.videoTime.setText(formattedDateTime);

        MediaItem mediaItem = MediaItem.fromUri(test);
        holder.player.setMediaItem(mediaItem);

        holder.player.prepare();

    }


    @Override
    public int getItemCount() {
        return videosArrayList.size(); //return size of list
    }


    //VIEW HOLDER//

    public class HolderVideo extends RecyclerView.ViewHolder{

        PlayerView videoView;
        TextView videoTitle, videoTime;
        ExoPlayer player = new ExoPlayer.Builder(context).build();

        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoDisplayed);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoTime = itemView.findViewById(R.id.videoTime);

            videoView.setPlayer(player);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
