package mx.com.qrplus.qrplusaccesos.Helpers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.com.qrplus.qrplusaccesos.R;

public class OnSwipeTouchListener extends Fragment implements View.OnTouchListener {

    ListView list;
    private GestureDetector gestureDetector;
    private Context context;
    int IdUsuario;
    List<String> ListaNombreInvitado = new ArrayList<>();
    List<String> ListaPaternoInvitado = new ArrayList<>();
    List<String> ListaMaternoInvitado = new ArrayList<>();
    List<String> ListaCorreoInvitados = new ArrayList<>();
    List<String> ListaIdInvitado = new ArrayList<>();
    AlertDialog.Builder dialogBulier;
    AlertDialog dialog;

    @SuppressLint("ValidFragment")
    public OnSwipeTouchListener(Context ctx, ListView list, List<String> ListaNombreInvitado, List<String> ListaPaternoInvitado, List<String> ListaMaternoInvitado,
                                List<String> ListaCorreoInvitado, List<String> ListaIdInvitado) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        context = ctx;
        this.list = list;
        this.ListaNombreInvitado = ListaNombreInvitado;
        this.ListaPaternoInvitado = ListaPaternoInvitado;
        this.ListaMaternoInvitado = ListaMaternoInvitado;
        this.ListaCorreoInvitados = ListaCorreoInvitado;
        this.ListaIdInvitado = ListaIdInvitado;
    }

    public OnSwipeTouchListener() {
        super();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight(int pos) {
        //Do what you want after swiping left to right


    }

    public void onSwipeLeft(int pos) {
        //Do what you want after swiping right to left
        String nombre = ListaNombreInvitado.get(pos) + " " + ListaPaternoInvitado.get(pos) + " " + ListaMaternoInvitado.get(pos);
        IdUsuario = Integer.parseInt(ListaIdInvitado.get(pos));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(" ¿Deseas eliminar el Usuario?").setIcon(R.drawable.error);
        builder.setMessage("Usuario: " + nombre).setIcon(R.drawable.error);
        builder.setCancelable(false);
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                elimimarInvitadoExp();
                list.invalidateViews();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    private void elimimarInvitadoExp() {
        new SetTreasureBoxAsyncTask().execute();
    }
    private class SetTreasureBoxAsyncTask extends AsyncTask<String, String, String>
    {
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

            SoapObject result = null;

            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_NAME_DELETE_INVITED);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF );
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("Persona_Externa_Id", IdUsuario);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_NAME_DELETE_INVITED, envelope);

                // Get the result
                result = (SoapObject)envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                MensajeId = body.getProperty("_1_MensajeId").toString();
                Mensaje = body.getProperty("_2_Mensaje").toString();
                UsuarioId = body.getProperty("_3_UsuarioId").toString();
//                Global.usuario_Id = Integer.parseInt(UsuarioId);
//                PerfilId = body.getProperty("_4_PerfilId").toString();
//                Global.perfil_Id = Integer.parseInt(PerfilId);
//                TipoId = body.getProperty("_5_TipoId").toString();
//                Global.tipo_id = Integer.parseInt(TipoId);
//                DomicilioId = body.getProperty("_6_DomiId").toString();
//                Global.domicilio_Id = Integer.parseInt(DomicilioId);
//                UsuId = body.getProperty("_7_UsuId").toString();
//                Global.Usu_Id = Integer.parseInt(UsuId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString() ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Mensaje").setIcon(R.drawable.error);
                builder.setMessage("Usuario eliminado");
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.invalidateViews();
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog= builder.create();
                dialog.show();
            }
            else
            {
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
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        }

    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPostion(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(getPostion(e1));
                else
                    onSwipeLeft(getPostion(e1));
                return true;
            }
            return false;
        }

    }
}

