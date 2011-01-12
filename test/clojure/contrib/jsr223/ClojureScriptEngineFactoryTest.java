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

import java.util.List;

import javax.script.ScriptEngine;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class ClojureScriptEngineFactoryTest {

	/**
	 * Runs the test suite in this class from the command line.
	 * 
	 * @param args	Arguments are ignored.
	 */
	public static void main(String[] args) {
		org.junit.runner.JUnitCore.runClasses(ClojureScriptEngineFactoryTest.class);
	}
	
	/**
	 * Provides compatibility with 3.x versions of JUnit.
	 * 
	 * @return A 3.x-compatible test suite.
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ClojureScriptEngineFactoryTest.class);
	}
	
    /*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Test the constructor.                                                | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/
	
	@Test
	public void defaultConstructor() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		Assert.assertNotNull(f);
	}
	
	@Test
	public void defaultInstanceClass() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		Assert.assertEquals(ClojureScriptEngineFactory.class, f.getClass());
	}

    /*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Mainline tests.                                                      | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/
	
	@Test
	public void getEngineName() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String name = f.getEngineName();
		Assert.assertEquals("Clojure Scripting Engine", name);
	}
	
	@Test
	public void getEngineVersion() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String name = f.getEngineVersion();
		Assert.assertEquals("1.2", name);
	}
	
	@Test
	public void getExtensions() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		List<String> extensions = f.getExtensions();
		Assert.assertEquals("clj", extensions.get(0));
		Assert.assertEquals(1, extensions.size());
	}
	
	@Test
	public void getLanguageName() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String name = f.getLanguageName();
		Assert.assertEquals("Clojure", name);
	}
	
	@Test
	public void getLanguageVersion() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String name = f.getLanguageVersion();
		Assert.assertEquals("1.2", name);
	}
	
	@Test
	public void getMethodCallSyntax_NotNull() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String name = f.getMethodCallSyntax("myObject", "method");
		Assert.assertNotNull(name);
		name = f.getMethodCallSyntax("myObject", "method", "arg1");
		Assert.assertNotNull(name);
		name = f.getMethodCallSyntax("myObject", "method", "arg1", "arg2");
		Assert.assertNotNull(name);
		name = f.getMethodCallSyntax("myObject", "method", "arg1", "arg2", "arg3");
		Assert.assertNotNull(name);
	}
	
	@Test
	public void getMethodCallSyntax() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String syntax = f.getMethodCallSyntax("myObject", "method");
		Assert.assertEquals("(.method myObject)", syntax); 
		syntax = f.getMethodCallSyntax("myObject", "method", "arg1");
		Assert.assertEquals("(.method myObject arg1)", syntax); 
		syntax = f.getMethodCallSyntax("myObject", "method", "arg1", "arg2");
		Assert.assertEquals("(.method myObject arg1 arg2)", syntax); 
		syntax = f.getMethodCallSyntax("myObject", "method", "arg1", "arg2", "arg3");
		Assert.assertEquals("(.method myObject arg1 arg2 arg3)", syntax); 
	}
	
	@Test
	public void getMimeTypes() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		List<String> mimeTypes = f.getMimeTypes();
		Assert.assertEquals("application/clojure", mimeTypes.get(0));
		Assert.assertEquals("text/clojure", mimeTypes.get(1));
		Assert.assertEquals(2, mimeTypes.size());
	}
	
	@Test
	public void getNames() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		List<String> names = f.getNames();
		Assert.assertEquals("Clojure", names.get(0));
		Assert.assertEquals(1, names.size());
	}
	
	@Test
	public void getOutputStatement_NotNull() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String statement = f.getOutputStatement("Hello, Clojure");
		Assert.assertNotNull(statement);
	}
	
	@Test
	public void getOutputStatement() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String statement = f.getOutputStatement("Hello, Clojure");
		Assert.assertEquals("(println \"Hello, Clojure\")", statement);
	}
	
	@Test
	public void getProgram_NotNull() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String program = f.getProgram();
		Assert.assertNotNull(program);
		program = f.getProgram("Hello, Clojure");
		Assert.assertNotNull(program);
	}
	
	@Test
	public void getProgram() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		String program = f.getProgram();
		Assert.assertEquals("", program);
		program = f.getProgram("(foo 1 2 3)");
		Assert.assertEquals("(foo 1 2 3)\n", program);
		program = f.getProgram("(foo 1 2 3)", "(bar a b)");
		Assert.assertEquals("(foo 1 2 3)\n(bar a b)\n", program);
		program = f.getProgram("(foo 1 2 3)", "(bar a b)", "(if true true false)");
		Assert.assertEquals("(foo 1 2 3)\n(bar a b)\n(if true true false)\n", program);
	}
	
	@Test
	public void getScriptEngine_NotNull() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		ScriptEngine engine = f.getScriptEngine();
		Assert.assertNotNull(engine);
	}
	
	@Test
	public void getScriptEngine() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		ScriptEngine engine = f.getScriptEngine();
		Assert.assertEquals(ClojureScriptEngine.class, engine.getClass());
	}
	
    /*+----------------------------------------------------------------------+
	  |                                                                      | 
	  | Negative tests.                                                      | 
	  |                                                                      | 
	  +----------------------------------------------------------------------+*/

	@Test(expected=NullPointerException.class)
	public void getMethodCallSyntax_NullObject() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		f.getMethodCallSyntax(null, "method");
	}

	@Test(expected=NullPointerException.class)
	public void getMethodCallSyntax_NullMethod() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		f.getMethodCallSyntax("myObject", null);
	}

	@Test(expected=NullPointerException.class)
	public void getOutputStatement_NullArgument() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		f.getOutputStatement(null);
	}

	@Test(expected=NullPointerException.class)
	public void getParameter_NullArgument() {
		ClojureScriptEngineFactory f = new ClojureScriptEngineFactory();
		f.getParameter(null);
	}

}
