#!/usr/bin/perl
&utf_wx($ARGV[0],$ARGV[1]);
sub utf_wx
{

$path=$_[0];
$infile =$_[1];


open(TMP, "$path/bin/tel/map_wx") || die "cant open";

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
		if((ord($chars[$i])>=3073 && ord($chars[$i])<=3183 ) || ord($chars[$i])==2404)
		{
			$current_char = ord($chars[$i]);
			$next_char = ord($chars[$i+1]);
			if($current_char>=3093 && $current_char<=3129) 
			{
				if($next_char>=3130 && $next_char<=3150)
				{
					if($next_char == 3149)
					{
						$i++;
					}
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
