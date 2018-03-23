import java.io.Serializable;
import java.util.LinkedList;

public class DiaryList implements Comparable<DiaryList>, Serializable{

	private LinkedList<Diary> diaries;
	
	public DiaryList() {
		diaries = new LinkedList<Diary>();
	}
	
	public DiaryList(Diary diary) {
		diaries = new LinkedList<Diary>();
		diaries.add(diary);
	}
	
	public boolean add(Diary d) {
		return diaries.add(d);
	}
	
	public boolean add(DiaryList dl) {
		return diaries.addAll(dl.getDiaries());
	}
	
	public boolean add(LinkedList<Diary> toAdd) {
		return diaries.addAll(toAdd);
	}
	
	public void print() {
		for (Diary diary : diaries) {
			System.out.println(diary);
		}
	}
	
	@Override
	public int compareTo(DiaryList otherList) {
		/*
		String thisName = diaries.getFirst().getSortableName();
		LinkedList<Diary> comparingTo = ((DiaryList) arg0).getDiaries();
		String comparingName = comparingTo.getFirst().getSortableName();
		return thisName.compareToIgnoreCase(comparingName);
		*/
		Diary comparingTo = otherList.getDiaries().getFirst();
		return this.getDiaries().getFirst().compareTo(comparingTo);
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
