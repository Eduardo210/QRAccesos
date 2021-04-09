package mx.com.qrplus.qrplusaccesos.ui.slideshow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.com.qrplus.qrplusaccesos.Helpers.Global;
import mx.com.qrplus.qrplusaccesos.Helpers.Helper;
import mx.com.qrplus.qrplusaccesos.Helpers.ListAdapterInvitados;
import mx.com.qrplus.qrplusaccesos.Helpers.OnSwipeTouchListener;
import mx.com.qrplus.qrplusaccesos.R;

public class SlideshowFragment extends Fragment {
    ListView list;
    List<String> ListaNombreInvitado = new ArrayList<>();
    List<String> ListaPaternoInvitado = new ArrayList<>();
    List<String> ListaMaternoInvitado = new ArrayList<>();
    List<String> ListaCorreoInvitados = new ArrayList<>();
    List<String> ListaIdInvitado = new ArrayList<>();

    AlertDialog.Builder dialogBulier;
    AlertDialog dialog;
    EditText nombreInvitado, ApePaternoInvitado, ApeMaternoInvitado, correoInvitado;
    Button btnAgregarInvitado, btnCancelarInvitado;
    //    mas bariables
    String nombre1, paterno1, materno1, correo1;
    //    Variables para el de agregar nuevo invitado
    int id_inivtado;
    String nameInvitado, paternoInivtado, maternoInvitado, emailInvitado;
    ImageView btnActualizar, btnNewContact;
    TextView Invitados;
    final Helper h = new Helper();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        list = root.findViewById(R.id.list);
        LlenarListaInvitados();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                dialogBulier = new AlertDialog.Builder(getContext());
                View popupview = getLayoutInflater().inflate(R.layout.popup, null);
                nombreInvitado = popupview.findViewById(R.id.edtNombreInvitado);
                nombreInvitado.setText(ListaNombreInvitado.get(position));
                ApePaternoInvitado = popupview.findViewById(R.id.edtPaternoInvitado);
                ApePaternoInvitado.setText(ListaPaternoInvitado.get(position));
                ApeMaternoInvitado = popupview.findViewById(R.id.edtMaterno);
                ApeMaternoInvitado.setText(ListaMaternoInvitado.get(position));
                correoInvitado = popupview.findViewById(R.id.edtCorrreoInvitado);
                correoInvitado.setText(ListaCorreoInvitados.get(position));
                btnCancelarInvitado = popupview.findViewById(R.id.btnCancelarUuario);
                btnCancelarInvitado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnAgregarInvitado = popupview.findViewById(R.id.btnGuardar);
                btnAgregarInvitado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameInvitado = nombreInvitado.getText().toString();
                        paternoInivtado = ApePaternoInvitado.getText().toString();
                        maternoInvitado = ApeMaternoInvitado.getText().toString();
                        emailInvitado = correoInvitado.getText().toString();
                        if (nombreInvitado.getText().toString().isEmpty()) {
                            h.CamposVacios(getActivity(), "Ingresa el Nombre del Invitado");
                        } else {
                            if (ApePaternoInvitado.getText().toString().isEmpty()) {
                                h.CamposVacios(getActivity(), "Ingresa el Apellido Paterno del Invitado");
                            } else {
                                if (ApeMaternoInvitado.getText().toString().isEmpty()) {
                                    h.CamposVacios(getActivity(), "Ingresa el Apellido Materno del Invitado");
                                } else {
                                    if (correoInvitado.getText().toString().isEmpty()) {
                                        h.CamposVacios(getActivity(), "Ingresa el Correo del Invitado");
                                    } else {
                                        id_inivtado = Integer.parseInt(ListaIdInvitado.get(position));
                                        obtieneDatosInvitadoNuevo(nameInvitado, paternoInivtado, maternoInvitado, emailInvitado);
                                        LlenarListaInvitados();
                                        dialog.dismiss();
                                    }
                                }
                            }
                        }
                    }
                });

                dialogBulier.setView(popupview);
                dialog = dialogBulier.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        list.setOnTouchListener(new OnSwipeTouchListener(getActivity(),
                list, ListaNombreInvitado, ListaPaternoInvitado, ListaMaternoInvitado, ListaCorreoInvitados, ListaIdInvitado));

        btnActualizar = root.findViewById(R.id.btnUpdateContactos);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Actualizando");
                progressDialog.setIcon(R.drawable.bien);
                progressDialog.setMessage("Cargando...");
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDialog.getProgress()<= progressDialog.getMax()){
                                Thread.sleep(100);
                                progressDialog.incrementProgressBy(10);
                                if (progressDialog.getProgress()== progressDialog.getMax()){
                                    progressDialog.dismiss();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                ListaNombreInvitado.clear();
                ListaPaternoInvitado.clear();
                ListaMaternoInvitado.clear();
                ListaCorreoInvitados.clear();
                ListaIdInvitado.clear();
                LlenarListaInvitados();



            }
        });
        Invitados = root.findViewById(R.id.invitados);
        btnNewContact = root.findViewById(R.id.btnNewContact);
        btnNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBulier = new AlertDialog.Builder(getContext());
                View popupview = getLayoutInflater().inflate(R.layout.popup, null);
                nombreInvitado = popupview.findViewById(R.id.edtNombreInvitado);
                ApePaternoInvitado = popupview.findViewById(R.id.edtPaternoInvitado);
                ApeMaternoInvitado = popupview.findViewById(R.id.edtMaterno);
                correoInvitado = popupview.findViewById(R.id.edtCorrreoInvitado);
                btnCancelarInvitado = popupview.findViewById(R.id.btnCancelarUuario);
                btnCancelarInvitado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnAgregarInvitado = popupview.findViewById(R.id.btnGuardar);
                btnAgregarInvitado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameInvitado = nombreInvitado.getText().toString();
                        paternoInivtado = ApePaternoInvitado.getText().toString();
                        maternoInvitado = ApeMaternoInvitado.getText().toString();
                        emailInvitado = correoInvitado.getText().toString();
                        if (nameInvitado.equals("")) {
                            Helper h = new Helper();
                            h.CamposVacios(getActivity(), "Ingresa el Nombre del Invitado");
                        } else {
                            if (paternoInivtado.equals("")) {
                                h.CamposVacios(getActivity(), "Ingresa el Apellido Paterno");
                            } else {
                                if (maternoInvitado.equals("")) {
                                    h.CamposVacios(getActivity(), "Ingresa el Apellido Materno");
                                } else {
                                    if (emailInvitado.equals("")) {
                                        h.CamposVacios(getActivity(), "Ingresa el Correo del Invitado");
                                    } else {
                                        obtieneDatosInvitadoNew(nameInvitado, paternoInivtado, maternoInvitado, emailInvitado);
                                        LlenarListaInvitados();
                                        dialog.dismiss();
                                    }
                                }
                            }
                        }
                    }
                });
                dialogBulier.setView(popupview);
                dialog = dialogBulier.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        return root;
    }

    public void obtieneDatosInvitadoNew(String nombre, String paterno, String materno, String correo) {
        nombre1 = nombre;
        paterno1 = paterno;
        materno1 = materno;
        correo1 = correo;
        new SetTreasureBoxAsyncTaskNuevo().execute();
    }

    private class SetTreasureBoxAsyncTaskNuevo extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String UsuarioId;
        String Codigo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = null;

            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_INSERT_PEOPLE_EXP);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Per_Nombre", nombre1);
                request.addProperty("Per_APaterno", paterno1);
                request.addProperty("Per_AMaterno", materno1);
                request.addProperty("Per_Domicilio_Id", Global.domicilio_Id);
                request.addProperty("Per_EmpRes_Id", Global.usuario_Id);
                request.addProperty("Per_Correo1", correo1);
                request.addProperty("Per_Id_Alta", Global.usuario_Id);
                request.addProperty("Per_Perfil_Id_Alta", Global.perfil_Id);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_INSERT_PEOPLE_EXP, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                UsuarioId = body.getProperty("_3_UsuarioId").toString();
                Codigo = body.getProperty("_4_PerfilId").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Mensaje).setIcon(R.drawable.bien);
