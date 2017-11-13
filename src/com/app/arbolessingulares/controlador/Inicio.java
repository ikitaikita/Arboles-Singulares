package com.app.arbolessingulares.controlador;

import com.app.arbolessingulares.modelo.DatabaseSqlite;





import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Inicio extends Activity {
	private static final int MENU_ABOUT = Menu.FIRST;
	private static final int MENU_EXIT = 2;
	Button Recuperar,Todos,Filtro,salir, FiltroCercanos, MapTodos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		//Recuperar = (Button) findViewById(R.id.buttonCercanos); //cercanos
		//Todos = (Button) findViewById(R.id.buttonTodos); //todos
		Filtro = (Button) findViewById(R.id.buttonFiltro);
		FiltroCercanos = (Button) findViewById(R.id.buttonFiltroCercanos);	
		MapTodos = (Button) findViewById(R.id.buttonMapArboles);	
		//Enviar = (Button) findViewById(R.id.button2);
		salir = (Button) findViewById(R.id.salir);
		
		
		salir.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view) {
		          finish();   	 
		     }
		 });
		
		/*Recuperar.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view){
		    	 Intent cercanos = new Intent(Inicio.this, RestaurantesCercanos.class);
			        startActivity(cercanos);
		     }
		}); */
		
		
		/*Todos.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view){
		    	 Intent recuperarTodos = new Intent(Inicio.this, TodosRestaurantes.class);
			        startActivity(recuperarTodos);
		     }
		});*/
		Filtro.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view){
		    	 Intent recuperarFiltro = new Intent(Inicio.this, FiltrarArboles.class);
			        startActivity(recuperarFiltro);
		     }
		});
		
		FiltroCercanos.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view){
		    	 Intent recuperarFiltroCercanos = new Intent(Inicio.this, FiltrarArbolesCercanos.class);
			        startActivity(recuperarFiltroCercanos);
		     }
		});
		MapTodos.setOnClickListener(new View.OnClickListener(){		      
		     public void onClick(View view){
		    	 Intent recuperarMapaRestaurantes = new Intent(Inicio.this, MapFiltrarArboles.class);
			        startActivity(recuperarMapaRestaurantes);
		     }
		});
		
		
	
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicio, menu);
		menu.add(0, MENU_ABOUT, 0, getString(R.string.about));
		menu.add(0, MENU_EXIT, 1, getString(R.string.exit));
		
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ABOUT:
				AcercaDe();
				return true;
			case MENU_EXIT:
				SalirAplicacion();
				return true;
			
		}
		return false;
	}
	public void AcercaDe(){
		Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);						
	}
	public void SalirAplicacion()
	{
		finish();
	}

	public void mensajeIncluir(){
		Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(400);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"NOTA: Configuración Inicial Realizada con Éxito", Toast.LENGTH_SHORT);
		    toast1.show();    	
	}
	
	public void mensajeNoIncluido(){
		Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(400);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error: Configuración Inicial No Realizada", Toast.LENGTH_SHORT);
		    toast1.show();    	
	}
}
