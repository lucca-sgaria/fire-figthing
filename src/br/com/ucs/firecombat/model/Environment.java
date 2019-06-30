package br.com.ucs.firecombat.model;

import java.util.List;

public class Environment {
	
	private List<Fire> fires;
	private Integer[][] matrix;
	
	public Environment() {
		this.matrix = new Integer[5][5];
		init();
	}
	
	private void init() {
	}
	
	public List<Fire> getFires() {
		return fires;
	}
	public void setFires(List<Fire> fires) {
		this.fires = fires;
	}
	public Integer[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(Integer[][] matrix) {
		this.matrix = matrix;
	}
	
	
	
}
