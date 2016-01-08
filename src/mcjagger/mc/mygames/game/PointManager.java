package mcjagger.mc.mygames.game;

import java.math.BigInteger;
import java.util.Collection;
import java.util.UUID;

public interface PointManager {
	
	public static enum Ranking {
		HIGHEST(1),
		LOWEST(-1);
		
		private int direction;
		
		Ranking(int direction) {
			this.direction = direction;
		}
		
		public boolean isFirstBetter(BigInteger val0, BigInteger val1) {
			
			int compare = val0.compareTo(val1);
			
			if (direction > 0)
				return compare > 0;
			else if (direction < 0)
				return compare < 0;
			else 
				return compare == 0;
		}
		
	}
	
	public void addPoints(UUID uuid, int points);
	public void removePoints(UUID uuid, int points);
	
	public void setRanking(Ranking ranking);
	public Ranking getRanking();
	
	public Collection<String> getWinners();

}
