package jx.net.dispatch;

import jx.buffer.separator.*;
import jx.zero.Memory;
import jx.zero.Debug;

public class Dispatch {
    UpperLayer[] upperLayers;
    int count = 0;
    public static final boolean verbose = false;
    public Dispatch(int numberOfClients) {
	upperLayers = new UpperLayer[numberOfClients];
    }

    public void add(int id, String name) {
	if (verbose) {
	    Debug.out.println("Count: " + count);
	    Debug.out.println("Upper: " + upperLayers);
	    Debug.out.println("Upper.length: " + upperLayers.length);
	}
	upperLayers[count++] = new UpperLayer(id, name);
    }

    public boolean registerConsumer(MemoryConsumer consumer, String name) {
        for (UpperLayer upperLayer : upperLayers) {
            if (upperLayer.name.equals(name)) {
                if (upperLayer.consumer != null) {
                    return false; // only one consumer can register
                }
                upperLayer.consumer = consumer;
                return true;
            }
        }
	return false;
    }

    public int findID(String name) {
        for (UpperLayer upperLayer : upperLayers) {
            if (upperLayer.name.equals(name)) {
                return upperLayer.id;
            }
        }
	Debug.out.println("Dispatch: Name " + name + "not found");
	return -1;
    }

    public String findName(int id) {
        for (UpperLayer upperLayer : upperLayers) {
            if (upperLayer.id == id) {
                return upperLayer.name;
            }
        }
	Debug.out.println("Dispatch: ID " + id + "not found");
	return "???";
    }
    
    public Memory dispatch(int id, Memory buf) {
        for (UpperLayer upperLayer : upperLayers) {
            if (upperLayer.id == id) {
                if (verbose) {
                    Debug.out.println("Dispatch: " + upperLayer.name + " packet received!");
                }
                if (upperLayer.consumer != null) {
                    return upperLayer.consumer.processMemory(buf);
                }
                return buf;
            }
        }
	return buf; // nobody is interested in this packet
    }
}
