#!/usr/bin/perl
&utf_wx($ARGV[0],$ARGV[1]);
sub utf_wx
{

$path=$_[0];
$infile =$_[1];


open(TMP, "$path/bin/pan/map_uw") || die "cant open";

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
		if((ord($chars[$i])>=2561 && ord($chars[$i])<=2676 ) || ord($chars[$i])==2404)
		{
			$current_char = ord($chars[$i]);
			$next_char = ord($chars[$i+1]);

			if(($current_char>=2581 && $current_char<=2617) || ($current_char>=2649 && $current_char<=2654)) 
			{
				if($next_char == 2620) 
				{
					if($current_char == 2616)
					{
						$current_char=2614;
					}
					elsif($current_char == 2582)
					{
						$current_char=2649;
					}
					elsif($current_char == 2583)
					{
						$current_char=2650;
					}
					elsif($current_char == 2588)
					{
						$current_char=2651;
					}
					elsif($current_char == 2603)
					{
						$current_char=2654;
					}
					elsif($current_char == 2610)
					{
						$current_char=2611;
					}
					print $mapp{$current_char};
					$i++;
				}
				elsif($next_char>=2618 && $next_char<=2637)
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
