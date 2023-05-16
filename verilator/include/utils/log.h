#ifndef _LOG_H
#define _LOG_H

#define log_write(...) \
  do { \
    extern FILE* log_fp; \
    fprintf(log_fp, __VA_ARGS__); \
    fflush(log_fp); \
  } while (0)

void init_log(const char *log_file);

#endif
