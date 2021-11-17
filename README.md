#### 最新版本

模块|Paging
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/Paging.svg)](https://jitpack.io/#like5188/Paging)

## 功能介绍
1、该项目是基于 kotlin + coroutines + androidx 开发的分页数据源。

2、返回的数据：
```java
    data class Result<ResultType>(
        var flow: Flow<ResultType>,
        val requestType: () -> RequestType,
        val initial: () -> Unit,
        val refresh: () -> Unit,
        val after: () -> Unit,
        val before: () -> Unit
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
    1、创建数据源继承自[com.like.paging.dataSource.byDataKeyed.DataKeyedPagingDataSource]、[com.like.paging.dataSource.byPageNoKeyed.PageNoKeyedPagingDataSource]，然后通过 result() 方法获取 [com.like.paging.Result]，再通过它进行相关操作。

    2、如果要使用数据库，可以通过[com.like.paging.dbHelper.IPagingDbHelper]、[com.like.paging.dbHelper.IDbHelper]工具类来辅助实现。
```