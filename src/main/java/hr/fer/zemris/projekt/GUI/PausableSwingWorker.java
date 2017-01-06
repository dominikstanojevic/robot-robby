package hr.fer.zemris.projekt.GUI;

import javax.swing.SwingWorker;

public abstract class PausableSwingWorker<T, V> extends SwingWorker<T, V> {
	
	private boolean isPaused;
	
	public void pause(){
		if(!isDone()){
			this.isPaused = true;
		}
	}
	
	public void resume(){
		if(!isDone()){
			this.isPaused = false;
		}		
	}
	
	public boolean isPaused(){
		return isPaused;
	}


}
