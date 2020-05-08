<%--
  Created by IntelliJ IDEA.
  User: 27140
  Date: 2020-05-04
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>部门管理</title>
    <jsp:include page="/common/backend_common.jsp"/>
    <jsp:include page="/common/page.jsp"/>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>

<div class="page-header">
    <h1>
        用户管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            部门的用户管理
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            部门列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 dept-add"></i>
            </a>
        </div>
        <div id="deptList">
        </div>
    </div>
    <div class="col-sm-9">
        <div class="col-xs-12">
            <div class="table-header">
                用户列表&nbsp;&nbsp;
                <a class="green" href="#">
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 user-add"></i>
                </a>
            </div>
            <div>
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="dataTables_length" id="dynamic-table_length"><label>
                                展示
                                <select id="pageSize" name="dynamic-table_length" aria-controls="dynamic-table" class="form-control input-sm">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select> 条记录 </label>
                            </div>
                        </div>
                    </div>
                    <table id="dynamic-table" class="table table-striped table-bordered table-hover dataTable no-footer" role="grid"
                           aria-describedby="dynamic-table_info" style="font-size:14px">
                        <thead>
                        <tr role="row">
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                姓名
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                所属部门
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                邮箱
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                电话
                            </th>
                            <th tabindex="0" aria-controls="dynamic-table" rowspan="1" colspan="1">
                                状态
                            </th>
                            <th class="sorting_disabled" rowspan="1" colspan="1" aria-label=""></th>
                        </tr>
                        </thead>
                        <tbody id="userList"></tbody>
                    </table>
                    <div class="row" id="userPage">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-dept-form" style="display: none;">
    <form id="deptForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">上级部门</label></td>
                <td>
                    <select id="parentId" name="parentId" data-placeholder="选择部门" style="width: 200px;"></select>
                    <input type="hidden" name="id" id="deptId"/>
                </td>
            </tr>
            <tr>
                <td><label for="deptName">名称</label></td>
                <td><input type="text" name="name" id="deptName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptSeq">顺序</label></td>
                <td><input type="text" name="seq" id="deptSeq" value="1" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="deptRemark">备注</label></td>
                <td><textarea name="remark" id="deptRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div id="dialog-user-form" style="display: none;">
    <form id="userForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td style="width: 80px;"><label for="parentId">所在部门</label></td>
                <td>
                    <select id="deptSelectId" name="deptId" data-placeholder="选择部门" style="width: 200px;"></select>
                </td>
            </tr>
            <tr>
                <td><label for="userName">名称</label></td>
                <input type="hidden" name="id" id="userId"/>
                <td><input type="text" name="username" id="userName" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userMail">邮箱</label></td>
                <td><input type="text" name="mail" id="userMail" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userTelephone">电话</label></td>
                <td><input type="text" name="telephone" id="userTelephone" value="" class="text ui-widget-content ui-corner-all"></td>
            </tr>
            <tr>
                <td><label for="userStatus">状态</label></td>
                <td>
                    <select id="userStatus" name="status" data-placeholder="选择状态" style="width: 150px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                        <option value="2">删除</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="userRemark">备注</label></td>
                <td><textarea name="remark" id="userRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>

