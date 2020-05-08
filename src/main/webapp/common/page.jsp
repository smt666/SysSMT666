<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--通用的分页插件。--%>
<script id="paginateTemplate" type="x-tmpl-mustache">
<%--我们首先先定义一个模板。--%>
<div class="col-xs-6">
    <div class="dataTables_info" id="dynamic-table_info" role="status" aria-live="polite">
        当前总计 {{total}} 条 ，总页码数为 {{from}} ~ {{to}} 。
    </div>
</div>
<%--接下来我们定义它的table样式。--%>
<div class="col-xs-6">
    <div class="dataTables_paginate paging_simple_numbers" id="dynamic-table_paginate">
        <ul class="pagination">
            <li class="paginate_button previous {{^firstUrl}}disabled{{/firstUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="1" data-url="{{firstUrl}}" class="page-action">首页</a>
            </li>
            <li class="paginate_button {{^beforeUrl}}disabled{{/beforeUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{beforePageNo}}" data-url="{{beforeUrl}}" class="page-action">前一页</a>
            </li>
            <li class="paginate_button active" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-id="{{pageNo}}" >第{{pageNo}}页</a>
                <input type="hidden" class="pageNo" value="{{pageNo}}" />
            </li>
            <li class="paginate_button {{^nextUrl}}disabled{{/nextUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{nextPageNo}}" data-url="{{nextUrl}}" class="page-action">后一页</a>
            </li>
            <li class="paginate_button next {{^lastUrl}}disabled{{/lastUrl}}" aria-controls="dynamic-table" tabindex="0">
                <a href="#" data-target="{{maxPageNo}}" data-url="{{lastUrl}}" class="page-action">尾页</a>
            </li>
        </ul>
    </div>
</div>
</script>

<%--定义好模板之后，我们开始对模板使用JS脚本检查页面的渲染。--%>
<script type="text/javascript">
    /*我们先把模板获取出来，交给Mustache引擎解析一下。*/
    var paginateTemplate = $("#paginateTemplate").html();
    Mustache.parse(paginateTemplate);
    /*然后我们定义一个用于分页的函数。
    * url            我们请求的链接
    * total          当前满足要求的总行数
    * pageNo         当前可以展示多少页
    * pageSize       每一页允许展示的行数，即每页显示多少条数据。
    * currentSize    当前这页返回的结果共有多少条
    * idElement      给出的值需要放到页面中的哪些元素中去。
    * callback       元素定义好后，我们进行回调的函数。 */
    function renderPage(url, total, pageNo, pageSize, currentSize, idElement, callback) {
        var maxPageNo = Math.ceil(total / pageSize);  // 计算出当前最大的页数（使用Math调用ceil函数，向上取整）。
        var paramStartChar = url.indexOf("?") > 0 ? "&" : "?";  // 从哪里开始。开始位置的参数的值。如果url以问号？结尾的，那么代表url已经有参数了，那么url末尾我们就使用&符合拼接。
        var from = (pageNo - 1) * pageSize + 1;  // 定义一个from变量，代表从多少条到多少条。【（当前第几页-1）*每页数据条数 + 1 】
        var view = {
            from: from > total ? total : from,
            to: (from + currentSize - 1) > total ? total : (from + currentSize - 1),
            total : total,
            pageNo : pageNo,
            maxPageNo : maxPageNo,
            nextPageNo: pageNo >= maxPageNo ? maxPageNo : (pageNo + 1),
            beforePageNo : pageNo == 1 ? 1 : (pageNo - 1),
            firstUrl : (pageNo == 1) ? '' : (url + paramStartChar + "pageNo=1&pageSize=" + pageSize),
            beforeUrl: (pageNo == 1) ? '' : (url + paramStartChar + "pageNo=" + (pageNo - 1) + "&pageSize=" + pageSize),
            nextUrl : (pageNo >= maxPageNo) ? '' : (url + paramStartChar + "pageNo=" + (pageNo + 1) + "&pageSize=" + pageSize),
            lastUrl : (pageNo >= maxPageNo) ? '' : (url + paramStartChar + "pageNo=" + maxPageNo + "&pageSize=" + pageSize)
        };
        $("#" + idElement).html(Mustache.render(paginateTemplate, view));

        $(".page-action").click(function(e) {
            e.preventDefault();
            $("#" + idElement + " .pageNo").val($(this).attr("data-target"));
            var targetUrl  = $(this).attr("data-url");
            if(targetUrl != '') {
                $.ajax({
                    url : targetUrl,
                    success: function (result) {
                        if (callback) {
                            callback(result, url);
                        }
                    }
                })
            }
        })
    }
</script>
