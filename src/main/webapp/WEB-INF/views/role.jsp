<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色</title>
    <jsp:include page="/common/backend_common.jsp" />
    <link rel="stylesheet" href="/ztree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="/assets/css/bootstrap-duallistbox.min.css" type="text/css">
    <script type="text/javascript" src="/ztree/jquery.ztree.all.min.js"></script>
    <script type="text/javascript" src="/assets/js/jquery.bootstrap-duallistbox.min.js"></script>
    <style type="text/css">
        .bootstrap-duallistbox-container .moveall, .bootstrap-duallistbox-container .removeall {
            width: 50%;
        }
        .bootstrap-duallistbox-container .move, .bootstrap-duallistbox-container .remove {
            width: 49%;
        }
    </style>
</head>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
        角色管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            维护角色与用户, 角色与权限关系
        </small>
    </h1>
</div>
<div class="main-content-inner">
    <div class="col-sm-3">
        <div class="table-header">
            角色列表&nbsp;&nbsp;
            <a class="green" href="#">
                <i class="ace-icon fa fa-plus-circle orange bigger-130 role-add"></i>
            </a>
        </div>
        <div id="roleList"></div>
    </div>
    <div class="col-sm-9">
        <div class="tabbable" id="roleTab">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a data-toggle="tab" href="#roleAclTab">
                        角色与权限
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#roleUserTab">
                        角色与用户
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div id="roleAclTab" class="tab-pane fade in active">
                    <ul id="roleAclTree" class="ztree"></ul>
                    <button class="btn btn-info saveRoleAcl" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>

                <div id="roleUserTab" class="tab-pane fade" >
                    <div class="row">
                        <div class="box1 col-md-6">待选用户列表</div>
                        <div class="box1 col-md-6">已选用户列表</div>
                    </div>
                    <select multiple="multiple" size="10" name="roleUserList" id="roleUserList" >
                    </select>
                    <div class="hr hr-16 hr-dotted"></div>
                    <button class="btn btn-info saveRoleUser" type="button">
                        <i class="ace-icon fa fa-check bigger-110"></i>
                        保存
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="dialog-role-form" style="display: none;">
    <form id="roleForm">
        <table class="table table-striped table-bordered table-hover dataTable no-footer" role="grid">
            <tr>
                <td><label for="roleName">名称</label></td>
                <td>
                    <input type="text" name="name" id="roleName" value="" class="text ui-widget-content ui-corner-all">
                    <input type="hidden" name="id" id="roleId"/>
                </td>
            </tr>
            <tr>
                <td><label for="roleStatus">状态</label></td>
                <td>
                    <select id="roleStatus" name="status" data-placeholder="状态" style="width: 150px;">
                        <option value="1">可用</option>
                        <option value="0">冻结</option>
                    </select>
                </td>
            </tr>
            <td><label for="roleRemark">备注</label></td>
            <td><textarea name="remark" id="roleRemark" class="text ui-widget-content ui-corner-all" rows="3" cols="25"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<script id="roleListTemplate" type="x-tmpl-mustache">
