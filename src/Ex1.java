import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ex1 {
    static boolean isOpen = false;
    public static void main(String[] args) {
        long startTime,endTime,time;
        boolean withTime = false;
        String [] ans=null;
        double finalTime=0;
        try {
            File file = new File("input2.txt");
            int i = 0, count = 0;
            Scanner scan = new Scanner(file);
            String algo = scan.nextLine();
            if (scan.nextLine().equals("with time"))
                withTime = true;
            if (scan.nextLine().equals("with open"))
                isOpen = true;
            String line = scan.nextLine();
            int row = Integer.parseInt(line.charAt(0)+"");
            int col = Integer.parseInt(line.charAt(2)+"");
            String[][] startBoard = new String[row][col];
            String[][] goalBoard = new String[row][col];
            String temp = scan.nextLine();
            while (temp.charAt(0) != 'G') {
                String[] ar = temp.split(",");
                startBoard[i++] = ar;
                if (temp.contains("_")) {
                    for (int j = 0; j < ar.length; j++) {
                        if (ar[j].equals("_"))
                            count++;
                    }
                }
                temp = scan.nextLine();
            }
            i = 0;
            while (scan.hasNextLine()) {
                String temp_goal = scan.nextLine();
                String[] ar = temp_goal.split(",");
                goalBoard[i++] = ar;
            }
            scan.close();
            Node gameNode = new Node(startBoard, 0, null, "", count);
            Node goalNode = new Node(goalBoard, 0, null, "", count);

            if(algo.equals("BFS")){
                    startTime = System.nanoTime();
                    ans=BFS(gameNode, goalNode).split(",");
                    endTime = System.nanoTime();
                    time=endTime-startTime;
                    finalTime=time / Math.pow(10, 9);
            }
            else if(algo.equals("A*")){
                startTime = System.nanoTime();
                ans=A_star(gameNode, goalNode).split(",");
                endTime = System.nanoTime();
                time=endTime-startTime;
                finalTime=time / Math.pow(10, 9);
            }
            else if(algo.equals("IDA*")){
                startTime = System.nanoTime();
                ans=IDA_star(gameNode, goalNode).split(",");
                endTime = System.nanoTime();
                time=endTime-startTime;
                finalTime=time / Math.pow(10, 9);
            }
            else if(algo.equals("DFID")){
                startTime = System.nanoTime();
                ans=DFID(gameNode, goalNode).split(",");
                endTime = System.nanoTime();
                time=endTime-startTime;
                finalTime=time / Math.pow(10, 9);
            }
            else if(algo.equals("DFBnB")){
                startTime = System.nanoTime();
                ans=DFBnB(gameNode, goalNode).split(",");
                endTime = System.nanoTime();
                time=endTime-startTime;
                finalTime=time / Math.pow(10, 9);
            }



        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
            try {
                FileWriter myWriter = new FileWriter("output.txt");
                myWriter.write(ans[0]+"\n"+ans[1]+"\n"+ans[2]+"\n");
                if(withTime)
                    myWriter.write(String.valueOf(finalTime)+" seconds");
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public static String getPath(Node node) {
        String result="";
        List<Node> l_path=new ArrayList<>();
        Node temp=node;
        Node temp2=null;
        while (temp.getFather()!=null){
            l_path.add(temp);
            temp=temp.getFather();
        }
        Collections.reverse(l_path);
        while (!l_path.isEmpty()) {
            temp2 = l_path.remove(0);
            if (l_path.size() == 0)
                result += temp2.getMove();
            else {
                result += temp2.getMove();
                result += "-";
            }
        }
        return result;
    }


    public static String BFS(Node start, Node Goal) {
        List<Node> nodeList = new ArrayList<>();
        int nodeNum=0;
        String fail = "fail";
        Hashtable<String, Node> open_list = new Hashtable<>();
        open_list.put(start.toString(),start);
        Hashtable<String, Node> close_list = new Hashtable<>();
        nodeList.add(start);
        while (!nodeList.isEmpty()) {
            Node n = nodeList.remove(0);
            close_list.put(n.toString(), n);
            n.setVisited(true);
            if(isOpen){
                for (Node node:open_list.values()){
                    System.out.println(Arrays.deepToString(node.getPuzzle()));
                }
            }
            List<Node> list = n.children();
            nodeNum += list.size();
            for (Node node : list) {
                if (!close_list.containsKey(node.toString())) {
                    if ((!open_list.containsKey(node.toString()))) {
                        if (node.equals(Goal)) {
                           return algoResult(node,nodeNum);
                        }
                        else {
                            open_list.put(node.toString(), node);
                            nodeList.add(node);
                        }
                    }
                }
            }
        }
        return fail;
    }



    static int staticCounter =0;
    public static String DFID(Node start, Node goal) {
        String result="";
        String ans="fail";
        String t_path="";
        int depth=0;
        for (depth = 0; depth < Integer.MAX_VALUE; depth++) {
            Hashtable<String, Node> open_list = new Hashtable<>();
            result=Limited_DFS(start,goal,depth,open_list);
            t_path=result;
            String [] path=t_path.split(",");

            if (!path[0].equals("cutoff")){
                ans=path[0];
                ans+=","+path[1];
                ans+=","+path[2];
                return ans;
            }
        }
        return ans;
    }
    public static String Limited_DFS(Node node,Node goal,int limit,Hashtable<String,Node> open_list){
        String result="";
        String fail="fail";
        boolean cutOff=false;
        if (node.equals(goal)){
            result=algoResult(node,staticCounter);
            return result;
        }
        else if(limit==0){
            cutOff=true;
            return "cutoff";
        }

        else{
            cutOff=false;
            open_list.put(node.toString(),node);
            List<Node> lists=node.children();
            staticCounter+=lists.size();
            for (Node temp:lists){
                if (open_list.containsKey(temp.toString())){
                    continue;
                }
                result=Limited_DFS(temp,goal,limit-1,open_list);
                if (result.equals("cutoff")){
                    cutOff=true;
                }
                else if(!result.equals(fail)){
                    return result;
                }
            }
            if(isOpen) {
                for (Node temp : open_list.values()) {
                    System.out.println(Arrays.deepToString(temp.getPuzzle()));
                }
            }
            open_list.remove(node);
            if (cutOff==true){
                return "cutoff";
            }
            else{
                return fail;
            }
        }
    }

    public static String A_star(Node start, Node goal) {
        Queue<Node> q = new PriorityQueue<>();
        String fail="fail";
        int nodeNum = 0;
        Hashtable<String, Node> open_list = new Hashtable<>();
        Hashtable<String, Node> close_list = new Hashtable<>();
        goal.update(goal);
        open_list.put(start.toString(),start);
        q.add(start);
        while (!q.isEmpty()) {
            Node node = q.remove();
            if (node.equals(goal)) {
                return algoResult(node,nodeNum);
            }
            node.setVisited(true);
            close_list.put(node.toString(), node);
            List<Node> list = node.children();
            for (Node s : list) {
                if (!close_list.containsKey(s.toString())) {
                    if ((!open_list.containsKey(s.toString()))) {
                        s.manhaten(s,goal);
                        open_list.put(s.toString(), s);
                        q.add(s);
                    } else if (s.getHuristic() < open_list.get(s.toString()).getHuristic()) {
                        if(isOpen) {
                            for (Node temp : open_list.values()) {
                                System.out.println(Arrays.deepToString(temp.getPuzzle()));
                            }
                        }
                        open_list.remove(s.toString());
                        open_list.put(s.toString(), s);
                        s.setFather(node);
                    }
                    nodeNum++;
                }
            }
        }
        return fail;
    }


    public static String IDA_star(Node start, Node goal){
        Stack<Node> nodeStack=new Stack<>();
        String fail="fail";
        goal.update(goal);
        Hashtable<String, Node> open_list = new Hashtable<>();
        open_list.put(start.toString(),start);
        start.manhaten(start,goal);
        int nodeNum=0;
        int minF=0;
        int trash_hold=start.getHuristic();
        while (trash_hold!=Integer.MAX_VALUE){
            nodeStack.push(start);
            minF=Integer.MAX_VALUE;
            start.setVisited(false);
            open_list.put(start.toString(),start);
            while (!nodeStack.isEmpty()){
                Node node=nodeStack.pop();
                if (node.getVisited()==true){
                    if(isOpen) {
                        for (Node temp : open_list.values()) {
                            System.out.println(Arrays.deepToString(temp.getPuzzle()));
                        }
                    }
                    open_list.remove(node.toString());
                }
                else{
                    nodeStack.push(node);
                    node.setVisited(true);
                    List<Node> children=node.children();
                    nodeNum+=children.size();
                    for(Node temp:children){
                        temp.manhaten(temp,goal);
                        if (temp.getHuristic()>trash_hold){
                            minF=Math.min(minF,temp.getHuristic());
                            continue;
                        }
                        if (temp.getVisited()==true&&open_list.containsKey(temp.toString())){
                            continue;
                        }
                        if (temp.getVisited()==false&&open_list.containsKey(temp.toString())){
                            if (temp.getHuristic()<open_list.get(temp.toString()).getHuristic()){
                                nodeStack.remove(open_list.get(temp.toString()));
                                if(isOpen) {
                                    for (Node temp1 : open_list.values()) {
                                        System.out.println(Arrays.deepToString(temp1.getPuzzle()));
                                    }
                                }
                                open_list.remove(temp.toString());
                            }
                            else{
                                continue;
                            }
                        }
                        if (temp.equals(goal)){
                            return algoResult(temp,nodeNum);
                        }
                        nodeStack.push(temp);
                        open_list.put(temp.toString(),temp);

                    }
                }
            }
            trash_hold=minF;
        }
        return fail;
    }


    public static String DFBnB(Node start,Node goal){
        int trash_hold=Integer.MAX_VALUE;
        int nodeNum=0;
        String ans="fail";
        Stack<Node> nodeStack=new Stack<>();
        Hashtable<String, Node> open_list = new Hashtable<>();
        open_list.put(start.toString(),start);
        nodeStack.push(start);
        goal.update(goal);
        while(!nodeStack.empty()){
            Node node=nodeStack.pop();
            if (node.getVisited()==true){
                open_list.remove(node.toString());
            }
            else{
                node.setVisited(true);
                nodeStack.push(node);
                List<Node> children=node.children();
                nodeNum+=children.size();
                for (Node temp:children){
                    temp.manhaten(temp,goal);
                }
                Collections.sort(children);
                List<Node>copy_list=listCopy(children);

                for (Node g:children){
                    if (g.getHuristic()>=trash_hold){
                        children.subList(children.indexOf(g), children.size()).clear();
                    }
                    else if(open_list.containsKey(g.toString()) && open_list.get(g.toString()).getVisited()==true){
                        copy_list.remove(g);
                    }
                    else if(open_list.containsKey(g.toString()) && open_list.get(g.toString()).getVisited()==false){
                        if (g.getHuristic()>=open_list.get(g.toString()).getHuristic()){
                            copy_list.remove(g);
                        }
                        else {
                            nodeStack.remove(open_list.get(g.toString()));
                            if(isOpen) {
                                for (Node temp : open_list.values()) {
                                    System.out.println(Arrays.deepToString(temp.getPuzzle()));
                                }
                            }
                            open_list.remove(open_list.get(g.toString()));
                        }
                    }
                    else if (g.equals(goal)){
                        trash_hold=g.getHuristic();
                        children.subList(children.indexOf(g), children.size()).clear();
                        ans=algoResult(g,nodeNum);
                    }
                }
                List<Node> ans_list=new ArrayList<>();
                for (Node temp:copy_list){
                    if (children.contains(temp))
                        ans_list.add(temp);
                }
                Collections.reverse(ans_list);
                for (Node temp:ans_list){
                    nodeStack.push(temp);
                    open_list.put(temp.toString(),temp);
                }
            }
        }

        return ans;
    }

    public static String algoResult(Node node,int nodeNum){
        String ans=getPath(node);
        ans+=",Num: " + nodeNum;
        ans+=",Cost: " + node.getCost();
        return ans;
    }

    public static List<Node> listCopy(List<Node> list){
        List<Node> temp = new ArrayList<>();
        for(Node node:list){
            temp.add(node);
        }
        return temp;
    }

}

