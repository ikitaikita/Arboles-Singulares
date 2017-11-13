package com.app.arbolessingulares.controlador;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.arbolessingulares.internal.Constants;
import com.app.arbolessingulares.internal.ImageThreadLoader;
import com.app.arbolessingulares.internal.PostImageLoadedListener;
import com.app.arbolessingulares.internal.Utils;
import com.app.arbolessingulares.modelo.Arbol;

 
public class FiltrarArbolesCercanos extends ListActivity {
 //private String jsonResult;
 private ListView lv;
 //private ArrayList<Restaurante> restaurantesAvaiable;
 private Handler handler = new Handler(new ResultMessageCallback());

 private ProgressDialog pDialog;
// private Button atras;
 private Spinner spinnerFiltroCercanos;
 private String filtro ="";

 //private AsyncTaskDialog task;
 public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
 private List<Arbol> m_arboles = null;
 private ArrayList<Arbol> m_arbolesAux = null;
 ImageThreadLoader imageLoader = new ImageThreadLoader();


 //private static final String urlconfiltro = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_restauranteXFiltro";
 //private static final String url2 = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";
 private static final String url = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_arboles";
 private static final String urlComun = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_comun_arboles";
 private static final int TWO_MINUTES = 1000 * 60 * 2;
//*****Posicion GPS*****//

	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;

  
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
 
  setContentView(R.layout.activity_traer_filtro_cercanos);
  spinnerFiltroCercanos = (Spinner)findViewById(R.id.spinnerFiltroCercanos) ;
  ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.filtro_arrays, android.R.layout.simple_spinner_item);
  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

  spinnerFiltroCercanos.setAdapter(adapter);
