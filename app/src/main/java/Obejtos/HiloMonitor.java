package Obejtos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cviestilista.R;
import org.json.JSONArray;
import org.json.JSONObject;
public class HiloMonitor implements Runnable, Response.Listener<JSONObject>, Response.ErrorListener {
    String name;
    Context context;
    int bandera=100000;
    private PendingIntent pendingIntent;

    public HiloMonitor(final Context context) {
        name="Monitor";
        this.context=context;


        //primera iteracion para establecer el estado base de la bandera, ya que si se inicializa en 0, y con la condicion del metodo response(no la que se crea enseguida, la que está por fuera como un metodo de la clase)
        //lo interpretará que hubo un cambio de estado en la tabla, es decir, una notificacion
        String url="http://cvixtepec.000webhostapp.com/MonitorRegistros.php";
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray array=response.optJSONArray("datosDevueltos");
                bandera=array.length();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(),"Error al consultar la cantidad de registros "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void run() {

        try {

            while (true){
                String url="http://cvixtepec.000webhostapp.com/MonitorRegistros.php";
                RequestQueue requestQueue= Volley.newRequestQueue(context);
                JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
                requestQueue.add(jsonObjectRequest);
                Thread.sleep(5000);
            }

        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //System.out.println("Error al monitorear "+error.getMessage());
        //Toast.makeText(context,"Error al monitorear "+error.getMessage(),Toast.LENGTH_LONG).show();
    }



    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray=response.optJSONArray("datosDevueltos");
        //System.out.println(jsonArray.length());

        if(bandera<jsonArray.length()){
            //System.out.println("Hubo un cambio");
            crearNotificacion();
            this.bandera=jsonArray.length();

        }else{
            //System.out.println("Sin cambio");
        }


    }

    private void crearNotificacion() {

       // ServiciosHoyFragment serviciosHoyFragment= new ServiciosHoyFragment();
        /*Intent intent= new Intent(context,ServiciosHoyFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,intent,0);*/
        //context.startActivity(intent);
       //setPendingIntent();

        Toast.makeText(context,"nueva notifiacion",Toast.LENGTH_LONG).show();
        NotificationCompat.Builder notificador=new NotificationCompat.Builder(context,null);
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        String chanelId="NOTIFIACION";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            int importance= NotificationManager.IMPORTANCE_HIGH;
            CharSequence notification="Estetica";
            String descripcion="Nueva estética registrada. ¡Verificala!...";


            NotificationChannel chanel= new NotificationChannel(chanelId, notification,importance);
            chanel.setDescription(descripcion);
            chanel.enableLights(true);
            chanel.setLightColor(Color.MAGENTA);
            chanel.enableVibration(true);
            chanel.setVibrationPattern(new long[]{100,100,100,100,100,100});
            manager.createNotificationChannel(chanel);
            notificador= new NotificationCompat.Builder(context,chanelId);
        }


        notificador.setSmallIcon(R.drawable.iconoestetica2)
                .setContentTitle("Estetica Canina").setContentText("Nueva Estética...")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Una nueva estetica se ha registrado, ¡VERIFICALO!")).setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificador.setChannelId(chanelId);
                manager.notify(1,notificador.build());
                       /* .setSmallIcon(R.drawable.iconoestetica2)
                        .setContentTitle("Estetica Canina").setContentText("Nueva Estética...")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Una nueva estetica se ha registrado, ¡VERIFICAlO!")).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificador.setVibrate(new long[]{1000});
        notificador.setLights(Color.GREEN,1000,1000).setAutoCancel(true);//.setContentIntent(pendingIntent).setAutoCancel(true);*/

        /*NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0,notificador.build());*/


    }

   /* private void setPendingIntent() {
        Intent intent= new Intent(context,ServiciosHoyFragment.class);
        TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(ServiciosHoyFragment.class);
        taskStackBuilder.addNextIntent(intent);
        pendingIntent=taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

    }*/
}