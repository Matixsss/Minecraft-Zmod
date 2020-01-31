package com.DystryktZ;

import com.DystryktZ.ranking.ServerRanking;

public class ZEventHandlerServer {
	
	private static long save_timer = 100000; //100sekund
	public static ServerRanking ranking;
	private Thread serverThread;
	
	public static void setTimer(long time)
	{
		save_timer = time;
	}
	

	public ZEventHandlerServer()
	{
		ranking = new ServerRanking();
		serverThread = new Thread(new ZThread());
		serverThread.start();
		//load
	}
	
	public void startThread()
	{
		serverThread.start();
	}
	
	private class ZThread implements Runnable
	{
		private boolean running = true;
		
		/*public void setRunning(boolean result)
		{
			running = result;
		}*/
		
		
		@Override
		public void run() {
			while(running)
			{
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


}
