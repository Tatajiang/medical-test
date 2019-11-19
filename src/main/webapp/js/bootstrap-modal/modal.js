(function($) {
  window.Modal = function() {
    var html = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel" data-backdrop="false">' +
      '<div class="modal-dialog modal-sm">' +
      '<div class="modal-content">' +
      '<div class="modal-header">' +
      '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
      '<h4 class="modal-title" id="modalLabel"><i class="[Icon]" style="margin-right:10px;"></i>[Title]</h4>' +
      '</div>' +
      '<div class="modal-body">' +
      '<p style="margin:0 0 0px;">[Message]</p>' +
      '</div>' +
      '<div class="modal-footer">' +
      '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
      '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>';
    var dialogdHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel" data-backdrop="false">' +
      '<div class="modal-dialog" style="width:[Width];">' +
      '<div class="modal-content">' +
      '<div class="modal-header">' +
      '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
      '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
      '</div>' +
      '<div class="modal-body" style="height:[Height];overflow-y:[Overflow];">' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>';
    var dialogButtonHtml = '<div id="[Id]" class="modal fade" role="dialog" aria-labelledby="modalLabel" data-backdrop="false">' +
    '<div class="modal-dialog" style="width:[Width];">' +
    '<div class="modal-content">' +
    '<div class="modal-header">' +
    '<button  type="button" data-dismiss="modal" aria-hidden="true" class="btn btn-default" style="float: right;margin-top: -5px;">关闭</button><button  type="button" id="[Buttonid]" class="btn ok btn-success" style="float: right;margin-top: -5px;">确认</button>' +
    '<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
    '</div>' +
    '<div class="modal-body" style="height:[Height];overflow-y:[Overflow];">' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>';
    var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
    var generateId = function() {
      var date = new Date();
      return 'mdl' + date.valueOf();
    }
    var init = function(options) {
      options = $.extend({}, {
        title: "操作提示",
        message: "提示内容",
        icon:'',
        btnok: "确定",
        btncl: "取消",
        width: 200,
        auto: false
      }, options || {});
      var modalId = generateId();
      var content = html.replace(reg, function(node, key) {
        return {
          Id: modalId,
          Title: options.title,
          Icon: options.icon,
          Message: options.message,
          BtnOk: options.btnok,
          BtnCancel: options.btncl
        }[key];
      });
      $('body').append(content);
      $('#' + modalId).modal({
        width: options.width
      });
      $('#' + modalId).on('hide.bs.modal', function(e) {
        $('body').find('#' + modalId).remove();
      });
      return modalId;
    }

    return {
      alert: function(options) {
        if (typeof options == 'string') {
          options = {
            message: options
          };
        }
        var id = init(options);
        var modal = $('#' + id);
        modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
        modal.find('.cancel').hide();

        return {
          id: id,
          on: function(callback) {
            if (callback && callback instanceof Function) {
              modal.find('.ok').click(function() {
                callback(true);
              });
            }
          },
          hide: function(callback) {
            if (callback && callback instanceof Function) {
              modal.on('hide.bs.modal', function(e) {
                callback(e);
              });
            }
          }
        };
      },
      confirm: function(options) {
        var id = init(options);
        var modal = $('#' + id);
        modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
        modal.find('.cancel').show();
        return {
          id: id,
          on: function(callback) {
            if (callback && callback instanceof Function) {
              modal.find('.ok').click(function() {
                callback(true);
              });
              modal.find('.cancel').click(function() {
                callback(false);
              });
            }
          },
          hide: function(callback) {
            if (callback && callback instanceof Function) {
              modal.on('hide.bs.modal', function(e) {
                callback(e);
              });
            }
          }
        };
      },
      dialog: function(options) {
        options = $.extend({}, {
          onReady: function() {},
          onShown: function(e) {}
        }, options || {});
        //var modalId = generateId();
        
        var content = dialogdHtml.replace(reg, function(node, key) {
          return {
            Id: options.id,
            Title: options.title,
            Width:$(window).width() > 1280 ? options.width+'px' : (options.width <= 1000 ? options.width+'px' : 'auto'),
            Height:options.height,
            Overflow:options.overflow
          }[key];
        });
        $('body').append(content);
        var target = $('#' + options.id);
        target.find('.modal-body').load(options.url);
        if (options.onReady())
          options.onReady.call(target);
        target.modal();
        target.on('shown.bs.modal', function(e) {
          if (options.onReady(e))
            options.onReady.call(target, e);
        });
        target.on('hide.bs.modal', function(e) {
          $('body').find(target).remove();
        });
      },
      dialogButton: function(options) {
          options = $.extend({}, {
            onReady: function() {},
            onShown: function(e) {}
          }, options || {});
          //var modalId = generateId();
          
          var content = dialogButtonHtml.replace(reg, function(node, key) {
            return {
              Id: options.id,
              Title: options.title,
              Width:$(window).width() > 1280 ? options.width+'px' : (options.width <= 1000 ? options.width+'px' : 'auto'),
              Height:options.height,
              Overflow:options.overflow,
              Buttonid: options.buttonid
            }[key];
          });
          $('body').append(content);
          var target = $('#' + options.id);
          target.find('.modal-body').load(options.url);
          if (options.onReady())
            options.onReady.call(target);
          target.modal();
          target.on('shown.bs.modal', function(e) {
            if (options.onReady(e))
              options.onReady.call(target, e);
          });
          target.on('hide.bs.modal', function(e) {
            $('body').find(target).remove();
          });
        }
    }
  }();
})(jQuery);