(ns speech-acts-classifier.core
  (:require [speech-acts-classifier.parser :as parser]
            [speech-acts-classifier.weka :as weka]))

(def results (weka/train-classifier "speech-acts-input-all.arff"))
(def predictor (:evaluator results))
(def data (:data results))

(defn classify-text [text]
  (let [stats (parser/gen-stats text)
        features [(:sen-len stats)
                  (:nn-num stats)
                  (:end-in-n stats)
                  (:begin-v stats)
                  (:wh-num stats)]]
    (weka/predict predictor data features)))

(defn respond [text]
  (let [question-mark? (re-find  #"\?$" text)
        type (if question-mark?
               :question
               (classify-text text))]
    (case type
      :question "That is an interesting question."
      :statement "Nice to know."
      :expressive ":)")))

(comment
  (classify-text "I like cheese")
  ;; -> :statement
  (classify-text "How do you make cheese")
  ;; -> :question
  (classify-text "Right on")
  ;; -> :expressive
  )
