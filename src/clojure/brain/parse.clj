
(ns brain.parse)

(declare parse-inst parse-loop)

(defn parse
  "Parses a BF string into an AST form."
  [insts]
  (loop [ast [] insts (filter (into #{} "<>-+,.[]") insts)]
    (if (empty? insts)
      ast
      (let [[term insts] (parse-inst insts)]
        (recur (conj ast term) insts)))))

(defn- parse-inst [insts]
  (case (first insts)
    \[ (parse-loop insts)
    \] (throw (ex-info "Unmatched ] found" {:error 'parse :code insts}))
    [(case (first insts)
       \< 'shift-left
       \> 'shift-right
       \- 'dec
       \+ 'inc
       \, 'read
       \. 'write) (rest insts)]))

(defn- parse-loop [insts]
  (loop [body [] insts (rest insts)]
    (cond (empty? insts) (throw (ex-info "Unmatched [ found" {:error 'parse :code insts}))
          (= (first insts) \]) [body (rest insts)]
          :else (let [[term insts] (parse-inst insts)]
                  (recur (conj body term) insts)))))
