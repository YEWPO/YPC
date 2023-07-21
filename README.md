NPC
=======================

> New Process Core

`RISC-V`五级流水先处理器，预计将支持`RISCV64IMZicsr`指令集。

## 开始

首先，你需要在 [此处](https://com-lihaoyi.github.io/mill)安装`mill`.

然后，按照 [此处](https://verilator.org/guide/latest/install.html)安装`verilator`.

测试整个设计：
```bash
make test
```

生成`verilog`代码：
```bash
make verilog
```

在`verilator`上面仿真：

```bash
make sim
```

查看`verilator`仿真时产生的波形文件：

**首先需要安装`gtkwave`**。对于`Ubuntu`，你可以通过`sudo apt install gtkwave`来安装。

```bash
make mv
```

清理项目中的编译中间文件：

```bash
make clean
```

## 详细设计

### 微架构设计图

[微架构设计图](./docs/微架构设计.drawio)

需要使用`draw.io`软件查看，你可以在[这里](https://draw.io)中查看。

### 控制信号表

[控制信号表文档](./docs/控制信号表.md)

## 测试

`TODO`

## Change FIRRTL Compiler

You can change the FIRRTL compiler between SFC (Scala-based FIRRTL compiler) and
MFC (MLIR-based FIRRTL compiler) by modifying the `useMFC` variable in `playground/src/Elaborate.scala`.
The latter one requires `firtool`, which is included under `utils/`.
