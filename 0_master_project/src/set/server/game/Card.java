/*
 *  Card.java
 *  
 *  Class used to implement a card object.
 *  Stores the card's fields, namely its number,
 *  symbol, shading, and color.
 *  
 */

package set.server.game;

public class Card {

	/*****************/
	/** Card fields **/
	/*****************/
	
	protected int number;
	protected int symbol;
	protected int shading;
	protected int color;
	
	/**********************/
	/** Card constructor **/
	/**********************/
	
	public Card(int newNum, int newSym, int newShad, int newCol) {
		number = newNum;
		symbol = newSym;
		shading = newShad;
		color = newCol;
	}
	
	/******************/
	/** Card methods **/
	/******************/
	
	// Card overwritten hashCode
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + color;
		result = prime * result + number;
		result = prime * result + shading;
		result = prime * result + symbol;
		return result;
	}

	// Card overwritten equals
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (color != other.color)
			return false;
		if (number != other.number)
			return false;
		if (shading != other.shading)
			return false;
		if (symbol != other.symbol)
			return false;
		return true;
	}

	// Card overwritten toString
	public String toString() {
		String cardID = Integer.toString(number) + Integer.toString(symbol)
						+ Integer.toString(shading) + Integer.toString(color);
		return cardID;
	}
	
}