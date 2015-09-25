#!usr/bin/perl

if(!$ARGV[0] || !$ARGV[1] || !$ARGV[2] || !$ARGV[3])
{
	print "USAGE:\n";
	print "\tperl run_command_on_files_in_dir.pl <command> <redirection=y/n> <in extension> <out extension> <in directory> [<out directory>]\n";
	exit(1);
}
else
{
	$cmd = $ARGV[0];
	$redir = $ARGV[1];
	$iext = $ARGV[2];

	if($iext eq "-none")
	{
		$iext = "";
	}

	$oext = $ARGV[3];

	if($oext eq "-none")
	{
		$oext = "";
	}

	$idir = $ARGV[4];

	if(!$ARGV[5]) { $odir = $idir }
	else { $odir = $ARGV[5] };
}

#opendir(DIR, $idir) or die("Couldn't open directory $idir\n");

@files = <$idir/*>;

#print join(" ", @files)."\n";

#while($file = readdir(DIR))
foreach $file(@files)
{
	if(!(-f $file))
	{ next; }

	@parts = split(/\//, $file);
	$file = @parts[@parts - 1];

	if($file eq "." || $file eq "..") { print "NA"; }
	elsif($iext eq "" || $file =~ /\.$iext/)
	{
		$ofile = $file.".".$oext;
		$ofile = $odir."\/".$ofile;
		$file = $idir."\/".$file;

		$actualcmd = "";
		if($redir =~ /^y$/i)
		{
			$actualcmd = "$cmd < $file > $ofile";
			print "Executing command: $actualcmd"."\n\n";
		}
		else
		{
			$actualcmd = "$cmd $file $ofile";
			print "Executing command: $actualcmd"."\n\n";
		}
		
		system($actualcmd);
	}
}

closedir(DIR);
