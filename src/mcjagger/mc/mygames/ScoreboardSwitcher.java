package mcjagger.mc.mygames;

import java.util.UUID;

public abstract class ScoreboardSwitcher {
	
	public abstract void showNow(UUID uuid, ScoreboardProvider sp, int tickDuration);	
	
	public abstract void addBoard(UUID uuid, ScoreboardProvider sp);
	public abstract void removeBoard(UUID uuid, ScoreboardProvider sp);
	
	public abstract void removeAll(ScoreboardProvider sp);
	
}
