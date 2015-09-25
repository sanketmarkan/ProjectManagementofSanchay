perl run-convertor.pl --path=$PWD --lang=$1 -stype $2 -s wx -t utf < $3 > /tmp/temp.wx
perl bin/hin/wx-unicode.pl < /tmp/temp.wx > $4
