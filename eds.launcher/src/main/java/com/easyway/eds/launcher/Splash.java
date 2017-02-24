package com.easyway.eds.launcher;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Splash extends JFrame implements Messenger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel imglabel;
	private ImageIcon img;
	private static JProgressBar pbar;
	private JLabel info;
	private int processed = 0;

	public Splash() {
		super("Splash");
		setSize(404, 310);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		img = new ImageIcon(getClass().getResource("/splash.jpg"));
		imglabel = new JLabel(img);
		setLayout(null);
		imglabel.setBounds(0, 0, 404, 310);

		info = new JLabel("Welcome");
		info.setPreferredSize(new Dimension(310, 30));
		info.setBounds(0, 270, 404, 20);
		info.setForeground(Color.GREEN);

		add(info);
		add(imglabel);

		pbar = new JProgressBar();
		pbar.setMinimum(0);
		pbar.setMaximum(100);
		pbar.setStringPainted(true);
		pbar.setForeground(Color.LIGHT_GRAY);
		add(pbar);
		pbar.setPreferredSize(new Dimension(310, 30));
		pbar.setBounds(0, 290, 404, 20);

		new Thread() {
			public void run() {
				int i = 0;
				while (i <= 100) {
					processed = i;
					pbar.setValue(i);
					try {
						sleep(90);
					} catch (InterruptedException e) {
						System.out.println(e);
					}
					i++;
				}
				dispose();
			}
		}.start();
	}

	
	
	
	@Override
	public void dispose() {
		while(processed < 50){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.dispose();
	}




	public void setInfo(String message) {
		info.setText(message);
	}

	@Override
	public void send(String message) {
		setInfo(message);
	}
}
