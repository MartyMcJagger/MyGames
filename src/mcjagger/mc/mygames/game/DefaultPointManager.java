package mcjagger.mc.mygames.game;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultPointManager implements PointManager {
	
	private Ranking ranking = Ranking.HIGHEST;
	private HashMap<UUID, BigInteger> map = new HashMap<UUID, BigInteger>();

	public DefaultPointManager() {}
	
	public DefaultPointManager(Ranking ranking) {
		this.ranking = ranking;
	}

	@Override
	public void addPoints(UUID uuid, int points) {
		BigInteger p = map.get(uuid);
		
		if (p == null)
			p = BigInteger.ZERO;
		
		p = p.add(BigInteger.valueOf(points));
		map.put(uuid, p);
	}

	@Override
	public void removePoints(UUID uuid, int points) {
		addPoints(uuid, -1 * points);
	}

	@Override
	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	@Override
	public Ranking getRanking() {
		return ranking;
	}

	@Override
	public Collection<String> getWinners() {
		
		BigInteger max = null;
		Set<UUID> highest = new HashSet<UUID>();
		
		for (Entry<UUID, BigInteger> entry : map.entrySet()) {
			if (max == null && entry.getValue() != null) {
				max = entry.getValue();
				highest = new HashSet<UUID>();
				highest.add(entry.getKey());
				
			} else if (max.equals(entry.getValue())) {
				highest.add(entry.getKey());
				
			} else if (getRanking().isFirstBetter(entry.getValue(), max)) {
				max = entry.getValue();
				highest = new HashSet<UUID>();
				highest.add(entry.getKey());
			}
		}
		
		Set<String> winners = new HashSet<String>();
		
		for (UUID uuid : highest) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				winners.add(player.getDisplayName());
		}
		
		return winners;
	}

}
