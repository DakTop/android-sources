//包括aidl的基础使用，in,out，inout定向tag的使用，也可以看做是客户端与服务端通信
package com.interfaces;
//使用aidl非默认的数据类型，必须引入相应实体类的包名。
import com.model.Book;
//定义跨进程通信中客户端与服务端需要调用的接口，
//这里注意在跨进程通信中客户端与服务端是相对而言的。
//因为它就是两个进程，谁都可以做服务端，谁都可以做客户端。
//所以这里定义的接口修改的都是Binder类所在进程的数据。
interface IBookInterface {
   //获取图书列表
   List<Book> getBookList();

   /*传参时除了Java基本类型以及String，CharSequence之外的类型
    * 都需要在前面加上定向tag，具体加什么量需而定。
    *
    * 向当前进程添加一条图书信息。
    */
   Book inBook(in Book book);

   Book outBook(out Book book);

   Book inoutBook(inout Book book);
}
