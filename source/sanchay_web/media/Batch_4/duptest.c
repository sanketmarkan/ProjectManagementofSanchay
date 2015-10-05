#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <signal.h>
#include <wait.h>

int main(){
	int f = open("test", O_RDONLY | O_WRONLY | O_CREAT, S_IRWXU);
    dup2(f, 1);
    printf("Writing to stdout\n");
	close(f);
	return 0;
}

