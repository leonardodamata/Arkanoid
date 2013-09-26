package br.com.especializacao.arkanoid;

//Pacotes utilizados
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.especializacao.motorgrafico.CGerenteEventos;
import br.com.especializacao.motorgrafico.CNioBuffer;
import br.com.especializacao.motorgrafico.CSprite;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


//Classe que implementa a atividade principal da aplicacao Android
public class MainActivity extends Activity {

	GLSurfaceView vrSuperficieDesenho = null;
	private int i = 0;
	private int centroA;
	private int centroL;
	private float fAngulo = 0.0f;
	//private int iIncAngulo = 1;
	private int sentidoA = 0;
	private int sentidoL = 0;
	private int totalA = 0;
	private int totalL = 0;
	private int velocidade = 10;

	//Metodo chamado no momento da criacao da Activity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String vDisplay = "";
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			vDisplay = "DENSITY_LOW";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			vDisplay = "DENSITY_MEDIUM";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			vDisplay = "DENSITY_HIGH";
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			vDisplay = "DENSITY_HIGH";		
			break;
		}
		Toast.makeText(getApplicationContext(), vDisplay, Toast.LENGTH_LONG).show();
		
		

		vrSuperficieDesenho = new GLSurfaceView(this);

		//Preparacao da janela da aplicacao (modo tela cheia)
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//Cria o objeto renderizador associado a superficie
		Renderizador render = new Renderizador();

		//Configurando a superficie de desenho
		vrSuperficieDesenho.setRenderer(render);

		//Registra a superficie de desenho ao tratador de eventos
		vrSuperficieDesenho.setOnTouchListener(CGerenteEventos.vrEventosTouch);

		//Ativa a superficie de desenho na janela
		setContentView(vrSuperficieDesenho); 

		//Inicializacao o gerenciador de acelerometro e o gerenciador de sons
		CGerenteEventos.vrEventosAcelerometro.inicializaAcelerometro(this);
	}


	//Metodo chamado no momento em que a Activity vai para segundo plano
	public void onPause(Bundle savedInstanceState)
	{
		super.onPause();
		vrSuperficieDesenho.onPause();
	}

	//Metodo chamado no momento em que a Activity volta do segundo plano
	public void onResume(Bundle savedInstanceState)
	{
		super.onResume();
		vrSuperficieDesenho.onResume();
	}

	class Renderizador implements Renderer
	{	
		//Constantes da classe
		//final int TAMANHO_QUADRO = 50;
		public int TAMANHO_QUADRO = 50;

		//Atributos da classe
		int iLargura = 0, iAltura = 0;
		int[] vetCodigoSons = null;
		CSprite[] vetSpritesJogador= null;
		int iDirecao = 0;
		int iTempX = 0;
		int iTempY = 0;
		Random vrRand = new Random();

		//Metodo chamado no momento em que a Activity e criada ou quando retorna do estado Resume
		public void onSurfaceCreated(GL10 vrOpenGL, EGLConfig vrConfigueXo)
		{
			//Configura a cor de limpeza da tela
			vrOpenGL.glClearColor(0,0,0,1);

			//Cria o vetor de sons
			vetCodigoSons = new int[2];
			//vetCodigoSons[0]  = CGerenteSons.vrEfeitos.carregaSom("efeito0.wav");
			//vetCodigoSons[1]  = CGerenteSons.vrEfeitos.carregaSom("efeito1.wav");

			//Cria o vetor de objetos e inicializa as posicoes
			//vetSpritesInimigos = new CSprite[10];

			//Carrega a musica do jogo
			//CGerenteSons.vrMusica.carregaMusica("musica.mid",true);
			//CGerenteSons.vrMusica.reproduzMusica();
		}

		//Metodo chamado quando tamanho da tela e alterado
		public void onSurfaceChanged(GL10 vrOpenGL, int pLargura, int pAltura)
		{	
			
			int tamanhoTela = 0;
			
			//Armazena a altura e largura da tela
			iLargura = pLargura;
			iAltura = pAltura;
			
			 
			
			// tamanho da barra 
			tamanhoTela = (iLargura * 5)/100;
			 TAMANHO_QUADRO = tamanhoTela;

			//Cria a barra
			vetSpritesJogador = new CSprite[1];
			//vetSpritesJogador[0] = new CSprite(iLargura/2 - (TAMANHO_QUADRO *3)/2,  20, TAMANHO_QUADRO);
			vetSpritesJogador[0] = new CSprite(iLargura/2 - (TAMANHO_QUADRO *3)/2,  20, TAMANHO_QUADRO);
			vetSpritesJogador[0].setCor(1, 1, 0);
			vetSpritesJogador[0].setEscala(4, 1);



			//Configura a viewport
			vrOpenGL.glViewport(0, 0, iLargura, iAltura);

			//Configura a janela de visualizacao
			//Ativa a matriz de projecao para configura��o da janela de visualizacao
			vrOpenGL.glMatrixMode(GL10.GL_PROJECTION);

			//Inicia a matriz de proje��o com a matriz identidade
			vrOpenGL.glLoadIdentity();

			//Seta as coordenadas da janela de visualizacao
			vrOpenGL.glOrthof(0.0f, iLargura, 0.0f, iAltura,  1.0f, -1.0f);

			//Enviando os vertices ao OpenGL
			vrOpenGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			vrOpenGL.glVertexPointer(2, GL10.GL_FLOAT, 0, CNioBuffer.geraNioBuffer( new float[]{0.0f, 0.0f, 0.0f, TAMANHO_QUADRO, TAMANHO_QUADRO, 0.0f, TAMANHO_QUADRO, TAMANHO_QUADRO}));

			//Configura a cor do desenho
			vrOpenGL.glColor4f(1.0f, 0.0f,0.0f,1.0f);

			//Inicia a matriz de desenho
			vrOpenGL.glMatrixMode(GL10.GL_MODELVIEW);

			//Inicia a matriz de desenho com a matriz identidade
			vrOpenGL.glLoadIdentity();
		}

		//Metodo chamado quempre que possivel para realizar o desenho grafico na superficie
		public void onDrawFrame(GL10 vrOpenGL)
		{		
			//Executa todas as etapas do jogo
			trataEventos();
			//	atualizaInimigos();
			desenha(vrOpenGL);
			pausa();
		}

		//Metodo responsavel pelo desenho
		public void desenha(GL10 vrOpenGL)
		{

			//Limpa o fundo da tela
			vrOpenGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
			vrOpenGL.glLoadIdentity();

			// quadrado na tela
			if(i == 0)
			{

				vrOpenGL.glTranslatef(iLargura/2, iAltura/2, 0.0f);
				centroL = iLargura/2;
				centroA = iAltura/2;
				totalA = iAltura;
				totalL = iLargura;
				i = 1;
			}
			else
			{
				vrOpenGL.glColor4f(0,0,1,1);
				vrOpenGL.glTranslatef(centroL, centroA, 0.0f);
				vrOpenGL.glRotatef(fAngulo, 0.0f, 0.0f, -1.0f);
				vrOpenGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
			if(sentidoA == 0 && sentidoL == 0) //Começa o jogo com direção para baixo;
			{
				centroA = baixo(centroA);
				sentidoA = 2;
				sentidoL = 2;
			}
			if(sentidoA == 2) //Mantem andando para baixo
			{
				centroA = baixo(centroA);
			}
			if(centroA == 0) //Se chegou embaixo, muda o sentido
			{
				centroA = cima(centroA);
				sentidoA = 1;
			}
			if(sentidoA == 1) //Mantem andando para cima
			{
				centroA = cima(centroA);
			}		
			if(centroA == totalA) //Se chegou em cima, muda o sentido
			{
				centroA = baixo(centroA);
				sentidoA = 2;
			}
			if(sentidoL == 1) //Mantem andando para a esquerda
			{
				centroL = esquerda(centroL);
			}
			if(centroL == 0) //Se chegou na esquerda, muda o sentido
			{
				centroL = direita(centroL);
				sentidoL = 2;
			}
			if(sentidoL == 2) //Mantem andando para a direita
			{
				centroL = direita(centroL);
			}
			if(centroL == totalL) //Se chegou na direita, muda o sentido
			{
				centroL = esquerda(centroL);
				sentidoL = 1;
			}

			//Desenha os sprites na tela
			for (int iIndex=0; iIndex < vetSpritesJogador.length; iIndex++)
			{
				vetSpritesJogador[iIndex].desenhaSprite(vrOpenGL);
			}


		}

		public int esquerda(int centroL)
		{
			//centroL--;
			centroL = centroL - velocidade;
			return centroL;	
		}
		public int direita(int centroL)
		{
			//centroL++;
			centroL = centroL + velocidade;
			return centroL;	
		}
		public int cima(int centroA)
		{
			//centroA++;
			centroA = centroA + velocidade;
			return centroA;
		}
		public int baixo(int centroA)
		{
			//centroA--;
			centroA = centroA - velocidade;
			return centroA;
		}

		//Metodo responsavel pelo tratamento de eventos
		public void trataEventos()
		{
			defineDirecaoJogador();
			atualizaPosicaoJogador();	
		}



		//Metodo utilizado para tratar a direcao do jogador com base no acelerometro
		public void defineDirecaoJogador()
		{
			//Verifica o valor do acelerometro e seta a direcao
			if (CGerenteEventos.vrEventosAcelerometro.getAcelX() < -1.5)
			{
				iDirecao = -1;
			}
			else if (CGerenteEventos.vrEventosAcelerometro.getAcelX() > 1.5)
			{
				iDirecao = 1;
			}
			else
			{
				iDirecao = 0;
			}
		}

		//Metodo utilizado para atualizar a posicao do jogador
		public void atualizaPosicaoJogador()
		{	
			//Atualiza a posicao da barra
			vetSpritesJogador[0].setPosicao(vetSpritesJogador[0].getPosicaoX() + iDirecao * 20, vetSpritesJogador[0].getPosicaoY());

			//Verifica colisao com as bordas da tela e ajusta caso necessario
			if (vetSpritesJogador[0].getPosicaoX() < 0)
			{
				vetSpritesJogador[0].setPosicao(0, vetSpritesJogador[0].getPosicaoY());
			}
			else if (vetSpritesJogador[0].getPosicaoX() + vetSpritesJogador[0].getLarguraQuadro() > iLargura)
			{
				vetSpritesJogador[0].setPosicao(iLargura - vetSpritesJogador[0].getLarguraQuadro(), vetSpritesJogador[0].getPosicaoY());
			}


		}

		//Metodo responsavel pela pausa no loop da aplicacao
		public void pausa()
		{
			try
			{
				Thread.sleep(30);
			}
			catch(Exception e)
			{	
			}
		}
	}
}
