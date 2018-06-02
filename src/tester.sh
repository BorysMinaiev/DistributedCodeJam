#!/bin/bash

javac Gen.java

for i in `seq 1 100`; do
	echo "TEST $i"
	java Gen > test.in
	/home/borys/dcj/dcj.sh test --source Main.java --library sandwich.java --nodes 1 --output all > out.1
	/home/borys/dcj/dcj.sh test --source Main.java --library sandwich.java --nodes 5 --output all > out.3
	# /home/borys/dcj/dcj.sh test --source Main.java --library sandwich.java --nodes 10 --output all > out.10
	cmp out.1 out.3 
	if [[ $? != 0 ]]; then 
		echo "out.1 is not equal to out.3"
		exit 123; 
	fi
	echo "answer:"
	cat out.1
	# cmp out.1 out.10
	# if [[ $? != 0 ]]; then 
	# 	echo "out.1 is not equal to out.10"
	# 	exit 312; 
	# fi
done