package mcjagger.mc.mygames;

import java.util.UUID;

public abstract class ScoreboardSwitcher {

	public abstract void setDefaultProvider(ScoreboardProvider sp);
	
	public abstract void setProvider(UUID uuid, ScoreboardProvider sp);
	public abstract void useDefaultProvider(UUID uuid);
	
}
