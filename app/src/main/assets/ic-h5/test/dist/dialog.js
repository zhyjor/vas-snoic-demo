/**
 * zhyjor
 *
 * https://github.com/zhyjor/widget-dialog
 *
 */
;
(function (root, factory) {
    //amd
    if (typeof define === 'function' && define.amd) {
        define(["jquery"], factory);
    } else if (typeof exports === 'object') { //umd
        module.exports = factory();
    } else {
        //bower
        root.Dialog = factory(jQuery);
    }
})(this, function ($) {
    $.fn.Dialog = function (settings) {
        var list = [];
        $(this).each(function () {
            var dialog = new Dialog();
            var options = $.extend({
                trigger: $(this)
            }, settings);
            dialog.init(options);
            list.push(dialog);
        });
        return list;
    };
    $.Dialog = function (settings) {
        if (settings.type === "alert") {
            var alert = new Dialog();
            var html = '<div class="ui-alert-title">' + settings.content + '</div>';
            var action = '';
            var className = 'ui-alert';
            if (settings.button) {
                if (typeof settings.button == 'boolean') {
                    settings.button = '确定';
                }
                ;
                action = '<p class="ui-dialog-action"><button class="ui-alert-submit  js-dialog-close">' + settings.button + '</button></p>';
            } else if (!settings.timer) {
                // settings.timer = 3000;
                className += ' ui-alert-tip';
            }
            html += action;
            var alertOptions = $.extend({
                target: html,
                animate: true,
                show: true,
                mask: true,
                className: className,
                afterHide: function (c) {
                    this.dispose();
                    settings.callback && settings.callback();
                }
            }, settings);
            alert.init(alertOptions);
            if (settings.timer) {
                setTimeout(function () {
                    alert.dispose();
                    settings.callback && settings.callback();
                }, settings.timer);
            }
            alert.touch(alert.mask, function () {
                if (typeof settings.button == 'undefined') {
                    alert.hide();
                    settings.callback && settings.callback();
                }
            });
        }
        if (settings.type === "confirm") {
            var dialog = new Dialog();
            var html = '<div class="ui-confirm-title">' + settings.content + '</div>';
            var action = '';
            if (!settings.buttons) {
                settings.buttons = [{
                    'yes': '确定'
                }, {
                    'no': '取消'
                }];
            }
            ;
            var btnstr = '';
            for (var i = 0, l = settings.buttons.length; i < l; i++) {
                var item = settings.buttons[i];
                if (item.yes) {
                    btnstr += '<td><button class="ui-confirm-submit " data-type="yes">' + item.yes + '</button></td>';
                }
                if (item.no) {
                    btnstr += '<td><button class="ui-confirm-no" data-type="no">' + item.no + '</button></td>';
                }
                if (item.close) {
                    btnstr += '<td><button class="ui-confirm-close js-dialog-close" data-type="close">' + item.close + '</button></td>';
                }
            }
            action = '<table class="ui-dialog-action"><tr>' + btnstr + '</tr></table>';
            if (settings.position == "bottom") {
                html = action + html;
            } else {
                html += action;
            }
            var options = $.extend({
                target: html,
                animate: true,
                show: true,
                fixed: true,
                mask: true,
                className: "ui-alert",
                afterHide: function (c) {
                    this.dispose();
                },
                beforeShow: function (c) {
                    dialog.touch($('.ui-confirm-submit', c), function () {
                        settings.callback && settings.callback.call(dialog, 'yes', c);
                    });
                    dialog.touch($('.ui-confirm-no', c), function () {
                        settings.callback && settings.callback.call(dialog, 'no', c);
                    });
                    dialog.touch($('.ui-confirm-close', c), function () {
                        settings.callback && settings.callback.call(dialog, 'close', c);
                    });
                }
            }, settings);
            dialog.init(options);
        }
        if (settings.type === "declare") {
            var dialog = new Dialog();
            var html = ' <div class="ui-declare-head">' + settings.bar + '</div><div class="ui-declare-title">' + settings.content + '</div>';
            var action = '';
            if (!settings.buttons) {
                settings.buttons = [{
                    'yes': '同意'
                }, {
                    'no': '取消'
                }];
            }
            ;
            var btnstr = '';
            for (var i = 0, l = settings.buttons.length; i < l; i++) {
                var item = settings.buttons[i];
                if (item.yes) {
                    btnstr += '<td><button class="ui-declare-submit " data-type="yes">' + item.yes + '</button></td>';
                }
                if (item.no) {
                    btnstr += '<td><button class="ui-declare-no" data-type="no">' + item.no + '</button></td>';
                }
                if (item.close) {
                    btnstr += '<td><button class="ui-declare-close js-dialog-close" data-type="close">' + item.close + '</button></td>';
                }
            }
            action = '<table class="ui-dialog-action"><tr>' + btnstr + '</tr></table>';
            if (settings.position == "bottom") {
                html = action + html;
            } else {
                html += action;
            }
            var options = $.extend({
                target: html,
                animate: true,
                show: true,
                fixed: true,
                mask: true,
                className: "ui-alert",
                afterHide: function (c) {
                    this.dispose();
                },
                beforeShow: function (c) {
                    dialog.touch($('.ui-declare-submit', c), function () {
                        settings.callback && settings.callback.call(dialog, 'yes', c);
                    });
                    dialog.touch($('.ui-declare-no', c), function () {
                        settings.callback && settings.callback.call(dialog, 'no', c);
                    });
                    dialog.touch($('.ui-declare-close', c), function () {
                        settings.callback && settings.callback.call(dialog, 'close', c);
                    });
                }
            }, settings);
            dialog.init(options);
        }
        if (settings.type === "prompt") {
            var dialog = new Dialog();
            var html = ' <div class="ui-prompt-head">' + settings.bar + '</div>' +
                '<div class="ui-prompt-title">' +
                '<input type="text" class="ui-prompt-input" placeholder="' + settings.placeholder + '" value=' + settings.content + '>' +
                '<span class="ui-prompt-input-alert"></span></div>';
            var action = '';
            if (!settings.buttons) {
                settings.buttons = [{
                    'yes': '确定'
                }, {
                    'no': '取消'
                }];
            }
            ;
            var btnstr = '';
            for (var i = 0, l = settings.buttons.length; i < l; i++) {
                var item = settings.buttons[i];
                if (item.yes) {
                    btnstr += '<td><button class="ui-prompt-submit " data-type="yes">' + item.yes + '</button></td>';
                }
                if (item.no) {
                    btnstr += '<td><button class="ui-prompt-no" data-type="no">' + item.no + '</button></td>';
                }
                if (item.close) {
                    btnstr += '<td><button class="ui-prompt-close js-dialog-close" data-type="close">' + item.close + '</button></td>';
                }
            }
            action = '<table class="ui-dialog-action"><tr>' + btnstr + '</tr></table>';
            if (settings.position == "bottom") {
                html = action + html;
            } else {
                html += action;
            }
            var options = $.extend({
                target: html,
                animate: true,
                show: true,
                fixed: true,
                mask: true,
                className: "ui-alert",
                afterHide: function (c) {
                    this.dispose();
                },
                beforeShow: function (c) {
                    dialog.inputCheck($('.ui-prompt-input'), false);
                    dialog.touch($('.ui-prompt-submit', c), function () {
                        if (dialog.inputCheck($('.ui-prompt-input'), true)) {
                            settings.callback && settings.callback.call(dialog, 'yes', $('.ui-prompt-input').val(), c);
                        }

                    });
                    dialog.touch($('.ui-prompt-no', c), function () {
                        settings.callback && settings.callback.call(dialog, 'no', $('.ui-prompt-input').val(), c);
                    });
                    dialog.touch($('.ui-prompt-close', c), function () {
                        settings.callback && settings.callback.call(dialog, 'close', $('.ui-prompt-input').val(), c);
                    });

                }
            }, settings);
            dialog.init(options);
        }
        if (settings.type === "toast") {
            var alert = new Dialog();
            var html = '<div class="ui-toast-title">' + settings.content + '</div>';
            var action = '';
            var className = 'ui-toast';

            html += action;
            var alertOptions = $.extend({
                target: html,
                animate: true,
                show: true,
                mask: true,
                className: className,
                afterHide: function (c) {
                    this.dispose();
                    settings.callback && settings.callback();
                }
            }, settings);
            alert.init(alertOptions);
            if (settings.timer) {
                setTimeout(function () {
                    alert.dispose();
                    settings.callback && settings.callback();
                }, settings.timer);
            }
        }
        if (settings.type === "imgad") {
            var dialog = new Dialog();
            var ImgLoad = new Image();
            ImgLoad.src = settings.content;
            ImgLoad.onerror = function () {
                console.log('图片加载失败不会显示');
            }
            ImgLoad.onload = function () {
                var html = '<div class="ui-imgad-title"><img src=' + ImgLoad.src + '></div>';
                var className = 'ui-imgad';

                var action = ' <div class="ui-dialog-action" style="margin-top: 30px;border: none;"><span class="ui-imgad-no" data-type="no">X</span></div>';
                html += action;
                var alertOptions = $.extend({
                    target: html,
                    animate: true,
                    show: true,
                    mask: true,
                    className: className,
                    afterHide: function (c) {
                        this.dispose();
                    },
                    beforeShow: function (c) {
                        dialog.touch($('.ui-imgad-no', c), function () {
                            settings.callback && settings.callback.call(dialog, 'no', c);
                        });
                    }
                }, settings);
                dialog.init(alertOptions);
            }
            //img加载延时，需要回调处理，先延时加载
            $('.ui-imgad-title img').load(function () {
                dialog.setPosition()
            });
        }
    };
    /*alert*/
    $.alert = function (content, button, callback, timer, settings) {
        var options = {};
        var defaults = {
            zIndex: 100,
            type: 'alert'
        };
        if (typeof content == 'object') {
            options = $.extend(defaults, content);
        } else {
            options = $.extend(defaults, {
                content: content,
                button: button,
                timer: timer,
                callback: callback,
                width: 283,
                height: 'auto'
            });
        }
        $.Dialog($.extend(options, settings));
    }
    /*
    buttons :[{yes:"确定"},{no:'取消'},{close:'关闭'}]
    */
    $.confirm = function (content, buttons, callback, settings) {
        var options = {};
        var defaults = {
            zIndex: 100,
            type: 'confirm'
        };
        if (typeof content == 'object') {
            options = $.extend(defaults, content);
        } else {
            options = $.extend(defaults, {
                content: content,
                buttons: buttons,
                width: 283,
                callback: callback
            });
        }
        $.Dialog($.extend(options, settings));
    }

    /**
     * 带有输入框的弹窗
     */
    $.prompt = function (content, buttons, callback, settings) {
        var options = {};
        var defaults = {
            zIndex: 100,
            type: 'prompt'
        };
        if (content) {
            if (!content.bar) {
                content.bar = '标题';
            }
            if (!content.content) {
                content.content = '';
            }
            if (!content.placeholder) {
                content.placeholder = '请输入价格分组名称';
            }
        } else {
            throw new Error('You need to set the param "content"')
        }

        options = $.extend(defaults, content, {
            buttons: buttons,
            width: 283,
            callback: callback
        });

        $.Dialog($.extend(options, settings));
    }

    /**
     * 一个正常的toast，可设置long,short
     */
    $.toast = function (content, timer, callback, settings) {
        var options = {};
        var _timer = timer ? timer : 3000
        var defaults = {
            zIndex: 100,
            type: 'toast',
            timer: 3000
        };
        if (typeof content == 'object') {
            options = $.extend(defaults, content);
        } else {
            options = $.extend(defaults, {
                content: content,
                timer: _timer,
                callback: callback,
                mask: false,
                width: 'none',
                height: 'auto'
            });
        }
        $.Dialog($.extend(options, settings));
    }

    /**
     * 协议弹窗，同意，取消，免责声明
     */
    $.declare = function (content, buttons, callback, settings) {
        var options = {};
        var defaults = {
            zIndex: 100,
            type: 'declare'
        };

        options = $.extend(defaults, content, {
            buttons: buttons,
            width: 283,
            callback: callback
        });

        $.Dialog($.extend(options, settings));
    }

    /**
     * 弹窗广告
     */
    $.imgad = function (content, callback, settings) {
        var options = {};
        var defaults = {
            zIndex: 100,
            type: 'imgad'
        };
        if (typeof content == 'object') {
            options = $.extend(defaults, content);
        } else {
            options = $.extend(defaults, {
                content: content,
                callback: callback,
                width: 283,
                mask: true,
                height: 'auto'
            });
        }
        $.Dialog($.extend(options, settings));
    }


    var Dialog = function () {
        var rnd = Math.random().toString().replace('.', '');
        this.id = 'dialog_' + rnd;
        this.settings = {};
        this.settings.closeTpl = $('<span class="ui-dialog-close js-dialog-close">x</span>');
        this.settings.titleTpl = $('<div class="ui-dialog-title"></div>');
        this.timer = null;
        this.showed = false;
        this.mask = $();
    }
    Dialog.prototype = {
        init: function (settings) {
            var _this = this;
            this.settings = $.extend({
                fixed: false //是否固定位置，
            }, this.settings, settings);
            if (this.settings.mask) {
                this.mask = $('<div class="ui-dialog-mask"/>');
                $('body').append(this.mask);
            }
            $('body').append('<div class="ui-dialog" id="' + this.id + '"></div>');
            this.dialogContainer = $('#' + this.id);
            var zIndex = this.settings.zIndex || 10;
            this.dialogContainer.css({
                'zIndex': zIndex
            });
            if (this.settings.className) {
                this.dialogContainer.addClass(this.settings.className);
            }
            ;
            this.mask.css({
                'zIndex': zIndex - 1
            });
            if (this.settings.closeTpl) {
                this.dialogContainer.append(this.settings.closeTpl);
            }
            if (this.settings.title) {
                this.dialogContainer.append(this.settings.titleTpl);
                this.settings.titleTpl.html(this.settings.title);
            }
            this.bindEvent();
            if (this.settings.show) {
                this.show();
            }
        },
        touch: function (obj, fn) {
            var move;
            //在某些浏览器里，如微信旧版本的iphone5s会出现闪一下就消失
            // $(obj).on('click', click);

            function click(e) {
                return fn.call(this, e);
            }

            $(obj).on('touchmove', function (e) {
                move = true;
            }).on('touchend', function (e) {
                e.preventDefault();
                if (!move) {
                    var returnvalue = fn.call(this, e, 'touch');
                    if (!returnvalue) {
                        e.preventDefault();
                        e.stopPropagation();
                    }
                }
                move = false;
            });
        },
        inputCheck: function (obj, shownull) {
            var pattern = /^[\u4e00-\u9fa5_a-zA-Z0-9_]+$/;
            var flag = true;

            function check(shownull) {
                var inputVal = $(obj).val();
                var inputAlert = $('.ui-prompt-input-alert');

                if (pattern.test(inputVal) || inputVal.length == 0) {
                    inputAlert.html('');
                    $('.ui-prompt-input-alert').show();
                    flag = true;
                } else {
                    inputAlert.html('输入了限制字符');
                    $('.ui-prompt-input-alert').show();
                    flag = false;
                    return;
                }

                if (inputVal.length > 10) {
                    inputAlert.html('输入长度达到上限');
                    $('.ui-prompt-input-alert').show();
                    flag = false;
                    return;
                } else if (inputVal.length <= 0) {
                    if (shownull) {
                        inputAlert.html('输入不能为空');
                        $('.ui-prompt-input-alert').show();
                        flag = false;
                        return;
                    }
                } else {
                    inputAlert.html('');
                    $('.ui-prompt-input-alert').show();
                    flag = true;
                }
            }

            $(obj).on('input', function () {
                check(true);
            });
            check(shownull);
            return flag;
        },
        bindEvent: function () {
            var _this = this;
            if (this.settings.trigger) {
                $(this.settings.trigger).click(function () {
                    _this.show()
                });
                _this.touch($(this.settings.trigger), function () {
                    _this.show()
                });
            }
            ;
            $(this.dialogContainer).on('click', '.js-dialog-close', function () {
                _this.hide();
                return false;
            })
            $(window).resize(function () {
                _this.setPosition();
            });
            $(window).scroll(function () {
                _this.setPosition();
            })
            $(document).keydown(function (e) {
                if (e.keyCode === 27 && _this.showed) {
                    _this.hide();
                }
            });
            $(this.dialogContainer).on('hide', function () {
                _this.hide();
            })
        },
        dispose: function () {
            this.dialogContainer.remove();
            this.mask.remove();
            this.timer && clearInterval(this.timer);
        },
        hide: function () {
            var _this = this;
            if (_this.settings.beforeHide) {
                _this.settings.beforeHide.call(_this, _this.dialogContainer);
            }
            this.showed = false;
            this.mask.hide();
            this.timer && clearInterval(this.timer);
            if (this.settings.animate) {
                this.dialogContainer.removeClass('zoomIn').addClass("zoomOut");
                setTimeout(function () {
                    _this.dialogContainer.hide();
                    if (typeof _this.settings.target === "object") {
                        $('body').append(_this.dialogContainer.hide());
                    }
                    if (_this.settings.afterHide) {
                        _this.settings.afterHide.call(_this, _this.dialogContainer);
                    }
                }, 500);
            } else {
                this.dialogContainer.hide();
                if (typeof this.settings.target === "object") {
                    $('body').append(this.dialogContainer)
                }
                if (this.settings.afterHide) {
                    this.settings.afterHide.call(this, this.dialogContainer);
                }
            }
        },
        show: function () {
            if (typeof this.settings.target === "string") {
                if (/^(\.|\#\w+)/gi.test(this.settings.target)) {
                    this.dailogContent = $(this.settings.target);
                } else {
                    this.dailogContent = $('<div>' + this.settings.target + '</div>')
                }
            } else {
                this.dailogContent = this.settings.target;
            }
            this.mask.show();
            this.dailogContent.show();
            this.height = this.settings.height || 'auto' //this.dialogContainer.height();
            this.width = this.settings.width || 'auto' //this.dialogContainer.width();
            this.dialogContainer.append(this.dailogContent).show().css({
                height: this.height,
                width: this.width
            });
            if (this.settings.beforeShow) {
                this.settings.beforeShow.call(this, this.dialogContainer);
            }
            this.showed = true;
            $(this.settings.trigger).blur();

            this.setPosition();
            var _this = this;
            // $.alert(this.settings.clientWidth)
            this.timer && clearInterval(this.timer);
            if (this.settings.fixed) {
                this.timer = setInterval(function () {
                    _this.setPosition();
                }, 1000);
            }
            if (this.settings.animate) {
                this.dialogContainer.addClass('zoomIn').removeClass('zoomOut').addClass('animated');
            }
        },
        setPosition: function () {
            if (this.showed) {
                var _this = this;
                this.dialogContainer.show();
                this.height = this.settings.height;
                this.width = this.settings.width;
                if (isNaN(this.height)) {
                    this.height = (this.dialogContainer.outerHeight && this.dialogContainer.outerHeight()) || this.dialogContainer.height();
                }
                if (isNaN(this.width)) {
                    this.width = (this.dialogContainer.outerWidth && this.dialogContainer.outerWidth()) || this.dialogContainer.width();
                }
                var clientHeight = $(window).height();
                var clientWidth = $(window).width();
                var ml = this.width / 2;
                var mt = this.height / 2;
                var left = clientWidth / 2 - ml;
                var top = clientHeight / 2 - mt;
                left = Math.floor(Math.max(20, left));
                top = Math.floor(Math.max(0, top));
                console.log("ch:" + clientHeight, "cw:" + clientWidth, "left:" + left, "top:" + top, "w:" + this.width, "h:" + this.height);
                var position = 'absolute';
                if (_this.settings.fixed) {
                    position = 'fixed';
                } else {
                    top = top + $(window).scrollTop();
                }
                var bottom = "auto";
                if (_this.settings.position == "bottom") {
                    top = "auto";
                    bottom = 0;
                }
                _this.dialogContainer.css({
                    position: position,
                    top: top,
                    left: left,
                    right: left,
                    bottom: bottom
                });
            }
        }
    }
    return Dialog;
});