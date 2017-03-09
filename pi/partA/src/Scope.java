import java.util.HashSet;
import java.util.Set;

public class Scope {
    public String name;
    public Set<String> functionCalls; // names of all the functions called in this scope. As per spec, duplicates are ignored

    Scope(String name) {
        this.name = name;
        this.functionCalls = new HashSet<String>();
    }
}
