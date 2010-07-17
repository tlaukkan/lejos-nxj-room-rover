package org.lejos.rover.mapper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.lejos.rover.math.Vector2f;

/**
 * Class for mapping rooms based on observations.
 * @author Tommi S. E. Laukkanen
 */
public class RoomMapper {
	
	private float mapWidth;
	private float cellWidth;
	private short[] probabilityMap;
	private int cellsPerMapEdge;
	
	public RoomMapper(float mapWidth, float cellWidth) {
		this.mapWidth=mapWidth;
		this.cellWidth=cellWidth;
		this.cellsPerMapEdge=2*(int)((mapWidth)/(2*cellWidth));
		this.mapWidth=cellWidth*cellsPerMapEdge/2;
		probabilityMap=new short[cellsPerMapEdge*cellsPerMapEdge];
		Arrays.fill(probabilityMap, (short)127);
	}
	
	private MapIndex getIndex(Vector2f vector) {
		return new MapIndex(getIndex(vector.getX()),getIndex(vector.getY()));
	}
	
	private Vector2f getVector(MapIndex index) {
		return new Vector2f(getCoordinate(index.getI()),getCoordinate(index.getJ()));
	}
	
	private int getIndex(float coordinate) {
		return (int)((coordinate+mapWidth)/cellWidth);
	}
	
	private float getCoordinate(int index) {
		return index*cellWidth-mapWidth;
	}
	
	private short getProbabilityValue(MapIndex index) {
		return getProbabilityValue(index.getI(),index.getJ());
	}
	
	private short getProbabilityValue(float x, float y) {
		int i=getIndex(x);
		int j=getIndex(y);
		return getProbabilityValue(i,j);
	}
	
	private short getProbabilityValue(int i,int j) {
		return probabilityMap[i+j*cellsPerMapEdge];
	}
	
	private void setProbabilityValue(MapIndex index, short value) {
		setProbabilityValue(index.getI(),index.getJ(),value);
	}
	
	private void setProbabilityValue(float x, float y, short value) {
		int i=getIndex(x);
		int j=getIndex(y);
		setProbabilityValue(i,j,value);
	}	
	
	private void setProbabilityValue(int i, int j, short value) {
		probabilityMap[i+j*cellsPerMapEdge]=value;
	}
	
	/**
	 * Adds cone observation.
	 * @param cone Observation cone
	 * @param hit True if cone has hit object in which case the distance is the distance to the object.
	 */
	public void addConeObservation(ObservationCone cone) {
		MapIndex startIndex=getIndex(cone.getPosition().add(Vector2f.fromPolar(cone.getOrientationAngle(), cone.getLength()/2)));
		
		HashSet<MapIndex> addedIndexes=new HashSet<MapIndex>();
		Vector<MapIndex> indexesToProcess=new Vector<MapIndex>();
		
		indexesToProcess.add(startIndex);
		while(indexesToProcess.size()>0) {
			MapIndex currentIndex=indexesToProcess.remove(0);
			Vector2f currentVector=getVector(currentIndex);
			
			// Continue without processing further this index if it is not inside cone.
			if(!cone.isInside(currentVector)&&startIndex!=currentIndex) {
				continue;
			}
			
			// Add new surrounding indexes to be processed if they have not been added before.
			addIndex(new MapIndex(currentIndex.getI()+1,currentIndex.getJ()+0),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()+1,currentIndex.getJ()+1),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()+0,currentIndex.getJ()+1),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()-1,currentIndex.getJ()+0),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()-1,currentIndex.getJ()-1),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()+0,currentIndex.getJ()-1),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()-1,currentIndex.getJ()+1),addedIndexes,indexesToProcess);
			addIndex(new MapIndex(currentIndex.getI()+1,currentIndex.getJ()-1),addedIndexes,indexesToProcess);
			
			// Set probability value
			float observedProbability=cone.getProbability(currentVector);
			float mapProbability=getProbabilityValue(currentIndex)/255f;
			
			// The higher the probability the higher weight the observation has.
			float dynamicWeight=(float)(0.05*2*Math.abs(0.5f-observedProbability));
			float newMapProbability=((1-dynamicWeight)*mapProbability+dynamicWeight*observedProbability);
			
			setProbabilityValue(currentIndex,(short)(255*newMapProbability));
		}
		
	}
	
	private void addIndex(MapIndex index,HashSet<MapIndex> addedIndexes,Vector<MapIndex> indexesToProcess) {
		if(addedIndexes.contains(index)) {
			return;
		}
		indexesToProcess.add(index);
		addedIndexes.add(index);
	}
		
	public Image getImage() {
		BufferedImage image=new BufferedImage(cellsPerMapEdge,cellsPerMapEdge,BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<cellsPerMapEdge;i++) {
			for(int j=0;j<cellsPerMapEdge;j++) {
				image.setRGB(i, cellsPerMapEdge-j-1, getProbabilityValue(i, j));
			}			
		}
		return image;
	}
}
