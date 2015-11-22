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

struct Field{
	Polynomial ideal;
	Polynomial elem[50];
	int order;
};
typedef struct Field Field;

void init_polynomial(Polynomial* p, int degree){
	p->deg = degree;
	int i;
	for(i=0;i<=degree;i++){
		p->cof[i] = 0;
	}
}
int rectify_degree(Polynomial* a, int start_idx){
	int i;
	for(i=start_idx; i>=0; i--){
		if(a->cof[i] != 0)
			{a->deg = i;break;}
	}
	return SUCCESS;
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

int compute_units_field(Field *f){
	int i,j,ct=1;
	Polynomial *tmp = malloc(sizeof(Polynomial));
	Polynomial *prod = malloc(sizeof(Polynomial));
	Polynomial *q = malloc(sizeof(Polynomial));
	Polynomial *r = malloc(sizeof(Polynomial));
	for(i=1;i < f->order; i++){
		for(j=i+1;j < f->order; j++){
			polynomial_product(&f->elem[j], &f->elem[i], prod, 2);
			polynomial_division(prod,&f->ideal,q,r,2);
			rectify_degree(r,10);
			//print_polynomial(r);
			if(polynomial_check_unity(r)){
			
				printf("Unit - %d:\n",ct);
				printf("1.) ");
				print_field_element(f,i);
				printf("2.) ");
				print_field_element(f,j);
				ct++;
				if(ct > 3){
					return SUCCESS;
				}

			}

			
		}


	}
	return NOTSUCCESS;
}

int print_field_element(Field* f,int idx){
	int i,j;
	Polynomial *p;
	p = &f->ideal;
	printf("%d",f->elem[idx].cof[0]);
	for(j=1; j <=f->elem[idx].deg; j++){
		printf(" + %dx^%d",f->elem[idx].cof[j],j);
	}
	
	printf(" \t+\t<");
	printf("%d",p->cof[0]);
	for(j=1; j <= p->deg; j++){
		printf(" + %dx^%d",p->cof[j],j);
	}
	printf(">\n");

}

int polynomial_check_unity(Polynomial* a){
	if(a->deg == 0 && a->cof[0] == 1)return 1;
	else return 0;
}

int get_generator_field(Field *f, Polynomial* g){
	int i,j,check[50],flag,k;
	Polynomial *tmp = malloc(sizeof(Polynomial));
	Polynomial *prod = malloc(sizeof(Polynomial));
	Polynomial *q = malloc(sizeof(Polynomial));
	Polynomial *r = malloc(sizeof(Polynomial));
	for(i = 1; i < f->order ; i++){
		copy_polynomial(g,&f->elem[i]);
		for(j=0;j<f->order;j++)
			check[j] = 0;
		check[i] = 1;
		copy_polynomial(tmp,g);
		for(j=2;j<f->order;j++){
			polynomial_product(g,tmp,prod,2);
			polynomial_division(prod,&f->ideal,q,r,2);
			copy_polynomial(tmp,r);

			//printf("** for power - %d\n",j);
			//print_polynomial(tmp);
			for(k=1;k<f->order;k++){
				if(polynomial_cmp(tmp,&f->elem[k]))
					check[k] = 1;
			}
		}
		//Check if all the polynomials have been covered
		flag = 0;
		for(j=1;j < f->order; j++){
			if(check[j] == 0){
				flag =1;
				//printf("Failed for polynomial\n");
				//print_polynomial(&f->elem[i]);
				break;
			}
		}
		if(flag == 0){
			return SUCCESS;
		}
	}
	return NOTSUCCESS;
}

int polynomial_cmp(Polynomial* a, Polynomial* b){
	int i;
	if(a->deg != b->deg)return 0;
	for(i = 0; i < a->deg; i++){
		if(a->cof[i] != b->cof[i])return 0;
	}
	return 1;
}

int copy_polynomial(Polynomial* a, Polynomial* b){
	int i;
	init_polynomial(a,b->deg);
	for(i=0;i<= a->deg; i++){
		a->cof[i] = b->cof[i];
	}
	return SUCCESS;
}

int polynomial_product(Polynomial* a, Polynomial *b, Polynomial *prod, int p){
	int i,max_deg,c,j,jd;
	max_deg = a->deg + b->deg;
	init_polynomial(prod,max_deg);
	for(i=0;i<=max_deg;i++){
		c = 0;
		for(j=0;j<=i;j++){
			jd = i - j;
			if(j <= a->deg && jd <= b->deg)
				c += mod((a->cof[j] * b->cof[jd]) , p);
		}
		prod->cof[i] = mod(c , p);
	}
	// Since the polynomials under consideration belong to Zp[X] ,where p is 
	// a prime number, degree of product will be exactly = to max_deg due to 
	// no zero divisors
	return SUCCESS;
}

int construct_finite_field_deg3(Field *f, Polynomial *a){
	int i,idx[5];
	copy_polynomial(&f->ideal,a);
	i=0;
	for(idx[2] = 0; idx[2] < 2; idx[2]++){
		for(idx[1] = 0; idx[1] < 2; idx[1]++){
			for(idx[0] = 0; idx[0] < 2; idx[0]++){
				f->elem[i].deg = a->deg - 1;
				f->elem[i].cof[0] = idx[0];
				f->elem[i].cof[1] = idx[1];
				f->elem[i].cof[2] = idx[2];
				++i;
			}
		}
	}
	f->order = i;
}

int construct_finite_field_deg5(Field *f, Polynomial *a){
	int i,idx[5];
	copy_polynomial(&f->ideal,a);
	i=0;
	for(idx[4] = 0; idx[4] < 2; idx[4]++){
		for(idx[3] = 0; idx[3] < 2; idx[3]++){
			for(idx[2] = 0; idx[2] < 2; idx[2]++){
				for(idx[1] = 0; idx[1] < 2; idx[1]++){
					for(idx[0] = 0; idx[0] < 2; idx[0]++){
						f->elem[i].deg = a->deg - 1;
						f->elem[i].cof[0] = idx[0];
						f->elem[i].cof[1] = idx[1];
						f->elem[i].cof[2] = idx[2];
						f->elem[i].cof[3] = idx[3];
						f->elem[i].cof[4] = idx[4];
						i++;	
					}
				}
			}
		}
	}
	f->order = i;
}

void print_field(Field *f){
	int i,idx[3],j;
	Polynomial *p;
	p = &f->ideal;
	printf("Finite field is of the form  p = 2, Zp[X]/<");
	printf("%d",p->cof[0]);
	for(i=1; i <= p->deg; i++){
		printf(" + %dx^%d",p->cof[i],i);
	}
	printf(">\n");
	printf("Order of the field = %d\n",f->order);
	printf("{ \n");
	for(i=0;i<f->order;i++){

		/*
		printf("%d",f->elem[i].cof[0]);
		for(j=1; j <=f->elem[i].deg; j++){
			printf(" + %dx^%d",f->elem[i].cof[j],j);
		}
		
		printf("\t+\t<");
		printf("%d",p->cof[0]);
		for(j=1; j <= p->deg; j++){
			printf(" + %dx^%d",p->cof[j],j);
		}
		printf(">\n");
		*/
		print_field_element(f,i);
		

	}
	printf("} \n");
}

int mod(int a,int b){
	int ret = a % b;
	if(ret < 0)
		ret += b;
	return ret;
}

int polynomial_division(Polynomial *g, Polynomial *h, Polynomial *q, Polynomial *r, int p){
	/*	
	 *	Given two polynomials g,h belonging ring of polynomials Zp[X]
	 	Calculates two polynomials q,r st g = hq + r
	 	Memory for all the polynomials must be allocated and freed in the calling method.
	 	This method neither allocates nor frees memory for Polynomials.That is the reponsibility of
	 	the polynomial class.
	 */
	int t,k,l,i,j;
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
			q->cof[i] = mod(t*(r->cof[i+l-1]),p);
			for(j=0;j<=l-1;++j){
				r->cof[i+j] = mod( r->cof[i+j] - ( mod((q->cof[i]) * (h->cof[j]),p) ),p );
			}
	}

	return SUCCESS;

}

