#Urdu to Roman
./uniconv -decode utf-8 -encode ucs-2-be -in jnk2 -out jnk3
./u2r.out <jnk3 >jnk4

#Roman to Urdu

./r2u.out <jnk3 >jnk4
./uniconv -decode ucs-2 -encode utf-8 -in jnk2 -out jnk3

#To generate out files
flex UrdutoRoamn.flex
gcc -g -o u2r.out lex.yy.c -lfl
