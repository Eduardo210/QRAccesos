package mx.com.qrplus.qrplusaccesos.Helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import mx.com.qrplus.qrplusaccesos.R;

public class ListAdapterEventos extends ArrayAdapter<String> {
    Activity context;
    private final List<String> ListaNombreEventos;
    private final List<String> ListaFechaInicio;
    private final List<String> ListaHoraInicio;
    private final List<String> ListaFechaFin;
    private final List<String> ListaHoraFin;
    private final List<String> ListaNumeroInvitados;
    private final List<String> ListaIdEvento;


    public ListAdapterEventos(Activity context, List<String> ListaNombreInvitado, List<String> ListaFechaInicio, List<String> ListaHoraInicio,List<String> ListaFechaFin,
                              List<String> ListaHoraFin,List<String> ListaNumeroInvitado, List<String> ListaIdEvento) {
        super(context, R.layout.mylisteventos, ListaNombreInvitado);
        this.context = context;
        this.ListaNombreEventos = ListaNombreInvitado;
        this.ListaFechaInicio = ListaFechaInicio;
        this.ListaHoraInicio = ListaHoraInicio;
        this.ListaFechaFin = ListaFechaFin;
        this.ListaHoraFin = ListaHoraFin;
        this.ListaNumeroInvitados = ListaNumeroInvitado;
        this.ListaIdEvento = ListaIdEvento;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View rowView=inflater.inflate(R.layout.listeventos, null,true);

        TextView nombreevento = (TextView) rowView.findViewById(R.id.txtEvento);
        TextView idevento = (TextView) rowView.findViewById(R.id.txtEventoId);
        TextView fechainicio = (TextView)rowView.findViewById(R.id.txtFechaInicio);
        TextView horainicio = (TextView)rowView.findViewById(R.id.txtHoraInicio);
        TextView fechafin = (TextView) rowView.findViewById(R.id.txtFechaFin);
        TextView horafin = (TextView) rowView.findViewById(R.id.txtHoraFin);
        TextView numeroinvitados = (TextView)rowView.findViewById(R.id.txtInvitados);

        nombreevento.setText(ListaNombreEventos.get(position));
        idevento.setText("Folio: "+ListaIdEvento.get(position));
        fechainicio.setText(ListaFechaInicio.get(position));
        horainicio.setText(ListaHoraInicio.get(position));
        fechafin.setText(ListaFechaFin.get(position));
        horafin.setText(ListaHoraFin.get(position));
        numeroinvitados.setText("Invitado(s): " + ListaNumeroInvitados.get(position));
        return rowView;

    };
}
