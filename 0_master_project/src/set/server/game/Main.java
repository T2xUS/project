package set.server.game;

public class Main {
	
	// Main
	public static void main(String[] args) {
		
		/*** Testing Game.class ***/
		Game NewGame = new Game();
		System.out.println("Board: " + NewGame.Board.toString());
		System.out.println("boardString: " + NewGame.boardString());
		System.out.println("Cards in deck: " + NewGame.CardsInDeck());
		
		NewGame.ReplaceCard(NewGame.Board.get(9));
		System.out.println("Board: " + NewGame.Board.toString());
		System.out.println("Cards in deck: " + NewGame.CardsInDeck());
		
		NewGame.RemoveCard(NewGame.Board.get(9));
		System.out.println("Board: " + NewGame.Board.toString());
		System.out.println("Cards in deck: " + NewGame.CardsInDeck());
		
		/*** Testing SetLogic.class ***/
		GameLogic NewLogic = new GameLogic();
		System.out.println("Total # of sets: " + NewLogic.CompleteSetCount);
			
		return;
	}

}
