package Obejtos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cviestilista.R;

import org.json.JSONObject;

import java.util.List;

import static com.example.cviestilista.R.*;


public class ServiciosRecyclerViewAdapter extends RecyclerView.Adapter<ServiciosRecyclerViewAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Servicio> listadoServicios;
    final Context context;
    RequestQueue reqquest;
    FragmentActivity activity;




    public ServiciosRecyclerViewAdapter(FragmentActivity activity,Context context, List<Servicio> listadoServicios) {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.listadoServicios=listadoServicios;
        this.activity=activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicio,parent,false);
        View view= inflater.inflate(layout.item_servicio,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Servicio servicio=listadoServicios.get(position);



        if (servicio.getEntregado().equals("si")){
            int color= activity.getResources().getColor(R.color.verde);
            holder.cardView.setCardBackgroundColor(color);
        }




        holder.txtNombrePerro.setText(servicio.getNombrePerro());
        holder.txtResponsable.setText(servicio.getNombreResponsable());
        holder.txtFecha.setText(servicio.getFecha());
        holder.txtComentario.setText(servicio.getComentarios());
        holder.txtServicio.setText(servicio.getServicio());
        //holder.txtComentario.setText("Comentarios del Servicio:"+servicio.getComentarios()+":"+servicio.getServicio());
        holder.txtContacto.setText(servicio.getNumTel());
        holder.id.setText(""+servicio.getId());
        String costo=""+servicio.getPrecio();
        if(costo.equals("0")){
            holder.txtCosto.setText("Precio no definido");
        }else{
            holder.txtCosto.setText(("Precio del servicio: "+servicio.getPrecio()));
        }




    }

    @Override
    public int getItemCount() {
        return listadoServicios.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Response.Listener<JSONObject>,Response.ErrorListener {
        public TextView txtNombrePerro;
        public  TextView txtResponsable;
        public TextView txtContacto;
        public TextView txtFecha;
        public TextView txtComentario;
        public TextView txtServicio;
        public TextView txtCosto;
        public CardView cardView;
        public TextView id;



        public ViewHolder (@NonNull View itemView){
            super(itemView);

            txtNombrePerro=itemView.findViewById(R.id.txtNombrePerro);
            txtResponsable=itemView.findViewById(R.id.txtResponsable);
            txtContacto= itemView.findViewById(R.id.txtContacto);
            txtFecha=itemView.findViewById(R.id.txtFecha);
            cardView=itemView.findViewById(R.id.cardViewServicio);
            txtCosto=itemView.findViewById(R.id.txtCostoItem);
            txtServicio=itemView.findViewById(R.id.txtServicio);
            txtComentario=itemView.findViewById(R.id.txtComentario);
            id=itemView.findViewById(R.id.idPerro);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(final View v) {
            final CharSequence opciones[]={"Anotaciones y modificación del estilista","Llamar al responsable","Eliminar"};
            AlertDialog.Builder dialogo= new AlertDialog.Builder(v.getContext());
            dialogo.setTitle("Elija una opcion");
            dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //which contiene la opciones del charsequence

                    switch (which){
                        case 0:
                            anotacionEstilista();
                            break;
                        case 1:
                            llamarResponsable();
                            break;
                        case 2:
                            String idEliminar=(String)id.getText().toString();
                            String url= "https://cvixtepec.000webhostapp.com/EliminarRegistro.php?id="+idEliminar+"";
                            eliminarRegistro(url);
                            break;

                    }
                }
            });
            dialogo.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void llamarResponsable() {
            String telefono= "tel:"+txtContacto.getText();
            Intent accion= new Intent(Intent.ACTION_CALL, Uri.parse(telefono));
            accion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if(context.checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
            context.startActivity(accion);


           AlertDialog.Builder builder= new AlertDialog.Builder(context);
           builder.setTitle("Llamada...");
           builder.setMessage("¿Se notificó al cliente?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {


                   String url="https://cvixtepec.000webhostapp.com/ActualizarEstadoRegistro.php?id="+id.getText().toString()+"";
                   reqquest= Volley.newRequestQueue(context.getApplicationContext());
                   JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                       @Override
                       public void onResponse(JSONObject response) {
                           Toast.makeText(context.getApplicationContext(),"Finalizado",Toast.LENGTH_LONG).show();
                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           Toast.makeText(context.getApplicationContext(),"Error al proceso",Toast.LENGTH_LONG).show();
                       }
                   });
                   reqquest.add(jsonObjectRequest);
               }
           }).setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

               }
           });

           builder.show();
            //Toast.makeText(context.getApplicationContext(),"acaba de terminar",Toast.LENGTH_LONG).show();
        }

        private void anotacionEstilista() {
            String idPerro=id.getText().toString();
            String nomPerro= txtNombrePerro.getText().toString();
            String responsable=txtResponsable.getText().toString();
            String numTel=txtContacto.getText().toString();
            String precio=txtCosto.getText().toString();
            String comentarios=txtComentario.getText().toString();
            String servicio=txtServicio.getText().toString();

            precio=buscaPrecio(precio); //busca el precio, ya que si un cliente reserva el precio por defecto es 0, por lo tanto en la tarjeta está como "precio no difinido"
            //en caso de que si halla precio, este aparece como p.e: "precio del servicio: 205"

            final Anotacion_dialogo_estilista_Fragment dialogo= new Anotacion_dialogo_estilista_Fragment(idPerro,nomPerro,responsable,servicio,numTel,precio,comentarios);
            dialogo.show(((FragmentActivity)context).getSupportFragmentManager(),"dialogoAnotacion");
        }

        private String buscaPrecio(String precio) {
            String precioEncontrado="";
            for (int i=0;i<precio.length();i++){


                if (Character.isDigit(precio.charAt(i))){
                    precioEncontrado=precioEncontrado+precio.charAt(i);

                }
            }
            return precioEncontrado;
        }

        private void eliminarRegistro(String url) {
            JsonObjectRequest jsonObjectRequest;
            reqquest= Volley.newRequestQueue(context.getApplicationContext());
            //Toast.makeText(context.getApplicationContext(),""+url,Toast.LENGTH_LONG).show();
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, this,this);
            reqquest.add(jsonObjectRequest);
        }

        @Override
        public void onResponse(JSONObject response) {

            Toast.makeText(context.getApplicationContext(),"Finalizado",Toast.LENGTH_LONG).show();


        }
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context.getApplicationContext(),"Error al proceso "+error.getMessage(),Toast.LENGTH_LONG).show();

        }


    }


}
