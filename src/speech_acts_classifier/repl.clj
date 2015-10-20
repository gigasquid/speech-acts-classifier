(ns speech-acts-classifier.repl
  (:gen-class)
  (:require [speech-acts-classifier.core :as c]))

(defn repl []
  (do
    (print ">> ")
    (flush))
  (let [input (read-line)]
    (if-not (= input "quit")
     (do
       (println (try (c/respond input)
                     (catch Exception e (str "Sorry: " e " - " (.getMessage e)))))
       (recur))
     (do (println "Bye!")
         (System/exit 0)))))

(defn -main [& args]
  (println "Hello.  Let's chat.")
  (flush)
  (repl))
