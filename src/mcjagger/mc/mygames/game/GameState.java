package mcjagger.mc.mygames.game;

public enum GameState {
	
	COOLING_DOWN(3),
	WARMING_UP(2),
	RUNNING(1),
	STOPPED(0),
	UNDER_MAINTENANCE(-1);
	
	private int id;
	
	private GameState(int id) {
		this.id = id;
	}
	
	public boolean isRunning() {
		return id > 0 && this != COOLING_DOWN;
	}
	
	public boolean canStart() {
		return id == 0;
	}
	
	public boolean canStop() {
		return id > 0 && this != COOLING_DOWN;
	}
	
}
