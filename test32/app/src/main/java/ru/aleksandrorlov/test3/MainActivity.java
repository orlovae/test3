package ru.aleksandrorlov.test3;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.aleksandrorlov.test3.fragment.EditUserFragment;
import ru.aleksandrorlov.test3.fragment.ViewUsersFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewUsersFragment viewUsersFragment;
    private EditUserFragment editUserFragment;
    private FragmentTransaction fT;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();
        FABBehavior();
        initFragment();
    }
    private void initFAB() {
        fab =  (FloatingActionButton)findViewById(R.id.fab);
    }

    private void initFragment() {
        viewUsersFragment = new ViewUsersFragment();
        editUserFragment = new EditUserFragment();

        fT = getSupportFragmentManager().beginTransaction();
        fT.add(R.id.container, viewUsersFragment);
        fT.commit();
    }

    private void FABBehavior() {
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                fT = getSupportFragmentManager().beginTransaction();
                fT.replace(R.id.container, editUserFragment);
                fT.commit();
                break;
        }
    }
}
