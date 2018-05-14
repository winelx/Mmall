<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html >
<html>
<head>
    <title></title>

    <meta charset="utf-8">
    <script src="http://unpkg.com/vue/dist/vue.js"></script>
    <script src="http://files.cnblogs.com/files/zycbloger/vue-resource.min.js"></script>
    <script type="text/javascript">
        window.onload = function () {
            var vm = new Vue({
                el: '#box',
                data: {
                    msg: 'Hello World!',
                },
                methods: {
                    get: function () {
                        //发送get请求
                        this.$http.get('get.do', {a: 1, b: 2}).then(function (res) {
                            alert(res.body);
                        }, function () {
                            alert('请求失败处理'); //失败处理
                        });
                    },

                    post: function () {
                        //发送post请求
                        this.$http.post('http://localhost:8080/user/check_valid.do', {
                            str: 1,
                            type: 2
                        }).then(function (res) {
                            alert(res.body);
                        }, function () {
                            alert('请求失败处理'); //失败处理
                        });
                    }
                }
            });
        }
    </script>
</head>
<body>
<div id="box">
    <input type="button" @click="get()" value="按钮get">
    <input type="button" @click="post()" value="按钮post">

    <form name="form1" action="manage/product/upload.do" method="post"
          enctype="multipart/form-data"
    >
        <input type="file" name="upload_file"/>
        <input type="submit" value="springmvc上传文件"/>

    </form>
    <form name="form1" action="manage/product/richtext_img_upload .do" method="post"
          enctype="multipart/form-data"
    >
        <input type="file" name="upload_file"/>
        <input type="submit" value="springmvc上传文件"/>

    </form>
    "
</div>
</body>
</html>