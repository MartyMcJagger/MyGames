package mcjagger.mc.mygames.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.game.GameModule;

public abstract class ModuleGameTimer extends GameModule {

	public abstract void tick(boolean isWarmup);
	
	public static final String OBJECTIVE_NAME = "gm.countdown";
	
	private final Game game;
	private int warmupTime = 0;
	private int runTime = 0;
	private int ticks = 0;
	private int period = 100;
	
	private Scoreboard scoreboard;
	
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

		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = scoreboard.registerNewObjective(OBJECTIVE_NAME, "dummy");
		obj.setDisplayName(ChatColor.GRAY + ChatColor.stripColor(game.getName()));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
	}
	
	private int taskId = -1;
	private Runnable runnable = new Runnable(){
		
		@Override
		public void run() {
			if (ticks >= runTime) {
				if (!game.announceWinnersAndStop())
					game.stop(true, true);
				if (taskId != -1)
					Bukkit.getScheduler().cancelTask(taskId);
				taskId = -1;
			} else if (ticks >= warmupTime) {
				tick(false);
			} else {
				tick(true);
			}
			
			ticks += period;
			
			Objective obj = scoreboard.getObjective(OBJECTIVE_NAME);
			obj.getScore("Time Remaining:").setScore((runTime - ticks) / 20);
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
		
		ticks = 0;
		
		taskId = Bukkit.getScheduler().runTaskTimer(
				MyGames.getArcade(), runnable, 0, period).getTaskId();
		
		game.addScoreboard(scoreboard);
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.game.GameModule#stopping()
	 */
	@Override
	public void stopping() {
		super.stopping();
		game.removeScoreboard(scoreboard);
		cancelTimer();
	}
	
	/* (non-Javadoc)
	 * @see mcjagger.mc.mygames.GameModule#stopped()
	 */
	@Override
	public void stopped() {
		super.stopped();
		cancelTimer();
	}
	
	private void cancelTimer() {
		if (taskId != -1)
			Bukkit.getScheduler().cancelTask(taskId);
		
		taskId = -1;
	}
	
}
