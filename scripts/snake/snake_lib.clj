;;
;; The Snake game, as published by Stuart Halloway in Programming Clojure.
;;
;; Additions:
;; - You lose the game if the thead touches the borders.
;; - The snake grows by file points incrementally, in as many moves.
;;

(ns snake.snake-lib
  (:gen-class)
  (:import (java.awt Color Dimension)
           (java.awt.event ActionListener KeyListener)
           (javax.swing JPanel JFrame Timer JOptionPane ImageIcon))
  (:use clojure.contrib.import-static
        [clojure.contrib.seq-utils :only (includes?)]))
(import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

;; Game constants

(def width 50)
(def height 30)
(def point-size 10)
(def turn-millis 75)
(def win-length 75)
(def dirs { VK_LEFT  [-1  0]
            VK_RIGHT [ 1  0]
            VK_UP    [ 0 -1]
            VK_DOWN  [ 0  1]})

;;
;; Functions for the part of the model that is immutable.
;;

(defn add-points
  "Adds the points listed in the optional argument. A point is a two-number vector. 
   The map calls + with elements from each argument in order produce a vector 
   whose elements are the totals for each position."
  [& points]
  (vec (apply map + points)))

(defn point-to-screen-rect
  "Translates from points in the game to points on the screen.
   A point in the game is a rect 'point-size' pixels by side, placed at 
   coordinates x, y times 'point-size' on the screen."
  [point]
  (map #(* point-size %)
    [(point 0) (point 1) 1 1]))

(defn create-apple
  "Creates a new apple that the snake eats."
  []
  {:location [(rand-int width) (rand-int height)]
   :color (Color. 210 50 90)
   :type :apple})

(defn create-snake
  "Creates a snake."
  []
  {:body (list [1 1])
   :dir [1 0]
   :type :snake
   :color (Color. 15 160 70)})

(defn move
  "Moves a snake."
  [{:keys [body dir] :as snake} grow-count]
  (assoc snake :body (cons (add-points (first body) dir) 
                           (if (pos? @grow-count) body (butlast body)))))

(defn win?
  "Test if the snake has won the game."
  [{body :body}]
  (>= (count body) win-length))

(defn lose?
  "The game is lost if the head touches its body or is out of bounds."
  [w h {[head & body] :body}]
  (let [x (first head)
        y (last head)]
    (or 
      (includes? body head)
      (= x -1)
      (= y -1)
      (> x w)
      (> y h))))
      

(defn eats?
  "Test if the snake is eating an apple."
  [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

(defn turn
  "Make the snake turn."
  [snake newdir]
  (assoc snake :dir newdir))

;;
;; Functions for mutable state
;;

(defn reset-game
  "Resets the game."
  [snake apple]
  (dosync (ref-set apple (create-apple))
          (ref-set snake (create-snake)))
  nil)

(defn update-direction
  "This function wraps 'turn' inside a transaction."
  [snake newdir]
  (when newdir (dosync (alter snake turn newdir))))

(defn update-positions 
  "Update the positions in the snake depending of whether it's eaten
   or it just moved. When it eats another apple is created."
  [snake apple grow-count]
  (dosync
    (if (eats? @snake @apple)
      (do 
        (ref-set apple (create-apple))
        (alter grow-count + 5)
        (alter snake move grow-count))
      (do
        (if (pos? @grow-count) (alter grow-count dec))
        (alter snake move grow-count))))
  nil)

;;
;; GUI functions
;;

(defn fill-point
  "Fills a single point."
  [g pt color]
  (let [[x y width height] (point-to-screen-rect pt)]
    (.setColor g color)
    (.fillRect g x y width height)))

;
; Multimethod for painting snakes and apples.
;
(defmulti paint 
  "Paints an apple or a snake, depending on the argument's :type value."
  (fn [g object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))

(defn game-panel
  "Creates the playing field."
  [frame snake apple grow-count]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint g @snake)
      (paint g @apple))
    (actionPerformed [e]
      (update-positions snake apple grow-count)
      (when (lose? width height @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You lose!"))
      (when (win? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You win!"))
      (.repaint this))
    (keyPressed [e]
      (update-direction snake (dirs (.getKeyCode e))))
    (getPreferredSize []
      (Dimension. (* (inc width) point-size)
                  (* (inc height) point-size)))
    (keyReleased [e])
    (keyTyped [e])))

;; Provides a well-known class for getting resources from this namespace.
(gen-class
  :name snake.Resource)

(defn class-available? [name]
  (try
    (Class/forName name) true
    (catch ClassNotFoundException _ false)))

(defn get-image
  "Reads an image as a jar resource or a regular file."
  []
  (.getImage
    (ImageIcon.
      (if (class-available? "snake.Resource")
        (.getResource (Class/forName "snake.Resource") "app.gif")
        "snake/app.gif"))))

(defn game
  "Creates the snake game."
  []
  (let [snake (ref (create-snake))
        apple (ref (create-apple))
        grow-count (ref 0)        
        frame (JFrame. "Snake")
        panel (game-panel frame snake apple grow-count)
        timer (Timer. turn-millis panel)
        image (get-image)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.setIconImage image)
      (.pack)
      (.setVisible true)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.start timer)
    [snake apple timer]))
