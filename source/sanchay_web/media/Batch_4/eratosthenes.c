#include<stdio.h>

main()
{
	int arr[500];
	for(i=0;i<500;++i)arr[i]=0;
	printf("Enter n");
	p = n;
	while(p<=n){
		if(arr[p]!== 0){
			++p;
			continue;
		}
		for(i=2;i*p<=n;++i){
			arr[i*p]=1;
		}
	}

}