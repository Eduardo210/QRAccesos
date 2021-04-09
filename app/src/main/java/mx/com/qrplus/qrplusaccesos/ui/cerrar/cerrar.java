package mx.com.qrplus.qrplusaccesos.ui.cerrar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.com.qrplus.qrplusaccesos.Login.LoginAccesos;
import mx.com.qrplus.qrplusaccesos.MainActivity;
import mx.com.qrplus.qrplusaccesos.R;


public class cerrar extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Esta seguro que desea cerrar sesion?").setIcon(R.drawable.alert);
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginAccesos.class);
                intent.putExtra("valor","1");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
