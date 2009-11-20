;;
;; Sample code taken from Programming Clojure, by Stuart Halloway.
;;
;; Used to count the lines in the snake directory 
;; from the JUnit tests.
;;

(ns clojure-loc
  (:import java.io.File)
  (:use [clojure.contrib.duck-streams :only (reader)]))

(defn non-blank? [line]
  (if (re-find #"\S" line) true false))

(defn non-svn? [file] 
  (not (.contains (.toString file) ".svn")))

(defn clojure-source? [file]
  (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
  (reduce 
   +
   (for [file (file-seq base-file) 
	 :when (and (clojure-source? file) (non-svn? file))]
     (with-open [rdr (reader file)]
       (count (filter non-blank? (line-seq rdr)))))))

(clojure-loc (File. "./scripts/snake"))
