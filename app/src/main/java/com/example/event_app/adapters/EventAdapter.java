package com.example.event_app.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_app.EventActivity;
import com.example.event_app.MainActivity;
import com.example.event_app.data.Event;
import com.example.event_app.databinding.ItemEventBinding;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private final MainActivity mainActivity;
    private final List<Event> list;

    public EventAdapter(MainActivity mainActivity, List<Event> list) {
        this.mainActivity = mainActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEventBinding binding = ItemEventBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = list.get(position);

        holder.binding.nameItem.setText(event.getName());
        holder.binding.dateItem.setText(event.getDate());
        holder.binding.placeItem.setText(event.getPlace());
        holder.binding.phoneItem.setText(event.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, EventActivity.class);
            intent.putExtra(EventActivity.MODE, EventActivity.DETAILS);
            intent.putExtra(EventActivity.EVENT, list.get(position));
            intent.putExtra(EventActivity.POSITION, position);
            mainActivity.activityResultLauncher.launch(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void insertData(Event event) {
        list.add(event);
        notifyItemInserted(list.size() - 1);
    }
    public void updateData(int position, Event event) {
        list.set(position, event);
        notifyItemChanged(position);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        final ItemEventBinding binding;


        public EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
