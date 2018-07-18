//注册服务端数据变化接口
package com.interfaces;
import com.interfaces.IServerDataChangeListener;

//adil中传输的数据类型也包括aidl接口类型。
interface IRegisterDataListenerInterface {

    void registerServerDataChangeListener(IServerDataChangeListener listener);
    void unRegisterServerDataChangeListener(IServerDataChangeListener listener);

}
