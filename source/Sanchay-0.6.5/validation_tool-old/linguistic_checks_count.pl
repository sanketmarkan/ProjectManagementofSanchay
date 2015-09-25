#!/usr/bin/perl

#use strict;

require "$ARGV[1]/shakti_tree_api.pl";
require "$ARGV[1]/feature_filter.pl";

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
			#my $file_string = $file_name[4]."_$j";
			$count_sent++;
#if($file_sent_hash{$file_string} eq "")
#{
	&validate_attributes($sent);
#			}
		}
	}
}

sub get_sequence()
{
	my $input1=$_[1];
	my $chunk_id=$_[0];
#$chunk_id=~s/[0-9]//g;
	my @temp_final_tree;
	my @temp_tree = &get_children(0, $input1);
	my $ssf_string1 = &get_field($temp_tree[0], 3, $input1);
	if($ssf_string1 eq "SSF")
	{
		@temp_final_tree = &get_children(1, $input1);
	}
	else
	{
		@temp_final_tree = @temp_tree;
	}
	my $final_pos_list;
	foreach my $seq(@temp_final_tree)
	{
		$temp_string_tree = &get_field($seq, 4, $input1);
		$temp_string_tree_fs = &read_FS($temp_string_tree, $input1);

		my @chunk_name = &get_values("name", $temp_string_tree_fs);

		if($chunk_name[0]=~/\'/ or $chunk_name[0]=~/\"/)
		{
			$chunk_name[0]=~s/\'//g;
			$chunk_name[0]=~s/\"//g;
		}
		
		my $temp_pos_tag;
		my $temp_lex_value;
		my $temp_pos_list="";

		if($chunk_name[0] eq $chunk_id)
		{
			@leaves = &get_children($seq, $input1);

			my @temp_node_leaves = &get_children($seq, $input1);

			foreach my $ite_temp(@temp_node_leaves)
			{
				my $temp_string_node = &get_field($ite_temp, 4, $input1);
				$temp_string_node_fs = &read_FS($temp_string_node, $input1);
				$temp_pos_tag = &get_field($ite_temp, 3, $input1);
				$temp_lex_value = &get_field($ite_temp, 2, $input1);
				if($temp_pos_list eq "")
				{
					$temp_pos_list = $temp_lex_value."_$temp_pos_tag";
				}
				else
				{
					$temp_pos_list = $temp_pos_list."__$temp_lex_value"."_$temp_pos_tag";
				}
			}
			$final_pos_list=$temp_pos_list;
		}
	}
	return $final_pos_list;
}

sub validate_attributes()
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
	my @ssf_tree = @final_tree;
	foreach $i(@final_tree)
	{
		$string_tree = &get_field($i, 4, $input);
		@leaves = &get_children($i, $input);
		
		my @node_leaves = &get_children($i, $input);

		my $pos_tag;
		my $lex_value;
		my $pos_list="";

		my $count_chunk=0;
		$index_chunk++;
		
		$string = &get_field($i, 4, $input);
		$string_fs = &read_FS($string, $input);

		my $string_child = &get_field($i, 4, $input);
		my $string_fs_child = &read_FS($string_child, $input);
		
		$chunk_label = &get_field($i, 3, $input);

		my @old_name = &get_values("name", $string_fs_child);
		my @old_drel = &get_values("drel", $string_fs_child);
		my @old_dmrel = &get_values("dmrel", $string_fs_child);
		my @old_reftype = &get_values("reftype", $string_fs_child);
		my @old_voicetype = &get_values("voicetype", $string_fs_child);
		my @old_stype = &get_values("stype", $string_fs_child);
		my @old_cotype = &get_values("coref", $string_fs_child);
		my @old_mtype = &get_values("mtype", $string_fs_child);
		my @old_vib = &get_values("vib", $string_fs_child);
		my @old_troot = &get_values("troot", $string_fs_child);
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

		@drel_value = split(/:/, $old_drel[0]);

		if(($old_vib[0] =~/कारण.*/) || ($old_vib[0]=~/वजह.*/))
		{
			if($old_drel[0] !~/rh.*/)
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: Chunks with ke_kAraNa/ke_kAraNa_se or kI_vajaha_se/kI_vajaha should be rh\n";
			}
		}
		
		if(($old_vib[0] =~/बजाय.*/) || ($old_vib[0]=~/खिलाफ.*/))
		{
			if($old_drel[0] !~/vmod.*/)
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: Chunks with ke_bajAya/ke_KilAPa should be vmod\n";
				#print "$old_drel[0]\t", $chunk_label, "::", "$pos_list\t", "$old_name[0]", "_$count_sent", "_$file_name[4]", "\n";
			}
		}
		if(($old_vib[0] =~/मुताबिक.*/) || ($old_vib[0]=~/अनुसार.*/) || ($old_vib[0]=~/तहत.*/))
		{
			if($old_drel[0] !~/k7.*/)
			{
				#print "$old_drel[0]\t", $chunk_label, "::", "$pos_list\t", "$old_name[0]", "_$count_sent", "_$file_name[4]", "\n";
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: Chunks with ke_muwAbika/ke_anusAra should be k7\n";
			}
		}
		my @r6_drel_value = split(/:/, $old_drel[0]);
		if($r6_drel_value[0] eq "r6")
		{
			foreach $ite_r6(@final_tree)
			{
				my $r6_node = &get_field($ite_r6, 4, $input);
				my $string_r6_node = &read_FS($r6_node, $input);
				my @r6_name = &get_values("name", $string_r6_node);

				if($r6_drel_value[1] eq $r6_name[0])
				{
					my @r6_drel = &get_values("drel", $string_r6_node);
					if($r6_drel[0] =~/pof.*/)
					{
						my @parent_name = split(/:/, $old_drel[0]);
						my $parent_list = &get_sequence($parent_name[1], $input);
						my $child_list = &get_sequence($old_name[0], $input);
						my $temp_parent = $parent_name[1];
						$temp_parent=~s/[0-9]//g;
						print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "Dependency Error: pof should have r6-k* attached to it\n";	
					}
				}
			}
		}

		if($r6_drel_value[0]=~/r6-/)
		{
			my @parent_name = split(/:/, $old_drel[0]);
			my $parent_list = &get_sequence($parent_name[1], $input);
			my $child_list = &get_sequence($old_name[0], $input);
			my $temp_parent = $parent_name[1];
			$temp_parent=~s/[0-9]//g;
			foreach $ite_r6(@final_tree)
			{
				my $r6_node = &get_field($ite_r6, 4, $input);
				my $string_r6_node = &read_FS($r6_node, $input);
				my @pof_name = &get_values("name", $string_r6_node);
				if( ($r6_drel_value[1] eq $pof_name[0]) && ($pof_name[0] !~/VG/) )
				{
					my @pof_drel = &get_values("drel", $string_r6_node);
					my $pof_chunk = &get_field($ite_r6, 3, $input);
					my @node_leaves = &get_children($ite_node,  $input);
					foreach $ite_node (@node_leaves)
					{
						$string_node = &get_field($ite_node, 4, $input);
						$string_node_fs = &read_FS($string_node, $input);
						$pos_tag = &get_field($ite_node, 3, $input);
						$lex_value_pof = &get_values("lex", $string_node_fs);
						my $pof_list_pof = "";
						if($pos_list_pof eq "")
						{
							$pos_list_pof = $lex_value_pof."_$pos_tag";
						}
						else
						{
							$pos_list_pof = $pos_list_pof."_$lex_value_pof"."_$pos_tag";
						}
					}

					if($pof_drel[0]!~/pof/)
					{
						#print "$pof_drel[0]\t", $pof_chunk, "::", "$pos_list_pof\t", "$pof_name[0]", "_$count_sent", "_$file_name[4]", "\n";
						print "$pof_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$pof_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "Dependency Error: Parent of r6-k* should be pof\n";	
					}
				}
				elsif( ($r6_drel_value[1] eq $pof_name[0]) && ($pof_name[0] =~/VG/) )
				{
						print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "Dependency Error: r6-k* should be attached to pof instead of the parent of pof\n";
				}
			}
		}

		if($chunk_label eq "NP")
		{
			my @np_node_name;
			my $flag_prp=0;
			my $flag_nn=0;
			my @leaves_np = &get_children($i, $input);
			for $node(@leaves_np)
			{
				my $np_pos_tag = &get_field($node, 3, $input);
				my $np_nodes = &get_field($node, 4, $input);
				my $string_np_nodes = &read_FS($np_nodes, $input);
				my @np_pos_vib = &get_values("vib", $string_np_nodes, $input);
				my @np_pos_lex = &get_values("lex", $string_np_nodes, $input);
				if( ($np_pos_tag eq "PRP") && ( ($np_pos_vib[0] eq "के") || ($np_pos_vib[0] eq "की") || ($np_pos_vib[0] eq "का") )) 
				{
					@np_node_name = &get_values("name", $string_np_nodes, $input);
					$flag_prp=1;
				}
				if($np_pos_tag eq "NN" or $np_pos_tag eq "NNP")
				{
					$flag_nn=1;
				}
			}
			if(@leaves_np > 1)
			{
				if($flag_prp==1 && $flag_nn==1)
				{
					$flag_prp=0;
					$flag_nn=0;
					my $child_list = &get_sequence($old_name[0], $input);
					print "$chunk_label\t", "$child_list\t",  "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
					print "Chunk Error: Chunk needs to be split\n";
				}
			}
		}

		if($chunk_label eq "CCP" || $chunk_label eq "NULL__CCP")
		{
			my $ccp_children=0;
			my $ccp_child_chunk;
			my @children_list;
			my $flag_rh=0;
			
			my @drel_label_ccp=split(/:/, $old_drel[0]);
			if($drel_label_ccp[0] eq "sent-adv")
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: CCP cannot be a sent-adv\n";
			}
			
			my @leaves_ccp = &get_children($i, $input);
			for $node(@leaves_ccp)
			{
				my $ccp_lex = &get_field($node, 2, $input);
				if($ccp_lex eq "क्योंकि")
				{
					if ( ($drel_label_ccp[0] ne "rh") && ($drel_label_ccp[0] ne "") )
					{
						my @parent_name = split(/:/, $old_drel[0]);
						my $parent_list = &get_sequence($parent_name[1], $input);
						my $child_list = &get_sequence($old_name[0], $input);
						my $temp_parent = $parent_name[1];
						$temp_parent=~s/[0-9]//g;
						print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "Dependency Error: \'kyoMki\' conjunct should be an rh\n";
					}
					else
					{
						$flag_rh=1;
					}
				}
			}

			for $ite_ccp(@final_tree)
			{
				my $ccp_child = &get_field($ite_ccp, 4, $input);
				my $string_ccp_child = &read_FS($ccp_child, $input);
				my @ccp_drel = &get_values("drel", $string_ccp_child, $input);
				my @ccp_name = &get_values("name", $string_ccp_child, $input);
				if($ccp_drel[0]=~/\'/ or $ccp_drel[0]=~/\"/)
				{
					$ccp_drel[0]=~s/\'//g;
					$ccp_drel[0]=~s/\"//g;
				}
				my @ccp_drel_name = split(/:/, $ccp_drel[0]);
				if($ccp_drel_name[1] eq $old_name[0] && $ccp_drel_name[0] eq "ccof")
				{
					$ccp_child_chunk = &get_field($ite_ccp, 3, $input);
					if (!(grep {$_ eq $ccp_child_chunk} @children_list))
					{
						$children_list[$ccp_children]=$ccp_child_chunk;
						$ccp_children++;
					}
				}
				if($flag_rh==1)
				{
					if($ccp_drel_name[1] eq $drel_label_ccp[1])
					{
						#print "Sentence id=$count_sent\t\"kyoMki\" conjunct has a child\n";
						$flag_rh=0;
					}
				}
			}
			if(@children_list > 1)
			{
				if (!(grep {$_  =~ /CCP.*/} @children_list))
				{
					my @parent_name = split(/:/, $old_drel[0]);
					my $parent_list = &get_sequence($parent_name[1], $input);
					my $temp_parent = $parent_name[1];
					$temp_parent=~s/[0-9]//g;
					print "\t", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
					print "Dependency Error: CCP should have children of same type\n";
				}
			}
		}

		my @old_drel_name = split(/:/, $old_drel[0]);
		my @old_dmrel_name = split(/:/, $old_dmrel[0]);
		my @old_reftype_name = split(/:/, $old_reftype[0]);
		my @old_coref_name = split(/:/, $old_coref[0]);

		my $flag_declarative;

		if($old_voicetype[0] eq "passive")
		{
			my %passive_tam_hash;
			open(IN, "$ARGV[1]/passive_tams") or die("can't open passive tam list");

			my @passive_lines=<IN>;
			my $lines;

			foreach $lines(@passive_lines)
			{
				chomp($lines);
				my @tam_value=split(/\t/, $lines);
				$passive_tam_hash{$tam_value[0]}=$tam_value[1];
			}

			my @tam = &get_values("vib", $string_fs_child);

			if($passive_tam_hash{$tam[0]} ne "p_1")
			{
				my $chunk_list = &get_sequence($old_name[0], $input);
				my $chunk_tag = $old_name[0];
				$chunk_tag=~s/[0-9]//g;
				print "$old_voicetype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "General Error: voicetype cannot be passive\n";
			}
		}

		if($old_stype[0] eq "imperative" || $old_stype[0] eq "declarative")
		{
			if($old_stype[0] eq "declarative")
			{
				$flag_declarative=1;
			}
			else
			{
				$flag_declarative=0;
			}
			my @tam = &get_values("vib", $string_fs_child);
			if ($tam[0] ne "0")
			{
				if($tam[0] ne "")
				{
					#print "Sentence id=$count_sent\tstype cannot be imperative\n";
				}
			}
			elsif($tam[0] eq "0")
			{
				if($flag_declarative==1)
				{
					my $chunk_list = &get_sequence($old_name[0], $input);
					my $chunk_tag = $old_name[0];
					$chunk_tag=~s/[0-9]//g;
					print "$old_stype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
					print "General Error: stype cannot be declarative\n";
				}
			}
		}

		if($old_stype[0] eq "interrogative" || $old_stype[0] eq "interjective" || $old_stype[0] eq "declarative")
		{
			my $count_interjective=0;
			my $count_wq=0;

			if($old_stype[0] eq "declarative")
			{
				$flag_declarative=1;
			}
			else
			{
				$flag_declarative=0;
			}
			my $flag_wq=0;
			my $flag_interjective=0;
			$string_tree = &get_field($i, 4, $input);
			@leaves = &get_children($i, $input);
			for $ii(@leaves)
			{
				my $string_leaves = &get_field($ii, 2, $input);
				if ($string_leaves eq "\?")
				{
					$flag_wq=1;
					if($flag_declarative==1)
					{
						my $chunk_list = &get_sequence($old_name[0], $input);
						my $chunk_tag = $old_name[0];
						$chunk_tag=~s/[0-9]//g;
						print "$old_stype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "General Error: stype cannot be declarative\n";
					}
					last;
				}
				if ($string_leaves eq "\!")
				{
					$flag_interjective=1;
					if($flag_declarative==1)
					{
						my $chunk_list = &get_sequence($old_name[0], $input);
						my $chunk_tag = $old_name[0];
						$chunk_tag=~s/[0-9]//g;
						print "$old_stype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "General Error: stype cannot be declarative\n";
					}
					last;
				}
			}
			if($flag_wq==0)
			{
				if($old_stype[0] eq "interrogative" && $flag_wq != 1)
				{
					$flag_wq=-1;
					$count_wq++;
					if($count_wq==1)
					{
						my $chunk_list = &get_sequence($old_name[0], $input);
						my $chunk_tag = $old_name[0];
						$chunk_tag=~s/[0-9]//g;
						print "$old_stype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "General Error: stype cannot be interrogative\n";
					}
				}
			}
			if($flag_interjective==0)
			{
				if($old_stype[0] eq "interjective" && $flag_interjective != 1)
				{
					$flag_interjective=-1;
					$count_interjective++;
					if($count_interjective==1)
					{
						my $chunk_list = &get_sequence($old_name[0], $input);
						my $chunk_tag = $old_name[0];
						$chunk_tag=~s/[0-9]//g;
						print "$old_stype[0]\t", "$chunk_tag", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
						print "General Error: stype cannot be interjective\n";
					}
				}
			}
		}

		if((@old_drel_name!=2) && ($old_drel_name[0] ne ""))
		{
			$flag=1;
			#print $old_drel[0],"\n";
		}

		if((@old_dmrel_name==2) && ($old_dmrel_name[0] ne ""))
		{
			$string_pos = &get_field($i, 3, $input);
			@pos_split = split(/__/, $string_pos);
			if(($pos_split[0] ne "NULL") or ($pos_split[1] eq ""))
			{
				my $chunk_list = &get_sequence($old_name[0], $input);
				my $chunk_tag = $old_name[0];
				$chunk_tag=~s/[0-9]//g;
				print "$old_dmrel[0]\t", "$chunk_label", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "General Error: Chunk with a valid dmrel must be NULL__X:\n";
			}
			if($old_mtype[0] eq "")
			{
				my $chunk_list = &get_sequence($old_name[0], $input);
				my $chunk_tag = $old_name[0];
				$chunk_tag=~s/[0-9]//g;
				print "$old_mtype[0]\t", "$chunk_label", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "General Error: mtype value must be present\n";
			}
			if($old_drel[0] ne "")
			{
				my $chunk_list = &get_sequence($old_name[0], $input);
				my $chunk_tag = $old_name[0];
				$chunk_tag=~s/[0-9]//g;
				print "$old_dmrel[0]\t", "$chunk_label", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "General Error: Chunk with a valid dmrel cannot have a drel\n";
			}
		}
		$string_pos = &get_field($i, 3, $input);
		@pos_split = split(/__/, $string_pos);
		if($old_name[0]=~/NULL__VG.*/)
		{
			if($old_troot[0] eq "")
			{
				my $chunk_list = &get_sequence($old_name[0], $input);
				my $chunk_tag = $old_name[0];
				$chunk_tag=~s/[0-9]//g;
				print "$old_troot[0]\t", "$chunk_label", "::$chunk_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "General Error: A NULL verb chunk should have troot\n";
			}
			
		}
		
		if ($old_drel_name[0] eq "ccof")
		{
			if( ($old_drel_name[1]=~/CCP.*/) || ($old_drel_name[1]=~/NULL__CCP.*/) )
			{
				#continue;
			}
			else
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: ccof should be the child of a CCP or a NULL__CCP\n";
			}
		}
		
		if ($old_drel_name[0] eq "nmod")
		{
			if( ($old_drel_name[1]=~/NP.*/) || ($old_drel_name[1]=~/NULL__NP.*/) || ($old_drel_name[1]=~/VGNN.*/) ||  ($old_drel_name[1]=~/CCP.*/) ||  ($old_drel_name[1]=~/NULL__CCP.*/))
			{
				#continue;
			}
			else
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: nmod should be the child of an NP or a VGNN\n";
			}
		}
		if ($old_drel_name[0] eq "vmod")
		{
			if( ($old_drel_name[1]=~/VG.*/) || ($old_drel_name[1]=~/NULL__VG.*/) ||  ($old_drel_name[1]=~/CCP.*/) ||  ($old_drel_name[1]=~/NULL__CCP.*/))
			{
				#continue;
			}
			else
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: vmod should be the child of a VG chunk\n";
			}
		}
		
		my $flag_k1=0;
		if($old_vib[0] =~/0_ने/ || $old_vib[0] eq "ने")
		{

			if( ($old_drel_name[0] ne "k1") && ($old_drel_name[0] ne "ccof"))
			{
				$flag_k1=1;
			}
			if($flag_k1 == 1)
			{
				$flag_k1=0;
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: Chunk with \'ne\' case marker must be k1\n";
			}
		}
		if( ($old_drel_name[0] eq "k1") && ($old_drel_name[1]=~/VGNN.*/) )
		{
			if( ($old_vib[0]!~/.*kA/) && ($old_vib[0]!~/.*kA_xvArA/ || $old_vib[0]!~/.*ke_xvArA/) && ($old_vib[0]!~/.*xvArA/) )
			{
				#print "Sentence id=$count_sent\tk1 for a VGNN should either have kA/ke/kI or ke_xvArA/xvArA as PSP\n";
			}
		}

		if($old_coref_name[0] ne "")
		{
			my $flag_rp=0;
			foreach my $ite (@final_tree)
			{
				my $string_tree = &get_field($ite, 4, $input);
				$pos_np = &get_field($ite, 3, $input);
				@leaves = &get_children($ite, $input);
				if($pos_np eq "NP")
				{
					for $ii(@leaves)
					{
						my $string_leaves = &get_field($ii, 3, $input);
						if ($string_leaves eq "PRP")
						{
							$flag_rp=1;
							last;
							# Check for relative pronouns
						}
					}
					if($flag_rp == 1)
					{
						last;
					}
				}
			}
			if($flag_rp == 0)
			{
				#print "Chunk with coref should have a relative pronoun\n";
			}
		}
		if( ($old_drel[0] =~/k1s.*/ ) || ($old_drel[0] =~/k2s.*/) )
		{
			my @k1s_drel=split(/:/, $old_drel[0]);
			my $count_k1=0;
			my $count_k2=0;

			foreach my $ite_k1s(@final_tree)
			{
				my $string_k1 = &get_field($ite_k1s, 4, $input);
				my $string_k1_fs = &read_FS($string_k1, $input);
				my @k1_drel = &get_values("drel", $string_k1_fs);
				if($k1_drel[0] eq "")
				{
					@k1_drel = &get_values("dmrel", $string_k1_fs);
				}
				my @k1_drel_head = split(/:/, $k1_drel[0]);
				if($old_drel[0] =~/k1s.*/ )
				{
					if( ($k1_drel_head[0] eq "k1") && ($k1s_drel[1] eq $k1_drel_head[1]) )
					{
					}
					else
					{
						$count_k1++;
					}
				}
				if($old_drel[0] =~/k2s.*/)
				{
					if( ($k1_drel_head[0] eq "k2") && ($k1s_drel[1] eq $k1_drel_head[1]) )
					{
					}
					else
					{
						$count_k2++;
					}
				}
			}
			if($count_k1 == @final_tree)
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: k1s cannot be present without a k1 for a verb\n";
			}
			if($count_k2 == @final_tree)
			{
				my @parent_name = split(/:/, $old_drel[0]);
				my $parent_list = &get_sequence($parent_name[1], $input);
				my $child_list = &get_sequence($old_name[0], $input);
				my $temp_parent = $parent_name[1];
				$temp_parent=~s/[0-9]//g;
				print "$old_drel[0]\t", "$chunk_label", "::$child_list", "---", "$temp_parent", "::$parent_list\t", "$old_name[0]", "::$count_sent", "::$file_name[-1]", "\t";
				print "Dependency Error: k2s cannot be present without a k2 for a verb\n";
			}
		}
	}
}
