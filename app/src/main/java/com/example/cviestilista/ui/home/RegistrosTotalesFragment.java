package com.example.cviestilista.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cviestilista.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Obejtos.Servicio;
import Obejtos.ServiciosRecyclerViewAdapter;

public class RegistrosTotalesFragment extends Fragment  implements Response.Listener<JSONObject>, Response.ErrorListener {

    private RegistrosTotalesViewModel registrosTotalesViewModel;
    private RecyclerView recyclerView;
    private ArrayList<Servicio>lisadoServicio;
    private ProgressDialog progressDialog;
    private  SwipeRefreshLayout swipeRefreshLayout;
   // private  ServiciosRecyclerViewAdapter serviciosRecyclerViewAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registros_totales, container, false);

        recyclerView=root.findViewById(R.id.recycler);
        //recyclerView=root.findViewById(R.id.listaServicios);
        lisadoServicio= new ArrayList<>();

        swipeRefreshLayout=root.findViewById(R.id.swipeRegistrosTotales);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getContext(),"Swipe",Toast.LENGTH_LONG).show();
                        llamarConsulta();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });


        llamarConsulta();
       // homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        //System.out.println("holaaaAS "+lisadoServicio.size());




        return root;
    }

    private void llamarConsulta() {
        //Toast.makeText(getContext(),"Hola",Toast.LENGTH_LONG).show();
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando...");
        progressDialog.show();
        RequestQueue reqquest;
        JsonObjectRequest jsonObjectRequest;



        reqquest= Volley.newRequestQueue(getContext());
        //
        String url= "https://cvixtepec.000webhostapp.com/ConsultaRegistro.php";
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        reqquest.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"Error al consultar los registros",Toast.LENGTH_LONG).show();
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json=response.optJSONArray("datosDevueltos");
        //System.out.println("tamaño"+json.length());
        ArrayList<Servicio>auxLista= new ArrayList<>();
        try {
        for (int i=0;i<json.length();i++){

                JSONObject jsonObject= json.getJSONObject(i);

                //Toast.makeText(getActivity().getApplicationContext(),jsonObject.optInt("nombrePerro"),Toast.LENGTH_LONG).show();
                int id= jsonObject.optInt("id");
                String nombrePerro=jsonObject.optString("nombrePerro");
                String responsable=jsonObject.optString("responsable");
                String servicio= jsonObject.optString("servicio");
                String comentario=jsonObject.optString("comentarios");
                String numTel=jsonObject.optString("numTel");
                int precio=jsonObject.optInt("precio");
                String fecha=jsonObject.optString("fecha");
                String entregado=jsonObject.optString("entregado");
            //System.out.println(nombrePerro+" Entregado? "+entregado);
                Servicio aux= new Servicio(id,nombrePerro,responsable,servicio,comentario,numTel,precio,fecha,entregado);
                auxLista.add(aux);
                //lisadoServicio.add(aux);

               //System.out.println("-->"+aux.toString());


        }


        asignaLista(auxLista);
        } catch (JSONException e) {
            Toast.makeText(getContext(),"No se pudo establecer la consulta",Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }



    }

    private void asignaLista(ArrayList<Servicio> auxLista) {
        //System.out.println("auxLista: "+auxLista.size());
        progressDialog.hide();
        lisadoServicio=auxLista;
        //System.out.println("auxLista asigna lisado: "+lisadoServicio.size());
        LinearLayoutManager linearLayout= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        ServiciosRecyclerViewAdapter serviciosRecyclerViewAdapter= new ServiciosRecyclerViewAdapter(getActivity(),getContext(),lisadoServicio);
        recyclerView.setAdapter(serviciosRecyclerViewAdapter);

    }
}
