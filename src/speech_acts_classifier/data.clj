(ns speech-acts-classifier.data
(:require [clj-http.client :as client]
          [hickory.core :as hickory]
          [hickory.select :as s]
          [clojure.string :as string]
          [speech-acts-classifier.parser :as parser]))

;;;; Gathering answers and statemets from answers.com and hand pruning them

(def r (client/get "http://www.answers.com/Q/FAQ/2528-9"))
(def body (:body r))
(def parsed-frag (hickory/parse body))
(def h (hickory/as-hickory parsed-frag))
(-> (s/select (s/class "question") h)
    first
    :content
    first
    :content
    first)
(def questions (mapv (fn [n] (-> n :content first :content first)) (s/select (s/class "question") h)))


;;; print the questions to buffer and then clean them up manually
(map println questions)

(-> (s/select (s/class "answer") h)
    first
    :content
    first
    (string/split #"\. ")
    first)
(def answers (mapv (fn [n] (-> n :content first (string/split #"\. ") first)) (s/select (s/class "answer") h)))

;;; print the questions to buffer and then clean them up manually
(map str answers)

;;;; Creating the arff files

(def question-lines (string/split (slurp "resources/questions-input-1.txt") #"\n"))
(def answer-lines (string/split (slurp "resources/answers-input-1.txt") #"\n"))
(def expressive-lines (string/split (slurp "resources/expressives-input-1.txt") #"\n"))

(def question-lines-test (string/split (slurp "resources/questions-input-2.txt") #"\n"))
(def answer-lines-test (string/split (slurp "resources/answers-input-2.txt") #"\n"))
(def expressive-lines-test (string/split (slurp "resources/expressives-input-2.txt") #"\n"))

(def header
"@relation speechacts

@attribute       sen_len            numeric
@attribute       nn_num             numeric
@attribute       end_in_n           numeric
@attribute       begin_v            numeric
@attribute       wh_num             numeric
@attribute       type               {assertion,question,expressive}

@data
")

(defn to-arff-data [data type]
  (let [data-lines (map parser/gen-stats data)
        out-lines (map (fn [l]
                         (str (:sen-len l) ","
                              (:nn-num l)  ","
                              (:end-in-n l) ","
                              (:begin-v l)  ","
                              (:wh-num l) ","
                              type "\n")) data-lines)]
   (apply str out-lines)))


;;; This just gens one consolidated training file of all the data
;; but you could split them up and not use the test files
(defn gen-arff-input-all [fname]
  (spit fname (str header
        (to-arff-data question-lines "question")
        (to-arff-data question-lines-test "question")
        (to-arff-data answer-lines "assertion")
        (to-arff-data answer-lines-test "assertion")
        (to-arff-data expressive-lines "expressive")
        (to-arff-data expressive-lines-test "expressive"))))

(gen-arff-input-all "resources/speech-acts-input-all.arff")

(defn gen-arff-input [fname]
  (spit fname (str header
        (to-arff-data question-lines "question")
        (to-arff-data answer-lines "assertion")
        (to-arff-data expressive-lines "expressive"))))

(gen-arff-input "resources/speech-acts-input.arff")

(defn gen-arff-tests [fname]
  (spit fname (str header
        (to-arff-data question-lines-test "question")
        (to-arff-data answer-lines-test "assertion")
        (to-arff-data expressive-lines-test "expressive"))))

(gen-arff-tests "resources/speech-acts-tests.arff")
