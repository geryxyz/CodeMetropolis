package codemetropolis.toolchain.placing.layout.pack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import codemetropolis.toolchain.commons.cmxml.Attribute;
import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.BuildableTree;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.commons.cmxml.comparators.BuildableSizeComparator;
import codemetropolis.toolchain.placing.exceptions.LayoutException;
import codemetropolis.toolchain.placing.layout.Layout;
import codemetropolis.toolchain.placing.layout.pack.RectanglePacker.Rectangle;

public class PackLayout extends Layout {
	
	public static final int TUNNEL_WIDTH = 2;
	public static final int TUNNEL_HEIGHT = 4;
	public static final String TUNNEL_ATTRIBUTE_TARGET = "target";
	
	private final int SPACE = 3;
	
	@Override
	public void apply(BuildableTree buildables) throws LayoutException {
		prepareBuildables(buildables);
		Map<Buildable, List<House>> houses = createHouses(buildables);
		BuildableWrapper root = new BuildableWrapper(buildables.getRoot());
		packRecursive(root, houses);
		
		makeTunnels(buildables);
	}
	
	private void makeTunnels(BuildableTree buildables) {
		
		List<Buildable> extendedBuildables = new ArrayList<Buildable>();
		
		for(Buildable b : buildables.getBuildables()) {
			if(b.getType() != Buildable.Type.TUNNEL) {
				continue;
			}
			
			Buildable parent = b.getParent();
			String id = b.getAttributeValue(TUNNEL_ATTRIBUTE_TARGET);
			
			if(id == null) {
				continue;
			}
			
			Buildable target = buildables.getBuildable(id);
			
			if(target == null) {
				continue;
			}
			
			b.setSizeY(TUNNEL_HEIGHT);

			if(parent.getPositionX()/2 == target.getPositionX()/2) {
				
				b.setPositionX(parent.getPositionX() + parent.getSizeX()/2 - TUNNEL_WIDTH/2);
				b.setSizeX(TUNNEL_WIDTH);
				
				int distance = target.getPositionZ() - parent.getPositionZ() + Math.abs(parent.getSizeZ() - target.getSizeZ());
				
				if(parent.getPositionZ()/2 < target.getPositionZ()/2) {
					// P - parent, T - target, X - undefined
					// Position:
					// X X X
					// X P X
					// X T X
					// X X X
					
					b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2);
					b.setSizeZ(distance);
					
					//b.addAttribute(new Attribute("orientation", "south"));
					b.addAttribute(new Attribute("standalone", "true"));
					
					
				} else {
					// Position:
					// X X X
					// X T X
					// X P X
					// X X X
					
					b.setPositionZ(target.getPositionZ() + target.getSizeZ()/2);
					b.setSizeZ(-distance);
					
					//b.addAttribute(new Attribute("orientation", "north"));
					b.addAttribute(new Attribute("standalone", "true"));
					
				}
			} else if (parent.getPositionZ()/2 == target.getPositionZ()/2) {
				
				b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2 - TUNNEL_WIDTH/2);
				b.setSizeZ(TUNNEL_WIDTH);
				
				int distance = target.getPositionX() - parent.getPositionX() + Math.abs(parent.getSizeX() - target.getSizeX());
				
				if(parent.getPositionX() < target.getPositionX()) {
					// Position:
					// X X X X
					// X P T X
					// X X X X
					
					b.setPositionX(parent.getPositionX() + parent.getSizeX()/2);
					b.setSizeX(distance);
					
					//b.addAttribute(new Attribute("orientation", "east"));
					
				} else {
					// Position:
					// X X X X
					// X T P X
					// X X X X
					
					b.setPositionX(target.getPositionX() + target.getSizeX()/2);
					b.setSizeX(-distance);
					
					//b.addAttribute(new Attribute("orientation", "west"));
				}
			} else if(parent.getPositionX()/2 > target.getPositionX()/2) {
				
				b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2);
				b.setSizeZ(TUNNEL_WIDTH);
				
				b.setPositionX(target.getPositionX() + target.getSizeX()/2 - TUNNEL_WIDTH/2);
				b.setSizeX(parent.getPositionX() - target.getPositionX());
				
				//b.addAttribute(new Attribute("orientation", "west"));
				
