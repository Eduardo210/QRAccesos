package mx.com.qrplus.qrplusaccesos.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsee.library.Bugsee;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mx.com.qrplus.qrplusaccesos.Helpers.Global;
import mx.com.qrplus.qrplusaccesos.Helpers.Helper;
import mx.com.qrplus.qrplusaccesos.MainActivity;
import mx.com.qrplus.qrplusaccesos.R;

public class LoginAccesos extends AppCompatActivity {
    //  Elementos de input para login de usuario y elementos de interfaz de usuario
    EditText edtPassword, edtUsuario, correoInvitado;
    ImageButton Observar;
    TextView txtResetPassword, txtAvisoPrivacidad, txtAvisoPrivacidadtext, txtTitulo;
    Button btnAceptar, btnCancelarRecuperacion, btnEnviarrecuperacion;
    String password, usuario, correoRecuperacion;
    android.app.AlertDialog.Builder dialogBulier;
    android.app.AlertDialog dialog;
    Switch estado;
    final Helper h = new Helper();
    String sesion = "0";
    SoapObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bugsee.launch(this, "352db7a9-f471-4320-a85c-e2080f30ad17");
        setContentView(R.layout.activity_login_accesos);

        estado = findViewById(R.id.RememberUserPassword);

        edtPassword = findViewById(R.id.edtPassword);
        Observar = findViewById(R.id.btnObservar);
        Observar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

//      TextView para Resetear la contraseña
        txtResetPassword = findViewById(R.id.txtResetPassword);
        SpannableString content = new SpannableString(txtResetPassword.getText());
        content.setSpan(new UnderlineSpan(), 0, txtResetPassword.length(), 0);
        txtResetPassword.setText(content);
        txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarPassword();
            }
        });
//      TextView para Ver los terminos y condiciones
        txtAvisoPrivacidad = findViewById(R.id.txtAvisoPrivacidad);
        SpannableString conte = new SpannableString(txtAvisoPrivacidad.getText());
        conte.setSpan(new UnderlineSpan(), 0, txtAvisoPrivacidad.length(), 0);
        txtAvisoPrivacidad.setText(conte);
        txtAvisoPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.VerificarConexionInternet(LoginAccesos.this)==true){
                    new TerminosYCondiciones().execute();
                }else{
                    h.ConectionNetwordFalse(LoginAccesos.this);
                }
            }
        });
