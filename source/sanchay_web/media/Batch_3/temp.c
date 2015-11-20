#include <stdio.h>
#include <math.h>
main(){
	int a,b,old_r,r,d,old_s,s,old_t,t,rdd,quotient;
	printf("Extended Euclidean Algorithm\n");
	printf("Enter a: ");
	scanf("%d",&a);
	printf("Enter b: ");
	scanf("%d",&b);
	s = 0;
	t = 1;
	r = b;
	old_s = 1;
	old_t = 0;
	old_r = a;
	while(r != 0){
		quotient = floor((double)old_r/r);
		old_r = r;
		r = old_r - quotient*r;
		old_s = s;
		s = old_s - quotient*s;
		old_t = t;
		t = old_t - t*quotient;
				
	}
	d = old_r;
	printf("Gcd(%d,%d) = %d such that %d.%d + %d.%d = %d\n",a,b,d,a,old_s,b,old_t,d);
}