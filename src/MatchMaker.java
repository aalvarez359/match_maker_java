/*Armando Alvarez
 * CSC501
 * Spring 2025
 */

import java.util.*;

public class MatchMaker { 
	private int n; //number of couples
	private LinkedList<Integer> freeMen = new LinkedList<Integer>(); //list of available men
	private LinkedList<Integer> freeWomen = new LinkedList<Integer>(); //list of available women
	private int[] nxt;     //array to keep track of next proposer
	private int[][] manPref, womanPref;  //2d array: rows = men or women in order. columns = index number of partners in ranking order (0=highest & n-1=lowest)
	private int[] currentMan;   //current man engaged with woman indexed
	private int[] currentWoman;  //current woman engaged with man indexed
	private int[][] wranking;  //2d array: rows = women in order. columns = ranking number of men (0=highest & n-1=lowest) in index number order of men
	private int[][] mranking;  //2d array: rows = men in order. columns = ranking number of women (0=highest & n-1=lowest) in index number order of women
	
	public MatchMaker(int n) {  //MatchMaker constructor

		this.n = n;
		this.manPref = new int[n][n];
		this.womanPref = new int[n][n];
		this.freeMen = new LinkedList<>();
		this.freeWomen = new LinkedList<>();
		this.nxt = new int[n];
		this.currentMan = new int[n];
		this.currentWoman = new int[n];
		this.wranking = new int[n][n];
		this.mranking = new int[n][n];
		Random random = new Random();
		
		for(int i=0;i < n;i++) {  
			freeMen.add(i);
		}
		
		for(int i=0;i < n;i++) {  
			freeWomen.add(i);
		}
		
		/*for(int i=0;i < n;i++) {
			current[i] = -1;
		}*/
		
		System.out.println("Men's Preference list:\n");
		
		for(int i=0;i < n;i++) {				//creates and prints random ranking of men's preferences
			List<Integer> numsList = new ArrayList<>();
			for(int j=0;j < n;j++) {
				numsList.add(j);
			}
			Collections.shuffle(numsList, random);
			for(int j=0;j < n;j++) {
				manPref[i][j] = numsList.get(j);
			}
		}
		for(int i=0;i < n;i++) {
			for(int j=0;j < n;j++) {
				System.out.print(manPref[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("\nWomen's Preference list:\n");
		
		for(int i=0;i < n;i++) {				//creates and prints random ranking of women's preferences
			List<Integer> numsList = new ArrayList<>();
			for(int j=0;j < n;j++) {
				numsList.add(j);
			}
			Collections.shuffle(numsList, random);
			for(int j=0;j < n;j++) {
				womanPref[i][j] = numsList.get(j);
			}
		}
		for(int i=0;i < n;i++) {
			for(int j=0;j < n;j++) {
				System.out.print(womanPref[i][j]);
			}
			System.out.println();
		}
		
		for(int i=0;i < n;i++) {			//each woman's ranking of men in men's index order
			for(int j=0;j < n;j++) {
				wranking[i][womanPref[i][j]] = j;
			}
		}
		
		for(int i=0;i < n;i++) {			//each man's ranking of women in women's index order
			for(int j=0;j < n;j++) {
				mranking[i][manPref[i][j]] = j;
			}
		}
	}
	
	public int[] manFianceFinder() {		//finds stable matching of couples with men proposing
		for(int i=0;i < n;i++) {			//initializes next partner with 0
			nxt[i] = 0;
		}
		for(int i=0;i < n;i++) {	// initializes each womans pairing with -1
			currentMan[i] = -1;
		}
		freeMen.clear();  
		for(int i=0;i < n;i++) {   //add free men to list
			freeMen.add(i);
		}
		while(!freeMen.isEmpty()) {		//run until there are no more free men
			int m = freeMen.removeFirst();		//take next free man
			int w = manPref[m][nxt[m]++];		//woman is man's next preference
			
			if(currentMan[w] == -1) {		//if woman is unpaired, pair her with free man
				currentMan[w] = m;
			}
			else if(wranking[w][currentMan[w]] > wranking[w][m]) {  //if the ranking of free man is lower (better) than woman's existing partner,
				int exFiance = currentMan[w];						//pair her with free man and add existing partner to the free man list
				currentMan[w] = m;
				freeMen.add(exFiance);
			}
			else {					//else if woman's current partner is better than free man, re-add free man to list
				freeMen.add(m);
			}
		}
		return currentMan;			//return the final pairings
	}
	
	public int[] womanFianceFinder() {		//finds stable matching of couples with women proposing
		for(int i=0;i < n;i++) {			//initializes next partner with 0
			nxt[i] = 0;
		}
		for(int i=0;i < n;i++) {	//initializes each mans pairing with -1
			currentWoman[i] = -1;
		}
		freeWomen.clear();
		for(int i=0;i < n;i++) {  //add free women to list
			freeWomen.add(i);
		}
		while(!freeWomen.isEmpty()) {			//run until there are no more free women
			int w = freeWomen.removeFirst();	//take next free woman
			int m = womanPref[w][nxt[w]++];		//man is woman's next preference
			
			if(currentWoman[m] == -1) {			//if man is unpaired, pair her with free woman
				currentWoman[m] = w;
			}
			else if(mranking[m][currentWoman[m]] > mranking[m][w]) {       //if the ranking of free woman is lower (better) than man's existing partner,
				int exFiance = currentWoman[m];							   //pair him with free woman and add existing partner to the free woman list
				currentWoman[m] = w;
				freeWomen.add(exFiance);
			}
			else {					//else if man's current partner is better than free woman, re-add free woman to list
				freeWomen.add(w);
			}
		}
		return currentWoman;		//return the final pairings
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc =  new Scanner(System.in);	//prompts user for number of couples
		System.out.println("Enter number of couples: ");
		int n = sc.nextInt();
		
		MatchMaker mm = new MatchMaker(n);		//creates MatchMaker with n couples and runs manFianceFinder
		int [] mmarriage = mm.manFianceFinder();
		System.out.println("\nMan Proposing:\nMan Woman\n");  //prints the final pairings with the man proposing
		for(int i = 0;i < n;i++) {
			System.out.println(mmarriage[i] + "   " + i + "\n");
		}
		
		int [] wmarriage = mm.womanFianceFinder();
		System.out.println("\nWoman Proposing:\nMan Woman\n");  //prints the final pairings with the woman proposing
		for(int i = 0;i < n;i++) {
			System.out.println(i + "   " + wmarriage[i] + "\n");
		}
		sc.close();

	}

}
