public class Main {
    public static void main(String[] args) {
        Viewer viewer = new Viewer();
        FieldGenerator generator = new FieldGenerator();
        int[][] field =  generator.generateField();
        for(int i = 0; i < field.length; i++) {
           for(int j = 0; j < field[i].length; j++) {
               System.out.print(field[i][j] + "  ");
           }
           System.out.println();
       }
   }
}
