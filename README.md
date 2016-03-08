# AndroidDemo-MessageSystemDemo
This is an Android demo to research the Android message system, each module and its funtion are listed below:

1. **MessagePoolDemo**
This module is to research the message pool's recycling and reusing messages.

2. **MemoryLeakInMessageSystemDemo**
This module is to show the easy-to-happen Activities' memory leaks caused by non-static anonymous inner classes like Handlers and Runnables and the prevention ways, that is using static inner classes with WeakReferences to the outer Activity object. LeakCanary libraries are included as one of the dependencies to help check the potential memory leaks in this module.



这是一个用于研究 Android 消息系统的 demo工程, 工程中各个module的主要作用如下:
1. **MessagePoolDemo**

    该 module主要用于研究 Message类中设计的消息池, 以及消息池对消息对象的回收与重复利用.
    
2. **MemoryLeakInMessageSystemDemo**

    该 module展示了在 Activity中使用 Android消息系统进行消息传递时, 非常容易导致内存泄露的一些不好的写法,
    这些导致内存泄露的写法通常来说都是在 Activity中使用了非静态匿名内部类(如: 非静态匿名的 Handler或 Runnable的子类
    对象), 使得这些内部类对象持有着 Activity的一个强引用, 在Activity销毁时, 由于仍然有未处理的任务或消息, 导致 Activity
    在退出时却无法被回收造成泄露. 为了避免由上述原因造成 Activity对象在内存中的泄露, 我们可以将 Handler, Runnable等的子类
    都定义成静态的内部类, 并将它们对外部的 Activity的引用由强引用改为弱引用. 该 module中也继承了 Square 公司用于检查内存泄露
    的工具 LeakCanary, 以帮助我们检查并展示该 module中发生内存泄漏的情况.
