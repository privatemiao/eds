package com.easyway.eds.launcher;

import de.idos.updates.Version;
import de.idos.updates.store.ProgressReport;

public class ConsoleReport implements ProgressReport {
	private Callback callback;
	private Messenger messenger;

	public ConsoleReport(Messenger messenger) {
		this.messenger = messenger;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void lookingUpLatestAvailableVersion() {
		System.out.println("Looking for latest available version");
		this.messenger.send("Looking for latest available version");
	}

	@Override
	public void latestAvailableVersionIs(Version value) {
		System.out.println("Found version: " + value);
		this.messenger.send("Found version: " + value);
	}

	@Override
	public void versionLookupFailed() {
		System.out.println("Could not determine latest version.");
		this.messenger.send("Could not determine latest version.");
	}

	@Override
	public void versionLookupFailed(Exception e) {
		System.out.println("Could not determine latest version: " + e.getMessage());
		this.messenger.send("Could not determine latest version: " + e.getMessage());
	}

	@Override
	public void startingInstallationOf(Version version) {
		System.out.println();
		System.out.println("Starting installation of " + version.asString());
		this.messenger.send("Starting installation of " + version.asString());
	}

	@Override
	public void assemblingFileList() {
		System.out.println("Assembling list of files to install.");
		this.messenger.send("Assembling list of files to install.");
	}

	@Override
	public void foundElementsToInstall(int numberOfElements) {
		System.out.println("Found " + numberOfElements + " files to install.");
		this.messenger.send("Found " + numberOfElements + " files to install.");
	}

	@Override
	public void installingFile(String name) {
		System.out.println("Installing " + name);
		this.messenger.send("Installing " + name);
	}

	@Override
	public void expectedSize(long size) {
		System.out.println("Downloading " + size + " bytes");
		this.messenger.send("Downloading " + size + " bytes");
	}

	@Override
	public void progress(long progress) {
		System.out.print(".");
	}

	@Override
	public void finishedFile() {
		System.out.println("Done.");
		this.messenger.send("Done.");
	}

	@Override
	public void finishedInstallation() {
		System.out.println("Finished installation.");
		this.messenger.send("Finished installation.");
		if (callback != null) {
			callback.doProcess();
			callback = null;
		}
	}

	@Override
	public void installationFailed(Exception e) {
		System.out.println("Installation failed: " + e.getMessage());
		this.messenger.send("Installation failed: " + e.getMessage());
	}

	@Override
	public void updateAlreadyInProgress() {
		System.out.println("Installation failed: An installation is already in progress.");
		this.messenger.send("Installation failed: An installation is already in progress.");
	}
}