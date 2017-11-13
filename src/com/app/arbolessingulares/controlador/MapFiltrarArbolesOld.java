package com.app.arbolessingulares.controlador;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.app.arbolessingulares.internal.Constants;
import com.app.arbolessingulares.internal.Utils;
import com.app.arbolessingulares.modelo.Arbol;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Contenido de la actividad Pestaña 3 del menú que realiza la visualizacion en el mapa de los arboles singulares en Castilla y Leon
 * Extiende de FragmentActivity e implementa el interfaz OnMarkerClickListener 
 * @see com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
 * @version 1.0
 * @author Victoria Marcos
 */
public class MapFiltrarArbolesOld extends android.support.v4.app.FragmentActivity implements OnMarkerClickListener{
	
	final int RQS_GooglePlayServices = 1;
	

	//private ImageThreadLoader imageLoader = new ImageThreadLoader();


	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;

	private LocationManager mLocationManager;
	private Location m_DeviceLocation = null;
	 private Spinner spinnerFiltro;
	private SupportMapFragment fm;
	
	private GoogleMap mMap = null;
	private Handler handler = new Handler(new ResultMessageCallback());

	private String filtro="";
	private ProgressDialog pDialog = null;
	private List<Arbol> m_arboles = null;
	private ArrayList<Arbol> m_arbolesAux = null;
    
