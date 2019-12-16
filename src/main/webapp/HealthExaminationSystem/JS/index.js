/*导航栏*/
$(".loginBefore").hide()
$("#medicalRecord").hide();

/*退出登录*/
function fnLogOut(){
    localStorage.setItem('jsessionid','');
    $(".loginBefore").hide()
    $(".loginAfter").show()  
}

/*-------------------------------------------------*/

/*轮播图*/
$("#myCarousel").carousel('cycle');
$("#myCarousel-second").carousel('cycle');

$('.btn[type="submit"]').click(function(e) {
    e.preventDefault();
});
var delay = 5000;
var itemCount = 5;
var area = $("#roll");
var speed = 3;
area.html(area.html() + area.html());

setInterval(async function() {
    await area.animate( {scrollTop:area.scrollTop() + area.height()}, 1000 / speed);
    if(area.scrollTop() >= area.height() * itemCount) {
        area.scrollTop(0);
    }
}, delay);

/*-------------------------------------------------*/

/*预约*/
$("#appointment").hide()
$(document).ready(function(){
    $(".appointment").click(function(){
        $("#appointment").show();
    });
    $(".iconfont").click(function(){
        $("#appointment").hide();
    });
    $("#startTime").click(function(){
        $(".dropdown-menu").hide();
    });
});
function fnAppointment() {
    var age = document.getElementById("age")
    var time = document.getElementById("startTime")
    var oError = document.getElementById("error_box")
    var isError = true;
    
    if (age.value.length > 3 || age.value.length < 0) {
        oError.innerHTML = "年龄有误"
        isError = false;
        return;
    }
    if (time.value.length == 3) {
        oError.innerHTML = "请输入时间"
        isError = false;
        return;
    }
    window.alert("预约成功")
    $("#appointment").hide()
}

/*-------------------------------------------------*/

/*登录*/
$("#login").hide()
$(document).ready(function(){
    $(".login").click(function(){
        $("#login").show();
    });
    $(".iconfont").click(function(){
        $("#login").hide();
    });
});
$(document).ready(function(){
    $(".register").click(function(){
        $("#register").show();
    });
});

function fnLogin() {
    var name = document.getElementById("name")
    var pass = document.getElementById("pass")
    var oError = document.getElementById("error_box")
    var isError = true;
  
    if(!/^1[34578]\d{9}$/.test(name.value)){
        oError.innerHTML = "账号输入不正确"
        isError = false;
        return;
    }
    if (pass.value.length > 11 || pass.value.length < 6) {
        oError.innerHTML = "密码请输入6-11位字符"
        isError = false;
        return;
    }
    //这里定义params,这里的密码不需要进行加密处理，因为我后台自己搞了加密解密比对方法。
    var params = {
        phone:name.value,
        password:pass.value
    }
    //在这里使用Ajax请求
    $.postExtend(loginUrl, params, function(data){
        console.log(data.data);
        if(data.code == 0){
            //使用sessionStorage存储jsession数据
            var dataParams = JSON.parse(data.data);
            console.log("登录数据 Jsessionid = " + dataParams.jsessionid);
            localStorage .setItem('jsessionid',dataParams.jsessionid);
            //这些操作可以使用一个函数总结起来。
            $("#login").hide();
            $(".loginBefore").show();
            $(".loginAfter").hide();
            //这里可以弄一个提醒登录成功，然后等 一秒或者点击登录成功得时候再跳页面。
            console.log("登录成功！")    
            //仅用于测试
            window.alert("登录成功！");
        }else{
            //这里也需要跳登录失败。
            console.log("登录失败！")
            //仅用于测试
            window.alert("登录失败！");
            //在登录框上显示错误信息
            oError.innerHTML = data.message;
        }
    });

    
}

/*-------------------------------------------------*/

/*注册*/
$("#register").hide()
$(document).ready(function(){
    $(".register").click(function(){
        $("#register").show();
    });
    $(".iconfont").click(function(){
        $("#register").hide();
    });
    $(".login").click(function(){
        $("#login").show();
        $("#register").hide();
    });
});

function fnregister() {
    var phone = document.getElementById("tel")
    var pass = document.getElementById("pass1")
    var oError = document.getElementById("error_box1")
    var isError = true;

/*  这里得校验方式需要修改修改
    后台得校验方式为：账号需要电话号码、密码需要6-11位字母与数字组成*/
    if(phone != /^(((1[3456789][0-9]{1})|(15[0-9]{1}))+\d{8})$/){
        oError.innerHTML = "账号输入不正确"
        isError = false;
        return;
    }

    if (pass.value.length > 11 || pass.value.length < 6) {
        oError.innerHTML = "密码请输入6-11位字符"
        isError = false;
        return;
    }

    //这里定义params,这里的密码不需要进行加密处理，因为我后台自己搞了加密解密比对方法。
    var params = {
        //gender:(".usex").val,
        //前面取值不对，现在使用测试值调试接口
        gender:"Male",
        password:pass.value,
        phone:phone.value
    }
    //在这里使用Ajax请求
    $.postExtend(registerUrl, params, function(data){
        if(data.code == 0){
            var dataParams = JSON.parse(data.data);
            console.log("注册数据 Jsessionid = " + dataParams.jsessionid);
            localStorage .setItem('jsessionid',dataParams.jsessionid);
            //隐藏部分操作
           $("#register").hide();
           $(".loginBefore").show();
           $(".right").hide();
           $(".loginAfter").hide();  
           window.alert("注册成功！")
        }else{
            window.alert("登录失败！" + data.message)
        }
    });

}

