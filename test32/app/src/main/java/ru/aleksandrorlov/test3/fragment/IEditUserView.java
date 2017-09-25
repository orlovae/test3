package ru.aleksandrorlov.test3.fragment;

import android.widget.EditText;

import ru.aleksandrorlov.test3.model.User;

/**
 * Created by alex on 23.09.17.
 */

public interface IEditUserView {
    void setNameButton(String nameButton);
    void showToast(String message);
    void setFocus(EditText editText);

}
