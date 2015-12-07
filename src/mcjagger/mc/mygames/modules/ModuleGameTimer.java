package mcjagger.mc.mygames.modules;

import org.bukkit.Bukkit;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.GameModule;
import mcjagger.mc.mygames.MyGames;

public abstract class ModuleGameTimer extends GameModule {

	public abstract void tick(boolean isWarmup);
	
	private final Game game;
	private int warmupTime = 0;
	private int runTime = 0;
	private int ticks = 0;
	private int period = 100;
	
	public ModuleGameTimer(Game game, int warmupTime, int runTime) {
		this(game, warmupTime, runTime, 100);
	}
	
	public ModuleGameTimer(Game game, int warmupTime, int runTime, int period) {
		super(game, false);
		
		if (period <= 0)
			throw new IllegalArgumentException("Period must be positive.");
		
		if (warmupTime < 0)
			throw new IllegalArgumentException("Warmup time may not be negative.");
		
		if (runTime <= 0)
			throw new IllegalArgumentException("Run time must be positive.");
		
		this.game = game;
		this.warmupTime = warmupTime;
		this.runTime = runTime + warmupTime;
	}
	
	private int taskId = -1;
	private Runnable runnable = new Runnable(){
		
		@Override
		public void run() {
			if (ticks >= runTime) {
				game.stop();
			} else if (ticks >= warmupTime) {
				tick(false);
			} else {
				tick(true);
			}
			
			ticks += period;
		}
		
	};
	
	public int getTicks() {
		return ticks;
	}
	
	public int getPeriod() {
		return period;
	}

	/* (non-Javadoc)
	 * @see mcjagger.mc.mygames.GameModule#started()
	 */
	@Override
	public void started() {
		super.started();
		
		taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
				MyGames.getArcade(), runnable, 0, period).getTaskId();
	}
	
	/* (non-Javadoc)
	 * @see mcjagger.mc.mygames.GameModule#stopped()
	 */
	@Override
	public void stopped() {
		super.stopped();
		
		if (taskId != -1)
			Bukkit.getScheduler().cancelTask(taskId);
		
		taskId = -1;
	}

}
