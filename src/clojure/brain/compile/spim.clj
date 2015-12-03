
(ns brain.compile.spim)

(declare start end ir1->spim)

(defn ir->spim
  "Converts IR to MIPS code for running on SPIM. num-cells is the number of
  cells supported at runtime."
  [ir num-cells]
  (str (start num-cells)
                                   (apply str (map ir1->spim ir))
                                   end))

(defn- start [num-cells]
  (str "\t.data\n"
       "cells: .space " num-cells "\n"
       "\n"
       "\t.text\n"
       "\t.globl main\n"
       "\n"
       "main:\n"
       "\tla $t0, cells\n"))

(def ^:private end (str "\n\tli $v0, 10\n"
                       "\tsyscall\n"))

(defn- ir1->spim [ir]
  (case (:op ir)
    mc (str "\n\tadd $t0, $t0, " (:val ir) "\n")
    mv (str "\n\tlb $t1, 0($t0)\n"
            "\tadd $t1, $t1, " (:val ir) "\n"
            "\tsb $t1, 0($t0)\n")
    read (apply str (repeat (:times ir)
                            (str "\n\tli $v0, 12\n"
                                 "\tsyscall\n"
                                 "\tsb $v0, 0($t0)\n")))
    write (str "\n\tli $v0, 11\n"
               "\tlb $a0, 0($t0)\n"
               (apply str (repeat (:times ir) "\tsyscall\n")))
    loop (str "\nloop" (:id ir) ":\n"
              "\tlb $t1, 0($t0)\n"
              "\tbeqz $t1, end" (:id ir) "\n"
              (apply str (map ir1->spim (:body ir)))
              "\n\tj loop" (:id ir) "\n"
              "end" (:id ir) ":\n")))
