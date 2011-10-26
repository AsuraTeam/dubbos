/*
 * Copyright 1999-2101 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.protocol.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.springframework.remoting.support.RemoteInvocation;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;

/**
 * 
 * @serial
 * @author ding.lid
 */
class SpringHandler2RmiInvocationHandler extends AbstractRmiInvocationHandler {
    
    private org.springframework.remoting.rmi.RmiInvocationHandler springHandler;

    SpringHandler2RmiInvocationHandler(org.springframework.remoting.rmi.RmiInvocationHandler springHandler, Class<?> type)
    {
        // check remote object and interface.
        if( type.isInterface() == false )
            throw new IllegalArgumentException("Service type must be interface. " + type.getName());

        this.springHandler = springHandler;
    }

    public Result invoke(Invocation inv)
        throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        RpcResult result = new RpcResult();
        try
        {
            RemoteInvocation i = new RemoteInvocation();
            i.setMethodName(inv.getMethodName());
            i.setParameterTypes(inv.getParameterTypes());
            i.setArguments(inv.getArguments());
            
            result.setResult(springHandler.invoke(i));
        }
        catch(InvocationTargetException e)
        {
            result.setException(e.getTargetException());
        }
        
        return result;
    }

}