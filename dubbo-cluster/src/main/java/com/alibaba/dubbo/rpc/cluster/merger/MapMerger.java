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
package com.alibaba.dubbo.rpc.cluster.merger;

import com.alibaba.dubbo.rpc.cluster.Merger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
@SuppressWarnings( "unchecked" )
public class MapMerger<T extends Map> implements Merger<T> {

    public static final String NAME = "map";

    public static final MapMerger INSTANCE = new MapMerger();

    public T merge(T ... items) {

        if ( items.length == 0 ) { return  null; }

        Map result = new HashMap();
        
        for( Map item : items ) {
            if ( item != null ) {
                result.putAll( item );
            }
        }

        return (T)result;
    }

}
