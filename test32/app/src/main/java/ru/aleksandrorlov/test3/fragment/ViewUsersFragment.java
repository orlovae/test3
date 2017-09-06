package ru.aleksandrorlov.test3.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.adapter.RecyclerViewAllUsersAdapter;

/**
 * Created by alex on 06.09.17.
 */

public class ViewUsersFragment extends Fragment {
    private RecyclerView recyclerViewAllUsers;
    private RecyclerViewAllUsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view){

    }

}