<script id="deptListTemplate" type="x-tmpl-mustache">
<ol class="dd-list">
    {{#deptList}}
        <li class="dd-item dd2-item dept-name" id="dept_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green dept-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red dept-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/deptList}}
</ol>
</script>
<script id="userListTemplate" type="x-tmpl-mustache">
{{#userList}}
<tr role="row" class="user-name odd" data-id="{{id}}"><!--even -->
    <td><a href="#" class="user-edit" data-id="{{id}}">{{username}}</a></td>
    <td>{{showDeptName}}</td>
    <td>{{mail}}</td>
    <td>{{telephone}}</td>
    <td>{{#bold}}{{showStatus}}{{/bold}}</td> <!-- 此处套用函数对status做特殊处理 -->
    <td>
        <div class="hidden-sm hidden-xs action-buttons">
            <a class="green user-edit" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-pencil bigger-100"></i>
            </a>
            <a class="red user-acl" href="#" data-id="{{id}}">
                <i class="ace-icon fa fa-flag bigger-100"></i>
            </a>
        </div>
    </td>
</tr>
{{/userList}}
</script>

<script type="application/javascript">
    $(function() {

        var deptList; // 存储树形部门列表
        var deptMap = {}; // 存储map格式的部门信息
        var userMap = {}; // 存储map格式的用户信息
        var optionStr = "";
        var lastClickDeptId = -1;

        var deptListTemplate = $('#deptListTemplate').html();
        Mustache.parse(deptListTemplate);
        var userListTemplate = $('#userListTemplate').html();
        Mustache.parse(userListTemplate);
        /*页面一大加载完毕，即会立即加载部门树的函数。*/
        loadDeptTree();
        /*定义突发函数，用来加载部门层级树。*/
        function loadDeptTree() {
            $.ajax({
                url: "/sys/dept/tree.json",
                success : function (result) {
                    if (result.ret) {
                        deptList = result.data;
                        var rendered = Mustache.render(deptListTemplate, {deptList: result.data});
                        /*渲染首层的html页面树结构。*/
                        $("#deptList").html(rendered);
                        /*然后继续渲染部门层级html（下一层html树结构）*/
                        recursiveRenderDept(result.data);
                        /*绑定一些按钮的操作。*/
                        bindDeptClick();
                    } else {
                        showMessage("加载部门列表", result.msg, false);
                    }
                }
            })
        }

        // 递归渲染部门树
        function recursiveRenderDept(deptList) {
            if(deptList && deptList.length > 0) {
                $(deptList).each(function (i, dept) {
                    deptMap[dept.id] = dept;
                    if (dept.deptList.length > 0) {
                        var rendered = Mustache.render(deptListTemplate, {deptList: dept.deptList});
                        /*把当前生成的这个html片段，绑定到页面元素上中。*/
                        $("#dept_" + dept.id).append(rendered);
                        /*生成html并且绑定后，继续自己调用自己（将下一层的html也进行绑定）。递归调用。*/
                        recursiveRenderDept(dept.deptList);
                    }
                })
            }
        }

        // 绑定部门点击事件
        function bindDeptClick() {

            $(".dept-name").click(function(e) {
                e.preventDefault();
                /*阻止JS中的冒泡事件。*/
                e.stopPropagation();
                var deptId = $(this).attr("data-id");
                handleDepSelected(deptId);
            });

            $(".dept-delete").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var deptId = $(this).attr("data-id");
                var deptName = $(this).attr("data-name");
                if (confirm("确定要删除部门[" + deptName + "]吗?")) {
                    console.log("delete dept:" + deptName);
                    $.ajax({
                        url: "/sys/dept/delete.json",
                        data: {
                            id: deptId
                        },
                        success: function (result) {
                            if (result.ret) {
                                showMessage("删除部门[" + deptName + "]", "操作成功", true);
                                loadDeptTree();
                            } else {
                                showMessage("删除部门[" + deptName + "]", result.msg, false);
                            }
                        }
                    });
                }
            });

            $(".dept-edit").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var deptId = $(this).attr("data-id");
                $("#dialog-dept-form").dialog({
                    model: true,
                    title: "编辑部门",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "<option value=\"0\">-</option>";
                        recursiveRenderDeptSelect(deptList, 1);
                        $("#deptForm")[0].reset();
                        $("#parentId").html(optionStr);
                        $("#deptId").val(deptId);
                        var targetDept = deptMap[deptId];
                        if (targetDept) {
                            $("#parentId").val(targetDept.parentId);
                            $("#deptName").val(targetDept.name);
                            $("#deptSeq").val(targetDept.seq);
                            $("#deptRemark").val(targetDept.remark);
                        }
                    },
                    buttons : {
                        "更新": function(e) {
                            e.preventDefault();
                            updateDept(false, function (data) {
                                $("#dialog-dept-form").dialog("close");
                            }, function (data) {
                                showMessage("更新部门", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-dept-form").dialog("close");
                        }
                    }
                });
            })
        }

        function handleDepSelected(deptId) {
            /*等于负1表示，移除高亮显示。*/
            if (lastClickDeptId != -1) {
                var lastDept = $("#dept_" + lastClickDeptId + " .dd2-content:first");
                lastDept.removeClass("btn-yellow");
                lastDept.removeClass("no-hover");
            }
            var currentDept = $("#dept_" + deptId + " .dd2-content:first");
            currentDept.addClass("btn-yellow");
            currentDept.addClass("no-hover");
            lastClickDeptId = deptId;
            /*在点部门列表的时候就会加载用户的列表*/
            loadUserList(deptId);
        }

        function loadUserList(deptId) {
            /*获取出当前页码需要展示的行数。*/
            var pageSize = $("#pageSize").val();
            /*定义项后台请求数据的url请求。*/
            var url = "/sys/user/page.json?deptId=" + deptId;
            /*定义一个当前可以展示多少页的值。如果获取到的数据没有记录那么急为空值的话，那么我们设置成默认1 。*/
            var pageNo = $("#userPage .pageNo").val() || 1;
            $.ajax({
                url : url,
                data: {
                    pageSize: pageSize,
                    pageNo: pageNo
                },
                success: function (result) {
                    renderUserListAndPage(result, url);
                }
            })
        }
        /*定义一个函数，来渲染我们的用户的分页列表。*/
        function renderUserListAndPage(result, url) {
            if (result.ret) {
                if (result.data.total > 0){
                    var rendered = Mustache.render(userListTemplate, {
                        userList: result.data.data,
                        "showDeptName": function() {
                            return deptMap[this.deptId].name;
                        },
                        "showStatus": function() {
                            return this.status == 1 ? '有效' : (this.status == 0 ? '无效' : '删除');
                        },
                        "bold": function() {
                            return function(text, render) {
                                var status = render(text);
                                if (status == '有效') {
                                    return "<span class='label label-sm label-success'>有效</span>";
                                } else if(status == '无效') {
                                    return "<span class='label label-sm label-warning'>无效</span>";
                                } else {
                                    return "<span class='label'>删除</span>";
                                }
                            }
                        }
                    });
                    $("#userList").html(rendered);
                    bindUserClick();
                    $.each(result.data.data, function(i, user) {
                        userMap[user.id] = user;
                    })
                } else {
                    $("#userList").html('');
                }
                var pageSize = $("#pageSize").val();
                var pageNo = $("#userPage .pageNo").val() || 1;
                renderPage(url, result.data.total, pageNo, pageSize, result.data.total > 0 ? result.data.data.length : 0, "userPage", renderUserListAndPage);
            } else {
                showMessage("获取部门下用户列表", result.msg, false);
            }
        }

        $(".user-add").click(function() {
            $("#dialog-user-form").dialog({
                model: true,
                title: "新增用户",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    optionStr = "";
                    recursiveRenderDeptSelect(deptList, 1);
                    $("#userForm")[0].reset();
                    $("#deptSelectId").html(optionStr);
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateUser(true, function (data) {
                            $("#dialog-user-form").dialog("close");
                            loadUserList(lastClickDeptId);
                        }, function (data) {
                            showMessage("新增用户", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-user-form").dialog("close");
                    }
                }
            });
        });
        function bindUserClick() {
            $(".user-acl").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var userId = $(this).attr("data-id");
                $.ajax({
                    url: "/sys/user/acls.json",
                    data: {
                        userId: userId
                    },
                    success: function(result) {
                        if (result.ret) {
                            console.log(result)
                        } else {
                            showMessage("获取用户权限数据", result.msg, false);
                        }
                    }
                })
            });
            $(".user-edit").click(function(e) {
                e.preventDefault();
                e.stopPropagation();
                var userId = $(this).attr("data-id");
                $("#dialog-user-form").dialog({
                    model: true,
                    title: "编辑用户",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        optionStr = "";
                        recursiveRenderDeptSelect(deptList, 1);
                        $("#userForm")[0].reset();
                        $("#deptSelectId").html(optionStr);

                        var targetUser = userMap[userId];
                        if (targetUser) {
                            /*选中指定的部门*/
                            $("#deptSelectId").val(targetUser.deptId);
                            /*配置用户的属性。*/
                            $("#userName").val(targetUser.username);
                            $("#userMail").val(targetUser.mail);
                            $("#userTelephone").val(targetUser.telephone);
                            $("#userStatus").val(targetUser.status);
                            $("#userRemark").val(targetUser.remark);
                            $("#userId").val(targetUser.id);
                        }
                    },
                    buttons : {
                        "更新": function(e) {
                            e.preventDefault();
                            updateUser(false, function (data) {
                                $("#dialog-user-form").dialog("close");
                                loadUserList(lastClickDeptId);
                            }, function (data) {
                                showMessage("更新用户", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-user-form").dialog("close");
                        }
                    }
                });
            });
        }

        $(".dept-add").click(function() {
            $("#dialog-dept-form").dialog({
                model: true,
                title: "新增部门",
                open: function(event, ui) {
                    /*把关闭按钮先隐藏掉。*/
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    /*新增部门时，我们给定一个允许什么都不选的一个默认的值-（只显示-这个中划线代表什么都没有）。*/
                    optionStr = "<option value=\"0\">-</option>";
                    /*相当于每次从第1层开始。*/
                    recursiveRenderDeptSelect(deptList, 1);
                    /*清空之前dialog中的数据，重置form 。*/
                    $("#deptForm")[0].reset();
                    /*然后将刚刚计算完成需要初始化的数据写进去。*/
                    $("#parentId").html(optionStr);
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();  /*默认的按钮样式*/
                        /*这个updateDept函数是用来进行数据交互的函数。
                        * 第1个参数，true代表是否为新增部门的save操作。
                        * 第2份参数是一个函数，代表新增或者修改操作在执行成功之后将要去回调的函数。*/
                        updateDept(true, function (data) {
                            $("#dialog-dept-form").dialog("close");
                        }, function (data) {
                            showMessage("新增部门", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-dept-form").dialog("close");
                    }
                }
            });
        });
        /*生成select下拉框里面的部门层级树的函数。*/
        function recursiveRenderDeptSelect(deptList, level) {
            /*如果传入的level没有值那么就默认设置成0 。*/
            level = level | 0;
            /*如果传入的deptList有值的话，那么就遍历这个部门列表。*/
            if (deptList && deptList.length > 0) {
                $(deptList).each(function (i, dept) {
                    /*每次开始新一轮遍历时，我们都更新一次deptMap，确保数据没有问题。*/
                    deptMap[dept.id] = dept;
                    var blank = "";
                    if (level > 1) {
                        /*如果level不是第一层部门的话，那么就遍历。*/
                        for(var j = 3; j <= level; j++) {
                            /*给遍历到的每个层级多加两个点类进行分割。*/
                            blank += "..";
                        }
                        /*并且早每个层级添加点分割后，我们再拼接一个字符，用它来不是层级的结构。*/
                        blank += "∟";
                    }
                    optionStr += Mustache.render("<option value='{{id}}'>{{name}}</option>", {id: dept.id, name: blank + dept.name});
                    /*开始处理下一层。如果存在下一层那么就递归执行。*/
                    if (dept.deptList && dept.deptList.length > 0) {
                        recursiveRenderDeptSelect(dept.deptList, level + 1);
                    }
                });
            }
        }

        function updateUser(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/user/save.json" : "/sys/user/update.json",
                data: $("#userForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadDeptTree();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }
        /*定义一个新增或者修改部门信息的函数。*/
        function updateDept(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/dept/save.json" : "/sys/dept/update.json",
                data: $("#deptForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadDeptTree();
                        if (successCallback) {
                            successCallback(result);
                        }
                    } else {
                        if (failCallback) {
                            failCallback(result);
                        }
                    }
                }
            })
        }
    })
</script>
</body>
</html>
