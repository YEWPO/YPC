NPC
=======================

New process core

Only support RISCV64I instructions.

## Getting Started

First, install mill by referring to the documentation [here](https://com-lihaoyi.github.io/mill).

Second, install verilator by referring to the documentation [here](https://verilator.org/guide/latest/install.html).

To run all tests in this design (recommended for test-driven development):
```bash
make test
```

To generate Verilog:
```bash
make verilog
```

To simulate on verilator:

```bash
make sim
```

If you want to watch the wave generated in verilator:

```bash
make mv
```

**YOU NEED INSTALL GTKWAVE FIRST**

Clean the build directory:

```bash
make clean
```

## Design Details

This process core is a single circle process core. We have decoder, algorithm logic unit, branch unit and registers parts.

  All the parts' digital paths are shown as follows.

The Decoder part:

![IMG_20230522_225541](https://raw.githubusercontent.com/YEWPO/yewpoblogonlinePic/main/IMG_20230522_225541.jpg)

The Algorithm Logic Unit part:

![IMG_20230508_215438](https://raw.githubusercontent.com/YEWPO/yewpoblogonlinePic/main/IMG_20230508_215438.jpg)

The Branch Unit part:

![IMG_20230516_233548](https://raw.githubusercontent.com/YEWPO/yewpoblogonlinePic/main/IMG_20230516_233548.jpg)

The Register part:

![IMG_20230522_225648](https://raw.githubusercontent.com/YEWPO/yewpoblogonlinePic/main/IMG_20230522_225648.jpg)

The memory:DRAM is simulated by verilator, and it's code is in `verilator/src/memory/memory.cpp`. All the parts are connected in the `playground/src/Top.scala` source code.

## Testing

This new process core didn't have enough tests. It has ran two programs and seemed that no error happened.

Most of the small parts such as IMM-Decoder, algorithm function parts and registers have been tested. The tests codes are in `playgroud/test` folder.

## Change FIRRTL Compiler

You can change the FIRRTL compiler between SFC (Scala-based FIRRTL compiler) and
MFC (MLIR-based FIRRTL compiler) by modifying the `useMFC` variable in `playground/src/Elaborate.scala`.
The latter one requires `firtool`, which is included under `utils/`.
