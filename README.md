#### 最新版本

模块|Paging
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/Paging.svg)](https://jitpack.io/#like5188/Paging)

## 功能介绍
1、该项目是基于 kotlin + coroutines + androidx 开发的分页数据源。

2、包括2种数据源：包含数据库、不包含数据库。

3、返回的数据：
```java
    data class Result<ResultType>(
        // 结果报告
        val resultReportFlow: Flow<ResultReport<ResultType>>,
        // 初始化操作
        val initial: () -> Unit,
        // 刷新操作
        val refresh: () -> Unit,
        // 失败重试操作
        val retry: () -> Unit,
        // 往后加载更多，不分页时不用设置
        val loadAfter: (() -> Unit)? = null,
        // 往前加载更多，不分页时不用设置
        val loadBefore: (() -> Unit)? = null
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
    创建数据源继承自[com.like.paging.byDataKeyed.DataKeyedPagingDataSource]、[com.like.paging.byDataKeyed.DataKeyedPagingDbDataSource]或者[com.like.paging.byPageNoKeyed.PageNoKeyedPagingDataSource]、[com.like.paging.byPageNoKeyed.PageNoKeyedPagingDbDataSource]，然后通过 result() 方法获取 [com.like.paging.Result]，再通过它进行相关操作。
```