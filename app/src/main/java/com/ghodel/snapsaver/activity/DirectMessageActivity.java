package com.ghodel.snapsaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.adapter.ContactAdapter;
import com.ghodel.snapsaver.helper.DBHelper;
import com.ghodel.snapsaver.model.ContactModel;
import com.ghodel.snapsaver.utils.MainUtil;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;

public class DirectMessageActivity extends BaseActivity {

    private CountryCodePicker ccp;
    private EditText edtPhone, edtMessage;
    private Button btnSending;
    private RecyclerView rvContact;

    private DBHelper dbHelper;
    private ArrayList<ContactModel> listDb = new ArrayList<>();
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);
        initView();
        initLogic();
        initListener();
    }

    @Override
    public void initView() {
        ccp = findViewById(R.id.ccp);
        edtPhone = findViewById(R.id.edt_phone);
        edtMessage = findViewById(R.id.edt_message);
        btnSending = findViewById(R.id.btn_sending);
        rvContact = findViewById(R.id.rv_contact);

    }

    @Override
    public void initLogic() {
        getSupportActionBar().setTitle("Direct Message Without Save Number");

        ccp.registerPhoneNumberTextView(edtPhone);
        loadHistoryPhone();
    }

    private void loadHistoryPhone(){
        dbHelper = new DBHelper(this);
        listDb = dbHelper.getContacts();

        if(listDb.size() == 0){
            Toast.makeText(this,"Data Kosong", Toast.LENGTH_SHORT).show();
        } else {
            rvContact.setHasFixedSize(true);
            rvContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvContact.setItemAnimator(new DefaultItemAnimator());
            contactAdapter = new ContactAdapter(getApplicationContext(), listDb);
            rvContact.setAdapter(contactAdapter);
            rvContact.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void initListener() {

        btnSending.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String messageText = edtMessage.getText().toString().trim();

                if(MainUtil.appInstalledOrNot(getApplicationContext(), "com.whatsapp")){
                    try {
                        Intent whatsappGo = new Intent(Intent.ACTION_VIEW);
                        whatsappGo.setData(Uri.parse("whatsapp://send?phone=" + ccp.getFullNumber() + "&text=" + messageText));
                        startActivity(whatsappGo);

                        // menambahkan data ke database
                        if(dbHelper.insertContact(ccp.getFullNumber(), messageText)){
                            Toast.makeText(DirectMessageActivity.this,"Berhasil input", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DirectMessageActivity.this,"Gagal input", Toast.LENGTH_SHORT).show();
                        }

                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "WhatsApp Not Found on this Phone :(" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listDb.clear();
        loadHistoryPhone();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}