package mx.com.qrplus.qrplusaccesos.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.qrplus.qrplusaccesos.Helpers.Global;
import mx.com.qrplus.qrplusaccesos.Helpers.Helper;
import mx.com.qrplus.qrplusaccesos.Helpers.ListAdapter;
import mx.com.qrplus.qrplusaccesos.MainActivity;
import mx.com.qrplus.qrplusaccesos.R;


public class EditaEvento extends Fragment {
    AlertDialog.Builder dialogBulier;
    AlertDialog dialog;
    EditText nombreInvitado, ApePaternoInvitado, ApeMaternoInvitado, correoInvitado;
    Button btnAgregarInvitado, btnCancelarInvitado;
    ImageView btnAddUsuario;
    String nombre1, paterno1, materno1, correo1;
    //    Variables para el de agregar nuevo invitado
    String nameInvitado, paternoInivtado, maternoInvitado, emailInvitado;
    Spinner estado;
    EditText nombreEvento, dateInicio, timeInicio, dateFin, timeFin;
    String domicilio, estated, fechaInicio, horaInicio, fechaFin, horaFin, nombreEventos, idEvento;
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String CERO = "0";
    int estadoEvento;
    ListView list, list2;
    List<String> ListaNombreInvitado = new ArrayList<>();
    List<String> ListaPaternoInvitado = new ArrayList<>();
    List<String> ListaMaternoInvitado = new ArrayList<>();
    List<String> ListaCorreoInvitados = new ArrayList<>();
    List<String> ListaIdInvitado = new ArrayList<>();
    List<String> ListaSeleccionInvitado = new ArrayList<>();

    //    -------------------------------------------------------------
    List<String> ListaInvitados = new ArrayList<>();

    //    --------------------------------------------------------
    List<String> ListaNombreInvitado1 = new ArrayList<>();
    List<String> ListaPaternoInvitado1 = new ArrayList<>();
    List<String> ListaMaternoInvitado1 = new ArrayList<>();
    List<String> ListaCorreoInvitados1 = new ArrayList<>();
    List<String> ListaIdInvitado1 = new ArrayList<>();
    List<String> ListaSeleccionInvitado1 = new ArrayList<>();
//    -----------------------------------------------------------------

    Helper h = new Helper();
    Button btnGuardarEvento, btnCancelar;

    String[] partes;
    String[] partes2;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
    Date date1, date2, hour2, hour3, hour4, hour5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edita_evento, container, false);
        Bundle result = this.getArguments();

        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        estado = view.findViewById(R.id.spinner);
        String[] opciones = {"Activo", "Cancelar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, opciones);
        estado.setAdapter(adapter);

        estated = result.getString("estatus");

        nombreEvento = view.findViewById(R.id.edtNombreEvento);
        nombreEventos = result.getString("nombreEvento");
        nombreEvento.setText(nombreEventos);

        dateInicio = view.findViewById(R.id.edtFechaInicio);
        fechaInicio = result.getString("fechaInicio");
        partes = fechaInicio.split("/");
        dateInicio.setText(partes[1] + "/0" + partes[0] + "/" + partes[2]);
        fechaInicio = partes[1] + "/0" + partes[0] + "/" + partes[2];

        timeInicio = view.findViewById(R.id.edtHoraInicio);
        horaInicio = result.getString("horaInicio");
        timeInicio.setText(horaInicio);

        dateFin = view.findViewById(R.id.edtFechaFin);
        fechaFin = result.getString("fechaFin");
        partes2 = fechaFin.split("/");
        dateFin.setText(partes2[1] + "/0" + partes2[0] + "/" + partes2[2]);
        fechaFin = partes2[1] + "/0" + partes2[0] + "/" + partes2[2];

        timeFin = view.findViewById(R.id.edtHoraFin);
        horaFin = result.getString("horaFin");
        timeFin.setText(horaFin);

        idEvento = result.getString("idEvento");

        dateInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();
                int yy = calendario.get(Calendar.YEAR);
                int mm = calendario.get(Calendar.MONTH);
                int dd = calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String fecha = dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year;
                        Date date = new Date();
                        try {
                            date1 = sdf.parse(fecha);
                            date2 = sdf.parse(dateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date1.before(date2)) {
                            dateInicio.setText(fechaInicio);
                            dateFin.setText(fechaInicio);
                            h.CamposVacios(getActivity(), "La hora no puede ser menor a la actual");
                        } else {
                            dateInicio.setText(fecha);
                            dateFin.setText(fecha);
                            fechaInicio = fecha;
                            fechaFin = fecha;
                        }

                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

        timeInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        timeInicio.setText(horaFormateada + ":" + minutoFormateado + ":00");
                        horaInicio = horaFormateada + ":" + minutoFormateado + ":00";
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
            }
        });

