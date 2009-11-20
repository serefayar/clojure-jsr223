/**
 * Implements the Java Scripting API for the Clojure programming language.
 * <p>
 * The Clojure Scripting Engine is available as a service provider through 
 * the {@code javax.script.ScriptEngineManager} class. It comes packaged in the 
 * file {@code clojure-jsr223.jar}, which depends on {@code clojure.jar} and 
 * {@code clojure-contrib.jar}.
 * <p>
 * To use this engine, place the file {@code clojure-jsr223.jar} in the same
 * location where the clojure jar files reside and add it to your <i>classpath</i>.
 * Then you may get an instance of the engine thus:
 * <pre>
 * // create a script engine manager
 * ScriptEngineManager factory = new ScriptEngineManager();
 * // create a Clojure engine
 * ScriptEngine engine = factory.getEngineByName("Clojure");
 * </pre>
 * 
 * @author Armando Blancas
 * @version 1.0
 */
package clojure.contrib.jsr223;

