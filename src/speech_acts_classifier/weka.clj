(ns speech-acts-classifier.weka
  (:require [clojure.java.io :as io])
  (:import (weka.core.converters ConverterUtils$DataSource)
           (weka.classifiers Evaluation)
           (weka.classifiers.evaluation EvaluationUtils)
           (weka.classifiers.trees RandomForest)
           (java.util Random)
           (weka.core Instance)))

(def classifier (new RandomForest))

(defn get-datasource [fname]
  (new ConverterUtils$DataSource
       (.getResourceAsStream (clojure.lang.RT/baseLoader) fname)))

(defn train-classifier [fname]
  (let [source (get-datasource fname)
        data (.getDataSet source)
        _ (.setClassIndex data (dec (.numAttributes data)))
        _  (.buildClassifier classifier data)
        e (new Evaluation data)]
    (.crossValidateModel e classifier data (.intValue (int 10)) (new Random 1) (into-array []))
    (println (.toSummaryString e))
    {:evaluator e
     :data data}))

(defn stats [ev]
  (println (.toSummaryString ev)))

(defn gen-instance [dataset [val0 val1 val2 val3 val4]]
  (let [i (new Instance 6)]
    (doto i
      (.setValue 0 (double val0))
      (.setValue 1 (double val1))
      (.setValue 2 (double val2))
      (.setValue 3 (double val3))
      (.setValue 4 (double val4))
      (.setValue 5 (Instance/missingValue))
      (.setDataset dataset))))

(defn predict [ev d vals]
  (let [v  (.evaluateModelOnce ev classifier (gen-instance d vals))]
    (case v
      0.0 :statement
      1.0 :question
      2.0 :expressive)))


(comment
  (def results (train-classifier "speech-acts-input-all.arff"))
  (def predictor (:evaluator results))
  (def data (:data results))

  (stats predictor)
  (predict predictor data [1 1 1 0 0])
;; -> :expressive

)
