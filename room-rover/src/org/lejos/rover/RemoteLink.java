package org.lejos.rover;

public class RemoteLink implements Runnable {

	private Thread thread;
	private Transmitter transmitter;
	private boolean stopRequest=false;
	
	public RemoteLink() {
		thread=new Thread(this);
	}
	
	public void start() {
		thread.start();
	}
	
	@Override
	public void run() {
		
		while(!stopRequest) {
					
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}					
			
			try {
				transmitter=new Transmitter();
				
				transmitter.listen();
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}					

				while(transmitter.isListening()) {					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
								
				while(transmitter.isConnected()) {
					
					transmitter.getMessageCoder().encodeKeepalive();

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					
				}
				
			}
			catch(Throwable t) {
				System.out.println(t.toString());
			}

			try {
				while(transmitter!=null&&transmitter.isConnected()) {
					transmitter.disconnect();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}					
				}
			}
			catch(Throwable t) {
				System.out.println(t.toString());
			}

			
			transmitter=null;
			
		}
		
	}
	
	public boolean isConnected() {
		return transmitter!=null&&transmitter.isConnected();
	}
	
	public MessageCoder getMessageCoder() {
		Transmitter trans=transmitter;
		if(trans!=null) {
			return trans.getMessageCoder();
		} else {
			return null;
		}
	}

	public void stop() {
		stopRequest=true;
		transmitter.disconnect();
		while(thread.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}

}