%{
/*urdu to roman*/
%}
%%
\x06\x22	printf("%c",65);/* alif - a*/
\x06\x27	printf("%c",97);/*alif mad - A*/
\x06\x28	printf("%c",98);/*beh - b*/
\x06\x7E	printf("%c",112);/*peh - p*/
\x06\x2A        printf("%c",116);/*teh - t*/
\x06\x79        printf("%c",84);/*tteh - T*/
\x06\x2B        printf("%c",88);/*se - X*/
\x06\x2C        printf("%c",106);/*jeem - j*/
\x06\x86        printf("%c",99);/*cheem - c*/
\x06\x2D	printf("%c",72);/*hah - H*/
\x06\x2E        printf("%c",75);/*khah - K*/
\x06\x2F        printf("%c",100);/*dal- d*/
\x06\x88        printf("%c",68);/*ddal - D*/
\x06\x30        printf("%c",122);/*jhal - z*/
\x06\x31        printf("%c",114);/*reh -r*/
\x06\x91        printf("%c",82);/*rreh - R*/
\x06\x98        printf("%c",90);/*zain - Z*/
\x06\x32        printf("%c",74);/*jeh - J*/
\x06\x33        printf("%c",115);/*seen - s*/
\x06\x34        printf("%c",83);/*sheen - S*/
\x06\x35        printf("%c",120);/*suad - x*/
\x06\x36        printf("%c%c",122,86);/*jvad - zV*/
\x06\x37        printf("%c",119);/*tah - w*/
\x06\x38        printf("%c%c",122,79);/*zah - zO*/
\x06\x39        printf("%c",69);/*ain - E*/
\x06\x3A        printf("%c",71);/*ghain - G*/
\x06\x41        printf("%c",102);/*feh - f*/
\x06\x42        printf("%c",113);/*qaf - q*/
\x06\xA9        printf("%c",107);/*kaf - k*/
\x06\x43        printf("%c",107);/*kaf - k*/
\x06\xAF        printf("%c",103);/*gaf - g*/
\x06\x44        printf("%c",108);/*lam - l*/
\x06\x45        printf("%c",109);/*meem - m*/
\x06\x46        printf("%c",110);/*noon - n*/
\x06\xBA        printf("%c",78);/*noo gunna - N*/
\x06\x48        printf("%c",118);/*waw- v*/
\x06\x21        printf("%c",111);/*hamza - o*/
\x06\xCC        printf("%c",121);/*choti ye - y*/
\x06\xD2        printf("%c",89);/*badi ye - Y*/
\x06\x24        printf("%c",87);/*waw hamza - W*/
\x06\x26        printf("%c",105);/*choti - i*/
\x06\xD3        printf("%c",101);/*badi ye hamza - e*/
\x06\xC1        printf("%c",104);/*hai - h*/
\x06\x47        printf("%c",104);/*hai - h*/
\x06\xBE        printf("%c",56);/*do chashmi - 8*/
\x06\x50        printf("%c",51);/*jair - 3*/
\x06\x4E        printf("%c",49);/*jabar - 1*/
\x06\x4F        printf("%c",52);/*pesh - 4*/
\x06\x52        printf("%c",48);/*jajam - 0(zero)*/
\x06\x51        printf("%c",50);/*tashdeed - 2*/
\x06\x4B        printf("%c",53);/*tanveen - 5*/
\x06\x4D        printf("%c",54);/*jashtanveen - 6*/
\x06\x4C        printf("%c",55);/*do pesh - 7*/
\x06\x70        printf("%c",57);/*khada jabar - 9*/
\x06\x57        printf("%c",85);/*ulta pesh  - U*/
\x06\x56        printf("%c",73);/*khada jair - I*/
\x06\xD4	printf("%c",46);/*fullstop*/
\n		printf("%c",yytext[0]);
\_		printf("%c",95);
.		{} //printf("ZZZ%cZZZ",yytext[0]);
%%
main(){
yylex();
}
