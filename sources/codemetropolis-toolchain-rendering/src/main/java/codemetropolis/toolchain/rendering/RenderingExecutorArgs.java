package codemetropolis.toolchain.rendering;

import codemetropolis.toolchain.commons.executor.ExecutorArgs;

public class RenderingExecutorArgs extends ExecutorArgs {
	
	private String inputFile;
	private String worldPath;
	private boolean overwriteSilently;
	private int maxTime;
	
	public RenderingExecutorArgs(String inputFile, String worldPath) {
		this(inputFile, worldPath, false);
	}
	
	public RenderingExecutorArgs(String inputFile, String worldPath, boolean overwriteSilently) {
		
		this(inputFile, worldPath, overwriteSilently, Integer.MAX_VALUE);
	}
	
	public RenderingExecutorArgs(String inputFile, String worldPath, boolean overwriteSilently, int maxTime) {
		super();
		this.inputFile = inputFile;
		this.worldPath = worldPath;
		this.overwriteSilently = overwriteSilently;
		this.maxTime = maxTime;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getWorldPath() {
		return worldPath;
	}

	public boolean isSilentOverwriteEnabled() {
		return overwriteSilently;
	}

	public int getMaxTime() {
		return maxTime;
	}
	
}
