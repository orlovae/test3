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

import static ru.aleksandrorlov.test3.MainActivity.NAME_BUTTON;

/**
 * Created by alex on 06.09.17.
 */

public class EditUserFragment extends Fragment {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextAvatarUrl;
    private Button button;

    private String nameButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nameButton = getArguments().getString(NAME_BUTTON);
        super.onCreate(savedInstanceState);
    }

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
        button = (Button)view.findViewById(R.id.button_edit);

        setNameButton(view);
    }

    private void setNameButton(View view) {
        if (nameButton.equals(getResources().getString(R.string.button_add))) {
            button.setText(nameButton);
        } else {
            button.setText(getResources().getString(R.string.button_edit));
        }
    }
}
