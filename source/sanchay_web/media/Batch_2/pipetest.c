// Author -- Ics231 Tas 
// About -- using pipe to send data from parent to child
// general libs to invoke sys calls 
#include<stdio.h> 
#include<stdlib.h> 
#include<unistd.h>
#include<sys/types.h>
#include<sys/ptrace.h>
#include<sys/stat.h> 
#include<signal.h>

int main(void)
{
	int     n;
	int     fd[2];
	pid_t   pid;
	char    line[100];
	if (pipe(fd) < 0)
		perror("pipe error\n");
	if ((pid = fork()) < 0) {
		perror("fork error\n");
	} else if (pid==0) {       /* parent */
		close(fd[0]);
		dup2(fd[1],STDIN_FILENO);
		execl("/bin/ls","ls",NULL);
	} else {                /* child */
		close(fd[1]);
		n = read(fd[0], line, 100);
		write(STDOUT_FILENO, line, n);
	}
	exit(0);
}
