package raiocalculator.app;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class IncidenciaRaioActivity extends MapActivity {

	LocationManager lm;
	Location l;
	MapView mapa;
	Long distanciaDaQuedaDoRaio;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		mapa = (MapView) findViewById(R.id.mapa);
		
		Intent i = getIntent();
		distanciaDaQuedaDoRaio = i.getLongExtra("distanciaDaQuedaDoRaio", 0);
		
		mapa.setSatellite(true);
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Double latitude = l.getLatitude() * 1E6;
		Double longitude = l.getLongitude() * 1E6;
		GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());
		mapa.getController().setCenter(geoPoint);
		mapa.getController().setZoom(18);
		
		//MinhaPosicaoOverlay localDoDispositivo = new MinhaPosicaoOverlay(getResources().getDrawable(R.drawable.ic_launcher), 100);
		//localDoDispositivo.adicionarOverlay(new OverlayItem(geoPoint, "Você", "Você"));
		//mapa.getOverlays().add(localDoDispositivo);
		
		MinhaPosicaoOverlay localDaQuedaDoRaio = new MinhaPosicaoOverlay(getResources().getDrawable(R.drawable.ic_38x38), 100);
		localDaQuedaDoRaio.adicionarOverlay(new OverlayItem(geoPoint, "Local que o raio caiu!", "Local que o raio caiu!"));
		mapa.getOverlays().add(localDaQuedaDoRaio);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}