#include <stdio.h>
#define MAX 1000
main()
{
    int prime[MAX],i,n,j;
    printf("Enter n: ");
    scanf("%d",&n);
    for(i=0;i<n;++i){
        prime[i] = 1;
    }
    prime[0] = 0;
    prime[1] = 0;
    for(i=2;i<n;++i){
        if(prime[i])
            for(j=i*i;j<n;j+=i)
                prime[j] = 0;
    }
    printf("Prime numbers within 1 and %d are: \n",n);
    for(i=0;i<n;++i)
        if(prime[i] == 1)printf("%d\n",i);

}