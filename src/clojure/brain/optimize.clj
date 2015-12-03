
(ns brain.optimize)

(declare remove-beggining-loops reduce-consec-terms)

(defn optimize
  "Does a series of optimizations on IR code."
  [ir]
  (->> ir
       (remove-beggining-loops)
       (reduce-consec-terms 'mc :val)
       (reduce-consec-terms 'mv :val)
       (reduce-consec-terms 'read :times)
       (reduce-consec-terms 'write :times)))

(defn- remove-beggining-loops
  "Loops that appear before any other instruction can be and are safely removed."
  [ir]
  (if (= (:op (first ir)) 'loop)
    (recur (rest ir))
    ir))

(defn- reduce-consec-terms
  "Reduces consecutive term of the same operation in an IR AST. op is the
  operation to reduce, key is the key to its value (ie. :val or :times), and ir
  is a list of IR terms."
  [op key ir]
  (loop [val 0 opt-ir [] ir ir]
    (if (empty? ir)
      (if (zero? val)
        opt-ir
        (conj opt-ir {:op op key val}))
      (let [term (first ir)]
        (condp = (:op term)
          op (recur (+ val (key term)) opt-ir (rest ir))
          'loop (if (zero? val)
                  (recur 0
                         (conj opt-ir (assoc term :body (reduce-consec-terms op key (:body term))))
                         (rest ir))
                  (recur 0
                         (conj opt-ir
                               {:op op key val}
                               (assoc term :body (reduce-consec-terms op key (:body term))))
                         (rest ir)))
          (if (zero? val)
            (recur 0 (conj opt-ir term) (rest ir))
            (recur 0 (conj opt-ir {:op op key val} term) (rest ir))))))))
