package com.example.crm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crm.adapters.AdapterListContacts;
import com.example.crm.models.Contact;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment {

    private static final String ARG_CONTACTS = "contacts";

    private List<Contact> contactList;
    private AdapterListContacts mAdapter;
    private ArrayList<Contact> contacts;

    public static ContactsFragment newInstance(List<Contact> contacts) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACTS, new ArrayList<>(contacts));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

//        if (getArguments() != null) {
//            contactList = (List<Contact>) getArguments().getSerializable(ARG_CONTACTS);
//        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            contacts = (ArrayList<Contact>) arguments.getSerializable("contacts");

//            Toasts.toastIconError(getContext(),contacts.size()+" contacts");

            if (contacts != null) {
                for (Contact contact : contacts) {
                    Log.d("ContactsFragment", "Name: " + contact.name+ ", Phone: " + contact.phone);
                }
            }
        }

        TextView emptyView = view.findViewById(R.id.empty_view);

        if (contacts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        mAdapter = new AdapterListContacts(contacts);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new AdapterListContacts.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Contact obj, int position) {

                Contact contact = contacts.get(position);

                Intent intent = new Intent(getContext(),
                        IndividualContactActivity.class);
                intent.putExtra("contactId",contact.id);
                startActivity(intent);



            }
        });

        return view;
    }
}

