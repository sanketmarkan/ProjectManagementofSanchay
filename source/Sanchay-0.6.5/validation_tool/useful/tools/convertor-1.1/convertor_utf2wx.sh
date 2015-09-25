perl run-convertor.pl --path=$PWD --lang=$1 -stype $2 -s utf -t wx < $3 > $4
sed -i 's/@//g' $4
