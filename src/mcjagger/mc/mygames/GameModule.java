package mcjagger.mc.mygames;

public abstract class GameModule extends Module {
	
	public GameModule(Game game) {
		super(game);
	}
	
	/**
	 * Invoked when Game starts
	 */
	public void started(){}
	/**
	 * Invoked when Game ends
	 */
	public void stopped(){}
}
