(ns speech-acts-classifier.parser
  (:require [stanford-talk.parser :as p]))

;;; Only gens-stats for the first sentence right now
(defn gen-stats [line]
  (let [annotated-line (p/process-text line)
        token-data (-> (group-by :sent-num (:token-data annotated-line)) first val)]
    {:orig line
     :token-data token-data
     :sen-len (count token-data)
     :nn-num (count (filter (fn [t]
                              (contains? #{"NN" "NNS" "NNP" "NNPS"} (:pos t)))
                            token-data))
     :end-in-n (if (contains? #{"NN" "NNS" "NNP" "NNPS" "JJ" "JJR" "JJS"} (:pos (last token-data)))
                 1
                 0)
     :begin-v (if (contains? #{"VB" "VBD" "VBG" "VBN" "VBP" "VPZ"}
                             (:pos (first token-data)))
                1
                0)
     :wh-num (count (filter (fn [t]
                              (contains? #{"WDT" "WRB" "WP" "WP$"}
                                         (:pos t)))
                            token-data))}))
