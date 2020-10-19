package com.example.cviestilista.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cviestilista.R;

import org.json.JSONObject;

public class AgregarServicioFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    private AgregarServicioViewModel agregarServicioViewModel;
    private  TextView txtResponsable,txtNomMascota,txtContacto,txtComentarios,txtPrecio;
    private CheckBox chbxBano,chbxBanoCorte,chbxCorte;
    private Button btnRegistrar;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agregarServicioViewModel =
                ViewModelProviders.of(this).get(AgregarServicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agregar_servicio, container, false);

        btnRegistrar=(Button) root.findViewById(R.id.btnRegistro);
        txtNomMascota=(EditText)root.findViewById(R.id.txtNomMas);
        txtResponsable=(EditText)root.findViewById(R.id.txtResponsable);
        txtContacto=(EditText)root.findViewById((R.id.txtNumeroTel));
        txtComentarios=(EditText)root.findViewById(R.id.txtComentarios);
        txtPrecio=(EditText)root.findViewById(R.id.txtCostoItem);
        chbxBano=(CheckBox)root.findViewById(R.id.chbxBanio);
        chbxBanoCorte=(CheckBox)root.findViewById(R.id.chbxCorteBanio);
        chbxCorte=(CheckBox)root.findViewById(R.id.chbCorte);
        btnRegistrar=(Button)root.findViewById(R.id.btnRegistro);

        requestQueue=Volley.newRequestQueue(getContext());
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peticionesRegistro();
            }
        });



        return root;
    }
    private void peticionesRegistro() {
        if (verificaCampos()) {
            if (verificaChecbox()) {
                String servicio = obtieneServicio();
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Cargando...");
                progressDialog.show();

                String url = "https://cvixtepec.000webhostapp.com/insertaRegistroEstilista.php?nombrePerro=" + txtNomMascota.getText().toString() + "&responsable=" + txtResponsable.getText().toString() + "&servicio=" + servicio + "&comentarios=" + txtComentarios.getText().toString() + "&numTel=" + txtContacto.getText().toString() + "&precio="+txtPrecio.getText().toString();
                //Toast.makeText(getContext(),""+txtPrecio.getText().toString(),Toast.LENGTH_LONG).show();

                url = url.replace(" ", "%20");
                JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
                requestQueue.add(json);
            } else {
                Toast.makeText(getContext(), "Hay multiples servicios seleccionados", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean verificaCampos() {
        if(txtResponsable.getText().toString().equals("") || txtNomMascota.getText().toString().equals("") || txtContacto.getText().toString().equals("") || txtPrecio.getText().toString().equals("")){
            Toast.makeText(getContext(),"Debe llenar los campos",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
    private String obtieneServicio() {
        String servicio;

        if(chbxCorte.isChecked()){
            servicio="Corte";
        }else{
            if(chbxBano.isChecked()){
                servicio="Baño";
            }else{
                servicio="Corte/Baño";
            }
        }
        return  servicio;
    }
    private boolean verificaChecbox() {
        boolean flag=false;

        if(chbxCorte.isChecked() && !chbxBanoCorte.isChecked() && !chbxBano.isChecked()){
            flag=true;
        }else{
            if(chbxBano.isChecked() && !chbxBanoCorte.isChecked() && !chbxCorte.isChecked()){
                flag=true;
            }else{
                if(chbxBanoCorte.isChecked() && !chbxBano.isChecked() && !chbxCorte.isChecked()){
                    flag=true;
                }else{
                    flag=false;
                }
            }
        }
        return flag;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"Error de registro",Toast.LENGTH_LONG).show();
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Registro con exito",Toast.LENGTH_LONG).show();
        progressDialog.hide();
        txtContacto.setText("");
        txtNomMascota.setText("");
        txtResponsable.setText("");
        txtComentarios.setText("");
        txtContacto.setText("");
        txtPrecio.setText("");
    }
}
