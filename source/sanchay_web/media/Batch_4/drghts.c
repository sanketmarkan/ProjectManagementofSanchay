// Shell Assignment ,IIIT-H, OS
// Author - Akshat Tandon 201503001

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/utsname.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/types.h>
#include <termios.h>
#include <errno.h>
#include <sys/stat.h>
#include <fcntl.h>

#define MAXSTRINGLENGTH 1024
#define MAXDIRLENGTH    2000
#define MAXARGS			20
#define MAXBUILTINS		9
#define TRUE 			1
#define FALSE 			0
#define SUCCESS 		0
#define NOTSUCCESS 		-1
#define EMPTYLINE 		-5
#define MAX_PIPES 		10
#define MAX_BG_PROCS	25

/* Buffer to store the parsed command */
struct CommandBuffer{
	char name[MAXSTRINGLENGTH];
	char *cargv[MAXARGS];
	char input_file[MAXSTRINGLENGTH];   // Name of file for input redirection
	char output_file[MAXSTRINGLENGTH];  // Name of file for output redirection
	int append_output_file;
	int num_cargv;
	int builtin;
	int background;
};
typedef struct CommandBuffer CommandBuffer;

struct BgProcess{
	char name[MAXSTRINGLENGTH];
	int bg_pid;
	int valid;
}bg_table[MAX_BG_PROCS];
typedef struct BgProcess BgProcess;

//Whenever you add a builtin modify the constant MAXBUILTINS
struct BuiltIn{
	char name[MAXSTRINGLENGTH];
	char id;
}listBuiltIn[] = {"cd",0,"pwd",1,"echo",2,"quit",3,"pinfo",4,"jobs",5,"kjob",6,"overkill",7,"fg",8};
typedef struct BuiltIn BuiltIn;


void initialize_bg_table(){
	//printf("Called\n");
	int i;
	for(i = 0; i < MAX_BG_PROCS; i++){
		bg_table[i].valid = FALSE;
	}
}

int add_bg_table(CommandBuffer *buf, int pid){
	int i;
	for(i = MAX_BG_PROCS - 1; i>=0; i--){
		if(bg_table[i].valid == TRUE ){
			if(i == MAX_BG_PROCS - 1)return NOTSUCCESS;
			strcpy(bg_table[i + 1].name,buf->name);
			bg_table[i + 1].bg_pid = pid;
			bg_table[i + 1].valid = TRUE;
			return SUCCESS;
		}
	}
	
	// list is empty , add at 0
	strcpy(bg_table[0].name,buf->name);
	bg_table[0].bg_pid = pid;
	bg_table[0].valid = TRUE;
	//printf(" Value stored in bg table - %s %d %d\n",bg_table[0].name,bg_table[0].bg_pid,bg_table[0].valid);
	//remove_bg_table(0);
	return SUCCESS;
}

int remove_bg_table(int pid){
	//printf("Entered remove bg , proc id %d\n",pid);
	//printf(" Value stored in bg table at 0 - %s %d %d\n",bg_table[0].name,bg_table[0].bg_pid,bg_table[0].valid);
	int i,proc_id = -1;
	for(i = 0; i < MAX_BG_PROCS; i++){
		//printf(" At index %d - %s %d %d\n",i,bg_table[i].name,bg_table[i].bg_pid,bg_table[i].valid);
		if(bg_table[i].bg_pid == pid && bg_table[i].valid == TRUE){
			proc_id = i;
			break;
		}
	}
	//printf("Proc id found is %d\n",proc_id);
	if(proc_id == -1)return NOTSUCCESS;

	if(proc_id >= 0 && proc_id < MAX_BG_PROCS && bg_table[proc_id].valid == TRUE){
		//printf("Bg process removed\n");
		bg_table[proc_id].valid = FALSE;
		return SUCCESS;
	}
	else return NOTSUCCESS;
}

int get_processid(int jobid){

	if(jobid>=0 && jobid < MAX_BG_PROCS &&  bg_table[jobid].valid == TRUE)
		return bg_table[jobid].bg_pid;
	else return -1;
}


/* Clear the buffer of its previous contents */
void initialize_CommandBuffer(CommandBuffer *buf){
	strcpy(buf->name,"");
	int index;
	for(index = 0; index < MAXARGS; ++index){
		buf->cargv[index] = NULL;
	}
	strcpy(buf->input_file,"");
	strcpy(buf->output_file,"");
	buf->num_cargv = 1;
	buf->builtin = -1;
	buf->append_output_file = FALSE;
	buf->background = FALSE;
}

