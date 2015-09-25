#!/usr/bin/perl

&wx_utf($ARGV[0]);
sub wx_utf
{

$infile =$_[0];


%vowel = qw(
a	2565
A	2566
i	2567
I	2568
u	2569
U	2570
e	2575
E	2576
o	2579
O	2580
);

%cons = qw(
z	2562
k	2581
K	2582
g	2583
G	2584
f	2585
c	2586
C	2587
j	2588
J	2589
F	2590
t	2591
T	2592
d	2593
D	2594
N	2595
w	2596
W	2597
x	2598
X	2599
n	2600
p	2602
P	2603
b	2604
B	2605
m	2606
y	2607
r	2608
l	2610
lZ	2611
v	2613
S	2614
s	2616
h	2617
KZ	2649
gZ	2650
jZ	2651
dZ	2652
PZ	2654
Y	2673	
M	2672
);
%join_vowel =qw(
a	2621
A	2622
i	2623
I	2624
u	2625
U	2626
e	2631
E	2632
o	2635
O	2636
);

$cons="[zkKgGfcCjJFtTdDNwWxXnpPbBmyrlvSshYM]Z?";
$vowel = "[aAiIuUeEoO]";

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
			$line = $';
			print chr($cons{$1});
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
