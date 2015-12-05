(defproject brain "0.1.0"
  :description "A library for compiling brainfuck code."
  :license {:name ""
            :url ""}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.ow2.asm/asm "5.0.3"]]

 :source-paths ["src/clojure"]
 :java-source-paths ["src/java"]
 :aot :all)