//Localizacion
  mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	CustomLocationListener customLocationListener = new CustomLocationListener();
	if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
  	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
  	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
  }
	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	
	spinnerFiltroCercanos.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
										
						
			//	Toast.makeText(parent.getContext(),"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),	Toast.LENGTH_SHORT).show();
				String escogido =  parent.getItemAtPosition(position).toString();
				
			    if(position==0) 
			    {				 
				mostrar("todos");		
				
					
				}
			    else mostrar(escogido);
				/*if(position==1) 
				{				 
				mostrar("Abedul");		
				
					
				}
				if(position==2) 
				{
				mostrar("Abeto");
					
				}
	
				if(position==3) 
				{
				mostrar("Acebo");
				
				}
				if(position==4) 
				{
				    mostrar("Alcornoque");
				
				}
				if(position==5) 
				{
				    mostrar("Aliso");
				
				}
				if(position==6) 
				{
				    mostrar("Arce");
				
				}
				if(position==7) 
				{
				    mostrar("Castaño");
				
				}
				if(position==8) 
				{
				    mostrar("Cedro");
				
				}
				if(position==9) 
				{
				    mostrar("Chopo");
				
				}
				if(position==10) 
				{
				    mostrar("Cipres");
				
				}
				if(position==11) 
				{
				    mostrar("Encina");
				
				}
				if(position==12) 
				{
				    mostrar("Enebro");
				
				}
				if(position==13) 
				{
				    mostrar("Espino");
				
				}
				if(position==14) 
				{
				    mostrar("Fresno");
				
				}
				if(position==15) 
				{
				    mostrar("Haya");
				
				}
				if(position==16) 
				{
				    mostrar("Madroño");
				
				}
				if(position==17) 
				{
				    mostrar("Moral");
				
				}
				if(position==18) 
				{
				    mostrar("Nogal");
				
				}
				if(position==19) 
				{
				    mostrar("Olmo");
				
				}
				
				if(position==20) 
				{
				    mostrar("Pino");
				
				}
				if(position==21) 
				{
				    mostrar("Pinsapo");
				
				}
				if(position==22) 
				{
				    mostrar("Roble");
				
				}
				if(position==23) 
				{
				    mostrar("Sauce");
				
				}
				if(position==24) 
				{
				    mostrar("Sequoia");
				
				}*/
					}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				mostrar("todos");
			}
			
		});
	 

		lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//int fixedpos = arg2 - 1;
				int fixedpos = arg2;
				
				
				Intent intent= new Intent(FiltrarArbolesCercanos.this,DetailArbolActivity.class); 
				
				intent.putExtra("arbol", m_arboles.get(fixedpos));
                startActivity(intent);
                               
			}
		});


 

 
 } 
 
 private void mostrar(String tipofiltro)
 {
	 filtro = tipofiltro;
	
 	 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoArboles));
	  
		
		 Thread thread = new Thread(new LoadTapentosWorker());
		 thread.start();
 }
 
 /**
	 * actualiza la localizacion
	 * implementa LocationListener
	 * @see android.location.LocationListener
	 */


		private class CustomLocationListener implements LocationListener{

			  public void onLocationChanged(Location argLocation) {
				
				  m_DeviceLocation = argLocation;
				  mLocationManager.removeUpdates(this);
			  }

			  public void onProviderDisabled(String provider) {}

			  public void onProviderEnabled(String provider) {}

			  public void onStatusChanged(String provider,
			    int status, Bundle extras) {}
		 }
	 
	/**
	 * Carga las Tapas y eventos en background. Implementa Runnable
	 * @see java.lang.Runnable
	 * 
	 */
	private class LoadTapentosWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				Thread.sleep(1000);
				if (filtro.equals("todos"))
				{
					double lati= 42.5932681;
					double lon= -5.5625113999;
					
					m_arbolesAux =( ArrayList<Arbol>)obtenerLista();
					m_arboles = (ArrayList<Arbol>)obtenerCercanos(m_arbolesAux,m_DeviceLocation.getLatitude(),m_DeviceLocation.getLongitude() );
					
					
					
				}else 
				{
					m_arbolesAux =( ArrayList<Arbol>)obtenerListaComun();
					m_arboles = (ArrayList<Arbol>)obtenerCercanos(m_arbolesAux,m_DeviceLocation.getLatitude(),m_DeviceLocation.getLongitude() );
					
			
					m_arbolesAux = Utils.listaFinalFiltrada(m_arboles, filtro); 
					m_arboles= m_arbolesAux;
					
				}
				
				
				//m_restaurantes = (ArrayList<Restaurante>)obtenerCercanos(m_restaurantesAux,lati,lon );
				//lista_ids_cercanos = obtenerIdsCercanos(m_restaurantesCercanos);
				
				

			} catch (Exception e) {
				messageReturn =  MESSAGE_ERROR;
			
			}

			handler.sendEmptyMessage(messageReturn);
		}
	}
	
	/**
	 * Metodo resultCalback LoadTapentosWorker
	 */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(FiltrarArbolesCercanos.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case  MESSAGE_OK:
			
				setListAdapter(new Adapter(FiltrarArbolesCercanos.this,R.layout.lista_item, m_arboles));			
				break;

			}

			return true; // lo marcamos como procesado
		}
	}
	
/*private ArrayList<Integer> obtenerIdsCercanos(ArrayList <Restaurante> lista_restaurantes)
{
	ArrayList<Integer> lista_ids = new ArrayList<Integer>();
	for (int i=0; i < lista_restaurantes.size(); i++ )
	{
		
		lista_ids.add(Integer.valueOf(lista_restaurantes.get(i).getID()));
	}
	return null;
}*/
	

private ArrayList<Arbol> obtenerCercanos (ArrayList <Arbol> lista_restaurantes, double milat, double milon)
{
	ArrayList<Arbol> lista_cercanos = new ArrayList<Arbol>();
	double distancia ;
	
	
	Iterator<Arbol> it = lista_restaurantes.iterator(); 
	while ( it.hasNext() ) { 
		Arbol rest = it.next(); 
		
		distancia = getDistancia(rest.getLatitud(), rest.getLongitud(), milat, milon);
		Log.i("distancia calculada en metros: ",String.valueOf(distancia));
		if(distancia<50)
		{
			rest.setDistancia(String.valueOf(redondear(distancia)));
			lista_cercanos.add(rest);
		}
		
		} 

	
	return lista_cercanos;
}

