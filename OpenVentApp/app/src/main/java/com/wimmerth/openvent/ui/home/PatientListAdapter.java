package com.wimmerth.openvent.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wimmerth.openvent.R;
import com.wimmerth.openvent.data.Patient;

import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView nameTv, idTv, co2Tv, o2Tv, status, mode;

        ClickListener listener;

        public interface ClickListener {
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);
        }

        ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.listener = listener;
            nameTv = itemView.findViewById(R.id.textName);
            idTv = itemView.findViewById(R.id.textBed);
            co2Tv = itemView.findViewById(R.id.co2Min);
            o2Tv = itemView.findViewById(R.id.o2Min);
            status = itemView.findViewById(R.id.statusMin);
            mode = itemView.findViewById(R.id.currentMode);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bind(Patient patient) {
            nameTv.setText(patient.getName());
            idTv.setText(String.valueOf(patient.getId()));
            while (patient.getApiData() == null) { // TODO DAS HIER GEHT GARNICHT!!!
                // wenn kein netz da ist hängt sich die app auf.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double co2exh = patient.getApiData().getProcessed().getExpiredCO2();
            co2Tv.setText("CO2: " + patient.getApiData().getProcessed().getExpiredCO2());
            o2Tv.setText("O2: " + patient.getApiData().getProcessed().getExpiredO2());
            mode.setText(patient.getApiData().getProcessed().getVentilationMode());

            if (co2exh >= 5.2) {
                status.setText("Critical");
                status.setTextColor(Color.parseColor("#F44336"));
            } else if (co2exh > 5.0) {
                status.setText("Okay");
                status.setTextColor(Color.parseColor("#FFC107"));
            } else {
                status.setText("Good");
                status.setTextColor(Color.parseColor("#4CAF50"));
            }
        }

        @Override
        public void onClick(View view) {
            assert listener != null;
            listener.onItemClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            assert listener != null;
            return listener.onItemLongClicked(getAdapterPosition());
        }
    }

    private List<Patient> patients;
    private ViewHolder.ClickListener listener;

    PatientListAdapter(List<Patient> patients, ViewHolder.ClickListener listener) {
        this.patients = patients;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View patientView = inflater.inflate(R.layout.item_patient, parent, false);

        // Return a new holder instance
        return new ViewHolder(patientView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.bind(patient);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}