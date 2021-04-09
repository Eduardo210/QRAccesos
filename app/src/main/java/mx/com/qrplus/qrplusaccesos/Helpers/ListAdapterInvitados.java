package mx.com.qrplus.qrplusaccesos.Helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.com.qrplus.qrplusaccesos.R;

public class ListAdapterInvitados extends ArrayAdapter<String> {
    Activity context;
    private final List<String> ListaNombreInvitado;
    private final List<String> ListaPaternoInvitado;
    private final List<String> ListaMaternoInvitado;
    private final List<String> ListaCorreoInvitado;
    private final List<String> ListaIdInvitado;

    public ListAdapterInvitados(Activity context, List<String> ListaNombreInvitado, List<String> ListaPaternoInvitado,
                                List<String> ListaMaternoInvitado, List<String> ListaCorreoInvitado, List<String> ListaIdInvitado) {
        super(context, R.layout.list, ListaNombreInvitado);
        this.context = context;
        this.ListaNombreInvitado = ListaNombreInvitado;
        this.ListaPaternoInvitado =ListaPaternoInvitado;
        this.ListaMaternoInvitado = ListaMaternoInvitado;
        this.ListaCorreoInvitado = ListaCorreoInvitado;
        this.ListaIdInvitado = ListaIdInvitado;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView nombre = (TextView) rowView.findViewById(R.id.ListaNombreInvitado);;
        TextView correo = (TextView) rowView.findViewById(R.id.txtCorreo);
        TextView idinvitado = (TextView) rowView.findViewById(R.id.txtIdUser);

        nombre.setText(ListaNombreInvitado.get(position) + " " + ListaPaternoInvitado.get(position) + " " + ListaMaternoInvitado.get(position));
        correo.setText(ListaCorreoInvitado.get(position));
        idinvitado.setText(ListaIdInvitado.get(position));

        return rowView;

    };
}
