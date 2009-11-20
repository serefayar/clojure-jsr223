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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/*
 * The annotated list of suite classes should include all Test classes
 * in the current package. Each one contains a suite of unit tests.
 */
@RunWith(Suite.class)
@SuiteClasses({
    ClojureScriptEngineFactoryTest.class,
    ClojureScriptEngineTest.class
})

/**
 * Runs all tests suites in this package.
 */
public class RunAllTests {
}
