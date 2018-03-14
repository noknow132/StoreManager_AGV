package test;

public class StuInfo {
	private int Id;
	private String Name;

	public StuInfo() {
	}

	public StuInfo(int id, String name) {
		Id = id;
		Name = name;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
