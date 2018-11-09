package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    private long id;
    private String firstName;
    private String lastName;
    private String headline;

    public Person() {
    }


    public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getHeadline() {
		return headline;
	}


	public void setHeadline(String headline) {
		this.headline = headline;
	}


	@Override
    public String toString() {
        return "Person{" +
                "First Name ='" + firstName + '\'' +
                "Last Name ='" + lastName + '\'' +
                ", Headline =" + headline +
                '}';
    }
}
