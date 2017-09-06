package ru.aleksandrorlov.test3;

import android.app.FragmentTransaction;
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

        initFragment();
    }

    private void initFragment() {
        viewUsersFragment = new ViewUsersFragment();
        editUserFragment = new EditUserFragment();

        FragmentTransaction fT = getFragmentManager().beginTransaction();
        fT.add(R.id.container, viewUsersFragment);
        fT.commit();
    }
}
