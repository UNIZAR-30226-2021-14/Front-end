package eina.unizar.freshtech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VerificacionActivity extends AppCompatActivity  {

    private static final int ACTIVITY_INICIARSESION=0;

    private EditText pin;
    private TextInputLayout huecoPin;
    private String codigo;

    String sEmail,sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion);

        pin = (EditText) findViewById(R.id.codigoVerificacion);
        huecoPin = (TextInputLayout) findViewById(R.id.huecoCodigoVerificacion);

        codigo = GeneradorContraseñas.getPassword(false, false, true, false, 6);

        // Sender email credential
        sEmail = "freshtechverify@gmail.com";
        sPassword = "123FreshTech";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        // Initialize session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });
        try {
            // Initialize email content
            Message message = new MimeMessage(session);
            // Sender email
            message.setFrom(new InternetAddress(sEmail));
            // Recipient email
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Preferencias.cargarCorreo(this)));
            Log.d("Correo de verificacion", "Mensaje => " + Preferencias.cargarCorreo(this));
            // Email subject
            message.setSubject("Verificación de inicio de sesión");
            // Email message
            message.setText("Este es tu código de seguridad de FreshTech:\n\n" + codigo + "\n\nSi no has solicitado este código " +
                    "al activar la verificación de inicio de sesión, dirígete a la pantalla de Configuración y cambia la contraseña inmediatamente. " +
                    "Si necesitas asistencia, ponte en contacto con nosotros a través de FreshTech@gmail.com.");

            // Send email
            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void onClickAtras(View view) {
        finish();
    }

    public void onClickValidar(View view) {
        if(pin.getText().toString().equals(codigo)) {
            Intent i = new Intent(this, PrincipalActivity.class);
            startActivityForResult(i, ACTIVITY_INICIARSESION);
            finish();
        } else {
            huecoPin.setError("Código inválido");
        }
    }

    public void onClickReenviarCodigo(View view) {
        codigo = GeneradorContraseñas.getPassword(false, false, true, false, 6);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        // Initialize session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });
        try {
            // Initialize email content
            Message message = new MimeMessage(session);
            // Sender email
            message.setFrom(new InternetAddress(sEmail));
            // Recipient email
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Preferencias.cargarCorreo(this)));
            Log.d("Correo de verificacion", "Mensaje => " + Preferencias.cargarCorreo(this));
            // Email subject
            message.setSubject("Verificación de inicio de sesión");
            // Email message
            message.setText("Este es tu código de seguridad de FreshTech:\n\n" + codigo + "\n\nSi no has solicitado este código " +
                    "al activar la verificación de inicio de sesión, dirígete a la pantalla de Configuración y cambia la contraseña inmediatamente. " +
                    "Si necesitas asistencia, ponte en contacto con nosotros a través de freshtechverify@gmail.com.");

            // Send email
            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Se ha enviado un nuevo código a su correo electrónico",Toast.LENGTH_SHORT).show();
    }

    private class SendMail extends AsyncTask<Message,String,String> {
        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
