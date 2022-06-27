package sg.edu.np.mad.Assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    //Initialising variables
    List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_faqrow, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        //Get respective FAQ object position in list to set to Adapter
        FAQ faq = faqList.get(position);

        //Set FAQ object values
        holder.questiontxt.setText(faq.getQuestion());
        holder.desctxt.setText(faq.getDescription());

        boolean isExpandable = faqList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE: View.GONE);

    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public class FAQViewHolder extends RecyclerView.ViewHolder{

        //Initialising variables
        TextView questiontxt, desctxt;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public FAQViewHolder(@NonNull View itemView){
            super(itemView);

            //Assigning layout ID's
            questiontxt = itemView.findViewById(R.id.faq_question);
            desctxt = itemView.findViewById(R.id.faq_description);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            // Set onClick listener for linear layout
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FAQ faq = faqList.get(getAdapterPosition());
                    faq.setExpandable(!faq.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
