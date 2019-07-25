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

### 通过以下代码即可测试后端登录逻辑

> 为了使其他的微服务服务的无状态化，这里使用的`网关(Spring Cloud Zuul)`进行登录的处理
> 
```java
	application.yml
	
	weixin.appid=wxd894479b4519d1e3
	weixin.appsecret=cb10f34eaa920b1de50be2e0f4048d6e
	weixin.grant_type=authorization_code
	weixin.login_api=https://api.weixin.qq.com/sns/jscode2session?appid=${weixin.appid}&secret=${weixin.appsecret}&js_code=%s&grant_type=${weixin.grant_type}
	
	//---------------------------------------------------------------

	@Value("${weixin.login_api}")
    private String loginApiRAW;

    @GetMapping("/login")
    public String check(@RequestParam("code") String code){
        RestTemplate restTemplate = new RestTemplate();
        String loginApi = String.format(loginApiRAW, code);
        String body = restTemplate.getForEntity(loginApi, String.class).getBody();
        System.out.println("body = " + body);
        return body;
    }
```
> 调用微信api，可得到类似下面的信息：

```json
{
	"session_key":"DcCLjxQEL4KCSGyLJqvyig==",
	"openid":"obght5f5SkdacqQz-VBQygTj6AYk"
}
```

当登录成功后，我们通过下面的接口将数据发送这后台保存，`wx.getUserInfo`接口，通过该api调用我们可以获取一下信息:
以下字段都是不可信的信息，登录状态的确定要根据

字段名|字段含义
:-|:-
avatarUrl| 微信头像
city | 所在城市
country|所在国家
gender| 用户性别, 1 代表 男
language | 微信所用语言
nickName | 微信昵称
province | 户籍所在地

## 微信小程序分享

小程序分享有两种方式：
* 页面右上方三个点，但是这里要通过`wx.showShareMenu`开启三个点的转发功能或在js逻辑页面中实现`onShareAppMessage`回调方法，也可以通过`wx.hideShareMenu`关闭该功能
* 另外就是通过`button`组件，设置`open-type="share"`就可以实现

转发时，我们可以在onShareAppMessage回调方法中实现我们的分享转发逻辑，
[官方说明](https://developers.weixin.qq.com/miniprogram/dev/reference/api/Page.html#onShareAppMessage-Object-object)
![](2019-07-25_150537.png)

>微信小程序暂时未开放可以转发到朋友圈的接口，但是基于我们产品的功能，可以换一种方式实现。
>例如：在阅读报告中提供一个生成一个带二维码的图片报告。