package ru.aleksandrorlov.test3.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.data.Contract;

import static ru.aleksandrorlov.test3.MainActivity.NAME_BUTTON;
import static ru.aleksandrorlov.test3.fragment.ViewUsersFragment.SEND_USER;

/**
 * Created by alex on 06.09.17.
 */

public class EditUserFragment extends Fragment {
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextAvatarUrl;
    private Button button;

    private String nameButton;
    private int idServer;

    private String firstName, lastName, email, avatar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nameButton = getArguments().getString(NAME_BUTTON);

        idServer = getArguments().getInt(SEND_USER, -1);
        if (idServer != -1) {
            getData();
        }

        super.onCreate(savedInstanceState);
    }

    private void getData() {
        String selection = Contract.User.COLUMN_ID_SERVER + " LIKE ?";;
        String[] selectionArgs = {"%" + idServer + "%"};

        Cursor data = getActivity().getContentResolver().query(Contract.User.CONTENT_URI, null,
                selection, selectionArgs, null);

        try {
            if (data != null && data.moveToFirst()) {
                int firstNameColIndex = data.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
                int lastNameColIndex = data.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
                int emailCollIndex = data.getColumnIndex(Contract.User.COLUMN_EMAIL);
                int avatarURLColIndex = data.getColumnIndex(Contract.User.COLUMN_AVATAR_URL);

                do {
                    firstName = data.getString(firstNameColIndex);
                    lastName = data.getString(lastNameColIndex);
                    email = data.getString(emailCollIndex);
                    avatar = data.getString(avatarURLColIndex);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) data.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.fragment_edit_user, container, false);

        initViews(view);

        if (idServer != -1) {
            setEditTextView(view);
        }

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
        if (nameButton != null && nameButton.equals(getResources().getString(R.string.button_add))) {
            button.setText(nameButton);
        } else {
            button.setText(getResources().getString(R.string.button_edit));
        }
    }

    private void setEditTextView (View view) {
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextEmail.setText(email);
        editTextAvatarUrl.setText(avatar);
    }
}
