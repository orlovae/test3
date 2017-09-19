package ru.aleksandrorlov.test3;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import ru.aleksandrorlov.test3.fragment.EditUserFragment;

import static ru.aleksandrorlov.test3.MainActivity.NAME_BUTTON;
import static ru.aleksandrorlov.test3.fragment.ViewUsersFragment.SEND_USER;

/**
 * Created by alex on 17.09.17.
 */

public class EditUserActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("EditUserActivity", "onCreate");

        if (savedInstanceState == null) {
            EditUserFragment editUserFragment = EditUserFragment.newInstanse(
                    getIntent().getIntExtra(SEND_USER, -1));
            Log.d("EditUserActivity", "id Server = " +getIntent().getIntExtra(SEND_USER, -1));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, editUserFragment)
                    .commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean isLarge = (newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_NORMAL;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isLarge) {
            finish();
        }
    }
}
