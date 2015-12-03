
(ns brain.ir)

(declare ast->ir-sub)

(defn ast->ir
  "Converts a BF AST into the intermediate representation."
  [ast]
  (first (ast->ir-sub [] ast 0)))

(defn- ast->ir-sub [ir ast next-id]
  (cond (empty? ast) [ir next-id]
        (vector? (first ast)) (let [[body next-id] (ast->ir-sub [] (first ast) next-id)]
                                (recur (conj ir {:op 'loop :id next-id :body body})
                                       (rest ast)
                                       (inc next-id)))
        :else (recur (conj ir (case (first ast)
                                shift-left {:op 'mc :val -1}
                                shift-right {:op 'mc :val 1}
                                dec {:op 'mv :val -1}
                                inc {:op 'mv :val 1}
                                read {:op 'read :times 1}
                                write {:op 'write :times 1}))
                     (rest ast)
                     next-id)))