<ol class="dd-list ">
    {{#roleList}}
        <li class="dd-item dd2-item role-name" id="role_{{id}}" href="javascript:void(0)" data-id="{{id}}">
            <div class="dd2-content" style="cursor:pointer;">
            {{name}}
            <span style="float:right;">
                <a class="green role-edit" href="#" data-id="{{id}}" >
                    <i class="ace-icon fa fa-pencil bigger-100"></i>
                </a>
                &nbsp;
                <a class="red role-delete" href="#" data-id="{{id}}" data-name="{{name}}">
                    <i class="ace-icon fa fa-trash-o bigger-100"></i>
                </a>
            </span>
            </div>
        </li>
    {{/roleList}}
</ol>
</script>

<script id="selectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}" selected="selected">{{username}}</option>
{{/userList}}
</script>

<script id="unSelectedUsersTemplate" type="x-tmpl-mustache">
{{#userList}}
    <option value="{{id}}">{{username}}</option>
{{/userList}}
</script>

<script type="text/javascript">
    $(function () {
        var roleMap = {};
        var lastRoleId = -1;
        var selectFirstTab = true;
        var hasMultiSelect = false;
        
        var roleListTemplate = $("#roleListTemplate").html();
        Mustache.parse(roleListTemplate);
        var selectedUsersTemplate = $("#selectedUsersTemplate").html();
        Mustache.parse(selectedUsersTemplate);
        var unSelectedUsersTemplate = $("#unSelectedUsersTemplate").html();
        Mustache.parse(unSelectedUsersTemplate);
        
        loadRoleList();

        // zTree
        <!-- 树结构相关 开始 -->
        //===========================================================================================================
        var zTreeObj = [];
        var modulePrefix = 'm_';    /*权限模块使用的前缀*/
        var aclPrefix = 'a_';       /*权限点使用的前缀*/
        var nodeMap = {};           /*所有的信息都会放到Map中。*/
        /*定义zTree使用的基础配置。*/
        var setting = {
            check: {
                enable: true,                /*开启多选框*/
                chkDisabledInherit: true,    /*多选框中的元素设置成不继承的模式。*/
                chkboxType: {"Y": "ps", "N": "ps"}, //auto check 父节点 子节点    ps代表如果我们勾选父节点那么子节点也会默认被勾选中
                autoCheckTrigger: true       /*代表我们那次点击时都会check相关的方法。*/
            },
            data: {
                simpleData: {             /*我们使用了普通的simpleData配置*/
                    enable: true,
                    rootPId: 0            /*指定了顶层的ID默认值为0（相当于是首层的权限模块的id ）。*/
                }
            },
            callback: {                     /*配置回调函数*/
                onClick: onClickTreeNode    /*这里相当于我们指定另一个onClick鼠标点击事件。*/
            }
        };
        //===========================================================================================================
        function onClickTreeNode(e, treeId, treeNode) { // 绑定单击事件
            var zTree = $.fn.zTree.getZTreeObj("roleAclTree");
            zTree.expandNode(treeNode);
        }
        /**加载角色列表信息，加载完之后使用Mustache进行渲染，渲染完毕后绑定鼠标单击功能，绑定完毕把角色列表放到全局的变量roleMap中。*/
        function loadRoleList() {
            $.ajax({
                url: "/sys/role/list.json",
                success: function (result) {
                    if (result.ret) {
                        var rendered = Mustache.render(roleListTemplate, {roleList: result.data});
                        /*当Mustache渲染完毕之后我们就把渲染后的HTML内容添加到页面中指定的元素标签(此处使用了id选择器指定了这个标签元素)中去。*/
                        $("#roleList").html(rendered);
                        bindRoleClick(); /*然后调用【绑定鼠标的点击事件】的方法，实现鼠标单击事件的触发功能。*/
                        /*遍历从后台获取到的result结果，其中，i是遍历时的索引，role是每次遍历的一项元素。*/
                        $.each(result.data, function(i, role) {
                            // 【更新一下roleMap】相当于把结果数据中的每一项，都依次遍历进roleMap的集合中，其中key为role的id ,value是每一项数据。
                            // 即，加载角色列表信息，加载完之后使用Mustache进行渲染，渲染完毕后绑定鼠标单击功能，绑定完毕把角色列表放到全局的变量roleMap中。
                            roleMap[role.id] = role;
                        });
                    } else {
                        showMessage("加载角色列表", result.msg, false);
                    }
                }
            });
        }
        /*绑定鼠标的点击事件。*/
        function bindRoleClick() {
            $(".role-edit").click(function (e) {
                e.preventDefault();
                e.stopPropagation();
                var roleId = $(this).attr("data-id");
                $("#dialog-role-form").dialog({
                    model: true,
                    title: "修改角色",
                    open: function(event, ui) {
                        $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                        $("#roleForm")[0].reset();
                        var targetRole = roleMap[roleId];
                        if (targetRole) {
                            $("#roleId").val(roleId);
                            $("#roleName").val(targetRole.name);
                            $("#roleStatus").val(targetRole.status);
                            $("#roleRemark").val(targetRole.remark);
                        }
                    },
                    buttons : {
                        "修改": function(e) {
                            e.preventDefault();
                            updateRole(false, function (data) {
                                $("#dialog-role-form").dialog("close");
                            }, function (data) {
                                showMessage("修改角色", data.msg, false);
                            })
                        },
                        "取消": function () {
                            $("#dialog-role-form").dialog("close");
                        }
                    }
                })
            });
            $(".role-name").click(function (e) {
               e.preventDefault();
               e.stopPropagation();
               var roleId = $(this).attr("data-id");
               handleRoleSelected(roleId);
            });
        }

        function handleRoleSelected(roleId) {
            if (lastRoleId != -1) {
                var lastRole = $("#role_" + lastRoleId + " .dd2-content:first");
                lastRole.removeClass("btn-yellow");
                lastRole.removeClass("no-hover");
            }
            var currentRole = $("#role_" + roleId + " .dd2-content:first");
            currentRole.addClass("btn-yellow");
            currentRole.addClass("no-hover");
            lastRoleId = roleId;

            $('#roleTab a:first').trigger('click');
            if (selectFirstTab) {
                loadRoleAcl(roleId);
            }
        }
        /**加载角色权限*/
        function loadRoleAcl(selectedRoleId) {
            /*等于一个负数，相当于没有被选中，那么我们直接return结束当前函数。*/
            if (selectedRoleId == -1) {
                return;
            }
            $.ajax({
                url: "/sys/role/roleTree.json",
                data : {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        // 正常返回数据后我们就开始渲染。
                        renderRoleTree(result.data);
                    } else {
                        showMessage("加载角色权限数据", result.msg, false);
                    }
                }
            });
        }
        /*在执行保存时会用到的函数。*/
        function getTreeSelectedId() {
            var treeObj = $.fn.zTree.getZTreeObj("roleAclTree");
            var nodes = treeObj.getCheckedNodes(true);
            var v = "";
            for(var i = 0; i < nodes.length; i++) {
                if(nodes[i].id.startsWith(aclPrefix)) {
                    v += "," + nodes[i].dataId;
                }
            }
            return v.length > 0 ? v.substring(1): v;
        }
        /**使用zTree的插件进行渲染，前提是我们必须先为zTree准备好数据（即JS可以获取到后台传过来的结果）。*/
        function renderRoleTree(aclModuleList) {
            /*在每次使用zTre时，都清空一下原有数据。*/
            zTreeObj = [];
            /*然后我们只能一个递归函数，来生成zTree需要进行渲染的数据。*/
            recursivePrepareTreeData(aclModuleList);
            for(var key in nodeMap) {
                zTreeObj.push(nodeMap[key]);
            }
            $.fn.zTree.init($("#roleAclTree"), setting, zTreeObj);
        }
        /**递归生成数据。*/
        function recursivePrepareTreeData(aclModuleList) {
            // prepare nodeMap
            /*如果首层有数据的情况下我们才开始处理（没有数据意味着没有权限模块的信息。）*/
            if (aclModuleList && aclModuleList.length > 0) {
                $(aclModuleList).each(function(i, aclModule) {
                    /*设置一个2选一的开关（变量），代表当前这个节点是否为被勾选的状态。*/
                    var hasChecked = false;
                    /*如果当前正在遍历的权限模块中，是有权限点数据的，并且权限点的数量有多个的话。*/
                    if (aclModule.aclList && aclModule.aclList.length > 0) {
                        /*这个时候，我们需要处理一下这个模块中的权限点。
                        * 因为当前这个权限点有多个，所以我们还得遍历一下这个权限点。*/
                        $(aclModule.aclList).each(function(i, acl) {   /*把权限点信息放到模块中去。*/
                            /*每一次遍历过程中就把数据存入到预先准备依赖收集数据的zTreeObj这个对象中。*/
                            zTreeObj.push({
                                id: aclPrefix + acl.id,                       /*【ID是哪个】权限点需使用到的id属性值*/
                                pId: modulePrefix + acl.aclModuleId,          /*【挂在哪个模块下】它对应的上级的权限模块对应的id属性值*/
                                name: acl.name + ((acl.type == 1) ? '(菜单)' : ''),    /*当前展示的名称*/
                                chkDisabled: !acl.hasAcl,                     /*是否要先隐藏起来。它取决于hasAcl（是否有权限操作）属性的值。*/
                                checked: acl.checked,                         /*是否默认为选中的状态，也取决于checked属性的值。*/
                                dataId: acl.id                                /*当前被选中的元素的ID（会使用它就像删除或者修改的操作）*/
                            });
                            /*判断，如果acl处于被选中状态的话，那么我们就让hasChecked变量为选中（即，打开开关）。*/
                            if(acl.checked) {
                                hasChecked = true;
                            }
                        });
                    }
                    /*接着，我们对权限模块进行处理。
                    先判断，当前的模块下是否还有子模块、如果有那么是否为多个、如果为多个那么是否还有权限点、权限点数量是否多个*/
                    if ((aclModule.aclModuleList && aclModule.aclModuleList.length > 0) ||
                        (aclModule.aclList && aclModule.aclList.length > 0)) {
                        /*如果以上2个有任意一个满足条件的话，那么就把数据放到zTree中nodeMap展示出来。*/
                        nodeMap[modulePrefix + aclModule.id] = {
                            id : modulePrefix + aclModule.id,
                            pId: modulePrefix + aclModule.parentId,
                            name: aclModule.name,
                            open: hasChecked
                        };
                        var tempAclModule = nodeMap[modulePrefix + aclModule.id];
                        while(hasChecked && tempAclModule) {
                            if(tempAclModule) {
                                nodeMap[tempAclModule.id] = {
                                    id: tempAclModule.id,
                                    pId: tempAclModule.pId,
                                    name: tempAclModule.name,
                                    open: true
                                }
                            }
                            tempAclModule = nodeMap[tempAclModule.pId];
                        }
                    }
                    recursivePrepareTreeData(aclModule.aclModuleList);
                });
            }
        }
        /**增加角色*/
        $(".role-add").click(function () {
            $("#dialog-role-form").dialog({
                model: true,
                title: "新增角色",
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", $(this).parent()).hide();
                    $("#roleForm")[0].reset();
                },
                buttons : {
                    "添加": function(e) {
                        e.preventDefault();
                        updateRole(true, function (data) {
                            $("#dialog-role-form").dialog("close");
                        }, function (data) {
                            showMessage("新增角色", data.msg, false);
                        })
                    },
                    "取消": function () {
                        $("#dialog-role-form").dialog("close");
                    }
                }
            })
        });
        /*绑定角色编辑的【保存】按钮事件。*/
        $(".saveRoleAcl").click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage("保存角色与权限点的关系", "请现在左侧选择需要操作的角色", false);
                return;
            }
            $.ajax({
                url: "/sys/role/changeAcls.json",
                data: {
                    roleId: lastRoleId,
                    aclIds: getTreeSelectedId()
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("保存角色与权限点的关系", "操作成功", true);
                    } else {
                        showMessage("保存角色与权限点的关系", result.msg, false);
                    }
                }
            });
        });

        function updateRole(isCreate, successCallback, failCallback) {
            $.ajax({
                url: isCreate ? "/sys/role/save.json" : "/sys/role/update.json",
                data: $("#roleForm").serializeArray(),
                type: 'POST',
                success: function(result) {
                    if (result.ret) {
                        loadRoleList();
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
        /*绑定事件。*/
        $("#roleTab a[data-toggle='tab']").on("shown.bs.tab", function(e) {
            if(lastRoleId == -1) {
                showMessage("加载角色关系","请先在左侧选择操作的角色", false);
                return;
            }
            /*获取出当前点击对象的属性，如果href="#roleAclTab"的话，代表我们点击的是第1个标签。*/
            if (e.target.getAttribute("href") == '#roleAclTab') {
                /*如果href="#roleAclTab"的话，代表我们点击的是第1个标签。我们就让第1个标签为true 。*/
                selectFirstTab = true;
                loadRoleAcl(lastRoleId);
            } else {
                selectFirstTab = false;
                loadRoleUser(lastRoleId);
            }
        });
        /*角色与用户的关系的维护。*/
        function loadRoleUser(selectedRoleId) {
            $.ajax({
                url: "/sys/role/users.json",
                data: {
                    roleId: selectedRoleId
                },
                type: 'POST',
                success: function (result) {
                    /*判断Ajax请求后台处理之后的返回的数据，是否为正常返回的。*/
                    if (result.ret) {
                        /*渲染【已选中了的用户】的模板（result.data.selected的selected数据是在Java代码的SysRoleController的【map.put("selected", selectedUserList);】中进行存入并且除了Ajax请求响应回此页面的。）*/
                        var renderedSelect = Mustache.render(selectedUsersTemplate, {userList: result.data.selected});
                        /*渲染【未选中了的用户】的模板*/
                        var renderedUnSelect = Mustache.render(unSelectedUsersTemplate, {userList: result.data.unselected});
                        /*都使用了Mustache引擎解析完毕后，我们把HTML内容添加到页面中指定的标签元素中，实现模板内容的页面渲染。*/
                        $("#roleUserList").html(renderedSelect + renderedUnSelect);

                        if(!hasMultiSelect) {
                            $('select[name="roleUserList"]').bootstrapDualListbox({
                                showFilterInputs: false,   /*设置成，不进行过滤。*/
                                moveOnSelect: false,       /*设置成，单击之后并不立即选中。*/
                                infoText: false
                            });
                            /*设置成true就代表不会再次载了（只加载一次）。*/
                            hasMultiSelect = true;
                        } else {
                            $('select[name="roleUserList"]').bootstrapDualListbox('refresh', true);
                        }
                    } else {
                        /*如果数据不是正常返回的话，那么我们给页面展示出一个提示信息，告诉用户错误的信息。*/
                        showMessage("加载角色用户数据", result.msg, false);
                    }
                }
            });
        }

        $(".saveRoleUser").click(function (e) {
            e.preventDefault();
            if (lastRoleId == -1) {
                showMessage("保存角色与用户的关系", "请现在左侧选择需要操作的角色", false);
                return;
            }
            $.ajax({
                url: "/sys/role/changeUsers.json",
                data: {
                    roleId: lastRoleId,
                    userIds: $("#roleUserList").val() ? $("#roleUserList").val().join(",") : ''
                },
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        showMessage("保存角色与用户的关系", "操作成功", true);
                    } else {
                        showMessage("保存角色与用户的关系", result.msg, false);
                    }
                }
            });
        });
    });
</script>
</body>
</html>
