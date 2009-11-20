package jsr223;

import java.io.File;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptVars { 
    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Clojure");

        File f = new File("samples/jsr223/ScriptVars.java");
        // expose File object as variable to script
        engine.put("file", f);

        // evaluate a script string. The script accesses "file" 
        // variable and calls method on it
        engine.eval("(println (.getAbsolutePath file))");
    }
}


