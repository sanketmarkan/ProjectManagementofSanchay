### This program takes hindi-SSF file in wx notation and runs vibhaktier on it.
### Usage 

#!/usr/bin/python

import sys,os
ifile=open(sys.argv[1])
ofile=open(sys.argv[2],"w")
ofile.close()
start=0
for line in ifile:
#	line=line.strip()
	if line[:9]=="<Sentence":
		line=line.replace("'","\"")
		start=1
		senid=line.strip().split('=')[1].rstrip(">").strip('"')
		print senid
		tfile=open("/tmp/vibhaktiinput.tmp","w")
		tfile.write(line)
	elif line[:10]=="</Sentence":
		start=0
		tfile.write(line)
		tfile.close()
		os.system("sh vibhakticomputation_run.sh /tmp/vibhaktiinput.tmp /tmp/vibhaktioutput.tmp")
		os.system("sed -i 's/<Sentence id=\"1\"/<Sentence id=\""+senid+"\"/g' /tmp/vibhaktioutput.tmp")
		os.system("cat /tmp/vibhaktioutput.tmp >> "+sys.argv[2])
#		os.system("rm -rf /tmp/vibhaktiinput.tmp /tmp/vibhaktioutput.tmp")
	elif start==1:
		tfile.write(line)
	elif start==0:
		ofile=open(sys.argv[2],"a")
		ofile.write(line)
		ofile.close()
