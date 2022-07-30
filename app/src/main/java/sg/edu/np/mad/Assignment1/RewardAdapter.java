package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder>
{
    //Initialising variables
    Context context;
    ArrayList<Rewards> rewardsArrayList;

    public RewardAdapter(Context context, ArrayList<Rewards> rewardsArrayList)
    {
        this.context = context;
        this.rewardsArrayList = rewardsArrayList;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View item = LayoutInflater.from(context).inflate(R.layout.activity_reward_viewholder, parent, false);

        return new RewardViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position)
    {
        //Get respective Listing object position in list to set
        Rewards rewards = rewardsArrayList.get(position);

        //Loading the Image into ImageView using Glide
        Glide.with(holder.logoProfile.getContext()).load(rewards.getLogoUrl()).into(holder.logoProfile);

        //Setting the Reward title into TextView
        holder.rewardTitle.setText(rewards.getRewardTitle());

        //Setting the Required Points into TextView
        holder.rewardPoints.setText(rewards.getRewardPoints().toString());

        //Setting the Quantity of Voucher Left into TextView
        holder.voucherQty.setText(rewards.getVoucherQty().toString());
    }

    @Override
    public int getItemCount()
    {
        return rewardsArrayList.size();
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder
    {
        //Initialising variables
        ImageView logoProfile;
        TextView rewardTitle;
        TextView voucherQty;
        TextView rewardPoints;

        public RewardViewHolder(@NonNull View itemView)
        {
            super(itemView);
            logoProfile = itemView.findViewById(R.id.logoProfile);
            rewardTitle = itemView.findViewById(R.id.voucherName);
            voucherQty = itemView.findViewById(R.id.voucherQty);
            rewardPoints = itemView.findViewById(R.id.exgPoint);

            //If reward listing is being selected, enlarge chosen listing
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //Start new intent
                    Intent intent = new Intent(view.getContext(), RewardDetails.class);
                    intent.putExtra("Title", rewardsArrayList.get(getBindingAdapterPosition()).rewardTitle);
                    intent.putExtra("Points", rewardsArrayList.get(getBindingAdapterPosition()).rewardPoints);
                    intent.putExtra("Qty", rewardsArrayList.get(getBindingAdapterPosition()).voucherQty);
                    intent.putExtra("Logo", rewardsArrayList.get(getBindingAdapterPosition()).logoUrl);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
