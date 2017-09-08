package ru.aleksandrorlov.test3;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.aleksandrorlov.test3.fragment.EditUserFragment;
import ru.aleksandrorlov.test3.fragment.ViewUsersFragment;

public class MainActivity extends AppCompatActivity {
    ViewUsersFragment viewUsersFragment;
    EditUserFragment editUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();

        initFragment();
    }

    private void initFragment() {
        viewUsersFragment = new ViewUsersFragment();
        editUserFragment = new EditUserFragment();

        FragmentTransaction fT = getSupportFragmentManager().beginTransaction();
        fT.add(R.id.container, viewUsersFragment);
        fT.commit();
    }

    private void initFAB() {
        FloatingActionButton fab =  (FloatingActionButton)findViewById(R.id.fab);
    }
}
