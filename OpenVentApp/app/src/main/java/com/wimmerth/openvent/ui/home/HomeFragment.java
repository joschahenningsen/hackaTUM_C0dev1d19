package com.wimmerth.openvent.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wimmerth.openvent.PatientDetailsActiviy;
import com.wimmerth.openvent.R;
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
        RecyclerView recyclerView = root.findViewById(R.id.patients);
        patients = new ArrayList<>();
        patients.add(new Patient("günda",1));
        adapter = new PatientListAdapter(patients,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(),DividerItemDecoration.VERTICAL));
        return root;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(context.getApplicationContext(), PatientDetailsActiviy.class);
        intent.putExtra("name",patients.get(position).getName());
        intent.putExtra("id",patients.get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }
}