/* Free the space occupied by buffer arguments */
void free_buffer(CommandBuffer *buf)
{
	int index = 0;
	for(index = 0; index < MAXARGS; index++){
		free(buf->cargv[index]);
	}
}
int count_pipes(char cmdline[]){
	int length = strlen(cmdline), index, num_pipes = 0;
	for(index = 0; index < length; index++){
		if(cmdline[index] == '|')num_pipes += 1;
	}
	return num_pipes;
}

/* Dsiplays the shell prompt on the terminal */
int display_prompt(struct utsname* userptr, char current_directory[], char home_directory[]){
	char current_dir_symbol[MAXDIRLENGTH];
	char hostname[MAXSTRINGLENGTH];

	if(strcmp(home_directory,current_directory) == 0){
		strcpy(current_dir_symbol,"~");
	}
	else{
		strcpy(current_dir_symbol,current_directory);
	}
	/* Determine the hostname of the system */
	if(gethostname(hostname,MAXSTRINGLENGTH) != SUCCESS){
		perror("dsh error display_prompt()");
	}
	printf("<%s@%s:%s>", userptr->nodename, hostname, current_dir_symbol);
	return SUCCESS;
}

/* Checks if the command line contains anything else than ; and ' ' */
int check_valid_line(char cmdline[], int* confirm_sc){
	int length = strlen(cmdline);
	int index;
	for(index = 0; index < length; index++){
		if(cmdline[index] == ';'){*confirm_sc = TRUE;}
		if(cmdline[index] != ';' && cmdline[index] != ' ' && cmdline[index] != '\n')return SUCCESS; 
	}
	return NOTSUCCESS;
}

/* Check if the command entered is a built in or not */
int check_builtIn(char cmd[]){
	int index;
	for(index = 0; index < MAXBUILTINS; index++){
		if(strcmp(cmd,listBuiltIn[index].name) == 0)return listBuiltIn[index].id;
	}
	return NOTSUCCESS;
}

/* Remove leading and trailing whitespaces */
int trim(char line[]){
	int start = -1,finish,length,index;
	length = strlen(line);
	for(index = 0; index < length; index++){
		if(line[index] != ' '){
			start = index;
			break;
		}
	}
	if(start == -1)return EMPTYLINE;
	strcpy(line,line + start);

	for(index = length - 1; index >= 0; index--){
		if(line[index] != '\n' && line[index] != ' '){
			finish = index;
			break;
		}
	}
	line[finish + 1] = '\0';
	return SUCCESS;
}

// Handler for SIGCHLD signal, used to show message on termination of a background process 
// This code heavily inspired from the sigchld handler at gnu.org
void sigchld_handler(int signum)
{
	
	//printf("Entered sigchld handler proc id %d\n",getpid());
	//printf(" Value stored in bg table - %s %d %d\n",bg_table[0].name,bg_table[0].bg_pid,bg_table[0].valid);
  int pid, status, serrno,i,flag;
  serrno = errno;
  while (1)
    {
      pid = waitpid (WAIT_ANY, &status, WNOHANG);

      //If foreground process , dont show error message
      if(tcgetpgrp(STDIN_FILENO) == getpgid(pid))break;

      // Check if the proc is in the background
      // This extra step was added only to prevent showing error messages
      // for commands b/w pipes 
      flag = FALSE;
      for(i = 0;i < MAX_BG_PROCS; i++){
      	if(bg_table[i].valid == TRUE && bg_table[i].bg_pid == pid){
      		flag = TRUE;
      		break;
      	}
      }
      if( flag == FALSE)break;


      if (pid < 0)
        {
          break;
        }
      if (pid == 0)
        break;
  	  if(pid > 0) {
    	   	printf("Process %d terminated with status %d\n",pid,status);
    	   	remove_bg_table(pid);
    	    if(WIFEXITED(status))printf("Exited normally\n");
    	   	else printf("Exited Abnormally\n");
    	   	break;
    	   }
      
    }
  errno = serrno;
  
}

