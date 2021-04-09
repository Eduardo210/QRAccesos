package mx.com.qrplus.qrplusaccesos.ui.gallery;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

public class GalleryFragment extends Fragment {

    AlertDialog.Builder dialogBulier;
    AlertDialog dialog;
    EditText nombreInvitado, ApePaternoInvitado, ApeMaternoInvitado, correoInvitado;
    Button btnAgregarInvitado, btnCancelarInvitado;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    EditText ethora, edtHoraFin;
    ImageView btnAddUsuario, btnUpdateInivtado;

    String FechaInicio, FechaFin;
    String HoraInicio, Horafin;

    //    mas bariables
    String nombre1, paterno1, materno1, correo1;
    //    Variables para el de agregar nuevo invitado
    String nameInvitado, paternoInivtado, maternoInvitado, emailInvitado;

    //    Lista para visualizar los invitados
    ListView list;
    List<String> ListaNombreInvitado = new ArrayList<>();
    List<String> ListaPaternoInvitado = new ArrayList<>();
    List<String> ListaMaternoInvitado = new ArrayList<>();
    List<String> ListaCorreoInvitados = new ArrayList<>();
    List<String> ListaIdInvitado = new ArrayList<>();
    List<String> ListaSeleccionInvitado = new ArrayList<>();
    List<String> ListaInvitados = new ArrayList<>();

    ArrayAdapter<String> adapter;

    Button btnGuardarEvento, btnCancelar;
    EditText nombreEvento;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
    Date date1, date2, hour2, hour3, hour4, hour5;

    final Helper h = new Helper();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        list = root.findViewById(R.id.list);

