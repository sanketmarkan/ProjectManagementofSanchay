
sub utf2wx
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];
	system("perl $path/bin/hin/unicode-wx.pl $file > .tmp.num");
	system("perl $path/bin/hin/utf82iscii.pl-new < .tmp.num > .tmp.iscii");
	system("$path/bin/hin/i8_wx/d8_ra_wp_r.out < .tmp.iscii > $output");
	system("rm -f .tmp.iscii");
	system("rm -f .tmp.num");
};
1;
