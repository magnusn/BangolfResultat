import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JCheckBox;

public class CompareDataStructures {

	public CompareDataStructures() {
		compareHashMap("classorder");
		compareTextFile("compare");
		compareIntMatrix("compareby");
		compareStringArray("comparefiles");
		compareHashtable("datastore");
		compareObject("dirhtm");
		compareObject("dirjmf");
		compareObject("dirskv");
		compareObject("dirsnitt");
		compareTextFile("klass");
		compareHashMap("klassmap");
		compareStringArray("klasstring");
		compareHashMap("licensemap");
		compareHashMap("licensenamemap");
		compareTextFile("namn");
		compareTextFile("orientation");
		compareHashMap("pnametrack");
		compareHashMap("ptrack");
		compareTextFile("snitt");
		compareJCheckBoxMatrix("snittapp");
		compareStringArray("snittstring");
	}

	private void compareObject(String file) {
		boolean equals = true;
		try {
			Object oldObject = (Object) loadOld(file);
			Object newObject = (Object) loadNew(file);

			if (oldObject == null && newObject == null) {
				equals = true;
			} else if (!oldObject.equals(newObject)) {
				equals = false;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareHashMap(String file) {
		boolean equals = true;
		try {
			HashMap oldMap = (HashMap) loadOld(file);
			HashMap newMap = (HashMap) loadNew(file);

			for (Object key : oldMap.keySet()) {
				if (!newMap.containsKey(key)) {
					equals = false;
					System.out.println("New map does not contain key: " + key);
				}
				if (!newMap.get(key).equals(oldMap.get(key))) {
					equals = false;
					System.out.println("Values are not equal!");
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareHashtable(String file) {
		boolean equals = true;
		try {
			Hashtable oldTable = (Hashtable) loadOld(file);
			Hashtable newTable = (Hashtable) loadNew(file);

			for (Object key : oldTable.keySet()) {
				if (!newTable.containsKey(key)) {
					equals = false;
					System.out
							.println("New table does not contain key: " + key);
				}
				if (!newTable.get(key).equals(oldTable.get(key))) {
					equals = false;
					System.out.println("Values are not equal!");
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareTextFile(String file) {
		boolean equals = true;
		try {
			BufferedReader oldReader = new BufferedReader(new FileReader(
					"installer/settings/" + file));
			BufferedReader newReader = new BufferedReader(new FileReader(
					"AppData/Settings/" + file));
			String oldString;
			while ((oldString = oldReader.readLine()) != null) {
				String newString = newReader.readLine();
				if (!oldString.equals(newString)) {
					equals = false;
					System.out.println("Text file differ: " + oldString + "!="
							+ newString);
				}
			}
			String newString;
			if ((newString = newReader.readLine()) != null) {
				equals = false;
				System.out.println("New text file contains extra string: "
						+ newString);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareIntMatrix(String file) {
		boolean equals = true;
		try {
			int[][] oldMatrix = (int[][]) loadOld(file);
			int[][] newMatrix = (int[][]) loadNew(file);

			if (oldMatrix.length != newMatrix.length) {
				System.out.println("Size not equal 1 for: " + file);
				equals = false;
			}

			for (int i = 0; i < oldMatrix.length; ++i) {
				if (oldMatrix[i].length != newMatrix[i].length) {
					System.out.println("Size not equal 2 for: " + file);
					equals = false;
				}
				for (int j = 0; j < oldMatrix[i].length; ++j) {
					if (oldMatrix[i][j] != newMatrix[i][j]) {
						System.out.println("Values differ for matrix");
						equals = false;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareJCheckBoxMatrix(String file) {
		boolean equals = true;
		try {
			JCheckBox[][] oldMatrix = (JCheckBox[][]) loadOld(file);
			JCheckBox[][] newMatrix = (JCheckBox[][]) loadNew(file);

			if (oldMatrix.length != newMatrix.length) {
				System.out.println("Size not equal 1 for: " + file);
				equals = false;
			}

			for (int i = 0; i < oldMatrix.length; ++i) {
				if (oldMatrix[i].length != newMatrix[i].length) {
					System.out.println("Size not equal 2 for: " + file);
					equals = false;
				}
				for (int j = 0; j < oldMatrix[i].length; ++j) {
					if (!oldMatrix[i][j].getText().equals(
							newMatrix[i][j].getText())
							|| !(oldMatrix[i][j].isSelected() == newMatrix[i][j]
									.isSelected())
							|| !(oldMatrix[i][j].isEnabled() == newMatrix[i][j]
									.isEnabled())) {
						System.out.println("Values differ for matrix");
						equals = false;
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	private void compareStringArray(String file) {
		boolean equals = true;
		try {
			String[] oldArray = (String[]) loadOld(file);
			String[] newArray = (String[]) loadNew(file);

			if (oldArray.length != newArray.length) {
				System.out.println("Size not equal for: " + file);
				equals = false;
			}

			for (int i = 0; i < oldArray.length; ++i) {
				if (!oldArray[i].equals(newArray[i])) {
					System.out.println("Values differ for array");
					equals = false;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (equals) {
			System.out.println(file + " equal!");
		}
	}

	public static void main(String[] args) {
		new CompareDataStructures();
	}

	public Object loadOld(String file) throws IOException,
			ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois;
		Object o = null;
		fis = new FileInputStream("installer/settings/" + file);
		ois = new ObjectInputStream(fis);
		o = ois.readObject();
		ois.close();
		return o;
	}

	public Object loadNew(String file) throws IOException,
			ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream ois;
		Object o = null;
		fis = new FileInputStream("AppData/Settings/" + file);
		ois = new ObjectInputStream(fis);
		o = ois.readObject();
		ois.close();
		return o;
	}

}
