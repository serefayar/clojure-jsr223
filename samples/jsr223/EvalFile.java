package jsr223;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalFile {
	
	/*
	 *  Let us assume that we have the file named "test.clj" with the following text:
	 * 
     *  (println "This is hello from test.clj")
     * 
     *  We can run the above Java as
     *
     *  java -cp clojure.jar;clojure-contrib.jar;clojure-223.jar EvalFile test.clj
	 */

    public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create Clojure engine
        ScriptEngine engine = factory.getEngineByName("Clojure");
        // evaluate Clojure code from given file - specified by first argument
        engine.eval(new java.io.FileReader(args[0]));
    }
    
}
