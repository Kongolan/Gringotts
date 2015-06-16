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
	private final long period = 20 * 60 * 60;

	@SuppressWarnings("deprecation")
	protected void start() {
		Date date = new Date(System.currentTimeMillis());
		int minutes = date.getMinutes();
		int seconds = date.getSeconds();
		long delay = period - (minutes * 60) - (seconds * 20);

		Bukkit.getScheduler().runTaskTimer(Gringotts.G, this, delay, period);
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

			switch (result) {
			case SUCCESS:
				String success = ChatColor.GREEN + "You earned " + interest
						+ " interest of your " + balance
						+ " balance at a rate of " + rate
						+ "%, which was added to your vault.";
				p.sendMessage(success);
				break;
			case INSUFFICIENT_SPACE:
				String no_space = ChatColor.RED + "You earned " + interest
						+ " interest of your " + balance
						+ " balance at a rate of " + rate
						+ "%, but you don't have enough space in your vault";
				p.sendMessage(no_space);
				break;
			default:
				p.sendMessage(ChatColor.RED
						+ "Something happened, which doesn't make sense");
				break;
			}
			String reminder = ChatColor.GREEN
					+ "Remember you can get it every exact hour, when you are online!";
			p.sendMessage(reminder);
		}
	}

	public void out(Object out) {
		System.out.println(out);
	}

}
