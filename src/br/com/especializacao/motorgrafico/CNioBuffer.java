/********************************************
 Programa: Prototipo1
 Descri��o: apresenta o prototipo de um jogo
 Autor: Silvano Maneck Malfatti
 Local: Faculdade Catolica do Tocantins
 ********************************************/

//Pacote da aplicacao
package br.com.especializacao.motorgrafico;


//Bibliotecas uitlizadas
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CNioBuffer 
{
	//Metodo estatico que cria um Nio Buffer com base no vetor passado
	public static FloatBuffer geraNioBuffer(float[] vetorCoordenadas)
	{
		//Cria o vetor de bytes para alocar a memoria necessaria
		ByteBuffer vrByteBuffer = ByteBuffer.allocateDirect(vetorCoordenadas.length * 4);
		vrByteBuffer.order(ByteOrder.nativeOrder());
				
		//Obtem um vetor de float a partir do vetor de bytes alocado
		FloatBuffer vrFloatBuffer = vrByteBuffer.asFloatBuffer();
		vrFloatBuffer.clear();	
							
		//Encapsula o vetor de coordenadas Java no objeto alocado previamente
		vrFloatBuffer.put(vetorCoordenadas);
				
		//Posicao atual 0 e limite para 5
		vrFloatBuffer.flip();
		
		return vrFloatBuffer;
	}
}
