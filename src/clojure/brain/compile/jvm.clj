
(ns brain.compile.jvm
  (:import [org.objectweb.asm ClassWriter Label Opcodes])
  (:require [clojure [string :as string]]))

(declare start-class end-class start-main end-main convert-insns convert-insn)

(defn ir->jvm
  "Converts IR to JVM bytecode. cls-name is the name of the output
  class. num-cells is the number of cells supported at runtime."
  [ir cls-name num-cells]
  (let [cw (ClassWriter. 0)]

    (start-class cw cls-name)

    (let [mw (.visitMethod cw (+ Opcodes/ACC_STATIC Opcodes/ACC_PUBLIC)
                           "main" "([Ljava/lang/String;)V" nil nil)]
      (start-main mw num-cells)
      (convert-insns ir mw)
      (end-main mw))

    (end-class cw)

    (.toByteArray cw)))

(defn- start-class [cw cls-name]
  (let [qualified-name (string/replace cls-name #"\." "/")]
    (do (.visit cw Opcodes/V1_5 Opcodes/ACC_PUBLIC qualified-name nil "java/lang/Object" nil)
        (doto (.visitMethod cw Opcodes/ACC_PUBLIC "<init>" "()V" nil nil)
          (.visitVarInsn Opcodes/ALOAD 0)
          (.visitMethodInsn Opcodes/INVOKESPECIAL "java/lang/Object" "<init>" "()V" false)
          (.visitInsn Opcodes/RETURN)
          (.visitMaxs 1 1)
          (.visitEnd)))))

(defn- end-class [cw] (.visitEnd cw))

(defn- start-main [mw num-cells]
  (doto mw
    (.visitIntInsn Opcodes/SIPUSH num-cells)
    (.visitIntInsn Opcodes/NEWARRAY Opcodes/T_CHAR)
    (.visitVarInsn Opcodes/ASTORE 1)
    (.visitInsn Opcodes/ICONST_0)
    (.visitIntInsn Opcodes/ISTORE 2)))

(defn- end-main [mw]
  (doto mw
    (.visitInsn Opcodes/RETURN)
    (.visitMaxs 5 5)
    (.visitEnd)))

(defn- convert-insns [ir mw]
  (if (empty? ir)
    mw
    (let [[insn & insns] ir]
      (do (convert-insn insn mw)
          (recur insns mw)))))


(defmulti ^:private convert-insn
  "Converts a single IR instruction to JVM bytecode."
  (fn [x y] (:op x)))

(defmethod convert-insn 'mc [insn mw] (.visitIincInsn mw 2 (:val insn)))

(defmethod convert-insn 'mv [insn mw]
  (doto mw
    (.visitVarInsn Opcodes/ALOAD 1)
    (.visitVarInsn Opcodes/ILOAD 2)
    (.visitInsn Opcodes/DUP2)
    (.visitInsn Opcodes/CALOAD)
    (.visitIntInsn Opcodes/SIPUSH (:val insn))
    (.visitInsn Opcodes/IADD)
    (.visitInsn Opcodes/I2C)
    (.visitInsn Opcodes/CASTORE)))

(defmethod convert-insn 'read [insn mw]
  (dotimes [i (:times insn)]
    (doto mw
      (.visitVarInsn Opcodes/ALOAD 1)
      (.visitVarInsn Opcodes/ILOAD 2)
      (.visitFieldInsn Opcodes/GETSTATIC "java/lang/System" "in"
                       "Ljava/io/InputStream;")
      (.visitMethodInsn Opcodes/INVOKEVIRTUAL "java/io/InputStream"
                        "read" "()I" false)
      (.visitInsn Opcodes/I2C)
      (.visitInsn Opcodes/CASTORE))))

(defmethod convert-insn 'write [insn mw]
  (dotimes [i (:times insn)]
    (doto mw
      (.visitFieldInsn Opcodes/GETSTATIC "java/lang/System" "out"
                       "Ljava/io/PrintStream;")
      (.visitVarInsn Opcodes/ALOAD 1)
      (.visitVarInsn Opcodes/ILOAD 2)
      (.visitInsn Opcodes/CALOAD)
      (.visitMethodInsn Opcodes/INVOKEVIRTUAL "java/io/PrintStream"
                        "print" "(C)V" false))))

(defmethod convert-insn 'loop [insn mw]
  (let [begin (new Label)
        end (new Label)]
    (doto mw
      (.visitLabel begin)
      (.visitVarInsn Opcodes/ALOAD 1)
      (.visitVarInsn Opcodes/ILOAD 2)
      (.visitInsn Opcodes/CALOAD)
      (.visitJumpInsn Opcodes/IFEQ end))
    (convert-insns (:body insn) mw)
    (doto mw
      (.visitJumpInsn Opcodes/GOTO begin)
      (.visitLabel end))))