private double getDistancia(String latRestS, String lonRestS, double miLat, double miLong)
{
	/*
	 * función a la que le pasamos la latitud y longitud del restaurante y  la latitud y longitud de nuestra posicion y nos devolverá una distancia en metros. 
	 */
	
	double latRest= Double.parseDouble(latRestS);
	double lonRest = Double.parseDouble(lonRestS);
	
	double earthRadius = 6371; //kilometros
	double dLat = Math.toRadians(latRest-miLat);
	double dLon = Math.toRadians(lonRest-miLong);
	double sindLat = Math.sin(dLat / 2);  
    double sindLng = Math.sin(dLon / 2);  
    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
            * Math.cos(Math.toRadians(miLat)) * Math.cos(Math.toRadians(latRest)); 
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));  
  
    double dist = earthRadius * c;  
	
	return dist;

	 
}

private double redondear(double numero)
{
       return Math.rint(numero*100)/100;
}

	
private List<Arbol> obtenerLista()
{
	 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
	 
	 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";
	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(url);
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
		   HttpEntity entity = response.getEntity();
		   BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
		   InputStream iStream = buffer.getContent();
		                    
		   String aux = "";
		            
		   BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
		   new StringBuilder();
		   String line;
		   while ((line = r.readLine()) != null) {
		     aux += line;
		   }
		   JSONObject jsonObject = new JSONObject(aux);
		   JSONArray arboles = jsonObject.getJSONArray("datos");
		  
		   for(int i = 0; i < arboles.length(); i++) {
			   JSONObject arboles_datos = arboles.getJSONObject(i);
			   idArbol = arboles_datos.getString("ID");
			    nombre = arboles_datos.getString("nombre");
			    descripcion = arboles_datos.getString("descripcion");
			    foto = arboles_datos.getString("foto");
			    provincia = arboles_datos.getString("provincia");
			    tmunicipal = arboles_datos.getString("tmunicipal");
			    latitud= arboles_datos.getString("latitud");
			    longitud= arboles_datos.getString("longitud");
			    direccion= arboles_datos.getString("direccion");
			    especie= arboles_datos.getString("especie");
			    altura= arboles_datos.getString("altura");
			    diametro= arboles_datos.getString("diametro");
			    edad= arboles_datos.getString("edad");
			    comun= arboles_datos.getString("comun");
			    
			    Arbol res = new Arbol(idArbol);
			    res.setNombre(nombre);
			    res.setDescripcion(descripcion);
			    res.setFoto(foto);
			    res.setProvincia(provincia);
			    res.setTmunicipal(tmunicipal);
			    res.setLatitud(latitud);
			    res.setLongitud(longitud);
			    res.setDireccion(direccion);
			    res.setEspecie(especie);
			    res.setAltura(altura);
			    res.setDiametro(diametro);
			    res.setEdad(edad);
			    res.setComun(comun);
			    //res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			 
			    
			    }
		  
	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}

private List<Arbol> obtenerListaComun()
{
	 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
	 
	 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";
	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(urlComun+"&filtro="+ filtro);
		   HttpClient httpClient = new DefaultHttpClient();
		   HttpResponse response = (HttpResponse)httpClient.execute(httpGet);
		   HttpEntity entity = response.getEntity();
		   BufferedHttpEntity buffer = new BufferedHttpEntity(entity);
		   InputStream iStream = buffer.getContent();
		                    
		   String aux = "";
		            
		   BufferedReader r = new BufferedReader(new InputStreamReader(iStream));
		   new StringBuilder();
		   String line;
		   while ((line = r.readLine()) != null) {
		     aux += line;
		   }
		   JSONObject jsonObject = new JSONObject(aux);
		   JSONArray arboles = jsonObject.getJSONArray("datos_filtro");
		  
		   for(int i = 0; i < arboles.length(); i++) {
			   JSONObject arboles_datos = arboles.getJSONObject(i);
			   idArbol = arboles_datos.getString("ID");
			    nombre = arboles_datos.getString("nombre");
			    descripcion = arboles_datos.getString("descripcion");
			    foto = arboles_datos.getString("foto");
			    provincia = arboles_datos.getString("provincia");
			    tmunicipal = arboles_datos.getString("tmunicipal");
			    latitud= arboles_datos.getString("latitud");
			    longitud= arboles_datos.getString("longitud");
			    direccion= arboles_datos.getString("direccion");
			    especie= arboles_datos.getString("especie");
			    altura= arboles_datos.getString("altura");
			    diametro= arboles_datos.getString("diametro");
			    edad= arboles_datos.getString("edad");
			    comun= arboles_datos.getString("comun");
			    
			    Arbol res = new Arbol(idArbol);
			    res.setNombre(nombre);
			    res.setDescripcion(descripcion);
			    res.setFoto(foto);
			    res.setProvincia(provincia);
			    res.setTmunicipal(tmunicipal);
			    res.setLatitud(latitud);
			    res.setLongitud(longitud);
			    res.setDireccion(direccion);
			    res.setEspecie(especie);
			    res.setAltura(altura);
			    res.setDiametro(diametro);
			    res.setEdad(edad);
			    res.setComun(comun);
			  //  res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			 
			    
			    }
		  
	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}


	
