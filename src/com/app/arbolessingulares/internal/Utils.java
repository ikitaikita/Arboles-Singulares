package com.app.arbolessingulares.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.app.arbolessingulares.modelo.Arbol;

import android.util.Log;

public class Utils {
	
	private static final String urltodos = "http://www.esunescandalo.com/apparboles/datos.class.php?tipo=mostrar_arboles";
	private static final String urlComun = "http://www.esunescandalo.com/apparboles/datos.class.php?tipo=mostrar_comun_arboles";
	private static final String urlProvincia ="http://www.esunescandalo.com/apparboles/datos.class.php?tipo=mostrar_provincia_arboles";
	private static final String urlfiltros ="http://www.esunescandalo.com/apparboles/datos.class.php?tipo=mostrar_filtro_arboles";
	
	public static List<Arbol> obtenerListaTodosArboles()
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
		 Log.i("entro obtenerListaTodosArboles","obtenerListaTodosArboles");
		 
		
		 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
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
				   // res.setRatings_average("0");
				   
				    lista_arboles.add(res);
				    
				    }
		

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_arboles;
	}
	
	public static List<Arbol> obtenerListaTodosArbolesComun(String filtrocomun)
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
		
		 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
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
	
	public static List<Arbol> obtenerListaTodosArbolesProvincia(String filtroprovincia)
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
		
		 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
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
				   // res.setRatings_average("0");
				   
				    lista_arboles.add(res);
				    
				    }
		

		   }catch (Exception e){
			   e.printStackTrace();
		   }

		 
		   return lista_arboles;
	}
	public static List<Arbol> obtenerListaTodosArboles2Filtros(String filtroprovincia, String filtrocomun)
	{
		 ArrayList<Arbol> lista_arboles = new ArrayList<Arbol>();
		 
		
		 String idArbol, nombre,descripcion,foto,provincia,tmunicipal,latitud,longitud,direccion,especie,altura,diametro,edad,comun="";

		
		
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
		

	public static ArrayList<String> obtenerIdsListaEntrada(String listaEntrada)
	
	{
		ArrayList <String> lista_ids = new ArrayList<String>();
		String[] arrayDatos = listaEntrada.split("\"");
		for (int i = 0; i < arrayDatos.length; i++) {
			//Log.i("dato id: ",arrayDatos[i] );
			if(isNumeric(arrayDatos[i])) 
				{
				
				Log.i("esnumerico:", "Si");
				lista_ids.add(arrayDatos[i]);
				}
			
		}
		return lista_ids;
	}
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	public static ArrayList<Arbol> listaFinalFiltrada(List<Arbol> lista_cercanos, String filtro)
	{
		//Log.i("lista_cercanos dentro de listaFinal: ", String.valueOf(lista_cercanos.size()));
		//Log.i("filtro en listaFinalFiltrada: ", filtro);
		ArrayList<Arbol> m_arbolesFinal = new ArrayList<Arbol>();
		for(int i=0; i<lista_cercanos.size(); i++)
		{
			//Log.i("nombre restaurante: ",lista_cercanos.get(i).getName());
			
			if(lista_cercanos.get(i).getComun()!=null)
			{
				//Log.i("lista_cercanos.get(i).getLista_cocina(): ",lista_cercanos.get(i).getLista_cocina());
				int pos = lista_cercanos.get(i).getComun().lastIndexOf(filtro);
				//Log.i("pppoooooooooooos: ", String.valueOf(pos));
				if(pos!=-1)
					{
					//Log.i("esDistinto de -1", "esDistinto de -1");
					m_arbolesFinal.add(lista_cercanos.get(i));
					}
			}
			
			//if(lista_cercanos.get(i).getLista_cocina().contains(filtro)) m_restaurantesFinal.add(lista_cercanos.get(i));
		}
		//Log.i("m_restaurantesFinal size: ", String.valueOf(m_restaurantesFinal.size()));
		return m_arbolesFinal;
		
	}

}
