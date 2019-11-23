/**
 * bootStrap 自定义表单验证
 */
$(function () {
    $('form').bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
        	required: {
        		selector:'[data-required="required"]',
        		validators: {
        			notEmpty: {
                        message: '该字段不能为空'
                    }
                }
            },
        	password: {
        		selector:'[data-password="password"]',
        		validators: {
        			notEmpty: {
                        message: '密码不能为空'
                    },
                    stringLength: {
                		min:6,
                		max:20,
                        message: '密码长度只能在6-20之间'
                    }
                }
            },
        	oldPassword: {
        		selector:'[data-oldPassword="oldPassword"]',
        		validators: {
        			notEmpty: {
                        message: '旧密码不能为空'
                    },
                    stringLength: {
                		min:6,
                		max:20,
                        message: '旧密码长度只能在6-20之间'
                    }
                }
            },
        	newPassword: {
        		selector:'[data-newPassword="newPassword"]',
        		validators: {
        			notEmpty: {
                        message: '新密码不能为空'
                    },
                    stringLength: {
                		min:6,
                		max:20,
                        message: '新密码长度只能在6-20之间'
                    }
                }
            },
            repeatPassword: {
        		selector:'[data-repeatPassword="repeatPassword"]',
        		validators: {
        			notEmpty: {
                        message: '重复密码不能为空'
                    },
                    identical: {
                		field: 'ipassword',
                        message: '确认密码与新密码不一致'
                    }
                }
            },
            dataSqlFile: {
        		selector:'[data-dataSqlFile="dataSqlFile"]',
        		validators: {
        			notEmpty: {
                        message: '文件名不能为空'
                    },
                    stringLength: {
                		min:1,
                		max:50,
                        message: '文件名长度只能在1-50之间'
                    },
                    callback: {
                        message: '文件名后缀必须是 .sql',
                        callback : function(value, validator,$field){
                            if(value.lastIndexOf(".sql") < 0){
                                return false;
                            }
                            return true;
                        }
                    }
                }
            },
            dictCodeRepeat: {
        		selector:'[data-dictCodeRepeat="dictCodeRepeat"]',
        		validators: {
        			notEmpty: {
                        message: '字典编号不能为空'
                    },
                    remote: {
                        message: '该编号已存在',
                        url:'list/dict/repeatCode.do',
                        data:{booleanVal:$('input[upDictCode="true"]').attr("upDictCode"),repeatName:$('#repeatDictCode').val()},
                        type: 'POST'
                    }
                }
            },
            levelNameRepeat: {
        		selector:'[data-levelNameRepeat="levelNameRepeat"]',
        		validators: {
        			notEmpty: {
                        message: '名称不能为空'
                    },
                    remote: {
                        message: '名称已存在',
                        url:'member/level/repeatCode.do',
                        data : {
                			'type':$('#levelName').attr('repeatType'),
                			'id' : $('#levelName').attr('mainId')
                		}
                    }
                }
            },
            memberNameRepeat: {
        		selector:'[data-memberNameRepeat="memberNameRepeat"]',
        		validators: {
                    regexp: {
                        regexp: /^[a-zA-Z0-9_-]{4,16}$/,
                        message: '账号由4到16位的字母、数字、下划线或减号组成'
                    },
                    remote: {
                        message: '账号已存在',
                        url:'member/reviewed/repeatName.do',
                        data : {
                			'type':$('#userName').attr('repeatType'),
                			'id' : $('#userName').attr('mainId')
                		}
                    },
                    data : {
            			'type':$('#levelName').attr('repeatType'),
            			'id' : $('#levelName').attr('mainId')
            		}
                }
            },
            ipassword: {
        		selector:'[data-ipassword="ipassword"]',
        		validators: {
        			notEmpty: {
                        message: '密码不能为空'
                    },
                    regexp: {
                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,20}$/,
                        message: '6-20位，可以包含数字、字母、下划线，并且要同时含有数字和字母'
                    }
                }
            },
            email: {
        		selector:'[data-email="email"]',
        		validators: {
        			notEmpty: {
                        message: '邮箱不能为空'
                    },
                    regexp: {
                        regexp: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                        message: '邮箱格式错误'
                    }
                }
            },
            phone: {
        		selector:'[data-phone="phone"]',
        		validators: {
        			notEmpty: {
                        message: '手机号码不能为空'
                    },
                    regexp: {
                        regexp: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/,
                        message: '手机号码格式错误'
                    }
                }
            },
            repeatIdCard: {
        		selector:'[data-repeatIdCard="repeatIdCard"]',
        		validators: {
        			notEmpty: {
                        message: '身份证号不能为空'
                    },
                    remote: {
                        message: '该身份证号已存在',
                        url:'student/repeatCard.do',
                        data:{vercard:$('input[upIdCard="true"]').attr("upIdCard"),repeatName:$('#idCardNumber').val()},
                    	type: 'POST'
                    },
                    regexp: {
                        regexp:  /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/,
                        message: '身份证号格式错误'
                    }
                }
            },
            nameBlank:{
            	selector:'[data-nameBlank="nameBlank"]',
        		validators: {
        			notEmpty: {
                        message: '银行账户不能为空'
                    },
                    regexp: {
                        regexp: /^([1-9]{1})(\d{14}|\d{18})$/,
                        message: '请输入15位或19位银行账户',
                    }
                }
            },
            age: {
        		selector:'[data-age="age"]',///
        		validators: {
        			notEmpty: {
                        message: '年龄不能为空'
                    },
                    regexp: {
                        regexp: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/,
                        message: '错误年龄'
                    }
                }
            },
            trueName: {
        		selector:'[data-trueName="trueName"]',
        		validators: {
        			notEmpty: {
                        message: '姓名不能为空'
                    },
                    regexp: {
                        regexp: /^[\u4E00-\u9FA5A-Za-z]+$/,
                        message: '姓名由汉字或英文组成'
                    }
                }
            },
            fixedTel: {
        		selector:'[data-fixedTel="fixedTel"]',///^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})www\.(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/
        		validators: {
        			notEmpty: {
                        message: '电话或手机号码不能为空'
                    },
                    regexp: {
                        regexp: /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$|(^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$)/,
                        message: '电话或手机号码格式错误'
                    }
                }
            },
            website: {
        		selector:'[data-website="website"]',//
        		validators: {
        			notEmpty: {
                        message: '不能为空'
                    },
                    regexp: {
                        regexp: /^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})www\.(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/,
                        message: '格式错误'
                    }
                }
            },
            textarea: {
                selector:'[data-textarea="textarea"]',
                validators: {
                    notEmpty: {
                        message: '详情内容不能为空'
                    },
                    stringLength: {
                        min: 10,
                        max: 100,
                        message: '详情长度必须在10到100之间'
                    }
                }
            }
        }
    });
});