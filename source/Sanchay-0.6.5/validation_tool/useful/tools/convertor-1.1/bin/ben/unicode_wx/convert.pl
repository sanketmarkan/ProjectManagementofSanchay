#!/usr/bin/perl
&utf_wx($ARGV[0],$ARGV[1]);
sub utf_wx
{

$path=$_[0];
$infile =$_[1];


open(TMP, "$path/bin/ben/unicode_wx/map_uw") || die "cant open";

while(<TMP>)
{
	chomp $_;
	($key, $val) = split(/\t/,$_);
	$mapp{$key} = $val;
}

close TMP;

open(F, "<:utf8", "$infile");

while(<F>) {
	binmode ( STDOUT, ":utf8" );
	@chars = split(//,$_);
	
	for($i=0; $i<=$#chars; $i++)
	{
		if((ord($chars[$i])>=2432 && ord($chars[$i])<=2559 ) || ord($chars[$i])==2404)
		{
			$current_char = ord($chars[$i]);
			$next_char = ord($chars[$i+1]);
			if(($current_char>=2453 && $current_char<=2489) || ($current_char>=2524 && $current_char<=2527)) 
			{
				if($next_char>=2490 && $next_char<=2509)
				{
					print $mapp{$current_char};
				}
				else
				{
					print $mapp{$current_char},"a";
				}
			}
			elsif( $current_char == 2404 )
			{
				print ".";
			}
			else
			{
				print $mapp{$current_char};
			}

		}
		else
		{
			print $chars[$i];
		}
	}
		

}

close(F);
}
1;
