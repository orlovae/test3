package ru.aleksandrorlov.test3.fragment;

import android.content.Context;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        idServer = getIdServer();
        Log.d("EditUserFragment onCreate", "idServer = " + idServer);

        if (idServer != -1) {
            getData();
            nameButton = getResources().getString(R.string.button_edit);
        } else {
            nameButton = getResources().getString(R.string.button_add);
        }
        super.onCreate(savedInstanceState);
    }

    private void getData() {
        String selection = Contract.User.COLUMN_ID_SERVER + " LIKE ?";

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
                    if (isEmailValid(editTextEmail.getText().toString())) {
                        if (idServer != -1) {
                            editUserToServer(createUser());
                        } else {
                            addUserToServer(createUser());
                        }
                    } else {
                        editTextEmail.requestFocus();

                        setToast(getResources().getString(R.string.email_no_valid));
                    }

                } else {
                    tmpEditText.requestFocus();

                    setToast(getResources().getString(R.string.field_valid_first) + " "
                            + tmpEditText.getHint().toString() + " "
                            + getResources().getString(R.string.field_valid_last));
                }
                break;
        }
    }

    private boolean invalideData() {
        boolean notNull = false;

        try {
            for (EditText item : editTextList
                    ) {
                boolean isItem = item.getText().toString().replaceAll(" ", "").length() != 0;
                //удаляет пробелы, что бы пользователь не мог зарегестировать имя из одного пробела
                if (isItem) {
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void editUserToServer(RequestBody requestBody) {
        ApiUser apiUser = ApiController.API();
        Call<ResponseBody> call = apiUser.editUser(idServer, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    setToast(getResources().getString(R.string.user_edit));
                }
                Log.d("editUserToServer", response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setToast(getResources().getString(R.string.user_no_edit));
            }
        });

    }

    private void addUserToServer(RequestBody requestBody) {
        ApiUser apiUser = ApiController.API();
        Call<ResponseBody> call = apiUser.setUser(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    setToast(getResources().getString(R.string.user_add));
                }
                Log.d("addUserToServer", response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setToast(getResources().getString(R.string.user_no_add));
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

    private void setToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
