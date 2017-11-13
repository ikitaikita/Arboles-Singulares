package com.app.arbolessingulares.controlador;

import com.app.arbolessingulares.internal.Constants;
import com.app.arbolessingulares.internal.ImageThreadLoader;
import com.app.arbolessingulares.internal.PostImageLoadedListener;
import com.app.arbolessingulares.internal.Utils;
import com.app.arbolessingulares.modelo.Arbol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.StrictMode;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.ListView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class FiltrarArboles extends ListActivity implements OnClickListener  {
 //private String jsonResult;
 private ListView lv;
 //private ArrayList<Restaurante> restaurantesAvaiable;
 private Handler handler = new Handler(new ResultMessageCallback());
 private String filtrocomun="";
 private String filtroprovincia="";
 private ProgressDialog pDialog;
 //private Button atras;
 private Spinner spinnerFiltro,spinnerFiltroProvincia;
 private Button buttonFiltrar;
 //private AsyncTaskDialog task;
 public final int MESSAGE_ERROR = -1;
	public final int MESSAGE_OK = 1;
 private List<Arbol> m_arboles = null;
private ArrayList<Arbol> m_arbolesAux = null;

ImageThreadLoader imageLoader = new ImageThreadLoader();


 /*private static final String urltodos = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_arboles";
 private static final String urlComun = "http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_comun_arboles";
 private static final String urlProvincia ="http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_provincia_arboles";
 private static final String urlfiltros ="http://192.254.226.126/apparboles/datos.class.php?tipo=mostrar_filtro_arboles";*/
 private static final int TWO_MINUTES = 1000 * 60 * 2;
//*****Posicion GPS*****//

	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;

  
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
 
  setContentView(R.layout.activity_traer_filtro);
  
  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
  StrictMode.setThreadPolicy(policy);
  
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
	//spinnerTipos.setActivated(false);
  spinnerFiltroProvincia.setAdapter(adapter2);
//Localizacion
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
			Log.i("escogido comun:" ,escogido);
			if(position==0) 
			   {				 
				//mostrar("todos","todos");
				filtrocomun="todos";
				
					
				}
			else filtrocomun = escogido;
			//mostrar(escogido,"");
		
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
	 
	   
	 
	 	
		
		
		//task = new AsyncTaskDialog();
		//task.execute();
		lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//int fixedpos = arg2 - 1;
				int fixedpos = arg2;
				
				
				Intent intent= new Intent(FiltrarArboles.this,DetailArbolActivity.class); 
				
				intent.putExtra("arbol", m_arboles.get(fixedpos));
                startActivity(intent);
                               
			}
		});

 // atras = (Button)findViewById(R.id.atras);

  /*atras.setOnClickListener(new View.OnClickListener(){		      
	     public void onClick(View view) {
	         finish();   	 
	     }
	 });*/

 // url = "http://" + IP + "/appcomecamino/datos.class.php?tipo=mostrar_restaurantes";
  
 

 
 } 
 
 private void mostrar(String tipofiltro, String fprovincia)
 {
	 filtrocomun = tipofiltro;
	 filtroprovincia = fprovincia;
	
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
						m_arboles =( ArrayList<Arbol>)Utils.obtenerListaTodosArboles2Filtros(filtroprovincia,filtrocomun);
					}
				//{
					//m_arboles =( ArrayList<Arbol>)obtenerListaTodosArbolesComun();
					
					//obtenerDatosRestaurantes(m_arboles);
					
					//m_arbolesAux = Utils.listaFinalFiltrada(m_arboles, filtro); 
					//m_arboles= m_arbolesAux;
					//Log.i("se han obtenido todos los restaurantes, total: ",String.valueOf(m_restaurantes.size()) );
				//}
				

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
				Toast.makeText(FiltrarArboles.this,
						getString(R.string.ErrorCarga), Toast.LENGTH_LONG)
						.show();
				break;
			case  MESSAGE_OK:
			
				setListAdapter(new Adapter(FiltrarArboles.this,R.layout.lista_item, m_arboles));			
				break;

			}

			return true; // lo marcamos como procesado
		}
	}
	


/*private List<Arbol> obtenerListaTodosArboles()
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
			    res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			    
			   
			    
			
			    
			   
			    //m_restaurantes.add(res);
			    }
	

	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}*/
/*private List<Arbol> obtenerListaTodosArbolesComun()
{
	 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
	 
	
	 String idArbol, nombre,descripcion,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(urlComun+"&filtro="+ filtrocomun);
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
			    res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			    
			   
			    
			
			    
			   
			    //m_restaurantes.add(res);
			    }
	

	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}*/	

/*private List<Arbol> obtenerListaTodosArbolesProvincia()
{
	 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
	 
	
	 String idArbol, nombre,descripcion,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(urlProvincia+"&filtro="+ filtroprovincia);
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
			    res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			    
			   
			    
			
			    
			   
			    //m_restaurantes.add(res);
			    }
	

	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}*/	
/*private List<Arbol> obtenerListaTodosArboles2Filtros()
{
	 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
	 
	
	 String idArbol, nombre,descripcion,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

	
	
	 try {
		   
		   
		   HttpGet httpGet = new HttpGet(urlfiltros+"&filtrop="+filtroprovincia+"&filtroc="+filtrocomun);
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
			    res.setRatings_average("0");
			   
			    lista_arboles.add(res);
			    
			    
			   
			    
			
			    
			   
			    //m_restaurantes.add(res);
			    }
	

	   }catch (Exception e){
		   e.printStackTrace();
	   }

	 
	   return lista_arboles;
}	*/
	
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
				
				
				
				final TextView nom = (TextView) v.findViewById(R.id.nombreArbol);
				nom.setTextSize(20);

				final TextView prov = (TextView) v.findViewById(R.id.provinciaArbol);
				final TextView comun = (TextView) v.findViewById(R.id.comun);
				final TextView altura = (TextView) v.findViewById(R.id.alturaArbol);
				nom.setText(p.getNombre());
				prov.setText(p.getProvincia());
				comun.setText(p.getComun());
				altura.setText(p.getAltura()+" m");
				final ImageView imagen = (ImageView) v.findViewById(R.id.image);
				
				if(p.getFoto().contains("jpg"))
				{
					
					
					Bitmap bmp;
                    try {
                        String urlpic = Constants.URIFROMSERVER+ p.getFoto();
                        Log.i("urlpic: ",urlpic);
                        bmp = BitmapFactory.decodeStream(new java.net.URL(urlpic).openStream());
                        if(bmp!=null)imagen.setImageBitmap(bmp);
                        else
                        {
                        	imagen.setBackgroundResource(R.drawable.button_photo);
                        }
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                    
//					Log.i("CONTIENE JPG*********************",p.getFoto() );
//					int id = getResources().getIdentifier("button_photo", "drawable", getPackageName());						      
//			    	 imagen.setImageResource(id);
				}else 
					imagen.setBackgroundResource(R.drawable.button_photo);
					//imagen.setImageBitmap(null);
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

	private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
       } catch (IOException e) {
           Log.i("", "Error getting bitmap", e);
       }
       return bm;
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
