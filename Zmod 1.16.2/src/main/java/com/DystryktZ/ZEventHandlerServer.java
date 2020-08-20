package com.DystryktZ;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.DystryktZ.ranking.ServerRanking;

public class ZEventHandlerServer {
	
	private static long save_timer = 100000; //100sekund
	public static ServerRanking ranking;

	private static ScheduledExecutorService serverSchedule;
	private static ZThread zthread; 
	
	public ZEventHandlerServer()
	{
		ranking = new ServerRanking();
		zthread = new ZThread();
		serverSchedule = Executors.newSingleThreadScheduledExecutor();
		serverSchedule.scheduleAtFixedRate(zthread, 0, save_timer, TimeUnit.MILLISECONDS);
		//serverThread = new Thread(new ZThread());
		//serverThread.start();
		//load
	}
	
	public static void setTimer(long time)
	{
		save_timer = time;
		serverSchedule.scheduleAtFixedRate(zthread, 0, save_timer, TimeUnit.MILLISECONDS);
	}
	
	
	private class ZThread implements Runnable
	{	
		@Override
		public void run() {
			try {
				System.out.println("{Saving ranking...}");
				ranking.GenerateServerRank();
				ranking.saveToJson();
				Thread.sleep(save_timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}


}
