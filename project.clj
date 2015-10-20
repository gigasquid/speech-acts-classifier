(defproject speech-acts-classifier "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main speech-acts-classifier.repl
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [stanford-talk "0.1.0"]
                 [clj-http "2.0.0"]
                 [hickory "0.5.4"]
                 [nz.ac.waikato.cms.weka/weka-stable "3.6.12"]])
