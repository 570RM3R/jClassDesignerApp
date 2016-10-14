package default_package.files.thread;

import default_package.Application;
import default_package.Bogus;
import default_package.ApplicationLayer;
import default_package.Stage;
import default_package.BorderPane;
import default_package.Thread;

public class ThreadExample extends Application implements ApplicationLayer {

	public String START_TEXT;
	public String PAUSE_TEXT;
	private Stage window;
	private double startTime;
	private double endTime;
	protected int slotRemaining;
	private boolean work;
	private BorderPane appPane;

	public  ThreadExample(double startTime, double endTime, boolean work) {
	}
	public void startwork() {
	}
	public void pauseWork() {
	}
	public boolean sleep(double timeToSleep) {
		return true;
	}
	private void initLayout() {
	}
	private void initHandlers() {
	}
	protected static  int calculateSlot(Thread thread) {
		int returnedObject = 0;
		return returnedObject;
	}
	public void main(String[] args) {
	}
}