int parse_input_redirection(char temp_cmdline[], char cmdline[],CommandBuffer *buf){
	int length = strlen(temp_cmdline), index = 0, index_two, idx_temp = 0;
	int len_file;
	char *char_ptr;
	char *ptr;
	char *svptr;
	char ch;
	//printf("Entered input redirection , |%s| \n",temp_cmdline);
	//printf("Length of cmdline = %d\n",length);
	for(index = 0; index < length; index++){
		if(temp_cmdline[index] == '<'){

			// Checks whether there are more characters after '<' or not
			if(index == length){
				printf("No filename after < \n");
				return NOTSUCCESS;
			}

			// Checks whether there is no >/< just after <
			for(index_two = index + 1; index_two < length; index_two++){
				// ch is a temp char
				ch = temp_cmdline[index_two];
				if(!isspace(ch)){
					if(ch == '>' || ch == '<')break;
					else idx_temp = index_two;
				}
			}
			if(idx_temp == 0 && !isspace(ch)){
				printf("Invalid io redirection, has %c in front of filename\n",ch);
				return NOTSUCCESS;
			}

			// Extract the input file name
			ptr = strtok_r(temp_cmdline + index + 1 , " \t\r\f\n\v\b>", &svptr);
			if(ptr == NULL){
				printf("No filename after < \n");
				return NOTSUCCESS;
			}

			// Copy the input file into command buffer
			strcpy(buf->input_file,ptr);

			// Erase the input file name from cmdline
			for(index_two = index; index_two < length; index_two++){
				if(cmdline[index_two] == buf->input_file[0])break;
			}
			len_file = strlen(buf->input_file);
			//printf("Index two = %d\n Length of input-file = %d\n",index_two,len_file);
			len_file += index_two;
			for(;index_two < len_file; index_two++){
				cmdline[index_two] = ' ';
			}
			
			//printf("Copied to buf->input_file |%s|\n",buf->input_file);
			break;
		}
	}
	return SUCCESS;
}

int parse_output_redirection(char temp_cmdline[], char cmdline[],CommandBuffer *buf){
	int length = strlen(temp_cmdline), index = 0, index_two, idx_temp = 0;
	int len_file;
	char *char_ptr;
	char *ptr;
	char *svptr;
	char ch;
	//printf("Entered input redirection , |%s| \n",temp_cmdline);
	//printf("Length of cmdline = %d\n",length);
	for(index = 0; index < length; index++){
		if(temp_cmdline[index] == '>'){

			// Check whether there are more characters after '>' or not
			if(index == length){
				printf("No filename after > \n");
				return NOTSUCCESS;
			}
           

            // Check whether the command is >>
            if(temp_cmdline[index + 1] == '>'){
            	//printf("File needs to be appended\n");
            	buf->append_output_file = TRUE;
            	++index;
            }

            if(index == length){
				printf("No filename after >> \n");
				return NOTSUCCESS;
			}

			// Checks whether there is no >/< just after <
			for(index_two = index + 1; index_two < length; index_two++){
				// ch is a temp char
				ch = temp_cmdline[index_two];
				if(!isspace(ch)){
					if(ch == '>' || ch == '<')break;
					else idx_temp = index_two;
				}
			}
			if(idx_temp == 0 && !isspace(ch)){
				printf("Invalid io redirection, unexpected token %c in front of filename\n",ch);
				return NOTSUCCESS;
			}

			// Extract the output file name
			ptr = strtok_r(temp_cmdline + index + 1, " \t\r\f\n\v\b<", &svptr);
			if(ptr == NULL){
				printf("No filename after > \n");
				return NOTSUCCESS;
			}

			// Copy the input file into command buffer
			strcpy(buf->output_file,ptr);

			// Erase the input file name from cmdline
			for(index_two = index; index_two < length; index_two++){
				if(cmdline[index_two] == buf->output_file[0])break;
			}
			len_file = strlen(buf->output_file);
			//printf("Index two = %d\n Length of input-file = %d\n",index_two,len_file);
			len_file += index_two;
			for(;index_two < len_file; index_two++){
				cmdline[index_two] = ' ';
			}
			
			//printf("Copied to buf->output_file |%s|\n",buf->output_file);
			break;
		}
	}
	return SUCCESS;
}



