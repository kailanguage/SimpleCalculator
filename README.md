# SimpleCalculator

## 简单Android计算器 

### 移动应用开发课程实验作业

**演示**

<img src="/image/one.png" width="100" height="200"><img src="/image/two.png" width="100" height="200">

**功能方面：**

加减乘除和根号，实现简单的a@b，得到的结果可作为下一个a。

可进行大量的加减乘除根号运算

在输入方面存在限制，如一个数不能两个小数点，不能连续运算符，连续根号除外，各种错误输入均不会使程序崩溃

 

**算法思想：** 

贪心暴力

①   从输入框获取字符串，存于ArrayList中，可增加、删除、清空

②   存在输入预处理

- 第一个字符不能是运算符号，正负根号除外
- 不能连续运算符号
- 一个数只能一个小数点
- 根号预处理，当根号前为一个数时添加乘号

③   当按下等号，将ArrayList中的运算符与数字分割

④   之后分别进行√、÷、×、-、+具有优先级运算，根号为从后往前计算，每次计算不断删除最后一个，并把最后一个根号替换为前一个运算结果，故可计算连续根号，直到只剩下最后一个根号并计算出结果；其他运算均为两个数中间夹杂一个运算符号，将这三个计算生成一个，不断将长式消除，直到只剩一个数


**如何使用：**
直接 gitclone 到本地或下载 zip解压，之后使用Android studio 3.x 打开build.gradle即可，等待下载完相关依赖即可运行
