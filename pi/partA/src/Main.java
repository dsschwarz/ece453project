public class Main {
    public static void main(String [] args) {
//        String filename = "callgraphtest.txt";
        String filename = args[0];

        System.out.println("Parsing file " + filename);

        Parser parser = new Parser();
        parser.parseFile(filename);

        System.out.println("Scope count " + parser.scopes.size());
    }
}
