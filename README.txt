Copyright (c) 2009 Armando Blancas. All rights reserved.
 
The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.

By using this software in any fashion, you are agreeing to be bound by
the terms of this license.

You must not remove this notice, or any other, from this software.
------------------------------------------------------------------------------

To Build: ant

(1) Requires Java 1.6

(2) The <javadoc> task in build.xml points to "/dev/tools/java/src" 
    for the Java sources. Change as needed or just ignore it and get
    the docs from the distribution jar in Downloads.
    
(3) Place clojure-jsr223.jar where you keep clojure.jar and clojure-contrib.jar,
    and add it to your classpath.

(4) The binary distribution clojure-jsr223 does not contain the Clojure
    distribution jars. They're here in the lib directory along with ant
    and junit for making the build reproducible but especially for
    convenience. Replace them or point to your own locations.
