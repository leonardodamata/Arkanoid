
//Pacote da classe
package br.com.especializacao.motorgrafico;

//Pacotes utilizados
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class CSpriteAnimacao 
{
	//Constantes da classe
	public static final int NORMAL = 0, HORIZONTAL = 1, VERTICAL = 2, VERTICAL_HORIZONTAL = 3;
	
	//Atributos puclicos a classe
	public int iPosX = 0, iPosY = 0;
	public int iDirX = 0, iDirY = 0;
	public int iLarguraQuadro = 0, iAlturaQuadro = 0;
	public int iQuadroAtual = 0;
	public float fAlpha = 1.0f;
	public float fAngulo = 0.0f;
	public float fEscalaX = 1.0f, fEscalaY = 1.0f;
	public int iEspelhamento = NORMAL;
	public boolean bVisible = true;
	
	//Atributos privados da classe
	private ArrayList<CAnimacao> vetAnimacoes = null;
	private int iCodImagem = 0;
	private int iLarguraImagem = 0;
	private int iAlturaImagem = 0;
	private GL10 vrOpenGL = null;
	private int iAnimacaoAtual = 0;
	private int iTotalColunas = 0;
	private int iTotalLinhas = 0;
	private float fCoordX1 = 0;
	private float fCoordY1 = 0;
	private float fCoordX2 = 0;
	private float fCoordY2 = 0;
	private float[] vetTexturas = null;
	
	
	//Construtor da classe
	public CSpriteAnimacao(GL10 pOpenGL, int pCodImagem, int pLarguraQuadro, int pAlturaQuadro, int pLarguraImagem, int pAlturaImagem)
	{
		vrOpenGL = pOpenGL;
		iLarguraQuadro = pLarguraQuadro;
		iAlturaQuadro = pAlturaQuadro;
		iLarguraImagem = pLarguraImagem;
		iAlturaImagem = pAlturaImagem;
		iCodImagem = CGerenteGrafico.carregaImagem(pCodImagem, vrOpenGL);
		vetAnimacoes = new ArrayList<CAnimacao>();
		vetTexturas = new float[8];
	}
	
	public void criaAnimacao(int iFPS, boolean bRepete, CQuadro[] pQuadros)
	{
		//Cria e configura o objeto da animacao
		CAnimacao vrNovaAnimacao= new CAnimacao(pQuadros);
		vrNovaAnimacao.bRepeticao = bRepete;
		vrNovaAnimacao.configuraIntervalo(iFPS);

		//Se for a primeira animacao, entao se torna default
		vetAnimacoes.add(vrNovaAnimacao);
	}
	
	//Metodo utilizado para retornar o total de animacoes
	public int retornaTotalAnimacoes()
	{
		return vetAnimacoes.size();
	}
	
	//Metodo utilizado para configurar o indice da animacao
	public void configuraAnimcaceoAtual(int iAnim)
	{
		//Verifica se existem animacoes cadastradas
		if (vetAnimacoes.size() == 0)
		{
			return;
		}
		
		//Verifica se o indice da animacao e valido e diferente da animacao atual
		if(iAnim != iAnimacaoAtual && iAnim < vetAnimacoes.size())
		{
			iAnimacaoAtual = iAnim;
			vetAnimacoes.get(iAnimacaoAtual).reiniciaAnimacao();
		}
	}
	
	//Metodo utilizado para reiniciar a animacao
	public void reiniciaAnimacao()
	{
		if (iAnimacaoAtual < vetAnimacoes.size())
		{
			vetAnimacoes.get(iAnimacaoAtual).reiniciaAnimacao();
		}
	}
	
	//Metodo que retorna a animacao atual
	public CAnimacao retornaAnimacaoAtual()
	{
		return vetAnimacoes.get(iAnimacaoAtual);
	}
	
	//Metodo que retorna o quadro atual da animacao
	public int retornaQuadroAtualAnimacao()
	{
		if (vetAnimacoes.size() >0)
		{
			return vetAnimacoes.get(iAnimacaoAtual).iQuadroAtual;
		}
		
		return 0;
	}
	
	//Metodo utilizado para atualizar as coordenadas de textura de acordo com o seu quadro
	public void atualizaSprite()
	{
		//Atualiza a animacao e o quadro atual
		if (vetAnimacoes.size() > 0)
		{
			vetAnimacoes.get(iAnimacaoAtual).atualizaAnimacao();
			iQuadroAtual = vetAnimacoes.get(iAnimacaoAtual).retornaIndiceQuadroAtual();
		}
		
		//Calcula as coordenadas de textura para o quadro atual da animacao
		iTotalColunas = iLarguraImagem / iLarguraQuadro;
		iTotalLinhas = iAlturaImagem / iAlturaQuadro;
				
		//Calcula as coordenadas do quadro
		fCoordX1 = (iQuadroAtual % iTotalColunas) * (1.0f / iTotalColunas);
		fCoordY1 = (iQuadroAtual / iTotalColunas) * (1.0f/ iTotalLinhas);
		fCoordX2 = fCoordX1 + 1.0f/(iLarguraImagem/iLarguraQuadro);
		fCoordY2 = fCoordY1 + 1.0f/(iAlturaImagem/iAlturaQuadro);
			
		//Configura as coordenadas do proximo quadro
		if (iEspelhamento == NORMAL)
		{
			vetTexturas[0] = fCoordX1;
			vetTexturas[1] = fCoordY2;
			vetTexturas[2] = fCoordX1;
			vetTexturas[3] = fCoordY1;
			vetTexturas[4] = fCoordX2;
			vetTexturas[5] = fCoordY2;
			vetTexturas[6] = fCoordX2;
			vetTexturas[7] = fCoordY1;
		}
		else if (iEspelhamento == HORIZONTAL)
		{
			vetTexturas[0] = fCoordX2;
			vetTexturas[1] = fCoordY2;
			vetTexturas[2] = fCoordX2;
			vetTexturas[3] = fCoordY1;
			vetTexturas[4] = fCoordX1;
			vetTexturas[5] = fCoordY2;
			vetTexturas[6] = fCoordX1;
			vetTexturas[7] = fCoordY1;
		}
		else if (iEspelhamento == VERTICAL)
		{
			vetTexturas[0] = fCoordX1;
			vetTexturas[1] = fCoordY1;
			vetTexturas[2] = fCoordX1;
			vetTexturas[3] = fCoordY2;
			vetTexturas[4] = fCoordX2;
			vetTexturas[5] = fCoordY1;
			vetTexturas[6] = fCoordX2;
			vetTexturas[7] = fCoordY2;
		}
		else if (iEspelhamento == VERTICAL_HORIZONTAL)
		{
			vetTexturas[0] = fCoordX2;
			vetTexturas[1] = fCoordY1;
			vetTexturas[2] = fCoordX2;
			vetTexturas[3] = fCoordY2;
			vetTexturas[4] = fCoordX1;
			vetTexturas[5] = fCoordY1;
			vetTexturas[6] = fCoordX1;
			vetTexturas[7] = fCoordY2;
		}
		
		//Atualiza o vetor de coordenadas
		vrOpenGL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, CNioBuffer.geraNioBuffer(vetTexturas));
	}	

	//Metodo utilizado para desenhar o sprite
	public void desenhaSprite()
	{
		//Verifica se esta visivel
		if (!bVisible)
			return;
		
		//Atualiza o estado do sprite
		atualizaSprite();
		
		//Assina a textura e chama as funcoes de desenho
		vrOpenGL.glBindTexture(GL10.GL_TEXTURE_2D, iCodImagem);
		vrOpenGL.glLoadIdentity();
		vrOpenGL.glColor4f(1.0f, 1.0f, 1.0f, fAlpha);
		vrOpenGL.glTranslatef(iPosX, iPosY, 0);
		vrOpenGL.glRotatef(fAngulo, 0.0f, 0.0f, 1.0f);
		vrOpenGL.glScalef(fEscalaX * iLarguraQuadro, fEscalaY * iAlturaQuadro, 1.0f);
		vrOpenGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	//Metodo utilizado para verificar a colisao
	public boolean colideSprite(CSpriteAnimacao vSprite)
	{
		//Calcula pontos em X
		float A = iPosX - (iLarguraQuadro * fEscalaX);
		float B = iPosX + (iLarguraQuadro * fEscalaX);
		float C = vSprite.iPosX - (vSprite.iLarguraQuadro * vSprite.fEscalaX);
		float D = vSprite.iPosX + (vSprite.iLarguraQuadro * vSprite.fEscalaX);
		
		//Verifica colisao em X
		if (A < D && B > C)
		{
			//Calcula pontos em Y
			A = iPosY - (iAlturaQuadro * fEscalaY);
			B = iPosY + (iAlturaQuadro * fEscalaY);
			C = vSprite.iPosY - (vSprite.iAlturaQuadro * vSprite.fEscalaY);
			D = vSprite.iPosY + (vSprite.iAlturaQuadro * vSprite.fEscalaY);
			
			//Verifica colisao em Y
			if (A < D && B > C)
			{
				return true;
			}
		}
		return false;
	}
	
	//Metodo utilizado para detectar colisao de um ponto com o Sprite
	public boolean colidePonto(int iX, int iY)
	{
		float A = iPosX - (fEscalaX * iLarguraQuadro);
		float B = iPosX + (fEscalaX * iLarguraQuadro);
		float C = iPosY - (fEscalaY * iAlturaQuadro);
		float D = iPosY + (fEscalaY * iAlturaQuadro);
		
		//Verifica se o ponto esta dentro dos limites do quadro
		if (iX >= A && iX <= B)
		{
			if (iY >= C && iY <= D)
			{
				return true;
			}
		}
			
		return false;
	}
}
