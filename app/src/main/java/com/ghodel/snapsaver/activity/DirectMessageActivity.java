package com.ghodel.snapsaver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.adapter.ContactAdapter;
import com.ghodel.snapsaver.helper.DBHelper;
import com.ghodel.snapsaver.model.ContactModel;
import com.ghodel.snapsaver.service.PhoneClipBoard;
import com.ghodel.snapsaver.utils.MainUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class DirectMessageActivity extends BaseActivity {

    private CountryCodePicker ccp;
    private EditText edtPhone, edtMessage;
    private Button btnSending;
    private RecyclerView rvContact;

    private DBHelper dbHelper;
    private ArrayList<ContactModel> listDb = new ArrayList<>();
    private ContactAdapter contactAdapter;

    private static final String TAG = DirectMessageActivity.class.getSimpleName();

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

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        assert clipboard != null;
        if ((clipboard.hasPrimaryClip())) {
            if ((clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                final ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String paste = item.getText().toString();
                edtPhone.setText(paste);
            }
        }

        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i(TAG, "ads error" + loadAdError.getMessage());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "ads load");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i(TAG, "ads clicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
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
                        whatsappGo.setData(Uri.parse("whatsapp://send?phone=" + ccp.getNumber() + "&text=" + messageText));
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