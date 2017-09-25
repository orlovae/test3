package ru.aleksandrorlov.test3.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.presenter.edituserfragment.IEditUser;
import ru.aleksandrorlov.test3.presenter.edituserfragment.PresenterEditUserImpl;

import static ru.aleksandrorlov.test3.fragment.ViewUsersFragment.SEND_USER;

/**
 * Created by alex on 06.09.17.
 */

public class EditUserFragment extends Fragment implements View.OnClickListener, IEditUserView {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextAvatarUrl;
    private Button button;

    private String nameButton;
    private int idServer;

    private User user;

    private List<EditText> editTextList;
    private EditText tmpEditText;

    private IEditUser presenter;

    public static EditUserFragment newInstanse(int idServer) {
        EditUserFragment editUserFragment = new EditUserFragment();
        Bundle args = new Bundle();
        args.putInt(SEND_USER, idServer);
        editUserFragment.setArguments(args);
        return editUserFragment;
    }

    public int getIdServer() {
        return getArguments().getInt(SEND_USER, -1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new PresenterEditUserImpl(this, context);

    }

    @Override
    public void setNameButton(String nameButton) {
        this.nameButton = nameButton;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        idServer = getIdServer();

        presenter.setIdServer(idServer);

        user = presenter.getData(idServer);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_edit_user, container, false);

        initViews(view);

        if (idServer != -1) {
            setView();
        }

        buttonBehavior();

        return view;
    }

    private void initViews(View view) {
        editTextFirstName = (EditText) view.findViewById(R.id.edit_text_first_name);
        editTextLastName = (EditText) view.findViewById(R.id.edit_text_last_name);
        editTextEmail = (EditText) view.findViewById(R.id.edit_text_email);
        editTextAvatarUrl = (EditText) view.findViewById(R.id.edit_text_avatar_url);

        if (idServer != -1) {
            editTextAvatarUrl.setVisibility(View.VISIBLE);
        }

        button = (Button) view.findViewById(R.id.button_edit);
        button.setText(nameButton);

        editTextList = new ArrayList<EditText>();
        editTextList.add(editTextFirstName);
        editTextList.add(editTextLastName);
        editTextList.add(editTextEmail);
        //Отключено, так как в т.з. нет проверки валидности поля url avatar
        //editTextList.add(editTextAvatarUrl);
    }

    private void setView() {
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        editTextEmail.setText(user.getEmail());
        editTextAvatarUrl.setText(user.getAvatarUrl());
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit:
                presenter.buttonOnClick(editTextList, editTextAvatarUrl);
                break;
        }
    }

    @Override
    public void setFocus(EditText editText) {
        editText.requestFocus();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
