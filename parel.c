#include <stdio.h>
#include <sys/dir.h>

struct ffblk
{
	char ff_reserved[21];//DOS文件保留字
	char ff_attrib;//文件属性
	int ff_ftime;//文件时间
	int ff_date;//文件日期
	long ff_fsize;//文件大小
	char ff_name[13];//文件名称
}

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