/* Parses individual commands */
int parser(char cmdline[], CommandBuffer *buf){
	char *ptr;
	char *svptr;
	char temp_cmdline[MAXSTRINGLENGTH];
	int index = 0,length,return_value;
	if(trim(cmdline) == EMPTYLINE)return EMPTYLINE;

	// Parse input redirection
	strcpy(temp_cmdline,cmdline);
	if(parse_input_redirection(temp_cmdline, cmdline, buf) != SUCCESS){
		return NOTSUCCESS;
	}

	// Parse output redirection
	strcpy(temp_cmdline,cmdline);
	if(parse_output_redirection(temp_cmdline, cmdline, buf) != SUCCESS){
		return NOTSUCCESS;
	}

	//printf("After io parsing, command line |%s|\n",cmdline);

	ptr = strtok_r(cmdline," \t\r\f\n\v\b<>",&svptr);
	while(ptr != NULL){
		if(index == 0){
			strcpy(buf->name, ptr);
		}
		buf->cargv[index] = (char *)malloc(sizeof(char) * MAXSTRINGLENGTH);
		strcpy(buf->cargv[index], ptr);
		//printf(" Parsed - |%s|\n",buf->cargv[index]);
		index++;
		ptr = strtok_r(NULL," \t\r\f\n\v\b<>",&svptr);
	}
	buf->num_cargv = index;

	/*Check if the command is a background process */
	length = strlen(buf->cargv[index-1]);
	if(buf->cargv[index-1][length-1] == '&'){
		buf->background = TRUE;
		//Removing & sign
		if(length == 1){
			free(buf->cargv[index - 1]);
			buf->cargv[index - 1] = NULL;
			buf->num_cargv = index - 1;
		}
		else{
			buf->cargv[index - 1][length - 1] = '\0';
		}
	}

	buf->builtin = check_builtIn(buf->name);
	return SUCCESS;
}

int execute_fg(CommandBuffer *buf, int term_stdin){
	int pid,pgid_foreground;
	pid = get_processid(atoi(buf->cargv[1]));
	if(pid == -1){
		printf("A background job with given id does not exist\n");
		return NOTSUCCESS;
	}
	if(remove_bg_table(pid) == NOTSUCCESS){
		printf("Process cannot be removed from bg_table\n");
		return NOTSUCCESS;
	}

	//Putting the process in the foreground group
	tcsetpgrp(term_stdin, pid);

	//Send the process a continue signal
	kill(pid,SIGCONT);

	//Waiting for the foreground process to terminate
	pgid_foreground = tcgetpgrp(term_stdin);
	waitpid(-1 * pgid_foreground,NULL,0);

	//Putting the shell back into foreground process
	tcsetpgrp(term_stdin, getpid());

	return SUCCESS;

}

int execute_overkill(){
	int i;
	for(i = 0; i < MAX_BG_PROCS; i++){
		if(bg_table[i].valid == TRUE){
			if(kill(bg_table[i].bg_pid,9) == -1){
				perror("Error in sending signals to all bg process ");
				return NOTSUCCESS;
			}
		}
	}
	return SUCCESS;
}


int execute_kjob(CommandBuffer *buf){
	int signal_id,pid;
	if(buf->num_cargv != 3){
		printf("Enter jobid and signalid correctly\n");
		return NOTSUCCESS;
	}
	pid = get_processid(atoi(buf->cargv[1]));
	if(pid == -1){
		printf("No such job\n");
		return NOTSUCCESS;
	}
	signal_id = atoi(buf->cargv[2]);
	if(kill(pid,signal_id) == -1){
		perror("Error in sending signal to process ");
		return NOTSUCCESS;
	}
	return SUCCESS;
}


//execute jobs builtin
void execute_jobs(){
	int i;
	for(i = 0 ; i < MAX_BG_PROCS; i++)
	{
		if(bg_table[i].valid == TRUE)
			printf("[%d] %s[%d]\n",i,bg_table[i].name,bg_table[i].bg_pid);
	}
}
//execute cd builtin
int execute_cd(CommandBuffer* buf){
	if(chdir(buf->cargv[1]) != SUCCESS){
		perror("dsh error execute_cd()");
		return NOTSUCCESS;
	}
	else return SUCCESS;
}
//execute pwd builtin
int execute_pwd(CommandBuffer* buf){
	char pwd_directory[MAXSTRINGLENGTH];
	if(getcwd(pwd_directory, MAXDIRLENGTH) == NULL){
		perror("dsh error getcwd execute_pwd()");
		return NOTSUCCESS;
	}
	else{
		printf("%s\n",pwd_directory);
		return SUCCESS;
	}
}

//execute echo builtin
int execute_echo(CommandBuffer *buf){
	int index;
	if(buf->num_cargv == 1);
	else{
		printf("\n");
		for(index = 1; index < buf->num_cargv; index++){
			printf("%s ",buf->cargv[index]);
		}
	}
	printf("\n");
	return SUCCESS;
}

//execute exit
void execute_quit(){
	if(execute_overkill() == -1)printf("Error in stopping background jobs\n");
	exit(0);
}

