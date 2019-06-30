package br.com.ucs.firecombat.model;

import java.util.ArrayList;
import java.util.List;

import br.com.ucs.firecombat.listener.FireAddedListener;
import br.com.ucs.firecombat.listener.FireRemovedListener;

public class Listeneres {
	private List<FireAddedListener> addedFireListeners = new ArrayList<>();
	private List<FireRemovedListener> removedFireListeners = new ArrayList<>();
	
	
	protected void notifyFireAddedListeners(Fire fire) {
		this.addedFireListeners.forEach(listener -> listener.onFireAdded(fire));
	}
	
	protected void notifyFireRemovedListeners(int x,int y) {
		this.removedFireListeners.forEach(listener -> listener.onFireRemoved(x,y));
	}
	
    public void registerFireAddedListener (FireAddedListener listener) {
        this.addedFireListeners.add(listener);
    }
    
    public void registerFireRemovedListener (FireRemovedListener listener) {
        this.removedFireListeners.add(listener);
    }
}
