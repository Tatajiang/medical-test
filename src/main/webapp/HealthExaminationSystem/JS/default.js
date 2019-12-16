//用于本地测试
//var myUrl = "http://localhost:8080/medical_test_war_exploded";
//用于服务器测试
var myUrl = "http://139.9.204.172";


//登录
var loginUrl = myUrl + "/service/rest/service.NoLoginService/collection/login";
//注册
var registerUrl = myUrl + "/service/rest/service.NoLoginService/collection/register";
//获取所有套餐记录
var getAllMedicalItemsUrl = myUrl + "/service/rest/service.NoLoginService/collection/getAllMedicalItems";
//通过Jsessionid获取用户信息判断用户是否登录
var  getUserBySessionIdUrl = myUrl + "/service/rest/service.NoLoginService/collection/getUserBySessionId";
