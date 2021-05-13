import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Ex1 {
    public static void main(String[] args) {
        boolean withTime = true;
        boolean isOpen = false;
        String start = "";
        String goal = "";
        try {
            File myObj = new File("C:\\Users\\saar9\\IdeaProjects\\Search_Problems\\src\\input2.txt");
            int i = 0, count = 0;
            Scanner myReader = new Scanner(myObj);
            String algo = myReader.nextLine();
            if (myReader.nextLine().equals("with time"))
                withTime = true;
            else
                withTime = false;
            if (myReader.nextLine().equals("with open"))
                isOpen = true;
            String size = myReader.nextLine();
            int row = Integer.parseInt("" + size.charAt(0));
            int col = Integer.parseInt("" + size.charAt(2));
            String[][] startBoard = new String[row][col];
            String[][] goalBoard = new String[row][col];
            String temp = myReader.nextLine();
            while (temp.charAt(0) != 'G') {
                String[] ar = temp.split(",");
                startBoard[i++] = ar;
                if (temp.contains("_")) {
                    for (int j = 0; j < ar.length; j++) {
                        if (ar[j].equals("_"))
                            count++;
                    }
                }
                temp = myReader.nextLine();
            }
            i = 0;
            while (myReader.hasNextLine()) {
                String temp_goal = myReader.nextLine();
                String[] ar = temp_goal.split(",");
                goalBoard[i++] = ar;
            }
            myReader.close();
            Node gameNode = new Node(startBoard, 0, null, "", count);
            gameNode.getMap().put(gameNode.getPuzzle().toString(), gameNode);
            Node goalNode = new Node(goalBoard, 0, null, "", count);

            switch (algo) {
                case "BFS":
                    long startTime = System.nanoTime();
                    BFS(gameNode, goalNode);
                    long estimatedTime = System.nanoTime() - startTime;
                    System.out.println(estimatedTime / Math.pow(10, 9));
                    break;
                case "DFID":
                    //DFID(gameNode,goalNode);
                    break;
                case "A*":
                     startTime = System.nanoTime();
                    A_star(gameNode, goalNode);
                     estimatedTime = System.nanoTime() - startTime;
                    System.out.println(estimatedTime / Math.pow(10, 9));
                    break;
                case "IDA*":
                    //IDA*(gameNode,goalNode);
                    break;
                case "DFBnB":
                    //DFBnB(gameNode,goalNode);
                    break;


            }


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void getPath(Node n) {
        Stack<Node> path = new Stack<>();
        Node temp = n;
        while (temp.getFather() != null) {
            path.push(temp);
            temp = temp.getFather();
        }
        while (!path.empty()) {
            Node n1 = path.pop();
            if (path.size() != 0)
                System.out.print(n1.getMove() + "-");
            else
                System.out.println(n1.getMove());

        }

    }

    public static void BFS(Node start, Node Goal) {
        LinkedList<Node> q = new LinkedList<>();
        int i = 0;
        int counter = 0;
        Hashtable<String, Node> open_list = start.getMap();
        Hashtable<String, Node> close_list = new Hashtable<>();
        q.add(start);
        while (!q.isEmpty()) {
            Node n = q.removeFirst();
            close_list.put(n.toString(),n);
            n.setVisited(true);
            List<Node> list = n.children();
            counter+=list.size();
            for (Node s : list) {
                if (!close_list.containsKey(s.toString())) {
                    if((!open_list.containsKey(s.toString()))) {
                        if (s.equals(Goal)) {
                            getPath(s);
                            System.out.println("Num: " + counter);
                            System.out.println("Cost: " + s.getCost());
                            return;
                        }
                        else {
                            open_list.put(s.toString(), s);
                            q.add(s);
                        }
                    }
                }
                //System.out.println(s);
            }
        }
    }

    public static void DFID(Node start, Node goal) {
        boolean result;
        int deapth = 50;
        for (int i = 0; i < deapth; i++) {
            Hashtable<String[][], Node> map = new Hashtable<>();

        }
    }

    public static void A_star(Node start, Node goal) {
        Queue<Node> q = new PriorityQueue<>();
        int i = 0;
        int counter = 0;
        Hashtable<String, Node> open_list = start.getMap();
        Hashtable<String, Node> close_list = new Hashtable<>();
        q.add(start);
        while (!q.isEmpty()) {
            Node n = q.remove();
            if (n.equals(goal)) {
                getPath(n);
                System.out.println("Num: " + counter);
                System.out.println("Cost: " + n.getCost());
                return;
            }
            n.setVisited(true);
            close_list.put(n.toString(),n);
            List<Node> list = n.children();
            for (Node s : list) {
                if (!close_list.containsKey(s.toString())) {
                    if((!open_list.containsKey(s.toString()))) {
                        open_list.put(s.toString(), s);
                        q.add(s);

                    }
                    else if (s.getCost() < open_list.get(s.toString()).getCost()) {
                    open_list.remove(s.toString());
                    open_list.put(s.toString(),s);
                    s.setFather(n);
               }
                    counter++;
                    }
                }
                //System.out.println(s);
            }
        }
    }

