package hello;

import java.util.Stack;

public class SmithWaterman {

	private final String one, two;
	private final int matrix[][];
	private int gap;
	private final int match;
	private final int o;
	private int l;
	private final int e;

	public SmithWaterman(String one, String two) {
		this.one = "-" + one.toLowerCase();
		this.two = "-" + two.toLowerCase();
		this.match = 2;

		// Define affine gap starting values
		o = -2;
		l = -1;
		e = -1;

		// initialize matrix to 0
		matrix = new int[one.length() + 1][two.length() + 1];
		for (int i = 0; i < one.length(); i++) {
			for (int j = 0; j < two.length(); j++) {
				matrix[i][j] = 0;
			}
		}

	}

	// returns the alignment score
	/**
	 * @return
	 */
	public double computeSmithWaterman() {
		for (int i = 0; i < one.length(); i++) {
			for (int j = 0; j < two.length(); j++) {
				gap = o + (l - 1) * e;
				if (i != 0 && j != 0) {
					if (one.charAt(i) == two.charAt(j)) {
						// match
						// reset l
						l = 0;
						matrix[i][j] = 0;
					} else  if(DNAType.A.name().toLowerCase().equals(String.valueOf(one.charAt(i))) || DNAType.A.name().toLowerCase().equals(String.valueOf(two.charAt(j)))){
						// gap
						l++;
						matrix[i][j] = 2;
					}
					else{
						matrix[i][j] = 1;
					}
				}
			}
		}

		// find the highest value
		double longest = 0;
		int iL = 0, jL = 0;
		for (int i = 0; i < one.length(); i++) {
			for (int j = 0; j < two.length(); j++) {
				if (matrix[i][j] > longest) {
					longest = matrix[i][j];
					iL = i;
					jL = j;
				}
			}
		}

		// Backtrack to reconstruct the path
		int i = iL;
		int j = jL;
		Stack<String> actions = new Stack<String>();

		while (i != 0 && j != 0) {
			// diag case
			if (Math.max(matrix[i - 1][j - 1], Math.max(matrix[i - 1][j], matrix[i][j - 1])) == matrix[i - 1][j - 1]) {
				actions.push("align");
				i = i - 1;
				j = j - 1;
				// left case
			} else if (Math.max(matrix[i - 1][j - 1],
					Math.max(matrix[i - 1][j], matrix[i][j - 1])) == matrix[i][j - 1]) {
				actions.push("insert");
				j = j - 1;
				// up case
			} else {
				actions.push("delete");
				i = i - 1;
			}
		}

		int maxMatchSet = actions.size();

		String alignOne = new String();
		String alignTwo = new String();

		Stack<String> backActions = (Stack<String>) actions.clone();
		for (int z = 0; z < one.length(); z++) {
			alignOne = alignOne + one.charAt(z);
			if (!actions.empty()) {
				String curAction = actions.pop();

				if (curAction.equals("insert")) {
					alignOne = alignOne + "-";
					
						while (!actions.isEmpty() &&actions.peek().equals("insert")) {
							alignOne = alignOne + "-";
							actions.pop();
							
					}
					
				}
			}
		}

		for (int z = 0; z < two.length(); z++) {
			alignTwo = alignTwo + two.charAt(z);
			if (!backActions.empty()) {
				String curAction = backActions.pop();
				if (curAction.equals("delete")) {
					alignTwo = alignTwo + "-";
					while (backActions.peek().equals("delete")) {
						alignTwo = alignTwo + "-";
						backActions.pop();
					}
				}
			}
		}
		int minMatchSet = backActions.size();

		// print alignment
		double realLengthStringOne = one.length() - 1;
		double realLenghtStringTwo = two.length() - 1;
		double totalOfMatricesElement = realLengthStringOne + realLenghtStringTwo;

		double value = (2 * maxMatchSet / totalOfMatricesElement) * 100;

		System.out.println("2 * " + maxMatchSet + " / " + "( " + realLengthStringOne + " + " + realLenghtStringTwo
				+ " ) " + "= " + value + "%");

		return value;
	}

	public void printMatrix() {

		for (int i = 0; i < one.length(); i++) {
			if (i == 0) {
				for (int z = 0; z < two.length(); z++) {
					if (z == 0) {
						System.out.print("  \t");
					}
					System.out.print(two.charAt(z) + " \t");

					if (z == two.length() - 1) {
						System.out.println();
					}
				}
			}

			for (int j = 0; j < two.length(); j++) {
				if (j == 0) {
					System.out.print(one.charAt(i) + " \t");
				}
				System.out.print(matrix[i][j] + " \t");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		// DNA sequence Test:

		SmithWaterman sw = new SmithWaterman("GGTTGACTA","TGTTACGG");
		System.out.println("Alignment Score: " + sw.computeSmithWaterman());

		sw.printMatrix();

	}
	
	enum DNAType {
		A,C,G,T
	}
}