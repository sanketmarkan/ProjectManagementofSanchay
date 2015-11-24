
sub utf2wx
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];

	system("perl $path/bin/ben/unicode_wx/convert.pl $path $file > $output");

};
1;
