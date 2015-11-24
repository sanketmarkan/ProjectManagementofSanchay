
sub utf2wx
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];

	system("perl $path/bin/mal/unicode-wx.pl $file > $output");
};
1;
