package ru.aleksandrorlov.test3.presenter.edituserfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.controllers.ApiController;
import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.fragment.IEditUserView;
import ru.aleksandrorlov.test3.model.RequestBody;
import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.rest.ApiUser;

/**
 * Created by alex on 23.09.17.
 */

public class PresenterEditUserImpl implements IEditUser {
    private IEditUserView view;
    private Context context;

    private User user;
    private int idServer;
    private EditText tmpEditText;

    public PresenterEditUserImpl(IEditUserView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void setIdServer(int idServer) {
        this.idServer = idServer;
        String nameButton;

        if (idServer != -1) {
            nameButton = context.getString(R.string.button_edit);
        } else {
            nameButton = context.getString(R.string.button_add);
        }

        view.setNameButton(nameButton);
    }

    @Override
    public User getData(int idServer) {
        String selection = Contract.User.COLUMN_ID_SERVER + " LIKE ?";

        String[] selectionArgs = {"%" + idServer + "%"};

        Cursor data = context.getContentResolver().query(Contract.User.CONTENT_URI, null,
                selection, selectionArgs, null);

        try {
            if (data != null && data.moveToFirst()) {
                int idServerColIndex = data.getColumnIndex(Contract.User.COLUMN_ID_SERVER);
                int firstNameColIndex = data.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
                int lastNameColIndex = data.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
                int emailCollIndex = data.getColumnIndex(Contract.User.COLUMN_EMAIL);
                int avatarURLColIndex = data.getColumnIndex(Contract.User.COLUMN_AVATAR_URL);

                do {
                    int id = data.getInt(idServerColIndex);
                    String firstName = data.getString(firstNameColIndex);
                    String lastName = data.getString(lastNameColIndex);
                    String email = data.getString(emailCollIndex);
                    String avatar = data.getString(avatarURLColIndex);

                    user = new User(id, firstName, lastName, email, avatar);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) data.close();
        }
        return user;
    }

    @Override
    public void buttonOnClick(List<EditText> editTextList, EditText editTextAvatarUrl) {
        if (invalideData(editTextList)) {
            if (isEmailValid(editTextList.get(2).getText().toString())) {
                if (idServer != -1) {
                    editUserToServer(createUser(editTextList, editTextAvatarUrl));

                    ContentValues cv = createCV();

                    String selection = Contract.User.COLUMN_ID_SERVER + " = ?";
                    String[] selectionArgs = {Integer.toString(idServer)};

                    context.getContentResolver().update(Contract.User.CONTENT_URI, cv, selection,
                            selectionArgs);

                } else {
                    addUserToServer(createUser(editTextList, editTextAvatarUrl));

                    ContentValues cv = createCV();

                    context.getContentResolver().insert(Contract.User.CONTENT_URI, cv);
                }
            } else {
                view.setFocus(editTextList.get(2));
                view.showToast(context.getResources().getString(R.string.email_no_valid));
            }

        } else {
            view.setFocus(tmpEditText);
            view.showToast(context.getResources().getString(R.string.field_valid_first)
                    + " "
                    + tmpEditText.getHint().toString() + " "
                    + context.getResources().getString(R.string.field_valid_last));
        }
    }

    private ContentValues createCV() {
        ContentValues cv = new ContentValues();
        cv.put(Contract.User.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(Contract.User.COLUMN_LAST_NAME, user.getLastName());
        cv.put(Contract.User.COLUMN_EMAIL, user.getEmail());
        cv.put(Contract.User.COLUMN_AVATAR_URL, user.getAvatarUrl());
        return cv;
    }

    private boolean invalideData(List<EditText> editTextList) {
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

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void editUserToServer(RequestBody requestBody) {
        ApiUser apiUser = ApiController.API();
        Call<ResponseBody> call = apiUser.editUser(idServer, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.showToast(context.getResources().getString(R.string.user_edit));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showToast(context.getResources().getString(R.string.user_no_edit));
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
                    view.showToast(context.getResources().getString(R.string.user_add));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showToast(context.getResources().getString(R.string.user_no_add));
            }
        });
    }

    private RequestBody createUser(List<EditText> editTextList, EditText editTextAvatarUrl) {
        this.user = new User(
                editTextList.get(0).getText().toString(),
                editTextList.get(1).getText().toString(),
                editTextList.get(2).getText().toString(),
                editTextAvatarUrl.getText().toString()
        );
        return new RequestBody(user);
    }

    @Override
    public void detachView() {
        view = null;
        context = null;
    }

    @Override
    public void destroy() {
        user = null;
        tmpEditText = null;
    }
}
