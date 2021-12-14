layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url: ctx + '/user/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "userListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'userName', title: '用户名', minWidth: 50, align: "center"},
            {field: 'email', title: '用户邮箱', minWidth: 100, align: 'center'},
            {field: 'phone', title: '用户电话', minWidth: 100, align: 'center'},
            {field: 'trueName', title: '真实姓名', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', minWidth: 150, templet: '#userListBar', fixed: "right", align: "center"}
        ]]
    });

    //绑定搜索时间  重载
    $(".search_btn").click(function (){
        tableIns.reload({
          where: { //设定异步数据接口的额外参数，任意设
            userName: $("input[name=userName]").val()
            ,email: $("input[name=email]").val()
            ,phone: $("input[name=phone]").val()
          }
          ,page: {
            curr: 1 //重新从第 1 页开始
          }
        });
    });

    //头工具栏事件
    //触发事件
    table.on('toolbar(users)', function(obj){
      var checkStatus = table.checkStatus(obj.config.id);
      switch(obj.event){
        case 'add':
          //layer.msg('添加');
            openAddAndUpdateDialog();
        break;
        case 'del':
          //layer.msg('删除');
            deleteUserDialog(checkStatus.data);
        break;
      }
    });

    //删除用户
    function deleteUserDialog(datas){
        //判断是否选择数据
        if (datas.length==0){
            layer.msg("请选择要删除的记录",{icon:5});
            return;
        }
        //询问框
        layer.confirm("确定要删除选中的数据吗",{
            btn: ["确认","取消"]
        },function (index){
            //关闭弹出层
            layer.close(index);
            var ids = "ids=";
            for (var i = 0; i < datas.length; i++) {
                if (i<datas.length-1){
                    ids = ids+datas[i].id+"&ids=";
                }else {
                    ids = ids+datas[i].id
                }
            }
            //发送ajax
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:ids,
                dataType:"json",
                success:function (data){
                    if (data.code==200){
                        layer.msg("删除成功");
                        //重载
                        tableIns.reload();
                    }else {
                        layer.msg(data.msg);
                    }
                }
            });
        });
    }

    //添加|修改用户
    function openAddAndUpdateDialog(userId){
        //定义url和title
        var url = ctx+"/user/toAddOrUpdatePage"
        var title = "<h2>用户模块--添加</h2>"
        if (userId!=null){
            url = url+"?id="+userId
            title = "<h2>用户模块--修改</h2>"
        }
        layui.layer.open({
            title: title,
            type: 2,
            content: url,
            maximum: true,
            area: ["650px","400px"]
        });

    }


    //工具条事件
    table.on('tool(users)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
      var layEvent = obj.event; //获得当前行数据
        if(layEvent === 'del'){ //删除
            layer.confirm('真的删除行么', {
                btn:["确认","取消"]
            },function(index){
                //发送ajax
                $.ajax({
                    type:"post",
                    url:ctx+"/user/delete",
                    data: {ids:obj.data.id},
                    success:function (data){
                        if (data.code==200){
                            layer.msg("删除成功");
                            //重载
                            tableIns.reload();
                        }else {
                            layer.msg(data.msg);
                        }
                    }
                });
              //layer.msg("删除")
              //关闭窗口
              layer.close(index);
        });
        } else if(layEvent === 'edit'){ //编辑
            openAddAndUpdateDialog(obj.data.id);
            //layer.msg("修改");
      }
    });

});

