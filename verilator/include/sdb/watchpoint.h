#ifndef _WATCHPOINT_H
#define _WATCHPOINT_H

#include "common.h"

void init_wp_pool();
void new_wp(char *str);
void free_wp(int no);
void print_watchpoints();
void check_watchpoints(bool *newtag);

#endif
