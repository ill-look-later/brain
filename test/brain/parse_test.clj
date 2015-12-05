(ns brain.parse-test
  (:use clojure.test
        brain.parse))

(deftest parse-test
  (testing "Parsing individual commands"
    (is (= (parse "<") ['shift-left]))
    (is (= (parse ">") ['shift-right]))
    (is (= (parse "-") ['dec]))
    (is (= (parse "+") ['inc]))
    (is (= (parse ",") ['read]))
    (is (= (parse ".") ['write]))
    (is (= (parse "[]") [[]])))
  (testing "Parsing complex expression"
    (is (= (parse "+[>+<-]>.,.")
           ['inc
            ['shift-right
             'inc
             'shift-left
             'dec]
            'shift-right
            'write
            'read
            'write]))))
