
//Pacote da classe
package br.com.especializacao.motorgrafico;

//Pacotes utilizados
import javax.microedition.khronos.opengles.GL10;

public class CSprite 
{
	//Atributos da classe
	int iVermelho = 1, iVerde   = 1, iAzul = 1;
	int iEscalaX = 	1, iEscalaY = 1;
	int iPosX = 0, iPosY = 0;
	int iAlturaQuadro  = 0;
	int iLarguraQuadro = 0;
	boolean bVisivel = true;

	//Construtor da classe
	public CSprite (int pPosX, int pPosY, int pTamanhoQuadro)
	{
		iPosX = pPosX;
		iPosY = pPosY;
		iLarguraQuadro = pTamanhoQuadro;
		iAlturaQuadro = pTamanhoQuadro;
	}
	
	//Metodo utilizado para retornar a posicao X do Sprite
	public int getPosicaoX()
	{
		return iPosX;
	}
	
	//Metodo utilizado para retornar a posicao Y do Sprite
	public int getPosicaoY()
	{
		return iPosY;
	}
	
	//Metodo utilizado para retornar a largura do quadro
	public int getLarguraQuadro()
	{
		return iLarguraQuadro * iEscalaX;
	}
	
	//Metodo utilizado para retornar a altura do quadro
	public int getAlturaQuadro()
	{
		return iAlturaQuadro * iEscalaY;
	}
	
	//Metodo utilizado para setar a visibilidade do sprite
	public void setVisibilidade(boolean pVisivel)
	{
		bVisivel = pVisivel;
	}
	
	//Metodo usado para desenhar o Sprite
	public void desenhaSprite(GL10 vrOpenGL)
	{
		if (!bVisivel)
			return;
		
		vrOpenGL.glLoadIdentity();
		vrOpenGL.glTranslatef(iPosX, iPosY, 0.0f);
		vrOpenGL.glScalef(iEscalaX, iEscalaY, 1.0f);
		vrOpenGL.glColor4f(iVermelho, iVerde, iAzul, 1.0f);
		vrOpenGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
		
	//Metodo utilizado para configurar a cor do objeto
	public void setCor(int pVermelho, int pVerde, int pAzul)
	{
		iVermelho = pVermelho;
		iVerde = pVerde;
		iAzul = pAzul;
	}
		
	//Metodo utilizado para setar a escala do Sprite
	public void setEscala(int iX, int iY)
	{
		iEscalaX = iX;
		iEscalaY = iY;
	}
	
	//Metodo utilizado para setar a posicao do sprite
	public void setPosicao(int iX, int iY)
	{
		iPosX = iX;
		iPosY = iY;
	}
	
	//Metodo utilizado para detectar colisao
	public boolean colidePonto(CSprite pSprite)
	{	
		return false;
	}
}
