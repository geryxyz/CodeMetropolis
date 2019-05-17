package codemetropolis.toolchain.rendering.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import codemetropolis.blockmodifier.World;
import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.BuildableTree;
import codemetropolis.toolchain.commons.cmxml.exceptions.CmxmlReaderException;
import codemetropolis.toolchain.rendering.events.ProgressEvent;
import codemetropolis.toolchain.rendering.events.ProgressEventListener;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.exceptions.RenderingException;
import codemetropolis.toolchain.rendering.exceptions.TooLongRenderDurationException;
import codemetropolis.toolchain.rendering.model.building.*;
import codemetropolis.toolchain.rendering.model.primitive.Boxel;

public class WorldBuilder {

	private static final int GROUND_LEVEL = 60;
	
	private World world;
	private List<Building> buildings = new ArrayList<Building>();
	private StopWatch stopWatch = new StopWatch();

	private int count = 0;
	private int total = 0;
	
	public WorldBuilder(String worldPath) {
		world = new World(worldPath, GROUND_LEVEL);
	}
	
	public void createBuildings(String inputPath) throws Exception{
		BuildableTree buildables = new BuildableTree();
		try {
			buildables.loadFromFile(inputPath);
		} catch (CmxmlReaderException e) {
			e.printStackTrace();
			return;
		}
		
		List<Floor> floors = new ArrayList<Floor>();
		List<Cellar> cellars = new ArrayList<Cellar>();
		List<Garden> gardens = new ArrayList<Garden>();
		List<Ground> grounds = new ArrayList<Ground>();

		for(Buildable b : buildables.getBuildables()) {
			switch(b.getType()) {
				case FLOOR: 
					Floor floor = new Floor(b);
					floors.add(floor);
					total += floor.getNumberOfBlocks();
					break;
				case CELLAR: 
					Cellar cellar = new Cellar(b);
					cellars.add(cellar);
					total += cellar.getNumberOfBlocks();
					break;
				case GARDEN: 
					Garden garden = new Garden(b);
					gardens.add(garden);
					total += garden.getNumberOfBlocks();
					break;
				case GROUND:
					Ground ground = new Ground(b);
					grounds.add(ground);
					total += ground.getNumberOfBlocks();
					break;
				case CONTAINER:
					break;
			}
		}
		
		buildings.addAll(grounds);
		buildings.addAll(gardens);
		buildings.addAll(cellars);
		buildings.addAll(floors);
		
		raiseProgressEvent(BuildPhase.READING_INPUT_FILE, 1, 1, -1);
	}
	
	public void createBlocks(File directory, int maxTime) throws TooLongRenderDurationException {
		raiseProgressEvent(BuildPhase.GENERATING_BLOCKS, 0, total, 0);
		stopWatch.reset();
		stopWatch.start();
		for(Building b : buildings) {
			count += b.toCSVFile(directory);
			long timeElapsed = stopWatch.getTime();
			int timeLeftInMinutes = (int) ((double)timeElapsed / count * (total - count)) / (1000 * 60);
			if(timeLeftInMinutes > maxTime) throw new TooLongRenderDurationException(timeLeftInMinutes, maxTime);
			raiseProgressEvent(BuildPhase.GENERATING_BLOCKS, count, total, timeElapsed);
		}
		stopWatch.stop();
	}
	
	public void build(File sourceDirectory) throws RenderingException {
		
		if(!sourceDirectory.exists()) {
			return;
		}
		
		raiseProgressEvent(BuildPhase.PLACING_BLOCKS, 0, total, 0);
        count = 0;
        stopWatch.reset();
		stopWatch.start();
        
        for(File f : sourceDirectory.listFiles()) {
            if(f.getName().matches("blocks\\.-?[0-9]*\\.-?[0-9]*.csv")) {
                try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
                	String blockString = reader.readLine();
                	while(blockString != null) {
                		Boxel boxel = Boxel.parseCSV(blockString);
                		boxel.render(world);
                		count++;
                		blockString = reader.readLine();
                	}
                } catch (IOException e) {
					throw new RenderingException(e);
				}
            }
            long timeSpent = stopWatch.getTime();
			raiseProgressEvent(BuildPhase.PLACING_BLOCKS, count, total, timeSpent);
        }
        
        world.finish();
        stopWatch.stop();
        raiseProgressEvent(BuildPhase.PLACING_BLOCKS, total, total, stopWatch.getTime());
	}
	
	public int getNumberOfBuildings() {
		return buildings.size();
	}
	
	public int getNumberOfBlocks() {
		return total;
	}
	
	public long getTimeElapsedDuringLastPhase() {
		return stopWatch.getTime();
	}
	
	//region PROGRESS EVENT
	private List<EventListener> listeners = new ArrayList<EventListener>();
	
	public synchronized void addEventListener(ProgressEventListener listener)  {
		listeners.add(listener);
	}
	
	public synchronized void removeEventListener(ProgressEventListener listener)   {
		listeners.remove(listener);
	}
	 
	private synchronized void raiseProgressEvent(BuildPhase phase, long count, long total, long timeElapsedInMillis) {
		ProgressEvent event = new ProgressEvent(this, phase, count, total, timeElapsedInMillis);
		
		for(EventListener listener : listeners) {
			((ProgressEventListener) listener).onNextState(event);
		}
    }
	//endregion
	
}
