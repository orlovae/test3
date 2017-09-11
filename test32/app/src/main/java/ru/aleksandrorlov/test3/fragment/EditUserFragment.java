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

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        editTextFirstName = (EditText)view.findViewById(R.id.edit_text_first_name);
        editTextLastName = (EditText)view.findViewById(R.id.edit_text_last_name);
        editTextEmail = (EditText)view.findViewById(R.id.edit_text_email);
        editTextAvatarUrl = (EditText)view.findViewById(R.id.edit_text_avatar_url);
        buttonEdit = (Button)view.findViewById(R.id.button_edit);
    }
}
