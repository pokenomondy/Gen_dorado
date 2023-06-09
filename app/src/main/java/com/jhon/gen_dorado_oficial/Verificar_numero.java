package com.jhon.gen_dorado_oficial;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Verificar_numero extends AppCompatActivity {

    Button btnsendverificarnumero;
    private EditText[] editTexts = new EditText[6];
    private EditText editCodigoVer1, editCodigoVer2, editCodigoVer3, editCodigoVer4, editCodigoVer5, editCodigoVer6;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "PhoneAuthActivity";
    String mVerificationId;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_numero);

        btnsendverificarnumero = (Button) findViewById(R.id.btn_send_verificarnumero);


        editCodigoVer1 = findViewById(R.id.editcodver1);
        editCodigoVer2 = findViewById(R.id.editcodver2);
        editCodigoVer3 = findViewById(R.id.editcodver3);
        editCodigoVer4 = findViewById(R.id.editcodver4);
        editCodigoVer5 = findViewById(R.id.editcodver5);
        editCodigoVer6 = findViewById(R.id.editcodver6);
        //Permitir que los edittext solo permitan ingresar una variable, y no hacer enter
        editTexts[0] = editCodigoVer1;
        editTexts[1] = editCodigoVer2;
        editTexts[2] = editCodigoVer3;
        editTexts[3] = editCodigoVer4;
        editTexts[4] = editCodigoVer5;
        editTexts[5] = editCodigoVer6;
        //focus en editver1 y mostrar teclado obligado
        editCodigoVer1.requestFocus();
        editCodigoVer1.setInputType(InputType.TYPE_CLASS_NUMBER);

        //Cambio en los textos de codigo de verificación
        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < editTexts.length - 1) {
                        editTexts[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        editTexts[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        //Variables de bases de datos

        mAuth = FirebaseAuth.getInstance();


        mVerificationId = getIntent().getStringExtra(MainActivity.verificacionId);


    }

    public void accion_verificarnumero(View view){
        String editCodigoVerificacion = editCodigoVer1.getText().toString() +
                editCodigoVer2.getText().toString() +
                editCodigoVer3.getText().toString() +
                editCodigoVer4.getText().toString() +
                editCodigoVer5.getText().toString() +
                editCodigoVer6.getText().toString();
        String code = editCodigoVerificacion;

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();

                            Toast.makeText(Verificar_numero.this,"Verificado correctamente",LENGTH_SHORT).show();

                            if(isNewUser){
                                Intent nuevouser = new Intent(Verificar_numero.this,RegistroNuevoUsuario.class);
                                startActivity(nuevouser);
                                finish();
                            }else{
                                Intent usuariorecurrente = new Intent(Verificar_numero.this,HomeActivity.class);
                                startActivity(usuariorecurrente);
                                finish();
                            }


                        } else {
                            Toast.makeText(Verificar_numero.this,"Codigo mal ingresado, hagalo de nuevo",LENGTH_SHORT).show();

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }
                    }
                });
    }


}
