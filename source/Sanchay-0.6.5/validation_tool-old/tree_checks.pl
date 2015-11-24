#!/usr/bin/perl

#use strict;

require "$ARGV[1]/shakti_tree_api.pl";
require "$ARGV[1]/feature_filter.pl";

my @drel_set=("k1", "k2", "k3", "k4", "k4a", "k5", "k1s", "k2s", "rh", "rt", "pof", "ras-neg", "ras-k1");
my %drel_hash;
foreach $ite (@drel_set)
{
	$drel_hash{$ite}="1";
}

@file_name=split(/\//, $ARGV[0]);

&read_story("$ARGV[0]");
$numBody = &get_bodycount();
$count_sent=0;
$flag=0;
for(my($bodyNum)=1;$bodyNum<=$numBody;$bodyNum++)
{
	$body = &get_body($bodyNum, $body);
	my($numPara) = &get_paracount($body);
	for(my($ii)=1;$ii<=$numPara;$ii++)
	{
		my($para);
		$para = &get_para($ii);
		my($numSent) = &get_sentcount($para);   
		for(my($j)=1;$j<=$numSent;$j++)
		{
			my($sent) = &get_sent($para, $j);
			$count_sent++;
			&check_tree($sent);
		}
	}
}

sub check_tree()
{
	my($i, @leaves, $new_fs, @tree, $line, $string, $file, @lines, @string2, $string_ref1, $string1, $string_name);
	my %mult_hash;

	$input = $_[0];
	my $flag=0;
	my %hash_index;
	my @final_tree;
	my @tree = &get_children(0, $input);
	my $ssf_string = &get_field($tree[0], 3, $input);
	if($ssf_string eq "SSF")
	{
		@final_tree = &get_children(1, $input);
	}
	else
	{
		@final_tree = @tree;
	}
	my $k, $index=0, $count=0, $index_chunk=0;
	@tree = &get_children($s, $input);
	$count_no_drel=0;

	foreach $i(@final_tree)
	{
		$string_tree = &get_field($i, 4, $input);
		@leaves = &get_children($i, $input);
		$index_chunk++;

		$string = &get_field($i, 4, $input);
		$string_fs = &read_FS($string, $input);

		my $string_child = &get_field($i, 4, $input);
		my $chunk_label = &get_field($i, 3, $input);
		my @chunk_part = split(/__/, $chunk_label);
		my $string_fs_child = &read_FS($string_child, $input);
		my @drel = &get_values("drel", $string_fs_child);
		my @dmrel = &get_values("dmrel", $string_fs_child);
		my @name = &get_values("name", $string_fs_child);

		if($drel[0] eq "")
		{
			if($dmrel[0] eq "")
			{
				$count_no_drel++;
			}
		}
		if($drel[0] ne "")
		{
			if($mult_hash{$drel[0]} eq "")
			{
				$mult_hash{$drel[0]}=1;
			}
			elsif($mult_hash{$drel[0]} ne "")
			{
				$mult_hash{$drel[0]}++;
			}
		}
		my $node;
	}
	if($count_no_drel>=2)
	{
		print "\t", "\t", "::", $count_sent, "::", $file_name[-1], "\t";
		print "General Error: Invalid tree (Hanging nodes)\n";
	}
	foreach my $key (keys %mult_hash)
	{
		my @key_drel = split(/:/, $key);
		if($mult_hash{$key}>=2)
		{
			if($drel_hash{$key_drel[0]} eq "1")
			{
				@err_drel = split(/:/, $key);
				$err_drel[1]=~s/[0-9]//g;
				print "$err_drel[1]\t", "\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "General Error: Same parent has $mult_hash{$key} children which are $err_drel[0]\n";
			}
		}
	}
}
