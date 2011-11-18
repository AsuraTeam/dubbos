/*
 * Copyright 1999-2011 Alibaba Group.
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
package com.alibaba.dubbo.common.serialize.serialization;

import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.dubbo.common.serialize.support.dubbo.DubboSerialization;

/**
 * @author ding.lid
 *
 */
public class DubboSerializationTest extends AbstractSerializationPersionFailTest {
    {
        serialization = new DubboSerialization();
    }
    
    /**
     * @desc:DubboSerialization error: java.lang.IllegalAccessError: tried to access class java.util.Arrays$ArrayList from class com.alibaba.dubbo.common.serialize.support.dubbo.Builder$bc6
     * @reason:in writeObject method, the first line is :java.util.Arrays$ArrayList v = (java.util.Arrays$ArrayList)$1; java.util.Arrays$ArrayList is a inter static class ,can not access.
     * @tradeoff: how to resolve：we need change writeObject method, replace the first line with java.util.ArrayList v = new java.util.ArrayList((List)$1) , and in the same time, modify the defaultArg method ,return special construct args for ArrayList ... too ugly to support.  
     */
    @Ignore
    @Test
    public void test_StringList_asListReturn() throws Exception {
        super.test_StringList_asListReturn();
    }

    // FIXME
    @Ignore("StackOverflowError")
    @Test(timeout=3000)
    public void test_LoopReference() throws Exception {}
    
    // FIXME DUBBO-63
    @Test
    public void test_URL_mutable_withType() throws Exception {
        super.test_URL_mutable_withType();
    }
}