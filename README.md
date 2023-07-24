NPC
=======================

> New Process Core
>
> Writen by Chisel

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

## 工作目录

```
.
|- docs // 该目录下存放了一些文档
|- playgroud
	|- src // NPC的硬件描述核心代码部分
	|_ test // NPC的测试代码
|- verilator
	|- include // 仿真器的头文件
	|- script // 仿真器的编译脚本
	|- src // 仿真器的源代码
	|_ tools // 仿真器的辅助工具
|- build.sc // scala环境配置文件
|- README.md
|_ Makefile
```

## 详细设计

### 微架构设计图

![微架构设计图](./docs/微架构设计.png)

在整个微架构设计当中，整个设计分为三个层面：数据通路层面，控制层面以及冒险层面。

- 数据通路层面：表示着正常数据流通过情况。

- 控制层面：根据指令生成控制信号，控制相应的逻辑单元的运算结果。

- 冒险层面：通过冒泡，转发，冲刷等手段，防止流水线发生结构冒险，数据冒险和控制冒险。

  数据冒险部分使用两份码图表示寄存器的使用情况，假设第一份码图为$A$，第二份码图为$B$。若两份码图中比特位均为0，则表示对应寄存器的值可以使用。若$A$中的比特位为1，则表示对应寄存器值需要被执行单元的运算结果更新。若$B$中的比特位为1，则表示对应寄存器值需要被内存读写单元更新。第一阶段数据冒险的解决方案：暂停流水线的执行。

  控制冒险，即无条件跳转和分支跳转指令的下一条指令的地址判断冒险。由于分支结果最早在执行阶段得出，所以我们第一阶段的解决方案是译码阶段暂停两个时钟周期。

### 控制信号

[控制信号文档](./docs/控制信号.md)，在该文档中详细介绍了各个信号的具体设计含义以及它们的作用。

### 控制信号表

[控制信号表](./docs/控制信号表.csv)，记录了每个指令具体产生的信号。

预计将编写`python`脚本使之能通过该表自动生成`chisel`代码。

## 测试

### 测试目的

通过单元测试和集成测试，尽最大可能保证局部模板的正确性，进而使整个设计正确，满足设计功能需求和产品的使用需求。

### 测试用例

`TODO`

## FIRRTL编译器

你可以通过在`playground/src/Elaborate.scala`代码中的`useMFC`变量设置使用`SFC(scala-based FIRRTL compiler)`还是使用`MFC(MLIR-based FIRRTL compiler)`编译器。后者需要在`utils/`目录下的`firtool`工具。
