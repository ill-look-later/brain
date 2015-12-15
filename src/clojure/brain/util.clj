
(ns brain.util
  (:import java.io.FileOutputStream brain.BytecodeLoader)
  (:use [brain ir optimize parse] [brain.compile jvm spim c]))

(defn write-jvm
  "Write some JVM bytecode to a file."
  [file bytecode]
  (with-open [writer (FileOutputStream. file)]
    (.write writer bytecode)))

(defn bf->jvm
  "Compiles a string of BF code to JVM bytecode."
  [code cls-name num-cells]
  (-> code
      parse
      ast->ir
      optimize
      (ir->jvm cls-name num-cells)))

(defn eval-bf
  "Evaluates a BF code string."
  [code num-cells]
  (BytecodeLoader/callMain
   (BytecodeLoader/loadBytecode
    (bf->jvm code "BFProgram" num-cells))))

(defn bf->spim
  "Compiles a string of BF code to MIPS assembly."
  [code num-cells]
  (-> code
      parse
      ast->ir
      optimize
      (ir->spim num-cells)))

(defn bf->c
  "Compiles a string of BF code to C."
  [code num-cells]
  (-> code
      parse
      ast->ir
      optimize
      (ir->c num-cells)))
