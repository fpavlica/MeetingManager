import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;

/**
 * A tree multiset-like data structure for Diaries. Diaries with the same key are stored in a linked list. 
 * Probably would've been easier to have a nextDiary reference in the Diary class instead of 
 * using the LinkedList library class but it's too late now.
 * @author AC12001 17/18 group 2
 *
 */
public class DiaryTree implements Serializable, Iterable<DiaryList> {
	private static final long serialVersionUID = 3708656793582367768L; //compiler-generated, for object saving
	private TreeSet<DiaryList> tree;
	private Stack<String> taskStack; //this really, really shouldn't be here but it makes saving easier and I have no time
	
	/**
	 * Constructor which initialises the tree of diary lists
	 */
	public DiaryTree() {
		tree = new TreeSet<DiaryList>();
		taskStack = new Stack<String>(); //noooooo :'(
	}
	
	/**
	 * Add a diary to the tree
	 * @param diaryToAdd	the diary to add
	 * @return	true if added successfully
	 */
	public boolean add(Diary diaryToAdd) {
		DiaryList dl = new DiaryList(diaryToAdd);
		return add(dl);		
	}
	
	/**
	 * Add a diary list to the tree
	 * @param listToAdd	the diarylist to add
	 * @return	true if added successfully
	 */
	public boolean add(DiaryList listToAdd) {
		DiaryList temp = searchByKey(listToAdd);
		if (temp == null) {
			//diary with this name doesn't exist in the set yet
			return tree.add(listToAdd);
		} else {
			//diary with this name already exists in the tree, can't add normally
			return temp.add(listToAdd);
		}
	}
	
	/**
	 * Remove a diary from the tree
	 * @param toRemove the diary to remove
	 * @return	true if removed successfully (the diary did exist in the tree);
	 */
	public boolean remove(Diary toRemove) {
		//find the list that contains this diary
		Diary temp = new Diary(toRemove.getFirstname(), toRemove.getLastname());
		DiaryList tempList = new DiaryList(temp);
		DiaryList remList = this.searchByKey(tempList);
		if (remList.hasOnlyOneElement()) {
			return this.tree.remove(remList);
		} else {
			return remList.remove(toRemove);
		}
	}
	
	/**
	 * Print out the tree
	 */
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
	public DiaryList searchByKey(DiaryList otherList) {
		//inspired by https://stackoverflow.com/a/25923832
		DiaryList floor = tree.floor(otherList);
		if (floor != null) {
			//otherList has left children, floor is the rightmost element in otherList's left subtree
			if (floor.compareTo(otherList) == 0) {
				//same name, found
				return floor;
			}
		} 
		//not found
		return null;
	}
	
	/**
	 * Convert the tree to an array by first converting it to an ArrayList
	 * @return the sorted array of diaries from the tree
	 */
	public Diary[] toArray() {
		//bad
		ArrayList<Diary> al = new ArrayList<Diary>();
		for (DiaryList dl : tree) {
			for (Diary d: dl.getDiaries()) {
				al.add(d);
			}
		}
		Diary[] tempDArr = new Diary[al.size()];
		return al.toArray(tempDArr);
	}

	@Override
	public Iterator<DiaryList> iterator() {
		// TODO Auto-generated method stub
		return tree.iterator();
	}

	/**
	 * @return the taskStack
	 */
	public Stack<String> getTaskStack() {
		return taskStack;
	}

	/**
	 * @param taskStack the taskStack to set
	 */
	public void setTaskStack(Stack<String> taskStack) {
		this.taskStack = taskStack;
	}

}