//execute pinfo
int execute_pinfo(CommandBuffer *buf){
	int proc_pid,path_len,index ;
	FILE* fl;
	char file_name[MAXSTRINGLENGTH];
	char proc_state[MAXSTRINGLENGTH];
	char path_name[MAXSTRINGLENGTH];
	char virtual_mem_size[MAXSTRINGLENGTH];
	char temp_string[MAXSTRINGLENGTH];

	if(buf->num_cargv == 1){
		proc_pid = (int)getpid();
		printf("--Shell Details--\n");
	}
	else proc_pid = atoi(buf->cargv[1]);

	//check if process with proc_id exists or not
	if(kill(proc_pid,0) == -1){
		return NOTSUCCESS;
	}

	sprintf(file_name,"/proc/%d/status",proc_pid);
	fl = fopen(file_name,"r");
	
	// Read the second line of proc
	for(index = 1; index <= 13; index++){
		fgets(temp_string,MAXSTRINGLENGTH,fl);
		if(index == 2)strcpy(proc_state,temp_string);
		if(index == 13)strcpy(virtual_mem_size,temp_string);
	}

	//Read the executable path
	sprintf(file_name,"/proc/%d/exe",proc_pid);
	path_len = readlink(file_name,path_name,sizeof(path_name) - 1);

	//Print the info extracted from files in /proc/pid 
	printf("Process ID - [%d]\n",proc_pid);
	printf("%s",proc_state);
	printf("%s",virtual_mem_size);
	if(path_len != -1){
		path_name[path_len] = '\0';
		printf("Executable Path %s\n",path_name);
	}	
	else printf("Error in finding path name\n");
	fclose(fl);
	return 0;
}

int execute_builtin(CommandBuffer* buf,char current_directory[]){
	int term_stdout;
	int term_stdin;

	// Save the file descriptor of terminal 
	term_stdout = dup(STDOUT_FILENO);
	term_stdin = dup(STDIN_FILENO);

	//Redirect I/O if any
	if(!(redirect_input(buf) == SUCCESS && redirect_output(buf) == SUCCESS)){
			printf("Redirection Failed\n");
			//Restore back to terminal
			dup2(term_stdout,STDOUT_FILENO);
			dup2(term_stdin,STDIN_FILENO);
			//Close the saved file descriptors pointing to terminal
			close(term_stdout);
			close(term_stdin);
			return NOTSUCCESS;
	}


	/* Execute the built ins routine based on the commands id */
	switch(buf->builtin){
		// Execute cd
		case 0:
		if(execute_cd(buf) != SUCCESS){
			printf("Execution of cd failed.\n");
		}
		else{
			if(getcwd(current_directory, MAXDIRLENGTH) == NULL){
				perror("dsh error getcwd execute_builtin()");
			}	
		}
		break;
		// Execute pwd
		case 1:
		if(execute_pwd(buf) != SUCCESS){
			printf("Execution of pwd failed.\n");
		}
		break;
		// Execute echo
		case 2:
		execute_echo(buf);
		break;

		case 3:
		execute_quit();
		break;

		case 4:
		if(execute_pinfo(buf) != SUCCESS)
			printf("Such a process does not exist\n");
		break;

		case 5:
		execute_jobs();
		break;

		case 6:
		if(execute_kjob(buf) != SUCCESS)
			printf("Signal not sent\n");
		break;

		case 7:
		if(execute_overkill() != SUCCESS)
			printf("Error in killing all process\n");
		break;

		case 8:
		if(execute_fg(buf,term_stdin) != SUCCESS)
			printf("Error putting processs to foreground\n");
		break;

		default:
		printf("Switch condition not possible \n");
		break;

	}

	//Restore back to terminal
	dup2(term_stdout,STDOUT_FILENO);
	dup2(term_stdin,STDIN_FILENO);

	//Close the saved file descriptors pointing to terminal
	close(term_stdout);
	close(term_stdin);
	return SUCCESS;
}