/*-------------------------------------------------*/

/*修改信息*/
$("#changeInformation").hide();
$(document).ready(function(){
    $(".changeInformation").click(function(){
        $("#changeInformation").show();
    });
    $(".iconfont").click(function(){
        $("#changeInformation").hide();
    });
});

/*-------------------------------------------------*/

/*图片容器*/
$(function() {
    $('#toright').hover(function() {
        $("#toleft").hide()
    }, function() {
        $("#toleft").show()
    })
    $('#toleft').hover(function() {
        $("#toright").hide()
    }, function() {
        $("#toright").show()
    })
})

var t;
var index = 0;
//自动播放
t = setInterval(play, 3000)

function play() {
    index++;
    if (index > 4) {
        index = 0
    }
    $("#lunbobox ul li").eq(index).css({
        "background": "#999",
        "border": "1px solid #ffffff"
    }).siblings().css({
        "background": "#cccccc",
        "border": ""
    })

    $(".lunbo a ").eq(index).fadeIn(1000).siblings().fadeOut(1000);
};

$("#lunbobox ul li").click(function() {//点击鼠标 图片切换

    //添加 移除样式
    //$(this).addClass("lito").siblings().removeClass("lito"); //给当前鼠标移动到的li增加样式 且其余兄弟元素移除样式   可以在样式中 用hover 来对li 实现
    $(this).css({
        "background": "#999",
        "border": "1px solid #ffffff"
    }).siblings().css({
        "background": "#cccccc"
    })
    var index = $(this).index(); //获取索引 图片索引与按钮的索引是一一对应的

    $(".lunbo a ").eq(index).fadeIn(1000).siblings().fadeOut(1000); // siblings  找到 兄弟节点(不包括自己）
});

$("#toleft").click(function() {//上一张、下一张切换
    index--;
    if (index <= 0) //判断index<0的情况为：开始点击#toright  index=0时  再点击 #toleft 为负数了 会出错
    {
        index = 4
    }
    console.log(index);
    $("#lunbobox ul li").eq(index).css({
        "background": "#999",
        "border": "1px solid #ffffff"
    }).siblings().css({
        "background": "#cccccc"
    })

    $(".lunbo a ").eq(index).fadeIn(1000).siblings().fadeOut(1000); // siblings  找到 兄弟节点(不包括自己）必须要写，$("#imgbox a ")获取到的是一个数组集合 。所以可以用index来控制切换
}); 

$("#toright").click(function() {
    index++;
    if (index > 4) {
        index = 0
    }
    console.log(index);
    $(this).css({
        "opacity": "0.5"
    })
    $("#lunbobox ul li").eq(index).css({
        "background": "#999",
        "border": "1px solid #ffffff"
    }).siblings().css({
        "background": "#cccccc"
    })
    $(".lunbo a ").eq(index).fadeIn(1000).siblings().fadeOut(1000); // siblings  找到 兄弟节点(不包括自己）
});
$("#toleft,#toright").hover(function() {
        $(this).css({
            "color": "black"
        })
    },
    function() {
        $(this).css({
            "opacity": "0.3",
            "color": ""
        })
    })

$("#lunbobox ul li,.lunbo a img,#toright,#toleft ").hover(//鼠标移进  移出
    function() {//鼠标移进
        $('#toright,#toleft').show()
        clearInterval(t);

    },
    function() {//鼠标移开
        t = setInterval(play, 3000)

        function play() {
            index++;
            if (index > 4) {
                index = 0
            }
            $("#lunbobox ul li").eq(index).css({
                "background": "#999",
                "border": "1px solid #ffffff"
            }).siblings().css({
                "background": "#cccccc"
            })
            $(".lunbo a ").eq(index).fadeIn(1000).siblings().fadeOut(1000);
        }
})

/*-------------------------------------------------*/

/*套餐信息*/
$(".news").hide();
$(document).ready(function(){
    $("#sideNavUl").mouseover(function (){
        $(".news").show();
    }).mouseout(function (){
        $(".news").hide();
    });
});


/*-------------------------------------------------*/

