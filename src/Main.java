import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    static int dim=9;
    static int[][] grid;
    static HashSet<Integer>[] available;
    public static void main(String[] args) throws FileNotFoundException {
        grid= new int[dim][dim];
        available= new HashSet[3*dim];
        addAllToAvailable();
        getGrid();
        solve();
        printGrid();

    }
    private static void getGrid() throws FileNotFoundException {
        Scanner scanner= new Scanner(new File("./sodukugrid.txt"));
        for(int i=0;i<dim;i++){
            String row = scanner.nextLine();
            for(int j=0;j<dim;j++){
                char c = row.charAt(j);
                int value = setNumber(c);
                grid[i][j]=value;
                int currentField = dim*i+j;
                int currentBox= 3*(currentField/(3*dim)) + ((currentField%dim)/3);
                available[i].remove(value);
                available[dim+j].remove(value);
                available[2*dim+currentBox].remove(value);
            }
        }
    }
    private static int setNumber(char c){
        int value = Character.getNumericValue(c);
        return (value != -1)? value:0;
    }
    private static boolean solve(){
        return solve(0,0);
    }
    private static boolean solve(int row, int col){
        int nextField = dim*row+col+1;
        if(nextField==81)
            return true;
        if(grid[row][col]!=0){
            return solve(nextField/dim,nextField%dim);
        }
        int currentField = dim*row+col;
        int currentBox= 3*(currentField/(3*dim)) + ((currentField%dim)/3);
        HashSet<Integer> remaining = new HashSet<>();
        remaining.addAll(available[row]);
        remaining.retainAll(available[dim+col]);
        remaining.retainAll(available[2*dim+currentBox]);
        if (remaining.size()==0)
            return false;
        for(int value:remaining){
            available[row].remove(value);
            available[dim+col].remove(value);
            available[2*dim+currentBox].remove(value);
            grid[row][col]= value;
            boolean works = solve(nextField/dim,nextField%dim);
            if (works){
                return true;
            }
            else{
                grid[row][col]=0;
                available[row].add(value);
                available[dim+col].add(value);
                available[2*dim+currentBox].add(value);
            }
        }
        return false;
    }
    private static void addAllToAvailable(){
        HashSet<Integer> tempVals = new HashSet<>();
        for(int i=1;i<=dim;i++){
            tempVals.add(i);
        }
        for(int i=0;i<available.length;i++){
            available[i] = new HashSet<>();
            available[i].addAll(tempVals);
        }
    }
    private static void printGrid(){
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                System.out.print(grid[i][j]+" ");
            }
            System.out.println();
        }
    }
}