        btnUpdateInivtado = root.findViewById(R.id.btnUpdateInvitados);
        btnUpdateInivtado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Actualizando");
                progressDialog.setIcon(R.drawable.bien);
                progressDialog.setMessage("Cargando...");
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                Thread.sleep(200);
                                progressDialog.incrementProgressBy(10);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                progressDialog.show();
            }
        });

        LlenarListaInvitados();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ejecutarLista(position);
            }
        });

        btnAddUsuario = root.findViewById(R.id.btnAddUsuario);
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
//      Fecha de inicio del Evento
        final EditText edtFechaInicio = root.findViewById(R.id.edtFechaInicio);
        final EditText edtFechaFinal = root.findViewById(R.id.edtFechaFin);
        ethora = root.findViewById(R.id.edtHoraInicio);
        edtHoraFin = root.findViewById(R.id.edtHoraFin);
        ethora.setEnabled(false);
        edtHoraFin.setEnabled(false);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = simpleDateFormat.format(new Date());
        edtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();
                int yy = calendario.get(Calendar.YEAR);
                int mm = calendario.get(Calendar.MONTH);
                int dd = calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String fecha = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year;
                        Date date = new Date();
                        try {
                            date1 = sdf.parse(fecha);
                            date2 = sdf.parse(dateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (date1.before(date2)) {
                            ethora.setEnabled(true);
                            edtHoraFin.setEnabled(true);
                            String hour1 = simpleDateFormat.format(new Date());
                            edtFechaInicio.setText(dateFormat.format(date));
                            edtFechaFinal.setText(dateFormat.format(date));
                            edtHoraFin.setText("23:59:59");
                            h.CamposVacios(getActivity(), "La fecha no puede ser anterior a la actual: " + dateFormat.format(date));
                        } else {
                            String hour1 = simpleDateFormat.format(new Date());
                            ethora.setEnabled(true);
                            edtHoraFin.setEnabled(true);
                            ethora.setText(hour1);
                            edtFechaInicio.setText(fecha);
                            FechaInicio = fecha;
                            edtFechaFinal.setText(fecha);
                            FechaFin = fecha;
                            edtHoraFin.setText("23:59:59");
                        }

                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

//        edtFechaFinal.setEnabled(false);
//        edtFechaFinal.setOnClickListener(new View.OnClickListener() {
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
//                        String fecha = dayOfMonth + "/" + monthOfYear + 1 + "/" + year;
//                        edtFechaFinal.setText(fecha);
//                        FechaFin = fecha;
//                    }
//                }, yy, mm, dd);
//                datePicker.show();
//            }
//        });

        ethora.setOnClickListener(new View.OnClickListener() {
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
                        String hour1 = simpleDateFormat.format(new Date());
                        try {
                            hour3 = hourFormat.parse(hour1);
                            hour2 = hourFormat.parse(horaFormateada + DOS_PUNTOS + minutoFormateado + ":00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (hour3.after(hour2) && date1.equals(date2)) {
                            h.CamposVacios(getActivity(), "La hora es menor a la actual");
                            edtHoraFin.setEnabled(true);
                            ethora.setText(hour1);
                            HoraInicio = hour1;
                        } else {
                            edtHoraFin.setEnabled(true);
                            ethora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + ":00");
                            HoraInicio = horaFormateada + DOS_PUNTOS + minutoFormateado + ":00";
                        }


                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
            }
        });


        edtHoraFin.setOnClickListener(new View.OnClickListener() {
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
                        try {
                            hour4 = hourFormat.parse(ethora.getText().toString());
                            hour5 = hourFormat.parse(horaFormateada + DOS_PUNTOS + minutoFormateado + ":00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (hour4.after(hour5) && date1.equals(date2)) {
                            h.CamposVacios(getActivity(), "La hora no puede ser menor");
                            edtHoraFin.setText("23:59:00");
                            Horafin = "23:59:00";
                        } else {
                            //Muestro la hora con el formato deseado
                            edtHoraFin.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + ":00");
                            Horafin = horaFormateada + DOS_PUNTOS + minutoFormateado + ":00";
                        }
                    }
                    //Estos valores deben ir en ese orden
                    //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                    //Pero el sistema devuelve la hora en formato 24 horas
                }, hora, minuto, false);

                recogerHora.show();
            }
        });

        btnGuardarEvento = root.findViewById(R.id.btnGuardarEvento);
        btnGuardarEvento.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                nombreEvento = root.findViewById(R.id.edtNombreEvento);
                if (nombreEvento.getText().toString().isEmpty()) {
                    h.CamposVacios(getActivity(), "Ingresa el Nombre del Evento");
                } else {
                    if (edtFechaInicio.getText().toString().isEmpty()) {
                        h.CamposVacios(getActivity(), "Ingresa la Fecha de Inicio");
                    } else {
                        if (edtFechaFinal.getText().toString().isEmpty()) {
                            h.CamposVacios(getActivity(), "Ingresa la Fecha de Fin del Evento");
                        } else {
                            if (ethora.getText().toString().isEmpty()) {
                                h.CamposVacios(getActivity(), "Ingresa la Hora de Inicio del Evento");
                            } else {
                                if (edtHoraFin.getText().toString().isEmpty()) {
                                    h.CamposVacios(getActivity(), "Ingresa la Hora de Fin del Evento");
                                } else {
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z");
//                                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(FechaInicio + " " + HoraInicio + ":00 Europe/Paris", formatter);
//                                    h.CamposVacios(getActivity(),zonedDateTime + "  \n");
//                                    GuardarEvento();
                                    if (ListaInvitados.isEmpty()) {
                                        h.CamposVacios(getActivity(), "Selecciona al menos un Invitado");
                                    } else {
                                        GuardarEvento();
//                                        EnviarInvitacionesPorCorreo();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        return root;
    }

    private void ejecutarLista(int position) {
//        ListaSeleccionInvitado.set(position,"1");
        if (ListaSeleccionInvitado.get(position).equals("1")) {
            ListaSeleccionInvitado.set(position, "0");
            ListaInvitados.remove(ListaIdInvitado.get(position));
            list.invalidateViews();
        } else {
            if (ListaSeleccionInvitado.get(position).equals("0")) {
                ListaSeleccionInvitado.set(position, "1");
                ListaInvitados.add(ListaIdInvitado.get(position));
                list.invalidateViews();
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
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_INSERT_EVENT);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());
                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("domicilio_id", Global.domicilio_Id);
                request.addProperty("residente_id", Global.usuario_Id);
                request.addProperty("FechaEventoInicio", FechaInicio + " " + ethora.getText().toString());
                request.addProperty("FechaEventoFin", FechaFin + " " + edtHoraFin.getText().toString());
                request.addProperty("Descripcion", nombreEvento.getText().toString());
                request.addProperty("SisUsuID_Alta", Global.Usu_Id);
                request.addProperty("SisPerfilID_Alta", Global.perfil_Id);
                request.addProperty("SisUsuTipo_Id", Global.tipo_id);
                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_INSERT_EVENT, envelope);
                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("Mensaje").toString();
                RespuestaId = body.getProperty("RespuestaId").toString();
                Global.NoIdEvento = Integer.parseInt(RespuestaId);
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
                builder.setTitle("Se guardo correctamente el Evento").setIcon(R.drawable.bien);
                builder.setCancelable(false);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EnviarInvitacionesPorCorreo();
                        h.ProgressDialog(getActivity(),"Enviando Correos");
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

    //  Método para enviar los correos
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
                builder.setTitle("Registro y Envio del Evento Correctamente").setIcon(R.drawable.bien);
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

    //  Llena la lista de inivtados
    private void LlenarListaInvitados() {
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
                    ListaSeleccionInvitado.add("0");
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
                ListAdapter listAdapter = new ListAdapter(getActivity(), ListaNombreInvitado, ListaPaternoInvitado,
                        ListaMaternoInvitado, ListaCorreoInvitados, ListaIdInvitado, ListaSeleccionInvitado);
                list.setAdapter(listAdapter);

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

    //  Metodo y clase inserta un nuevo invitado a la posible lista de invitados
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

//  Metodo para agregar in nuvo contacto a la lista

}

