#!/usr/bin/perl

&wx_utf($ARGV[0]);
sub wx_utf
{

$infile =$_[0];


%vowel = qw(
H	2947
a	2949
A	2950
i	2951
I	2952
u	2953
U	2954
eV	2958
e	2959
E	2960
oV	2962
o	2963
O	2964
);

%cons = qw(
k	2965
f	2969
c	2970
j	2972
F	2974
t	2975
N	2979
w	2980
n	2984
nY	2985
p	2986
m	2990
y	2991
r	2992
rY	2993
l	2994
lY	2995
lYY	2996
v	2997
R	2999
S	3000
h	3001
);
%join_vowel =qw(
A	3006
i	3007
I	3008
u	3009
U	3010
eV	3014
e	3015
E	3016
oV	3018
o	3019
O	3020
);

$cons="[kfcjFtNwnpmyrlvRSh]Y?Y?";
$vowel = "[HaAiIuUeEoO]V?";

open(F, "<:utf8", "$infile");
binmode ( STDOUT, ":utf8" );

while($line=<F>) {
	@chars=();
	$flag = 0;
	while(1)
	{
		if($line=~/^($vowel)/ and $flag==0)
		{
			print chr($vowel{$1});
			$line = $';
		}
		elsif($line=~/^($vowel)/ and $flag==1)
		{
			print chr($join_vowel{$1}) if($1 ne "a");
			$line = $';
			$flag=0;
		}
		elsif($line=~/^($cons)/)
		{
			$flag = 1;
			print chr($cons{$1});
			$line = $';
			if($line!~/^$vowel/)
			{
				print chr(3021);
			}
		}
		elsif($line=~/^(.)/)
		{
			$flag=0;
			print $1;
			$line = $';
		}
		elsif(length($line)>0)
		{
			$flag=0;
			@chars = split(//,$line);
			print $chars[0];
			shift @chars;
			$line = join("",@chars);
		}
		else
		{
			last;
		}
	}
}

close(F);
}
1;
