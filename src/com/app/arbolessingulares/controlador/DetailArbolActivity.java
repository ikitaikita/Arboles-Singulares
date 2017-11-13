package com.app.arbolessingulares.controlador;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.app.arbolessingulares.internal.Constants;
import com.app.arbolessingulares.internal.ImageThreadLoader;
import com.app.arbolessingulares.modelo.Arbol;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailArbolActivity extends Activity implements OnClickListener{
	private static final int WORKER_MSG_OK = 1;
	private static final int WORKER_MSG_ERROR = -1;
	private static final String url = "http://192.254.226.126/appcomecamino/datos.class.php?tipo=mostrar_restauranteX";


	//private ImageThreadLoader imageLoader = new ImageThreadLoader();
	private Handler handler = new Handler(new ResultMessageCallback());
	
	private ImageThreadLoader imageLoader = new ImageThreadLoader();
	//layout
	private ImageView photo;
	private TextView nombre, nombreCientifico, textdedscriptionArbol,description, provArbol, municipalArbol, alturaArbol,diamArbol,edadArbol ;
	private TextView textespecieArbol, textprovArbol,textmunicipalArbol,textalturaArbol,textdiamArbol,textedadArbol   ;
	//Button Menu, bWeb;
	
	
	//private ImageView pointPlus;
	//private ImageView pointLess;
	//private ImageView share;
	private Button share;
	//private TextView nVotesPlus;
	//private TextView nVotesMinus;
	//private RatingBar ratingBar;
	private Activity activity;
	
	private ImageButton gotoMap;
	//private Activity activity;
	private Arbol mArbol;
	
	private float puntos_rating;
	public final int MESSAGE_ERROR = -1;
    public final int MESSAGE_OK = 1;
    private ProgressDialog pDialog;	
	private Location m_DeviceLocation = null;
	private LocationManager mLocationManager;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity = this;
		setContentView(R.layout.detail_arbol_layout);
		photo = (ImageView) findViewById(R.id.tapaPhoto);
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		CustomLocationListener customLocationListener = new CustomLocationListener();
		if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, customLocationListener);
        	m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, customLocationListener);
		m_DeviceLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  
		//pDialog = ProgressDialog.show(this, getString(R.string.INFORMACION), getString(R.string.cargandoRestaurantes));
		
		//mRestaurante= (Restaurante) getIntent().getSerializableExtra("restaurante");
		nombre = (TextView) findViewById(R.id.nombreArbol);
		textespecieArbol = (TextView) findViewById(R.id.textespecieArbol);
		nombreCientifico = (TextView) findViewById(R.id.nombreCientifico);
		textdedscriptionArbol = (TextView) findViewById(R.id.textdedscriptionArbol);
		description = (TextView) findViewById(R.id.descriptionArbol);
		textprovArbol = (TextView) findViewById(R.id.textprovArbol);
		provArbol =(TextView) findViewById(R.id.provArbol);
		textmunicipalArbol = (TextView) findViewById(R.id.textmunicipalArbol);
		municipalArbol = (TextView) findViewById(R.id.municipalArbol);
		textalturaArbol = (TextView) findViewById(R.id.textalturaArbol);
		alturaArbol=(TextView) findViewById(R.id.alturaArbol);
		textdiamArbol = (TextView) findViewById(R.id.textdiamArbol);
		diamArbol =(TextView) findViewById(R.id.diamArbol);
		textedadArbol = (TextView) findViewById(R.id.textedadArbol);
		edadArbol=(TextView) findViewById(R.id.edadArbol);
		
		//Menu = (Button)findViewById(R.id.buttonMenus);
		//bWeb = (Button)findViewById(R.id.buttonWeb);
		//Desayuno = (Button)findViewById(R.id.buttonDesayunos);
		//myWebView = (WebView) this.findViewById(R.id.webView);
		//ratingBar = (RatingBar) findViewById(R.id.ratingRestaurante);
		//puntos_rating=0;
		share = (Button) findViewById(R.id.share);
		share.setEnabled(true);
		//share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		
		description.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {          
		
		
		}
		mArbol= (Arbol) getIntent().getExtras().getSerializable("arbol");
		if(mArbol!=null)
		{
			
			ponerDatosEnView();
		
		}
		gotoMap = (ImageButton) findViewById(R.id.gotoMap);
		gotoMap.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		

			Intent intent = new Intent(getApplicationContext(),
					DetailMapArbolActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("arbol", mArbol);		
			

			startActivity(intent);
			overridePendingTransition(R.anim.scale_from_corner,
					R.anim.scale_to_corner);
			activity.finish();

		}
	});
		
	
	

	
	}
	
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
		
		@SuppressLint("ResourceAsColor")
		private void ponerDatosEnView()
		{
			//asethin = Typeface.createFromAsset(getAssets(),"fonts/Azrael.ttf");
			//nombre.setTypeface(asethin);
			nombre.setTextSize(30);			
			nombre.setText(mArbol.getNombre());
			textespecieArbol.setTextSize(20);
			textespecieArbol.setText("Especie");
			nombreCientifico.setText(mArbol.getEspecie());
			if(!mArbol.getDescripcion().equals(""))
			{
				textdedscriptionArbol.setTextSize(20);
				textdedscriptionArbol.setText("Descripcion");
				description.setText(mArbol.getDescripcion());
			}
			
			//description.setTextSize(20);
			textprovArbol.setTextSize(20);
			textprovArbol.setText("Provincia");
			provArbol.setText(mArbol.getProvincia());
			textmunicipalArbol.setTextSize(20);
			textmunicipalArbol.setText("Termino Municipal");
			municipalArbol.setText(mArbol.getTmunicipal());
			textalturaArbol.setTextSize(20);
			textalturaArbol.setText("Altura m");
			alturaArbol.setText(mArbol.getAltura());
			textdiamArbol.setTextSize(20);
			textdiamArbol.setText("Diametro cm");
			diamArbol.setText(mArbol.getDiametro());
			if(!mArbol.getEdad().equals(""))
			{
				textedadArbol.setTextSize(20);
				textedadArbol.setText("Edad años");
				edadArbol.setText(mArbol.getEdad());
			}
			Bitmap cachedImage = null;
			photo.setImageBitmap(null);
			if(mArbol.getFoto().contains("jpg"))
			{
				
				 
				new ImageDownloaderTaskUser(photo).execute(Constants.URIFROMSERVER+ mArbol.getFoto());
				
				
//				try {
//					
//
//					PostImageLoadedListener pill = new PostImageLoadedListener(
//							photo);
//					String uri_imagen = Constants.URIFROMSERVER+ mArbol.getFoto();
//					cachedImage = imageLoader.loadImage(uri_imagen,pill);
//					//cachedImage = imageLoader.loadImage(topoos.Images.Operations.GetImageURIThumb(mBabyFoto.getUriFoto(),topoos.Images.Operations.SIZE_LARGE), pill);
//					
//					if (cachedImage != null) {
//						photo.setImageBitmap(cachedImage);
//					}
//					
//
//				} catch (Exception e) {
//					
//				}
			
			}
			
			
			
			
		}
		
		
		class ImageDownloaderTaskUser extends AsyncTask<String, Void, Bitmap> {
	        private final WeakReference<ImageView> imageViewReference;

	        public ImageDownloaderTaskUser(ImageView imageView) {
	            imageViewReference = new WeakReference<ImageView>(imageView);
	        }

	        @Override
	        protected Bitmap doInBackground(String... params) {

	            return downloadBitmapUser(params[0]);
	        }

	        @Override
	        protected void onPostExecute(Bitmap bitmap) {
	            if (isCancelled()) {
	                bitmap = null;
	            }

	            if (imageViewReference != null) {
	                ImageView imageView = imageViewReference.get();
	                if (imageView != null) {
	                    if (bitmap != null) {
	                        imageView.setImageBitmap(bitmap);

	                        //imageView.setImageDrawable(Utils.roundImageDrawable(bitmap, getActivity().getResources()));

	                    } else {
	                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.button_photo);
	                        imageView.setImageDrawable(placeholder);
	                    }
	                }
	            }
	        }
	    }
		
		 private Bitmap downloadBitmapUser(String url) {
		        HttpURLConnection urlConnection = null;
		        try {
		            URL uri = new URL(url);
		            urlConnection = (HttpURLConnection) uri.openConnection();
		            int statusCode = urlConnection.getResponseCode();
		            if (statusCode != 200) {
		                return null;
		            }

		            InputStream inputStream = urlConnection.getInputStream();
		            if (inputStream != null) {
		                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		                //Bitmap bitmap2 = Utils.getRoundedShape(bitmap);
		                return bitmap;
		            }
		        } catch (Exception e) {
		            urlConnection.disconnect();
		            //Log.w("ImageDownloader", "Error downloading image from " + url);
		        } finally {
		            if (urlConnection != null) {
		                urlConnection.disconnect();
		            }
		        }
		        return null;
		    }



	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		

		case R.id.share:
			showOneDialog();
		default:
			break;
		}
		
	}
	

	
	   /**
     * Clase privada para la gestion de la actividad DetailTapentoActivity
     * @author Victoria Marcos
     *
     */
	private class ResultMessageCallback implements Callback {

		public boolean handleMessage(Message arg0) {
			pDialog.dismiss();
			// Cerramos la pantalla de progreso

			switch (arg0.what) {
			case WORKER_MSG_ERROR:
				Toast.makeText(DetailArbolActivity.this, "Error",
						Toast.LENGTH_LONG).show();
				break;
			case WORKER_MSG_OK:
				
				ponerDatosEnView();
				break;
			}

			pDialog.dismiss();
			return true; 
		}
	}
	
	private void showOneDialog() {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);	
		
		sharingIntent.setType("text/plain");
		
											
		
	
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.TEXTORESTAURANTE + mArbol.getNombre()+ Constants.TEXTOENLACE+ mArbol.getDireccion());
			    
				
		
		
		
		
		startActivity(Intent.createChooser(sharingIntent,"Compartir via"));


	}

}
