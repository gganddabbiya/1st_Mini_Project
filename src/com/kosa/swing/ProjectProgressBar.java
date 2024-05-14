package com.kosa.swing;

import javax.swing.JProgressBar;

public class ProjectProgressBar {
	public static JProgressBar createProjectProgressBar(int taskCount, int completeTaskCount) {
		double progressPercentage = ((double) completeTaskCount / taskCount) * 100;
		
		JProgressBar progressBar = new JProgressBar(0, 100); 
		progressBar.setString(completeTaskCount + "/" + taskCount);
		progressBar.setStringPainted(true);
		progressBar.setValue((int)progressPercentage);
		
		return progressBar;
	}
	
}
