package com.easyway.eds.launcher;

import java.io.File;
import java.io.IOException;

import de.idos.updates.UpdateAvailability;
import de.idos.updates.UpdateSystem;
import de.idos.updates.Updater;
import de.idos.updates.configuration.ConfiguredUpdateSystem;

public class EDSBootLoader {

	public static final String MAIN_CLASS = "com.easyway.eds.Application";
	public static final String MAIN_METHOD = "start";

	public static void main(String[] arguments) throws Exception {
		Splash splash = new Splash();
		splash.setVisible(true);

		UpdateSystem updateSystem = ConfiguredUpdateSystem.loadProperties().create();
		ConsoleReport report = new ConsoleReport(splash);
		updateSystem.reportAllProgressTo(report);
		Updater updater = updateSystem.checkForUpdates();
		updater.runCheck();
		if (UpdateAvailability.Available == updater.hasUpdate()) {
			updater.updateToLatestVersion();
			System.out.println("\tInstalled version: " + updater.getInstalledVersion().asString());
			System.out.println("\tLastest version: " + updater.getLatestVersion().asString());
			System.out.println("\tUpdated: " + updater.hasUpdate());
			System.out.println("\tHas update? " + updater.hasUpdate());
			report.setCallback(new Callback() {

				@Override
				public void doProcess() {
					File versionFolder = updateSystem.getFolderForVersionToRun();
					try {
						runJar(versionFolder);
						Thread.sleep(1000);
						splash.dispose();
						System.exit(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			File versionFolder = updateSystem.getFolderForVersionToRun();
			System.out.println(versionFolder.getAbsolutePath());
			runJar(versionFolder);
			Thread.sleep(1000);
			splash.dispose();
			System.exit(1);
		}

	}

	private static void runJar(File folder){
		File[] files = folder.listFiles();
		System.out.println(files[0].getAbsolutePath());
		try {
			Runtime.getRuntime().exec("java -jar " + files[0].getAbsolutePath());
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}