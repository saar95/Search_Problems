import java.util.*;

public class Node implements Comparable{

    private static Hashtable<String,int []>m = new Hashtable<>();

    private static Hashtable<String,Node> map = new Hashtable<>();
    private int cost, row, col;
    private String[][] puzzle;
    private String move_made;
    private Node father;
    private boolean visited;
    private int[] index1;
    private int[] index2;
    private int empty_block;
    private int huristic;


    Node(String[][] puzzle, int cost, Node father, String move_made, int empty_block) {
        this.puzzle = puzzle;
        this.cost = cost;
        this.father = father;
        this.index1 = new int[2];
        this.index2 = new int[2];
        this.visited = false;
        this.move_made = move_made;
        this.empty_block = empty_block;
        this.row = puzzle.length;
        this.col = puzzle[0].length;
        this.huristic=0;
        find();
    }

    Node() {
        this.puzzle = null;
        this.cost = 0;
        this.father = null;
        this.index1 = new int[2];
        this.index2 = new int[2];
        this.visited = false;
        this.move_made = "";
        this.row = this.col = 0;
        this.empty_block = 0;
        this.huristic=0;
        find();
    }

    Node(Node node) {
        this.visited = node.visited;
        this.cost = node.cost;
        this.move_made = node.move_made;
        this.row = node.row;
        this.col = node.col;
        this.empty_block= node.empty_block;
        this.father = node.father;
        //this.index1=Arrays.copyOf(node.index1,2);
        //this.index2=Arrays.copyOf(node.index2,2);
        this.puzzle=new String[node.puzzle.length][node.puzzle[0].length];
        for (int i = 0; i < node.puzzle.length; i++) {
            for (int j = 0; j <node.puzzle[0].length ; j++) {
                this.puzzle[i][j]=node.puzzle[i][j];
            }
        }

        this.huristic=node.huristic;
        find();
    }

    public String getMove() {
        return this.move_made;
    }
    public int getHuristic() {
        return this.huristic;
    }

