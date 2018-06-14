package com.dev.nathan.testtcc.panicbutton;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.dev.nathan.testtcc.R;
import com.shamanland.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainPanicButtonActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 10;
    public static final String SETTINGS_FILE = "SETTINGS_FILE";
    private static final String MESSAGE_TEXT_KEY =  "messageText";

    private Button btnPanic;
    private FloatingActionButton addContact;
    private ListView listContacts;
    private SlidingUpPanelLayout contactsDrawer;
    private Locator locator;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic);


        Toolbar webViewToolbar = findViewById(R.id.panic_toolbar);
        setSupportActionBar(webViewToolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Botão do Panico");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }else {
            locator = Locator.getLocator(this);
            initGui();
        }

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.SEND_RESPOND_VIA_MESSAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.SEND_SMS}, REQUEST_LOCATION);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.SEND_RESPOND_VIA_MESSAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.SEND_SMS}, REQUEST_LOCATION);
        }



       else if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 2);

        }else {
            locator.requestUpdate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_message) {
            editMessageContent();
        }
        if (id == R.id.action_show_position) {
            showPositionInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (contactsDrawer.isPanelExpanded()){
            contactsDrawer.collapsePanel();
        } else super.onBackPressed();
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione os numeros").setMessage("Selecione a origem para importar números de telefone")
                .setNegativeButton("Contatos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseContact();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Numero de Telefone", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choosePhoneNumber();
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void editMessageContent() {
        new InsertMessageContentDialog(this).show();
    }

    private void showPositionInfo() {
        try {
            new PositionInfoDialog(locator.getLocation(), MainPanicButtonActivity.this).show();
        } catch (Locator.NoPositionProvidersException e) {
            locator.showSettingsAlert();
        }
    }

    private void choosePhoneNumber() {
        new PersonAddNumberDialog(MainPanicButtonActivity.this, listContacts).show();
    }

    private void chooseContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == RESULT_PICK_CONTACT) {

                Uri contractData = data.getData();
                ContactRetriever cr = new ContactRetriever(getApplicationContext(), contractData);
                Person p = cr.getPerson();

                if (p == null) Toast.makeText(this, "Phone number not found!", Toast.LENGTH_SHORT).show();

                else {
                    PersonManager.savePerson(p, getApplicationContext());
                    listContacts.setAdapter(new PersonAdapter(this, PersonManager.getSavedPersons(this)));
                }
            }
        }
    }


    private void initGui() {

        btnPanic = (Button) findViewById(R.id.btn_panic);
        btnPanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Location loc = locator.getLocation();
                    SharedPreferences sp = getSharedPreferences(SETTINGS_FILE, 0);

                    String messageText = sp.getString(MESSAGE_TEXT_KEY, null);

                    if (messageText == null) {
                        messageText = "Precisso de ajuda";
                    }

                    messageText += "\nCOORDENADAS: \nLat: " + loc.getLatitude() + " \nLng: " + loc.getLongitude() + "\n\nLOCALIZAÇÃO: \n" + Locator.getAddressFromLocation(loc, getApplicationContext());
                    //Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();

                    SmsManager smsMan = SmsManager.getDefault();
                    ArrayList<Person> recipients = PersonManager.getSavedPersons(getApplicationContext());
                    for (Person p : recipients){
                        smsMan.sendTextMessage(p.getNumber(), null, messageText, null, null);
                    }

                    Toast.makeText(getApplicationContext(), "Messagem Enviada", Toast.LENGTH_SHORT).show();
                } catch (Locator.NoPositionProvidersException e) {
                   // locator.showSettingsAlert();
                    Toast.makeText(getBaseContext(),"Não foi possível conectar ao gps !Tente novamente",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addContact = (FloatingActionButton) findViewById(R.id.fab_action_add_contact);
        addContact.setImageResource(R.drawable.ic_action_content_add);
        addContact.setSize(FloatingActionButton.SIZE_NORMAL);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });

        listContacts = (ListView) findViewById(R.id.contacts_list);
        contactsDrawer = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        contactsDrawer.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                addContact.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPanelCollapsed(View view) {
                addContact.setVisibility(View.VISIBLE);
            }


            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        listContacts.setAdapter(new PersonAdapter(getApplicationContext(), PersonManager.getSavedPersons(getApplicationContext())));
    }
}
