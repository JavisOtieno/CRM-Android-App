package com.example.crm;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.crm.utils.EncryptedPrefsUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private LinearLayout calendarLayout;
    private LinearLayout contactsLayout;
//    private LinearLayout contactsTestLayout;
    private LinearLayout locationsLayout;
    private LinearLayout callsLayout;
    private LinearLayout meetingsLayout;
    private LinearLayout visitsLayout;
    private LinearLayout tasksLayout;
    private LinearLayout logoutLayout;
    private LinearLayout profileLayout;
//    private LinearLayout statsLayout;
    private LinearLayout leadsLayout;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more, container, false);

        calendarLayout = view.findViewById(R.id.calendarLayout);
//        contactsTestLayout = view.findViewById(R.id.contactsTestLayout);
        locationsLayout = view.findViewById(R.id.locationsLayout);
        contactsLayout = view.findViewById(R.id.contactsLayout);
        callsLayout = view.findViewById(R.id.callsLayout);
        meetingsLayout = view.findViewById(R.id.meetingsLayout);
        visitsLayout = view.findViewById(R.id.visitsLayout);
        tasksLayout = view.findViewById(R.id.tasksLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);
        profileLayout = view.findViewById(R.id.profileLayout);
//        statsLayout = view.findViewById(R.id.statsLayout);
        leadsLayout = view.findViewById(R.id.leadsLayout);


        calendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), CalendarActivity.class);
                startActivity(intent);

            }
        });

        locationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), LocationsActivity.class);
                startActivity(intent);

            }
        });

//        statsLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getContext(), StatsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        leadsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), LeadsActivity.class);
                startActivity(intent);

            }
        });

        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ContactsActivity.class);
                startActivity(intent);

            }
        });

        callsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), CallsActivity.class);
                startActivity(intent);

            }
        });

        meetingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MeetingsActivity.class);
                startActivity(intent);

            }
        });

        visitsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), VisitsActivity.class);
                startActivity(intent);

            }
        });

        tasksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), TasksActivity.class);
                startActivity(intent);

            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                startActivity(intent);

                EncryptedPrefsUtil.saveString("userId", "0");
                EncryptedPrefsUtil.saveString("authToken", "");
                EncryptedPrefsUtil.saveString("email", "");
                EncryptedPrefsUtil.saveString("password","");
                Intent intent = new Intent(getContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);

            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}