    @Override
    public String toString() {
        String ans = "[";
        for(int i=0; i<this.getPuzzle().length; i++){
            ans += "{";
            for(int j=0; j<this.getPuzzle()[i].length; j++){
                ans += this.getPuzzle()[i][j];
                if(!(j==this.getPuzzle()[i].length-1))
                    ans+=",";
            }
            ans+="} ";
        }
        ans += "]\n";
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //if (!(o instanceof Node)){ return false;}
        // if (this.getClass()!=o.getClass()){return false;}
        if (o == null) {
            return false;
        }

        for (int i = 0; i < this.getPuzzle().length; i++) {
            for (int j = 0; j < this.getPuzzle()[i].length; j++) {
                //if(this.puzzle[i][j] != ((Node) o).getPuzzle()[i][j]){
                if (!this.puzzle[i][j].equals(((Node) o).getPuzzle()[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(this.puzzle);
        return result;
    }


    public void find() {
        int[] arr = new int[2];
        int[] arr1 = new int[2];
        int count = 0;
        for (int i = 0; i < this.puzzle.length; i++) {
            for (int j = 0; j < this.puzzle[0].length; j++) {
                if (this.empty_block == 1) {
                    if (this.puzzle[i][j].equals("_")) {
                        arr[0] = i;
                        arr[1] = j;
                        this.index1 = arr;
                    }
                }
                if (this.empty_block == 2) {
                    if (this.puzzle[i][j].equals("_") && count == 0) {
                        arr[0] = i;
                        arr[1] = j;
                        this.index1 = arr;
                        count++;
                    }
                    else if (this.puzzle[i][j].equals("_") && count == 1) {
                        arr1[0] = i;
                        arr1[1] = j;
                        this.index2 = arr1;
                        count++;
                    }
                }
            }
        }
    }

    public void update(Node goal){
        for (int i = 0; i < goal.getPuzzle().length; i++) {
            for (int j = 0; j < goal.getPuzzle()[0].length; j++) {
                int [] temparr = new int[2];
                temparr[0]=i;
                temparr[1]=j;
                m.put(goal.getPuzzle()[i][j],temparr);
            }
        }
    }

    public Hashtable<String,Node> getMap(){
        return this.map;
    }


    public List<Node> one_block(Node father, int[] index) {
        int row = this.row, col = this.col;
        int[] first = new int[2];
        String[][] board = new String[row][col];
        List<Node> list = new LinkedList<>();
        if (Arrays.equals(father.index1,index))
            first = this.index1;
        if (Arrays.equals(father.index2,index))
            first = this.index2;
        int i = first[0];
        int j = first[1];
        if ((j + 1) < col) {
            board = puzzle_copy(father.getPuzzle());
            String under = "_";
            board[i][j] = board[i][j + 1];
            board[i][j + 1] = under;
            String number_moved = board[i][j] + "L";

            Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);
                    list.add(a);
        }
        if ((i + 1) < row) {
            board = puzzle_copy(father.getPuzzle());
            String under = "_";
            board[i][j] = board[i + 1][j];
            board[i + 1][j] = under;
            String number_moved = board[i][j] + "U";
            Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);
                    list.add(a);
        }
        if ((j - 1) >= 0) {
            board = puzzle_copy(father.getPuzzle());
            String under = "_";
            board[i][j] = board[i][j - 1];
            board[i][j - 1] = under;
            String number_moved = board[i][j] + "R";
            Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                    list.add(a);
        }
        if ((i - 1) >= 0) {
            board = puzzle_copy(father.getPuzzle());
            String under = "_";
            board[i][j] = board[i - 1][j];
            board[i - 1][j] = under;
            String number_moved = board[i][j] + "D";

            Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                    list.add(a);

        }
        if(list.contains(this.father))
            list.remove(this.father);
        return list;
    }

    public List<Node> two_block(Node father) {
        int row = this.row, col = this.col;
        String[][] board = new String[row][col];
        List<Node> list = new LinkedList<>();
        String under = "_";
        int[] first = this.index1;
        int[] second = this.index2;
        int i = first[0];
        int j = first[1];
        int i1 = second[0];
        int j1 = second[1];
        if (i1 > i) {
            if ((j + 1) < col && (j1 + 1) < col) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1][j1 + 1];
                board[i][j] = board[i][j + 1];
                board[i][j + 1] = under;
                board[i1][j1 + 1] = under;
                String number_moved = board[i][j] + "&" + board[i1][j1] + "L";
                    Node a = new Node(board, father.getCost() + 6, father, number_moved, father.empty_block);
                        list.add(a);

            }
            if ((j - 1) >= 0 && (j1 - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1][j1 - 1];
                board[i][j] = board[i][j - 1];
                board[i][j - 1] = under;
                board[i1][j1 - 1] = under;
                String number_moved = board[i][j] + "&" + board[i1][j1] + "R";

                    Node a = new Node(board, father.getCost() + 6, father, number_moved, father.empty_block);

                        list.add(a);
            }
            if ((j + 1) < col) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i][j + 1];
                board[i][j + 1] = under;
                String number_moved = board[i][j] + "L";

                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

            }
            if ((j - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i][j - 1];
                board[i][j - 1] = under;
                String number_moved = board[i][j] + "R";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

            }
            if ((i - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i - 1][j];
                board[i - 1][j] = under;
                String number_moved = board[i][j] + "D";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

            }
            if ((j1 + 1) < col) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1][j1 + 1];
                board[i1][j1 + 1] = under;
                String number_moved = board[i1][j1] + "L";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);
                        list.add(a);
            }
            if ((i1 + 1) < row) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1 + 1][j1];
                board[i1 + 1][j1] = under;
                String number_moved = board[i1][j1] + "U";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

            }
            if ((j1 - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1][j1 - 1];
                board[i1][j1 - 1] = under;
                String number_moved = board[i1][j1] + "R";
                Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

                return list;
            }
        }

        else if (j1 > j) {
            if ((i + 1) < row && (i1 + 1) < row) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i + 1][j];
                board[i1][j1] = board[i1 + 1][j1];
                board[i + 1][j] = under;
                board[i1 + 1][j1] = under;
                String number_moved = board[i][j] + "&" + board[i1][j1] + "U";
                    Node a = new Node(board, father.getCost() + 7, father, number_moved, father.empty_block);

                       list.add(a);

            }
            if ((i - 1) >= 0 && (i1 - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i - 1][j];
                board[i - 1][j] = under;
                board[i1][j1] = board[i1 - 1][j1];
                board[i1 - 1][j1] = under;
                String number_moved = board[i][j] + "&" + board[i1][j1] + "D";
                    Node a = new Node(board, father.getCost() + 7, father, number_moved, father.empty_block);

                       list.add(a);

            }
            if ((i + 1) < row) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i + 1][j];
                board[i + 1][j] = under;
                String number_moved = board[i][j] + "U";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                       list.add(a);

            }
            if ((j - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i][j - 1];
                board[i1][j - 1] = under;
                String number_moved = board[i][j] + "R";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                       list.add(a);

            }
            if ((i - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i][j] = board[i - 1][j];
                board[i - 1][j] = under;
                String number_moved = board[i][j] + "D";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);
            }
            if ((j1 + 1) < col) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1][j1 + 1];
                board[i1][j1 + 1] = under;
                String number_moved = board[i1][j1] + "L";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                    list.add(a);

            }
            if ((i1 + 1) < row) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1 + 1][j1];
                board[i1 + 1][j1] = under;
                String number_moved = board[i1][j1] + "U";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);

                        list.add(a);

            }
            if ((i1 - 1) >= 0) {
                board = puzzle_copy(father.getPuzzle());
                board[i1][j1] = board[i1 - 1][j1];
                board[i1 - 1][j1] = under;
                String number_moved = board[i1][j1] + "D";
                    Node a = new Node(board, father.getCost() + 5, father, number_moved, father.empty_block);
                        list.add(a);

            }

            return list;
        }
        if(list.contains(this.father))
            list.remove(this.father);
        return list;
    }


    public List<Node> children() {
        List<Node> child_list = new LinkedList<>();
        int[] first;
        int[] second;
        String[][] board = this.puzzle;
        if (this.empty_block == 1) {
            child_list = one_block(this, this.index1);
        }
        if (this.empty_block == 2) {
            first = Arrays.copyOf(index1,2);
            second = Arrays.copyOf(index2,2);
            switch ((this.distance())) {
                case 1:
                    Node temp = new Node(this);
                    List<Node> first_list = one_block(temp, first);
                    Node temp2 = new Node(this);
                    List<Node> second_list = one_block(temp2, second);
                    for (Node temp3 : first_list) {
                        child_list.add(temp3);
                    }
                    for (Node temp4 : second_list) {
                        child_list.add(temp4);
                    }
                    return child_list;
                case 2:
                    Node s_temp = new Node(this);
                    List<Node> s_second_list = one_block(s_temp, index2);
                    Node s_temp2 = new Node(this);
                    List<Node> s_first_list = one_block(s_temp2, index1);
                    for (Node temp3 : s_second_list) {
                        child_list.add(temp3);
                    }
                    for (Node temp4 : s_first_list) {
                        child_list.add(temp4);
                    }
                    return child_list;
                case 3:
                    Node t_temp = new Node(this);
                    child_list = two_block(t_temp);
                    return child_list;
            }
        }
        return child_list;
    }

    private static String[][] puzzle_copy(String[][] puzzle) {
        String[][] board = new String[puzzle.length][puzzle[0].length];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                board[i][j] = puzzle[i][j];
            }
        }
        return board;
    }

    public int distance() {
        int i1 = this.getIndex1()[0];
        int j1 = getIndex1()[1];
        int i2 = getIndex2()[0];
        int j2 = getIndex2()[1];
        int distance = (int)Math.sqrt(Math.pow((i1 - i2),2)+Math.pow((j1 - j2),2));
        if (distance == 1) {
            return 3;
        }
        return 1;
    }

    public void manhaten(Node temp,Node goal){
        int ans=0;
        for (int i = 0; i <temp.getPuzzle().length; i++) {
            for (int j = 0; j < temp.getPuzzle()[0].length; j++) {
                if(!temp.getPuzzle()[i][j].equals("_")){
                    int [] index=m.get(temp.getPuzzle()[i][j]);
                    ans += 3*(Math.abs(index[0] - i) + Math.abs(index[1] - j));
                }
            }
        }
        this.huristic=ans+this.cost;
    }
    public void setVisited(boolean a) {
        this.visited = a;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPuzzle(String[][] puzzle) {
        this.puzzle = puzzle;
    }

    public Node getFather() {
        return father;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public int[] getIndex1() {
        return index1;
    }

    public int[] getIndex2() {
        return index2;
    }


    public String[][] getPuzzle() {
        return puzzle;
    }

    @Override
    public int compareTo(Object o) {
        if(this.getHuristic()<((Node) o).getHuristic())
            return -1;
        else if(this.getHuristic()>((Node) o).getHuristic())
            return 1;
        else return 0;
    }
}
