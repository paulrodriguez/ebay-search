package edu.ucla.cs.cs144;

public class SearchConstraint {	
	private String fieldName;
	private String value;
	
	public SearchConstraint() {}
	
	public SearchConstraint(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
