package com.entity;

public class Temp {
    private String id;

    private String name;

    private Integer age;

    private String path;

    public Temp() {
		super();
	}

	public Temp(String id, String name, Integer age, String path) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.path = path;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }
}