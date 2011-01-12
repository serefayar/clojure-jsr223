/*
 * Copyright (c) 2009 Armando Blancas. All rights reserved.
 * 
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * 
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * 
 * You must not remove this notice, or any other, from this software.
 */
package clojure.contrib.jsr223;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.junit.Test;

public class ClojureScriptEngineTest {

	/**
	 * Runs the test suite in this class from the command line.
	 * 
	 * @param args	Arguments are ignored.
	 */
	public static void main(String[] args) {
		org.junit.runner.JUnitCore.runClasses(ClojureScriptEngineTest.class);
	}
	
	/**
	 * Provides compatibility with 3.x versions of JUnit.
	 * 
	 * @return A 3.x-compatible test suite.
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ClojureScriptEngineTest.class);
	}
	
    /*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Test the constructor.                                                | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/
	
	@Test
	public void fromFactory() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		Assert.assertNotNull(f);
		Assert.assertNotNull(f.getScriptEngine());
	}
	
	@Test
	public void fromFactoryInstanceClass() {
		ScriptEngineFactory f = new ClojureScriptEngineFactory();
		ScriptEngine engine = f.getScriptEngine();
		Assert.assertEquals(ClojureScriptEngineFactory.class, f.getClass());
		Assert.assertEquals(ClojureScriptEngine.class, engine.getClass());
	}
	
	@Test
	public void constructor() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		ClojureScriptEngine engine = new ClojureScriptEngine(f);
		Assert.assertNotNull(f);
		Assert.assertNotNull(engine);
	}

	@Test
	public void constructorInstanceClass() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		ClojureScriptEngine engine = new ClojureScriptEngine(f);
		Assert.assertEquals(ClojureScriptEngineFactory.class, f.getClass());
		Assert.assertEquals(ClojureScriptEngine.class, engine.getClass());
	}

    /*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Mainline tests.                                                      | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/
	
	@Test
	public void bindings() {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		Bindings bindings = engine.createBindings();
		Assert.assertNotNull(bindings);
		Assert.assertEquals(SimpleBindings.class, bindings.getClass());
	}
	
	@Test
	public void eval_predef() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		Object result = engine.eval("(+ 3 4)");
		Assert.assertEquals(7, result);
	}
	
	@Test
	public void eval_defn() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		Object result = engine.eval("(defn mid [low high] (/ (+ low high) 2)) (mid 10 20)");
		Assert.assertEquals(15, result);
	}
	
	@Test
	public void eval_WithStringReader() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		StringReader r = new StringReader("(defn mid [low high] (/ (+ low high) 2)) (mid 10 20)");
		Object result = engine.eval(r);
		Assert.assertEquals(15, result);
	}
	
	@Test
	public void eval_WithFileReader() throws ScriptException, FileNotFoundException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		FileReader r = new FileReader("./scripts/clojure-loc.clj");
		Object result = engine.eval(r);
		Assert.assertEquals(200, result);
	}
	
	@Test
	public void eval_WithBinding() throws ScriptException {
        File file = new File("./scripts/converter.clj");
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
        engine.put("myFile", file);

		String s = (String) engine.eval("(defn get-path [f] (.getPath f)) (get-path myFile)");
		Assert.assertEquals("./scripts/converter.clj", s.replace(File.separatorChar, '/'));
	}
	
	@Test
	public void getInterface() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
        engine.eval(
        	"(import java.awt.event.ActionListener) " + 
        	"(def ActionListenerImpl                " +
        	"  (proxy [ActionListener] []           " +
        	"    (actionPerformed [evt]             " + 
        	"      (println evt))))");

        Invocable inv = (Invocable) engine;
        ActionListener listener = inv.getInterface(ActionListener.class);
		Assert.assertEquals(true, listener instanceof ActionListener);
	}
	
	@Test
	public void getInterface_WithNS() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
        engine.eval(
        	"(ns foo-ns)                            " +
        	"(import java.awt.event.ActionListener) " + 
        	"(def ActionListenerImpl                " +
        	"  (proxy [ActionListener] []           " +
        	"    (actionPerformed [evt]             " + 
        	"      (println evt))))");

        Invocable inv = (Invocable) engine;
        ActionListener listener = inv.getInterface("foo-ns", ActionListener.class);
		Assert.assertEquals(true, listener instanceof ActionListener);
	}
	
	@Test
	public void invokeFunction() throws ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		engine.eval("(defn mid [low high] (/ (+ low high) 2)) (mid 10 20)");
		
        Invocable inv = (Invocable) engine;
        Object result = inv.invokeFunction("mid", 10, 20);
		Assert.assertEquals(15, result);
	}
	
	@Test
	public void invokeFunction_WithNS() throws ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		engine.eval("(ns bar-ns)(defn middle [low high] (/ (+ low high) 2)) (middle 10 20)");
		
        Invocable inv = (Invocable) engine;
        Object result = inv.invokeFunction("bar-ns/middle", 10, 20);
		Assert.assertEquals(15, result);
	}
	
	@Test
	public void invokeMethod() throws ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		engine.eval("(defn midpoint [low high] (/ (+ low high) 2)) (midpoint 10 20)");
		
        Invocable inv = (Invocable) engine;
        Object result = inv.invokeMethod(null, "midpoint", 10, 20);
		Assert.assertEquals(15, result);
	}
	
	@Test
	public void invokeMethod_WithNS() throws ScriptException, NoSuchMethodException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		engine.eval("(ns bar-ns)(defn midpoint [low high] (/ (+ low high) 2)) (midpoint 10 20)");
		
        Invocable inv = (Invocable) engine;
        Object result = inv.invokeMethod(null, "bar-ns/midpoint", 10, 20);
		Assert.assertEquals(15, result);
	}
/*	
	@Test
	public void compileLibrary() throws ScriptException {
		Delete delete = new Delete();
		delete.setDir(new File("classes"));
		delete.execute();
		Mkdir mkdir = new Mkdir();
		mkdir.setDir(new File("classes"));
		mkdir.execute();
		File file = new File("classes/snake/snake_main.class");
		Assert.assertFalse(file.exists());
		
		Compilable compile = (Compilable) new ClojureScriptEngineFactory().getScriptEngine();
		compile.compile("snake.snake-main");
		Assert.assertTrue(file.exists());
	}

	@Test
	public void compileLibrary_FromReader() throws ScriptException, FileNotFoundException {
		Delete delete = new Delete();
		delete.setDir(new File("classes"));
		delete.execute();
		Mkdir mkdir = new Mkdir();
		mkdir.setDir(new File("classes"));
		mkdir.execute();
		File file = new File("classes/snake/snake_main.class");
		Assert.assertFalse(file.exists());
		file = new File("classes/converter.class");
		Assert.assertFalse(file.exists());

		Compilable compile = (Compilable) new ClojureScriptEngineFactory().getScriptEngine();
		compile.compile(new FileReader("scripts/to-compile.txt"));
		file = new File("classes/snake/snake_main.class");
		Assert.assertTrue(file.exists());
		file = new File("classes/converter.class");
		Assert.assertTrue(file.exists());
	}
*/
	/*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Negative tests.                                                      | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/

	@Test(expected=NullPointerException.class)
	public void constructor_NullArgument() {
		new ClojureScriptEngine(null);
	}

	@Test(expected=NullPointerException.class)
	public void eval_NullScript() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		String script = null;
		engine.eval(script);
	}
	
	@Test(expected=NullPointerException.class)
	public void eval_NullReader() throws ScriptException {
		ScriptEngine engine = new ClojureScriptEngineFactory().getScriptEngine();
		Reader reader = null;
		engine.eval(reader);
	}

}
