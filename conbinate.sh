#!/bin/sh
echo "hello"
echo "now excuting java LogTest"
for((var=1;var<=30;var++))
do
	if [ ! -f log.${var} ];then
		echo "log.${var} not exist!"
		echo "进入下一次循环"
		continue
	fi
	echo "log.${var} exist!"
	echo "调用LogTest!"
	java LogTest log.${var}
done
echo "finish LogTest"
