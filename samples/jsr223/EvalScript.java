package jsr223;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalScript {
	
    public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a Clojure engine
        ScriptEngine engine = factory.getEngineByName("Clojure");
        // evaluate Clojure code from String
        engine.eval("(println \"Hello, World\")");
    }
    
}
