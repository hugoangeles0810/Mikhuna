package com.jasoftsolutions.mikhuna.remote.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListJsonResponseData<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("count")
	private Integer count;
	
	@SerializedName("results")
	private List<T> results;

    private Long lastUpdate;

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getResults() {
        return results;
    }
    public void setResults(List<T> results) {
        this.results = results;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
