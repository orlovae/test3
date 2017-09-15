package ru.aleksandrorlov.test3;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.aleksandrorlov.test3.fragment.EditUserFragment;
import ru.aleksandrorlov.test3.fragment.ViewUsersFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewUsersFragment viewUsersFragment;
    private EditUserFragment editUserFragment;
    private FragmentManager fm;
    private FloatingActionButton fab;

    public static final String NAME_BUTTON = "nameButton";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        initFAB();
        FABBehavior();
        initFragment();
    }
    private void initFAB() {
        fab =  (FloatingActionButton)findViewById(R.id.fab);
    }

    private void initFragment() {
        viewUsersFragment = new ViewUsersFragment();

        fm.beginTransaction()
                .add(R.id.container, viewUsersFragment)
                .addToBackStack(null)
                .commit();
    }

    private void FABBehavior() {
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                Bundle arg = new Bundle();
                arg.putString(NAME_BUTTON, getResources().getString(R.string.button_add));

                editUserFragment = new EditUserFragment();
                editUserFragment.setArguments(arg);
                fm.beginTransaction()
                        .replace(R.id.container, editUserFragment)
                        .addToBackStack(null)
                        .commit();
                FABShow(false);
                break;
        }
    }

    public void FABShow(boolean isShow) {
        if (isShow) {
            fab.show();
        } else {
            fab.hide();
        }
    }
}
