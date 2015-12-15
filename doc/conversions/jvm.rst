
JVM Bytecode
============

start
-----

| .class public BFProgram
| .super java/lang/Object
|
| .method public <init>()V
|    aload_0
|    invokenonvirtual java/lang/Object/<init>()V
|    return
| .end method
|
| .method public static main([Ljava/lang/String;)V
|    ; .limit stack 2
|    ; Create the array
|    sipush 30000
|    newarray char
|    astore_1
|
|    ; Store the array pointer
|    iconst_0
|    istore_2

Conversions
-----------

| {:op mc :val #}
|     iinc 2, #
|
| {:op mv :val #}
|     aload_1
|     iload_2
|     dup2
|     caload
|     sipush #
|     iadd
|     i2c
|     castore
|
| {:op read :times #} - Repeat # times.
|     aload_1
|     iload_2
|     getstatic java/lang/System/in Ljava/io/InputStream;
|     invokevirtual java/io/InputStream/read()I
|     i2c
|     castore
|
| {:op write :times #} - Repeat # times.
|     getstatic java/lang/System/out Ljava/io/PrintStream;
|     aload_1
|     iload_2
|     caload
|     invokevirtual java/io/PrintStream/print(C)V
|
|
| {:op loop :id # :body [...]}
| loop#:
|     aload_1
|     iload_2
|     caload
|     ifeq          end#
|     ...
|     goto          loop#
| end#:

end
---

|    return
| .end method
