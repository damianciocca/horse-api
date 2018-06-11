package com.etermax.spacehorse.core.player.resource.request;

import com.etermax.spacehorse.core.player.model.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeProfileRequest {

	@JsonProperty("name")
	private String name;
	@JsonProperty("gender")
	private Gender gender;
	@JsonProperty("age")
	private int age;

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public ChangeProfileRequest(@JsonProperty("name") String name, @JsonProperty("gender") Gender gender, @JsonProperty("age") int age) {
		this.name = name;
		this.gender = gender;
		this.age = age;
	}
}
