layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    //引入formSelects模块
        formSelects = layui.formSelects;


    //添加|更新用户
    form.on("submit(addOrUpdateUser)",function (data){
        //弹出loading层
        var index = top.layer.msg("数据提交中...",{
            icon:7,time:false,shade:0.9
        });
        //判断添加还是修改
        var url = ctx+"/user/save";
        if ($("input[name=id]").val()){
            url = ctx+"/user/update";
        }
        //发送ajax
        $.post(url,data.field,function (result){
            if (result.code==200){
                setTimeout(function (){
                    //关闭loading弹窗
                    top.layer.close(index);
                    top.layer.msg("操作成功");
                    //关闭所有iframe层
                    layer.closeAll("iframe");
                    //刷新
                    parent.location.reload();
                },300);
            }else {
                layer.msg(result.msg);
            }
        },"json")
        //取消默认跳转
        return false;
    })

    //关闭弹出层
    $("#closeBtn").click(function (){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    //加载下拉框数据
    var userId = $("input[name='id']").val();
    formSelects.config("selectId",{
        type:"post",
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId,
        keyName: "roleName",
        keyVal: "id"
    },true);

});