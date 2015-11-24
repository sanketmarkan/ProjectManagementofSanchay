#!C:/perl/bin/perl
while($in=<>)
{
	chomp($in);
	$in1=&convertToTelugu($in);
	print $in1,"\n";
}
sub convertToTelugu
{
local($str)=@_;
%ini = (
'a' => "\xE0\xB0\x85",
'A' => "\xE0\xB0\x86",	
'i' => "\xE0\xB0\x87",
'I' => "\xE0\xB0\x88",
'u' => "\xE0\xB0\x89",
'U' => "\xE0\xB0\x8A",
'q' => "\xE0\xB0\x8B",
'Y' => "\xE0\xB0\x8E",
'e' => "\xE0\xB0\x8F",
'E' => "\xE0\xB0\x90",
'V' => "\xE0\xB0\x92",
'o' => "\xE0\xB0\x93",
'O' => "\xE0\xB0\x94",
);

%tr = (
'z' => "\xE0\xB0\x81",
'M' => "\xE0\xB0\x82",
'H' => "\xE0\xB0\x83",
'a' => "",
'A' => "\xE0\xB0\xBE",
'i' => "\xE0\xB0\xBF",
'I' => "\xE0\xB1\x80",
'u' => "\xE0\xB1\x81",
'U' => "\xE0\xB1\x82",
'q' => "\xE0\xB1\x83",
'Q' => "\xE0\xB1\x84",
'Y' => "\xE0\xB1\x86",
'e' => "\xE0\xB1\x87",
'E' => "\xE0\xB1\x88",
'V' => "\xE0\xB1\x8A",
'o' => "\xE0\xB1\x8B",
'O' => "\xE0\xB1\x8C",
'^' => "\xE0\xB1\x8D",
'k' => "\xE0\xB0\x95",
'g' => "\xE0\xB0\x97",
'f' => "\xE0\xB0\x99",
'c' => "\xE0\xB0\x9A",
'j' => "\xE0\xB0\x9C",
'F' => "\xE0\xB0\x9E",
't' => "\xE0\xB0\x9F",
'd' => "\xE0\xB0\xA1",
'N' => "\xE0\xB0\xA3",
'w' => "\xE0\xB0\xA4",
'x' => "\xE0\xB0\xA6",
'n' => "\xE0\xB0\xA8",
'p' => "\xE0\xB0\xAA",
'b' => "\xE0\xB0\xAC",
'm' => "\xE0\xB0\xAE",
'K' => "\xE0\xB0\x96",
'G' => "\xE0\xB0\x98",
'C' => "\xE0\xB0\x9B",
'J' => "\xE0\xB0\x9D",
'T' => "\xE0\xB0\xA0",
'D' => "\xE0\xB0\xA2",
'W' => "\xE0\xB0\xA5",
'X' => "\xE0\xB0\xA7",
'P' => "\xE0\xB0\xAB",
'B' => "\xE0\xB0\xAD",
'y' => "\xE0\xB0\xAF",
'r' => "\xE0\xB0\xB0",
'3' => "\xE0\xB0\xB1",
'l' => "\xE0\xB0\xB2",
'L' => "\xE0\xB0\xB3",
'v' => "\xE0\xB0\xB5",
'S' => "\xE0\xB0\xB6",
'R' => "\xE0\xB0\xB7",
's' => "\xE0\xB0\xB8",
'h' => "\xE0\xB0\xB9",
);
			$str =~ s/rY/3/g;
			$str =~ s/lY/L/g;
			$str =~ s/kZ/1/g;
			$str =~ s/KZ/2/g;
			$str =~ s/gZ/4/g;
			$str =~ s/jZ/5/g;
			$str =~ s/dZ/6/g;
			$str =~ s/DZ/7/g;
			$str =~ s/PZ/8/g;
			$str =~ s/yz/9/g;
			$str =~ s/eV/Y/g;
			$str =~ s/oV/V/g;
			$str =~s/([3KGCJFTDNWXPBLSRkgfcjtdnwxpbmyrlvsh45]$)/$1\^/g;			
			$str =~ s/([1-9KGCJFTDNWXPBLSRZkgfcjtdnwxpbmyrlvsh])([^AEIOUQYVaeiouq\^])/$1^$2/go;
			$str =~ s/([^1-9KGCJFTDNWXPBLSRZkgfcjtdnwxpbmyrlvsh]|^)([AEIOUQYVaeiouq])/$1$ini{$2}/g;
			$str =~ s/([A-Za-z0-9\^])/$tr{$1}/g;
return($str);
}
1;
