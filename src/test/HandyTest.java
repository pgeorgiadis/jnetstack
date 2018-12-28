package test;

public class HandyTest {
	public static void main(String[] args) {
		expressionTest();
	}
	
	public static void expressionTest() {
		int kati = 0;
		System.out.println(kati+=5);
	}
	
	public static void changeTest() {
		String s = new String("timh 1");
		HandyTest.change(s);
		System.out.println(s);
	}
	
	public static void change(String s) {
		s = new String("allagmeno");
	}
	
	public void arrayEqualityTest() {
		byte[] a = new byte[] {1,2,3,4};
		byte[] b = new byte[] {4,3,2,1};
		byte[] c = new byte[] {1,2,3,4};
		
		System.out.println(a.equals(b));
		System.out.println(a.equals(c));
	}
}