int find_zp_inverse(int p,int a){
	int i;
	for(i=0; i<p; i++){
		//printf("i=%d a=%d p=%d\n modulo = %d\n",i,a,p,(i*a)%p);
		if( mod(i*a,p) == 1)return i;
	}
	return NOTSUCCESS;
}

int find_irreducible_polynomial_deg3(Polynomial* a, int p){
	int i,j,k,res,x,flag,c,loop,r;
	int idx[4];
	r = 3;
	init_polynomial(a,r);
	a->cof[r] = 1;
	for(idx[2] = 0; idx[2] < p; idx[2]++){
		for(idx[1] = 0; idx[1] < p; idx[1]++){
			for(idx[0] = 0; idx[0] < p; idx[0]++){

				//Set the coefficients of the polynomials
				a->cof[2] = idx[2];
				a->cof[1] = idx[1];
				a->cof[0] = idx[0];

				//At this point we have got a new polynomial with degree = 3
				// The polynomials of degree 2/3 is irreducible iff no roots exist.
				//Check if a root exists
				res = 0;
				flag = 0;
				c=0;
				//Sets x
				for(i = 0; i< p;i++){
					//Iterates over all coefficients
					for(loop = 0;loop < 3; loop++){
						x = 1;
						for(j = 0;j<loop;j++){
							x = mod(x*i,p);
						}
						c = mod(c + mod(a->cof[loop] * x,p) , p );
					}
					if(c == 0){flag = 1; break;}
				}
				if(flag == 0)return SUCCESS;
			}
		}
	}
	return NOTSUCCESS;
	
}

