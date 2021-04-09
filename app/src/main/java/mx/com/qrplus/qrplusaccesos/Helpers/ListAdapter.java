package mx.com.qrplus.qrplusaccesos.Helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.com.qrplus.qrplusaccesos.R;

public class ListAdapter extends ArrayAdapter<String> {
    Activity context;
    private final List<String> ListaNombreInvitado;
    private final List<String> ListaPaternoInvitado;
    private final List<String> ListaMaternoInvitado;
    private final List<String> ListaCorreoInvitado;
    private final List<String> ListaIdInvitado;
    private final List<String> ListaSeleccionInvitado;

    public ListAdapter(Activity context, List<String> ListaNombreInvitado, List<String> ListaPaternoInvitado,
                       List<String> ListaMaternoInvitado, List<String> ListaCorreoInvitado, List<String> ListaIdInvitado,
                       List<String> ListaSeleccionInvitado) {
        super(context, R.layout.list, ListaNombreInvitado);
        this.context = context;
        this.ListaNombreInvitado = ListaNombreInvitado;
        this.ListaPaternoInvitado =ListaPaternoInvitado;
        this.ListaMaternoInvitado = ListaMaternoInvitado;
        this.ListaCorreoInvitado = ListaCorreoInvitado;
        this.ListaIdInvitado = ListaIdInvitado;
        this.ListaSeleccionInvitado = ListaSeleccionInvitado;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView nombre = (TextView) rowView.findViewById(R.id.ListaNombreInvitado);;
        TextView correo = (TextView) rowView.findViewById(R.id.txtCorreo);
        TextView idinvitado = (TextView) rowView.findViewById(R.id.txtIdUser);
        if (ListaSeleccionInvitado.get(position).equals("1")){
            rowView.setBackgroundColor(R.drawable.btnguardarazul);
        }

//        List<String> nombres = new ArrayList<>();
//        for (int i = 0; i <ListaIdInvitado.size() ; i++) {
//            nombres.add(ListaNombreInvitado.get(i) + " " + ListaPaternoInvitado.get(i) + " " + ListaMaternoInvitado.get(i));
//        }
//        Collections.sort(nombres);
        nombre.setText(ListaNombreInvitado.get(position) + " " + ListaPaternoInvitado.get(position) + " " + ListaMaternoInvitado.get(position));
        correo.setText(ListaCorreoInvitado.get(position));
        idinvitado.setText(ListaIdInvitado.get(position));
        return rowView;

    };
}
