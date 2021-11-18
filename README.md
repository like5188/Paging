#### 最新版本

模块|Paging
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/Paging.svg)](https://jitpack.io/#like5188/Paging)

## 功能介绍
1、该项目是基于 kotlin + coroutines + androidx 开发的分页数据源。

2、返回的数据：
```java
/**
 * @param flow              数据流。在每次调用[Flow.collect]来触发请求之前都需要调用[setRequestType]设置请求类型
 * @param setRequestType    设置请求类型
 */
data class PagingResult<ResultType>(
    var flow: Flow<ResultType>,
    val setRequestType: (RequestType) -> Unit,
)
```

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    dependencies {
        // coroutines
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:版本号'
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:版本号'

        implementation 'com.github.like5188:Paging:版本号'
    }
```

2、使用
```java
    1、创建数据源继承自[com.like.paging.dataSource.byDataKeyed.DataKeyedPagingDataSource]、[com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource]，然后通过 pagingResult() 方法获取 [com.like.paging.PagingResult]，再通过它进行相关操作。

    2、如果要使用数据库，可以通过[com.like.paging.dbHelper.IPagingDbHelper]、[com.like.paging.dbHelper.IDbHelper]工具类来辅助实现。
```