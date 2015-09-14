#include <stdio.h>
#include <dir.h>

int main()
{
	struct ffblk ffblk;
	int done;
	printf("Directory listing of *.* \n");
	done=findfirst("*.*",&ffblk,0);
	while(!done)
	{
		printf("%s\n",ffblk.ff_name);
		done=findnext(&ffblk);
	}
	return 0;
}