//      Verficicamos los datos de los edtTexview
        edtUsuario = findViewById(R.id.edtUsuario);
        btnAceptar = findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.VerificarConexionInternet(LoginAccesos.this) == true) {
                    if (edtUsuario.getText().toString().isEmpty() && edtPassword.getText().toString().isEmpty()) {

                        h.CamposVacios(LoginAccesos.this, "Ingresa los campos");
                    } else {
                        if (!edtUsuario.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()) {

                            usuario = edtUsuario.getText().toString();
                            password = edtPassword.getText().toString();
                            Global.correoUsuario = usuario;
                            GuardarPreferencias();
                            validarUsaurio();
                            edtUsuario.setText("");
                            edtPassword.setText("");
                        } else {
                            h.AccesoCorrecto(LoginAccesos.this, "Usuario y/o Contraseña Incorrectos", MainActivity.class);
                            edtUsuario.setText("");
                            edtPassword.setText("");
                            edtUsuario.setText("");
                        }
                    }
                } else {
                    h.ConectionNetwordFalse(LoginAccesos.this);
                }
            }
        });
        Intent recibir = getIntent();
        sesion = recibir.getStringExtra("valor");

        if (sesion == null) {
            cargarPreferencias();
        } else {
            if (sesion.equals("1")) {
            }
        }


    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        estado.setChecked(preferences.getBoolean("checked", false));
        edtUsuario.setText(preferences.getString("usuario", ""));
        edtPassword.setText(preferences.getString("password", ""));
    }

    private void GuardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean valor = estado.isChecked();
        if (valor == true) {
            String usuario = edtUsuario.getText().toString();
            String password = edtPassword.getText().toString();
            editor.putBoolean("checked", valor);
            editor.putString("usuario", usuario);
            editor.putString("password", password);
        } else {
            editor.putBoolean("checked", valor);
            editor.putString("usuario", "");
            editor.putString("password", "");
        }
        editor.commit();
    }

    private void RecuperarPassword() {
        dialogBulier = new android.app.AlertDialog.Builder(this);
        View popupview = getLayoutInflater().inflate(R.layout.popuprecuperarpassword, null);

        correoInvitado = popupview.findViewById(R.id.edtCorreoRecuperacion);
        btnCancelarRecuperacion = popupview.findViewById(R.id.btnCancelarrecuperacion);
        btnCancelarRecuperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnEnviarrecuperacion = popupview.findViewById(R.id.btnEnviarCorreo);
        btnEnviarrecuperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.VerificarConexionInternet(LoginAccesos.this)==true){
                    correoRecuperacion = correoInvitado.getText().toString();
                    if (correoRecuperacion.isEmpty()) {
                        Helper h = new Helper();
                        h.CamposVacios(LoginAccesos.this, "Ingresa el correo electronico");
                    } else {
                        new EnviarRecuperacionPassword().execute();
                        dialog.dismiss();
                    }
                }else{
                    h.ConectionNetwordFalse(LoginAccesos.this);
                }
            }
        });

        dialogBulier.setView(popupview);
        dialog = dialogBulier.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void validarUsaurio() {
        new SetTreasureBoxAsyncTask().execute();
    }

    private class SetTreasureBoxAsyncTask extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String UsuarioId;
        String PerfilId;
        String TipoId;
        String DomicilioId;
        String UsuId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_LOGIN);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("usuario", usuario);
                request.addProperty("password", password);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_LOGIN, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                UsuarioId = body.getProperty("_3_UsuarioId").toString();
                Global.usuario_Id = Integer.parseInt(UsuarioId);
                PerfilId = body.getProperty("_4_PerfilId").toString();
                Global.perfil_Id = Integer.parseInt(PerfilId);
                TipoId = body.getProperty("_5_TipoId").toString();
                Global.tipo_id = Integer.parseInt(TipoId);
                DomicilioId = body.getProperty("_6_DomiId").toString();
                Global.domicilio_Id = Integer.parseInt(DomicilioId);
                UsuId = body.getProperty("_7_UsuId").toString();
                Global.Usu_Id = Integer.parseInt(UsuId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                int idmensaje = Integer.parseInt(MensajeId);
                if (idmensaje < 128) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                    builder.setTitle("Usuario y/o Contraseña Incorrectos").setIcon(R.drawable.alert);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            edtUsuario.setText("");
                            edtPassword.setText("");
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    isCancelled();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                    builder.setTitle("Acceso correcto").setIcon(R.drawable.bien);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(LoginAccesos.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    isCancelled();
                }

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                builder.setTitle("ALERTA");
                builder.setMessage("No hay conexion con el Servidor");
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    private class EnviarRecuperacionPassword extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String UsuarioId;
        String Codigo;
        String NombreCompleto, Usu_Identificado, UsuContrasenia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = null;

            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_RESET_PASSWORD);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Email", correoRecuperacion);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_RESET_PASSWORD, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                UsuarioId = body.getProperty("_3_UsuarioId").toString();
                NombreCompleto = body.getProperty("_4_NombreCompleto").toString();
                Usu_Identificado = body.getProperty("_5_Usu_Identificador").toString();
                UsuContrasenia = body.getProperty("_6_Usu_Contrasenia").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                builder.setTitle("Mensaje No: " + MensajeId);
                builder.setMessage(Mensaje);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int idmensaje = Integer.parseInt(MensajeId);
                        if (idmensaje < 128) {
                            dialogInterface.dismiss();
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                isCancelled();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                builder.setTitle("ALERTA");
                builder.setMessage("No hay conexion con el Servidor");
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    private class TerminosYCondiciones extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String Titulo;
        String cuerpo;
        String Fecha;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = null;

            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_DOCUMENTS_LEGAL);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

//                Parameters
                request.addProperty("usuarioWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Tipo_Doc", "1");


                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_DOCUMENTS_LEGAL, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                Titulo = body.getProperty("_3_Titulo").toString();
                cuerpo = body.getProperty("_4_Cuerpo").toString();
                Fecha = body.getProperty("_5_Fecha").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                dialogBulier = new android.app.AlertDialog.Builder(LoginAccesos.this);
                View popupview = getLayoutInflater().inflate(R.layout.popupavisodeprivacidad, null);
                txtTitulo = popupview.findViewById(R.id.txttitulo);
                txtTitulo.setText(Titulo);
                txtAvisoPrivacidadtext = popupview.findViewById(R.id.txtViewTerminos);
                txtAvisoPrivacidadtext.setText(cuerpo);
                dialogBulier.setView(popupview);
                dialog = dialogBulier.create();
                dialog.setCancelable(false);
                dialog.show();
                Button cerrar = popupview.findViewById(R.id.btnCerrarTerminos);
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAccesos.this);
                builder.setTitle("ALERTA");
                builder.setMessage("No hay conexion con el Servidor");
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }


}

