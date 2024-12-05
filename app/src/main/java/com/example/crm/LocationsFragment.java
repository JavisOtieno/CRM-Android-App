package com.example.crm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crm.adapters.AdapterListContacts;
import com.example.crm.adapters.AdapterListLocations;
import com.example.crm.models.Contact;
import com.example.crm.models.Location;

import java.util.ArrayList;
import java.util.List;


public class LocationsFragment extends Fragment {

    private static final String ARG_CONTACTS = "locations";

    private List<Location> locationList;
    private AdapterListLocations mAdapter;

    public static LocationsFragment newInstance(List<Location> locations) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACTS, new ArrayList<>(locations));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            locationList = (List<Location>) getArguments().getSerializable(ARG_CONTACTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        TextView emptyView = view.findViewById(R.id.empty_view);

        if (locationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AdapterListLocations(locationList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterListLocations.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Location obj, int position) {

                Location location = locationList.get(position);

                Intent intent = new Intent(getContext(),
                        IndividualLocationActivity.class);
                intent.putExtra("locationId",location.id);
                startActivity(intent);



            }
        });

        return view;
    }
}