//        dateFin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar calendario = Calendar.getInstance();
//                int yy = calendario.get(Calendar.YEAR);
//                int mm = calendario.get(Calendar.MONTH);
//                int dd = calendario.get(Calendar.DAY_OF_MONTH);
//                final JSONArray uno = new JSONArray();
//                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        String fecha = dayOfMonth + "/" + "0"+(monthOfYear + 1) + "/" + year;
//                        dateFin.setText(fecha);
//                        fechaFin = fecha;
//                    }
//                }, yy, mm, dd);
//                datePicker.show();
//            }
//        });

        timeFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Formateo el hora obtenido: antepone el 0 si son menores de 10
                        String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                        String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                        //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        //Muestro la hora con el formato deseado
                        timeFin.setText(horaFormateada + ":" + minutoFormateado + ":00");
                        horaFin = horaFormateada + ":" + minutoFormateado + ":00";
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
            }
        });

        list2 = view.findViewById(R.id.list2);
        LlenarListaContactos();
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ejecutarLista(position);
            }
        });
        btnAddUsuario = view.findViewById(R.id.btnAddUsuario);
        btnAddUsuario.setOnClickListener(new View.OnClickListener() {
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

        btnGuardarEvento = view.findViewById(R.id.btnGuardarEvento);
        btnGuardarEvento.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                nombreEvento = view.findViewById(R.id.edtNombreEvento);
                if (nombreEvento.getText().toString().isEmpty()) {
                    h.CamposVacios(getActivity(), "Ingresa el Nombre del Evento");
                } else {
                    if (dateInicio.getText().toString().isEmpty()) {
                        h.CamposVacios(getActivity(), "Ingresa la Fecha de Inicio");
                    } else {
                        if (dateFin.getText().toString().isEmpty()) {
                            h.CamposVacios(getActivity(), "Ingresa la Fecha de Fin del Evento");
                        } else {
                            if (timeInicio.getText().toString().isEmpty()) {
                                h.CamposVacios(getActivity(), "Ingresa la Hora de Inicio del Evento");
                            } else {
                                if (timeFin.getText().toString().isEmpty()) {
                                    h.CamposVacios(getActivity(), "Ingresa la Hora de Fin del Evento");
                                } else {
                                    for (int i = 0; i < ListaIdInvitado.size(); i++) {
                                        ListaInvitados.add(ListaIdInvitado.get(i));
                                    }
                                    GuardarEvento();
                                }
                            }
                        }
                    }
                }
            }
        });
        list = view.findViewById(R.id.list);


        return view;
    }

    private void ejecutarLista(int position) {
//        ListaSeleccionInvitado.set(position,"1");
        if (ListaSeleccionInvitado1.get(position).equals("1")) {
            ListaSeleccionInvitado1.set(position, "0");
            ListaInvitados.remove(ListaIdInvitado1.get(position));
            list2.invalidateViews();
        } else {
            if (ListaSeleccionInvitado1.get(position).equals("0")) {
                ListaSeleccionInvitado1.set(position, "1");
                ListaInvitados.add(ListaIdInvitado1.get(position));
                list2.invalidateViews();
            }
        }

    }

    private void GuardarEvento() {
        new SetTreasureBoxAsyn().execute();
    }

    private class SetTreasureBoxAsyn extends AsyncTask<String, String, String> {
        String MensajeId;
        String RespuestaId;
        String UsuarioId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject result = null;

            try {
                String select = estado.getSelectedItem().toString();
                if (select.equals("Activo")) {
                    estadoEvento = 128;
                } else {
                    if (select.equals("Desactivar")) {
                        estadoEvento = 1;
                    }
                }
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_EDIT_EVENT);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());
                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("IDEvento", idEvento);
                request.addProperty("domicilio_id", Global.domicilio_Id);
                request.addProperty("reesidente_id", Global.usuario_Id);
                request.addProperty("FechaEventoInicio", fechaInicio + " " + horaInicio);
                request.addProperty("FechaEventoFin", fechaFin + " " + horaFin);
                request.addProperty("Estatus", estadoEvento);
                request.addProperty("Descripcion", nombreEvento.getText().toString());
                request.addProperty("SisUsuID_Alta", Global.Usu_Id);
                request.addProperty("SisPerfilID_Alta", Global.perfil_Id);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_EDIT_EVENT, envelope);
                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("Mensaje").toString();
                RespuestaId = body.getProperty("RespuestaId").toString();
                Global.NoIdEvento = Integer.parseInt(idEvento);
                UsuarioId = body.getProperty("UsuarioId").toString();


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
                builder.setTitle("Guardando evento").setIcon(R.drawable.bien);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String select = estado.getSelectedItem().toString();
                        if (select.equals("Activo")) {
                            EnviarInvitacionesPorCorreo();
                            h.ProgressDialog(getActivity(), "Guardando Cambios del Evento");
                            dialogInterface.dismiss();
                        } else {
                            if (select.equals("Desactivar")) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
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

    private void EnviarInvitacionesPorCorreo() {
        new EnviarCorreos().execute();
    }

    private class EnviarCorreos extends AsyncTask<String, String, String> {
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
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_REGISTER_INVITED);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());
                String cadenaenviaridcorreos = "";
                for (int i = 0; i < ListaInvitados.size(); i++) {
                    cadenaenviaridcorreos += ListaInvitados.get(i) + "@";
                }

