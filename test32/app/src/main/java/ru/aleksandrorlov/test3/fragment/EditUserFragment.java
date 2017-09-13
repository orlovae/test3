package ru.aleksandrorlov.test3.fragment;

import android.database.Cursor;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.controllers.ApiController;
import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.model.RequestBody;
import ru.aleksandrorlov.test3.rest.ApiUser;

import static ru.aleksandrorlov.test3.MainActivity.NAME_BUTTON;
import static ru.aleksandrorlov.test3.fragment.ViewUsersFragment.SEND_USER;

/**
 * Created by alex on 06.09.17.
 */

public class EditUserFragment extends Fragment implements View.OnClickListener {
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextAvatarUrl;
    private Button button;

    private String nameButton;
    private int idServer;

    private String firstName, lastName, email, avatar;

    private List<EditText> editTextList;
    private EditText tmpEditText;

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
        String selection = Contract.User.COLUMN_ID_SERVER + " LIKE ?";
        ;
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
        View view = (View) inflater.inflate(R.layout.fragment_edit_user, container, false);

        initViews(view);

        if (idServer != -1) {
            setEditTextView();
        }

        buttonBehavior();

        return view;
    }

    private void initViews(View view) {
        editTextFirstName = (EditText) view.findViewById(R.id.edit_text_first_name);
        editTextLastName = (EditText) view.findViewById(R.id.edit_text_last_name);
        editTextEmail = (EditText) view.findViewById(R.id.edit_text_email);
        editTextAvatarUrl = (EditText) view.findViewById(R.id.edit_text_avatar_url);
        button = (Button) view.findViewById(R.id.button_edit);

        setNameButton();

        editTextList = new ArrayList<EditText>();
        editTextList.add(editTextFirstName);
        editTextList.add(editTextLastName);
        editTextList.add(editTextEmail);
        //Отключено, так как в т.з. нет проверки валидности поля url avatar
        //editTextList.add(editTextAvatarUrl);
    }

    private void setNameButton() {
        if (nameButton != null && nameButton.equals(getResources().getString(R.string.button_add))) {
            button.setText(nameButton);
        } else {
            button.setText(getResources().getString(R.string.button_edit));
        }
    }

    private void setEditTextView() {
        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
        editTextEmail.setText(email);
        editTextAvatarUrl.setText(avatar);
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit:
                if (invalideData()) {
                    addUserToServer(createUser());
                } else {
                    tmpEditText.requestFocus();

                    String fieldValidFirst = getResources().getString(R.string.field_valid_first);
                    String fieldValidLast = getResources().getString(R.string.field_valid_last);

                    String field = fieldValidFirst + " " + tmpEditText.getHint().toString() + " "
                            + fieldValidLast;
                    Toast.makeText(getActivity(), field, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean invalideData() {
        boolean notNull = false;

        try {
            for (EditText item : editTextList
                    ) {
                //удаляет пробелы, что бы пользователь не мог зарегестировать имя из одного пробела
                if (item.getText().toString().replaceAll(" ", "").length() != 0) {
                    notNull = true;
                } else {
                    tmpEditText = item;
                    notNull = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notNull;
    }

    private void addUserToServer(RequestBody requestBody) {
        ApiUser apiUser = ApiController.getApi();
        Call<ResponseBody> call = apiUser.setUser(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response", response.toString());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private RequestBody createUser() {
        User user = new User(
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextAvatarUrl.getText().toString()
        );
        return new RequestBody(user);
    }
}
