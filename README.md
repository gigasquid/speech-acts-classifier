# speech-acts-classifier

An experiment with parsing natural language and classifying the [speech act](https://en.wikipedia.org/wiki/Speech_act) of the sentence.
This is especially important when a machine is trying to understand the meaning of a sentence in an environment, like a chat session,
where missing punctuation is common.

This project classifies three speech acts: statements, questions, and expressives.  Expressives are speech acts that express a mental state of the speaker.  For example, "Thanks", "Ok", "lol".

The parsing and annotation is done with the [wrapper](https://github.com/gigasquid/stanford-talk) around the [Stanford CoreNLP](http://nlp.stanford.edu/software/corenlp.shtml) library.

The classification uses the [weka](http://www.cs.waikato.ac.nz/ml/weka/) java library.  A random forest model was trained on the following sentence features of the [pos annotations](https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html):

* Sentence length
* Number of nouns in the sentence (NN, NNS, NNP, NNPS)
* If the sentence ends in a noun or adjective (NN, NNS, NNP, NNPS, JJ, JJR, JJS)
* If the sentence begins in a verb (VB, VBD, VBG, VBP, VPZ)
* The count of the wh, (like who, what) markers (WDT, WRB, WP, WP$)

Training data for statements and questions were scraped from answers.com and then cleaned up by hand.  The expressives were hand entered.

* ~ 200 statements
* ~ 200 questions
* ~ 80 expressives

Summary of the Trained Model with cross validation:

```
Correctly Classified Instances         407               85.3249 %
Incorrectly Classified Instances        70               14.6751 %
Kappa statistic                          0.7658
Mean absolute error                      0.1185
Root mean squared error                  0.2665
Relative absolute error                 28.3497 %
Root relative squared error             58.3073 %
Total Number of Instances              477
```

The random forest model was chosen after interactively running the data through different models in weka explorer.

## Usage

There are two main ways to use it.

The first is to use the `classify-text` function in the core.  This will return back a keyword that is either `:question`, `:statement`, or `:expressive`.

```clojure
(ns talk
  (:require [speech-acts-classifier.core :as c]))

(c/classify-text "I like cheese")
;; -> :statement

(c/classify-text "How do you make cheese")
;; -> :question

(c/classify-text "Right on")
;; -> :expressive
```

The second way is even more fun.  It is a super simple chat bot based on your text.
It will do a quick check to see if the text ends with a question mark.  If not, it will run the classifier.


```
lein run
```

```
Hello.  Let's chat.
>> I like cheese
Nice to know.
>> Where do you go to buy your cheese
That is an interesting question.
>> wow
:)
>>
```

## References
[Classifying Sentences as Speech Acts in Message Board Posts](https://www.cs.utah.edu/~riloff/pdfs/emnlp11-speechacts.pdf)
[Automated Speech Act Classification For Online Chat](http://ceur-ws.org/Vol-710/paper22.pdf)
[Student Speech Act Classification Using Machine Learning ](https://www.aaai.org/ocs/index.php/FLAIRS/FLAIRS11/paper/viewFile/2635/3040)

## Further Exploration

* Train data on a subset of [NPS Chat Corpus] (http://faculty.nps.edu/cmartell/NPSChat.htm)
* Experiment with auto-detection of best features from data
* Look at other classification techniques


## License

Copyright © 2015 Carin Meier

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
