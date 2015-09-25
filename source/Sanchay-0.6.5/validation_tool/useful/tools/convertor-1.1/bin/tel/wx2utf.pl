
sub wx2utf
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];

	system("$path/bin/hin/wx_to_i8.exe <  $file > tmp.is");
	system("python $path/iscii2utf8.py 7 < tmp.is > $output");
	system("rm -f tmp.is");
};
1;
