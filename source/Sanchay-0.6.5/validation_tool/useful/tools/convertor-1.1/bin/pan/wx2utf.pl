
sub wx2utf
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];

	system("perl $path/bin/pan/convert_wu.pl $file > $output");
};
1;
