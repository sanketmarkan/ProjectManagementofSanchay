#!/usr/bin/perl

use Getopt::Long "GetOptions";
$ENV{"LC_ALL"} = "C";
&GetOptions("help!"=>\$help,'mode=s' => \$mode,
		'path=s'=> \$path,
		'stype=s'=>\$stype,
		'lang=s'=>\$slang,
		's=s' => \$src,
		't=s' => \$tgt,
	   );
print "Unprocessed by Getopt::Long\n" if $ARGV[0];

foreach (@ARGV) {
        print "$_\n";
        exit(0);
}


if($help eq 1)
{
        print "SSF-UTF-WX Convertor  - SSF 1.0\n(17th June 2008 last modified on 17th June 2008)\n\n";
        print "usage : ./run-convertor.pl --path=/home/convertor-1.0 --lang=hin -s utf -t wx \n";
        exit(0);
}

if($path eq "")
{
        print "Please Specify the Path as defined in --help\n";
        exit(0);

}
if($stype eq "")
{
        print "Please Specify the Source Language type in ssf/text as defined in --help\n";
        exit(0);

}
if($slang eq "")
{
        print "Please Specify the Language as defined in --help\n";
        exit(0);

}

if($src eq "")
{
        print "Please Specify the Src Font as defined in --help [utf/wx]\n";
        exit(0);

}
if($tgt eq "")
{
        print "Please Specify the Target Font as defined in --help [utf/wx]\n";
        exit(0);

}

#binmode STDOUT , ":utf8";

my $bin=$path."/bin";
require "$bin/$slang/utf2wx.pl";
require "$bin/$slang/wx2utf.pl";
require "$path/API/feature_filter.pl";

