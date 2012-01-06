package raiocalculator.app;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * 
 * 
 * @author ramonrabello
 *
 */
public class MinhaPosicaoOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> itens;
	private float raioIncidenciaRelampago;
	//on
	
	public MinhaPosicaoOverlay(Drawable marcador, float raio) {
		super(boundCenterBottom(marcador));
		itens = new ArrayList<OverlayItem>();
		raioIncidenciaRelampago = raio;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		Paint desenhoParaCirculo = new Paint();
		desenhoParaCirculo.setColor(0xff4cafea);
		desenhoParaCirculo.setAntiAlias(true);
		desenhoParaCirculo.setAlpha(50);
		desenhoParaCirculo.setStrokeWidth(0);
		desenhoParaCirculo.setStyle(Style.FILL_AND_STROKE);
		
		
		Paint desenhoParaLinha = new Paint();
		desenhoParaLinha.setColor(Color.RED);
		desenhoParaLinha.setStyle(Style.FILL);
		
		
		Projection projection = mapView.getProjection();
		Point p = projection.toPixels(getCenter(), new Point(mapView.getScrollX(),mapView.getScrollY()));
		
		//canvas.drawLine(p.x, p.y, p.x + raioIncidenciaRelampago, p.y + raioIncidenciaRelampago, desenhoParaLinha);
		canvas.drawCircle(p.x, p.y, raioIncidenciaRelampago, desenhoParaCirculo);
		super.draw(canvas, mapView, shadow);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return itens.get(i);
	}
	
	@Override
	public int size() {
		return itens.size();
	}
		
	/**
	 * 
	 * @param item
	 */
	public void adicionarOverlay(OverlayItem item){
		itens.add(item);
		populate();
	}
}