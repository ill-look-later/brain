
========
Compiler
========

AST
===

The toplevel node is a vector, and all [] loops are vectors.
The other BF instructions translate as follows.

< -> shift-left
> -> shift-right
- -> dec
+ -> inc
, -> read
. -> write

Example
-------

BF Code
~~~~~~~

<>-+,.[>]

AST
===

[shift-left
 shift-right
 dec
 inc
 read
 write
 [shift-right]]

Intermediate Representation
===========================

The intermediate representation consists of 4 forms. Let # be some number.

{:op mc :val #}              - Modifies the cell pointer by adding # to it.
{:op mv :val #}              - Modifies the current cell value by adding # to it.
{:op read :times #}          - Reads a character value into the current cell # times.
{:op write :times #}         - Writes the character value of the current cell # times.
{:op loop :id # :body [...]} - Loops over its body in the same fashion as BF. # is a unique ID number.

MIPS Conversion
===============

Here's a quick sketch of how IR code should compile to MIPS.
Note that the # is a number.

Start
-----

    .data
cells: .space 30000

    .text
    .globl main

main:
    la $t0, cells

Conversions
-----------

{:op mc :val #}
    add $t0, $t0, #

{:op mv :val #}
    lb $t1, 0($t0)
    add $t1, $t1, #
    sb $t1, 0($t0)

{:op read :times #} - Repeat # times.
    li $v0, 12
    syscall
    sb $v0, 0($t0)

{:op write :times #} - Repeat **only** syscall # times.
    li $v0, 11
    lb $a0, 0($t0)
    syscall

{:op loop :id # :body [...]}
    loop#:
        lb $t1, 0($t0)
        beqz $t1, end#

        ...

        j loop#
    end#:

End
---

    li $v0, 10
    syscall

JVM Bytecode
============

start
-----

.class public BFProgram
.super java/lang/Object

.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

.method public static main([Ljava/lang/String;)V
   ; .limit stack 2
   ; Create the array
   sipush 30000
   newarray char
   astore_1

   ; Store the array pointer
   iconst_0
   istore_2

Conversions
-----------

{:op mc :val #}
    iinc 2, #

{:op mv :val #}
    aload_1
    iload_2
    dup2
    caload
    sipush #
    iadd
    i2c
    castore

{:op read :times #} - Repeat # times.
    aload_1
    iload_2
    getstatic java/lang/System/in Ljava/io/InputStream;
    invokevirtual java/io/InputStream/read()I
    i2c
    castore

{:op write :times #} - Repeat # times.
    getstatic java/lang/System/out Ljava/io/PrintStream;
    aload_1
    iload_2
    caload
    invokevirtual java/io/PrintStream/print(C)V


{:op loop :id # :body [...]}
loop#:
    aload_1
    iload_2
    caload
    ifeq          end#
    ...
    goto          loop#
end#:

end
---

   return
.end method
