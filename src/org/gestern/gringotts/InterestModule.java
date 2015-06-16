package org.gestern.gringotts;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.gestern.gringotts.api.Eco;
import org.gestern.gringotts.api.PlayerAccount;
import org.gestern.gringotts.api.TransactionResult;
import org.gestern.gringotts.api.impl.GringottsEco;

public class InterestModule implements Runnable {
	private final Eco eco = new GringottsEco();
	private final double rate = 1.0 / 100.0;
	private long timer = 20 * 60 * 60;

	@SuppressWarnings("deprecation")
	protected void start() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		int minutes = date.getMinutes();
		int seconds = date.getSeconds();
		long delay = 20 * (60 - seconds) * (60 - minutes);
		out(delay);
		Bukkit.getScheduler().runTaskTimer(Gringotts.G, this, delay, timer);
	}

	@Override
	public void run() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			PlayerAccount acc = eco.player(p.getUniqueId());

			if (!acc.exists())
				continue;

			if (acc.vaultBalance() == 0.0)
				continue;

			double balance = acc.balance();
			double interest = Math.floor(balance * rate);
			TransactionResult result = acc.add(interest);

			String message = "You can get it every exact hour, when you are online!";
			switch (result) {
			case SUCCESS:
				message = ChatColor.GREEN + "You earned " + interest
						+ " interest of your " + balance
						+ " balance at a rate of " + rate
						+ "%, which was added to your vault. " + message;
				break;
			case INSUFFICIENT_SPACE:
				message = ChatColor.GREEN + "You earned " + interest
						+ " interest of your " + balance
						+ " balance at a rate of " + rate
						+ "%, which was added to your vault. " + message;
				break;
			default:
				return;
			}
			p.sendMessage(message);

		}
	}

	public void out(Object out) {
		System.out.println(out);
	}

}