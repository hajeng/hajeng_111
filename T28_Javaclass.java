import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

class printMenu{

	void printmenu() {
		System.out.println("MENU:");
	}
}
public class T28_Javaclass {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fi1 = new FileInputStream("/Users/hajeng/Desktop/order.txt");
		int ch1;
		int number = 0;
		byte[] bt = new byte[1024];
		int i =0 ;
		
		String[] menu = new String[100];
		String[] tableNum = new String[100];
		String[] price = new String[100];
		String[] menuNum = new String[100];
		
		while((ch1 = fi1.read())!=-1) {
			bt[i] = (byte)ch1;
			i++;
		}
		String line = new String(bt);
		String[] a = new String[1024];
		a = line.split(",");
		
	    for(i=0;i<4;i++) {
	    	if(i==0) {
	    		System.out.println("TABLE NUMBER:");
	    		for(int j=0; j<5; j++) {
	    			tableNum[j] = a[number];
	    			System.out.print(tableNum[j]);
	    			number += 4;
	    		}
	    	}else if(i==1) {
	    		System.out.println("\n\nMENU:");
	    		number = 0;
	    		for(int j=0;j<5;j++) {
	    			menu[j] = a[number+1];
	    			System.out.println(menu[j]);
	    			number += 4;
	    		}
	    	}else if(i==2) {
	    		System.out.println("\nPRICE:");
	    		number = 0;
	    		for(int j=0;j<5;j++) {
	    			price[j] = a[number+2];
	    			System.out.println(price[j]);
	    			number += 4;
	    		}
	    	}else if(i==3) {
	    		System.out.println("\nMENU NUMBER:");
	    		number = 0;
	    		for(int j=0;j<5;j++) {
	    			menuNum[j] = a[number+3];
	    			System.out.println(menuNum[j]);
	    			number += 4;
	    		}
	    	}
	    }
		fi1.close();
	}

}
