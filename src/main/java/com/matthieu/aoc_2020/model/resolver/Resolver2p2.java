package com.matthieu.aoc_2020.model.resolver;

import java.util.List;

import com.matthieu.aoc_2020.exception.PrepareDataException;
import com.matthieu.aoc_2020.exception.SolveException;
import com.matthieu.aoc_2020.model.Matrix;

public class Resolver2p2 implements Resolver {

	private Matrix data;
	private int validPassword;
	
	@Override
	public void prepareData(List<String> values) throws PrepareDataException {
		this.data = new Matrix(values);
		this.validPassword = 0;
	}

	@Override
	public boolean solve() throws SolveException {
		for (String [] row : data.getDatas()) {
			String[] splitedLimit = row[0].split("-");
			int a = Integer.parseInt(splitedLimit[0]);
			int b = Integer.parseInt(splitedLimit[1]);
			
			char letter = row[1].charAt(0);
			
			boolean aTrue = (row[2].length() - 1) > (a + 1);
			boolean bTrue = (row[2].length() - 1) > (b + 1);
			
			if(aTrue) {
				aTrue = row[2].charAt(a + 1) == letter;
			}
			if(bTrue) {
				bTrue = row[2].charAt(b + 1) == letter;
			}
			
			if(aTrue ^ bTrue) {
				validPassword++;
			}
		}
		return true;
	}

	@Override
	public String get() {
		return String.valueOf(this.validPassword);
	}
}
