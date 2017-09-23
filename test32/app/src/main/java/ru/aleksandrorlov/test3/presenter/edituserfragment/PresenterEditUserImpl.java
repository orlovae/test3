package ru.aleksandrorlov.test3.presenter.edituserfragment;

import android.content.Context;
import android.database.Cursor;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.data.Contract;
import ru.aleksandrorlov.test3.fragment.IEditUserView;
import ru.aleksandrorlov.test3.model.User;

/**
 * Created by alex on 23.09.17.
 */

public class PresenterEditUserImpl implements IEditUser {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private IEditUserView view;
    private Context context;

    private User user;
    private int idServer;

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
    public void detachView() {
        view = null;
        context = null;
    }

    @Override
    public void destroy() {
        user = null;
    }
}
