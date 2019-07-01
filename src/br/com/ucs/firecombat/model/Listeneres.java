package br.com.ucs.firecombat.model;

import java.util.ArrayList;
import java.util.List;

import br.com.ucs.firecombat.listener.*;

public class Listeneres {
	private List<FireAddedListener> addedFireListeners = new ArrayList<>();
	private List<FireRemovedListener> removedFireListeners = new ArrayList<>();

	private List<FireFighterAddedListener> addedFireFighterListeners = new ArrayList<>();
	private List<FireFighterRemovedListener> removedFireFighterListeners = new ArrayList<>();

	private List<RefugeeAddedListener> addedRefugeeListener = new ArrayList<>();
	private List<RefugeeRemovedListener> removedRefugeeListener = new ArrayList<>();

	protected void notifyFireAddedListeners(Fire fire) {
		this.addedFireListeners.forEach(listener -> listener.onFireAdded(fire));
	}

	protected void notifyFireRemovedListeners(int x, int y) {
		this.removedFireListeners.forEach(listener -> listener.onFireRemoved(x, y));
	}

	public void registerFireAddedListener(FireAddedListener listener) {
		this.addedFireListeners.add(listener);
	}

	public void registerFireRemovedListener(FireRemovedListener listener) {
		this.removedFireListeners.add(listener);
	}

	protected void notifyFireFighterAddedListeners(Firefighter fire) {
		this.addedFireFighterListeners.forEach(listener -> listener.onFireFighterAdded(fire));
	}

	protected void notifyFireFighterRemovedListeners(int x, int y) {
		this.removedFireFighterListeners.forEach(listener -> listener.onFireFighterRemoved(x, y));
	}

	public void registerFireFighterAddedListener(FireFighterAddedListener listener) {
		this.addedFireFighterListeners.add(listener);
	}

	public void registerFireFighterRemovedListener(FireFighterRemovedListener listener) {
		this.removedFireFighterListeners.add(listener);
	}


	protected void notifyRefugeeAddedListeners(Refugee refugee) {
		this.addedRefugeeListener.forEach(listener -> listener.onRefugeeAdd(refugee));
	}

	protected void notifyRefugeeRemovedListeners(int x, int y) {
		this.removedRefugeeListener.forEach(listener -> listener.onRefugeeRemoved(x, y));
	}

	public void registerRefugeeAddedListener(RefugeeAddedListener listener) {
		this.addedRefugeeListener.add(listener);
	}

	public void registerRefugeeRemovedListener(RefugeeRemovedListener listener) {
		this.removedRefugeeListener.add(listener);
	}


}
