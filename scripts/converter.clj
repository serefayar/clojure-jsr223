;;
;; This is the sample program from the Clojure site.
;;

(ns converter
  (:gen-class)
  (:import (javax.swing JFrame JLabel JTextField JButton UIManager)
           (java.awt.event ActionListener)
           (java.awt GridLayout)))

(defn celsius []
  (let [frame (JFrame. "Celsius Converter")
        temp-text (JTextField.)
        celsius-label (JLabel. "Celsius")
        convert-button (JButton. "Convert")
        fahrenheit-label (JLabel. "Fahrenheit")]
    (.addActionListener convert-button
      (proxy [ActionListener] []
        (actionPerformed [evt]
          (let [c (Double/parseDouble (.getText temp-text))]
            (.setText fahrenheit-label
               (str (+ 32 (* 1.8 c)) " Fahrenheit"))))))
    (UIManager/setLookAndFeel (UIManager/getSystemLookAndFeelClassName))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add temp-text)
      (.add celsius-label)
      (.add convert-button)
      (.add fahrenheit-label)
      (.setSize 300 80)
      (.setVisible true))))

(defn -main
  [& args]
  (celsius))