private class Adapter extends ArrayAdapter<Arbol> {

	private ArrayList<Arbol> items;
	//private DecimalFormat df = new DecimalFormat("0.00"); 

	public Adapter(Context context, int textViewResourceId,
			List<Arbol> items) {
		super(context, textViewResourceId, items);
		this.items = (ArrayList<Arbol>) items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.lista_item, null);
		}

		final Arbol p = items.get(position);
	
		if (p != null) {
			
			final ImageView imagen = (ImageView) v.findViewById(R.id.image);
			final TextView nom = (TextView) v.findViewById(R.id.nombreArbol);
			nom.setTextSize(20);

			final TextView prov = (TextView) v.findViewById(R.id.provinciaArbol);
			final TextView comun = (TextView) v.findViewById(R.id.comun);
			final TextView altura = (TextView) v.findViewById(R.id.alturaArbol);
			nom.setText(p.getNombre());
			prov.setText(p.getProvincia());
			comun.setText(p.getComun());
			altura.setText(p.getAltura()+" m");
			if(!p.getDistancia().equals("0"))
			{
				final TextView tdist = (TextView) v.findViewById(R.id.textdistancia);
				final TextView dist = (TextView) v.findViewById(R.id.distanciaArbol);
				tdist.setText("Distancia");
				dist.setText(p.getDistancia()+" km");
			}
			if(p.getFoto().contains("jpg"))
			{
				Log.i("CONTIENE JPG*********************",p.getFoto() );
				int id = getResources().getIdentifier("button_photo", "drawable", getPackageName());						      
		    	 imagen.setImageResource(id);
			}else imagen.setImageBitmap(null);
			/*if (imagen != null) {			
				
				imagen.setImageBitmap(null);
				Bitmap cachedImage = null;
				try {

					PostImageLoadedListener pill = new PostImageLoadedListener(imagen);
					
					//cachedImage = downloadImagen(p.getUriFoto());
					String uri_imagen = Constants.URIFROMSERVER+ p.getFoto();
					//Log.i("uri_imagen ***************", uri_imagen);
					
					cachedImage = imageLoader.loadImage(uri_imagen,pill);
					//cachedImage = imageLoader.loadImage(topoos.Images.Operations.GetImageURIThumb(p.getName(),topoos.Images.Operations.SIZE_SMALL),	pill);

					if (cachedImage != null ) {
						Log.i("HAY FOTO*****: ",p.getNombre());
						imagen.setImageBitmap(cachedImage);
						
					}
					

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
		            e.printStackTrace();
					
				}
			}*/


			

		}

		return v;

	}

}
 
	private void actualizaMejorLocaliz(Location localiz) {
	       if (m_DeviceLocation == null
	                    || localiz.getAccuracy() < 2*m_DeviceLocation.getAccuracy()
	                    || localiz.getTime() - m_DeviceLocation.getTime() > TWO_MINUTES) {
	            
	    	   m_DeviceLocation = localiz;
	             
	       }
	}
 

 }