int handle_pipes(int proc_index, int pfd[MAX_PIPES][2], int num_pipes)
{
	//printf("S----------------------\nFor process - %d\n",proc_index);
	//printf(" Entered Pipe Handler\n Number of pipes %d\n",num_pipes);
	int i;
	if(num_pipes > 0){

		// Redirect its standard I/O streams to pipe streams

		// Check if the process is a first process
								// If not then duplicate STD_IN on read end of previous pipe
		if(proc_index != 0){
			if(pfd[proc_index - 1][0] != STDIN_FILENO){
				//printf("Done input redirection\n");
				if(dup2(pfd[proc_index - 1][0], STDIN_FILENO) == -1){
					perror("Error in pipe input redirection, proc index ");
					return NOTSUCCESS;
				}
				if(close(pfd[proc_index - 1][0]) == -1){
					perror("Error in closing pipe file descriptor,proc index ");
					return NOTSUCCESS;
				}
			}
		}

		// Checks if the process is the last process
		// If not then duplicate STD_OUT on write end of current pipe
		if(proc_index != num_pipes){
			if(pfd[proc_index][1] != STDOUT_FILENO){
				//printf("Done output redirection\n");
				if(dup2(pfd[proc_index][1], STDOUT_FILENO) == -1){
					perror("Error in pipe output redirection, proc index");
					return NOTSUCCESS;
				}
				if(close(pfd[proc_index][1]) == -1){
					perror("Error in closing pipe file descriptor,proc index");
					return NOTSUCCESS;
				}
			}
		}

		// Close the pipe descriptors not in use
		for(i = 0; i < num_pipes; i++){
			if(i != proc_index){
				//printf("Pipe %d's write fd closed\n",i);
				if(close(pfd[i][1]) == -1){
					perror("Error in closing pipe descriptors not in use ");
					return NOTSUCCESS;
				}
			}
			if(i != proc_index - 1){
				//printf("Pipe %d's read fd closed\n",i);
				if(close(pfd[i][0]) == -1){
					perror("Error in closing pipe descriptors not in use ");
					return NOTSUCCESS;
				}
			}

		}
	}
	//printf("E------------------------------\n");
	return SUCCESS;
} 


// The working of the below initialize_shell() function is taken
// from gnu.org 's documentation regarding shell initiaization
void initialize_shell(char home_directory[])
{
	int controlling_terminal = isatty(STDIN_FILENO);
	pid_t shell_pid;
	struct termios t_modes;

	//Continue only if there is a controlling terminal
	if(controlling_terminal)
	{
		/* Check if this sub-shell is in the foregroud process list of the
			main shell.If not so, stop it so that the main shell puts it
			in foreground group. */
		while(tcgetpgrp(STDIN_FILENO) != (shell_pid = getpid()))
			kill(-shell_pid,SIGTTIN);
		/* For this shell ignore some signal values so as to prevent
			accidental closure of the shell */
		signal(SIGTTIN,SIG_IGN);
		signal(SIGINT,SIG_IGN);
		signal(SIGQUIT,SIG_IGN);
		signal(SIGTSTP,SIG_IGN);
		signal(SIGTTOU,SIG_IGN);
		signal(SIGCHLD,sigchld_handler);
		/* Sets the shells process group id to its
		   own process id */
		setpgid(shell_pid, shell_pid);
		/* Make the foregrounds process group id  equal to shells
		   process group id */
		tcsetpgrp(STDIN_FILENO, shell_pid);

		tcgetattr(STDIN_FILENO, &t_modes);
	}

	//Sets shells home directory to current directory
	if(getcwd(home_directory, MAXDIRLENGTH) == NULL){
		perror("dsh error getcwd initialize_shell()");
	}

}

int push_process_background(CommandBuffer *buf, int pid){
	if(add_bg_table(buf,pid) == NOTSUCCESS){
		printf("Not able to add to bg_table\n");
		return NOTSUCCESS;
	}
	setpgid(pid,pid);
	return SUCCESS;
}

// Redirect input to a file if user wishes so
int redirect_input(CommandBuffer *buf){
	//printf("Entered redirect input\n");
	if(strcmp(buf->input_file,"") == 0)return SUCCESS;
	int file_fd;
	file_fd = open(buf->input_file, O_RDONLY);
	//printf(" File fd = %d\n",file_fd);
	if(file_fd == -1){
		perror("Error in opening the file ");
		return NOTSUCCESS;
	}
	if(dup2(file_fd,STDIN_FILENO) == -1){
		printf("Duplication of file descriptor failed ");
		return NOTSUCCESS;
	}
	return SUCCESS;
}

