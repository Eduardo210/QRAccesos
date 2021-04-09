package mx.com.qrplus.qrplusaccesos.Helpers;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import mx.com.qrplus.qrplusaccesos.Login.LoginAccesos;
import mx.com.qrplus.qrplusaccesos.R;

public class Helper extends AppCompatActivity {


    public void UsuarioIncorrecto(Context context, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mensaje").setIcon(R.drawable.error);
        builder.setMessage(mensaje);
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

    public void AccesoCorrecto(final Context context, String mensaje, final Class clase) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mensaje").setIcon(R.drawable.bien);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, clase);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void CamposVacios(Context context, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mensaje).setIcon(R.drawable.alert);
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

    public void ProgressDialog(Context context, String titulo) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(titulo);
        progressDialog.setIcon(R.drawable.bien);
        progressDialog.setMessage("Cargando...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
//        progressDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                progressDialog.dismiss();
//            }
//        });
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

    public static boolean VerificarConexionInternet(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }
    public void ConectionNetwordFalse(Context context){
        Toast.makeText(context, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
    }
}



