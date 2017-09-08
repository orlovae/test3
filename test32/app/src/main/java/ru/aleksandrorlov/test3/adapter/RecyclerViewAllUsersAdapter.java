package ru.aleksandrorlov.test3.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ru.aleksandrorlov.test3.R;
import ru.aleksandrorlov.test3.data.Contract;

/**
 * Created by alex on 06.09.17.
 */

public class RecyclerViewAllUsersAdapter extends RecyclerView.Adapter<RecyclerViewAllUsersAdapter.ViewHolder>{
    private Context context;
    private Cursor dataCursor;
    private int width, height;

    private final float COMPRESSION_PICTURE = 0.2f;

    public RecyclerViewAllUsersAdapter(Context context, Cursor cursor, int width, int height) {
        this.context = context;
        this.dataCursor = cursor;
        this.width = width;
        this.height = height;
    }

    public Cursor swapCursor (Cursor cursor){
        if (dataCursor == cursor){
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item_all_users_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        int idColIndex = dataCursor.getColumnIndex(Contract.User.COLUMN_ID);
        int firstNameColIndex = dataCursor.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
        int lastNameColIndex = dataCursor.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
        int emailColIndex = dataCursor.getColumnIndex(Contract.User.COLUMN_EMAIL);
        int avatarPathColIndex = dataCursor.getColumnIndex(Contract.User.COLUMN_AVATAR_PATH);

        int id = dataCursor.getInt(idColIndex);

        String firstNameFromCursor = dataCursor.getString(firstNameColIndex);
        String lastNameFromCursor = dataCursor.getString(lastNameColIndex);
        String emailFromCursor = dataCursor.getString(emailColIndex);
        String avatarPath = dataCursor.getString(avatarPathColIndex);


        Uri imageUri = null;
        if (avatarPath != null) {
            imageUri = Uri.fromFile(new File(avatarPath));
        }

        Picasso.with(context)
                .load(imageUri)
                .resize((int)Math.round(width * COMPRESSION_PICTURE),
                        (int)Math.round(height * COMPRESSION_PICTURE))
                .placeholder(R.drawable.progress_animation)
                .into(holder.imageViewAvatar);

//        holder.imageViewAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewHamsterActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("title", firstNameFromCursor);
//                intent.putExtra("description", lastNameFromCursor);
//                intent.putExtra("imagePath", avatarPath);
//                intent.putExtra("likeHamster", likeHamsterFromCursor);
//                context.startActivity(intent);
//            }
//        });

        holder.textViewFirstName.setText(firstNameFromCursor);
        holder.textViewLsatName.setText(lastNameFromCursor);
        holder.textViewEmail.setText(emailFromCursor);

//        holder.checkBoxLikeHamster.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.checkBoxLikeHamster.isChecked()){
//                    holder.checkBoxLikeHamster.setChecked(true);
//                } else {
//                    holder.checkBoxLikeHamster.setChecked(false);
//                }
//                selectLikeHamsterToHamsterTable(id, likeHamsterFromCursor);
//            }
//        });
    }

//    private void selectLikeHamsterToHamsterTable(int id, boolean likeHamster){
//        int intLikeHamster = castBooleanToInt(likeHamster);
//        ContentValues cv = new ContentValues();
//        cv.put(Contract.Hamster.COLUMN_FAVORITE, intLikeHamster);
//
//        String selection = Contract.Hamster.COLUMN_ID + " = ?";
//        String[] selectionArgs = {Integer.toString(id)};
//
//        context.getContentResolver().update(Contract.Hamster.CONTENT_URI, cv, selection,
//                selectionArgs);
//    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewFirstName;
        TextView textViewLsatName;
        TextView textViewEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView){
            imageViewAvatar = (ImageView)itemView.findViewById(R.id.image_view_avatar);
            textViewFirstName = (TextView)itemView.findViewById(R.id.text_view_first_name);
            textViewLsatName = (TextView) itemView.findViewById(R.id.text_view_last_name);
            textViewEmail = (TextView) itemView.findViewById(R.id.text_view_email);
        }
    }
}
