%{
/*roman to urdu*/
%}
%%
a	printf("%c%c",06,39);/* 0622 alif - a*/
A	printf("%c%c",06,34);/*0627 alif mad - A*/
b	printf("%c%c",06,40);/*0628 beh - b*/
p	printf("%c%c",06,126);/*067E peh - p*/
t	printf("%c%c",06,42);/*062A teh - t*/
T	printf("%c%c",06,121);/*0679 tteh - T*/
X	printf("%c%c",06,43);/*062B se - X*/
j	printf("%c%c",06,44);/*062C jeem - j*/
c	printf("%c%c",06,134);/*0686 cheem - c*/
H	printf("%c%c",06,45);/*062D hah - H*/
K	printf("%c%c",06,46);/*062E khah - K*/
d	printf("%c%c",06,47);/*062F dal- d*/
D	printf("%c%c",06,136);/*0688 ddal - D*/
z	printf("%c%c",06,48);/*0630 jhal - z*/
r	printf("%c%c",06,49);/*0631 reh -r*/
R	printf("%c%c",06,145);/*0691 rreh - R*/
Z	printf("%c%c",06,152);/*0698 zain - Z*/
J	printf("%c%c",06,50);/*0632 jeh - J*/
s	printf("%c%c",06,51);/*0633 seen - s*/
S	printf("%c%c",06,52);/*0634 sheen - S*/
x	printf("%c%c",06,53);/*0635 suad - x*/
zV	printf("%c%c",06,54);/*0636 jvad - zV*/
w	printf("%c%c",06,55);/*0637 tah - w*/
zO	printf("%c%c",06,56);/*0638 zah - zO*/
E	printf("%c%c",06,57);/*0639 ain - E*/
G	printf("%c%c",06,58);/*063A ghain - G*/
f	printf("%c%c",06,65);/*0641 feh - f*/
q	printf("%c%c",06,66);/*0642 qaf - q*/
k	printf("%c%c",06,169);/*06A9 keheh - k*/
g	printf("%c%c",06,175);/*06AF gaf - g*/
l	printf("%c%c",06,68);/*0644 lam - l*/
m	printf("%c%c",06,69);/*0645 meem - m*/
n	printf("%c%c",06,70);/*0646 noon - n*/
N	printf("%c%c",06,186);/*06BA noo gunna - N*/
v	printf("%c%c",06,72);/*0648 waw- v*/
o	printf("%c%c",06,33);/*0621 hamza - o*/
y	printf("%c%c",06,204);/*06CC choti ye - y*/
Y	printf("%c%c",06,210);/*06D2 badi ye - Y*/
W	printf("%c%c",06,36);/*0624 waw hamza - W*/
i	printf("%c%c",06,38);/*0626 choti - i*/
e	printf("%c%c",06,211);/*06D3 badi ye hamza - e*/
h	printf("%c%c",06,193);/*06C1 hai - h*/
8	printf("%c%c",06,190);/*06BE do chashmi - 8*/
3	printf("%c%c",06,80);/*0650 jair - 3*/
1	printf("%c%c",06,78);/*064E jabar - 1*/
4	printf("%c%c",06,79);/*064F pesh - 4*/
0	printf("%c%c",06,82);/*0652 jajam - 0(zero)*/
2	printf("%c%c",06,81);/*0651 tashdeed - 2*/
5	printf("%c%c",06,75);/*064B tanveen - 5*/
6	printf("%c%c",06,77);/*064D jashtanveen - 6*/
7	printf("%c%c",06,76);/*064C do pesh - 7*/
9	printf("%c%c",06,112);/*0670 khada jabar - 9*/
U	printf("%c%c",06,87);/*0657 ulta pesh  - U*/
I	printf("%c%c",06,86);/*0656 khada jair - I*/
.	printf("%c%c",06,212);/*fullstop*/
\n	printf("%c%c",00,10);/*printf("%c",yytext[0]);*/
%%
main(){
printf("%c%c",254,255);
yylex();
}
