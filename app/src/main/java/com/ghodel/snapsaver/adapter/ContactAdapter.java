package com.ghodel.snapsaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.helper.DBHelper;
import com.ghodel.snapsaver.model.ContactModel;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<ContactModel> listDb;
    private DBHelper dbHelper;

    public ContactAdapter(Context context, ArrayList<ContactModel> listDb) {
        this.context = context;
        this.listDb = listDb;

    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        final ContactModel contactModel = listDb.get(position);

        holder.tvPhone.setText("To : " + contactModel.getPhone());
        holder.tvMessage.setText("Message : " + "\n" + contactModel.getMessage());

        holder.imgResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappGo = new Intent(Intent.ACTION_VIEW);
                whatsappGo.setData(Uri.parse("whatsapp://send?phone=" + contactModel.getPhone() + "&text=" + contactModel.getMessage()));
                context.startActivity(whatsappGo);
            }
        });

        holder.imgHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(context);
                dbHelper.deleteItem(contactModel.getPhone());
                delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDb.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        LinearLayout bgCardText;
        TextView tvPhone, tvMessage;
        ImageView imgHapus, imgResend;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            bgCardText = itemView.findViewById(R.id.bg_text);
            tvMessage = itemView.findViewById(R.id.tv_msg);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            imgHapus = itemView.findViewById(R.id.img_hapus);
            imgResend = itemView.findViewById(R.id.img_resend);

        }
    }

    private void delete(int position){
        listDb.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listDb.size());
    }
}