//                builder.setMessage(Mensaje);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int idmensaje = Integer.parseInt(MensajeId);
                        if (idmensaje < 128) {
                            ListaNombreInvitado.clear();
                            ListaPaternoInvitado.clear();
                            ListaMaternoInvitado.clear();
                            ListaCorreoInvitados.clear();
                            ListaIdInvitado.clear();
                            LlenarListaInvitados();
                            dialogInterface.dismiss();
                        } else {
                            nombreInvitado.setText("");
                            ApePaternoInvitado.setText("");
                            ApeMaternoInvitado.setText("");
                            correoInvitado.setText("");
                            dialogInterface.dismiss();
                        }
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
                isCancelled();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    public void obtieneDatosInvitadoNuevo(String nombre, String paterno, String materno, String correo) {
        nombre1 = nombre;
        paterno1 = paterno;
        materno1 = materno;
        correo1 = correo;
        new SetTreasureBoxAsyncTask().execute();
    }

    private class SetTreasureBoxAsyncTask extends AsyncTask<String, String, String> {
        String MensajeId;
        String Mensaje;
        String UsuarioId;
        String Codigo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = null;

            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_UPDATE_INVITED);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Persona_Externa_Id", id_inivtado);
                request.addProperty("Persona_Nombre", nombre1);
                request.addProperty("Persona_APaterno", paterno1);
                request.addProperty("Persona_AMaterno", materno1);
                request.addProperty("Persona_Correo1", correo1);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_UPDATE_INVITED, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                UsuarioId = body.getProperty("_3_UsuarioId").toString();
                Codigo = body.getProperty("_4_PerfilId").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Mensaje).setIcon(R.drawable.bien);
