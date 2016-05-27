# 安装与初始化

## 安装说明

### 服务端安装

#### 推荐配置

* CPU: Intel Xeon 双核 2.0 GHz 或更快.

* 内存: 2 GiB 或更大.

* 操作系统: Ubuntu 16.04 LTS 或 CentOS 7.2.

* 硬盘剩余空间: 10 GB (9.31 GiB) 或更大.

* 网络: 基于交换式以太网的 100 Mbps LAN 或更快.

#### 其它可选配置 (无支持服务)

* 操作系统: Windows Server 2008 R2 或更高版本.

#### 准备运行环境

##### 1. 安装操作系统

参考官方文档.

##### 2. 安装 Python 2.7

* Ubuntu 16.04 LTS 或 CentOS 7.2: 无需手动操作.

* Windows: 参考 Python 官方文档.

##### 3. 安装 Python 运行库

服务器程序依赖 `flask`、`flask-sqlalchemy` 和 `mysql-connector`.

* Ubuntu 16.04 LTS:
```
apt install python-flask python-sqlalchemy python-mysql.connector
```

* CentOS 7.2:
```
yum install python-flask python-sqlalchemy mysql-connector-python
```

* Windows: 参考 Python 官方文档.

#### 安装服务端程序

* Ubuntu 16.04 LTS 或 CentOS 7.2:
```
./mart-server-install.sh
```

* Windows: 自行编写 Windows 服务或添加计划任务使之自动启动.

### 数据库安装

### 客户端安装

#### 推荐配置

* CPU: Intel 双核 2.0 GHz 或更快、AMD 双核 2.0 GHz 或更快.

* 内存: 2 GiB 或更大.

* 操作系统: Windows 7 或更高版本、Ubuntu 16.04 LTS 或更高版本.

* 硬盘剩余空间: 5 GB (4.16 GiB) 或更大.

* 网络: 10 Mbps LAN 或更快.

#### 其它可选配置 (无支持服务)

* 网络: 54 Mbps WLAN 或更快.

#### 准备运行环境

1. 安装操作系统.

2. 安装 Java Runtime Environment 8.

#### 安装客户端程序

* Ubuntu 16.04 LTS 或 CentOS 7.2: 创建一个 shell 脚本并添加可执行权限:
```
#! /bin/bash
java /path/to/client
```
也可以创建一个 `.desktop` 启动器.

* Windows:
创建一个 cmd 脚本:
```
/path/to/java /path/to/client
```
也可以创建一个快捷方式.

## 初始化配置

### 数据库配置

### 服务端配置

把 /usr/local/mart/config.example 复制到 /etc/mart/config, 然后按需修改服务器及数据库配置.

### 客户端配置
