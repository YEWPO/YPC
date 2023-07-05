#include "sdb/watchpoint.h"
#include "sdb/expr.h"

#define NR_WP 32

typedef struct watchpoint {
  int NO;
  char str[65536];
  word_t val;
  struct watchpoint *next;
} WP;

static WP wp_pool[NR_WP] = {};
static WP *head = NULL, *free_ = NULL;

void init_wp_pool() {
  int i;
  for (i = 0; i < NR_WP; i ++) {
    wp_pool[i].NO = i;
    wp_pool[i].next = (i == NR_WP - 1 ? NULL : &wp_pool[i + 1]);
  }

  head = NULL;
  free_ = wp_pool;
}

void new_wp(char* str) {
  WP* ptr = free_;

  Assert(ptr != NULL, "No more watchpoint.");

  bool success;
  ptr->val = expr(str, &success);

  if (success == false) {
    ptr->val = 0;
    printf("invalid expression: %s\n", str);
    return;
  }
  strncpy(ptr->str, str, 65536);

  free_ = free_->next;
  ptr->next = head;
  head = ptr;

  printf("add watchpoint NO: %d, EXPR: %s\n", ptr->NO, str);
}

void free_wp(int no) {
  if (head == NULL) {
    printf(ANSI_FMT("no such watchpoint!", ANSI_FG_RED) "\n");
    return;
  }

  if (no == head->NO) {
    WP *ptr = head;
    head = head->next;
    ptr->next = free_;
    free_ = ptr;
    printf("delete watchpoint NO: %d\n", no);
    return;
  }

  WP* lptr = head;
  WP* nptr = lptr->next;
  while (nptr != NULL) {
    if (nptr->NO == no) {
      break;
    }
    lptr = nptr;
    nptr = nptr->next;
  }

  if (nptr == NULL) {
    printf(ANSI_FMT("no such watchpoint!", ANSI_FG_RED) "\n");
    return;
  }

  lptr->next = nptr->next;
  nptr->next = free_;
  free_ = nptr;

  printf("delete watchpoint NO: %d\n", no);
}

void print_watchpoints() {
  WP* ptr = head;

  while (ptr != NULL) {
    printf("NO: %d\tEXPR: %s\n", ptr->NO, ptr->str);
    ptr = ptr->next;
  }
  printf("\n");
}

void check_watchpoints(bool *newtag) {
  WP* ptr = head;
  *newtag = false;

  while (ptr != NULL) {
    bool success;
    word_t val = expr(ptr->str, &success);
    if (val != ptr->val) {
      *newtag = true;
      printf("watchpoint NO %d:\n"
          "old value: " FMT_WORD "\t new value: " FMT_WORD "\n\n", ptr->NO, ptr->val, val);
      ptr->val = val;
    }
    ptr = ptr->next;
  }
}