// Redirect output to a file if the user wishes so
int redirect_output(CommandBuffer *buf){
	//printf("Enterd redirect_output\n");
	if(strcmp(buf->output_file,"") == 0)return SUCCESS;
	int file_fd;
	if(buf->append_output_file == TRUE){
		file_fd = open(buf->output_file, O_WRONLY| O_CREAT| O_APPEND, S_IRUSR| S_IWUSR);
	}
	else file_fd = open(buf->output_file, O_WRONLY| O_CREAT| O_TRUNC, S_IRUSR| S_IWUSR);
	//printf(" File fd = %d\n",file_fd);
	if(file_fd == -1){
		perror("Error in opening/creating the file ");
		return NOTSUCCESS;
	}
	if(dup2(file_fd,STDOUT_FILENO) == -1)
	{
		perror("Duplication of file descriptor failed ");
		return NOTSUCCESS;
	}
	//printf("Ok");
	return SUCCESS;
}

// Checks that there is only one & at the last, if there are pipes
int check_background_pipe(char cmdline[])
{
	int length = strlen(cmdline),flag = FALSE;
	int index,count = 0;
	for(index = 0; index < length; index++){
		if(cmdline[index] == '&')count++;
	}
	if(count > 1)return NOTSUCCESS;
	else if(count == 1){
		//Check if the only background is in the last command
		for(index = length - 1; index >=0; index--){
			if(!isspace(cmdline[index])){
				if(cmdline[index] != '&')flag = TRUE;
				else break;
			}
		}
		if(flag == FALSE)return SUCCESS;
		else return NOTSUCCESS;
	}
	else return SUCCESS;
}

void set_signal_default(){
	// Set the signal to default values since it was changed for shell
	signal(SIGTTIN,SIG_DFL);
	signal(SIGINT,SIG_DFL);
	signal(SIGQUIT,SIG_DFL);
	signal(SIGTSTP,SIG_DFL);
	signal(SIGTTOU,SIG_DFL);
}

