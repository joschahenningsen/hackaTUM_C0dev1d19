package com.wimmerth.openvent.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wimmerth.openvent.PatientDetailsActiviy;
import com.wimmerth.openvent.R;
import com.wimmerth.openvent.connection.AlarmServerConnectionService;
import com.wimmerth.openvent.connection.BreakConnectionService;
import com.wimmerth.openvent.data.Change;
import com.wimmerth.openvent.data.Patient;
import com.wimmerth.openvent.ui.home.HomeFragment;

import java.util.List;

public class GalleryFragment extends Fragment implements NewsListAdapter.ViewHolder.ClickListener {

    public static boolean pause;
    public static List<Change> changes;
    private Context context;
    private NewsListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final Button button = root.findViewById(R.id.breakButton);
        if (pause) {
            button.setText(R.string.endBreak);
        } else {
            button.setText(R.string.newBreak);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause) {
                    button.setText(R.string.newBreak);
                    //BreakConnectionService.endPause(changes);
                    AlarmServerConnectionService.sendSignal("resume");
                    adapter.notifyDataSetChanged();
                } else {
                    button.setText(R.string.endBreak);
                    AlarmServerConnectionService.sendSignal("pause");
                    BreakConnectionService.startPause();
                }
                pause = !pause;
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.recViewPause);
        adapter = new NewsListAdapter(changes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        context = getContext();
        return root;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(context.getApplicationContext(), PatientDetailsActiviy.class);
        intent.putExtra("id", changes.get(position).getBed());
        for (Patient p : HomeFragment.patients) {
            if (p.getId() == changes.get(position).getBed()) {
                intent.putExtra("name", p.getName());
            }
        }
        startActivity(intent);
    }
}
