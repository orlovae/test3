package ru.aleksandrorlov.test3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.aleksandrorlov.test3.adapter.RecyclerViewAllUsersAdapter;
import ru.aleksandrorlov.test3.fragment.EditUserFragment;

import static ru.aleksandrorlov.test3.fragment.ViewUsersFragment.SEND_USER;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
        RecyclerViewAllUsersAdapter.OnItemClickListener {
    private FloatingActionButton fab;

    private String nameButton;
    private int idServer = -1;
    boolean withEditUser = true;

    public static final String NAME_BUTTON = "nameButton";
    public static final int EDIT_BUTTON = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();

        FABBehavior();

        if (savedInstanceState != null) {
            idServer = savedInstanceState.getInt(SEND_USER);
        }

        withEditUser = (findViewById(R.id.container) != null);

        if (withEditUser) {
            showEditUser(idServer);
        }
    }

    private void showEditUser(int idServer) {
        Log.d("MainActivity", "withEditUser = " + withEditUser);
        if (withEditUser) {
            EditUserFragment editUserFragment = (EditUserFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            if (editUserFragment == null || editUserFragment.getIdServer() != idServer) {
                editUserFragment = EditUserFragment.newInstanse(idServer);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, editUserFragment)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra(SEND_USER, idServer);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEND_USER, idServer);
    }

    private void initFAB() {
        fab =  (FloatingActionButton)findViewById(R.id.fab);
    }

    private void FABBehavior() {
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showEditUser(EDIT_BUTTON);
                break;
        }
    }

    @Override
    public void onItemClick(int idServer) {
        this.idServer = idServer;

        showEditUser(idServer);
        Log.d("MainActivity", "idServer = " + idServer);
    }
}
