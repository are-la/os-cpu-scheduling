# os-cpu-scheduling

> **Authored by are-la（软件2006班陈鸣, CSU）**

### 数据结构

#### 常用工具类

1.`PCB`类来存储进程相关信息

  主要包括以下几个变量：

* `String name`  保存进程的名称
* `int runtime`   保存进程所需的运行时间
* `int arrivetime`   保存进程到达时间
* `int priority`   保存优先级调度时进程的优先级
* `string condition`  保存进程目前的状态 是否被挂起 初始化值为未被挂起
* `PCB nextPcb`    保存下一个pcb的地址
* `Int sizeOfMem` 保存进程运行所需要的内存大小



  2.`Table`类用来存储内存空间被占用的情况

​    主要包括以下几个变量：

* `int startAddress` 保存进程占用空间的起始地址 
* `int length` 保存进程占用的内存的长度
* `String status` 保存内存空间的状态是被占用还是空闲
* `String name` 保存占用内存空间的进程名称



3.`myCollection`类实现`Comparator`接口

​    主要用于重写`sort`函数的排序算子 按照优先数增序排序 即将优先级最大的放在最前面



#### 常用视图类

* `updataMenPane` 一设置内存初始大小并显示
* `ProcessPane` 一 设置cpu道数界面
* `NewPane` 一 手动创建新进程界面



#### Main类中的数据结构

* `int size=100` 初始化内存大小为一百
* `int menShengyu=50` 初始化进程可使用内存为五十
* `List<PCB> ready list `一存放就绪队列中的进程
* `List<PCB> behind list` 一存放后备队列中的进程
* `List<PCB> hanglist` 一存放挂起队列中的进程
* `List<PCB> finish list` 一存放终止队列中已完成的进程



### 功能函数

* `clear()` 一清空所有数据
* `Init()` 一随机产生进程并显示
* `changStates()` 一判断把符合条件的进程从后备队列放入就绪队列 或者把就绪队列中第一个的进程放入cpu运行
* `findSpace()` 一判断剩余内存空间是否足以支撑把进程从后备队列放入就绪队列
* `displayBehind()` 一更新后备队列的显示
* `display Ready()` 一更新就绪队列的显示



### 核心算法

设置一个计时器任务，当程序一运行起来的时候就开始每隔100毫秒去运行任务代码判断处理解挂问题，处理解卦问题响应速度为100毫秒，再设置判断每1秒去更新一下cpu调度，主要更新为将后备队列的进程放入就绪队列，将就绪队列的第一个进程放入运行队列。运行一个更新时间单位后再判断，把正在运行的进程由放入就绪队列最后，如果是优先级调度就进行排序。再次重复上述过程！



