package com.etermax.spacehorse.core.catalog.model.csv.sheet;

import java.util.ArrayList;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class Sheet {
	private ArrayList<String> headers = new ArrayList<>();
	private ArrayList<ArrayList<String>> rows = new ArrayList<>();
	private String name = "";

	public ArrayList<String> getHeaders() {
		return headers;
	}

	public ArrayList<ArrayList<String>> getRows() {
		return rows;
	}

	public String getName() {
		return name;
	}

	public Sheet() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addHeader(String header) {
		headers.add(header);
	}

	public int getHeaderIndex(String headerName) {
		int index = headers.indexOf(headerName);
		if (index < 0) {
			throw new ApiException("Header " + headerName + " not found in " + name);
		}
		return index;
	}

	public ArrayList<String> addRow() {
		ArrayList<String> row = new ArrayList<>();

		rows.add(row);

		return row;
	}
}
