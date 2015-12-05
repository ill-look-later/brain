(ns brain.ir-test
  (:require [brain [parse :as parse]])
  (:use clojure.test
        brain.ir))

(deftest ir-test
  (testing "Converting individual AST nodes to IR"
    (is (= (ast->ir ['shift-left]) [{:op 'mc :val -1}]))
    (is (= (ast->ir ['shift-right]) [{:op 'mc :val 1}]))
    (is (= (ast->ir ['dec]) [{:op 'mv :val -1}]))
    (is (= (ast->ir ['inc]) [{:op 'mv :val 1}]))
    (is (= (ast->ir ['read]) [{:op 'read :times 1}]))
    (is (= (ast->ir ['write]) [{:op 'write :times 1}]))
    (is (= (ast->ir [[]]) [{:op 'loop :id 0 :body []}])))
  (testing "Converting complex AST to IR"
    (is (= (ast->ir (parse/parse "+[>+<-]>.,."))
           [{:op 'mv :val 1}
            {:op 'loop :id 0
             :body [{:op 'mc :val 1}
                    {:op 'mv :val 1}
                    {:op 'mc :val -1}
                    {:op 'mv :val -1}]}
            {:op 'mc :val 1}
            {:op 'write :times 1}
            {:op 'read :times 1}
            {:op 'write :times 1}]))))
