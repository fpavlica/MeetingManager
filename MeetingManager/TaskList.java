import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class TaskList implements Serializable {
    private LinkedList<String> tasks = new LinkedList<String>();

    public TaskList(){

    }

    /**
     *
     * @param a takes in the tasks object defined above as the global variable
     * @param task takes in the string of the task
     * @return returns true if re orderd
     */
    public boolean addTaskAlphaOrder(LinkedList<String> a, String task) {
        ListIterator<String> li = a.listIterator();

        while (li.hasNext()) {
            int compare = li.next().compareTo(task); // compares to the next task when iterating through the list

            if (compare == 0) {
                //This means that they are equal do not add to the list
                System.out.println(task + " is already included");
                return false;
            } else if (compare > 0) {
                // for example the letter b in the alphabet index is grater than a thus switch them if the case
                li.previous();
                li.add(task);
                return true;
            } else if (compare < 0);
            // otherwise
        }
        li.add(task);
        return true;
    }

    public void delete(int index){
       tasks.remove(index);
    }

    public void printTasks(LinkedList<String> p){
        Iterator<String> iterate = p.iterator();

        while(iterate.hasNext()){
            System.out.println(iterate.next());
        }
    }

    /** writeFile() method - writes the file of the object
     *
     * @param tasks To print the task object
     * @throws IOException whenever an input or output failed
     */
    public void writeFile(TaskList tasks) throws IOException{

        FileOutputStream o = new FileOutputStream("Tasks.bin");
        ObjectOutputStream oo = new ObjectOutputStream(o); //Outputs object
        oo.writeObject(tasks);

    }

    /** readFile() method - reads the file
     *
     * @throws IOException Whenever an input or output failed
     * @throws ClassNotFoundException If a class is not found it throws an exception
     */
    public void readFile() throws IOException, ClassNotFoundException{

        FileInputStream oRead = new FileInputStream("Tasks.bin");
        ObjectInputStream ooRead = new ObjectInputStream(oRead);
        TaskList saveTree = (TaskList) ooRead.readObject(); //Reads the task object

        System.out.println(tasks); //Prints the saved task list
    }
}
