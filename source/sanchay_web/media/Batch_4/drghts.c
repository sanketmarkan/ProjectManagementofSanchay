#include<stdio.h>

typedef struct adjnode{
	int nbr;
	struct adjnode *next;
}adjnode;

typedef struct graphnode{
	int value;
	struct adjnode* ptr;
}graphnode;

void add_edge(int a,int b){
	struct adjnode* j;
	j = graph[a].ptr;
	if()*
	while(j != NULL){
		j = j->next;
	}
	j = malloc(sizeof(adjnode));
	j->nbr = b;
	j->next = NULL;
}

graphnode graph[50000];
main(){
	int n,m,a,b;
	scanf("%d %d",&n,&m);
	for(i=0;i<n;++i){
		graph[i].ptr = NULL;
	}
	for(i=0;i<n;++i){
		scanf("%d",&graph[i].value); 
	}
	for(i=0;i<m;++i){
		scanf("%d %d",&a,&b);
	}
	add_edge(a,b);
	add_edge(b,a);
}

