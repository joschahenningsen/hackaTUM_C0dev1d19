package com.wimmerth.openvent.ui.changeMonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wimmerth.openvent.R;
import com.wimmerth.openvent.data.Change;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descr, bedNr, prevState, newState;

        ClickListener listener;

        public interface ClickListener {
            void onItemClicked(int position);
        }

        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            bedNr = itemView.findViewById(R.id.bedNumberNews);
            descr = itemView.findViewById(R.id.description);
            prevState = itemView.findViewById(R.id.newsOldState);
            newState = itemView.findViewById(R.id.newsNewState);
            itemView.setOnClickListener(this);
        }

        void bind(Change change) {
            bedNr.setText(String.valueOf(change.getBed()));
            descr.setText(change.getDescr());
            prevState.setText(change.getOld());
            newState.setText(change.getNew());
        }

        @Override
        public void onClick(View view) {
            assert listener != null;
            listener.onItemClicked(getAdapterPosition());
        }

    }

    private List<Change> changes;
    private ViewHolder.ClickListener clickListener;

    public NewsListAdapter(List<Change> changes, ViewHolder.ClickListener clickListener) {
        this.changes = changes;
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View changeView = inflater.inflate(R.layout.breaking_news, parent, false);

        // Return a new holder instance
        return new ViewHolder(changeView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Change c = changes.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return changes.size();
    }
}
