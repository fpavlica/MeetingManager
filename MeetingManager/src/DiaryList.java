import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A special list which should only contain Diaries with the same name.
 * A component of a multiset-like structure, see DiaryTree.java.
 * @author AC12001 17/18 group 2
 *
 */
public class DiaryList implements Comparable<DiaryList>, Serializable, Iterable<Diary>{

	private static final long serialVersionUID = -669192092115463780L; //compiler-generated, for object saving
	private LinkedList<Diary> diaries;
	
	/**
	 * Default constructor, initialises a linked list of diaries
	 */
	public DiaryList() {
		diaries = new LinkedList<Diary>();
	}
	
	/**
	 * Constructor that initialises a linked list of diaries and adds the specified diary to it.
	 * @param diary the diary to add
	 */
	public DiaryList(Diary diary) {
		diaries = new LinkedList<Diary>();
		diaries.add(diary);
	}
	
	/**
	 * Add a diary to the list
	 * @param d	the diary to add
	 * @return	true if added successfully
	 */
	public boolean add(Diary d) {
		return diaries.add(d);
	}

	/**
	 * Add a diary list to the existing list
	 * @param dl	the diary to add
	 * @return	true if added successfully
	 */
	public boolean add(DiaryList dl) {
		return diaries.addAll(dl.getDiaries());
	}

	/**
	 * Add a linked list of diaries to the existing diary list
	 * @param toAdd	the diary to add
	 * @return	true if added successfully
	 */
	public boolean add(LinkedList<Diary> toAdd) {
		return diaries.addAll(toAdd);
	}
	
	/**
	 * remove a diary from the list
	 * @param toRemove the diary to remove
	 * @return	true if removed successfully (the diary did exist in the tree
	 */
	public boolean remove(Diary toRemove) {
		return diaries.remove(toRemove);
	}
	
	/**
	 * checks if the first element is equal to the last one.
	 * @return true if the list only has one element
	 */
	public boolean hasOnlyOneElement() {
		return this.diaries.getFirst() ==this.diaries.getLast();
	}
	
	/**
	 * Print out the diary list to the console
	 */
	public void print() {
		for (Diary diary : diaries) {
			System.out.println(diary.getAllInfo());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Only compares to the first element of the other diary list as all names in the list must be equal.
	 */
	@Override
	public int compareTo(DiaryList otherList) {
		Diary comparingTo = otherList.getDiaries().getFirst();
		return this.getDiaries().getFirst().compareTo(comparingTo);
	}
	@Override
	public Iterator<Diary> iterator() {
		// TODO Auto-generated method stub
		return diaries.iterator();
	}
	/**
	 * @return the diaries
	 */
	public LinkedList<Diary> getDiaries() {
		return diaries;
	}
	/**
	 * @param diaries the diaries to set
	 */
	public void setDiaries(LinkedList<Diary> diaries) {
		this.diaries = diaries;
	}


}
