# DistributedCodeJam
Some code for Google DCJ Contest (https://codejam.withgoogle.com/codejam/resources/faq#dcj)

Как заставить работать:
1. Скачать DCJ testing tool for Linux отсюда https://codejam.withgoogle.com/codejam/resources/quickstart-guide#dcj
2. Распаковать, прописать alias в .bashrc
3. В config.json поменять путь:
  "java-include-dirs": [
    "/usr/lib/jvm/java-8-openjdk-amd64/include/"
  ],
4. В /usr/lib/jvm/java-8-openjdk-amd64/include/jni.h поменять include "jni_md.h" на include "linux/jni_md.h"
5. Запустить dcj --source Main.java --library sandwich.java --nodes 10
