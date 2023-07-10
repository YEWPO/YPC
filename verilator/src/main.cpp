#include "common.h"
#include <getopt.h>
#include "simulate/simulator.h"
#include "memory/pmem.h"
#include "sdb/sdb.h"
#include "simulate/difftest.h"
#include "utils/state.h"

static char *log_file;
static char *img_file;
static char *dl_file;
long img_size;
void init_disasm(const char *triple);
void func_sym_init(const char *elf_file);
void dl_init(const char *dl_file, long size);

static void parse_args(int argc, char *argv[]) {
  const struct option table[] = {
    {"batch"    , no_argument      , NULL, 'b'},
    {"log"      , required_argument, NULL, 'l'},
    {"help"     , no_argument      , NULL, 'h'},
    {"elf"      , required_argument, NULL, 'e'},
    {"dl"       , required_argument, NULL, 'd'},
    {0          , 0                , NULL,  0 },
  };
  int o;
  while ( (o = getopt_long(argc, argv, "-hbl:e:d:", table, NULL)) != -1) {
    switch (o) {
      case 'b': sdb_set_batch_mode(); break;
      case 'l': log_file = optarg; break;
      case 'e': func_sym_init(optarg); break;
      case 'd': dl_file = optarg; break;
      case 1: img_file = optarg; return;
      default:
        printf("Usage: %s [OPTION...] IMAGE [args]\n\n", argv[0]);
        printf("\t-b,--batch              run with batch mode\n");
        printf("\t-l,--log=FILE           output log to FILE\n");
        printf("\t-e,--elf=ELFFILE        elf file of program\n");
        printf("\t-d,--dynamic=DLFILE     dynamic linke file\n");
        printf("\n");
        exit(0);
    }
  }
}

static long load_img() {
  if (img_file == NULL) {
    Log("No image is given. Use the default build-in image.");
    return 4096; // built-in image size
  }

  FILE *fp = fopen(img_file, "rb");
  Assert(fp, "Can not open '%s'", img_file);

  fseek(fp, 0, SEEK_END);
  long size = ftell(fp);

  Log("The image is %s, size = %ld", img_file, size);

  fseek(fp, 0, SEEK_SET);
  int ret = fread(guest_to_host(CONFIG_MBASE), size, 1, fp);
  assert(ret == 1);

  fclose(fp);
  return size;
}

int main(int argc, char *argv[]) {
  parse_args(argc, argv);

  init_log(log_file);

  img_size = load_img();

  simulator_init();

  IFDEF(CONFIG_DIFFTEST, dl_init(dl_file, img_size));

  sdb_init();

  IFDEF(CONFIG_ITRACE, init_disasm("riscv64" "-pc-linux-gnu"));

  sdb_mainloop();

  simulator_destroy();

  return is_exit_status_bad();
}