int find_irreducible_polynomial_deg5(Polynomial* a, int p){
	int idx[5],flag1,flag2,i1,i0;
	Polynomial *deg1 = malloc(sizeof(Polynomial));
	Polynomial *deg2 = malloc(sizeof(Polynomial));
	Polynomial *q = malloc(sizeof(Polynomial));
	Polynomial *r = malloc(sizeof(Polynomial));
	init_polynomial(deg1, 1);
	init_polynomial(deg2, 2);
	init_polynomial(a, 5);
	a->cof[5] = 1;
	deg1->cof[1] = 1;
	deg2->cof[2] = 1;
	for(idx[4] = 0; idx[4] < p; idx[4]++){
		for(idx[3] = 0; idx[3] < p; idx[3]++){
			for(idx[2] = 0; idx[2] < p; idx[2]++){
				for(idx[1] = 0; idx[1] < p; idx[1]++){
					for(idx[0] = 0; idx[0] < p; idx[0]++){

						a->cof[4] = idx[4];
						a->cof[3] = idx[3];
						a->cof[2] = idx[2];
						a->cof[1] = idx[1];
						a->cof[0] = idx[0];

						//print_polynomial(a);

						//At this point we have got a new polynomial with degree 5
						//Any factorization of the above polynomial will have a polynomial
						// of degree 1 or 2
						//A polynomial of degree 5 is irreducible iff no polynomial of degree
						// 1 or 2 is a divisor or it.

						//Check wether any polynomial of degree 1 divides the new polynomial
						//int polynomial_division(Polynomial *g, Polynomial *h, Polynomial *q, Polynomial *r, int p){
						
						flag1=0;
						for(i0 = 0; i0 < p; i0++){
							//printf("Entered\n");
							deg1->cof[0] = i0;
							if(polynomial_division(a,deg1,q,r,p) != SUCCESS){
								printf("Not able to do polynomial divison \n");
								return NOTSUCCESS;
							}
							//printf("Pol r -:\n");
							//print_polynomial(r);
							if(r->deg == 0 && r->cof[0] == 0){
								flag1=1;
								break;
							}
						}

						if(flag1==1)continue;

						
						flag2=0;
						for(i1=0; i1<p; i1++){
							for(i0=0; i0<p ;i0++){
								//printf("Entered 2\n");
								deg2->cof[0] = i0;
								deg2->cof[1] = i1;
								if(polynomial_division(a,deg2,q,r,p) != SUCCESS){
									printf("Not able to do polynomial divison \n");
									return NOTSUCCESS;
								}
								if(r->deg == 0 && r->cof[0] == 0){
									flag2=1;
									break;
								}
							}
						}
						if(flag2==0){

							free(deg1);
							free(deg2);
							free(q);
							free(r);
							return SUCCESS;

						}
						
					}
				}
			}
		}
	}
	free(deg1);
	free(deg2);
	free(q);
	free(r);
	return NOTSUCCESS;	
}


main(){
	int p,r,i;
	Polynomial *a = malloc(sizeof(Polynomial));
	Polynomial *g = malloc(sizeof(Polynomial));
	Field *f3 = malloc(sizeof(Field));
	Field *f5 = malloc(sizeof(Field));
	p = 2; // My roll no. is 201503001 
	for(i=0;i<35;i++)
		printf("#");
	printf("---PART A---");
	for(i=0;i<35;i++)
		printf("#");
	printf("\n");
	printf("Construction of Finite fields\n");
	printf("p = %d for Zp[x]\n",p);
	printf("Constructing finite field of order = %d^3\n",p);
	if(find_irreducible_polynomial_deg3(a,p) != SUCCESS){
		printf("Not able to find find irreducible\n");
	}
	construct_finite_field_deg3(f3,a);
	print_field(f3);
//int construct_finite_field_deg5(Field *f, Polynomial *a){

	printf("Constructing finite field of order = %d^5\n",p);
	if(find_irreducible_polynomial_deg5(a,p) != SUCCESS){
		printf("Not able to find find irreducible\n");
	}
	construct_finite_field_deg5(f5,a);
	print_field(f5);

	for(i=0;i<35;i++)
		printf("#");
	printf("---PART B---");
	for(i=0;i<35;i++)
		printf("#");
	printf("\n");

	printf("Finding the generator of F1\n");
	if(get_generator_field(f3,g) == NOTSUCCESS){
		printf("Generator not found\n");
		exit(1);
	}
	printf("Generator is \n");
	print_polynomial(g);

	for(i=0;i<35;i++)
		printf("#");
	printf("---PART C---");
	for(i=0;i<35;i++)
		printf("#");
	printf("\n");

	printf("Finding the units of F2\n");
	if(compute_units_field(f5) != SUCCESS){
		printf("Not able to calculate units.\n");
		exit(1);
	}


}