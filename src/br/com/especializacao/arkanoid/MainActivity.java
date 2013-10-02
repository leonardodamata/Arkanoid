package br.com.especializacao.arkanoid;

//Pacotes   utilizados
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.especializacao.motorgrafico.CGerenteEventos;
import br.com.especializacao.motorgrafico.CGerenteGrafico;
import br.com.especializacao.motorgrafico.CGerenteSons;
import br.com.especializacao.motorgrafico.CGerenteTempo;
import br.com.especializacao.motorgrafico.CIntervaloTempo;
import br.com.especializacao.motorgrafico.CNioBuffer;
import br.com.especializacao.motorgrafico.CQuadro;
import br.com.especializacao.motorgrafico.CSprite;
import br.com.especializacao.motorgrafico.CSpriteAnimacao;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;


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
		/*	super.onCreate(savedInstanceState);

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
		CGerenteEventos.vrEventosAcelerometro.inicializaAcelerometro(this);*/

		super.onCreate(savedInstanceState);

		vrSuperficieDesenho = new GLSurfaceView(this);

		//Preparacao da janela da aplicacao (modo tela cheia)
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Renderizador render = new Renderizador();
		render.setActivity(this);

		//Configurando a superficie de desenho
		vrSuperficieDesenho.setRenderer(render);

		//Registra a superficie de desenho ao tratador de eventos
		vrSuperficieDesenho.setOnTouchListener(CGerenteEventos.vrEventosTouch);

		//Ativa a superficie de desenho na janela
		setContentView(vrSuperficieDesenho); 
	}


	//Metodo chamado no momento em que a Activity vai para segundo plano
	public void onPause(Bundle savedInstanceState)
	{
		super.onPause();
		vrSuperficieDesenho.onPause();
		CGerenteSons.vrMusica.pausaMusica();
		CGerenteGrafico.release();
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

		final int ABERTURA = 0, MENU = 1, JOGO = 2, AJUDA = 3; 
		//final int TAMANHO_QUADRO = 50;

		//Atributos da classe
		//	int TAMANHO_QUADRO = 50;
		int TAMANHO_QUADRO = 1;
		int iEstado = ABERTURA; 
		CIntervaloTempo vrTempo = null;
		int iLargura = 0, iAltura = 0;
		int iSubstado = 0;
		//int[] vetCodigoSons = null;
		CSprite[] vetSpritesJogador= null;
		CSpriteAnimacao vrSpriteLogoUno = null;
		CSpriteAnimacao vrSpriteTitulo = null;
		CSpriteAnimacao vetBotoes[] = null;
		Activity vrActivity = null;
		int vetCodigoSons[];
		int iDirecao = 0;
		int iTempX = 0;
		int iTempY = 0;
		Random vrRand = new Random();




		//Metodo utilizado para armazenar uma referencia da activity
		public void setActivity(Activity pActivity)
		{
			vrActivity = pActivity;
			CGerenteTempo.inicializaGerente();
			CGerenteGrafico.validaContexto(vrActivity);
			CGerenteSons.inicializa(vrActivity);
		} 

		//Metodo chamado no momento em que a Activity e criada ou quando retorna do estado Resume
		public void onSurfaceCreated(GL10 vrOpenGL, EGLConfig vrConfigueXo)
		{

			//Configura a cor de limpeza da tela
			vrOpenGL.glClearColor(0,0,0,1);

			//Cria os codigos para os efeitos sonoros
			vetCodigoSons = new int[1];
			vetCodigoSons[0]  = CGerenteSons.vrEfeitos.carregaSom("btnOn.wav");


			/*//Configura a cor de limpeza da tela
			vrOpenGL.glClearColor(0,0,0,1);

			//Cria o vetor de sons
			vetCodigoSons = new int[2];
			//vetCodigoSons[0]  = CGerenteSons.vrEfeitos.carregaSom("efeito0.wav");
			//vetCodigoSons[1]  = CGerenteSons.vrEfeitos.carregaSom("efeito1.wav");

			//Cria o vetor de objetos e inicializa as posicoes
			//vetSpritesInimigos = new CSprite[10];

			//Carrega a musica do jogo
			//CGerenteSons.vrMusica.carregaMusica("musica.mid",true);
			//CGerenteSons.vrMusica.reproduzMusica();*/
		}

		//Metodo chamado quando tamanho da tela e alterado
		public void onSurfaceChanged(GL10 vrOpenGL, int pLargura, int pAltura)
		{	

			//Gerencia os estados do jogo
			/*	if (iEstado == ABERTURA)
			{
				abertura();
			}
			if (iEstado == MENU)
			{*/
			//Armazena a altura e largura da tela
			iLargura = pLargura;
			iAltura = pAltura;

			//Configura a viewport
			vrOpenGL.glViewport(0, 0, iLargura, iAltura);

			//Configura a janela de visualizacao
			//Ativa a matriz de projecao para configuração da janela de visualizacao
			vrOpenGL.glMatrixMode(GL10.GL_PROJECTION);

			//Inicia a matriz de projeção com a matriz identidade
			vrOpenGL.glLoadIdentity();

			//Seta as coordenadas da janela de visualizacao
			vrOpenGL.glOrthof(0.0f, iLargura, 0.0f, iAltura,  1.0f, -1.0f);

			//Inicia a matriz de desenho
			vrOpenGL.glMatrixMode(GL10.GL_MODELVIEW);

			//Inicia a matriz de desenho com a matriz identidade
			vrOpenGL.glLoadIdentity();

			//Configura a cor do desenho, habilita transparencia, uso de textura e vetores de vertices texturas
			vrOpenGL.glColor4f(1.0f, 1.0f,1.0f,1.0f);
			vrOpenGL.glEnable(GL10.GL_BLEND);
			vrOpenGL.glEnable(GL10.GL_ALPHA_TEST);
			vrOpenGL.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			vrOpenGL.glEnable(GL10.GL_TEXTURE_2D);
			vrOpenGL.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			vrOpenGL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			//Enviando os vertices ao OpenGL
			vrOpenGL.glVertexPointer(2, GL10.GL_FLOAT, 0, CNioBuffer.geraNioBuffer(new float[]{-TAMANHO_QUADRO, -TAMANHO_QUADRO, 
					-TAMANHO_QUADRO, TAMANHO_QUADRO, 
					TAMANHO_QUADRO, -TAMANHO_QUADRO, 
					TAMANHO_QUADRO, TAMANHO_QUADRO}));	
			//Inicializa um interalo de tempo de 3s
			vrTempo = new CIntervaloTempo();
			vrTempo.reiniciaTempo(3000);

			vrSpriteLogoUno = new CSpriteAnimacao(vrOpenGL, R.drawable.facto, 512, 256, 512, 256);
			vrSpriteLogoUno.iPosX = iLargura/2;
			vrSpriteLogoUno.iPosY = iAltura/2;
			vrSpriteLogoUno.fAlpha = 0.0f;
			vrSpriteLogoUno.fEscalaX = ((iLargura    /  5.0f) / vrSpriteLogoUno.iLarguraQuadro);
			vrSpriteLogoUno.fEscalaY = ((iAltura    /   6.0f) / vrSpriteLogoUno.iAlturaQuadro);

			vrSpriteTitulo = new CSpriteAnimacao(vrOpenGL, R.drawable.titulo, 256, 64, 256, 64);
			vrSpriteTitulo.iPosX = iLargura/6;
			vrSpriteTitulo.fAngulo = 30;
			vrSpriteTitulo.iPosY = iAltura/2;
			vrSpriteTitulo.fEscalaX = ((iLargura     /  8.0f) / vrSpriteTitulo.iLarguraQuadro);
			vrSpriteTitulo.fEscalaY = ((iAltura     /   10.0f) / vrSpriteTitulo.iAlturaQuadro);

			//Cria o vetor de sprites
			vetBotoes = new CSpriteAnimacao[3];
			vetBotoes[0] = new CSpriteAnimacao(vrOpenGL, R.drawable.jogar, 128, 42, 128, 128);
			vetBotoes[1] = new CSpriteAnimacao(vrOpenGL, R.drawable.ajuda, 128, 42, 128, 128);
			vetBotoes[2] = new CSpriteAnimacao(vrOpenGL, R.drawable.sair, 128, 42, 128, 128);

			//Cria os quadros da animacao
			CQuadro[] vrQuadro0 = new CQuadro[1];
			vrQuadro0[0] = new CQuadro(0);
			CQuadro[] vrQuadro1 = new CQuadro[1];
			vrQuadro1[0] = new CQuadro(1);
			CQuadro[] vrQuadro2 = new CQuadro[1];
			vrQuadro2[0] = new CQuadro(2);

			//Configura a escala, as animacoes e posicao de cada botao
			for (int iIndex = 0; iIndex < 3; iIndex++)
			{
				vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro0);
				vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro1);
				vetBotoes[iIndex].criaAnimacao(1, false, vrQuadro2);
				vetBotoes[iIndex].iPosX = iLargura/2;
				vetBotoes[iIndex].configuraAnimcaceoAtual(0);
				vetBotoes[iIndex].fEscalaX = ((iLargura     /  13.0f) / vetBotoes[iIndex].iLarguraQuadro);
				vetBotoes[iIndex].fEscalaY = ((iAltura     /  14.0f) / vetBotoes[iIndex].iAlturaQuadro);
			}
			vetBotoes[0].iPosY = iAltura/2 + iAltura/4;
			vetBotoes[1].iPosY = iAltura/2;
			vetBotoes[2].iPosY = iAltura/2 - iAltura/4;
			
			// clique nos botões
			
			
			//	} 
			/*if (iEstado == JOGO)
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
				//Ativa a matriz de projecao para configuraçã  o da janela de visualizacao
				vrOpenGL.glMatrixMode(GL10.GL_PROJECTION);

				//Inicia a matriz de projeção com a matriz identidade
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
			} */


		}

		//Metodo chamado quempre que possivel para realizar o desenho grafico na superficie
		public void onDrawFrame(GL10 vrOpenGL)
		{		

			//Limpa o fundo da tela
			vrOpenGL.glClear(GL10.GL_COLOR_BUFFER_BIT);

			
			//Gerencia os estados do jogo
			if (iEstado == ABERTURA)
			{
				abertura();
			}
			else if (iEstado == MENU)
			{
				menu();
			} 
			else if (iEstado == JOGO)
			{
				jogo(vrOpenGL);
			} 


			//Executa as etapas de um jogo
			CGerenteTempo.atualiza();
			CGerenteEventos.vrEventosTouch.atualizaEstados();
			
			//Pausa no loop da aplicacao
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
				Thread.sleep(40);
			}
			catch(Exception e)
			{	
			}
		}

		// Méto que gerencia o jogo

		public void jogo(GL10 vrOpenGL)
		{

			//Inicializacao o gerenciador de acelerometro e o gerenciador de sons
			CGerenteEventos.vrEventosAcelerometro.inicializaAcelerometro(vrActivity);
			//Executa todas as etapas do jogo
			trataEventos();
			//	atualizaInimigos();
			desenha(vrOpenGL);
			//	pausa(); 
		
		}
		//Metodo que gerencia o estado de abertura
		public void abertura()
		{
			vrTempo.atualiza();
			if (iSubstado == 0)
			{
				if (vrTempo.tempoFinalizado())
				{
					vrSpriteLogoUno.fAlpha+=0.02;
					if (vrSpriteLogoUno.fAlpha >= 1.0f)
					{
						iSubstado = 1;
					}
				}
			}
			else
			{
				vrSpriteLogoUno.fAlpha-=0.03f;
				if (vrSpriteLogoUno.fAlpha<=0.0f)
				{
					//Carrega uma musica de fundo
					CGerenteSons.vrMusica.carregaMusica("musica.mid",true);
					CGerenteSons.vrMusica.reproduzMusica();
					iEstado = MENU;
				}
			}
			vrSpriteLogoUno.desenhaSprite();
		} 

		//Metodo que gerencia o estado do Menu
		public void menu()
		{
			vrSpriteTitulo.desenhaSprite();
			
			//Verifica se houve toque no botao e configura as animacoes
			for(int iIndex=0; iIndex < 3; iIndex++)
			{		
				if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_DOWN || 
						CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_MOVE)
				{
					if (vetBotoes[iIndex].colidePonto((int)CGerenteEventos.vrEventosTouch.fPosX, iAltura - (int)CGerenteEventos.vrEventosTouch.fPosY))
					{
						if (vetBotoes[iIndex].iQuadroAtual != 1)
						{
							vetBotoes[iIndex].configuraAnimcaceoAtual(1);
							CGerenteSons.vrEfeitos.reproduzSom(vetCodigoSons[0]);
						}
					}
					else
					{
						vetBotoes[iIndex].configuraAnimcaceoAtual(0);
					}
				}
				else if (CGerenteEventos.vrEventosTouch.iTipoEventoAtual == MotionEvent.ACTION_UP)
				{
					if (vetBotoes[iIndex].colidePonto((int)CGerenteEventos.vrEventosTouch.fPosX, iAltura - (int)CGerenteEventos.vrEventosTouch.fPosY))
					{
						if (vetBotoes[iIndex].iQuadroAtual != 0)
						{
							vetBotoes[iIndex].configuraAnimcaceoAtual(0);
						}
					}
				}
				
				//Atualiza sprites
				vetBotoes[iIndex].desenhaSprite();
			}

		}
	}
}
