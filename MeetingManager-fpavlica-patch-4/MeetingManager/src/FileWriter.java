import java.io.*;

public class FileWriter {
    /** writeFile() method - writes the file of the object
     *
     * @param users To print the tree object
     * @throws IOException whenever an input or output failed
     */
    public void writeFile(DiaryTree users) throws IOException{
        //TreeNode tn = new TreeNode();

        FileOutputStream o = new FileOutputStream("Data.bin");
        ObjectOutputStream oo = new ObjectOutputStream(o); //Outputs object
        oo.writeObject(users);

    }

    /** readFile() method - reads the file
     *
     * @throws IOException Whenever an input or output failed
     * @throws ClassNotFoundException If a class is not found it throws an exception
     */
    public void readFile() throws IOException, ClassNotFoundException{

        FileInputStream oRead = new FileInputStream("Data.bin");
        ObjectInputStream ooRead = new ObjectInputStream(oRead);
        DiaryTree saveTree = (DiaryTree) ooRead.readObject(); //Reads the tree object

        System.out.println(saveTree.print()); //Prints the saved tree
    }
}
