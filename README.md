# CurrentTask
我的的任务
```
小程序:
	1. 微信登录
	2. 分享
	3. 支付
```
## 微信小程序登录
微信小程序提供一个`wx.login`接口,通过这个接口我们就可以获取一个`code`具体使用流程如下：
---
![登录流图](登录流程图.jpg)


[UnionID 机制说明](https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html)
> 为了使其他的微服务服务的无状态化，这里使用的`网关(Spring Cloud Zuul)`进行登录的处理

当登录成功后，我们通过下面的接口将数据发送这后台保存，`wx.getUserInfo`接口，通过该api调用我们可以获取一下信息:

以下字段都是不可信的信息，登录状态的确定要根据
{"session_key":"DcCLjxQEL4KCSGyLJqvyig==","openid":"obght5f5SkdacqQz-VBQygTj6AYk"}


字段名|字段含义
:-|:-
avatarUrl| 微信头像
city | 所在城市
country|所在国家
gender| 用户性别, 1 代表 男
language | 微信所用语言
nickName | 微信昵称
province | 户籍所在地