	//private static final String url2 = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";
	 private static final String urltodos = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_arboles";
	 private static final String urlComun = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_comun_arboles";
	//private List<POI> m_pois = null;

		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide titlebar of application must be before setting the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.pestana3);		
		  spinnerFiltro = (Spinner)findViewById(R.id.spinnerFiltro) ;
		  ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.filtro_arrays, android.R.layout.simple_spinner_item);
		  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
			//spinnerTipos.setActivated(false);
		  spinnerFiltro.setAdapter(adapter);
		


		//obtenemos mapa
		fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview); 
	    mMap = fm.getMap();
	    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //establecemos tipo de mapa       
	    mMap.setMyLocationEnabled(true);

	    mMap.getUiSettings().setZoomControlsEnabled(true);
	    mMap.getUiSettings().setCompassEnabled(true);


        mMap.setOnMarkerClickListener(this);

        mMap.setInfoWindowAdapter(new InfoWindowAdapter(){ //adaptador de la ventana que sale al pulsar el marcador

			@Override
			public View getInfoContents(Marker marker) {
				// TODO Auto-generated method stub
				View popup =getLayoutInflater().inflate(R.layout.popup_layout, null);

				//ImageView imagen = (ImageView)popup.findViewById(R.id.image);
					
				
				
				TextView nombre=(TextView)popup.findViewById(R.id.title);
				
				TextView cientifico=(TextView)popup.findViewById(R.id.cientificoArbol);
				TextView comun=(TextView)popup.findViewById(R.id.comunArbol);
				TextView provincia=(TextView)popup.findViewById(R.id.provArbol);
				TextView municipal=(TextView)popup.findViewById(R.id.municipalArbol);
				TextView altura=(TextView)popup.findViewById(R.id.alturaArbol);
				TextView diametro=(TextView)popup.findViewById(R.id.diamArbol);
				TextView edad=(TextView)popup.findViewById(R.id.edadArbol);
				Arbol res = m_arboles.get(Integer.parseInt(marker
                        .getSnippet()));
				
				
				nombre.setText(marker.getTitle());
				cientifico.setText("Nombre Cientifico: " + res.getEspecie());
				comun.setText("Nombre Comun: " + res.getComun());	
				provincia.setText("Provincia: " + res.getProvincia());
				municipal.setText("Termino Municipal: " +res.getTmunicipal());
				altura.setText("Altura cms: " +res.getAltura());
				diametro.setText("Diametro cms: " +res.getDiametro());
				if(!res.getEdad().equals(""))
				{
					edad.setText("Edad años: " +res.getEdad());
				}
			    
			
				return popup;
			}

			@Override
			public View getInfoWindow(Marker arg0) {
				
				return null;
			}
        	
        }       
        
        );
    
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		CustomLocationListener customLocationListener = new CustomLocationListener();
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
        	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
		m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 spinnerFiltro.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
											
							
					//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
					String escogido =  parent.getItemAtPosition(position).toString();
				if(position==0) 
				   {				 
					mostrar("todos");		
					
						
					}
				else mostrar(escogido);
				
				/*if(position==1) 
					{				 
					mostrar("celiacos");		
					
						
					}
				if(position==2) 
					{
					mostrar("vegana");
					
					}
			
				if(position==3) 
				{
				    mostrar("vegetariana");
				
				}
				if(position==4) 
				{
				    mostrar("tradicional");
				
				}*/
		
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					mostrar("todos");
				}
				
			});
		//pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
	
		//Thread thread = new Thread(new LoadTapentosWorker());
		// thread.start();

		
	}


	private void mostrar(String tipofiltro)
	 {
		 filtro = tipofiltro;
		
	 	 pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoArboles));
		  
			
			 Thread thread = new Thread(new LoadTapentosWorker());
			 thread.start();
	 }


	/**
	 * clase privada para la gestion y actualizacion de la localizacion
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

	

	
	 @Override
	 protected void onResume() {
	
	  super.onResume();

	  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
	  
	  if (resultCode == ConnectionResult.SUCCESS){
	   Toast.makeText(getApplicationContext(), 
	     "isGooglePlayServicesAvailable SUCCESS", 
	     Toast.LENGTH_LONG).show();
	  }else{
	   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
	  }
	  
	 }

	    @Override
	    protected void onPause() {
	        fm.onPause();
	        super.onPause();
	    }

	   /* @Override
	    protected void onDestroy() {
	        fm.onDestroy();
	        super.onDestroy();
	    }*/

	    @Override
	    public void onLowMemory() {
	        super.onLowMemory();
	        fm.onLowMemory();
	    }


	


	/**
	 * Añade los marcadores de las tapas y eventos en el mapa después de cargarlas
	 */
	private void drawTapas() {
        
		mMap.clear();
		int i = 0;
		
		if (m_arboles != null) {
			for (final Arbol p : m_arboles) {
				
				LatLng markerPosition = new LatLng (Double.parseDouble(p.getLatitud()) ,Double.parseDouble(p.getLongitud()));	
				
		
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition,5));				
				String descrip =""; //para la descripcion de la tapa y/o evento
				String id = p.getID();
				//descrip = p.getGuid();
				
				//añade el marcador al mapa
				mMap.addMarker(new MarkerOptions()			        
			        .position(markerPosition)
			        .title(p.getNombre())
			        .snippet(""+i)
			        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_verde))
			        .anchor(0.5f, 0.5f));
				i++;
				
			}
		}
		
	}
	

	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();

			switch (arg0.what) {
			case MESSAGE_ERROR:
				Toast.makeText(MapFiltrarArbolesOld.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case MESSAGE_OK:
				//Log.i("drawTapas:", "drawTapas");
				drawTapas();
				break;
			}

			return true; // lo marcamos como procesado
		}
	}




	/**
	 * clase privada para la carga de tapas y eventos en segundo plano o background
	 * implementa Runnable
	 * @see  java.lang.Runnable
	 */
	private class LoadTapentosWorker implements Runnable {

		public void run() {

			int messageReturn = MESSAGE_OK;

			try {
				Thread.sleep(1000);
				if (filtro.equals("todos"))
				{
					
					m_arboles =( ArrayList<Arbol>)obtenerListaTodosArboles();
					//Log.i("m_restaurantes size",String.valueOf(m_restaurantes.size()));
				
					//obtenerDatosRestaurantes(m_arboles);
					//Log.i("despues de obtener datos m_restaurantes size",String.valueOf(m_restaurantes.size()));
					//Log.i("se han obtenido todos los restaurantes, total: ",String.valueOf(m_restaurantes.size()) );
				}else 
				{
					
					m_arboles =( ArrayList<Arbol>)obtenerListaTodosArbolesComun();
					
					//obtenerDatosRestaurantes(m_arboles);
					
					m_arbolesAux = Utils.listaFinalFiltrada(m_arboles, filtro); 
					m_arboles= m_arbolesAux;
					Log.i("se han obtenido todos los arboles despues de la seleccion, total: ",String.valueOf(m_arboles.size()) );
				}
				
				


			} catch (Exception e) {
				messageReturn =  MESSAGE_ERROR;
				Log.e("Upload", e.getMessage());
			}
           
			handler.sendEmptyMessage(messageReturn);
		}
	}


	
	private List<Arbol> obtenerListaTodosArboles()
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
			
		 String idArbol, nombre,descripcion,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
		 try {
			   
			   
			   HttpGet httpGet = new HttpGet(urltodos);
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
				    
				    
				   
				    
				
				    
				   
				    //m_restaurantes.add(res);
				    }
		

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_arboles;
	}
	private List<Arbol> obtenerListaTodosArbolesComun()
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
		
		 String idArbol, nombre,descripcion,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
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
				    
				    
				   
				    
				
				    
				   
				    //m_restaurantes.add(res);
				    }
		

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_arboles;
	}	

	@Override
	public boolean onMarkerClick(Marker marcador) {

	  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador.getPosition(),9));
      marcador.showInfoWindow();
      
	  return true;
	}
	

}


