#include <stdio.h>
main(){
	int a,b,r,rd,rdd,d;
	printf("Euclidean Algorithm\n");
	printf("Enter a: ");
	scanf("%d",&a);
	printf("Enter b: ");
	scanf("%d",&b);
	r = a;
	rd = b;
	while(rd != 0){
		rdd = r % rd;
		r = rd;
		rd = rdd;
	}
	d = r;
	printf("Gcd(%d,%d) = %d\n",a,b,d);

}