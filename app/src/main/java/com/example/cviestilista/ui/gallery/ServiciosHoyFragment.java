package com.example.cviestilista.ui.gallery;

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
import androidx.lifecycle.ViewModelProviders;
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

public class ServiciosHoyFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private ServiciosHoyViewModel serviciosHoyViewModel;

    private RecyclerView recyclerView;
    private ArrayList<Servicio> lisadoServicio;
    private ProgressDialog process;
    private  SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serviciosHoyViewModel =
                ViewModelProviders.of(this).get(ServiciosHoyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_registros_hoy, container, false);



        recyclerView=root.findViewById(R.id.recyclerViewRegistrosHoy);
        lisadoServicio= new ArrayList<>();

        swipeRefreshLayout=root.findViewById(R.id.swipeRegistrosHoy);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getContext(),"Swipe",Toast.LENGTH_LONG).show();
                        consultarDatos();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });


        consultarDatos();
        return root;
    }



    private void consultarDatos() {
        process= new ProgressDialog(getContext());
        process.setTitle("Cargando...");
        process.show();
        RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(getContext());
        String url="https://cvixtepec.000webhostapp.com/ConsultaRegistroHoy.php";
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"Error al consultar los registros",Toast.LENGTH_LONG).show();
        process.hide();
    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray datosObtenidos= response.optJSONArray("datosDevueltos");
        try {
            if(datosObtenidos.get(0).toString().equals("No")){
                Toast.makeText(getContext(),"Sin reservaciones",Toast.LENGTH_LONG).show();
                process.hide();
            }else{
                ArrayList<Servicio>auxLista= new ArrayList<>();
                try {
                    for (int i=0;i<datosObtenidos.length();i++){

                        JSONObject jsonObject= datosObtenidos.getJSONObject(i);
                        int id= jsonObject.optInt("id");
                        String nombrePerro=jsonObject.optString("nombrePerro");
                        String responsable=jsonObject.optString("responsable");
                        String servicio= jsonObject.optString("servicio");
                        String comentario=jsonObject.optString("comentarios");
                        String numTel=jsonObject.optString("numTel");
                        int precio=jsonObject.optInt("precio");
                        String fecha=jsonObject.optString("fecha");
                        String entregado=jsonObject.optString("entregado");
                        Servicio aux= new Servicio(id,nombrePerro,responsable,servicio,comentario,numTel,precio,fecha,entregado);
                        auxLista.add(aux);
                    }


                    asignaLista(auxLista);
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"No se pudo establecer la consulta",Toast.LENGTH_LONG).show();
                    process.hide();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void asignaLista(ArrayList<Servicio> auxLista) {
        lisadoServicio=auxLista;
        LinearLayoutManager linearLayout= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        ServiciosRecyclerViewAdapter serviciosRecyclerViewAdapter= new ServiciosRecyclerViewAdapter(getActivity(),getContext(),lisadoServicio);
        recyclerView.setAdapter(serviciosRecyclerViewAdapter);
        process.hide();
    }


}