int main(int argc, char *argv[])
{

	/* Variable Declarations */
	struct utsname userdata;
	char current_directory[MAXDIRLENGTH],home_directory[MAXSTRINGLENGTH];
	CommandBuffer buf;
	
	char cmdline[MAXSTRINGLENGTH], pipecmd_copy[MAXSTRINGLENGTH];
	char *nextcmd,*svptr1,*pipecmd,*pipeptr,nextcmd_copy[MAXSTRINGLENGTH];
	int confirm_sc,child_pid,parent_pid,exited_pid,exited_status_code;
	int parser_status,proc_index,num_pipes = 0,wait_status,index;
	int pfd[MAX_PIPES][2],pgid_foreground,j,status = 0;

	//Prepares the shell to handle background/foreground process'
	initialize_shell(home_directory);

	//Initialize background process table
	initialize_bg_table();

	/* Sets the default directory to users home directory */
	if(getcwd(current_directory, MAXDIRLENGTH) == NULL){
		perror("dsh error getcwd main():");
		return NOTSUCCESS;
	}

	/* Extracts out the username and operating system name */
	if(uname(&userdata) != SUCCESS){
		perror("dsh error uname main():");
		return NOTSUCCESS;
	}


	while(TRUE){
		/* Displays prompt */
		if(display_prompt(&userdata, current_directory, home_directory) != SUCCESS){
			return NOTSUCCESS;
		}

		/* read the line from the terminal */
		if(fgets(cmdline, MAXSTRINGLENGTH, stdin) == NULL){
			//printf("Error in reading a line main()\n");
			//return NOTSUCCESS;
			printf("\n");
			continue;
		}
	
		/* Check whether cmdline contains anything else than ; and ' ' */
		confirm_sc = FALSE;
		if(check_valid_line(cmdline,&confirm_sc) == NOTSUCCESS){
			if(confirm_sc == TRUE)printf("Enter a valid command\n");
			continue;
		}

		/* Tokenize the commands on ; and parse and execute each one of them */
		nextcmd = NULL;
		nextcmd = strtok_r(cmdline,";",&svptr1);
		int loop_count = 1;
		while(nextcmd != NULL){
			//printf("Command \n: %s",nextcmd);
			
			/*Make a copy of the command to be used for parsing */
			strcpy(nextcmd_copy,nextcmd);
			int d = strlen(nextcmd_copy);
			//printf("Length of command %d\n",d);
			/* Clears the buffer */ 

			// Counts the number of pipe
			num_pipes = count_pipes(nextcmd_copy);

			if(num_pipes > 0){

			
				if(check_background_pipe(nextcmd) != SUCCESS){
					printf("Conflict between pipes and background: Background process can't be between pipes\n");
					nextcmd = strtok_r(NULL,";",&svptr1);
					continue;
				}
				
				int flag = FALSE;

				// Create pipes
				for(index = 0; index < num_pipes; index++){
					if(pipe(pfd[index]) == -1){
						perror("Pipe can't be created ");
						flag = TRUE;
					}
				}
				if(flag == TRUE){// if pipe was not created it continues
					nextcmd = strtok_r(NULL,";",&svptr1);
					continue;
				}
			}

			proc_index = 0;
			pipecmd = NULL;
			pipecmd = strtok_r(nextcmd_copy,"|",&pipeptr);
			while(pipecmd != NULL){
					/* Parses the cmdline */
				strcpy(pipecmd_copy,pipecmd);
				initialize_CommandBuffer(&buf);
				free_buffer(&buf);
				parser_status = parser(pipecmd_copy, &buf);
		    	if(parser_status == EMPTYLINE){
		    		printf("Entered empty line slot\n"); 
		    		nextcmd = strtok_r(NULL,"|",&pipeptr);
		    		continue; 
		    	}
		    	else if(parser_status == NOTSUCCESS){
		    		printf("Enter valid command\n"); 
		    		nextcmd = strtok_r(NULL,"|",&pipeptr); 
		    		continue; 
		    	}

				/* Check wheteher the command is a built in */
				if(buf.builtin != NOTSUCCESS){
					if(execute_builtin(&buf,current_directory) == NOTSUCCESS){
						printf("Execution of builtin failed\n");
					}
				}

				else{
					signal(SIGCHLD,sigchld_handler);
					parent_pid = getpid();
					//printf(" Process created %d \n",loop_count++);
					//printf(" fork called \n");
					child_pid = fork();
					//Process creation failed
					if(child_pid < 0){
						perror("Process creation failed main()");
					}

					// Child process
					else if(child_pid == 0){

						//Get the pid of the child process
						int pid = getpid();

						//Adding the child into its own process group
						setpgid(pid,pid);
						
						if(buf.background == TRUE)
						{
							//If process is background process
							printf("Process ID: [%d]\n",pid);
						}
						else 
						{
							// If process is a foregroud process
							tcsetpgrp(STDIN_FILENO, pid);
						}

						// Redirect I/O if required
						if(!(redirect_input(&buf) == SUCCESS && redirect_output(&buf) == SUCCESS))
						{
							printf("Redirection Failed\n");
							kill(pid,SIGINT);
						}

						set_signal_default();

						// Handling for Pipes
						if(handle_pipes(proc_index,pfd,num_pipes) == NOTSUCCESS){
							printf("Error in handling pipes\n");
							kill(getpid(),SIGINT);
						}
						
						if(execvp(buf.cargv[0], buf.cargv) == NOTSUCCESS){
							perror("System command not able to execute");
							kill(getpid(),SIGINT);
						}
			
					}

					// Parent process
					else if(child_pid > 0){
						setpgid(child_pid, child_pid);
						if(buf.background == FALSE){
							// Sets the process group id as that of parent
							tcsetpgrp(STDIN_FILENO, child_pid);
						}
						else{
							// We make each background process a process group in itself.
							printf("Background Process\n");	
							if(add_bg_table(&buf,child_pid) == NOTSUCCESS){
								printf(" Background proces could not be added to bg_table.\n");
								
							}				
						}
						
					}
				} // <-- closes else
				pipecmd = strtok_r(NULL,"|",&pipeptr);
				proc_index++;
			} // <-- closes while of pipe
			// Parent process
			

			
			for(j = 0; j < num_pipes; j++){
				//printf(" PARENT -- , closing pipe fd %d\n",j);
				if(close(pfd[j][0]) == -1){
					perror("Error in closing pipe read file descriptors of parent process");
					break;
				}
				if(close(pfd[j][1]) == -1){
					perror("Error in closing pipe write file descriptors of parent process");
					break;
				}
			}
			num_pipes = 0;

			
			// Waits for the foreground process created above
			status = 0;
			pgid_foreground = tcgetpgrp(STDIN_FILENO);
			do{
				wait_status = waitpid(-1 * pgid_foreground, &status, WUNTRACED);
				if(wait_status == -1 && errno != ECHILD)
				{
					perror("Error during waiting ");
					break;
				}

				// To handle foreground jobs which have been stopped by Ctrl + Z
				if(WIFSTOPPED(status)){
					printf("Process(%d) stopped and sent to background\n",wait_status);
					push_process_background(&buf,wait_status);
					break;
				}
			}while(wait_status > 0);


			//Change back to shell
			tcsetpgrp(STDIN_FILENO, getpid());
	
		    nextcmd = strtok_r(NULL,";",&svptr1);  
		}			
	}
}
