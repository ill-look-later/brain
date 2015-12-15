
(ns brain.compile.c)

(declare main ir1->c)

(defn ir->c
  "Converts IR to C code for running code natively. num-cells is the number of
  cells supported at runtime."
  [ir num-cells]
  (main num-cells (apply str (map ir1->c ir))))

(defn- main [num-cells body]
  (str "#include <stdio.h>\n"
       "\n"
       "int main() {\n"
       "\tchar cells[" num-cells "] = {0};\n"
       "\tchar* ptr = cells;\n"
       body
       "\n\treturn 0;\n"
       "}"))

(defmulti ^:private ir1->c :op)

(defmethod ir1->c 'mc [ir]
  (str "\n\tptr += " (:val ir) ";\n"))

(defmethod ir1->c 'mv [ir]
  (str "\n\t*ptr += " (:val ir) ";\n"))

(defmethod ir1->c 'read [ir]
  (apply str (repeat (:times ir)
                     "\n\tscanf(\"%c\", ptr);\n")))
(defmethod ir1->c 'write [ir]
  (apply str (repeat (:times ir) "\n\tprintf(\"%c\", *ptr);\n")))

(defmethod ir1->c 'loop [ir]
  (str "\n\twhile(*ptr) {\n"
       (apply str (map ir1->c (:body ir)))
       "\t}\n"))
