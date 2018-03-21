import java.util.TreeSet;

public class DiaryTree {
	private TreeSet<DiaryList> tree;
	
	public DiaryTree() {
		tree = new TreeSet<DiaryList>();
	}
	
	public boolean add(Diary diaryToAdd) {
		DiaryList dl = new DiaryList(diaryToAdd);
		return add(dl);		
	}
	public boolean add(DiaryList listToAdd) {
		// if !tree.add -> already contains, find it and change it
		DiaryList temp = search(listToAdd);
		if (temp == null) {
			//diary with this name doesn't exist in set yet
			return tree.add(listToAdd);
		} else {
			//diary with this name already exists in the tree, can't add normally
			return temp.add(listToAdd);
		}
	}
	
	public void print() {
		for (DiaryList dl : tree) {
			dl.print();
		}
	}
	
	/**
	 * Finds a DiaryList in the tree using compareTo() and returns it if it exists, otherwise returns null
	 * @param otherList the list with a name that is to be found in the tree 
	 * @return the found DiaryList or null if it doesn't exist
	 */
	public DiaryList search(DiaryList otherList) {
		//inspired by https://stackoverflow.com/a/25923832
		DiaryList floor = tree.floor(otherList);
		if (floor == null) {
			//name not found
			return null; //TODO hacky, just skips a part of the code
		}
		int temp = floor.compareTo(otherList);
		if (temp == 0) {
			//same name, found
			return floor;
		} else {
			//name not found
			return null;
		}
	}
}
