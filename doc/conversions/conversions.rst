
====================
Compiler Conversions
====================

The following sections document the representations and conversion performed by
the compiler.

.. contents::

AST
===

The toplevel node is a vector, and all [] loops are vectors.
The other BF instructions translate as follows.

| < -> shift-left
| > -> shift-right
| - -> dec
| + -> inc
| , -> read
| . -> write

Example
-------

BF Code
~~~~~~~

<>-+,.[>]

AST
~~~

| [shift-left
|  shift-right
|  dec
|  inc
|  read
|  write
|  [shift-right]]

Intermediate Representation
===========================

The intermediate representation consists of 4 forms. Let # be some number.

| {:op mc :val #}              - Modifies the cell pointer by adding # to it.
| {:op mv :val #}              - Modifies the current cell value by adding # to it.
| {:op read :times #}          - Reads a character value into the current cell # times.
| {:op write :times #}         - Writes the character value of the current cell # times.
| {:op loop :id # :body [...]} - Loops over its body in the same fashion as BF. # is a unique ID number.

.. include:: jvm.rst
.. include:: mips.rst
.. include:: c.rst
