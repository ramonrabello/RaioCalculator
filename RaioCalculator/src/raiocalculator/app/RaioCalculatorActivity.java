package raiocalculator.app;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 
 * 
 * @author @ramonrabello
 *         @fabiolimaf
 */
public class RaioCalculatorActivity extends Activity {

	private long intervaloEntreRaioTrovao;
	private long distanciaDaQuedaDoRaio;
	private TextView textoDistancia;
	private ToggleButton botaoCalculo;
	private Chronometer cronometro;
	private Button botaoMapa;
	private TransitionDrawable transicao;
	private MediaPlayer mp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textoDistancia = (TextView) findViewById(R.id.textoDistancia);
		botaoCalculo = (ToggleButton) findViewById(R.id.botaoCalculo);
		botaoMapa = (Button) findViewById(R.id.botaoMapa);
		cronometro = (Chronometer) findViewById(R.id.chronometer1);
		transicao = (TransitionDrawable) botaoCalculo.getBackground();
	}
	
	private void aplicarEfeitoDeRaio() {
		
		final Handler handler = new Handler();		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				handler.post(new Runnable(){

					@Override
					public void run() {
						
						// 1a piscada do raio
						transicao.startTransition(200);
						transicao.reverseTransition(200);
						
					}				
				});				
			}
		}).start();
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	public void calcularDistanciaDoTrovao(View v) {
		if (botaoCalculo.isChecked()) {
			processarQuedaDoRaio();
		} else {
			processarEscutaDoTrovao();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		aplicarEfeitoDeRaio();
	}
	
	/**
	 * 
	 * @param transicao
	 */
	private void processarQuedaDoRaio() {
		
		botaoMapa.setVisibility(View.INVISIBLE);
		new Thread(new Runnable() {
			public void run() {
				cronometro.post(new Runnable() {
					public void run() {
						cronometro.setBase(SystemClock.elapsedRealtime());
						cronometro.start();
					}
				});
			}
		}).start();
		aplicarEfeitoDeRaio();

		textoDistancia.setText(getResources().getString(R.string.texto_quando_ouvir_trovao));
	}

	/**
	 * 
	 * 
	 */
	private void processarEscutaDoTrovao() {
		
		// constantes para o c‡lculo da distancia da queda do raio
		Integer intervaloZonaPerigosa = getResources().getInteger(R.integer.intervalo_raio_trovao);
		Integer velocidadeDoSomNoAr = getResources().getInteger(R.integer.velocidade_som_no_ar);
		
		new Thread(new Runnable() {
			public void run() {
				cronometro.post(new Runnable() {
					public void run() {
						cronometro.stop();
					}
				});
			}
		}).start();
		
		mp = MediaPlayer.create(RaioCalculatorActivity.this, R.raw.som_trovao);
		
		new Thread(new Runnable() {
			public void run() {
				// utilizado para representar o som do trov‹o
				mp.start();
			}
		}).start();
		
		// vibra o telefone quando acontecer o trov‹o
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(mp.getDuration());

		// d = t * velocidade do som
		intervaloEntreRaioTrovao = (SystemClock.elapsedRealtime() - cronometro.getBase()) / 1000;
		
		distanciaDaQuedaDoRaio = intervaloEntreRaioTrovao * velocidadeDoSomNoAr;
		
		// converte o valor da dist‰ncia em formato decimal
		DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.getDefault()));
		String distanciaFormatada = df.format(distanciaDaQuedaDoRaio);
		String textoFormatado = null;
		
		// configura a cor da distancia que o raio caiu
		SpannableString text = new SpannableString("");
		
		// se o trovao caiu perto ou longe
		if (intervaloEntreRaioTrovao < intervaloZonaPerigosa) {
		    textoFormatado = getResources().getString(R.string.texto_distancia_raio_perto, new Object[] { distanciaFormatada });
		    text = new SpannableString(textoFormatado);
			text.setSpan(new ForegroundColorSpan(Color.RED), 19, 28, 0);
		} else {
			textoFormatado = getResources().getString(R.string.texto_distancia_raio_longe, new Object[] { distanciaFormatada });
			text = new SpannableString(textoFormatado);
			text.setSpan(new ForegroundColorSpan(0xff387e25), 19, 28, 0);
		}
		botaoMapa.setVisibility(View.VISIBLE);
		textoDistancia.setText(text);
	}
	
	/**
	 * 
	 * 
	 * @param v
	 */
	public void exibirLocalDoRaioNoMapa(View v){
		Intent i = new Intent(this, IncidenciaRaioActivity.class);
		i.putExtra("distanciaDaQuedaDoRaio", distanciaDaQuedaDoRaio);
		startActivity(i);
	}
}