//                builder.setMessage(Mensaje);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!MensajeId.equals("OK")) {
                            dialogInterface.dismiss();
                        } else {
                            ListaNombreInvitado.clear();
                            ListaPaternoInvitado.clear();
                            ListaMaternoInvitado.clear();
                            ListaCorreoInvitados.clear();
                            ListaIdInvitado.clear();
                            LlenarListaInvitados();
                            dialogInterface.dismiss();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                isCancelled();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    public void LlenarListaInvitados() {
        new TerminosYCondiciones().execute();
    }

    private class TerminosYCondiciones extends AsyncTask<String, String, String> {
        String Invitacion;
        String Mensaje;
        String RespuestaId;
        String UsuarioId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ListaNombreInvitado.clear();
            ListaPaternoInvitado.clear();
            ListaMaternoInvitado.clear();
            ListaCorreoInvitados.clear();
            ListaIdInvitado.clear();
            SoapObject result = null;
            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_LIST_INVITED);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("NombreUsuarioWCF", Global.usuarioWCF);
                request.addProperty("PwsWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Usuario", Global.usuario_Id);
                request.addProperty("Perfil", Global.perfil_Id);
                request.addProperty("EventosId", 0);
                request.addProperty("TipoUsuario", Global.tipo_id);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_LIST_INVITED, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                Mensaje = body.getProperty("Mensaje").toString();
                RespuestaId = body.getProperty("RespuestaId").toString();
                UsuarioId = body.getProperty("UsuarioId").toString();
                SoapObject bodyRespuesta = (SoapObject) body.getProperty("Invitacion");
                for (int i = 0; i < bodyRespuesta.getPropertyCount(); i++) {
                    SoapObject elementoRespuesta = (SoapObject) bodyRespuesta.getProperty(i);
                    ListaNombreInvitado.add(elementoRespuesta.getProperty("Persona_Nombre").toString());
                    ListaPaternoInvitado.add(elementoRespuesta.getProperty("Persona_APaterno").toString());
                    ListaMaternoInvitado.add(elementoRespuesta.getProperty("Persona_AMaterno").toString());
                    ListaCorreoInvitados.add(elementoRespuesta.getProperty("Persona_Correo1").toString());
                    ListaIdInvitado.add(elementoRespuesta.getProperty("Persona_Externa_Id").toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {

                ListAdapterInvitados listAdapter = new ListAdapterInvitados(getActivity(),
                        ListaNombreInvitado, ListaPaternoInvitado, ListaMaternoInvitado, ListaCorreoInvitados, ListaIdInvitado);
                list.setAdapter(listAdapter);
                String numero = String.valueOf(ListaIdInvitado.size());
                Invitados.setText(numero);

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



