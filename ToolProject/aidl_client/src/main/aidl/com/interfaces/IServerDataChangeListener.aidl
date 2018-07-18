// 服务端数据更新监听，用于服务端向客户端通信。
package com.interfaces;
import com.model.Book;
//此接口作为接口数据类型跨进程传递
interface IServerDataChangeListener {
     void deleteBook(int position, in Book b);
     void updateBook(int position,in Book b);
     void initBookList(in List<Book> list);
 }
