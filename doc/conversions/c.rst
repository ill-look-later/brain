
C Conversion
============

Here's a quick sketch of how IR code should compile to C.
Note that the # is a number.

Start
-----

#include <stdio.h>

| int main() {
|     char cells[30000] = {0};
|     char* ptr = cells;

Conversions
-----------

| {:op mc :val #}
|     ptr += #;
|
| {:op mv :val #}
|     * ptr += #;
|
| {:op read :times #} - Repeat # times.
|     scanf("%c", ptr);
|
| {:op write :times #} - Repeat # times.
|     printf("%c", * ptr);
|
| {:op loop :id # :body [...]}
|     while(* ptr) {
|         ...
|     }

End
---

|     return 0;
| }
