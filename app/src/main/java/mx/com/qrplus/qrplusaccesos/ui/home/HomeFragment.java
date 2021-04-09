package mx.com.qrplus.qrplusaccesos.ui.home;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import mx.com.qrplus.qrplusaccesos.Helpers.ListAdapterEventos;
import mx.com.qrplus.qrplusaccesos.R;
import mx.com.qrplus.qrplusaccesos.ui.gallery.GalleryFragment;

public class HomeFragment extends Fragment {

    // Elemento lista donde se acumularan los elementos de la consulta
    ListView list;
    android.app.AlertDialog.Builder dialogBulier;
    android.app.AlertDialog dialog;
    // Elementos de lista donde se van a acumular los elementos de la consulta
    List<String> ListaDomicilioEvento = new ArrayList<>();
    List<String> ListaStatusEvento = new ArrayList<>();
    List<String> ListaFechaInicio = new ArrayList<>();
    List<String> ListaHoraInicio = new ArrayList<>();
    List<String> ListaFechaFin = new ArrayList<>();
    List<String> ListaHoraFin = new ArrayList<>();
    List<String> ListaNumeroInvitados = new ArrayList<>();
    List<String> ListaNombreEventos = new ArrayList<>();
    List<String> ListaIdEvento = new ArrayList<>();
    // Elementos de donde se partiran el elemeto fecha y hora de la consulta
    String[] partes;
    String[] parte2;
    String[] parte3;
    //  Elemento de actualizar por medio de una imagen donde funcionara como botón
    ImageView btnActualizar;
    TextView estadoEventos;
    Helper h = new Helper();
    ImageView btnAgregarEvento;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//      Cargamos el Fragment al elemento del activity
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnAgregarEvento = root.findViewById(R.id.btnAgregarEventio);
        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFragment fr=new GalleryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home,fr)
                        .addToBackStack(null)
                        .commit();
            }
        });
//      Instacioamos el elemento de la lista con el xml
        list = root.findViewById(R.id.list);
//      Invocamos al metodo consulta evento del usuario
        ConsultarEventosUsuario();
//      Evento onClick para cuando presione un elemnto de la lista donde se desata el evento
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle result = new Bundle();
                String domicilio, estatus, fechaInicio, horaInicio, fechaFin, horaFin, nombreEvento, idEvento;
                domicilio = ListaDomicilioEvento.get(position);
                estatus = ListaStatusEvento.get(position);
                fechaInicio = ListaFechaInicio.get(position);
                horaInicio = ListaHoraInicio.get(position);
                fechaFin = ListaFechaFin.get(position);
                horaFin = ListaHoraFin.get(position);
                nombreEvento = ListaNombreEventos.get(position);
                idEvento = ListaIdEvento.get(position);
                Fragment nuevoFragmento = new EditaEvento();
                result.putString("domicilio", domicilio);
                result.putString("estatus",estatus);
                result.putString("fechaInicio", fechaInicio);
                result.putString("horaInicio", horaInicio);
                result.putString("fechaFin",fechaFin);
                result.putString("horaFin", horaFin);
                result.putString("nombreEvento",nombreEvento);
                result.putString("idEvento",idEvento);

                EditaEvento fr=new EditaEvento();
                fr.setArguments(result);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home,fr)
                        .addToBackStack(null)
                        .commit();
            }
        });
//      Boton de actualizar
        btnActualizar = root.findViewById(R.id.btnActualizarEventos);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarEventosUsuario();
                h.ProgressDialog(getActivity(),"Cargando...");

            }
        });
        estadoEventos = root.findViewById(R.id.estadoeventos);
        return root;
    }

    private void ConsultarEventosUsuario() {
        new TerminosYCondiciones().execute();
    }

    private class TerminosYCondiciones extends AsyncTask<String, String, String> {
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
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_SELECT_EVENT);
                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());
                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Usuario", Global.usuario_Id);
                request.addProperty("estatus", 128);
                request.addProperty("Perfil", Global.perfil_Id);
                request.addProperty("TipoUsuario", Global.tipo_id);
                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);
                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_SELECT_EVENT, envelope);
                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                usuarioId = body.getProperty("_3_UsuarioId").toString();
//              Lismpiamos los elementos
                ListaDomicilioEvento.clear();
                ListaHoraFin.clear();
                ListaHoraInicio.clear();
                ListaIdEvento.clear();
                ListaNombreEventos.clear();
                ListaFechaFin.clear();
                ListaFechaInicio.clear();
                ListaStatusEvento.clear();
                SoapObject bodyRespuesta = (SoapObject) body.getProperty("leventos");
                for (int i = 0; i < bodyRespuesta.getPropertyCount(); i++) {
                    SoapObject elementoRespuesta = (SoapObject) bodyRespuesta.getProperty(i);
                    ListaDomicilioEvento.add(elementoRespuesta.getProperty("Domicilio").toString());
                    ListaStatusEvento.add(elementoRespuesta.getProperty("Estatus").toString());
                    String dato1 = elementoRespuesta.getProperty("FechaEventoInicio").toString();
                    partes = dato1.split(" ");
                    ListaFechaInicio.add(partes[0]);
                    ListaHoraInicio.add(partes[1]+" "+partes[2]);
                    String dato2 = elementoRespuesta.getProperty("FechaEventoFin").toString();
                    parte2 = dato2.split(" ");
                    ListaFechaFin.add(parte2[0]);
                    ListaHoraFin.add(parte2[1]+" "+parte2[2]);
                    ListaNumeroInvitados.add(elementoRespuesta.getProperty("Invitados").toString());
                    ListaNombreEventos.add(elementoRespuesta.getProperty("Nombre").toString());
                    ListaIdEvento.add(elementoRespuesta.getProperty("id").toString());
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
                if (ListaIdEvento.isEmpty()){
                    estadoEventos.setText("No hay eventos programados");
                }else{
                    ListAdapterEventos listAdapter = new ListAdapterEventos(getActivity(),
                            ListaNombreEventos, ListaFechaInicio, ListaHoraInicio, ListaFechaFin, ListaHoraFin, ListaNumeroInvitados, ListaIdEvento);
                    list.setAdapter(listAdapter);
                }

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


