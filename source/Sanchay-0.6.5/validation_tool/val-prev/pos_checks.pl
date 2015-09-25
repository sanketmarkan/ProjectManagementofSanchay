#!/usr/bin/perl

#use strict;
require "$ARGV[1]/shakti_tree_api.pl";
require "$ARGV[1]/feature_filter.pl";

open(IN, "$ARGV[1]/POS_tags.txt") or die ("File cannot be opened");
@pos_tags=<IN>;

%pos_hash;

foreach $line (@pos_tags)
{
	chomp($line);
	$pos_hash{$line}=1;
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
			&pos_checks($sent);
		}
	}
}

sub pos_checks()
{
	my($i, @leaves, $new_fs, @tree, $line, $string, $file, @lines, @string2, $string_ref1, $string1, $string_name);

	$input = $_[0];
	my $flag=0;
	my %hash_index;
	my %hash_chunk;
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

	foreach $i(@final_tree)
	{
		$string_tree = &get_field($i, 4, $input);
		@leaves = &get_children($i, $input);
		my $count_chunk=0;
		$index_chunk++;

		$string = &get_field($i, 4, $input);
		$string_fs = &read_FS($string, $input);

		my $string_child = &get_field($i, 4, $input);
		my $string_fs_child = &read_FS($string_child, $input);

		my @old_drel = &get_values("drel", $string_fs_child);
		my @old_dmrel = &get_values("dmrel", $string_fs_child);
		my @old_reftype = &get_values("reftype", $string_fs_child);
		my @old_voicetype = &get_values("voicetype", $string_fs_child);
		my @old_stype = &get_values("stype", $string_fs_child);
		my @old_cotype = &get_values("coref", $string_fs_child);
		my @old_mtype = &get_values("mtype", $string_fs_child);
		my @old_vib = &get_values("vib", $string_fs_child);
		my @old_troot = &get_values("troot", $string_fs_child);
		my @old_name = &get_values("name", $string_fs_child);
		#my @old_attr = &get_attributes($string_fs_child);

		if($old_drel[0]=~/\'/ or $old_drel[0]=~/\"/)
		{
			$old_drel[0]=~s/\'//g;
			$old_drel[0]=~s/\"//g;
		}

		if($old_dmrel[0]=~/\'/ or $old_dmrel[0]=~/\"/)
		{
			$old_dmrel[0]=~s/\'//g;
			$old_dmrel[0]=~s/\"//g;
		}

		if($old_voicetype[0]=~/\'/ or $old_voicetype[0]=~/\"/)
		{
			$old_voicetype[0]=~s/\'//g;
			$old_voicetype[0]=~s/\"//g;
		}

		if($old_stype[0]=~/\'/ or $old_stype[0]=~/\"/)
		{
			$old_stype[0]=~s/\'//g;
			$old_stype[0]=~s/\"//g;
		}

		if($old_coref[0]=~/\'/ or $old_coref[0]=~/\"/)
		{
			$old_coref[0]=~s/\'//g;
			$old_coref[0]=~s/\"//g;
		}

		if($old_vib[0]=~/\'/ or $old_vib[0]=~/\"/)
		{
			$old_vib[0]=~s/\'//g;
			$old_vib[0]=~s/\"//g;
		}

		if($old_reftype[0]=~/\'/ or $old_reftype[0]=~/\"/)
		{
			$old_reftype[0]=~s/\'//g;
			$old_reftype[0]=~s/\"//g;
		}

		if($old_mtype[0]=~/\'/ or $old_mtype[0]=~/\"/)
		{
			$old_mtype[0]=~s/\'//g;
			$old_mtype[0]=~s/\"//g;
		}
		if($old_troot[0]=~/\'/ or $old_troot[0]=~/\"/)
		{
			$old_troot[0]=~s/\'//g;
			$old_troot[0]=~s/\"//g;
		}
		if($old_name[0]=~/\'/ or $old_name[0]=~/\"/)
		{
			$old_name[0]=~s/\'//g;
			$old_name[0]=~s/\"//g;
		}

		my @old_drel_name = split(/:/, $old_drel[0]);
		my @old_dmrel_name = split(/:/, $old_dmrel[0]);
		my @old_reftype_name = split(/:/, $old_reftype[0]);
		my @old_coref_name = split(/:/, $old_coref[0]);


		my $flag_wq=0;
		$string_tree = &get_field($i, 4, $input);
		my $chunk_label = &get_field($i, 3, $input);
		my @chunk_part = split(/__/, $chunk_label);
		@leaves = &get_children($i, $input);
		for $ii(@leaves)
		{
			my $string_leaves = &get_field($ii, 3, $input);
			if ($pos_hash{$string_leaves} ne "1")
			{
				$string_pos = &get_field($ii, 4, $input);
				$string_pos_fs = &read_FS($string_pos, $input);

				my @posn_attribute = &get_values("posn", $string_pos_fs);
				my $node_value = &get_field($ii, 2, $input);

				if($posn_attribute[0]=~/\'/ or $posn_attribute[0]=~/\"/)
				{
					$posn_attribute[0]=~s/\'//g;
					$posn_attribute[0]=~s/\"//g;
				}
				if($chunk_part[0] ne "NULL")
				{
					print "$string_leaves\t", "$node_value\t", "$posn_attribute[0]::$count_sent", "::$file_name[-1]\t";
					print "POS Error: Invalid POS tag in $old_name[0]\n";
				}

				elsif($chunk_part[0] eq "NULL" && @chunk_part==2)
				{
					if($pos_hash{$string_leaves} ne "1")
					{
						print "$string_leaves\t", "$node_value\t", "$posn_attribute[0]::$count_sent","::$file_name[-1]\t";
						print "POS Error: Invalid POS tag in $old_name[0]\n";
					}
				}
			}
		}
	}
}
