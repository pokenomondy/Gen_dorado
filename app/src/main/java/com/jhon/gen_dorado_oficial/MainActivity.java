package com.jhon.gen_dorado_oficial;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText editNumero;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "PhoneAuthActivity";

    public static final String verificacionId = "com.jhon.gen_dorado_oficial.MainActivity";
    String mVerificationId;

    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNumero = findViewById(R.id.editNumero);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //Crear dialogo de inicio
        progressDialog = new ProgressDialog(MainActivity.this); // se inicializa
        dialog = new Dialog(MainActivity.this); //Inicializamos el dialog

        //Desactivamos modo noche en los celualres
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
                progressDialog.dismiss();

                //puede que esto em genere un problema , pero vamos a mirar, ya que apague la función
                //signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Dialog_no_inicio();
                progressDialog.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                progressDialog.dismiss();


                Intent intent = new Intent(MainActivity.this, Verificar_numero.class);
                intent.putExtra(verificacionId, mVerificationId);
                startActivity(intent);
            }
        };

    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUi();
    }

    //BOTON PARA EMPEZAR A VERIFICAR NUMERO Y ENVIAR SMS
    public void accion_numerodetelefono(View view) {
        String phoneNumber = editNumero.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+57" + phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        mAuth.setLanguageCode("es");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    //UPDATE DE INTERFAZ GRAFICA, SI ES USUARIO RECURENTE O NO
    private void updateUi() {
        if (firebaseUser != null) {
            Intent recurrente = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(recurrente);
        }
    }

    //DIALOG CUANDO NO SE PERMITA INICIAR SESIÓN

    private void Dialog_no_inicio() {

        Button oknoinicio;
        dialog.setContentView(R.layout.nosesion); //Conxión con vista creada

        oknoinicio = dialog.findViewById(R.id.oknoinicio);

        oknoinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para cerrar el dialogo apenas se da ok
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false); //Al presional fuer de la animación, esta seguira mostrandose, a menos que hagan click en ok
        dialog.show();
    }
}