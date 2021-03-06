package ru.aleksandrorlov.test3.presenter.edituserfragment;

import android.widget.EditText;

import java.util.List;

import ru.aleksandrorlov.test3.model.User;
import ru.aleksandrorlov.test3.presenter.IBasePresenter;

/**
 * Created by alex on 23.09.17.
 */

public interface IEditUser extends IBasePresenter {
    User getData(int idServer);
    void setIdServer(int idServer);
    void buttonOnClick(List<EditText> editTextList, EditText editTextAvatarUrl);
}
