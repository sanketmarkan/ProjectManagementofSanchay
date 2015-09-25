if [ -z $1 ]
then
  file=/dev/stdin;
else
  file=$1;
fi

./wx_to_i8.exe <  $file > tmp.is
perl iscii2utf8.pl-new  < tmp.is
rm -f tmp.is

