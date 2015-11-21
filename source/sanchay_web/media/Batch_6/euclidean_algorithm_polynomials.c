#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#define MAX 50
#define SUCCESS     1
#define NOTSUCCESS  0

struct Polynomial{
	int deg;
	int cof[MAX];
};
typedef struct Polynomial Polynomial;

void init_polynomial(Polynomial* p, int degree){
	p->deg = degree;
	int i;
	for(i=0;i<=degree;i++){
		p->cof[i] = 0;
	}
}

void print_polynomial(Polynomial* p){
	int i;
	for(i=0;i<35;i++)
		printf("-");
	printf("\n");
	printf("Degree : %d\n",p->deg);
	printf("%d",p->cof[0]);
	for(i=1; i <= p->deg; i++){
		printf(" + %dx^%d",p->cof[i],i);
	}
	printf("\n");
	for(i=0;i<35;i++)
		printf("-");
	printf("\n");
}

int polynomial_division(Polynomial *g, Polynomial *h, Polynomial *q, Polynomial *r, int p){
	/*
	 *	

	*/
	int t,k,l;
	k = g->deg + 1;
	l = h->deg + 1;
	t = find_zp_inverse(p,h->cof[l-1]);
	if(t == NOTSUCCESS){
		printf("Not able to find zp inverse of %d\n",h->cof[h->deg]);
		return NOTSUCCESS;
	}
	//printf("inverse is -- %d\n",t);
	init_polynomial(r,l-2);
	for(i=0;i<=k-1;i++){
		r->cof[i] = g->cof[i];
	}
	init_polynomial(q,k-l);
	for(i=k-l;i>=0;i--){
			q->cof[i] = ( t*(r->cof[i+l-1]) )%p;
			for(j=0;j<=l-1;++j){
				r->cof[i+j] = (r->cof[i+j] - ( (q->cof[i]) * (h->cof[j])  )%p ) %p;
			}
	}

	return SUCCESS;

}

int find_zp_inverse(int p,int a){
	int i;
	for(i=0; i<p; i++){
		if( (i*a)%p == 1)return i;
	}
	return NOTSUCCESS;
}


main(){
	int degree,i,*ptr,p,t,k,l,j;
	Polynomial *g = malloc(sizeof(Polynomial));
	Polynomial *h = malloc(sizeof(Polynomial));
	Polynomial *r = malloc(sizeof(Polynomial));
	Polynomial *q = malloc(sizeof(Polynomial));
	printf("Polynomial Division\n");
	printf("Enter p of Zp[X] : ");
	scanf("%d",&p);
	printf("Enter polynomial g belonging to Z%d[X] :\n",p);
	printf("Enter k (degree+1): ");
	scanf("%d",&k);
	//k-1 is the degree
	init_polynomial(g,k-1);
	printf("Enter its coffecients\n");
	for(i=0;i<= g->deg;i++){
		scanf("%d",&g->cof[i]);
	}
	printf("Enter polynomial h belonging to Z%d[X] :\n",p);
	printf("Enter l (degree+1): ");
	scanf("%d",&l);
	//l-1 is the degree
	init_polynomial(h,l-1);
	printf("Enter its coffecients\n");
	for(i=0;i<= h->deg;i++){
		scanf("%d",&h->cof[i]);
	}
	//print_polynomial(g);
	//print_polynomial(h);
	if(polynomial_division(g, h, q, r, p) != SUCCESS){
		printf("Not able to do polynomial division\n");
		exit(1);
	}
	printf("q is:\n");
	print_polynomial(q);
	printf("r is:\n");
	print_polynomial(r);

}