				Buildable new_b = new Buildable(UUID.randomUUID().toString(), "", Buildable.Type.TUNNEL);
				new_b.setPositionX(target.getPositionX() + target.getSizeX()/2 - TUNNEL_WIDTH/2);
				new_b.setSizeX(TUNNEL_WIDTH);
				
				int distance = parent.getPositionZ() - target.getPositionZ() + Math.abs(parent.getSizeZ() - target.getSizeZ());
				
				if(parent.getPositionZ()/2 > target.getPositionZ()/2) {
					// Position:
					// X X X X
					// X T X X
					// X X P X
					// X X X X
					
					new_b.setPositionZ(target.getPositionZ() + target.getSizeZ()/2);
					new_b.setSizeZ(distance);
					
					//new_b.addAttribute(new Attribute("orientation", "north"));
					
				} else {
					// Position:
					// X X X X
					// X X P X
					// X T X X
					// X X X X
					
					new_b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2);
					new_b.setSizeZ(-distance);
					
					//new_b.addAttribute(new Attribute("orientation", "south"));
				}
				
				// SAMPLE
				new_b.setPositionY(0);
				new_b.setSizeY(TUNNEL_HEIGHT);
				
				extendedBuildables.add(new_b);
				
				target.addAttribute(new Attribute("target", ""));
				target.addChild(new_b);
				
			} else {
				b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2 - TUNNEL_WIDTH/2);
				b.setSizeZ(TUNNEL_WIDTH);
				
				b.setPositionX(parent.getPositionX() + parent.getSizeX()/2 - TUNNEL_WIDTH/2);
				b.setSizeX(target.getPositionX() - parent.getPositionX());
				
				b.addAttribute(new Attribute("orientation", "east"));
				
				Buildable new_b = new Buildable(UUID.randomUUID().toString(), "", Buildable.Type.TUNNEL);
				new_b.setPositionX(target.getPositionX() + target.getSizeX()/2 - TUNNEL_WIDTH/2);
				new_b.setSizeX(TUNNEL_WIDTH);
				
				int distance = target.getPositionZ() - parent.getPositionZ() + Math.abs(parent.getSizeZ() - target.getSizeZ()) - TUNNEL_WIDTH/2;
				
				if(parent.getPositionZ()/2 > target.getPositionZ()/2) {
					// Position:
					// X X X X
					// X X T X
					// X P X X
					// X X X X
					
					new_b.setPositionZ(target.getPositionZ() + target.getSizeZ()/2 - TUNNEL_WIDTH/2);
					new_b.setSizeZ(-distance);
					
					//new_b.addAttribute(new Attribute("orientation", "north"));

				} else {
					// Position:
					// X X X X
					// X P X X
					// X X T X
					// X X X X
					
					new_b.setPositionZ(parent.getPositionZ() + parent.getSizeZ()/2 - TUNNEL_WIDTH/2);
					new_b.setSizeZ(distance);
					
					//new_b.addAttribute(new Attribute("orientation", "south"));
					
				}
				
				new_b.setSizeY(TUNNEL_HEIGHT);
				
				extendedBuildables.add(new_b);
				
				target.addChild(new_b);
			}
		}
		buildables.getBuildables().addAll(extendedBuildables);
	}
	
	
	private void prepareBuildables(BuildableTree buildables) {
		for(Buildable b : buildables.getBuildables()) {
				if(b.isRoot()) continue;
				b.setPositionYR(b.getParent().getPositionY() + 1);
		}
		
		buildables.getRoot().setPositionYR(GROUND_LEVEL);
	}
	
	private Point getMaxSizes(Collection<BuildableWrapper> buildables) {
		int maxX = 0, maxZ = 0;
		for(BuildableWrapper b : buildables) {
			if(b.getSizeX() > maxX) maxX = b.getSizeX();
			if(b.getSizeZ() > maxZ) maxZ = b.getSizeZ();
		}
		return new Point(maxX, 0, maxZ);
	}
	
	public void packRecursive(BuildableWrapper root, Map<Buildable, List<House>> houses) {
		List<BuildableWrapper> tempChildren = root.getChildren(houses);
		List<BuildableWrapper> children = new ArrayList<BuildableWrapper>();
		
		for(BuildableWrapper c : tempChildren) {
			if (c.buildable instanceof Buildable && ((Buildable)c.buildable).getType() == Buildable.Type.TUNNEL) {
				continue;
			} 
			children.add(c);
		}
		
		for(BuildableWrapper c : children) {
			
			if(!c.getChildren(houses).isEmpty()) {
				packRecursive(c, houses);
			}
		}
		Collections.sort(children);
		Collections.reverse(children);
		pack(children, SPACE);
	}
	
	private void pack(Collection<BuildableWrapper> buildables, int space) {
		Point startingSize = getMaxSizes(buildables);
		pack(buildables, startingSize.getX(), startingSize.getZ(), space);
	}
	
	private void pack(Collection<BuildableWrapper> buildables, int sizeX, int sizeZ, int space) {
		RectanglePacker<BuildableWrapper> packer = new RectanglePacker<BuildableWrapper>(sizeX, sizeZ, space);
		
		for(BuildableWrapper b : buildables) {
			if(packer.insert(b.getSizeX(), b.getSizeZ(), b) == null) {
				if(sizeX > sizeZ) {
					sizeZ++;
				} else {
					sizeX++;
				}
				pack(buildables, sizeX, sizeZ,  space);
				return;
			};
		}

		for(BuildableWrapper b : buildables) {
			Rectangle r = packer.findRectangle(b);
			b.setPositionX(r.x + space);
			b.setPositionZ(r.y + space);
		}
		
		if(!buildables.isEmpty()) {
			Point parentSize = calculateParentSize(buildables, space);
			Buildable parent = buildables.iterator().next().getParent();
			parent.setSizeX(parentSize.getX());
			parent.setSizeZ(parentSize.getZ());
		}
	}
	
	private Point calculateParentSize(Collection<BuildableWrapper> buildables, int space) {
		BuildableWrapper firstChild = buildables.iterator().next();
		int minX = firstChild.getPositionX();
		int maxX = firstChild.getPositionX() + firstChild.getSizeX();
		int minZ = firstChild.getPositionZ();
		int maxZ = firstChild.getPositionZ() + firstChild.getSizeZ();
		
		for(BuildableWrapper b : buildables) {
			if(b.getPositionX() < minX) minX = b.getPositionX();
			if(b.getPositionX() + b.getSizeX() > maxX) maxX = b.getPositionX() + b.getSizeX();
			if(b.getPositionZ() < minZ) minZ = b.getPositionZ();
			if(b.getPositionZ() + b.getSizeZ() > maxZ) maxZ = b.getPositionZ() + b.getSizeZ();
		}
		
		return new Point(
				maxX - minX + 2 * space,
				0,
				maxZ - minZ + 2 * space
				);
	}
	
	private void sort(List<Buildable> buildables) {
		Collections.sort(buildables, new BuildableSizeComparator());
		Collections.reverse(buildables);
	}
	
	private List<Buildable> getFloorsAndCellarsSorted(BuildableTree buildables) {
		List<Buildable> result = new ArrayList<Buildable>();
		for(Buildable b : buildables.getBuildables()) {
			if(b.getType() != Buildable.Type.FLOOR && b.getType() != Buildable.Type.CELLAR) continue;
			result.add(b);
		}
		sort(result);
		return result;
	}
	
	private Map<Buildable, List<House>> createHouses(BuildableTree buildables) throws LayoutException {
		Map<Buildable, List<House>> houses = new HashMap<Buildable, List<House>>();
		for(Buildable b : getFloorsAndCellarsSorted(buildables)) {
			if(b.getType() != Buildable.Type.FLOOR && b.getType() != Buildable.Type.CELLAR) continue;
			List<House> housesOfParent = houses.get(b.getParent());
			if(housesOfParent == null) {
				housesOfParent = new ArrayList<House>();
				houses.put(b.getParent(), housesOfParent);
			}
			
			boolean addedSuccessfully = false;
			for(House h : housesOfParent) {
				if(h.add(b)) {
					addedSuccessfully = true;
					break;
				}
			}
			
			if(!addedSuccessfully) {
				House h = new House(MIN_HEIGHT, MAX_HEIGHT);
				h.add(b);
				housesOfParent.add(h);
			}
		}
		return houses;
	}
}
