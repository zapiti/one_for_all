package com.dev.nathan.testtcc.panicbutton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.EditText;


public class InsertMessageContentDialog {
    private AlertDialog dialog;
    private EditText messageText;
    private Context context;
    
    public InsertMessageContentDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        messageText = new EditText(context);
        messageText.setHint("SOS: EU preciso da sua ajuda , Por favor me salve!");
        
        dialog = builder.setTitle("Inserir um texto a ser enviado")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();                   
                    }
                })
                .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMessage();
                        dialog.dismiss();
                    }
                })
                .setView(messageText)
                .create();
    }

    private void saveMessage() {
        String text = messageText.getText().toString();
        if (!text.equals("") && text != null) {
            SharedPreferences settings = context.getSharedPreferences(MainPanicButtonActivity.SETTINGS_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("mensagem de texto", text);
            editor.commit();
        }

    }

    public void show(){
        dialog.show();
    }
}
