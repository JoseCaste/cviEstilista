package Obejtos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cviestilista.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Anotacion_dialogo_estilista_Fragment extends DialogFragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Activity actividad;
    String id;
    String nomPerro;
    String responsable;
    String servicio;
    String numTel;
    String precio;
    String comentarios;
    TextView dialogNombrePerro;
    TextView dialogResponsable;
    TextView dialogPrecio;
    TextView dialogComentarios;
    TextView dialogNumTel;
    TextView dialogServicio;
    Button btnActualizar;


    public Anotacion_dialogo_estilista_Fragment() {
        // Required empty public constructor
    }

    public Anotacion_dialogo_estilista_Fragment(String id,String nomPerro, String responsable,String servicio, String numTel, String precio, String comentarios) {
        this.id=id;
        this.nomPerro=nomPerro;
        this.responsable=responsable;
        this.numTel=numTel;
        this.precio=precio;
        this.comentarios=comentarios;
        this.servicio=servicio;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogo();
    }

    private AlertDialog crearDialogo() {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View v= inflater.inflate(R.layout.fragment_anotacion_dialogo_estilista_,null);
        builder.setView(v);
        dialogNombrePerro=v.findViewById(R.id.dialogNombrePerro);
        dialogResponsable=v.findViewById(R.id.dialogNombreResponsable);
        dialogNumTel=v.findViewById(R.id.dialogNumTel);
        dialogPrecio=v.findViewById(R.id.dialogPrecio);
        dialogComentarios=v.findViewById(R.id.dialogComentarios);
        dialogServicio=v.findViewById(R.id.dialogServicio);
        btnActualizar=v.findViewById(R.id.btnActualizarAnotacionEstilista);

        dialogNombrePerro.setText(this.nomPerro);
        dialogResponsable.setText(this.responsable);
        dialogNumTel.setText(this.numTel);
        dialogComentarios.setText(this.comentarios);
        dialogPrecio.setText(this.precio);
        dialogServicio.setText(this.servicio);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * "Comentarios del Servicio: "+servicio.getComentarios()+"\n"+"Servicio: "+servicio.getServicio()
                 * "Precio del servicio: "+servicio.getPrecio()
                 *
                 * Se descompone los comentarios ya que es un comentario general para cada cardview, pero no podemos poner
                 * eso en la base de datos, porque en ella hay, servicio, comentarios como atributos independientes
                 * */

                String cadena=dialogComentarios.getText().toString();
                enviarActualizacion(cadena);
               // Toast.makeText(getContext(),"id: "+id,Toast.LENGTH_LONG).show();
            }
        });
        return builder.create();
    }

    private void enviarActualizacion(String cadena) {
       // for (String elemento: aux) {
            /**
            * este arreglo contiene {Comentarios del servicio, comentario escrito por el usuario, Servicio, servicio Elegido por el usuario}
            * y lo imprime asi:
             *
             * I/System.out: Comentarios del Servicio
             * I/System.out:  nada
             * I/System.out: Servicio
             *      Corte/Baño
             *
             *      se mandara al servidor que tiene la base de datos los elementos con el
             *      indice 1 y 2; es decir: p.e nada y Corte/Baño
            * **/
         //   System.out.println(elemento);
        //}
        String url="https://cvixtepec.000webhostapp.com/ActualizaRegistro.php?idServicio="+id+"&nombrePerro="+dialogNombrePerro.getText().toString()+"&responsable="+dialogResponsable.getText().toString()+"&servicio="+dialogServicio.getText().toString()+"&comentarios="+"\n"+dialogComentarios.getText().toString()+"&numTel="+dialogNumTel.getText().toString()+"&precio="+dialogPrecio.getText().toString()+"";
        url=url.replace(" ","%20");


        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.actividad=(Activity) context;

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(),"Error al modificar",Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getContext(),"Actualización exitosa",Toast.LENGTH_LONG).show();

        dismiss();

    }
}
