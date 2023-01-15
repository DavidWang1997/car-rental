# Car Rental Api Document


<a name="overview"></a>
## 概览

### 版本信息
*版本* : 1.0.0


### URI scheme
*域名* : localhost:8080  
*基础路径* : /


### 标签

* CarController : Car Controller
* OrderController : Order Controller
* UserController : User Controller




<a name="paths"></a>
## 资源

<a name="carcontroller_resource"></a>
### CarController
Car Controller


<a name="queryusingpost"></a>
#### 查询指定时间段内的可用车型信息
```
POST /car/query
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*可选*|令牌|string|
|**Body**|**durationVo**  <br>*必填*|durationVo|[DurationVo](#durationvo)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**3**|系统异常|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/car/query
```


###### 请求 header
```
json :
"string"
```


###### 请求 body
```
json :
{
  "endTime" : "2023-01-26 13:00:10",
  "startTime" : "2023-01-25 13:00:10"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="ordercontroller_resource"></a>
### OrderController
Order Controller


<a name="cancelusingpost"></a>
#### 取消订单
```
POST /order/cancel
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*必填*|鉴权token|string|
|**Body**|**orderId**  <br>*必填*|orderId|integer (int32)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**2004**|用户权限不足|无内容|
|**3**|系统异常|无内容|
|**3002**|当前状态不支持取消|无内容|
|**3004**|订单不存在|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/order/cancel
```


###### 请求 header
```
json :
"string"
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="payusingpost"></a>
#### 支付订单
```
POST /order/pay
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*必填*|鉴权token|string|
|**Body**|**orderId**  <br>*必填*|orderId|integer (int32)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**2004**|用户权限不足|无内容|
|**3**|系统异常|无内容|
|**3003**|当前状态不支持付款|无内容|
|**3004**|订单不存在|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/order/pay
```


###### 请求 header
```
json :
"string"
```


###### 请求 body
```
json :
{ }
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="queryusingget"></a>
#### 查询本用户订单
```
GET /order/query
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*必填*|鉴权token|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**3**|系统异常|无内容|


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/order/query
```


###### 请求 header
```
json :
"string"
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="reserveusingpost"></a>
#### 预定
```
POST /order/reserve
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*必填*|鉴权token|string|
|**Query**|**modelId**  <br>*必填*|modelId|integer (int32)|
|**Body**|**durationVo**  <br>*必填*|durationVo|[DurationVo](#durationvo)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**3**|系统异常|无内容|
|**3001**|预定失败|无内容|
|**3005**|车型不存在|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/order/reserve
```


###### 请求 header
```
json :
"string"
```


###### 请求 query
```
json :
{
  "modelId" : 0
}
```


###### 请求 body
```
json :
{
  "endTime" : "2023-01-26 13:00:10",
  "startTime" : "2023-01-25 13:00:10"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="usercontroller_resource"></a>
### UserController
User Controller


<a name="loginusingpost"></a>
#### 用户登录接口
```
POST /user/login
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*可选*|令牌|string|
|**Body**|**query**  <br>*必填*|query|[LoginQuery](#loginquery)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**2001**|用户不存在|无内容|
|**2003**|用户密码错误|无内容|
|**3**|系统异常|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/user/login
```


###### 请求 header
```
json :
"string"
```


###### 请求 body
```
json :
{
  "password" : "asdf1234",
  "userName" : "Tom"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```


<a name="registerusingpost"></a>
#### 用户注册接口
```
POST /user/register
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Header**|**token**  <br>*可选*|令牌|string|
|**Body**|**userVo**  <br>*必填*|userVo|[UserVo](#uservo)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**1**|成功|无内容|
|**1001**|参数校验不通过|无内容|
|**2**|失败|无内容|
|**200**|OK|[Response](#response)|
|**2002**|用户名已存在|无内容|
|**3**|系统异常|无内容|


##### 消耗

* `application/json`


##### 生成

* `*/*`


##### HTTP请求示例

###### 请求 path
```
/user/register
```


###### 请求 header
```
json :
"string"
```


###### 请求 body
```
json :
{
  "email" : "000000000@qq.com",
  "password" : "asdf1234",
  "phone" : "000-0000-0000",
  "userName" : "Tom"
}
```


##### HTTP响应示例

###### 响应 200
```
json :
{
  "code" : 0,
  "data" : "object",
  "msg" : "string"
}
```




<a name="definitions"></a>
## 定义

<a name="durationvo"></a>
### DurationVo
时间段


|名称|说明|类型|
|---|---|---|
|**endTime**  <br>*必填*|预定结束时间  <br>**样例** : `"2023-01-26 13:00:10"`|string (date-time)|
|**startTime**  <br>*必填*|预定开始时间  <br>**样例** : `"2023-01-25 13:00:10"`|string (date-time)|


<a name="loginquery"></a>
### LoginQuery

|名称|说明|类型|
|---|---|---|
|**password**  <br>*必填*|密码  <br>**样例** : `"asdf1234"`|string|
|**userName**  <br>*必填*|用户名  <br>**样例** : `"Tom"`|string|


<a name="response"></a>
### Response

|名称|说明|类型|
|---|---|---|
|**code**  <br>*可选*|**样例** : `0`|integer (int32)|
|**data**  <br>*可选*|**样例** : `"object"`|object|
|**msg**  <br>*可选*|**样例** : `"string"`|string|


<a name="uservo"></a>
### UserVo
用户注册信息


|名称|说明|类型|
|---|---|---|
|**email**  <br>*可选*|电子邮箱  <br>**样例** : `"000000000@qq.com"`|string|
|**password**  <br>*必填*|密码  <br>**样例** : `"asdf1234"`|string|
|**phone**  <br>*可选*|电话号码  <br>**样例** : `"000-0000-0000"`|string|
|**userName**  <br>*必填*|用户名  <br>**样例** : `"Tom"`|string|





