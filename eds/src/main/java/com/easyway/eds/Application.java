package com.easyway.eds;

import java.awt.AWTException;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Application {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private static final int DEFAULT_PORT = 8888;
	private static final String TITLE = "Easyway Deamon Service";

	private JFrame frame = null;

	private JLabel lblQutity = new JLabel("0");
	private JLabel lblCPUUsage = new JLabel("0");
	private JLabel lblSerialNumber = new JLabel("----");
	private JLabel lblTotalSpace = new JLabel("0");
	private JLabel lblFreeSpace = new JLabel("0");
	private JLabel lblTotalMemory = new JLabel("0");
	private JLabel lblFreeMemory = new JLabel("0");
	private JLabel lblMemoryUsage = new JLabel("0");
	
	private JButton btnRefresh = new JButton("Refresh");

	private void createAndShowGUI() {
		frame = new JFrame(TITLE);
		try {
			frame.setIconImage(ImageIO.read(getClass().getResourceAsStream("/logo.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(false);

		if (SystemTray.isSupported()) {
			createTray();
		}

		frame.setLayout(new GridLayout(4, 1));

		JPanel panelCPU = new JPanel(new GridLayout(2, 2));
		panelCPU.add(new JLabel("Qutity: "));
		panelCPU.add(lblQutity);
		panelCPU.add(new JLabel("Usage: "));
		panelCPU.add(lblCPUUsage);
		panelCPU.setBorder(BorderFactory.createTitledBorder("CPU"));
		frame.add(panelCPU);

		JPanel panelDisk = new JPanel(new GridLayout(3, 2));
		panelDisk.add(new JLabel("SerialNumber: "));
		panelDisk.add(lblSerialNumber);
		panelDisk.add(new JLabel("TotalSpace: "));
		panelDisk.add(lblTotalSpace);
		panelDisk.add(new JLabel("FreeSpace: "));
		panelDisk.add(lblFreeSpace);
		panelDisk.setBorder(BorderFactory.createTitledBorder("Disk"));
		frame.add(panelDisk);

		JPanel panelMemory = new JPanel(new GridLayout(3, 2));
		panelMemory.add(new JLabel("TotalMemory: "));
		panelMemory.add(lblTotalMemory);
		panelMemory.add(new JLabel("FreeMemory: "));
		panelMemory.add(lblFreeMemory);
		panelMemory.add(new JLabel("Useage: "));
		panelMemory.add(lblMemoryUsage);
		panelMemory.setBorder(BorderFactory.createTitledBorder("Memory"));
		frame.add(panelMemory);
		
		frame.add(btnRefresh);
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshInfo();
				btnRefresh.setEnabled(false);
			}
		});

		refreshInfo();
	}

	private void refreshInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HardwareInfoDTO info = Service.getInstance().getHardwareInfo();
				lblQutity.setText("" + info.getCpuInfo().getQuantity());
				lblCPUUsage.setText("" + info.getCpuInfo().getUsedrate() + "%");
				lblSerialNumber.setText(info.getDiskInfo().getSerialNumber());
				lblTotalSpace.setText("" + info.getDiskInfo().getTotalSpace() + "G");
				lblFreeSpace.setText("" + info.getDiskInfo().getFreeSpace() + "G");
				lblTotalMemory.setText("" + info.getMemoryInfo().getTotalMemory() + "M");
				lblFreeMemory.setText("" + info.getMemoryInfo().getFreeMemory() + "M");
				lblMemoryUsage.setText("" + info.getMemoryInfo().getUsedrate() + "%");
				if (!btnRefresh.isEnabled()){
					btnRefresh.setEnabled(true);
				}
			}
		}).start();

	}

	private void createTray() {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/hdd.png"));
		TrayIcon trayIcon = new TrayIcon(image, "Show/Hide");

		PopupMenu popupMenu = new PopupMenu();
		MenuItem menuItem = new MenuItem("Exit");
		popupMenu.add(menuItem);
		trayIcon.setPopupMenu(popupMenu);

		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		trayIcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					frame.setVisible(!frame.isVisible());
					if (frame.isVisible()) {
						refreshInfo();
					}
				}
			}
		});
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});

		try {
			startHttpServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startHttpServer() throws IOException {
		String _port = System.getenv("EDS_PORT");
		int port = DEFAULT_PORT;
		if (_port != null && _port.trim().length() > 0) {
			port = Integer.parseInt(_port.trim());
		}
		InetSocketAddress addr = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(addr, 0);

		server.createContext("/hardware", new HttpHandler() {

			@Override
			public void handle(HttpExchange exchange) throws IOException {
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = null;
					ObjectMapper mapper = new ObjectMapper();
					try {
						Map<String, String> params = parseParameters(exchange.getRequestURI().getQuery());
						responseBody = exchange.getResponseBody();
						String jsonpPrefixed = params.get("callback");
						if (jsonpPrefixed == null) {
							mapper.writeValue(responseBody, Service.getInstance().getHardwareInfo());
						} else {
							String jsonStr = mapper.writeValueAsString(Service.getInstance().getHardwareInfo());
							String jsonp = jsonpPrefixed + "(" + jsonStr + ")";
							responseBody.write(jsonp.getBytes());
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (responseBody != null) {
							responseBody.close();
						}
					}
				}
			}
		});
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port " + port);
	}

	private Map<String, String> parseParameters(String query) {
		if (query == null) {
			return Collections.emptyMap();
		}

		Map<String, String> params = new HashMap<>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			String[] keyVals = pair.split("=");
			params.put(keyVals[0], keyVals[1]);
		}

		return params;
	}

	public static void main(String[] args) {
		new Application().start();
	}
}