//                Parameters
                request.addProperty("NombreUsuarioWCF", Global.usuarioWCF);
                request.addProperty("PwsWCF", Global.passwordWCF + formattedDate);
                request.addProperty("evento_id", Global.NoIdEvento);
                request.addProperty("PersonaExternaInvitada_id", cadenaenviaridcorreos);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_REGISTER_INVITED, envelope);

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
//      Aqui van los datsoq eu se enviarion los correos
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Se enviaron las Invitaciones Correctamente").setIcon(R.drawable.bien);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                isCancelled();
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

    private void LlenarListaInvitados() {
        new ListaInvitados().execute();
    }

    private class ListaInvitados extends AsyncTask<String, String, String> {
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

            SoapObject result = null;
            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_LIST_INVITED_EVENT);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("NombreUsuarioWCF", Global.usuarioWCF);
                request.addProperty("PwsWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Usuario", Global.usuario_Id);
                request.addProperty("EventoId", Integer.parseInt(idEvento));
                request.addProperty("Perfil", Global.perfil_Id);
                request.addProperty("TipoUsuario", Global.tipo_id);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_LIST_INVITED_EVENT, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                Mensaje = body.getProperty("Mensaje").toString();
                RespuestaId = body.getProperty("RespuestaId").toString();
                UsuarioId = body.getProperty("UsuarioId").toString();
                SoapObject bodyRespuesta = (SoapObject) body.getProperty("Invitaciones");
                for (int i = 0; i < bodyRespuesta.getPropertyCount(); i++) {
                    SoapObject elementoRespuesta = (SoapObject) bodyRespuesta.getProperty(i);
                    ListaIdInvitado.add(elementoRespuesta.getProperty("Persona_Externa_Id").toString());
                    ListaNombreInvitado.add(elementoRespuesta.getProperty("Persona_Nombre").toString());
                    ListaPaternoInvitado.add(elementoRespuesta.getProperty("Persona_APaterno").toString());
                    ListaMaternoInvitado.add(elementoRespuesta.getProperty("Persona_AMaterno").toString());
                    ListaCorreoInvitados.add(elementoRespuesta.getProperty("Persona_Correo1").toString());
                    ListaSeleccionInvitado.add("1");
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
//                ListAdapter listAdapter = new ListAdapter(getActivity(), ListaNombreInvitado, ListaPaternoInvitado,
//                        ListaMaternoInvitado, ListaCorreoInvitados, ListaIdInvitado, ListaSeleccionInvitado);
//                list.setAdapter(listAdapter);
                compararelementos();


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

    private void compararelementos() {
        for (int i = 0; i < ListaIdInvitado1.size(); i++) {
            if (ListaIdInvitado.contains(ListaIdInvitado1.get(i))) {
                ListaIdInvitado1.remove(i);
                ListaCorreoInvitados1.remove(i);
                ListaMaternoInvitado1.remove(i);
                ListaPaternoInvitado1.remove(i);
                ListaNombreInvitado1.remove(i);
                ListaSeleccionInvitado1.remove(i);
            }
            ListAdapter listAdapter = new ListAdapter(getActivity(), ListaNombreInvitado, ListaPaternoInvitado,
                    ListaMaternoInvitado, ListaCorreoInvitados, ListaIdInvitado, ListaSeleccionInvitado);
            list.setAdapter(listAdapter);

            ListAdapter listAdapter1 = new ListAdapter(getActivity(), ListaNombreInvitado1, ListaPaternoInvitado1,
                    ListaMaternoInvitado1, ListaCorreoInvitados1, ListaIdInvitado1, ListaSeleccionInvitado1);
            list2.setAdapter(listAdapter1);
        }



    }

    private void LlenarListaContactos() {
        new ListaContactos().execute();
    }

    private class ListaContactos extends AsyncTask<String, String, String> {
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
                request.addProperty("EventosId", 0);
                request.addProperty("Perfil", Global.perfil_Id);
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
                    ListaNombreInvitado1.add(elementoRespuesta.getProperty("Persona_Nombre").toString());
                    ListaPaternoInvitado1.add(elementoRespuesta.getProperty("Persona_APaterno").toString());
                    ListaMaternoInvitado1.add(elementoRespuesta.getProperty("Persona_AMaterno").toString());
                    ListaCorreoInvitados1.add(elementoRespuesta.getProperty("Persona_Correo1").toString());
                    ListaIdInvitado1.add(elementoRespuesta.getProperty("Persona_Externa_Id").toString());
                    ListaSeleccionInvitado1.add("0");
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
//                ListAdapter listAdapter = new ListAdapter(getActivity(), ListaNombreInvitado1, ListaPaternoInvitado1,
//                        ListaMaternoInvitado1, ListaCorreoInvitados1, ListaIdInvitado1, ListaSeleccionInvitado1);
//                list2.setAdapter(listAdapter);
                LlenarListaInvitados();

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

    //    Metodo para agrgar un nuevo elemento a la lista de contactos
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
}
