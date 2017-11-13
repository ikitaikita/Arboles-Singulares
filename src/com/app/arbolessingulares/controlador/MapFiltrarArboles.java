package com.app.arbolessingulares.controlador;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


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
public class MapFiltrarArboles extends android.support.v4.app.FragmentActivity implements OnMarkerClickListener,OnClickListener{
	
	final int RQS_GooglePlayServices = 1;
	

	//private ImageThreadLoader imageLoader = new ImageThreadLoader();


	public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;

	private LocationManager mLocationManager;
	private Location m_DeviceLocation = null;
	 private Spinner spinnerFiltro,spinnerFiltroProvincia;
	 private Button buttonFiltrar;
	 private String filtrocomun="";
	 private String filtroprovincia="";
	private SupportMapFragment fm;
	
	private GoogleMap mMap = null;
	private Handler handler = new Handler(new ResultMessageCallback());

	private String filtro="";
	private ProgressDialog pDialog = null;
	private List<Arbol> m_arboles = null;
	private ArrayList<Arbol> m_arbolesAux = null;
    
	//private static final String url2 = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";
	// private static final String urltodos = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_arboles";
	// private static final String urlComun = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_comun_arboles";
	//private List<POI> m_pois = null;

		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// hide titlebar of application must be before setting the layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.pestana3);	
		buttonFiltrar = (Button) findViewById(R.id.filtro);
		  buttonFiltrar.setEnabled(true);
		  buttonFiltrar.setOnClickListener(this);
		  spinnerFiltro = (Spinner)findViewById(R.id.spinnerFiltro) ;
		  ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.filtro_arrays, android.R.layout.simple_spinner_item);
		  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
			//spinnerTipos.setActivated(false);
		  spinnerFiltro.setAdapter(adapter);
		  
		  spinnerFiltroProvincia = (Spinner)findViewById(R.id.spinnerFiltroProvincia) ;
		  ArrayAdapter adapter2=ArrayAdapter.createFromResource(this, R.array.provincia_arrays, android.R.layout.simple_spinner_item);
		  adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
		  spinnerFiltroProvincia.setAdapter(adapter2);
		


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
				altura.setText("Altura m: " +res.getAltura());
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
					//mostrar("todos");	
					filtrocomun="todos";
					
						
					}
				else filtrocomun = escogido;
					//mostrar(escogido);
				
	
		
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					mostrar("todos","todos");
				}
				
			});
		 
		 spinnerFiltroProvincia.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
											
							
					//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
				String escogido =  parent.getItemAtPosition(position).toString();
				Log.i("escogido provincia:" ,escogido);
				if(position==0) 
				   {				 
					//mostrar("todos","todos");
					filtroprovincia ="todos";
					
						
					}
				else filtroprovincia = escogido; 
				//mostrar("",escogido);
			
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					mostrar("todos","todos");
				}
				
			});

		
	}


	private void mostrar(String tipofiltro, String fprovincia)
	 {
		 filtrocomun = tipofiltro;
		 Log.i("filtrocomun: ",filtrocomun);
		 filtroprovincia = fprovincia;
		 Log.i("filtroprovincia: ",filtroprovincia);
		
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
				Toast.makeText(MapFiltrarArboles.this,
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
				
				if (filtrocomun.equals("todos") && filtroprovincia.equals("todos"))
				{
					m_arboles =( ArrayList<Arbol>)Utils.obtenerListaTodosArboles();
					Log.i("m_arboles size",String.valueOf(m_arboles.size()));
				
					//obtenerDatosRestaurantes(m_arboles);
					Log.i("despues de obtener datos m_arboles size",String.valueOf(m_arboles.size()));
					//Log.i("se han obtenido todos los restaurantes, total: ",String.valueOf(m_restaurantes.size()) );
				}else 
					if(!filtrocomun.equals("todos") && filtroprovincia.equals("todos"))
					{
						m_arboles =( ArrayList<Arbol>)Utils.obtenerListaTodosArbolesComun(filtrocomun);
					}
					else if(filtrocomun.equals("todos") && !filtroprovincia.equals("todos"))
					{
						m_arboles =( ArrayList<Arbol>)Utils.obtenerListaTodosArbolesProvincia(filtroprovincia);
					}
					else if(!filtrocomun.equals("todos")&&!filtroprovincia.equals("todos"))
					{
						m_arboles =( ArrayList<Arbol>)Utils.obtenerListaTodosArboles2Filtros(filtroprovincia, filtrocomun);
					}
				/*if (filtro.equals("todos"))
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
				}*/
				
				


			} catch (Exception e) {
				messageReturn =  MESSAGE_ERROR;
				Log.e("Upload", e.getMessage());
			}
           
			handler.sendEmptyMessage(messageReturn);
		}
	}




	@Override
	public boolean onMarkerClick(Marker marcador) {

	  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador.getPosition(),9));
      marcador.showInfoWindow();
      
	  return true;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.filtro:
			mostrar(filtrocomun,filtroprovincia);
			break;
		
			
		default:
			break;
		}
		
	}
	

}


