/**
 * Resize function without multiple trigger
 * 
 * Usage:
 * $(window).smartresize(function(){  
 *     // code here
 * });
 */
(function($,sr){
    // debouncing function from John Hann
    // http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
    var debounce = function (func, threshold, execAsap) {
      var timeout;

        return function debounced () {
            var obj = this, args = arguments;
            function delayed () {
                if (!execAsap)
                    func.apply(obj, args); 
                timeout = null; 
            }

            if (timeout)
                clearTimeout(timeout);
            else if (execAsap)
                func.apply(obj, args);

            timeout = setTimeout(delayed, threshold || 100); 
        };
    };

    // smartresize 
    jQuery.fn[sr] = function(fn){  return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr); };

})(jQuery,'smartresize');
/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var CURRENT_URL = window.location.href.split('#')[0].split('?')[0],
    $BODY = $('body'),
    $MENU_TOGGLE = $('#menu_toggle'),
    $SIDEBAR_MENU = $('#sidebar-menu'),
    $SIDEBAR_FOOTER = $('.sidebar-footer'),
    $LEFT_COL = $('.left_col'),
    $RIGHT_COL = $('.right_col'),
    $NAV_MENU = $('.nav_menu'),
    $FOOTER = $('footer');

	
	
// Sidebar
function init_sidebar() {
// TODO: This is some kind of easy fix, maybe we can improve this
var setContentHeight = function () {
	// reset height
	var bodyHeight = $BODY.outerHeight(),
		footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
		leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
		contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

	// normalize content
	contentHeight += footerHeight - 58;
	
	$RIGHT_COL.css('min-height', contentHeight);
};

  $SIDEBAR_MENU.find('a').on('click', function(ev) {
        var $li = $(this).parent();
    	 if ($li.is('.active')) {
             $li.removeClass('active active-sm');
             $('ul:first', $li).slideUp(function() {
                 setContentHeight();
             });
         } else {
        	 // prevent closing menu if we are on child menu
        	 $SIDEBAR_MENU.find('li').removeClass('active active-sm');
             if (!$li.children().is('.child_menu')) {
                 //$SIDEBAR_MENU.find('li ul').slideUp();
             }else{
 				if ( $BODY.is( ".nav-sm" ) ){
 					//$SIDEBAR_MENU.find( "li ul" ).slideUp();
 				}
 			}
             $li.addClass('active');//1级
             $li.parent().parent().addClass('active');//2级
             $li.parent().parent().parent().parent().addClass('active');//3级
             $li.parent().parent().parent().parent().parent().parent().addClass('active');//4级
             $li.parent().parent().parent().parent().parent().parent().parent().parent().addClass('active');//5级
             $('ul:first', $li).slideDown(function() {
                 setContentHeight();
             });
             
             $(this).parent().parent().children("li").children("ul").each(function(){
            	 $(this).css("display","none");
             })
        	 $(this).parent().children("ul").css("display","block");
         }
    });

// toggle small or large menu 
$MENU_TOGGLE.on('click', function() {
		console.log('clicked - menu toggle');
		if ($BODY.hasClass('nav-md')) {
			$SIDEBAR_MENU.children("div").children("ul").children("li").each(function(){
				$(this).children("ul").mouseover(function(){
		           	 $(this).css("display","block");
	            }).mouseout(function(){
	            	 $(this).css("display","none"); 
	            })
			})
			$SIDEBAR_MENU.css("overflow-y","inherit");
			$SIDEBAR_MENU.find('li.active ul').hide();
			$SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
		} else {
			$SIDEBAR_MENU.children("div").children("ul").children("li").each(function(){
				$(this).children("ul").unbind("mouseover");
				$(this).children("ul").unbind("mouseout");
			})
			$SIDEBAR_MENU.css("overflow-y","auto");
			$SIDEBAR_MENU.find('li.active-sm ul').show();
			$SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
		}

	$BODY.toggleClass('nav-md nav-sm');

	setContentHeight();

	$('.dataTable').each ( function () { $(this).dataTable().fnDraw(); });
});

	// check active menu
	$SIDEBAR_MENU.find('a[href="' + CURRENT_URL + '"]').parent('li').addClass('current-page');

	$SIDEBAR_MENU.find('a').filter(function () {
		return this.href == CURRENT_URL;
	}).parent('li').addClass('current-page').parents('ul').slideDown(function() {
		setContentHeight();
	}).parent().addClass('active');

	// recompute content when resizing
	$(window).smartresize(function(){  
		setContentHeight();
	});

	setContentHeight();

	// fixed sidebar
	if ($.fn.mCustomScrollbar) {
		$('.menu_fixed').mCustomScrollbar({
			autoHideScrollbar: true,
			theme: 'minimal',
			mouseWheel:{ preventDefault: true }
		});
	}
};
// /Sidebar
$(document).ready(function() {
	init_sidebar();
});	

function idCardNumber(UUserCard){
	//获取输入身份证号码 
	var sex;
	var obj = new Object();
	if(UUserCard != null && UUserCard != '' && UUserCard.length == 18){
		//获取出生日期 
		var birthday = UUserCard.substring(6, 10) + "-" + UUserCard.substring(10, 12) + "-" + UUserCard.substring(12, 14); 
		//获取性别 
		if (parseInt(UUserCard.substr(16, 1)) % 2 == 1) { 
		 	sex = 1;
		} else {
		 	sex = 0;
		} 
		//获取年龄 
		var myDate = new Date(); 
		var month = myDate.getMonth() + 1; 
		var day = myDate.getDate();
	
		var age = myDate.getFullYear() - UUserCard.substring(6, 10) - 1; 
		if (UUserCard.substring(10, 12) < month || UUserCard.substring(10, 12) == month && UUserCard.substring(12, 14) <= day) { 
			age++; 
		}
		obj['birthday'] = birthday;
		obj['age'] = age;
		obj['sex'] = sex;
		return obj;
	}
}