/*体检记录*/
$(function () {
    $("#btn").click(function () {
        var $sea=$('#txt').val();
        //先隐藏全部，再把符合筛选条件的值显示
        $('table tbody tr').hide().filter(':contains('+$sea+')').show();
    });
});
$(document).ready(function(){
    $(".medicalRecord").click(function(){
        $("#medicalRecord").show();
        $("#myCarousel").hide()
        $("#firstScreen").hide()
        $("#introduce").hide()
        $("#contact").hide()
    });
    $(".firstScreen").click(function(){
        $("#medicalRecord").hide();
        $("#myCarousel").show()
        $("#firstScreen").show()
        $("#introduce").show()
        $("#contact").show()
    });
    $(".main").click(function(){
        $("#medicalRecord").hide();
        $("#myCarousel").show()
        $("#firstScreen").show()
        $("#introduce").show()
        $("#contact").show()
    });
    $("#txt").click(function(){
        $(".ico").hide()
    });
});

//套餐数据，需要将以下数据添加到静态页面中(其中 showImg 使用，myUrl + /service/rest/tk.File/fc507b71dab54d678ca610c20655a7ea)
/*[
    {\"suitableSex\":\"两者都\",\"ageMin\":16,\"ageMax\":100,\"createTime\":\"2019-11-23 13:06\",\"meaning\":\"通过仪器测量人体基本健康指标。例如：血压是否正常，有无体重偏低、超重或肥胖。\",\"medicalName\":\"一般检查A\",\"id\":\"fc073ea338f542b6a17d4ecdb5023e36\",\"items\":\"体重\",\"showImg\":\"b8c80b4f26cd4c1599644b19adc28db4\",\"isShow\":null},
    {\"suitableSex\":\"两者都\",\"ageMin\":1,\"ageMax\":111,\"createTime\":\"2019-11-23 19:44\",\"meaning\":\"撒旦飞洒地方技术的技法卢卡斯京东方考虑\",\"medicalName\":\"一般检查B\",\"id\":\"edf09ad6bd654055911437be7a24db60\",\"items\":\"默认项目\",\"showImg\":\"fc507b71dab54d678ca610c20655a7ea\",\"isShow\":null}
] */

//进入网页调用该接口获取套餐信息
$(
    function(){
        //获取H5存储的session值
        var jessionid= localStorage.getItem('jsessionid');
        console.log(jessionid)
        var params = {
            jessionid:jessionid
        }
        //判断用户是否登录
        $.postExtend(getUserBySessionIdUrl,params,function(data){
            console.log(data);
            if(data.code == 0){
                var dataParams = JSON.parse(data.data);
                //修改H5存储的数据
                localStorage .setItem('jsessionid',dataParams.jsessionid);
                //处理登录的样式内容（隐藏登录注册内容）
                //初始页面时没有该内容，如何进行隐藏。
                // window.onload = function(){
                //     $("#login").hide();
                //     $(".loginBefore").show();
                //     $(".loginAfter").hide();
                // }
                $(document).ready(function(){
                    $("#login").hide();
                    $(".loginBefore").show();
                    $(".loginAfter").hide();
                  });
            }else if(data.code == 7 ){
                //修改H5存储数据
                localStorage .setItem('jsessionid',null);
                window.alert(data.message);
            }else{
                //修改H5存储数据
                sessionStorage.setItem('jsessionid',null);
            }
        })
        

        $.postExtend(getAllMedicalItemsUrl,{},function(data){
            if(data.code == 0 ){
                var json = JSON.parse(data.data);
                json.forEach(function(itemCount,index){
                    console.log(itemCount);
                    /*加载导航栏数据*/
                    $("#sideNavUl").append("<li class='combo'>"+itemCount.medicalName+"<span class='iconfont fr'>&#xe63d;</span> </li>");
                    $("#content1").append("<div id='new"+index+"' class='panel'>"+itemCount.medicalName+"</div>");
                    //加载图片(如果后台没有给出图片，你就要使用默认图片)
                    $("#lunboDiv").append("<a href='javascript:void(0)'><img src='"+myUrl+"/service/rest/tk.File/"+itemCount.showImg+"'></a>");
                    //添加active
                    onload = function () {
                        function removeActiveClass(node) {
                            node.className = '';
                        }

                        document.querySelector('ul[id=sideNavUl]').onmouseover = function (e) {
                            Array.prototype.forEach.call(document.querySelectorAll('ul[id=sideNavUl] > li'), removeActiveClass);
                            var target = e.target;
                            target.className = 'li-active';
                        }
                    }
                    /*对应显示
                    $(window).on('load',function(){
                        $("#content1>div").eq(0).show().siblings().hide();
                    })*/
                    $("#sideNavUl").find("li").mouseover(function () {
                        $(this).addClass("li-active").siblings().removeClass("li-active");
                        var index = $(this).index();
                        $("#content1>div").eq(index).show().siblings().hide();

                    })
                });
            }else{
                console.log("获取数据失败！");
            }
        })
    }
)
