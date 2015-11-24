#!/usr/bin/perl

#use strict;

require "$ARGV[1]/shakti_tree_api.pl";
require "$ARGV[1]/feature_filter.pl";

my @chunk=("NP","VGF", "VGNN", "VGNF", "VGINF", "JJP", "CCP", "RBP", "FRAGP", "NEGP", "BLK");
my %chunk_hash;
foreach $ite (@chunk)
{
	$chunk_hash{$ite}="1";
}

my @np_pos_tags=("NN", "NNP", "NST", "PRP", "DEM", "WQ", "QF", "QC");
my %np_hash;
foreach $ite (@np_pos_tags)
{
	$np_hash{$ite}="1";
}

my @punctuation=("\.","\?", "\!", "\,", "\;", "\:", "\-", "\(", "\)", "\'", "\"");
my %punctuation_hash;
foreach $ite (@punctuation)
{
	$punctuation_hash{$ite}="1";
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
			&check_chunk($sent);
		}
	}
}

sub check_chunk()
{
	my($i, @leaves, $new_fs, @tree, $line, $string, $file, @lines, @string2, $string_ref1, $string1, $string_name);

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
		my @name = &get_values("name", $string_fs_child);
		my @tam = &get_values("vib", $string_fs_child);

		@node_leaves = &get_children($i, $input);

		my $pos_tag;
		my $lex_value;
		my $pos_list="";

		foreach $ite_node (@node_leaves)
		{
			$string_node = &get_field($ite_node, 4, $input);
			$string_node_fs = &read_FS($string_node, $input);
			$pos_tag = &get_field($ite_node, 3, $input);
			$lex_value = &get_field($ite_node, 2, $input);
			if($pos_list eq "")
			{
				$pos_list = $lex_value."_$pos_tag";
			}
			else
			{
				$pos_list = $pos_list."__$lex_value"."_$pos_tag";
			}
		}

		if( ($tam[0] eq "कर") || ($tam[0] eq "ता_हो+या") )
		{
			if ($chunk_label ne "VGNF")
			{
				print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "Chunk Error: Chunk with kara/wA_ho+yA TAM should be VGNF\n";
			}
		}
		
		if($tam[0] eq "ना")
		{
			if ($chunk_label ne "VGNN")
			{
				#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
				print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "Chunk Error: Chunk with nA TAM should be VGNN\n";
			}
		}
		my $flag_psp=0;
		if($chunk_label=~/VG/)
		{
			my @node_vg = &get_children($i, $input);
			foreach $ite_vg (@node_vg)
			{
				$string_vg = &get_field($ite_vg, 3, $input);
				if($string_vg eq "PSP")
				{
					$flag_psp=1;
				}
			}
			if (($chunk_label ne "VGNN") && ($flag_psp==1))
			{
				$flag_psp=0;
				print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "Chunk Error: Verb chunk with a PSP should be VGNN\n";
			}
		}
		if(@chunk_part == 1)
		{
			if($chunk_hash{$chunk_part[0]} ne "1")
			{
				#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
				print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "Chunk Error: Invalid chunk label\n";
			}
		}
		elsif ( (@chunk_part == 2) && ($chunk_part[0] eq "NULL") )
		{
			if($chunk_hash{$chunk_part[1]} ne "1")
			{
				#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
				print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
				print "Chunk Error: Invalid chunk label\n";
			}
		}
		elsif(@chunk_part > 2)
		{
			#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
			print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
			print "Chunk Error: Invalid chunk label\n";
		}
		my $node;
		my $np_pos_count=-1;
		my $vg_pos_count=-1;
		my $jjp_pos_count=-1;
		my $ccp_pos_count=-1;
		my $rbp_pos_count=-1;
		my $negp_pos_count=-1;
		my $temp_count=0;
		foreach $node(@leaves)
		{
			my $chunk_node = &get_field($node, 3, $input);
			my $pos_tag = &get_field($node, 3, $input);
			if($pos_tag eq "CC")
			{
				if($chunk_label ne "CCP")
				{
					if($chunk_label ne "NULL__CCP")
					{
						#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
						print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
						print "Chunk Error: Only CCP/NULL__CCP can have a CC\n";
					}
				}
			}
			if ($chunk_label eq "NP")
			{
				if($np_pos_count==-1)
				{
					$np_pos_count=0;
				}
				if($np_pos_count==0)
				{
					if($np_hash{$pos_tag} eq "1")
					{
						$np_pos_count++;
					}
				}
			}
			if($chunk_label eq "VGF" || $chunk_label eq "VGNF" || $chunk_label eq "VGNN" || $chunk_label eq "VGINF")
			{
				if($vg_pos_count==-1)
				{
					$vg_pos_count=0;
				}
				if($vg_pos_count==0)
				{
					if($pos_tag eq "VM")
					{
						$vg_pos_count++;
					}
				}
				if($chunk_label eq "VGF")
				{
					if($pos_tag eq "PSP")
					{
						#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
						print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
						print "Chunk Error: VGF cannot have a PSP\n";
					}
				}
			}
			if ($chunk_label eq "JJP")
			{
				if($jjp_pos_count==-1)
				{
					$jjp_pos_count=0;
				}
				if($jjp_pos_count==0)
				{
					if($jjp_pos_count==0)
					{
						if($pos_tag eq "JJ" || $pos_tag eq "QF")
						{
							$jjp_pos_count++;
						}
					}
				}

			}
			if($chunk_label eq "CCP")
			{
				if($ccp_pos_count==-1)
				{
					$ccp_pos_count=0;
				}
				if($ccp_pos_count==0)
				{
					if($pos_tag eq "CC" || $pos_tag eq "SYM")
					{
						$ccp_pos_count++;
					}
				}
			}
			if($chunk_label eq "RBP")
			{
				if($rbp_pos_count==-1)
				{
					$rbp_pos_count=0;
				}
				if($rbp_pos_count==0)
				{
					if($pos_tag eq "RB" || $pos_tag eq "WQ")
					{
						$rbp_pos_count++;
					}
				}
			}
			if($chunk_label eq "NEGP")
			{
				if($negp_pos_count==-1)
				{
					$negp_pos_count=0;
				}
				if($negp_pos_count==0)
				{
					if($pos_tag eq "NEG")
					{
						$negp_pos_count++;
					}
				}
			}
		}
		if( ($np_pos_count==0) || ($vg_pos_count==0) || ($jjp_pos_count==0) || ($ccp_pos_count==0) || ($rbp_pos_count==0) || ($negp_pos_count==0))
		{
			#print "chunk__", $name[0], "__", $count_sent, "__", $file_name[2], "\n";
			print "$chunk_label\t", "$pos_list\t", $name[0], "::", $count_sent, "::", $file_name[-1], "\t";
			print "Chunk Error: Invalid POS tags for this particular chunk\n";
		}		
	}
}
