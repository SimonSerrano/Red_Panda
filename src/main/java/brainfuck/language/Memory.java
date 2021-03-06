package brainfuck.language;

import brainfuck.language.exceptions.OutOfMemoryException;
import brainfuck.language.exceptions.ValueOutOfBoundException;

/**
 * Cette classe permet de gerer la mémoire de notre interpreteur.
 * Elle contient de nombreuses méthodes afin de manipuler la mémoire
 * 
 * @author  Red_Panda
 */

public class Memory {

	public static final int MEMORY_SIZE = 30000;
	public static final short MEMORY_FOR_FUNCTION = 255;
	private short[] mArray;
	private int pointer;
	private int maxArray;

	/**
     * Le constructeur de la classe mémoire
     */
	
	public Memory(boolean memoryForFunction) {
		if(memoryForFunction)
			mArray = new short[MEMORY_FOR_FUNCTION];
		else
			mArray = new short[MEMORY_SIZE - MEMORY_FOR_FUNCTION];
		pointer = 0;
	}

	/**
	 * Methode permettant d'incrementer la case mémoire actuellement pointée
	 * @throws ValueOutOfBoundException
	 */
	public void incr() throws ValueOutOfBoundException {
		if (mArray[pointer] == 255)
			throw new ValueOutOfBoundException();
		mArray[pointer]++;
	}

	/**
	 * Methode permettant de décrementer la case mémoire actuellement pointée
	 * @throws ValueOutOfBoundException
	 */
	public void decr() throws ValueOutOfBoundException {
		if (mArray[pointer] == 0)
			throw new ValueOutOfBoundException();
		mArray[pointer]--;
	}

	/**
	 * Methode permettant de décaler vers la droite la position du pointeur mémoire
	 * @throws OutOfMemoryException
	 */

	public void right() throws OutOfMemoryException {
		if (pointer >= mArray.length - 1)
			throw new OutOfMemoryException();
		pointer++;
		if (pointer >= maxArray)
			maxArray = pointer;
	}

	/**
	 * Methode permettant de décaler vers la gauche la position du pointeur mémoire
	 * @throws OutOfMemoryException
	 */

	public void left() throws OutOfMemoryException {
		if (pointer <= 0)
			throw new OutOfMemoryException();
		pointer--;
	}

	/**
	 * Renvoie l'état de la mémoire actuelle
	 * @return l'état de la mémoire actuelle
	 */

	public String writeStateOfMemory(){
		StringBuilder resume = new StringBuilder();
		for (int i =  0; i <= maxArray; i++){
			if(mArray[i] != 0) {
				resume.append('C');
				resume.append(i);
				resume.append(": ");
				resume.append(mArray[i]);
				resume.append(' ');
			}
		}
		return resume.toString();
	}


	/**
	 * Change la valeur de la case mémoire actuellement pointée
	 * @param value la nouvelle valeur de la case mémoire
	 */
	public void updateMemory(short value) throws ValueOutOfBoundException {
		if(value > 255)
			throw new ValueOutOfBoundException();


		mArray[pointer] = value;
		Metrics.DATA_WRITE++;
	}

	/**
	 * Retourne la position du pointeur mémoire
	 * @return La position du pointeur mémoire
	 */

	public int getPointer(){return pointer;}

	/**
	 * Retourne la valeur de la case mémoire actuellement pointée
	 * @return La valeur de la case mémoire actuellement pointée
	 */

	public short getCellValue() {
		return  mArray[pointer];
	}

	public void setPointer(int value) {
		pointer = value;
	}

	public void resetMemory() {
		mArray = new short[mArray.length];
	}
}
