package mx.com.qrplus.qrplusaccesos.ui.ResetPassword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class reset_password extends Fragment {
    EditText edtactual, edtnueva, edtconfirmar;
    ImageView btnactual, btnnueva, btnconfirmar;
    String pswactual, pswnueva, pswconfirmar;
    Button btnmodificar;
    Helper h = new Helper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        edtactual = view.findViewById(R.id.edtactual);
        edtnueva = view.findViewById(R.id.edtnueva);
        edtconfirmar = view.findViewById(R.id.edtconfirmar);

        btnactual = view.findViewById(R.id.btnactual);
        btnnueva = view.findViewById(R.id.btnnueva);
        btnconfirmar = view.findViewById(R.id.btnconfirmar);

        btnmodificar = view.findViewById(R.id.btnmodificar);

        btnactual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtactual.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edtactual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return false;
            }
        });

        btnnueva.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtnueva.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edtnueva.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return false;
            }
        });

        btnconfirmar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtconfirmar.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edtconfirmar.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return false;
            }
        });

        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pswactual = edtactual.getText().toString();
                pswnueva = edtnueva.getText().toString();
                pswconfirmar = edtconfirmar.getText().toString();
                if (edtactual.getText().toString().isEmpty()){
                    h.CamposVacios(getActivity(), "Ingresa tu contraseña actual");
                }else{
                    if (edtnueva.getText().toString().isEmpty()){
                        h.CamposVacios(getActivity(),"Ingresa la nueva contraseña");
                    }else{
                        if (edtconfirmar.getText().toString().isEmpty()){
                            h.CamposVacios(getActivity(),"Ingresa la confirmación de la contraseña");
                        }else{
                            if (pswactual.equals(pswconfirmar)){
                                h.CamposVacios(getActivity(),"Las nuevas contraseñas no coinciden, verifica por favor");
                            }else{
                                ejecutarCambiarContrasena();
                            }
                        }
                    }
                }
            }
        });
        Button cancelar = view.findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }

    private void ejecutarCambiarContrasena() {
        new CambiarPassword().execute();
    }

    private class CambiarPassword extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String usuarioId;
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
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_CHANSE_PASSWORD);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

//                Parameters
                request.addProperty("usuarioWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Email", Global.correoUsuario);
                request.addProperty("SisUsu_Contrasenia", pswconfirmar);


                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_CHANSE_PASSWORD, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                usuarioId = body.getProperty("_3_UsuarioId").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Se cambio correctamente la contraseña");
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
