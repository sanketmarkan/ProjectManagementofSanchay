
sub wx2utf
{	
	$path=@_[0];
	$file=@_[1];
	$output=@_[2];

	system("$path/bin/urd/r2u.out < $file > jnk");
	system("$path/bin/urd/uniconv -decode ucs-2 -encode utf-8 -in jnk -out $output");
	system("rm -f jnk");
};
1;
