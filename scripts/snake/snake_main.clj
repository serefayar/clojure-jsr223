;;
;; The Snake game, as published by Stuart Halloway in Programming Clojure.
;;
;; Main program for ATO Compilation.

(ns snake.snake-main
  (:gen-class)
  (:use snake.snake-lib))

(defn -main
  "Runs the game from the main program."
  [& args]
  (game))