if($src eq "wx" and $tgt eq "utf")
{
	while($line=<STDIN>)
	{
		chomp ($line);
		if(($stype eq "SSF") or ($stype eq "ssf"))
		{
			($num,$lex,$pos,$fs) = split(/\t/,$line);
			if($lex ne "((" and $lex ne "))")
                	{
                       		system("echo $lex > word");
                       		&wx2utf($path,"word","out_word");
                       		open(FILE1,"out_word");
                		$lex_out = <FILE1>;
                       		chomp($lex_out);
                       		if($lex_out eq "")
                       		{
                               		$lex_out = $lex;
                       		}
				system("rm -f word out_word");
                	}
			if($fs ne "")
			{
				@fss = split(/\|/,$fs);
				my $len = @fss;
				@string  = "";
				$newfs = "";
				my $i=0;
				foreach $af (@fss)
				{
                	           	my $FSreference = &read_FS($af,$line);
					my @lex_root = &get_values("lex",$FSreference);
					my @fs_vib = &get_values("vib",$FSreference);
					my @fs_head = &get_values("head",$FSreference);
                                	my @fs_name = &get_values("name",$FSreference);
					foreach $field (@lex_root)
					{
						system("echo $field > word ");
						&wx2utf($path,"word","out_word");
						open(FILE1,"out_word");
						$val_out = <FILE1>;
						chomp($val_out);
						system("rm -f word out_word");
						my @lex_arr=();
						push @lex_arr,$val_out;
						&update_attr_val("lex", \@lex_arr,$FSreference,$af);
						$string[$i]=&make_string($FSreference,$af);
					}
					foreach $field1 (@fs_vib)
                                	{
                                		system("echo $field1 > word ");
	                                        &wx2utf($path,"word","out_word");
        	                                open(FILE1,"out_word");
                	                        $vib_out = <FILE1>;
                        	                chomp($vib_out);
                                	        system("rm -f word out_word");
                                        	my @fs_vib_arr=();
	                                        push @fs_vib_arr,$vib_out;
        	                                &update_attr_val("vib", \@fs_vib_arr,$FSreference,$af);
                	                        $string[$i]=&make_string($FSreference,$af);
                        	        }
					foreach $field (@fs_head)
	                                {
        	                                $flag = 0;
                	                        if($field=~/^\".*\"/)
                        	                {
                                	                $field=~s/\"//g;
                                        	        $flag = 1;
	                                        }
        	                                system("echo $field > word ");
                	                        &wx2utf($path,"word","out_word");
                        	                open(FILE1,"out_word");
                                	        $val_out = <FILE1>;
                                        	chomp($val_out);
	                                        if($flag)
        	                                {
                	                                $val_out = "\"".$val_out."\"";
                        	                }
                                	        system("rm -f word out_word");
                                        	my @head_arr=();
	                                        push @head_arr,$val_out;
        	                                &update_attr_val("head", \@head_arr,$FSreference,$af);
                	                        $string[$i]=&make_string($FSreference,$af);
                        	        }
                                	foreach $field (@fs_name)
	                                {
        	                                $flag = 0;
                	                        if($field=~/^\".*\"/)
                        	                {
                                	                $field=~s/\"//g;
                                        	        $flag = 1;
	                                        }
        	                                system("echo $field > word ");
                	                        &wx2utf($path,"word","out_word");
                        	                open(FILE1,"out_word");
                                	        $val_out = <FILE1>;
                                        	chomp($val_out);
	                                        if($flag)
						{
	                                                $val_out = "\"".$val_out."\"";
        	                                }
                	                        system("rm -f word out_word");
                        	                my @name_arr=();
                                	        push @name_arr,$val_out;
                                        	&update_attr_val("name", \@name_arr,$FSreference,$af);
	                                        $string[$i]=&make_string($FSreference,$af);
        	                        }
					$i++;
				}
				foreach $string (@string)
				{	
					if(--$len)
					{
                        	       		$newfs=$newfs.$string."|";
					}
					else
					{
						$newfs=$newfs.$string;
					}
				}
				delete @string[0..$#string];
				delete @lex_root[0..$#lex_root];
				delete @fss[0..$#fss];
				if($line =~ /\(\(/ or $line =~ /\)\)/)
				{
					($num,$lex,$pos,$fs) = split(/\t/,$line);
					print $num,"\t",$lex,"\t",$pos,"\t",$newfs,"\n";
				}
				else
				{
					print $num,"\t",$lex_out,"\t",$pos,"\t",$newfs,"\n";
				}
			}
			else{
				if($lex ne "((" and $lex ne "))")
				{
					print $num,"\t",$lex_out,"\t",$pos,"\t",$fs,"\n";
				}
				else{
					print $line."\n";
				}
			}
		}
		elsif(($stype eq "TEXT") or ($stype eq "text"))
		{
			@words = split(/\s/,$line);
			$symbol=0;
			$symbol1=0;
                        foreach $lex (@words)
                        {
				if($lex=~/^\@.*/)
                        	{
                                	($sym,$lex)=split(/\@/,$lex);
	                                print "\@";
        	                }
				elsif($lex=~/^\‘.*/)
		                {
                 	       		if($lex=~/^\‘.*\‘/)
                        		{
                        			($lex,$sym)=split(/\‘/,$lex);
                                		$symbol4=1;
                        		}
                        		($sym,$lex)=split(/\‘/,$lex);
                		        print FA "\‘";
                		}
		                elsif($lex=~/^.*\‘/)
                		{
		                        ($lex,$sym)=split(/\‘/,$lex);
                		        $symbol4=1;
                		}
                                elsif($lex=~/^\'.*/)
                                {
                                        if($lex=~/^\'.*\'/)
                                        {
                                                $symbol=1;
                                        }
                                        ($sym,$lex)=split(/\'/,$lex);
                                        print "\'";
                                }
                                elsif($lex=~/^.*\'/)
                                {
                                        ($lex,$sym)=split(/\'/,$lex);
					$symbol=1;
				}
                                elsif($lex=~/^\`.*/)
                                {
                                        if($lex=~/^\`.*\`/)
                                        {
                                                $symbol3=1;
                                        }
                                        ($sym,$lex)=split(/\`/,$lex);
                                        print "\`";
                                }
                                elsif($lex=~/^.*\`/)
                                {
                                        ($lex,$sym)=split(/\`/,$lex);
                                        $symbol3=1;
				}
                                elsif($lex=~/^\(.*/)
                                {
                                	($sym,$lex)=split(/\(/,$lex);
                                        print "\(";
					if($lex=~/^.*\)/)
					{
                                		($lex,$sym)=split(/\)/,$lex);
						$symbol2 =1;
					}
                                }
                                elsif($lex=~/^.*\)/)
                                {
                                	($lex,$sym)=split(/\)/,$lex);
                                        $symbol2=1;
                                }
                                elsif($lex=~/^\".*/)
                                {
					if($lex=~/^\".*\"/)
					{
						$symbol1=1;
					}
                                	($sym,$lex)=split(/\"/,$lex);
                                        print "\"";
                                }
				else{
					if($lex=~/^.*\"/)
					{
                                               	($lex,$sym)=split(/\"/,$lex);
						$symbol1=1;
					}
                                }
                   		system("echo $lex > word");
                       		&wx2utf($path,"word","out_word");
                       		open(FILE1,"out_word");
	                	$lex_out = <FILE1>;
        	               	chomp($lex_out);
                	       	if($lex_out eq "")
                       		{
                              		$lex_out = $lex;
                       		}
				system("rm -f word out_word");
				if(!$symbol and !$symbol1 and !$symbol2 and !$symbol3)
				{
					print $lex_out." ";
				}
				elsif($symbol){
					print $lex_out."\' ";
				}
				elsif($symbol2){
					print $lex_out."\) ";
				}
				elsif($symbol3){
					print $lex_out."\` ";
				}
				elsif($symbol4){
					print $lex_out."\‘ ";
				}
				else{
					print $lex_out."\" ";
				}
				$symbol =0;
				$symbol1 =0;
				$symbol2 =0;
				$symbol3 =0;
			}
			print "\n";
		}
	}

}
elsif($src eq "utf" and $tgt eq "wx")
{
	while($line = <STDIN>)
	{
		chomp ($line);
		if(($stype eq "SSF") or ($stype eq "ssf"))
		{
			($num,$lex,$pos,$fs) = split(/\t/,$line);
#print $lex." hi \n";
		
			if($lex ne "((" and $lex ne "))")
			{
				system("echo $lex > word");
				&utf2wx($path,"word","out_word");
				open(FILE1,"out_word");
				$lex_out = <FILE1>;
				chomp($lex_out);
				if($lex_out eq "")
				{
					$lex_out = $lex;
				}
		#		system("rm -f word out_word");
			}
			if($fs ne "")
			{
				@fss = split(/\|/,$fs);
				my $len = @fss;
				@string  = "";
				$newfs = "";
				my $i=0;
				foreach $af (@fss)
				{
					my $FSreference = &read_FS($af,$line);
					my @lex_root = &get_values("lex",$FSreference);
					my @fs_vib = &get_values("vib",$FSreference);
					my @fs_head = &get_values("head",$FSreference);
	                                my @fs_name = &get_values("name",$FSreference);
					foreach $field (@lex_root)
					{
						system("echo $field > word ");
						&utf2wx($path,"word","out_word");
						open(FILE1,"out_word");
						$val_out = <FILE1>;
						chomp($val_out);
						system("rm -f word out_word");
						my @lex_arr=();
						push @lex_arr,$val_out;
						&update_attr_val("lex", \@lex_arr,$FSreference,$af);
						$string[$i]=&make_string($FSreference,$af);
					}
					foreach $field1 (@fs_vib)
                        	        {
                                	        system("echo $field1 > word ");
                                        	&utf2wx($path,"word","out_word");
	                                        open(FILE1,"out_word");
        	                                $vib_out = <FILE1>;
                	                        chomp($vib_out);
                        	                system("rm -f word out_word");
                                	        my @vib_arr=();
                                        	push @vib_arr,$vib_out;
	                                        &update_attr_val("vib", \@vib_arr,$FSreference,$af);
        	                                $string[$i]=&make_string($FSreference,$af);
                	                }
					foreach $field (@fs_head)
	                                {
        	                                $flag=0;
                	                        if($field=~/^\".*\"/)
                        	                {
                                	                $field=~s/\"//g;
                                        	        $flag = 1;
	                                        }
        	                                system("echo $field > word ");
                	                        &utf2wx($path,"word","out_word");
                        	                open(FILE1,"out_word");
                                	        $head_out = <FILE1>;
                                        	chomp($head_out);
	                                        if($flag)
        	                                {
                	                                $head_out = "\"".$head_out."\"";
                        	                }
                                	        system("rm -f word out_word");
                                        	my @head_arr=();
	                                        push @head_arr,$head_out;
        	                                &update_attr_val("head", \@head_arr,$FSreference,$af);
                	                        $string[$i]=&make_string($FSreference,$af);
                        	        }
                                	foreach $field (@fs_name)
	                                {
        	                                $flag=0;
                	                        if($field=~/^\".*\"/)
                        	                {
                                	                $field=~s/\"//g;
                                        	        $flag = 1;
	                                        }
        	                                system("echo $field > word ");
                	                        &utf2wx($path,"word","out_word");
                        	                open(FILE1,"out_word");
                                	        $name_out = <FILE1>;
                                        	chomp($name_out);
	                                        if($flag)
        	                                {
						         $name_out = "\"".$name_out."\"";
        	                                }
	                                        system("rm -f word out_word");
                	                        my @name_arr=();
                        	                push @name_arr,$name_out;
                                	        &update_attr_val("name", \@name_arr,$FSreference,$af);
                                        	$string[$i]=&make_string($FSreference,$af);
	                                }
					$i++;
	
				}
				foreach $string (@string)
				{
					if(--$len)
					{
						$newfs=$newfs.$string."|";
					}
					else
					{
						$newfs=$newfs.$string;
					}
				}
				delete @string[0..$#string];
				delete @lex_root[0..$#lex_root];
				delete @fss[0..$#fss];
				if($line =~ /\(\(/ or $line =~ /\)\)/)
				{
					($num,$lex,$pos,$fs) = split(/\t/,$line);
					print $num,"\t",$lex,"\t",$pos,"\t",$newfs,"\n";                       
		        	}
				else
				{
					print $num,"\t",$lex_out,"\t",$pos,"\t",$newfs,"\n";
				}
			}
			else{
                        	if($lex ne "((" and $lex ne "))")
	                        {
        	                	print $num,"\t",$lex_out,"\t",$pos,"\t",$fs,"\n";
                	        }
                        	else{
	                                print $line."\n";
        	                }
                	}
		}
		elsif(($stype eq "TEXT") or ($stype eq "text"))
		{
			@words = split(/\s/,$line);
                        $symbol=0;
                        foreach $lex (@words)
                        {
                                if($lex=~/^\@.*/)
                                {
                                        ($sym,$lex)=split(/\@/,$lex);
                                        print "\@";
                                }
                                elsif($lex=~/^\'.*/)
                                {
                                        if($lex=~/^\'.*\'/)
                                        {
                                                $symbol=1;
                                        }
                                        ($sym,$lex)=split(/\'/,$lex);
                                        print "\'";
                                }
                                elsif($lex=~/^.*\'/)
                                {
                                        ($lex,$sym)=split(/\'/,$lex);
                                        $symbol=1;
				}
                                elsif($lex=~/^\`.*/)
                                {
                                        if($lex=~/^\`.*\`/)
                                        {
                                                $symbol3=1;
                                        }
                                        ($sym,$lex)=split(/\`/,$lex);
                                        print "\`";
                                }
                                elsif($lex=~/^.*\`/)
                                {
                                        ($lex,$sym)=split(/\`/,$lex);
                                        $symbol3=1;
				}
                                elsif($lex=~/^\(.*/)
                                {
                                	($sym,$lex)=split(/\(/,$lex);
                                        print "\(";
					if($lex=~/^.*\)/)
					{
                                		($lex,$sym)=split(/\)/,$lex);
						$symbol2 =1;
					}
                                }
                                elsif($lex=~/^.*\)/)
                                {
                                	($lex,$sym)=split(/\)/,$lex);
                                        $symbol2=1;
                                }
                                elsif($lex=~/^\".*/)
                                {
					if($lex=~/^\".*\"/)
					{
						$symbol1=1;
					}
                                	($sym,$lex)=split(/\"/,$lex);
                                        print "\"";
                                }
				else{
					if($lex=~/^.*\"/)
					{
                                               	($lex,$sym)=split(/\"/,$lex);
						$symbol1=1;
					}
                                }
                   		system("echo $lex > word");
                       		&utf2wx($path,"word","out_word");
                       		open(FILE1,"out_word");
                		$lex_out = <FILE1>;
                       		chomp($lex_out);
                       		if($lex_out eq "")
                       		{
                              		$lex_out = $lex;
                       		}
				system("rm -f word out_word");
				if(!$symbol and !$symbol1 and !$symbol2 and !$symbol3)
				{
					print $lex_out." ";
				}
				elsif($symbol){
					print $lex_out."\' ";
				}
				elsif($symbol2){
					print $lex_out."\) ";
				}
				elsif($symbol3){
					print $lex_out."\` ";
				}
				else{
					print $lex_out."\" ";
				}
				$symbol =0;
				$symbol1 =0;
				$symbol2 =0;
				$symbol3 =0;
			}
			print "\n";
		}
        }
}
