package ru.aleksandrorlov.test3.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.aleksandrorlov.test3.R;

/**
 * Created by alex on 06.09.17.
 */

public class EditUserFragment extends Fragment {
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextAvatarUrl;
    Button buttonEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.fragment_edit_user, container, false);

        return view;
    }
}
