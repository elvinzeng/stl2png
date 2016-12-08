将PRO/E等3D建模工具生成的STL格式的模型文件生成缩略图的工具。
### 用法
```bash
java -jar stl2png.jar -s cube.stl -t cube.png
```
test_stl/stl2png.jar是我编译好的测试包，可以直接使用。
如果成功，则在最后量的输出中可以看到：
```bash
success
transform STL to png total takes 1515 millisecond
```
### 参数
```bash
elvin@elvin-pc ~/workspace/stl2png/test_stl $ java -jar stl2png.jar -h
usage:
	-s STL file path
	-t target png file path
	-h help
	-v show details of transform
	-V calculate volume and output it
	-S calculate surface area and output it
```
### 运行环境
java8、povray3.7、Linux。  
使用之前请先安装好java8、povray3.7。  
由于目前我在服务器上使用这个程序，所以暂时没有考虑过要支持Windows，如果有需要，可以fork之。  
### 项目由来
由于业务需要，需要将STL文件转换为预览图片。后来在github上找到一个名为stljs的项目，nodejs写的。于是我用上了这个开源工具，不过后来发现，用上之后服务器总是会因内存溢出而挂掉。经过测试服务器上的大量测试，我有充分的理由认为这个开源项目性能相当低下。1.占用内存非常多，2.转换很慢。比如，经过我大量实验证明stljs转换70M左右的STL文件就需要消耗3个G的内存，用掉数十秒（接近一分钟）。于是我放弃了那个开源工具，开始研究其实现。然后自己写了一个，改进了转换方式，降低了内存占用，也提高了效率。在我的机器上，8个G的物理内存（空闲的大概800M左右），8个G的交换空间，i3，linux mint下测试同样那个70M的STL文件，一共用去16秒左右就转换完了。内存占用大概几百兆，交换空间占用率稍微抖动了一点。目前这个程序已经能满足我的需求了。不过由于这个项目的创意以及方法来源于别人开源的项目，我只是分析了代码换用其他语言重新实现了一遍，并改进了一下机制，所以我决定将这个小项目的源码开放。既然从开源世界中来，就让它回开源世界去。以后有这种需要的朋友们或许也能从这里得到灵感。
###  优点
转换体积大的STL文件时比较节省资源，效率也相对较高。
