
public enum Test1 {
	lijin(1,"何琴"),lijin2(2, "zjh"), bieren(3, "ym");
	private int age;
	private String name;
	private Test1(int age, String name){
		this.age = age;
		this.name = name;
	}
	public int getAge(){
		return age;
		
	}
	
	public String getName(){
		return name;
	}
}
