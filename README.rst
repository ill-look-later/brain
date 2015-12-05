=====
Brain
=====

Brain is a clojure library for compiling brainfuck (BF) code. It can also
evaluate BF at runtime by loading the compiled JVM code at runtime, kinda like
clojure.

Special Notes
=============

* For the JVM compiled code, reading EOF sets the current cell to -1.
