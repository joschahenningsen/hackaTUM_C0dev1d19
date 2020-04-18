package com.wimmerth.openvent.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wimmerth.openvent.PatientDetailsActiviy;
import com.wimmerth.openvent.R;
import com.wimmerth.openvent.connection.Caller;
import com.wimmerth.openvent.connection.CallerMeassurement;
import com.wimmerth.openvent.data.Change;
import com.wimmerth.openvent.data.Measurement;
import com.wimmerth.openvent.data.Patient;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PatientListAdapter.ViewHolder.ClickListener {

    private Context context;
    public static List<Patient> patients;
    private PatientListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Neuer Patient");
                alertDialog.setMessage("Patient und ID eingeben");
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText nameText = new EditText(context);
                nameText.setHint("Name");
                layout.addView(nameText);
                final EditText idText = new EditText(context);
                idText.setHint("ID");
                layout.addView(idText);
                alertDialog.setView(layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMarginEnd(20);
                lp.setMarginStart(20);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        patients.add(new Patient(nameText.getText().toString(), Integer.parseInt(idText.getText().toString())));
                    }
                });
                alertDialog.show();
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.patients);
        adapter = new PatientListAdapter(patients, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        context = getContext();
        adapter.notifyDataSetChanged();
        return root;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(context.getApplicationContext(), PatientDetailsActiviy.class);
        intent.putExtra("name", patients.get(position).getName());
        intent.putExtra("id", patients.get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Patient löschen");
        alertDialog.setMessage(patients.get(position).getName()+" löschen?");
        alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                patients.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        return true;
    }
}
