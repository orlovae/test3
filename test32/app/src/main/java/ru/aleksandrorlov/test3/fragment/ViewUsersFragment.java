package ru.aleksandrorlov.test3.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.adapter.RecyclerViewAllUsersAdapter;
import ru.aleksandrorlov.test3.data.Contract;

/**
 * Created by alex on 06.09.17.
 */

public class ViewUsersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerViewAllUsers;
    private RecyclerViewAllUsersAdapter adapter;
    private int height, width;

    private int LOADER_ID = 2;
    public static final String SEND_USER = "send User";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.heightPixels;
        height = metrics.widthPixels;

        boolean isLandscape = width > height;

        if (!isLandscape) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        initViews(view);
        initRecyclerView();

        return view;
    }

    private void initViews(View view) {
        recyclerViewAllUsers = (RecyclerView)view.findViewById(R.id.recycler_view_all_users);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewAllUsersAdapter(getActivity(), null, width, height);
        recyclerViewAllUsers.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerViewAllUsers.setAdapter(adapter);
        recyclerViewAllUsers.setLayoutManager(layoutManager);

        adapter.SetOnItemClickListener(new RecyclerViewAllUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idServer) {
                Bundle arg = new Bundle();
                arg.putInt(SEND_USER, idServer);
                EditUserFragment editUserFragment = new EditUserFragment();
                editUserFragment.setArguments(arg);
                FragmentManager fM = getFragmentManager();
                fM.beginTransaction()
                        .replace(R.id.container, editUserFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                Contract.User.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
