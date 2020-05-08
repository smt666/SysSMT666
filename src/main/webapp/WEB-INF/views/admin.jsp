<%@ page import="smt666.common.util.RequestHolder" %>
<%@ page import="smt666.mall.model.SysUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="/common/backend_common.jsp"/>
</head>

<body class="no-skin">
<div id="navbar" class="navbar navbar-default ace-save-state" >
    <div class="navbar-container ace-save-state">
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
            <span class="sr-only">Toggle sidebar</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <div class="navbar-header pull-left">
            <a href="/admin/page.do" class="navbar-brand">
                <small>
                    <i class="fa fa-leaf"></i>&nbsp;
                    SMT666控制台
                </small>
            </a>
        </div>
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue dropdown-modal">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <span class="user-info">
                            <small>欢迎,</small>
                            Admin
                        </span>
                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>
                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="#"> <i class="ace-icon fa fa-cog"></i>
                                设置
                            </a>
                        </li>
                        <li>
                            <a href="profile.html"> <i class="ace-icon fa fa-user"></i>
                                个人资料
                            </a>
                        </li>

                        <li class="divider"></li>
                        <li>
                            <a href="/logout.page"> <i class="ace-icon fa fa-power-off"></i>
                                注销
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>

    </div>

</div>

<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div id="sidebar" class="sidebar responsive">
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed')
            } catch (e) {
            }
        </script>


        <ul class="nav nav-list">
            <li class="active">
                <a class="popstyle" href="/sys/user/page.json" target="_blank">
                    <i class="menu-icon fa fa-tachometer"></i>
                    <span class="menu-text"> 管理列表 </span>
                    <i class="ace-icon fa fa-plus-circle orange bigger-130 acl-add pull-right"></i>
                </a>
                <b class="arrow"></b>
            </li>
            <li class="">
                <a href="#" class="dropdown-toggle">
                    <i class="menu-icon fa fa-desktop"></i>
                    <span class="menu-text"> 权限管理 </span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>

                <b class="arrow"></b>

                <ul class="submenu">

                    <li class="">
                        <a class="popstyle" href="/sys/dept/dept.page" target="_blank">
                            <i class="menu-icon fa fa-caret-right"></i>
                            用户管理
                        </a>
                        <b class="arrow"></b>
                    </li>
                    <li class="">
                        <a class="popstyle" href="/sys/role/role.page" target="_blank">
                            <i class="menu-icon fa fa-caret-right"></i>
                            角色管理
                        </a>
                        <b class="arrow"></b>
                    </li>
                    <li class="">
                        <a class="popstyle" href="/sys/aclModule/acl.page" target="_blank">
                            <i class="menu-icon fa fa-caret-right"></i>
                            权限管理
                        </a>
                        <b class="arrow"></b>
                    </li>
                    <li class="">
                        <a class="popstyle" href="/sys/log/log.page" target="_blank">
                            <i class="menu-icon fa fa-caret-right"></i>
                            权限更新记录
                        </a>
                        <b class="arrow"></b>
                    </li>
                </ul>
            </li>

            <li class="">
                <a href="#" class="dropdown-toggle">
                    <i class="menu-icon fa fa-desktop"></i>
                    <span class="menu-text"> 系统管理 </span>
                    <b class="arrow fa fa-angle-down"></b>
                </a>

                <b class="arrow"></b>

                <ul class="submenu">
                    <li class="">
                        <a class="popstyle" href="/config/config.page" target="_blank">
                            <i class="menu-icon fa fa-caret-right"></i>
                            全局配置管理
                        </a>
                        <b class="arrow"></b>
                    </li>
                </ul>
            </li>
        </ul>
        <!-- /.nav-list -->

        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i class="ace-icon fa fa-angle-double-left ace-save-state" data-icon1="ace-icon fa fa-angle-double-left"
               data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>

        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
        </script>
    </div>

    <%--<div class="main-content">
        <iframe id="innerFrame" src="/sys/dept/dept.page" width="99%" style="min-height: 768px;"></iframe>
    </div>--%>
    <!-- /.main-content -->

    <%--<a href="index.html#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse display">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>--%>

    <!-- /.main-container -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs ace-save-state" id="breadcrumbs">
                <ul class="breadcrumb">
                    <li>
                        <i class="ace-icon fa fa-home home-icon"></i>
                        <a href="#">Home</a>
                    </li>
                    <li>
                        <a href="#">Other Pages</a>
                    </li>
                    <li class="active">Blank Page</li>
                </ul><!-- /.breadcrumb -->


                <div class="nav-search" id="nav-search">
                    <form class="form-search">
                        <span class="input-icon">
                            <input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
                            <i class="ace-icon fa fa-search nav-search-icon"></i>
                        </span>
                    </form>
                </div><!-- /.nav-search -->
            </div>

            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <iframe id="innerFrame" src="/sys/dept/dept.page" width="99%" style="min-height: 820px;border: none;"></iframe>
                        <a href="index.html#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse display">
                            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
                        </a>
                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->

<div class="footer">
    <div class="footer-inner">
        <div class="footer-content">
            <span class="bigger-120 pull-right">
                <%--<span class="blue bolder"></span>--%>&nbsp;&nbsp;
                <a href="http://www.beian.miit.gov.cn/"><font color="#7b6888" size="1">浙ICP备19029318号</font></a> &nbsp;&nbsp;&copy; &nbsp;&nbsp;&nbsp;&nbsp;
            </span>
        </div>
    </div>
</div>

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div>

</div>
<!-- /.main-container -->


<!-- basic scripts -->

<!--[if !IE]> -->
<script src="/assets/js/jquery-2.1.0.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="/assets/js/jquery-1.11.0.min.js"></script>
<![endif]-->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='/assets/js/jquery.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='/assets/js/jquery1x.min.js'>" + "<" + "/script>");
</script>
<![endif]-->

<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
<script src="/assets/js/excanvas.min.js"></script>
<![endif]-->
<script src="/assets/js/jquery-ui.custom.min.js"></script>
<script src="/assets/js/jquery.ui.touch-punch.min.js"></script>
<script src="/assets/js/jquery.easypiechart.min.js"></script>
<script src="/assets/js/jquery.sparkline.min.js"></script>
<script src="/assets/js/flot/jquery.flot.min.js"></script>
<script src="/assets/js/flot/jquery.flot.pie.min.js"></script>
<script src="/assets/js/flot/jquery.flot.resize.min.js"></script>

<!-- ace scripts -->
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>

<script>

    $(document).ready(function () {
        $(".popstyle").removeAttr("target");
        $(".popstyle").each(function () {
            var $this = $(this)
            tmp = $this.attr("href");
            $this.attr("data", tmp);
            $this.attr("href", "javascript:void(0)");
        })

        $(".popstyle").click(function () {
            var $this = $(this)
            $("iframe").attr(
                    'src',
                    $this.attr("data")
            );
        });

        $(".direct").click(function () {
            var $this = $(this)
            $("iframe").attr(
                    'src',
                    $this.attr("data-value")
            );
        });
    });
</script>
</body>
</html>

