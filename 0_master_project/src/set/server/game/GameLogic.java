package set.server.game;

import java.util.ArrayList;

public class GameLogic {
	
	/**********************/
	/** GameLogic fields **/
	/**********************/
	
	protected ArrayList<Card> CompleteCardList;
	protected ArrayList<ArrayList<Card>> SetList, CompleteSetList;
	protected int CompleteSetCount;
	
	/***************************/
	/** GameLogic constructor **/
	/***************************/
	
	public GameLogic() {
		
		CompleteCardList = new ArrayList<Card>();		
		
		// Fill up card list
		for(int numType = 1; numType <= 3; numType++) {
			for(int symType = 1; symType <= 3; symType++) {
				for(int shadType = 1; shadType <= 3; shadType++) {
					for(int colType = 1; colType <= 3; colType++) {
						CompleteCardList.add(new Card(numType, symType, shadType, colType));
					}
				}
			}
		}
		
		// Get complete list of sets and complete set count (debug)
		CompleteSetList = getSets(CompleteCardList);
		CompleteSetCount = getSetCount(CompleteSetList);
	}
	
	/***********************/
	/** GameLogic methods **/
	/***********************/
	
	// Check if triplet is a set
    public boolean isSet(Card a, Card b, Card c) {
    	
		if (!((a.number == b.number) && (b.number == c.number))
				&& !((a.number != b.number) && (b.number != c.number) && (a.number != c.number))) {
			return false;
		}

		if (!((a.symbol == b.symbol) && (b.symbol == c.symbol))
				&& !((a.symbol != b.symbol) && (b.symbol != c.symbol) && (a.symbol != c.symbol))) {
			return false;
		}

		if (!((a.shading == b.shading) && (b.shading == c.shading))
				&& !((a.shading != b.shading) && (b.shading != c.shading) && (a.shading != c.shading))) {
			return false;
		}

		if (!((a.color == b.color) && (b.color == c.color))
				&& !((a.color != b.color) && (b.color != c.color) && (a.color != c.color))) {
			return false;
		}
		
    	return true;
    }
    
    // Get positions of one set of cards that form a set on board
    public ArrayList<Integer> getSetPosition(ArrayList<Card> pool) {
    	
    	ArrayList<Integer> setPosList = new ArrayList<Integer>();
    	
    	synchronized(pool) {
    		
	    	if(pool == null) return setPosList;
	    	
	    	int poolSize = pool.size();
	    	Card noCard = new Card(0,0,0,0);
	    	
	    	for(int ia = 0; ia < poolSize; ia++) {
	    		Card a = pool.get(ia);
	    		if(a.equals(noCard)) continue; // if no card at position, then go to next card
	    		
	    		for(int ib = ia + 1; ib < poolSize; ib++) {
	    			Card b = pool.get(ib);
	    			if(b.equals(noCard)) continue;
	    			
	    			for(int ic = ib + 1; ic < poolSize; ic++) {
	    				Card c = pool.get(ic);
	    				if(c.equals(noCard)) continue;
	    				
	    				if(isSet(a, b, c)) {
	    					setPosList.add(ia);
	    					setPosList.add(ib);
	    					setPosList.add(ic);
	    					break;
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	return setPosList;
    }
    
    
    // Get possible sets from the playing field or in total
    public ArrayList<ArrayList<Card>> getSets(ArrayList<Card> pool) {
    	
    	ArrayList<ArrayList<Card>> setList = new ArrayList<ArrayList<Card>>();
    	
    	synchronized(pool) {
    		
	    	if(pool == null) return setList;
	    	
	    	int poolSize = pool.size();
	    	Card noCard = new Card(0,0,0,0);
	    	
	    	for(int ia = 0; ia < poolSize; ia++) {
	    		Card a = pool.get(ia);
	    		if(a.equals(noCard)) continue; // if no card at position, then go to next card
	    		
	    		for(int ib = ia + 1; ib < poolSize; ib++) {
	    			Card b = pool.get(ib);
	    			if(b.equals(noCard)) continue;
	    			
	    			for(int ic = ib + 1; ic < poolSize; ic++) {
	    				Card c = pool.get(ic);
	    				if(c.equals(noCard)) continue;
	    				
	    				if(isSet(a, b, c)) {
	    					ArrayList<Card> set = new ArrayList<Card>();
	    					set.add(a);
	    					set.add(b);
	    					set.add(c);
	    					setList.add(set);
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	return setList;
    }
    
    // Get number of sets from playing field or in total
    public int getSetCount(ArrayList<ArrayList<Card>> setList) {
    	return setList.size();
    }
    
    // Check if any more sets on field
    public boolean noMoreSets(ArrayList<ArrayList<Card>> setList) {
    	int setCount = getSetCount(setList);
    	if(setCount > 0) {
    		return false;
    	}
    	return true;
    }
}
